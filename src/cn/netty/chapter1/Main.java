package cn.netty.chapter1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

//P10 区分异步同步 阻塞非阻塞   added by :Intopass
/*  netty的非阻塞 本质还是NIO 的 调用底层操作系统的 多路复用模型 函数返回值为void只是一种表现形式罢了
 * 并不是因为回调函数返回值为void类型,所以才是非阻塞的,为什么一定要用Future.get来接受返回值,让他异步回调函数正常执行,回调的结果我不关心也可以,
 * 所以完成非阻塞本质还是NIO
 */
public class Main {
    public static void main(String[] args) {
        new Main().test();
        new Main().test2();
        new Main().test3();
    }

    // 同步阻塞
    private void test() {
        System.out.println("start test()");
        String result = getResult();
        System.out.println(result);
    }

    // 异步轮询
    private void test2() {
        System.out.println("start test2()");
        AtomicReference<String> result = getAsyncResult();
        //get:Gets the current value.
        System.out.println(result.get());
        threadSleep(3);
        System.out.println(result.get());
    }

    // 异步回调
    private void test3() {
        System.out.println("start test3()");
        getAsyncResult(result -> {
            System.out.println(result);
        });
    }

    public String getResult() {
        threadSleep(2);
        return "Hello";
    }

    public AtomicReference<String> getAsyncResult() {
        AtomicReference<String> result = new AtomicReference<>();
        new Thread(() -> result.set(getResult())).start();
        return result;
    }

    public void getAsyncResult(Consumer<String> callback) {
        new Thread(() -> callback.accept(getResult())).start();
    }

    private void threadSleep(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/*
 稍微改一下，就可以用boolean控制重试了。

    private void test3() {
        AtomicInteger retry = new AtomicInteger(3);
        System.out.println("start test3()");
        getAsyncResult(result -> {
            System.out.println(result);
            return retry.decrementAndGet() > 0;
        });
    }

    public void getAsyncResult(Function<String, Boolean> callback) {
        new Thread(() -> {
            while (callback.apply(getResult())) {
            }
        }).start();
    }
 */

/*
    private void test() {
        System.out.println("start test()");
        getSyncResult(s -> {
            System.out.println(s);
        });
        System.out.println("end test()");
    }

    public void getSyncResult(Consumer<String> callback) {
        callback.accept(getResult());
    }

同步不需要回调
但是可以回调
*/
