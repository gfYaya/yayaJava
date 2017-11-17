package cn.my.yaya.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步创建节点
 */
public class Zookeeper_Create_API_ASync_Usage implements Watcher{
    private static CountDownLatch connectSemaphore = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectSemaphore.countDown();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181",5000,new Zookeeper_Create_API_ASync_Usage());
        System.out.println(zookeeper.getState());
        try {
            connectSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // create 中的 Object ctx 参数 用来表示文件中的内容
        zookeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallBack(),"I am context ");
        zookeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallBack(), "I am context. ");
        zookeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallBack(), "I am context. ");
        Thread.sleep(Integer.MAX_VALUE);
    }
}

class IStringCallBack implements AsyncCallback.StringCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, String name) { //返回值为void?和Future中的 E call() 不一样
        // rc =>Result Code ?
        System.out.println("Create path result: [" + rc + ", " + path + ", " + ctx + ", real path name: " + name);
    }
}