package thinkinjava.reflect;

import javax.xml.bind.SchemaOutputResolver;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Yaya on 2017/9/4.
 * Java 编程思想第四版 P338-339  动态代理
 */

//前奏 普通的代理

interface Interface {
    void dosomething();

    void somethingElse(String args);
}

class RealObject implements Interface {
    public void dosomething() {
        System.out.println("dosomething");
    }

    public void somethingElse(String args) {
        System.out.println("somethingElse" + args);
    }
}

//动态代理
class DynamicProxyHandler implements InvocationHandler {
    Object proxied;

    //在我看来,仅仅用来传参
    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    /*public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }*/

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("*******proxy: " + proxy.getClass() + ", method: " + method + ", args: " + args);
        if (args != null) {
            for (Object arg : args) {
                System.out.println("  " + arg);
            }
        }
        return null; // 如果为null,则后续的interface的函数并没有调用
        //return method.invoke(proxied, args);
    }
}

class SimpleDynamicProxy {
    public static void consumer(Interface iface) {
        iface.dosomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        RealObject real = new RealObject();
        //consumer(real);
        //每次调用proxy对象的方法,都会执行invoke?
        Interface proxy = (Interface) Proxy.newProxyInstance(
        Interface.class.getClassLoader(),
        new Class[]{Interface.class},
        new DynamicProxyHandler(real) );
        consumer(proxy);
    }
}
