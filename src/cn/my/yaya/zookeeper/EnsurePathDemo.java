package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

/**
 * EnsurePath
 * 其提供了一种能够确保数据节点存在的机制，当上层业务希望对一个数据节点进行操作时，操作前需要确保该节点存在。　
 */
public class EnsurePathDemo {
    static String path = "/zk-book-ensure/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) throws Exception {
        client.start();
        System.out.println("client.getNamespace() :"+ client.getNamespace());
        client.usingNamespace("zk-book-ensure");

        EnsurePath ensurePath = new EnsurePath(path);
        // First time, synchronizes and makes sure all nodes in the path are created. Subsequent calls with this instance are NOPs.
        ensurePath.ensure(client.getZookeeperClient());
        ensurePath.ensure(client.getZookeeperClient());
        System.out.println("client.getNamespace() 2:"+ client.getNamespace());

        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c");
        ensurePath2.ensure(client.getZookeeperClient());
    }
}
