package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 *  Curator Recipes 的Master选举
 */
public class CuratorRecipes_MasterSelect {
    static String masterPath = "/curator_recipes_master_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).
            retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

    public static void main(String[] args) {
        client.start();
        LeaderSelector selector = new LeaderSelector(client, masterPath,
                new LeaderSelectorListenerAdapter() {
                    @Override
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        System.out.print("成为Master角色");
                        Thread.sleep(1000);
                        System.out.println("完成Master操作,释放Master权利");
                    }
                });
        selector.autoRequeue(); // Calling this method puts the leader selector into a mode where it will always requeue itself.
                                // requeue :将被替换过的报文重新分配到指定输出队列中
        selector.start();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
       以上结果会反复循环，并且当一个应用程序完成Master逻辑后,另外一个应用程序的相应方法才会被调用，
       即 当一个应用实例成为Master后，其他应用实例会进入等待，直到当前Master挂了或者推出后才会开始选举Master。
          个人觉得 文中的 等待 应该指的是 Master选举 等待
     */
}
