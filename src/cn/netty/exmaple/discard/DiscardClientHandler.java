package cn.netty.exmaple.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

//Handles a client-side channel.
// SimpleChannelInboundHandler<Object>  SimpleChannelInboundHandler<Object> => which allows to explicit only handle a specific type of messages.
public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {
    private ByteBuf content;
    private ChannelHandlerContext ctx;

    //当从Channel 读取数据时被调用
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // Server is supposed to send nothing, but if it sends something, discard it.
        System.out.println("client channel read0");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client channel active");
        this.ctx = ctx;
        //initialize the message    //directBuffer 直接内存
        content = ctx.alloc().directBuffer(DiscardClient.SIZE).writeZero(DiscardClient.SIZE);
        //send the initial message
        genericTraffic();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client channel inactive");
        content.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client channel exception");
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        /* 运行时间长了 即便是关掉IDE 也会导致内存被长时间占用没有被回收,显然是netty的直接内存在每次关闭程序的时候,没有进行回收,
         *  导致后续启动几次都会报出OOM异常,尝试自己在抛出OOM异常,手动清空ByteBuf
         *  ! 场景再也没有复现出来
         */
        ReferenceCountUtil.release(content); //手动释放堆外内存
        ctx.close();
    }

    private void genericTraffic(){
        // Flush the outbound buffer to the socket. Once flushed, generate the same amount of traffic again.
        ctx.writeAndFlush(content.retainedDuplicate()).addListener(trafficGenerator);
    }

    //每个出站操作都将返回一个ChannelFuture。注册到ChannelFuture 的ChannelFutureListener 将在操作完成时被通知该操作是成功了还是出错了。
    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if(channelFuture.isSuccess()){
                genericTraffic();
            }else{
                channelFuture.cause().printStackTrace();
                channelFuture.channel().close();
            }
        }
    };
}
