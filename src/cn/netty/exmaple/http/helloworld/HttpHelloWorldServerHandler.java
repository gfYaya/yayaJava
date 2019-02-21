package cn.netty.exmaple.http.helloworld;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };

    //AsciiString 就是字节数组 ,只不过String 是char数组
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler channelReadComplete");
        ctx.flush();
        //super.channelReadComplete(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("handler channelRead0");
        if(msg instanceof HttpRequest){
            HttpRequest req = (HttpRequest) msg;
            boolean keepAlive = HttpUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

            if(!keepAlive){
                /**
                 * A {@link ChannelFutureListener} that closes the {@link Channel} which is
                 * associated with the specified {@link ChannelFuture}.
                 */
                 /*
                 ChannelFutureListener CLOSE = new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        future.channel().close();
                    }
                };  */
                 //HTTP 请求没有keep-alive ,则添加关闭的事件监听
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            }else{
                /*  为何响应头也要设置keep-alive?
                    徐中 answer: 如果响应报文不设置keep-alive，接收方接收到响应报文之后，TCP不就直接断开了吗?
                    如果响应头里面有keep-alive，就会复用TCP信道，继续下一次http传输.
                 */
                /*  从打印的handler各个生命周期执行的流程来看, response.headers().set(CONNECTION, KEEP_ALIVE);这段
                  代码无论是否注释,执行结果都是一样的,没有变化.
                     但是使用了netstat 进行监听的时候 ,貌似结果也是相同,没有区别
                     应该是rfc规范硬性要求:  https://tools.ietf.org/html/rfc6223#section-7.2
                 */
                /*  https://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Persistent%20Connections
                   The persistent connection ends when either side closes the connection or after the receipt of a response
                   which lacks the "keep-alive" keyword.
                 */
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("handler exceptionCaught");
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
