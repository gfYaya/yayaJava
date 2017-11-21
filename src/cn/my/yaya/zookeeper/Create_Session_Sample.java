package cn.my.yaya.zookeeper;

import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;

/**
 * http://www.cnblogs.com/leesf456/p/6032716.html
 * ZkClient 创建节点
 */
public class Create_Session_Sample {
    public static void main(String[] args) throws IOException, InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        System.out.println("ZooKeeper session established.");
    }
}
