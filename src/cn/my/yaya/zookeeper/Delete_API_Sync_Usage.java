package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 同步删除节点
 */
public class Delete_API_Sync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                System.out.println("Event.EventType.None:"+Event.EventType.None);
                System.out.println("event.getType():"+event.getType());
                System.out.println("event.getPath():"+event.getPath());
                connectSemaphore.countDown();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "/zk-book";
        zk=new ZooKeeper("localhost:2181",5000,new Delete_API_Sync_Usage());
        try {
            connectSemaphore.await();
            zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path);
            zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("success create znode: " + path + "/c1");

            zk.delete(path + "/c1", -1);
            System.out.println("success delete znode: " + path + "/c1");
            zk.delete(path,-1); // -1 is the version?
            System.out.println("success to delete znode: " + path);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            zk.delete(path, -1);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            System.out.println("fail delete znode: " + path);
            e.printStackTrace();
        }

    }
}
