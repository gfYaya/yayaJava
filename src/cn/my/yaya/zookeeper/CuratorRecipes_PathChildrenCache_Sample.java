package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * CuratorRecipes 子节点监控
 */
public class CuratorRecipes_PathChildrenCache_Sample {
    static String path="/zk-book-recipes";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).
            retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

    public static void main(String[] args) throws Exception {
        client.start();
        PathChildrenCache cache = new PathChildrenCache(client , path ,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        switch(event.getType()){
                            case CHILD_ADDED: //PathChildrenCacheEvent 内部类的Type类型
                                System.out.println("CHILD_ADDED," + event.getData().getPath());
                                break;
                            case CHILD_UPDATED:
                                System.out.println("CHILD_UPDATED," + event.getData().getPath());
                                break;
                            case CHILD_REMOVED:
                                System.out.println("CHILD_REMOVED," + event.getData().getPath());
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
        Thread.sleep(1000);
        client.create().withMode(CreateMode.PERSISTENT).forPath(path+"/c1");
        Thread.sleep(1000);
        client.delete().forPath(path+"/c1");
        Thread.sleep(1000);
        client.delete().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
