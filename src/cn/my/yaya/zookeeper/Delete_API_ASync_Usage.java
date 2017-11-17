package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步删除节点
 */
public class Delete_API_ASync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                connectSemaphore.countDown();
            }

        }
    }

    public static void main(String[] args) throws IOException {
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new Delete_API_ASync_Usage());
        try {
            connectSemaphore.await();
            zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path);
            zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path + "/c1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

        zk.delete(path, -1, new IVoidCallback(), null);
        zk.delete(path + "/c1", -1, new IVoidCallback(), null);
        zk.delete(path, -1, new IVoidCallback(), null);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class IVoidCallback implements AsyncCallback.VoidCallback{

    @Override
    public void processResult(int rc, String path, Object ctx) {
        System.out.println(rc + ", " + path + ", " + ctx);
    }
}
