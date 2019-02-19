package cn.netty.exmaple.http.snoop;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;

public class HttpSnoopClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public HttpSnoopClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("client initializer initChannel");
        ChannelPipeline p = ch.pipeline();

        //Ennable HTTPS  if nessary
        if(sslCtx != null){
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpClientCodec());

        //remove the following line if you don't want atomic content decompression
        p.addLast(new HttpContentDecompressor());

        //Uncomment the following line if you don't want to hanlde HttpContents.
        //p.addLast(new HttpObjectAggregator(1048576));

        p.addLast(new HttpSnoopClientHandler());
    }

}
