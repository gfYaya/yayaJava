package cn.my.yaya.exec.thread_first.chapter1.chapter1_2;

/**
 * Created by Yaya on 2016/9/5.
 * Java多线程设计模式  Page 55
 */
public class Gate {
    private int counter =0;
    private String name="Nobody";
    private String address="Nowhere";
    public synchronized void pass(String name,String address){
        this.counter++;
        this.name=name;
        this.address=address;

    }

    public synchronized String toString(){
        return "No."+counter+":"+name+","+address;
    }

    private void checked(){
        if( name.charAt(0) == address.charAt(0)){
            System.out.println("****BROKEN****"+toString() );
        }
    }
}
