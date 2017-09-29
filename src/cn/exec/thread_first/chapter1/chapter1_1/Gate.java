package cn.exec.thread_first.chapter1.chapter1_1;

/**
 * Created by Yaya on 2016/9/2.
 * Java 多线程设计模式 P50
 */
public class Gate {
    private int counter=0;
    private String name="Nobody";
    private String address="Nowhere";

    public void pass(String name,String address){
        this.counter++;
        this.name=name;
        this.address=address;
        check();
    }

    public String toString(){
        return "No."+counter+":"+name+","+address;
    }

    private void check(){
        if( name.charAt(0) != address.charAt(0) ){
            System.out.println("***** BROKEN *****" +toString() );
        }
    }
}
