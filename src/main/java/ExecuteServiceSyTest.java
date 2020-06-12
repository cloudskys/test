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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br> 
 * 〈CountDownLatch 示例〉
 *
 * @author Administrator
 * @create 2020/6/10
 * @since 1.0.0
 */
public class ExecuteServiceSyTest {

    public void doJs2Logs(HttpServletRequest req, HttpServletResponse resp) {
        String data = req.getParameter("data");
        //List<SystemClientLogsPO> logs = JSONObject.parseArray(data, SystemClientLogsPO.class);

        //MultiThreadExecutor multiThreadExecutor = new MultiThreadExecutor(logs);
       // multiThreadExecutor.execute();

        return;
    }
    class MultiThreadExecutor<T> {

        /**
         * 每个线程处理记录数
         */
        private static final int EXEC_COUNT_PER_THREAD = 1000;

        /**
         * 总线程数
         */
        private int threadCount = Runtime.getRuntime().availableProcessors() * 2;

        /**
         * 线程对象
         */
        private ExecutorService excutors = null;

        /**
         * 每个线程处理记录数
         */
        private int splitCount = 0;

        /**
         * 线程处理的数据
         */
        private List<T> data = null;

        /**
         * 初始化构造器
         *
         * @param data
         */
        public MultiThreadExecutor(List<T> data) {
            this(EXEC_COUNT_PER_THREAD, data);
        }

        /**
         * 初始化构造器
         *
         * @param splitCount 线程处理数
         * @param data       处理的数据
         */
        public MultiThreadExecutor(int splitCount, List<T> data) {
            this.splitCount = splitCount;
            this.data = data;
            this.excutors = Executors.newFixedThreadPool(threadCount);
        }

        /**
         * 执行
         */
        public void execute() {
            if (data == null || data.size() <= 0) {
                return;
            }

            // 调用服务处理，形成多线程事务执行
            int count = (int) Math.ceil(data.size() * 1.0 / splitCount);

            //logger.info(String.format("总计%s条数据，每批%s条，分割成%s批，线程数为%s。", data.size(), splitCount, count, threadCount));

            for (int i = 0; i < count; i++) {
                // 数据分批
                final List<T> d = data.subList(i * splitCount, (i + 1) * splitCount);
                // 执行
                excutors.execute(new Runnable() {
                    public void run() {
                        //int ret = systemClientLogsService.insertClientLogs((List<SystemClientLogsPO>) d);
                       // logger.info(String.format("线程%s，插入%s条，成功%s条。", Thread.currentThread().getId(), d.size(), ret));
                    }
                });
            }
        }

    }
}
