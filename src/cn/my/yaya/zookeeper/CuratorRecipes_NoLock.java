package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.txn.TxnHeader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

/**
 * Curator Recipes 分布式锁demo  无分布式锁情况
 */
public class CuratorRecipes_NoLock {

    public static void main(String[] args) {
        final CountDownLatch down = new CountDownLatch(1);
        for(int i=0;i<10;i++){
            new Thread(
                    new Runnable(){

                        @Override
                        public void run() {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                            String orderNo = sdf.format(new Date());
                            System.err.println("生成的订单号是 : " + orderNo);
                        }
                    }
            ).start();
        }
        down.countDown();
    }

}
