package cn.my.yaya.test4justjava.other;

/**
 * Created by Yaya on 2017/6/5.
 * 测试实例变量如何实例化会造成堆内存溢出
 */
public class Test {
    //public Test t = new Test();
    public static Test t ;//如果 去掉构造器中的信息,改为 t = new Test();就不会报出堆内存溢出
    public Test(){
        t = new Test();
    }

    public static void main(String[] args) {
        Test test = new Test(); //堆内存溢出 Exception in thread "main" java.lang.StackOverflowError  无论变量t 是否是static
    }
}
