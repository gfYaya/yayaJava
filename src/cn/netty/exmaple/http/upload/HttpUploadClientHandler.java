package cn.netty.exmaple.http.upload;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.jboss.netty.handler.codec.http.HttpChunk;

/**
 * Handler that just dumps the contents of the response from the server
 */
public class HttpUploadClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private boolean readingChunks;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("upload client channelRead0");
        if(msg instanceof HttpResponse){ //Is this not chunked as we used the HttpResposne?whether need the content-length?
            HttpResponse response = (HttpResponse) msg;
            System.err.println("STATUS: " + response.status());
            System.err.println("VERSION: " + response.protocolVersion());

            if(!response.headers().isEmpty()){
                for(CharSequence name : response.headers().names()){
                    for(CharSequence value : response.headers().getAll(name)){
                        System.err.println("HEADER: " + name + " = " + value);
                    }
                }
            }

            if(response.status().code() == 200 && HttpUtil.isTransferEncodingChunked(response)){
                readingChunks = true;
                System.err.println("CHUNKED CONTENT {");
            }else{
                System.err.println("CONTENT {");
            }
        }

        if(msg instanceof HttpContent){ // chunk ?If msg instanceof HttpContent.
            HttpContent chunk = (HttpContent)msg;
            System.err.println(chunk.content().toString(CharsetUtil.UTF_8));

            if(msg instanceof LastHttpContent){
                if(readingChunks){
                    System.err.println("} END OF CHUNKED CONTENT");
                }else{
                    System.err.println("} END OF CONTENT");
                }
                readingChunks = false;
            }else{
                System.err.println(chunk.content().toString(CharsetUtil.UTF_8));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("upload client exceptionCaught");
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.channel().close();
    }
}
