package cn.my.yaya.test4justjava.inherit.t2;

import cn.my.yaya.test4justjava.inherit.t1.InheritPrivateTest;

/**
 * Created by Yaya on 2017/4/14.
 */
public class MainTest {
    public static void main(String[] args) {
        try {
            InheritPrivateTest ipInstance =  InheritPrivateTest.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ;
    }
}
