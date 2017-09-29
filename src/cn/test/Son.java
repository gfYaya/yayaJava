package cn.test;

/**
 * Created by Yaya on 2017/4/28.
 * 上抓行对象属性测试
 */
public class Son extends Parent {
    public String name = "S";  //如果子类重新覆盖了父类的属性,上转型对象调用的是父类的属性

    public String getName(){
        return this.name;   //如果子类重新覆盖了父类的方法,上转型对象调用的是子类的方法
    }

    public static void main(String[] args) {
        Parent p = new Son();
        System.out.println(p.name);  // A
        System.out.println(p.getName());  //S
    }

}
