package jvm.my.yaya.chapter3;

/*
 * dadd操作是否支持多个操作数,一个操作栈的数全部可参与dadd执行?
 * result: 一个函数只有一个，长度根据需要
 *         每个指令要使用的操作数是一定的
 */
public class DoubleAddBytecodeTest {
    public static void main(String[] args) {
        double a = 1.0;
        double b = 2.0;
        double c = 3.0;
        double d = a+b+c;
        //使用了 两个dadd 操作
    }
}
