package cn.test.treemap;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Yaya on 2017/4/10.
 */
// 用于检测TreeMap是否会去重
public class TreeMapTest {

    public static void main(String[] args) {
        Map<Integer, String> treemap = new TreeMap<Integer, String>();
        treemap.put(1, "abc");
        treemap.put(1, "bcd");
//        System.out.println(treemap.get(1));
        for(int tmp:treemap.keySet() ){
            System.out.println("Key:"+tmp+",Value:"+treemap.get(tmp)); //Key:1,Value:bcd
            // treemap也会去重,也就是说 Map本身不支持一个key多个Value,hashmap去重的说法严重有问题
        }
    }
}

