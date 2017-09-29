package com.company.yaya;

/**
 * Created by Yaya on 2016/7/31.
 */
class A
{
    public static int x=2;
    public static void printa()
    {
        System.out.println("A:"+x);
    }
}


class B extends A
{
    public static int x=100;
    public static void printb()
    {
        System.out.println("B:"+x);
    }

    public static void printa()
    {
        System.out.println("B:"+(100+x));
    }

}

public class Example
{
    public static void main(String[] args)
    {
        A a1 = new A();
        a1.printa();

        B b1 = new B();
        b1.printa();
        b1.printb();

        System.out.println("second");
        A a2 = new B();
        a2.printa();
        //a2.printb();  =>syntex error
    }
}
