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

/**
 * 〈一句话功能简述〉<br> 
 * 〈CountDownLatch 示例〉
 *
 * @author Administrator
 * @create 2020/6/10
 * @since 1.0.0
 */
public class CountDownLatchTest {
    public void initFlowMaster(String threadCount) throws InterruptedException {
        ConcurrentLinkedQueue<Map<String, Object>> datas = new ConcurrentLinkedQueue<Map<String, Object>>();
        datas.addAll(notExistsList);
        // 多线程处理
        int threadPoolSize;
        if(threadCount != null && threadCount.length() > 0) {
            threadPoolSize = Integer.valueOf(threadCount);
        } else {
            threadPoolSize = Runtime.getRuntime().availableProcessors() * 4;
        }

        final CountDownLatch count = new CountDownLatch(threadPoolSize);
        ExecutorService es = Executors.newFixedThreadPool(threadPoolSize);

        // 执行任务
        for (int i = 0; i < threadPoolSize; i++) {
            es.execute(new RegisterAccountThread(datas, i, count, appId, appSecret, imageRootPath));
        }

        // 等待执行完
        try {
            count.await();
        } catch (InterruptedException e) {
            logger.error("线程计数器异常", e);
        }
        long endTime = System.currentTimeMillis();

    }
    public class RegisterAccountThread implements Runnable {

        /**
         * 数据容器
         */
        private ConcurrentLinkedQueue<Map<String, Object>> datas = new ConcurrentLinkedQueue<Map<String, Object>>();

        /**
         * 编号
         */
        private int threadNo = 0;

        /**
         * 线程记录数
         */
        private CountDownLatch count = null;

        /**
         * 网易云信APPKEY
         */
        private String appId = null;
        /**
         * 网易云信APPSECRET
         */
        private String appSecret = null;
        /**
         * 图片服务器目录
         */
        private String imageRootPath = null;

        /**
         * 同步数据记录总条数
         */
        int dataCount = 0;

        /**
         * 同步数据记录条数
         */
        int syncCount = 0;

        public RegisterAccountThread() {
        }

        /**
         * 初始化容器和线程计数器
         *
         * @param datas 数据
         * @param threadNo 线程编号
         * @param count countDown
         * @param appId 固定参数
         * @param appSecret 固定参数
         * @param imageRootPath 固定参数
         */
        public RegisterAccountThread(ConcurrentLinkedQueue<Map<String, Object>> datas, int threadNo,  CountDownLatch count, String appId, String appSecret, String imageRootPath) {
            this.threadNo = threadNo;
            this.count = count;
            this.datas = datas;
            this.appId = appId;
            this.appSecret = appSecret;
            this.imageRootPath = imageRootPath;
        }

        public void run() {
            try {
                execute();
            } finally {
                this.count.countDown();
            }
            logger.info(String.format("=============线程【%s】同步数据结束总计：%s/%s条。====================", threadNo, syncCount, dataCount));
        }

        /**
         * 执行
         */
        public void execute() {
            Map<String, Object> uinfo = null;
            while ((uinfo = datas.poll()) != null) {
                // 重构参数
                Map<String, Object> uinfoTmp = new HashMap<String, Object>();
                String userId = String.valueOf(uinfo.get("userId"));
                uinfoTmp.put("userId", userId);
                uinfoTmp.put("userName", uinfo.get("userName"));
                uinfoTmp.put("avatar", uinfo.get("avatar"));
                String mobilephone = StringUtils.toString(uinfo.get("mobilephone"));
                if(StringUtils.isMobilephoneCN(mobilephone)) {
                    uinfoTmp.put("mobilephone", mobilephone);
                }
                uinfoTmp.put("imgRootPath", imageRootPath);
                dataCount++;
                boolean isTrue = neteaseRequestService.syncNeteaseUser(appId, appSecret, uinfoTmp);
                if(isTrue) {
                    syncCount++;
                    logger.info(String.format("线程【%s】同步数据第%s/%s条，用户Id：%s。", threadNo, syncCount, dataCount, userId));
                } else {
                    logger.info(String.format("线程【%s】同步数据失败，用户Id：%s。", threadNo, userId));
                }
            }
        }
    }
}
