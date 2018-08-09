package cn.my.yaya.other.accessPriority.b;

import cn.my.yaya.other.accessPriority.a.Aclass;

public class Cclass extends Aclass {
    public void show() {
        Aclass aa = new Aclass();
        aa.showA1();
        aa.showA2();
        aa.showA3();
        aa.showA4();

        this.showA1();
        this.showA2();
        this.showA3();
        this.showA4();

        System.out.println(this.a1);
        System.out.println(this.a2);
        System.out.println(this.a3);
        System.out.println(this.a4);

        System.out.println(aa.a1);
        System.out.println(aa.a2);
        System.out.println(aa.a3);
        System.out.println(aa.a4);
    }
}
