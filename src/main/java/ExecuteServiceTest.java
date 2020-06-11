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
public class ExecuteServiceTest {

    public void doJs2Logs(HttpServletRequest req, HttpServletResponse resp) {
        String data = req.getParameter("data");
        List<SystemClientLogsPO> logs = JSONObject.parseArray(data, SystemClientLogsPO.class);

        MultiThreadExecutor multiThreadExecutor = new MultiThreadExecutor(logs);
        multiThreadExecutor.execute();

        return;
    }
}
