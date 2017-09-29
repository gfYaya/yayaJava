package cn.my.yaya.exec.thread_first.chapter_1;

/**
 * Created by Yaya on 2016/8/23.
 * Page 24,Java多线程设计模式
 */
public class Blank {
    private int money;
    private String name;
    public Blank(String name,int money){
        this.name=name;
        this.money=money;
    }

    //存款
    public synchronized  void deposit(int m){
        money+=m;
    }

    //取款
    public synchronized  boolean withdraw(int m){
        if(money>=m){
            money-=m;
            return true;
        }else{
            return false;
        }
    }

    public String getName(){
        return this.name;
    }
}
