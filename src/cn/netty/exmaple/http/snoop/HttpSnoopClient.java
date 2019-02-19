package cn.netty.exmaple.http.snoop;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpSnoopServer}.
 */
public final class HttpSnoopClient {

    static final String  URL = System.getProperty("url", "http://127.0.0.1/8080");

    public static void main(String[] args) throws Exception {
        //Represents a Uniform Resource Identifier (URI) reference.
        URI uri  = new URI(URL);
    }
}
