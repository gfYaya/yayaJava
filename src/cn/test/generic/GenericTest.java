package cn.test.generic;

/**
 * 关于泛型参数列表的 测试,是否会影响返回值和函数的泛型参数限制
 */
public class GenericTest<T> {
    private T t;

    //public  void  setT(T t){
    //public  <String>  T setT(T t){
    public  <T>  T setT(T t){
        /*
        如果函数声明了<T>那么其实类的同名范型是会被隐藏起来的，而这个T和类的T不是一个T，
        this.t的类型是类型的T，函数的t类型是函数的T，不是一个T。            -- Intopass@zhihu
         */
        //.this.t = t;  //Error   函数的参数 和泛型的定义 不一致 ,导致报错
        return null;
    }
}
