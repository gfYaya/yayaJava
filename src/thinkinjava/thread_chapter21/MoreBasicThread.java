package thinkinjava.thread_chapter21;

/**
 * Thinking in Java <Verstion4>, Page 654 --> segment 1118
 */
public class MoreBasicThread {
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++){
            new Thread(new LiftOff()).run();//与书上不一致 , 多处理器由线程调度器自己控制
        }
        System.out.println("Waiting for LiftOff");
    }
}
