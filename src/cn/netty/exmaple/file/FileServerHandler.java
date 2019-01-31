package cn.netty.exmaple.file;

import io.netty.channel.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        RandomAccessFile raf = null;
        long length = -1;
        try{
            //the system-dependent filename
            raf = new RandomAccessFile(msg, "r");
            length = raf.length();
        }catch(Exception e){
            ctx.writeAndFlush("ERR: " + e.getClass().getSimpleName() + ": " + e.getMessage() + '\n');
            return;
        }finally{
            if(length <0 && raf != null){
                raf.close();
            }
        }

        ctx.write("OK: " + raf.length() +'\n');
        //不开启SSL 才能使用零拷贝?  =>零拷贝数据加密或者压缩的文件系统是不可用的——只能传输文件的原始内容
        /* 大宽宽:
            任何要对数据作加工的都不可以,比如你用http，但是却想对数据做个zip压缩,如果你的原始数据不是压缩的，而想在输出到socket时实时压缩，
            就不可以但如果你一开始就压缩好了，存文件系统里，就可以了.
         */
        if(ctx.pipeline().get(SslHandler.class) != null){
            //SSL not enabled - can use zero-copy file transfer
            /** class DefaultFileRegion :
             * Default {@link FileRegion} implementation which transfer data from a {@link FileChannel} or {@link File}.
             * Be aware that the {@link FileChannel} will be automatically closed once {@link #refCnt()} returns {@code 0}.
             */
            ctx.write(new DefaultFileRegion(raf.getChannel(),0, length));
        }else{
            //SSL enabled - cannot use zero-copy file transfer
            // ChunkedFile => Creates a new instance that fetches data from the specified file.
            ctx.write(new ChunkedFile(raf));
        }
        ctx.writeAndFlush('\n');
    }

    //在到服务器的连接已经建立之后将被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("file server handler channel active");
        //super.channelActive(ctx);
        ctx.writeAndFlush("HELLO: Type the path of the file to retrieve.\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("file server handler channel exception");
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            ctx.writeAndFlush("ERR: " + cause.getClass().getSimpleName() + ": " + cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
    }
}
