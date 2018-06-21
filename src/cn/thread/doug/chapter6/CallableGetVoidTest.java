package cn.thread.doug.chapter6;

import java.util.concurrent.*;

public class CallableGetVoidTest {

    public static void main(String[] args) {
        ExecutorService exe = Executors.newCachedThreadPool();
        Future<Void> future = exe.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                //return new Void();
                return null; //貌似这个地方就是这么用的
            }
        });

        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
