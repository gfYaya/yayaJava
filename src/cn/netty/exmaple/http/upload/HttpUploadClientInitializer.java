package cn.netty.exmaple.http.upload;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpUploadClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public HttpUploadClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("upload client initChannel");
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc()));
        }
        /** Creates a new instance with the default decoder options
         * ({@code maxInitialLineLength (4096}}, {@code maxHeaderSize (8192)}, and
         * {@code maxChunkSize (8192)}).
         */
        pipeline.addLast("codec", new HttpClientCodec());

        //remove the following line if you don't want automic content decompress.
        pipeline.addLast("inflator", new HttpContentDecompressor());

        //to be used since huge file transfere
        pipeline.addLast("chunkedWriter",new ChunkedWriteHandler());

        pipeline.addLast("handler", new HttpUploadClientHandler());
    }

}
