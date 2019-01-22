package cn.netty.inaction.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//P4
public class BlockingDemo {
    public static void main(String[] args) {
        final int portNumber = 8080;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request=null, response = null;
            //postman 请求 localhost:8080 程序便自动停止了
            //整个流程是基于TCP的,需要使用telnet 来测试,虽然能接受请求,但是未进行HTTP请求处理和解析,要处理 HTTP 协议，需要得精通 RFC 2616 规范。

            //while ((request = in.readLine()) != null) {

            //telnet localhost 8080  返回"This is yaya" ,IDE 控制台为Socket IO异常,如果在输入 Done 于控制台中,在打入telnet localhost 8080 命令之后,便可以输出Done /n this is done
            //如果此时postman 发送请求 locahost:8080 ,IDR控制台输出null;无论 body报文体有什么,都不会输出
            while(true){
                request = in.readLine();
                System.out.println(request);
                if (request == null){
                    break;
                }
                if ("Done".equals(request)) {
                    System.out.println("this is done");
                    break;
                }
                //response = processRequest(request);
                response = "This is yaya";
                //out.println(response);
                out.write(response);
                out.flush();
                out.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
