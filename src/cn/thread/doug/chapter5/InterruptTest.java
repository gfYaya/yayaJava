package cn.thread.doug.chapter5;

import java.util.concurrent.TimeUnit;

/* coding by Intopass
 * Page 78
 */
public class InterruptTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (!Thread.interrupted()) { //currentThread().isInterrupted(true); true =>clear the interrupted flag
                System.out.println("Running");
            }
            System.out.println("Done");
        });
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
            //TimeUnit.SECONDS.sleep(100); //never stop
        } catch (InterruptedException e) {
            e.printStackTrace();//there is no exception in the runtime , by the end, the console print "Done".
        }

        thread.interrupt();
    }

}
