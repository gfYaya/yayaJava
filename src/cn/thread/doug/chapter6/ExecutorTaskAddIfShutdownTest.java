package cn.thread.doug.chapter6;

import java.util.concurrent.*;

/* page 101
 * 如果executor执行shutdown,从开始关闭到真正的关闭期间,是否可以向该线程池中加入新的任务
 *  while(!exe.isShutdown() ) 产生疑惑. 另一个就是isShutdown()是表示开始关闭还是已经关闭才返回true?
 *
 *  warning:isShutdown() => Returns true if this executor has been shut down.
 */
public class ExecutorTaskAddIfShutdownTest {
    //private static final int MAX_NUM = Runtime.getRuntime().availableProcessors();
    private static final int MAX_NUM = 4;
    //private ExecutorService exe = Executors.newFixedThreadPool(MAX_NUM);

    public static void main(String[] args) {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_NUM);
        CyclicBarrier cb = new CyclicBarrier(MAX_NUM + 1);
        for(int i = 0;i<MAX_NUM; i++){
            final int ii = i; //jdk8 不是据说可以不使用final 来控制匿名内部类了吗?为何还不行?
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("this is thread "+ ii + ", start");
                    try {
                        // used for the next thread initial,make sure all the threads in executor is ready.maybe i could use CyclicBarrier for this.
                        //Thread.sleep(5000);

                        cb.await();
                        //Thread.sleep(5000);//used for shutdown()
                        System.out.println("this is thread "+ ii + ", over");
                        System.out.println("thread" + ii + ",exe is shutting down : " +exe.isShutdown());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            exe.submit(thread);//可以直接放入runnable,上面的thread有点多此一举.....
        }
        try {
            cb.await();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        exe.shutdown();
        System.out.println("exe is shutting down : " + exe.isShutdown());

        CountDownLatch cdl = new CountDownLatch(1);//CyclicBarrier could be used here.
        try {
            // exe.submit(new Runnable() { => java.util.concurrent.RejectedExecutionException:
            exe.execute(new Runnable() { //also, here is an exception
                @Override
                public void run() {
                    System.out.println("new Thread is starting");
                    cdl.countDown();
                }
            });

        }catch(RejectedExecutionException e){
            e.printStackTrace();
            System.out.println("The executor is shutting down now.");
            cdl.countDown();
        }


        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("exe is shutting down(the last time): " + exe.isShutdown());

    }

}
