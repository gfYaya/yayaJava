package cn.my.yaya.other;

import java.lang.ref.WeakReference;

/**
 * https://zhuanlan.zhihu.com/p/28258571
 */
public class WeakReferenceTest {
    public static void main(String[] args) {
        WeakReference<String>  rs= new WeakReference<String>(new String("Hello"));
        System.out.println(rs.get());
        System.gc();
        System.out.print(rs.get());
    }
}
