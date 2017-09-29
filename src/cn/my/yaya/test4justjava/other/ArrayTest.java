package cn.my.yaya.test4justjava.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaya on 2017/8/22.
 * 测试Java数组与Array是否和Python一样 将一个变量赋值给另一个变量, seq 赋值传递的是引用,类似于JDK1.7的string.intern()
 * result:JDK1.6 JDK1.8的 Array数组 与 List容器 传递的是引用,但是基础类型和String无效
 */
public class ArrayTest {
    public static void main(String[] args) {
        int[] a = new int[]{0, 0, 0, 0};
        int[] b = a;
        a[3] = 1;
        for (int tmp : b) {
            System.out.print(tmp); //[0,0,0,1]
        }

        System.out.println("------------");
        List<Integer> list_1 = new ArrayList<Integer>();
        List<Integer> list_2 =list_1;
        list_1.add(0);
        System.out.println("list_1:"+list_1.size()); // 1
        System.out.println("list_2:"+list_2.size()); // 1

        //字符串测试
        String str_1 ="str";
        String str_2 =str_1;
        str_1 += str_1 + "a";
        System.out.println(str_2); //str

        int i1=10;
        int i2=i1;
        i1 = 13;
        System.out.println(i2);
    }
}
