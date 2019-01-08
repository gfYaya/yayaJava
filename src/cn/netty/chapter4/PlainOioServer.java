package cn.netty.chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

//P39
//不使用Netty完成OIO 阻塞式网络编程
public class PlainOioServer {

    public void server(int port) throws IOException{
        final ServerSocket serverSocket = new ServerSocket(port);
        try{
            for(;;){
                //accept():Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made.
                final Socket clientSocket = serverSocket.accept(); //接受连接
                System.out.println("Accepted connection from "+clientSocket);
                new Thread(new Runnable() {  //创建一个新的线程来处理该连接
                    @Override
                    public void run() {
                        OutputStream out;
                        try{
                            out = clientSocket.getOutputStream();
                            out.write("Hi\r\n".getBytes(Charset.forName("UTF-8"))); //将消息写给已连接的客户端
                            out.flush();
                            clientSocket.close(); //关闭连接
                        }catch(IOException e){
                            e.printStackTrace();
                        }finally{
                            try{
                                clientSocket.close();
                            }catch(IOException e){
                                // ignore on close
                            }
                        }
                    }
                }).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new PlainOioServer().server(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
