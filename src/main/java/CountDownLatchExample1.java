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
public class CountDownLatchExample1 {
    public static void main(String[] args) throws InterruptedException {
        final long time = System.currentTimeMillis();
        final String url = "http://localhost/user/";
        int len = 10;
        final CountDownLatch latch = new CountDownLatch(len);
        for (int i = 0; i < len; i++) {
            final int id = i;
            new Thread(new Runnable() {
                public void run() {
                    latch.countDown();
                    //HttpClientUtil.sendGet(url +  );
                    long usetime = System.currentTimeMillis() - time;
                    System.out.println("到第" + id + "个请求已用时：" + usetime / 1000 + "秒");
                }
            }).start();
        }
        latch.await();
    }

}
