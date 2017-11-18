package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 同步更新数据 (CAS 乐观锁, 客户端携带的version将无法匹配，无法更新成功，因此可以有效地避免分布式更新的并发问题)
 */
public class SetData_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                connectSemaphore.countDown();
            }
        }
    }

    public static void main(String[] args) {
        String path = "/zk-book";
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new SetData_API_Sync_Usage());
            connectSemaphore.await();
            zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("success create znode: " + path);
            zk.getData(path, true, null);

            Stat stat = zk.setData(path, "456".getBytes(), -1);
            System.out.println("czxID: " + stat.getCzxid() + ", mzxID: " + stat.getMzxid() + ", version: " + stat.getVersion());
            Stat stat2 = zk.setData(path, "456".getBytes(), stat.getVersion());//setData中的version参数设置-1含义为客户端需要基于数据的最新版本进行更新操作。
            System.out.println("czxID: " + stat2.getCzxid() + ", mzxID: " + stat2.getMzxid() + ", version: " + stat2.getVersion());

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
