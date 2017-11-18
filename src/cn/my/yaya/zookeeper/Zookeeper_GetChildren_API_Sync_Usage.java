package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *同步获取子节点
 */
public class Zookeeper_GetChildren_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                connectSemaphore.countDown();
            }else if(Event.EventType.NodeChildrenChanged == event.getType()){
                try {
                    System.out.println("Reget Child :" + zk.getChildren(event.getPath(),true));
                    /*if the watch is true and the call is successful (no exception is thrown),
                    * a watch will be left on the node with the given path. The watch willbe
                    * triggered by a successful operation that deletes the node of the given
                    * path or creates/delete a child under the node.
                     */
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String path = "/zk-book-1";
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new Zookeeper_GetChildren_API_Sync_Usage());
            connectSemaphore.await();
            zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path);
            zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c1");
            List<String> childrenList = zk.getChildren(path, true);
            System.out.println(childrenList);

            zk.create(path + "/c2", "".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c2");
            Thread.sleep(1000);
            zk.create(path + "/c3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path + "/c3");
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
