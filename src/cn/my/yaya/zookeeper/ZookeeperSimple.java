package cn.my.yaya.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * http://www.cnblogs.com/leesf456/p/6028416.html
 */
public class ZookeeperSimple implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive wathced event :" +watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            System.out.println("Event.KeeperState.SyncConnected:"+Event.KeeperState.SyncConnected);
            System.out.println("watchedEvent.getState():"+watchedEvent.getState());
            connectedSemaphore.countDown();
        }
    }

    public static void main(String[] args) throws IOException {
        ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181",5000,new ZookeeperSimple());
        System.out.println("zookeeper state:" + zookeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zookeeper session established");
    }
}
