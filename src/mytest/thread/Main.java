package mytest.thread;

import java.util.concurrent.CountDownLatch;

//from  Intopass
public class Main {
    private static Object lock = new Object();
    private static int amount = 0;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
               //synchronized (lock) {
                int temp = amount;
                //threadSleep(100);
                amount = temp + 1;
                //}
            }
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                //synchronized (lock) {
                int temp = amount;
                //threadSleep(150);
                amount = temp + 1;
                //}
            }
            countDownLatch.countDown();
        }).start();

        try {
            countDownLatch.await();
            System.out.println(amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void threadSleep(int millis) {
        //加上sleep是为了让并发冲突体现的更明显,不加的话，也有可能会冲突，不过概率要低
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
