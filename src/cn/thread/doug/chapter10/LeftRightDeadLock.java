package cn.thread.doug.chapter10;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//P171 deadlock测试 (抱死式死锁 Deadly-Embrace,如果所有线程使用相同调用资源[锁]顺序,可以避免死锁)
//jps   // jstack -l pid  (top -Hp)
public class LeftRightDeadLock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight(){
        synchronized (left){
            System.out.println("a left");
            try {
                //必须要对锁住的对象进行wait,因为需要对锁进行释放,从锁池释放,放到等待池中.
                left.wait(1000);
                //wait(1000); //Exception in thread "Thread-0" Exception in thread "Thread-1" java.lang.IllegalMonitorStateException
                System.out.println("a left wait to end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (right){
                System.out.println("a right");
                System.out.println("this is left to right");
            }
        }
    }

    public void rightLeft(){
        synchronized (right){
            System.out.println("b left");
            try {
                right.wait(1000);
                //wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("b left wait to end");
            synchronized (left){
                System.out.println("this is right to left");
            }
        }
    }

    public void testClassMonitor(){
        synchronized (LeftRightDeadLock.class){
            try {
                Class<?> a = LeftRightDeadLock.class;
                a.wait();
                //LeftRightDeadLock.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        LeftRightDeadLock lock = new LeftRightDeadLock();
        CyclicBarrier c = new CyclicBarrier(2);
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                lock.leftRight();
            }
        });
        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                lock.rightLeft();
            }
        });
        a.start();
        b.start();
    }

}


