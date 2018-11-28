package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.SendTaskToTB;

/**
 * 订单状态批量修改
 * create by wangmindong
 * create timer 2013-03-25
 *
 */
public interface OrderStatusDao {

	
	 /**
     * 批量更新order表订单状态
     * @param list
     * @return
     */
    public boolean batchModifyOrdStatus(List<UpdateInfo> list);
    
    /**
     * 批量更新问题件订单状态
     * @param list
     * @return
     */
    public boolean batchUpdateOrderStatusByMailNo(List<UpdateInfo> list);
    
    /**
     * 批量添加task 到 ec_core_send_taskToTB
     */
    public boolean batchAddSendTaskToTB(List<SendTaskToTB> list);
    
}
