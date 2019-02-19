package cn.netty.exmaple.http.snoop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public class HttpSnoopClient {
    static final String URL = System.getProperty("url","http://127.0.0.1:8080/");

    public static void main(String[] args) throws Exception{
        URI uri = new URI(URL);
        String schema = uri.getScheme() == null? "http" : uri.getScheme();
        System.out.println("client schema:"+schema);
        String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
        System.out.println("client host:"+host);
        int port = uri.getPort();
        if(port == -1){
            if("http".equalsIgnoreCase(schema)){
                port = 80;
            }else if("https".equalsIgnoreCase(schema)){
                port = 443;
            }
        }

        if(!"http".equalsIgnoreCase(schema) && !"https".equalsIgnoreCase(schema)){
            System.err.println("Only http(s) is supported");
            return;
        }

        //Configure SSL context if necessary
        final boolean ssl = "https".equalsIgnoreCase(schema);
        final SslContext sslContext;
        if(ssl){
            sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }else{
            sslContext = null;
        }

        //Configure the client
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // NioSocketChannel : {@link io.netty.channel.socket.SocketChannel} which uses NIO selector based implementation.
                    .channel(NioSocketChannel.class) //the method channel() is used to bind the Selector which you need to choose?
                    .handler(new HttpSnoopClientInitializer(sslContext));

            //Make the connection attempt
            Channel ch = b.connect(host, port).sync().channel();

            //Prepare the http request
            HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            request.headers().set(HttpHeaderNames.HOST, host);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

            //Set some sample cookies
            request.headers().set(HttpHeaderNames.COOKIE, io.netty.handler.codec.http.cookie.ClientCookieEncoder.STRICT.encode(
                    new io.netty.handler.codec.http.cookie.DefaultCookie("my-cookie", "foo"),
                    new DefaultCookie("another-cookie", "bar")));

            //send the http request
            ch.writeAndFlush(request);

            //wait for the server to close the connection
            ch.closeFuture().sync();
        }finally{
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
    }
}
