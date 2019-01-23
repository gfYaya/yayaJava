package cn.netty.exmaple.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf firstMessage;

    public EchoClientHandler(){
        System.out.println("EchoClientHandler");
        //Creates a new big-endian Java heap buffer with the specified {@code capacity}
        firstMessage = Unpooled.buffer(EchoClient.SIZE);
        for(int i=0; i< firstMessage.capacity(); i++){
            //System.out.println("EchoClientHandler, i:"+i);
            //Sets the specified byte at the current {@code writerIndex} and increases the {@code writerIndex} by {@code 1} in this buffer.
            firstMessage.writeByte((byte)i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        System.out.println("client channel active");
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //super.channelInactive(ctx);
        System.out.println("client channel inactive");
    }

    //使用没有执行!!!
    /*  徐靖峰:channelReadComplete()是底层socket.Read()之后调用的。
    比如1000个字节的包分为了10个100字节的分包，底层socket每次read100字节就会调用channelReadComplete，而当读完10个分包组装成1000字节后才会调用channelRead()来处理。
     */
    /* 个人猜测: channelRead 当从Channel 读取数据时被调用 , channelReadComplete 当Channel上的一个读操作完成时被调用.
        由于报文是空的,等同于没有报文信息,所以无法触发 channelRead(), 也是报文就因为是空的,所以报文拿到的时候就直接完成了,所以触发了 channelReadComplete().
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        System.out.println("client channel read");
        //Request to write a message via this {@link ChannelHandlerContext} through the {@link ChannelPipeline}.
        ctx.write(firstMessage);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        System.out.println("client channel read complete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        System.out.println("client exception");
        cause.printStackTrace();
        ctx.close();
    }
}
