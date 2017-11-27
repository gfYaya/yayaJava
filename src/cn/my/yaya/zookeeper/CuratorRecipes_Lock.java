package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Curator Recipse , 分布式锁
 *  并不是有序的,只是生成单一的流水号
 */
public class CuratorRecipes_Lock {
    static String lockPath = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        client.start();
        final InterProcessLock lock = new InterProcessMutex(client,lockPath);
        final CountDownLatch down = new CountDownLatch(1);

        for(int i=0;i<=30;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        down.await();
                        lock.acquire();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.out.println("生成的订单号是 : " + orderNo);
                    try{
                        lock.release();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        down.countDown();
    }
}
