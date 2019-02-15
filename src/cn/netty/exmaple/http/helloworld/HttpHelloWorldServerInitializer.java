package cn.netty.exmaple.http.helloworld;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpHelloWorldServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if(sslCtx != null){
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        //A combination of {@link HttpRequestDecoder} and {@link HttpResponseEncoder} which enables easier server side HTTP implementation.
        pipeline.addLast(new HttpServerCodec());
        /* Sends a <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec8.html#sec8.2.3">100 CONTINUE</a>
         * {@link HttpResponse} to {@link HttpRequest}s which contain a 'expect: 100-continue' header. It
         * should only be used for applications which do <b>not</b> install the {@link HttpObjectAggregator}.
         * */
        // 100-continue? 一次post请求中的第一个请求?  //https://zhuanlan.zhihu.com/p/25028045
        //it should be added after {@link HttpServerCodec} but before any other handlers that might send a {@link HttpResponse}.
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new HttpHelloWorldServerHandler());
    }

}
