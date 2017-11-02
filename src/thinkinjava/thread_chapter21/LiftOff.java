package thinkinjava.thread_chapter21;

/**
 * Thinking in Java <Verstion4>, Page 654   -->segment:1116
 */
public class LiftOff implements Runnable {

    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount++;
    //private final int id = ++taskCount;
    public LiftOff(){

    }

    public LiftOff(int countDown){
        this.countDown = countDown;
    }

    public String status(){
        return "#" + id + "(" +(countDown > 0 ? countDown : "Liftoff!") + "), ";
    }
    @Override
    public void run() {
        while(countDown -- > 0){
            System.out.print(status());
            Thread.yield();
        }
    }
}
