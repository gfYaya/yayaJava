package jvm.my.yaya.chatper2;

/**
 * Java虚拟机规范 2.11.4 :
 * 窄化类型转换可能会导致转换结果产生不同的正负号、不同的数量级，转换过程很可能会导致数值丢失精度.
 */
public class NarrowCastTest {
    public static void main(String[] args) {
        System.out.println((byte)128); // -127
        //如果转换结果v的值太小（包括足够小的负数以及负无穷大的情况），
        //无法使用T类型表示的话，那转换结果取int或long类型所能表示的最小数字。
    }
}
