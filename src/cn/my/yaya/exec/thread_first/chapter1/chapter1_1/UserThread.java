package cn.my.yaya.exec.thread_first.chapter1.chapter1_1;

/**
 * Created by Yaya on 2016/9/2.
 */
public class UserThread extends Thread {
    private final Gate gate;
    private final String myname;
    private final String myaddress;

    public UserThread(Gate gate,String myname,String myaddress){
        this.gate=gate;
        this.myname=myname;
        this.myaddress=myaddress;
    }

    @Override
    public void run(){
        System.out.println(myname +" BEGIN");
        while(true){
            gate.pass(myname,myaddress);
        }
    }
}
