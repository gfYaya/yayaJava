package cn.thread.doug.chapter7;
/*
 * P119 如果一个线程已经执行结束,这个时候对该线程执行中断会如何?
 * result: 没有影响,但是设置中断从某种程度上来说算是失败,因为判断线程的中断标志位显示false.
 */
public class ThreadOverThenInterrupt {

    public static void main(String[] args) {
        Thread t = new Thread( ()->{
            System.out.println("LOLOLOLO");
        });
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt();
        System.out.println(t.isInterrupted()); //false
    }
}
