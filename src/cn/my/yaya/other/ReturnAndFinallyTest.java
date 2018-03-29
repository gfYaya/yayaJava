package cn.my.yaya.other;

// JVM 虚拟机规范中提到 在try执行让权和转义操作之前,会先执行finally代码块,进行验证
public class ReturnAndFinallyTest {

    public static String callback(){
        try{
            return "a";
        }finally{
            return "b";
        }
        // return "c";  //unreachable statement
    }

    public static void main(String[] args) {
        System.out.println(callback()); //b
    }
}
