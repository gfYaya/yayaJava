package cn.netty.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/*
 * P17 并根据netty 的github项目进行自改,进行完整测试,补全部分demo 参考netty github的example
 */
public class EchoServer {
    private final int port;
    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            int port = 8007;
            new EchoServer(port).start();
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class) //指定所使用的NIO传输Channel
                    .localAddress(new InetSocketAddress(port)) //使用指定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>(){//添加一个EchoServerHandler 到子Channel的ChannelPipeline  =>http://ifeve.com/channel-pipeline/
                        //当一个新的连接被接受时,一个新的子channel会被创建,而ChannelInitializer将会把你的一个EchoServerHandler的实例将添加到该Channel的ChannelPipeline中
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //Inserts {@link ChannelHandler}s at the last position of this pipeline.
                            ch.pipeline().addLast(serverHandler); //EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
                        }
                    });
            ChannelFuture f = b.bind().sync(); //异步地绑定服务器;调用sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync(); //获取Channel 的CloseFuture，并且阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync(); //关闭EventLoopGroup,释放所有的资源
        }
    }
}
