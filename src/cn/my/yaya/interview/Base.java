package cn.my.yaya.interview;

/**
 *
 */
public class Base {
    private String baseName = "base";
    public Base()
    {
        callName();
    }

    public void callName()  //null
    //private void callName() //base
    {
        System. out. println(baseName);
    }

    static class Sub extends Base
    {
        private String baseName = "sub";
        public void callName()
        {
            System. out. println (baseName) ;
        }
    }
    public static void main(String[] args)
    {
        Base b = new Sub();
        //调用父类的构造器,但是父类中的构造器执行的callName方法却是子类的,如果是将父类的callName改为private 便输出base
        //充分说明,构造器也是要符合多态特性的
        /*
         所谓的多态,就是jvm虚拟机的一个指令而已,如果是private或final的，就无法被覆盖了.
         虚拟机会换一个指令invokevirtual与invokespecial.invokevirtual的话，就会某个地方查找方法,
         invokespecial的话，就是写死了某个方法.
         显然 前者是多态,后者 属于前期绑定
        */
        Sub sub = new Sub();
        Base base = new Base();
    }

}
