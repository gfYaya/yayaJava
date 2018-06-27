package cn.thread.doug.chapter7;

import java.util.concurrent.TimeUnit;

/* 线程中断之后,线程是否处于消亡状态?如果不是,由于Java中断属于协作式,
 * 中断状态重新清理之后,是否可以继续运行?
 * P115
 */
public class IsInterruptThenEnd {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) { //currentThread().isInterrupted(true); true =>clear the interrupted flag
                //function isInterrupted(boolean ClearInterrupted) is private and native,so I don't know how interrupt int some other manner without reflect.
                System.out.println(Thread.currentThread().isInterrupted());
            }
            System.out.println(Thread.currentThread().isInterrupted());
        });
        thread.start();

        try {
            //TimeUnit.SECONDS.sleep(1);
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        //System.out.println(thread.isInterrupted());
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //重置中断标记位.但是怎么做?.
    }
}
