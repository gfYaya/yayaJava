package cn.my.yaya.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * ZkClient
 */
public class Exist_Node_Sample {
    public static void main(String[] args) {
        String path = "/zk-book";
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 2000);
        System.out.println("Node " + path + " exists " + zkClient.exists(path));
    }
}
