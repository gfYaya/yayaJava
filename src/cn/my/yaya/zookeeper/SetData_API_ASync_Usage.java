package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步更新数据
 */
public class SetData_API_ASync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk=null;

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectSemaphore.countDown();
            }
        }
    }

    public static void main(String[] args) {
        String path = "/zk-book";
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new SetData_API_ASync_Usage());
            connectSemaphore.await();

            zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path);
            zk.setData(path, "456".getBytes(), -1, new IStatCallback(), null);

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

class IStatCallback implements AsyncCallback.StatCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc: " + rc + ", path: " + path + ", stat: " + stat);
    }
}
