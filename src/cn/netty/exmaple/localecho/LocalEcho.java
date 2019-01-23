package cn.netty.exmaple.localecho;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LocalEcho {

    static final String PORT = System.getProperty("port", "test_port");

    public static void main(String[] args) throws Exception {
        //Address to bind on / connect on
        final LocalAddress addr = new LocalAddress(PORT);

        //{@link MultithreadEventLoopGroup} which must be used for the local transport.
        EventLoopGroup serverGroup = new DefaultEventLoopGroup();
        //{@link MultithreadEventLoopGroup} implementations which is used for NIO {@link Selector} based {@link Channel}s.
        EventLoopGroup clientGroup = new NioEventLoopGroup();   //Nio event loops are also OK.

        try{
            // Note that we can use any event loop to ensure certain local channels
            // are handled by the same event loop thread which drives a certain socket channel
            // to reduce the communication latency between socket channels and local channels.
            ServerBootstrap sb = new ServerBootstrap();
            //参数只有一个group?  ==> return group(group, group);
            sb.group(serverGroup)
                    .channel(LocalServerChannel.class)
                    .handler(new ChannelInitializer<LocalServerChannel>(){
                        @Override
                        protected void initChannel(LocalServerChannel localServerChannel) throws Exception {
                            localServerChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    })
                    //LocalChannel: A {@link Channel} for the local transport.
                    .childHandler(new ChannelInitializer<LocalChannel>(){
                        @Override
                        protected void initChannel(LocalChannel localChannel) throws Exception {
                            localChannel.pipeline().addLast( new LoggingHandler(LogLevel.INFO), new LocalEchoServerHandler());
                        }
                    });

            Bootstrap cb = new Bootstrap();
            cb.group(clientGroup).channel(LocalChannel.class).handler(new ChannelInitializer<LocalChannel>() {
                @Override
                protected void initChannel(LocalChannel localChannel) throws Exception {
                    localChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new LocalEchoClientHandler());
                }
            });

            //start server
            sb.bind(addr).sync();

            //start the client
            Channel ch = cb.connect(addr).sync().channel();

            //read commands from stdin =>stdin是标准输入，一般指键盘输入到缓冲区里的东西。
            System.out.println("Enter text (quit to end)");
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for(;;){
                String line = in.readLine();
                if(line == null || "quit".equalsIgnoreCase(line)){
                    break;
                }
                //sends the recieved line to the server
                lastWriteFuture = ch.writeAndFlush(line);
            }

            //Write until all messages are flushed before closing the channel.
            if(lastWriteFuture!= null){
                //Waits for this future to be completed without interruption.
                lastWriteFuture.awaitUninterruptibly();
            }

        }finally{
            serverGroup.shutdownGracefully();
            clientGroup.shutdownGracefully();
        }

    }
}
