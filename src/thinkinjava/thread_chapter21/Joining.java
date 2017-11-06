package thinkinjava.thread_chapter21;

/**
 * thinking in Java <Verstion4>, Page 670 --> segment 1143
 */
class Sleeper extends Thread{
    private int duration;
    public Sleeper(String name,int sleepTime){
        super(name);
        duration =sleepTime;
        start();
    }

    public void run(){
        try {
            sleep(duration);//挂起sleeper
        } catch (InterruptedException e) {
            System.out.println(getName()+ " was interrrupted. " + "isInterrupted(); " + isInterrupted());
            return;
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread{
    private Sleeper sleeper;
    public Joiner(String name,Sleeper sleeper){
        super(name);
        this.sleeper = sleeper;
        start();
    }

    public void run(){
        try {
            sleeper.join();//挂起当前线程 Joiner ,让权给sleeper
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(getName() + " join compeleted");
    }
}


public class Joining {
    public static void main(String[] args) {
        Sleeper sleepy = new Sleeper("Sleepy",1500),grumpy = new Sleeper("Grumpy",1500);//150000也是如此执行结果
        Joiner dopey = new Joiner("Dopey",sleepy),doc = new Joiner("Doc",grumpy);
        grumpy.interrupt();
    }
}
