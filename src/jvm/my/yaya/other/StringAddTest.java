package jvm.my.yaya.other;

//javap -v 测试该类中的constant pool是否存在 "ab"
// result 依然么有
public class StringAddTest {
    public String a1 = "a";
    public String b2 = "b";

    public static void main(String[] args) {
        StringAddTest s = new StringAddTest();
        System.out.println(s.a1 + s.b2);
    }
}
