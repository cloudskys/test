import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SemaphoreServiceDemo {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10000);
    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
    ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 15, 600, TimeUnit.SECONDS, queue, handler);

    private static Semaphore semaphore = new Semaphore(10);

    private static Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

    private static AtomicLong totalDeal = new AtomicLong(0);
    public void initFlowMaster() throws InterruptedException {
        // 获取所有的B端用户， 有店铺 或者 R店
        List<Map<String, Integer>> bUser = flowMasterDAO.getAllBrokerUser();
        List<Integer> bUserIds = bUser.stream().map(x-> x.get("userId")).collect(Collectors.toList());
        // 推荐人的 流量主缓存
        int pageNo = 1;
        int pageSize = 2000;
        Integer total = flowMasterDAO.countMemberRecommend();
        //log.info("member_recommand total : " + total);
        Integer totalPage = total / pageSize + 10;
       // log.info("member_recommand totalPage : " + totalPage);
        while(pageNo < totalPage) {
            semaphore.acquire();
            pool.execute(new InitFlowMaster(pageNo, pageSize, bUserIds));
            //log.info("deal with member_recommand  page : " + pageNo);
            pageNo++;
        }
    }


    class InitFlowMaster implements  Runnable {

        private Date date;

        private List<Integer> bUserIds;

        private Integer pageNo;

        private Integer pageSize;


        public InitFlowMaster (Date date, List<Integer> bUserIds) {
            this.date = date;
            this.bUserIds = bUserIds;
        }

        public InitFlowMaster (Integer pageNo, Integer pageSize,List<Integer> bUserIds) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.bUserIds = bUserIds;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            findFlowMasterFromMemberRecommend(pageNo, pageSize, bUserIds);
        }
        private void findFlowMasterFromMemberRecommend(int pageNo, int pageSize, List<Integer> bUserIds){
            List<Map<String, Integer>> memberRecommends = flowMasterDAO.getMemberRecommends((pageNo-1) * pageSize, pageSize);
            // 如果没有结果集直接返回
            if (memberRecommends == null || memberRecommends.size() == 0) {
                log.info("pageNo = " + pageNo + " pageSize = 0");
                return;
            }
            log.info("pageNo = " + pageNo + " pageSize = " + memberRecommends.size());
            log.info("total deal with = " + totalDeal.addAndGet(memberRecommends.size()));
            List<Map<String, Object>> valuesList = new ArrayList<>();
            foreachList(memberRecommends, bUserIds, valuesList);
            insertFlowMaster(null, null, valuesList, true);
            semaphore.release();
        }
    }

}