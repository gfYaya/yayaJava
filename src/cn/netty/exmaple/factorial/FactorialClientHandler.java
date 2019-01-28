package cn.netty.exmaple.factorial;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handler for a client-side channel.  This handler maintains stateful
 * information which is specific to a certain channel using member variables.
 * Therefore, an instance of this handler can cover only one channel.  You have
 * to create a new handler instance whenever you create a new channel and insert
 * this handler to avoid a race condition.
 */
public class FactorialClientHandler extends SimpleChannelInboundHandler<BigInteger> {
    private ChannelHandlerContext ctx;
    private int recievedMessage;
    //private int next;
    private int next = 1;
    final BlockingQueue<BigInteger> answer = new LinkedBlockingQueue();

    public BigInteger getFactorial(){
        boolean interrupted = false;
        try{
            for(;;){
                try {
                    //Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
                    return answer.take();
                }catch(InterruptedException e){
                    interrupted = true;
                }
            }
        }finally {
            if (interrupted){
                Thread.currentThread().interrupt();
            }
        }
    }

    //Is called for each message of type {@link I}.
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
        System.out.println("factorial client handler read0");
        recievedMessage++;
        if(recievedMessage == FactorialClient.COUNT){
            //Offer the answer after closing the connection.
            ctx.channel().close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    boolean offered = answer.offer(msg);
                    assert offered;
                }
            });
        }
    }

    //当一个新的连接已经被建立时，channelActive(ChannelHandlerContext)将会被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("factorial client handler active");
        //super.channelActive(ctx);
        this.ctx = ctx;
        sendNumbers();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("factorial client handler exception");
        super.exceptionCaught(ctx, cause);
    }

    private void sendNumbers(){
        //don't send more than 4096 numbers
        //  class ChannelFuture extends Future<Void> ,the generic type is Void !
        ChannelFuture future = null;
        for(int i = 1; i<4096 && next<=FactorialClient.COUNT; i++){
            future = ctx.write(Integer.valueOf(next));
            next++;
        }
        if(next<=FactorialClient.COUNT){
            assert future != null;
            future.addListener(numberSender);
        }
        ctx.flush();
    }

    private final ChannelFutureListener numberSender = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(future.isSuccess()){
                sendNumbers();
            }else{
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };
}
