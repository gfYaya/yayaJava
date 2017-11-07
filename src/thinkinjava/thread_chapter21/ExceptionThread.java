package thinkinjava.thread_chapter21;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * thinking in Java <Verstion4>, Page 672 --> segment 1147
 */
public class ExceptionThread  implements  Runnable{

    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        try{
            ExecutorService exec= Executors.newCachedThreadPool();
            exec.execute(new ExceptionThread());
        }catch(RuntimeException e){
            System.out.println("Exception has been handled");
        }
    }
}
