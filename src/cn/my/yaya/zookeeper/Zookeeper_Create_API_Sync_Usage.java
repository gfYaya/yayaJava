package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * http://www.cnblogs.com/leesf456/p/6028416.html
 * 同步方式 创建节点
 */
public class Zookeeper_Create_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectSemaphore.countDown();
        }
    }

    public static void main(String[] args) throws IOException {
        ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181",5000, new Zookeeper_Create_API_Sync_Usage()); // 5000 session timeout
        System.out.println(zookeeper.getState());
        try {
            connectSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            List<String> list=zookeeper.getChildren("/",new Zookeeper_Create_API_Sync_Usage());
            System.out.println(list);
            String path1 =zookeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("Success create znode: " + path1);
            String path2 = zookeeper.create("/zk-test-ephemeral-", "yaya".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("Success create znode: " + path2);
            List<String> list2=zookeeper.getChildren("/",new Zookeeper_Create_API_Sync_Usage());
            System.out.println(list2);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
