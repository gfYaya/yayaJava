package mytest.thread;

import java.util.concurrent.CountDownLatch;

//Main的升级版
public class Main2 {
    private static Object lock = new Object();
    private static int amount = 0;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                //synchronized (lock) {
                    int temp = amount;
                    System.out.printf("t=1,i=%d,read amount = %d%n", i, temp);
                    threadSleep(100);
                    amount = temp + 1;
                    System.out.printf("t=1,i=%d,write amount = %d%n", i, temp + 1);
                //}
                threadSleep(100);
            }
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                //synchronized (lock) {
                    int temp = amount;
                    System.out.printf("t=2,i=%d,read amount = %d%n", i, temp);
                    threadSleep(150);
                    amount = temp + 1;
                    System.out.printf("t=2,i=%d,write amount = %d%n", i, temp + 1);
                //}
                threadSleep(100);
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
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
