package cn.netty.exmaple.factorial;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.ssl.SslContext;

public class FactorialClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslContext;

    public FactorialClientInitializer(SslContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if(sslContext != null) {
            // newHandler return type is SslHandler,
            // class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler
            pipeline.addLast(sslContext.newHandler(ch.alloc(), FactorialClient.HOST, FactorialClient.PORT));
        }  //todo  因为这个花括号 放在了最后 导致Client的 ,System.err.format("Factorial of %,d is: %,d", COUNT, handler.getFactorial()); 报空指针问题

            //enable stream compression (you can remove these two if unnecessary)
            pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
            pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

            //Add the number codec first.
            pipeline.addLast(new BigIntegerDecoder());
            pipeline.addLast(new NumberEncoder());

            // and then business logic
            pipeline.addLast(new FactorialClientHandler());
    }
}
