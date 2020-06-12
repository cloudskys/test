/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: CountDownLatchTest
 * Author:   Administrator
 * Date:     2020/6/10 11:12
 * Description: CountDownLatch 示例
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈一句话功能简述〉<br> 
 * 〈CountDownLatch 示例〉
 *
 * @author Administrator
 * @create 2020/6/10
 * @since 1.0.0
 */
public class CountDownLatchExample {
    public static void concurrenceTest() {
        /**
         * 模拟高并发情况代码
         */
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        int length = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(length); // 相当于计数器，当所有都准备好了，再一起执行，模仿多并发，保证并发量
        final CountDownLatch countDownLatch2 = new CountDownLatch(length); // 保证所有线程执行完了再打印atomicInteger的值
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        try {
            for (int i = 0; i < length; i++) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            countDownLatch.await(); //一直阻塞当前线程，直到计时器的值为0,保证同时并发
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //每个线程增加1000次，每次加1
                        for (int j = 0; j < 100000; j++) {
                            atomicInteger.incrementAndGet();
                        }
                        countDownLatch2.countDown();
                    }
                });
                countDownLatch.countDown();
            }

            countDownLatch2.await();// 保证所有线程执行完
            System.out.println(atomicInteger);
            executorService.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        concurrenceTest();
    }
}
