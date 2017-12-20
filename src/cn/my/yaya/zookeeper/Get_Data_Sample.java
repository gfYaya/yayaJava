package cn.my.yaya.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * ZkClient 获取节点数据
 */
public class Get_Data_Sample {
    public static void main(String[] args) {
        String path = "/zk-book";
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
        zkClient.createEphemeral(path, "123");

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("Node " + dataPath + " changed, new data: " + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("Node " + dataPath + " deleted.");
            }
        });
        //System.out.println(zkClient.readData(path));
        zkClient.writeData(path, "456");
        try {
            Thread.sleep(1000);
            zkClient.delete(path);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
