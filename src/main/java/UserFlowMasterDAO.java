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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈一句话功能简述〉<br> 
 * 〈CountDownLatch 示例〉
 *
 * @author Administrator
 * @create 2020/6/10
 * @since 1.0.0
 */
public interface UserFlowMasterDAO {
    List<Map<String, Integer>> getAllBrokerUser();

    List<Map<String, Integer>> getMemberRecommends(int pageNo, int pageSize);

    /**
     * 插入流量表
     * @param superiorId :
     * @param userId :
     * @param i :
     * @param date :
     * @param date1 :
     * @return int :
     **/
   // int insertFlowMaster(@Param("flowMasterId") Integer superiorId, @Param("flowId") Integer userId, @Param("state") int i, @Param("createDate") Date date, @Param("updateDate") Date date1);

    /**
     * 查找推荐人
     *
     **/
   // Map<String, Integer> findReferrer(@Param("userId") Integer userId);

    /**
     * 查找流量主
     * @param userId :
     * @return com.heyou.entity.user.UserFlowMaster :
     **/
   // List<UserFlowMaster> getFlowMasterByUserId(@Param("userId") Integer userId);

    /**
     * 查找流量主的子集
     * @param flowMasterId
     * @return
     */
    List<Integer> selectFlowByMaster( Integer flowMasterId);

    /**
     * 当前用户是不是B(含大B小B)
     * @param userId :
     * @return int :
     **/
    int isBrokerRoleByUserId(Integer userId);

    /**
     * 当前用户是不是流量主（小B）
     * @param userId
     * @return
     */
    int isMinBrokerRoleByUserId(Integer userId);

    void insertFlowMasters(List<Map<String, Object>> valuesList);

    /**
     * 查询流量主的流量
     * @param flowMasterId
     * @param bindingTime
     * @return
     */
    //List<FlowMasterVO> getFlowMasterFlow(@Param("flowMasterId") int flowMasterId, @Param("bindingTime") String bindingTime);

    /**
     * 根据流量主id 和流量id 查询出绑定时间
     * @param flowMasterId
     * @param flowId
     * @return
     */
   // Date getFlowTime(@Param("flowMasterId") Integer flowMasterId, @Param("flowId") Integer flowId);

    List<Map<String, Integer>> getMemberRecommendsByDate( Date date,  Date time);

    //void updateMemberRecommend(@Param("superiorId") Integer superiorId, @Param("userId") Integer userId, @Param("testField") String testField, @Param("testField2") String testField2);

    Integer countMemberRecommend();
}
