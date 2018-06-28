package cn.thread.doug.chapter7;

import java.util.concurrent.TimeUnit;

/*   synchronize 编程中表示 的是原子的  如果是原子的 是不是就意味着不可中断?但是Java中的中断是协作式的 而不是抢占式
 *   因为Java无法操作OS系统操作 做到上下文切换的环境保存  那么 Java的 线程A 正在执行 syn代码块 另一个线程 调用a.interrupt()
 *   此时代码块是否能检测到 中断状态?
 *
 *   result : 能
 *   P116
 */
public class SynInterrupt {
    public static void main(String[] args) throws InterruptedException {
        final Object lock = new Object();

        Thread t = new Thread() {
            public void run() {
                synchronized (lock) {
                    boolean isInterrupted = false;
                    do {
                        System.out.println("hello");
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (isInterrupted = Thread.currentThread().isInterrupted());
                }
            }
        };
        t.start();

        TimeUnit.SECONDS.sleep(2);
        t.interrupt();
        System.out.println("main end");
    }
}
