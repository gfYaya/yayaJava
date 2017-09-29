package cn.my.yaya.test4justjava.inherit;

/**
 * Created by Yaya on 2017/6/5.
 * 关于方法重写和重载验证
 * result:方法重写,方法返回值不参与比较判定
 *        方法重载,父子之间参数列表相同的方法返回值也必须相同,所以interface接口声明的函数也要有返回值类型(设计本身如此)
 */
public class Father {
    //public void a(){}  //显然 返回类型不参与方法重载比较
    public int a(){
        return 0;
    }

    public void a(String a){

    }
}

class Son extends Father{

    //public void a(){}  //报错, void返回类型不允许,显然返回类型也参与方法重写比较.方法重写 比较的也是参数列表,只不过对返回类型有限制
    public int a(){
        return 1;
    }
}
