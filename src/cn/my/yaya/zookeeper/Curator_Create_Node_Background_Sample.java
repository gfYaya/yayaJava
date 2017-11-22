package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Curator 异步接口.
 */
public class Curator_Create_Node_Background_Sample {
    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).
            retryPolicy(new ExponentialBackoffRetry(5000,3)).build();
    static CountDownLatch semaphore = new CountDownLatch(1);
    static ExecutorService tp = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        client.start();
        System.out.println("Main thread :" + Thread.currentThread().getName());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("event[code: " + event.getResultCode() + ", type: " + event.getType() + "]" +
                                ", Thread of processResult: " + Thread.currentThread().getName());
                        System.out.println();
                        semaphore.countDown();
                    }
                }
        ,tp).forPath(path, "init".getBytes());

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("event[code: " + event.getResultCode() + ", type: " + event.getType() + "]" +
                                ", Thread of processResult: " + Thread.currentThread().getName());
                        semaphore.countDown();
                    }
                }
        ).forPath(path,"init".getBytes());

        semaphore.await();
        tp.shutdown();
    }
}
