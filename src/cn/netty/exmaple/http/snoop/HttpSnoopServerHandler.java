package cn.netty.exmaple.http.snoop;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<SocketChannel> {
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
    protected void channelRead0(ChannelHandlerContext ctx, SocketChannel msg) throws Exception {
        System.out.println("server handler channelRead0");
        if(msg instanceof HttpRequest){
            HttpRequest request = this.request = (HttpRequest)msg;
        }

        if(HttpUtil.is100ContinueExpected(request)){
            send100Continue(ctx);
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

            //todo
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

}
