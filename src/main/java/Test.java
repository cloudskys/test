import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
/**
 * @author
 *
 * Semaphore用来做特殊资源的并发访问控制是相当合适的，如果有业务场景需要进行流量控制，可以优先考虑Semaphore。
 * @create 2018-06-03 下午4:21
 **/
public class Test {
    private static Semaphore semaphore = new Semaphore(5);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i=0;i<10;i++){
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("start");
                        semaphore.acquire();
                        System.out.println("do");
                        Thread.sleep(10000);
                        semaphore.release();
                        System.out.println("release");
                    }catch (Exception e){

                    }
                }
            });
        }
        pool.shutdown();
    }
}
