package cn.netty.exmaple.http.upload;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.zookeeper.KeeperException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * This class is meant to be run against {@link HttpUploadServer}.
 */
public class HttpUploadClient {

    static final String BASE_URL = System.getProperty("baseUrl", "http://127.0.0.1:8080/");
    static final String FILE = System.getProperty("file", "upload.txt");

    public static void main(String[] args) throws Exception{
        String postSimple, postFile, get;
        if(BASE_URL.endsWith("/")){
            postSimple = BASE_URL + "formpost";
            postFile = BASE_URL + "formpostmultipart";
            get = BASE_URL + "formget";
        }else{
            postSimple = BASE_URL + "/formpost";
            postFile = BASE_URL + "/formpostmultipart";
            get = BASE_URL + "/formget";
        }

        URI uriSimple = new URI(postSimple);
        String scheme = uriSimple.getScheme() == null? "http" : uriSimple.getScheme();
        String host = uriSimple.getHost() == null? "127.0.0.1" : uriSimple.getHost();

        int port = uriSimple.getPort();
        if(port == -1){
            if("http".equalsIgnoreCase(scheme)){
                port = 80;
            }else if("https".equalsIgnoreCase(scheme)){
                port = 443;
            }
        }
        if(!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)){
            System.err.println("Only HTTP(S) is supported.");
            return;
        }

        final boolean ssl = "https".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        URI uriFile = new URI(postFile);
        File file = new File(FILE);
        if(!file.canRead()){
            throw new FileNotFoundException(FILE);
        }

        //Configure the client
        EventLoopGroup group = new NioEventLoopGroup();

        //set up the factory:here using a mixed memory/disk base on size threshold.
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);//Disk if minisize exceed

        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file on exit (in normal exit)
        DiskFileUpload.baseDirectory = null; //System temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on exit (in normal exit)
        DiskAttribute.baseDirectory = null; // system temp directory

        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new HttpUploadClientInitializer(sslCtx));

            //Simple get form: no factory used(not usable)
            List<Map.Entry<String, String>> bodyList = formget(b, host, port, get, uriSimple);
        }finally{

        }
    }

    /**
     * Standard usage of HTTP API in Netty without file Upload (get is not able to achieve File upload
     * due to limitation on request size).
     *
     * @return the list of headers that will be used in every example after
     **/
    private static List<Map.Entry<String, String>> formget(Bootstrap bootstrap, String host, int port, String get,
                                                           URI uriSimple) throws Exception {
        // XXX/formget
        // No use of HttpPostRequestEncoder since not a POST.
        Channel channel = bootstrap.connect(host, port).sync().channel();

        //Prepare the Http Request
        QueryStringEncoder encoder = new QueryStringEncoder(get);
        //add form attribute
        encoder.addParam("getform", "GET");
        encoder.addParam("info", "first value");
        // ?? ��� what's this? We need to send this message, why?
        encoder.addParam("secondinfo", "secondvalue ���&");
        //not the big one since it is not compitable with GET size.
        // encoder.addParam("thirdinfo", textArea);
        encoder.addParam("thirdinfo", "third value\r\ntest second line\r\n\r\nnew line\r\n");
        encoder.addParam("Send", "Send");

        URI uriGet = new URI(encoder.toString());
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uriGet.toASCIIString());
        HttpHeaders headers = request.headers();
        headers.set(HttpHeaderNames.HOST, host);
        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        headers.set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP + "," + HttpHeaderValues.DEFLATE);

        headers.set(HttpHeaderNames.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        headers.set(HttpHeaderNames.ACCEPT_LANGUAGE, "fr");
        headers.set(HttpHeaderNames.REFERER, uriSimple.toString());
        headers.set(HttpHeaderNames.USER_AGENT, "Netty Simple Http Client side");
        headers.set(HttpHeaderNames.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        //connection will not close but needed
        //heaaders.set("Connection", "keep-alive");
        //headers.set("Keep-Alive", "300");

        //encode : Encodes the specified cookies into a single Cookie header value.
        headers.set(HttpHeaderNames.COOKIE, ClientCookieEncoder.STRICT.encode(


        ));

        //todo
        return null;
    }
}
