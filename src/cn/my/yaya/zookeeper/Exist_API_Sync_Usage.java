package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 节点是否存在 同步
 * result:
 *   · 无论节点是否存在，都可以通过exists接口注册Watcher。

 　　· 注册的Watcher，对节点创建、删除、数据更新事件进行监听。

 　　· 对于指定节点的子节点的各种变化，不会通知客户端。
 */
public class Exist_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null ;

    @Override
    public void process(WatchedEvent event) {
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                if (Event.EventType.None == event.getType() && null == event.getPath()) {
                    connectSemaphore.countDown();
                } else if (Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("success create znode: " + event.getPath());
                    /* If the watch is true and the call is successful (no exception is thrown),
                     * a watch will be left on the node with the given path. The watch will be
                     * triggered by a successful operation that creates/delete the node or sets
                     * the data on the node. */
                    zk.exists(event.getPath(), true);
                } else if (Event.EventType.NodeDeleted == event.getType()) {
                    System.out.println("success delete znode: " + event.getPath());
                    zk.exists(event.getPath(), true);
                } else if (Event.EventType.NodeDataChanged == event.getType()) {
                    System.out.println("data changed of znode: " + event.getPath());
                    zk.exists(event.getPath(), true);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        String path = "/zk-book";
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new Exist_API_Sync_Usage());
            connectSemaphore.await();

            zk.exists(path, true);

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
