package cn.my.yaya.test4justjava.generic;

/**
 * Created by Yaya on 2017/4/24.
 */
public class GenericTest2 {
    public <T> void  print(T t){ // <T> 可以表名该方法是个泛型方法 如果去掉<T> ,则会报错,形参T 未定义类型

    }

    public <T> void  print(){

    }
}
