package cn.my.yaya.zookeeper;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * http://www.cnblogs.com/leesf456/p/6032716.html
 * http://curator.apache.org/apidocs/index.html
 */
public class Curator_Create_Session_Sample {
    public static void main(String[] args) {
        /** ExponentialBackoffRetry
         * @param baseSleepTimeMs initial amount of time to wait between retries
         * @param maxRetries max number of times to retry
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 5000, 3000, retryPolicy);
        client.start();
        System.out.println("Zookeeper session1 established. ");
        CuratorFramework client2 = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).
                retryPolicy(retryPolicy).namespace("base").build();
        client2.start();
        System.out.println("Zookeeper session2 established. ");
    }
}
