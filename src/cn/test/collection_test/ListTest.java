package cn.test.collection_test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaya on 2017/6/15.
 * 测试 List A中被嵌套的List B,如果先 B放入到A中,再向B中添加元素 是否能成功加入进去
 * result : L26 结果为3,说明还是能影响的
 */
public class ListTest {
    private List<List<Integer>> list = new ArrayList<List<Integer>>();
    //private List<List<Integer>> list2 = new ArrayList<ArrayList<Integer>>();
    // incompatible types,泛型中的template不支持强转和转型,里面的 List<Integer> 已经被当成一个泛型了
    // 此时 List<Integer> 已经是template了 不能做修改了

    public static void main(String[] args) {
        List<Integer> innerList = new ArrayList();
        innerList.add(1);
        innerList.add(2);
        ListTest t = new ListTest();
        //t.list.add(10, innerList);  //Exception in thread "main" java.lang.IndexOutOfBoundsException: Index: 10, Size: 0
        t.list.add(0, innerList);
        System.out.println(t.list.size());
        innerList.add(2,3);
        System.out.println("innerList.size:"+innerList.size()); //3

    }

}
