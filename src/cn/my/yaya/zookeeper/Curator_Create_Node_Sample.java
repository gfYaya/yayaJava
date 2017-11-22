package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Curator 创建节点
 */
public class Curator_Create_Node_Sample {
    public static void main(String[] args) {
        String path="/zk-book-curator/c1";
        //String path="/zk-book-curator";
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").connectionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("yaya").build();
                // retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        client.start();
        try {
            // Version 4.0.0 最新版本的时候 报错  MethodNotFound
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
            System.out.println("success create znode: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
