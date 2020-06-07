import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    // number of clients
    public static int clientCount = 6;

    // number of windows
    public static int windowsCount = 3;

    // simulate random processing time
    static Random rand = new Random();
    // date format
    static SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void main(String[] args) throws Exception {
        System.out.println("-----------------------------------------------------------");
        System.out.println("     Time        Thread Name    Client ID   Message");
        System.out.println("-----------------------------------------------------------");
        ExecutorService executorService = Executors.newCachedThreadPool();
        // Another constructor: new Semaphore(permits, fair), where
        // - permits: the initial number of permits available.
        // - boolean faire: true if this semaphore will guarantee first-in first-out granting
        //   of permits under contention, else false.
        Semaphore semaphore = new Semaphore(windowsCount);
        CountDownLatch countDownLatch = new CountDownLatch(clientCount);
        for (int i = 0; i < clientCount; i++) {
            final int clientID = i + 1;
            executorService.execute(() -> {
                try {
                    // Acquire a permit. A new client will be waited if no window available.
                    semaphore.acquire();
                    // Actual workload lies here.
                    process(clientID);
                    // Release the window so that other client can use.
                    semaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        // Forces the main thread to be waited until all processes are done.
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("-----------------------------------------------------------");
    }

    private static void process(int clientID) throws InterruptedException {
        String tname = Thread.currentThread().getName();
        Date d1 = new Date();
        System.out.printf("%s   %s      %d       Begin.\n", f.format(d1), tname, clientID);
        Thread.sleep(rand.nextInt(800) + 200);
        Date d2 = new Date();
        System.out.printf("%s   %s      %d       Done in %d ms.\n", f.format(d2), tname, clientID,
                (d2.getTime() - d1.getTime()));
    }
}