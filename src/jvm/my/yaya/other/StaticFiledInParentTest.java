package jvm.my.yaya.other;

/*  为何static 子类调用父类的,子类不会触发<clinit>
 *  https://mp.weixin.qq.com/s?__biz=MzUzNTY4NTYxMA==&mid=2247484350&idx=1&sn=e63e03e5506ab8d64fb28a986ef414cf&chksm=fa80f33bcdf77a2d46110904b47ddf3da58bf4fa88be82e6f4795ae0657f730ffbcc6d9d6297&scene=0#rd
 */
class Parent {
    static int a = 100;

    static {
        System.out.println("parent init！");
    }
}

class Child extends Parent {
    static int a = 200;

    static {
        System.out.println("child init！");
    }
}

public class StaticFiledInParentTest {
    public static void main(String[] args) {
        System.out.println(Child.a);
        //System.out.println(new Child().a);
    }
}
