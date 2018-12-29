package cn.netty.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/*
 * P17 并根据netty 的github项目进行自改,进行完整测试,补全部分demo 参考netty github的example
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    //private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
        /*firstMessage = Unpooled.buffer(EchoClient.SIZE);
        for (int i = 0; i < firstMessage.capacity(); i ++) {
            firstMessage.writeByte((byte) i);
        }*/
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //ctx.writeAndFlush(firstMessage);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",
                CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
