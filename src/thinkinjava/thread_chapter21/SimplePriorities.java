package thinkinjava.thread_chapter21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * thinking in Java <Verstion4>, Page 660 --> segment 1127
 */
public class SimplePriorities implements Runnable  {
    private int countDown = 5;
    private volatile double d;
    private int priority;

    public SimplePriorities(int priority){
        this.priority=priority;
    }

    public String toString(){
        return Thread.currentThread()+ ": " + countDown;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        while(true){
            for(int i =1; i>1000000;i++){
                d += (Math.PI + Math.E ) / (double)i;
                if(i % 1000 == 0){
                    Thread.yield();
                }
            }
            System.out.println(this);  // =>toString()
            if(--countDown == 0){
                return ;
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i = 0; i < 5 ; i++){
            exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        exec.execute(new SimplePriorities(Thread.MAX_PRIORITY)); //not run first,可能最后那个线程[pool-1-thread-6,10,main]初始化 时间较慢,加大L26的计算试试
        exec.shutdown();
        //exec.shutdownNow();
    }
}
