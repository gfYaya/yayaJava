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
            System.out.println("done");
            System.out.println("================");
            if(Thread.interrupted()){
                System.out.println("this thread is revived");
            }

            //todo  自己进行中断标志重置
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

        //todo
        //重置中断标记位.但是怎么做?.Thread.interrupted()肯定不行,目前在main方法这个主线程中
        /*thread.interrupted();
        System.out.println("-----------------------");
        while (!thread.isInterrupted()){
            System.out.println("main:"+ thread.isInterrupted());
            //still console literal "false" again and again,and can't stop by itself?why? I have interrupted it.
        }*/

        /*  Add By Intopass and 叶次然
         *  线程被中断 != 停止运行,线程依然处于存活中,可以自己在run方法中实现中断标志位重新清楚,
         *  毕竟线程只是标志性中断,没有真正的中断,不影响代码正常执行
         */
    }
}
