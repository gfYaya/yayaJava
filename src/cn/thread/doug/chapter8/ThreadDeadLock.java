package cn.thread.doug.chapter8;

import java.util.concurrent.*;

/*
 * Page 139
 * the result:如果发生死锁,任务将一直处于停滞,无任何输出,除了 "RenderPageTask 依赖LoadFileTask任务返回的结果..."
 */
public class ThreadDeadLock {
    ExecutorService exec = Executors.newSingleThreadExecutor();

    /**
     * 该任务会提交另外一个任务到线程池，并且等待任务的执行结果
     * @author Yaya Match
     */
    public class RenderPageTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("RenderPageTask 依赖LoadFileTask任务返回的结果...");
            Future<String> header,footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();

            //将发生死锁 --- 由于任务正在等待子任务的结果 => 目前只能执行一个任务,
            // 可是这个任务如果返回值不执行完,该任务无法执行结束,线程池无法给第二个任务让出位置
            return header.get()+page+footer.get();
            //return "abc";  // ==> get是产生死锁的关键,如果只是return literal ,没使用get ,就不会产生死锁.
        }

        public String renderBody(){
            return "render body is ok.";
        }
    }

    public static void main(String[] args) {
        ThreadDeadLock lock = new ThreadDeadLock();
        Future<String> result = lock.exec.submit(lock.new RenderPageTask());
        try {
            System.out.println("last result:"+result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }finally{
            lock.exec.shutdown();
        }
    }

}

class LoadFileTask implements Callable<String> {
    private String fileName;
    public LoadFileTask(String fileName){
        this.fileName = fileName;
    }

    @Override
    public String call() throws Exception {
        System.out.println("LoadFileTask execute call..."+ this.fileName);
        return fileName;
    }
}
