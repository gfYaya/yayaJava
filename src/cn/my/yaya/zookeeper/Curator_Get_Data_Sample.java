package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Curator获取数据
 */
public class Curator_Get_Data_Sample {
    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(5000,3)).build();
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"init".getBytes());
        Stat stat= new Stat();
        System.out.println(new String(client.getData().storingStatIn(stat).forPath(path)));
    }
}
