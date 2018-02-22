package jvm.my.yaya.other;

//判断字段名称在常量池中是否会被重用
//result: 不会,从结果上来看 只会用到descriptor中的内容 name_index之类的常量池引用并不会被使用
public class FieldNameReuseInConstantPool {
    private static String test = "hh";//名称为test

    public static void main(String[] args) {
        String a = new StringBuffer("te").append("st").toString();
        System.out.println(a.intern() == a);
    }
}
