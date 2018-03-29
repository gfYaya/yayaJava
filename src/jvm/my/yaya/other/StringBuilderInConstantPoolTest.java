package jvm.my.yaya.other;

//测试 stringbuilder中的 字符串 是否在常量池中
public class StringBuilderInConstantPoolTest {
    public static void main(String[] args) {
        System.out.println(new StringBuilder("a").append("b"));
        //System.out.println("a" + "b");
    }
}
