package cn.my.yaya.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 *  Curator Recipes 分布式Barrier
 */
public class CuratorRecipes_Barrier {
    static String barrier_path = "/curator_recipes_barrier_path";
    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for(int i=0;i< 5;i++){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString("127.0.0.1:2181")
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                        client.start();
                        barrier = new DistributedBarrier(client,barrier_path);
                        System.out.println(Thread.currentThread().getName()+"号barrier设置");
                        barrier.setBarrier(); // client.create().creatingParentsIfNeeded().forPath(barrierPath);
                        barrier.waitOnBarrier(); //Blocks until the barrier node comes into existence
                        System.err.println("启动...");

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        barrier.removeBarrier();
    }
}
//结果表明通过DistributedBarrier可以实现类似于CyclicBarrier的分布式Barrier功能。

