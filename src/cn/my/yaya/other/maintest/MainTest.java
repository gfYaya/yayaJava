package cn.my.yaya.other.maintest;

import java.nio.file.Files;

//测试一个类的main方法 能否调用另一个类的 main方法
//result : 可以的
public class MainTest {
    public static void main(String[] args) {
        System.out.println("Main starting..");
        InvokeFunc.main(args);
    }
}
