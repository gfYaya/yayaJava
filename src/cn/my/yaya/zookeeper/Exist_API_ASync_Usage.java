package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步更新节点
 */
public class Exist_API_ASync_Usage implements Watcher{
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    @Override
    public void process(WatchedEvent event) {
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                if (Event.EventType.None == event.getType() && null == event.getPath()) {
                    connectedSemaphore.countDown();
                } else if (Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("success create znode: " + event.getPath());
                    zk.exists(event.getPath(), true, new IIStatCallback(), null);
                } else if (Event.EventType.NodeDeleted == event.getType()) {
                    System.out.println("success delete znode: " + event.getPath());
                    zk.exists(event.getPath(), true, new IIStatCallback(), null);
                } else if (Event.EventType.NodeDataChanged == event.getType()) {
                    System.out.println("data changed of znode: " + event.getPath());
                    zk.exists(event.getPath(), true, new IIStatCallback(), null);
                }
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        String path = "/zk-book";
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 5000,new Exist_API_ASync_Usage());
            connectedSemaphore.await();

            zk.exists(path, true, new IIStatCallback(), null);

            zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zk.setData(path, "123".getBytes(), -1);

            zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path + "/c1");

            zk.delete(path + "/c1", -1);
            zk.delete(path, -1);

            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }
}

class IIStatCallback implements AsyncCallback.StatCallback {
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc: " + rc + ", path: " + path + ", stat: " + stat);
    }
}