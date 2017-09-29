package cn.my.yaya.test4justjava.string;

/**
 * Created by Yaya on 2017/5/26.
 * 说明:http://blog.csdn.net/seu_calvin/article/details/52291082
 * JDK 6时,执行intern(),如果常量池已含有此literal,则返回该常量池中的literal的引用,否则,先于常量池中创建该literal,再返回引用;
 * JDK 7时,有些不同,如果常量池已经含有literal(JDK 7,常量池移动到堆内存中,new String()的时候,会同时在常量池中创建一个literal,同时并在堆内存中创建这个对象).对于常量池中
 * 含有该literal时,处理的方式与JDK 6无异,但是不含有时,常量池中不需要再存储一份对象了，直接存储堆中的引用。
 */
public class InternTest {
    public static void main(String[] args) {
        String s2 = new StringBuilder("ja").append("va").toString();
        System.out.println(s2.intern() == s2 );

        String s1 = new StringBuilder("go").append("od").toString();
        System.out.println(s1.intern() == s1);

        String s3 = new StringBuilder("Ja").append("va").toString();
        System.out.println(s3.intern() == s3 );

        String s4 = new StringBuilder("Go").append("od").toString();
        System.out.println(s4.intern() == s4);

        String a1= "ab";
        String a2= "a" + "b";
        System.out.println(a1 == a2); //JDK 6和 8:true

        String b1=new String("ab");
        String b2= new String("a") + "b";
        System.out.println(b1 == b2); //JDK 6和 8:false

        final String c1=new String("ab");
        final String c2= new String("a") + "b";
        System.out.println(c1 == c2); //JDK 6和 8:false
    }
}
