package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.SendTaskToTB;

/**
 * 批量操作
 * create by wangmindong
 * create timer 2013-03-25
 *
 */
public interface OrderStatusUpdService {

	/**
     * 更新订单状态
     * @param
     * 		list 订单状态更新list
     * 		questionList 问题件状态更新list
     * 		
     * @return boolean
     */
    
    public boolean batchUpdateOrdStatus(List<UpdateInfo> list,List<UpdateInfo> questionList);
    
    /**
     * 批量添加task 到 ec_core_taskToTB
     * @param list
     * @return boolean
     */
    public boolean batchAddTask2TB(List<SendTaskToTB> list);
}
