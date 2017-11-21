package cn.my.yaya.zookeeper;

import org.I0Itec.zkclient.ZkClient;

/**
 * ZkClient 删除节点
 */
public class Del_Data_Sample {
    public static void main(String[] args) {
        String path = "/zk-book-2";
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        //the second param is the data this Znode saves.
        zkClient.createPersistent(path,"");
        zkClient.createPersistent(path+"/c1", "");
        zkClient.deleteRecursive(path);
        System.out.println("success delete znode.");
    }
}
