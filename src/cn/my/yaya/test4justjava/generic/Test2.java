package cn.my.yaya.test4justjava.generic;

import java.util.ArrayList;

/**
 * Created by Yaya on 2017/4/24.
 *  参考demo: http://blog.csdn.net/lonelyroamer/article/details/7868820
 */
public class Test2 {

    public static <T> T pick(T x, T y){
        return y;
    }

    public static void main(String[] args) {
        // 不指定泛型的时候
        int i = Test2.pick(1, 2); // 两个参数都是Integer类型,所以T是 Integer类型
        Number f = Test2.pick(1, 1.2); // 这两个参数一个是Integer,另一个是Float类型,所以取同一父类的最小级 Number
        Object o = Test2.pick(1, "asd"); // 这两个参数一个是Integer, 另一个是String类型, 所以取同一父类的最小级 Object
        // Number n = Test2.pick(2, "zxc"); //Error:类型不匹配,不一致,泛型不支持向下转型的原因?

        // 指定泛型的时候
        int a = Test2.<Integer>pick(1 ,2); // 指定为Integer了,所以传入的参数只能是Integer或者其子类
        //int b = Test2.<Integer>pick(1, 2.2); //argument: 2.2 位置 编译报错,指定了Integer,不能为Float
        Number c = Test2.<Number>pick(1 ,2.2); //指定为Number,所以可以为Integer和Float

        // 在泛型类中不指定泛型,泛型类型为Object
        ArrayList arrayList = new ArrayList();
        arrayList.add(1);
        arrayList.add(2.2);
        arrayList.add("asd");

    }

}
