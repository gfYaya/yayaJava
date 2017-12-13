package mytest.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//线程池测试 判断线程池开辟的任务是否是异步的
public class PoolTest {
    private static ExecutorService pool;

    public static void main(String[] args) {
        int currThreadAmount = Runtime.getRuntime().availableProcessors();
        System.out.println("processors: " + currThreadAmount);
        pool= Executors.newFixedThreadPool(currThreadAmount);
        pool.execute(new TaskZero());
        System.out.println("main");
    }

}

class TaskZero implements Runnable{

    @Override
    public void run() {
        System.out.println("taskzero");
    }
}