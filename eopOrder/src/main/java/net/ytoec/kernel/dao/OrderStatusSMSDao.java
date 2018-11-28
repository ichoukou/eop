package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.remote.xml.WaiteSendSMSInfo;
/**
 * 订单状态用于发送短信
 * create by hufei
 * create timer 2013-04-08
 *
 */
public interface OrderStatusSMSDao {
   
	/**
	 * 批量添加要发送短信的订单
	 * @param list
	 * @return
	 */
	public boolean batchAddOrdForSMS(List<WaiteSendSMSInfo> list);
	/**
	 * 批量查询
	 * @param list
	 * @return
	 */
	public List<WaiteSendSMSInfo> batchSearchOrdForSMS(Map<String, Object> map);
	
	
	/**
	 * 批量删除ec_core_orderstatus_sms中的订单信息
	 * @param list
	 * @return
	 */
	public boolean batchDeleteOrd(List<WaiteSendSMSInfo> list);
}
