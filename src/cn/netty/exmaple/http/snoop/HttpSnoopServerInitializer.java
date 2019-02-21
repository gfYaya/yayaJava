package cn.netty.exmaple.http.snoop;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslContext;

    public HttpSnoopServerInitializer(SslContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if(sslContext != null){
            p.addLast(sslContext.newHandler(ch.alloc()));
        }
        p.addLast(new HttpRequestDecoder());
        //Uncomment the following line if you don't want to handle HttpChunks.
        //p.addLast(new HttpObjectAggregator(1048576));
        p.addLast(new HttpResponseEncoder());
        //Remove the following line if you don't want automatic content compression.
        /* 按照已给的netty demo,在HttpSnoopClientInitializer ,是使用了HttpContentDecompressor(),这个地方demo竟然给的是注释掉
         * HttpContentCompressor,有点匪夷所思,不应该要么都注释,要么都不注释,为何注释HttpContentCompressor 而不注释HttpContentDecompressor
         */
        //p.addLast(new HttpContentCompressor());
        p.addLast(new HttpSnoopServerHandler());
    }
}
