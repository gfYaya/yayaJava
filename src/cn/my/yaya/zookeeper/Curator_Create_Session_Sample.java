package cn.my.yaya.zookeeper;


import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * http://www.cnblogs.com/leesf456/p/6032716.html
 * http://curator.apache.org/apidocs/index.html
 */
public class Curator_Create_Session_Sample {
    public static void main(String[] args) {
        /**
         * @param baseSleepTimeMs initial amount of time to wait between retries
         * @param maxRetries max number of times to retry
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    }
}
