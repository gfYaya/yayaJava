package cn.netty.exmaple.http.snoop;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpSnoopClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("client handler channelRead0");
        if(msg instanceof HttpResponse){
            HttpResponse response = (HttpResponse)msg;
            System.err.println("STATUS:"+response.status());
            System.err.println("VERTION:"+response.protocolVersion());
            System.err.println();

            if (!response.headers().isEmpty()){
                for(CharSequence name: response.headers().names()){
                    for(CharSequence value: response.headers().getAll(name)){
                        System.err.println("HEADER: " + name + " = " + value );
                    }
                }
                System.out.println();
            }

            //Checks to see if the transfer encoding in a specified {@link HttpMessage} is chunked
            /*
              有时候服务生成HTTP回应是无法确定消息大小的，比如大文件的下载，或者后台需要复杂的逻辑才能全部处理页面的请求，
            这时用需要实时生成消息长度，服务器一般使用chunked编码。注意chunk数据长度的单位是字节，不包括后面的\r\n。以一个长度为0的块作为结尾。
            把所有的chunk数据部分组合起来存入文件，就是一个标准的gzip压缩文件。 -- https://www.jianshu.com/p/c09efa423c2d
             */
            if(HttpUtil.isTransferEncodingChunked(response)){
                System.err.println("CHUNKED content {");
            }else{
                System.err.println("content {");
            }
        }

        if (msg instanceof HttpContent){
            HttpContent content = (HttpContent) msg;
            System.err.print(content.content().toString(CharsetUtil.UTF_8));
            System.err.flush();

            //LastHttpContent : The last {@link HttpContent} which has trailing headers.
            //question: 如果没有使用chunked.他怎么知道是最后一个 http content? http没有使用chunked 而是自动对响应体进行分割?
            //徐靖峰 answer: 没有使用chunked的话，不就是依赖于 content-length 来确定报文长度吗?
            /* answer: http发送数据的时候在头文件里会声明数据长度分隔符.接收端根据分隔符来分隔接收数据，根据接收的长度来判断文件有没传输完成
                  TCP传输数据每个块是有大小限制的,所以要分段传输.即一个HTTP请求可能是多个TCP数据包组成.其实 http 传输数据的时候不用管TCP包
                  的问题，头文件里声明的分隔符主要是业务数据区分，而不是数据包的分割
             */
            if(content instanceof LastHttpContent){
                System.err.println("} END OF CONTENT");
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client handler exceptionCaught");
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
