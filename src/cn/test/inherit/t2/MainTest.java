package cn.test.inherit.t2;

import cn.test.inherit.t1.InheritPrivateTest;

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
