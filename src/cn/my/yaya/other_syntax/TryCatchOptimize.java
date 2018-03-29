package cn.my.yaya.other_syntax;

/**
 * Java线程池运行一个 Thread/Runnable,每个任务一个 run方法,想要把run中所有的异常全部捕获并不造成任务因为异常而退出,并能记录异常
 * 信息和日志,直接的方法是 run整个函数体都使用 try-catch,但是却不够美观.尝试使用Thread.setDefaultUncaughtExceptionHandler解决.
 * 能生成Annotation更好
 */
public class TryCatchOptimize {

    public static void main(String[] args) {
        Thread t = new Thread(new AdminThread());
        //t.setDefaultUncaughtExceptionHandler(new ExceptionHandler()); //对当前线程设置默认catch
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        t.start();
    }

}

class AdminThread implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("start ... Exception");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new NullPointerException(); //直接exception
    }
}

class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) { //调用此方法来进行，对异常处理，需要实现UncaughtExceptionHandler 接口
        System.out.println("Thread:" + t + " Exception message:" + e);
    }

}
