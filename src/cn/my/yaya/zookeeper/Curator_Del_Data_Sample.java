package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Curator删除节点
 */
public class Curator_Del_Data_Sample {
    public static void main(String[] args) throws Exception {
        String path = "/zk-book/c1";
        CuratorFramework client =CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"init".getBytes());
        Stat stat = new Stat();//Stat 用来存储节点信息?
        System.out.println(new String(client.getData().storingStatIn(stat).forPath(path)));
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        System.out.println("success delete znode " + path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
