package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * Curator Recipes 的 分布式计算器
 */
public class CuratorRecipes_DistAtomicInt {
    static String distatomicint_path = "/curator_recipes_distatomicint_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

    public static void main(String[] args) {
        client.start();
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client,distatomicint_path, new RetryNTimes(3, 1000));
        try {
            AtomicValue<Integer> rc =  atomicInteger.add(8);
            System.out.println("Result: " + rc.succeeded());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
