package cn.netty.exmaple.http.cors;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Please refer to the {@link CorsConfig} javadocs for information about all the
 * configuration options available.
 *
 * Below are some of configuration discussed in this example:
 * <h3>Support only a specific origin</h3>
 * To support a single origin instead of the wildcard use the following:
 * <pre>
 * CorsConfig corsConfig = CorsConfig.withOrigin("http://domain1.com")
 * </pre>
 *
 * <h3>Enable loading from the file system</h3>
 * To enable the server to handle an origin specified as 'null', which happens
 * when a web browser loads a file from the local file system use the following:
 * <pre>
 * corsConfig.isNullOriginAllowed()
 * </pre>
 *
 * <h3>Enable request headers</h3>
 * To enable additional request headers:
 * <pre>
 * corsConfig.allowedRequestHeaders("custom-request-header")
 * </pre>
 *
 * <h3>Expose response headers</h3>
 * By default a browser only exposes the following simple header:
 * <ul>
 * <li>Cache-Control</li>
 * <li>Content-Language</li>
 * <li>Content-Type</li>
 * <li>Expires</li>
 * <li>Last-Modified</li>
 * <li>Pragma</li>
 * </ul>
 * Any of the above response headers can be retrieved by:
 * <pre>
 * xhr.getResponseHeader("Content-Type");
 * </pre>
 * If you need to get access to other headers this must be enabled by the server, for example:
 * <pre>
 * corsConfig.exposedHeaders("custom-response-header");
 * </pre>
 */
public class HttpCorsServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslContext;

    public HttpCorsServerInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
        ChannelPipeline pipeline = ch.pipeline();
        if(sslContext != null){
            pipeline.addLast(sslContext.newHandler(ch.alloc()));
        }
        //Encodes an {@link HttpResponse} or an {@link HttpContent} into a {@link ByteBuf}.
        pipeline.addLast(new HttpResponseEncoder());
        //Decodes {@link ByteBuf}s into {@link HttpRequest}s and {@link HttpContent}s.
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpObjectAggregator(65536));
        /* class ChunkedWriteHandler:  A {@link ChannelHandler} that adds support for writing a large data stream
        * asynchronously neither spending a lot of memory nor getting
        * {@link OutOfMemoryError}.  Large data streaming such as file
        * transfer requires complicated state management in a {@link ChannelHandler}
        * implementation.  {@link ChunkedWriteHandler} manages such complicated states
        * so that you can send a large data stream without difficulties.
        * */
        //https://en.wikipedia.org/wiki/Chunked_transfer_encoding
        //Chunked transfer encoding allows a server to maintain an HTTP persistent connection for dynamically generated content
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new CorsHandler(corsConfig));
        pipeline.addLast(new OkResponseHandler());
    }

}
