package cn.my.yaya.other.accessPriority.b;

import cn.my.yaya.other.accessPriority.a.Aclass;

public class Cclass extends Aclass {

    public void show() {
        Aclass aa = new Aclass();
        //aa.showA1(); //error
        //aa.showA2(); //error
        //aa.showA3(); //error
        aa.showA4();

        //this.showA1(); //error
        //this.showA2(); //error
        this.showA3();
        this.showA4();

        //System.out.println(this.a1);  //error
        //System.out.println(this.a2);  //error //之所以报错,是因为 等同于没继承,因为父类的属性无访问权限,等同于看不到,自然就相当于没继承过来
        System.out.println(this.a3);
        System.out.println(this.a4);

        //System.out.println(aa.a1);  //error
        //System.out.println(aa.a2);  //error
        //System.out.println(aa.a3);  //error
        System.out.println(aa.a4);

        Cclass cc = new Cclass();
    }
}
