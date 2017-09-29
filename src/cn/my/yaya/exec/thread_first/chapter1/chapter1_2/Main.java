package cn.my.yaya.exec.thread_first.chapter1.chapter1_2;


/**
 * Created by Yaya on 2016/9/2.
 */
public class Main {
    public static void main(String args[]){
        System.out.println("Testing Gate,hit CTRL+C to exit");
        Gate gate =new Gate();
        new UserThread(gate,"Alice","Alaska").start();
        new UserThread(gate,"Bobby","Brazil").start();
        new UserThread(gate,"Chris","Canada").start();
    }
}
