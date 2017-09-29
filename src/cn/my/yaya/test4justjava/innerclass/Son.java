package cn.my.yaya.test4justjava.innerclass;

/**
 * Created by Yaya on 2017/5/18.
 * 测试:内部类属于外部类的属性一部分,那么父类的内部类能否被子类继承? 经过证实 内部类也属于父类的属性的一部分,也会被继承.;
 */
class Father {

    public class Play {
        public int p = 1;

        public void soutPlay() {
            System.out.println(this.p);
        }
    }

    public static class Hello {

    }
}

public class Son extends Father {

    public static void main(String[] args) {
        Father father = new Father();
        Father.Play play1 = father.new Play();

        Son son = new Son();
        Son.Play play = son.new Play();
        System.out.println(Son.Play.class.getName()); //cn.test.innerclass.Father$Play
        System.out.println(Hello.class.getName()); //cn.test.innerclass.Father$Hello

        play1.p += 1;
        System.out.println(play.p); //依然是1,说明没直接关系,虽然内部类的反射路径依然是父类的
    }

}