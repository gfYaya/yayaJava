package cn.my.yaya.test4justjava;

/**
 * Created by Yaya on 2017/9/1.
 * https://www.zhihu.com/question/64763016/answer/223938175
 * 测试:静态类属性 直接访问子类静态属性, 父类与子类的变化
 * result: A ,12 => 可能和static是静态绑定,与正常的对象上转型不太一致
 */

class A {
    static int i = 12;
    static {
        System.out.println("A");
    }
}
class B extends A{
    static {
        System.out.println("B");
    }
}

public class StaticTest {
    public static void main(String[] args) {
        System.out.println(B.i);
    }
}
