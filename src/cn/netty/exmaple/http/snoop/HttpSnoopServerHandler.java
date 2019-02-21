package cn.netty.exmaple.http.snoop;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/*为何使用Object?而不是使用SocketChannel?使用Object没问题,但是使用SocketChannel 浏览器请求 http://127.0.0.1:8080/ 会一直处于请求中,无法得到相应
  public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<SocketChannel> {
    是因为 if(msg instanceof HttpRequest){  之后没用else情况 进行return ?毕竟SocketChannel 和HttpRequest无继承关系
 */
public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;

    /** Buffer that restores the response content */
    private final StringBuilder buf = new StringBuilder();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server handler channelReadComplete");
        //super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server handler exceptionCaught");
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server handler channelRead0");
        if(msg instanceof HttpRequest){
            HttpRequest request = this.request = (HttpRequest)msg;

            if(HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            buf.setLength(0);
            buf.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
            buf.append("===================================\r\n");

            buf.append("VERSION: ").append(request.protocolVersion()).append("\r\n");
            buf.append("HOSTNAME: ").append(request.headers().get(HttpHeaderNames.HOST, "unknown")).append("\r\n");
            buf.append("REQUEST_URI: ").append(request.uri()).append("\r\n\r\n");

            HttpHeaders headers = request.headers();
            if(!headers.isEmpty()){
                for(Map.Entry<String, String> h:headers){
                    CharSequence key = h.getKey();
                    CharSequence value = h.getValue();
                    buf.append("HEADER: ").append(key).append(" = ").append(value).append("\r\n");
                }
                buf.append("\r\n");
            }

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> params = queryStringDecoder.parameters();
            if(!params.isEmpty()){
                for(Map.Entry<String, List<String>> p : params.entrySet()){
                    String key = p.getKey();
                    List<String> vals = p.getValue();
                    for(String val : vals){
                        buf.append("PARAM: ").append(key).append(" = ").append(val).append("\r\n");
                    }
                }
                buf.append("\r\n");
            }

            appendDecoderResult(buf, request);
        }

        if(msg instanceof HttpContent){
            HttpContent httpContent = (HttpContent)msg;

            ByteBuf content = httpContent.content();
            if(content.isReadable()){
                buf.append("CONTENT: ");
                buf.append(content.toString(CharsetUtil.UTF_8));
                buf.append("\r\n");
                appendDecoderResult(buf, request);
            }

            if(msg instanceof LastHttpContent){
                buf.append("END OF CONTENT\r\n");

                LastHttpContent trailer = (LastHttpContent)msg;
                //https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Trailer
                if(!trailer.trailingHeaders().isEmpty()){
                    buf.append("\r\n");
                    for(CharSequence name : trailer.trailingHeaders().names()){
                        for(CharSequence value : trailer.trailingHeaders().getAll(name)){
                            buf.append("TRAILING HEADER: ");
                            buf.append(name).append(" = ").append(value).append("\r\n");
                        }
                    }
                    buf.append("\r\n");
                }

                if(!writeResponse(trailer, ctx)){
                    //If is keep-alive is off, close the connection once the content is fully written.
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    private static void appendDecoderResult(StringBuilder buf, HttpObject o){
        DecoderResult result = o.decoderResult();
        if(result.isSuccess()){
            return;
        }

        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx){
        //decide whether to close the connection or not
        // channelRead0 => HttpRequest request = this.request = (HttpRequest)msg;
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        //Build the response object
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, currentObj.decoderResult().isSuccess()? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        if(keepAlive){
            //Add 'Content-Length' header only for a keep-alive connection
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        //Encode cookie
        String cookieString = request.headers().get(HttpHeaderNames.COOKIE);
        if(cookieString != null){
            //拿取到的是好多cookie?request中只能拿到一个Cookie吧,response 可以有多个Set-Cookie,request也可以有Set-Cookie?
            //decode():Decodes the specified Set-Cookie HTTP header value into a {@link Cookie}.
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieString);
            if(!cookies.isEmpty()){
                //reset the cookie if necessary.
                for(Cookie cookie:cookies){
                    response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
                }
            }
        }else{
            //Browser sent no cookie. Add some.
            response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("key1","value1"));
            response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("key2","value2"));
        }

        //write the response
        ctx.write(response);
        return keepAlive;
    }

}
