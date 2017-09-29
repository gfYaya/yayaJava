package cn.my.yaya.test4justjava.reflection;

/**
 * Created by Yaya on 2016/5/19.
 * Java编程思想 V4,p315改进版
 *  同时验证 static代码块实在类接在的时候执行
 *  result:(1)只执行part1部分:执行结果只有 "loading Gum" ,说明Class.forName()不会触发类的初始化,但是会创建Class的引用
 *    其次static静态代码块在类的加载时候就自动执行了,与类的初始化无关(类的加载是JVM在收到"类的引用"的调用便触发了,属于动态加载)
 *         (2)只执行part2部分:结果 先显示"loading Gum" ,后显示"init Gum" ,算是应证可上一个结论
 *         (3)只执行part3部分:结果 只显示"gum",也就是说,static final属于"编译期常量",不会触发类的加载和初始化
 */

class Candy {
    static {
        System.out.println("loading Candy");
    }
}

class Gum {
    public static final String gum="Gum";

    public Gum(){
        System.out.println("init Gum");
    }

    static {
        System.out.println("loading Gum");
    }
}

class Cookie {
    static {
        System.out.println("loading Cookie");
    }
}

public class SweetShop {
    public static void main(String[] args) {
        //Part 1
//        try{
//            Class.forName("cn.test.reflection.Gum");
//        }catch(ClassNotFoundException e){
//            System.out.println("Can't find Gum");
//        }

        //Part 2
//        Gum gum = new Gum();  //init Gum

        //Part 3
        System.out.println(Gum.gum);
    }

}

//loading Gum
