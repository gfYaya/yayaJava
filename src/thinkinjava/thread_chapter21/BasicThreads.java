package thinkinjava.thread_chapter21;

/**
 * Thinking in Java <Verstion4>, Page 655 --> segment 1118.
 */
public class BasicThreads {
    public static void main(String[] args)  {
        Thread t = new Thread(new LiftOff());
        t.start();
        /*try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("Waiting for LiftOff"); // first print
    }
}
