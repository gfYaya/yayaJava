package jvm.my.yaya.chatper2;

// Java虚拟机规范 SE8
public class CharTest {
    public char a;
    public double ozero = +0.0;
    public double nzero = -0.0;


    public static void main(String[] args) {
        //JVM 规范说 char 默认值 是Unicode的null码点('\u0000')
        System.out.println(new CharTest().a);
        System.out.println(new CharTest().a == ' ');
        System.out.println(new CharTest().a == '\0'); //unicode null码点

        //对 JVM的 正数0 和负数0的 测试
        CharTest test = new CharTest();
        System.out.println(test.ozero == test.nzero);
        System.out.println(Double.toHexString(test.ozero));
        System.out.println(Double.toHexString(test.nzero));
    }
}

