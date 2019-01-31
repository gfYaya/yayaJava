package cn.netty.exmaple.http.file;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

/*   public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter .
 * A special {@link ChannelInboundHandler} which offers an easy way to initialize a {@link Channel} once it was
 * registered to its {@link EventLoop}.
 */
public class HttpStaticFileServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslContext;

    public HttpStaticFileServerInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if(sslContext != null){
            pipeline.addLast(sslContext.newHandler(ch.alloc()));
        }
        /**
         * A combination of {@link HttpRequestDecoder} and {@link HttpResponseEncoder}
         * which enables easier server side HTTP implementation.
         */
        pipeline.addLast(new HttpServerCodec());
        //content
        pipeline.addLast(new HttpObjectAggregator(65536));
        //writing a large data stream asynchronously neither spending a lot of memory nor getting {@link OutOfMemoryError}.
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpStaticFileServerHandler());
    }
}
