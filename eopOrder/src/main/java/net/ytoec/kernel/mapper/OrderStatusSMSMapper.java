package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.remote.xml.WaiteSendSMSInfo;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;

/**
 * 订单状态用于发送短信
 * create by hufei
 * create timer 2013-04-08
 *
 */
public interface OrderStatusSMSMapper <T extends WaiteSendSMSInfo> extends BaseSqlMapper<T>{
	/**
	 * 向ec_core_orderstatus_sms插入订单信息
	 * @param list
	 * @throws DataAccessException
	 */
	 
	public void batchAddOrdForSMS(List<WaiteSendSMSInfo> list) throws DataAccessException;
	
	/**
	 * 向ec_core_orderstatus_sms查询订单信息
	 * @param list
	 * @return
	 * @throws DataAccessException
	 */
	public List<WaiteSendSMSInfo> batchSearchOrdForSMS (Map<String, Object> map) throws DataAccessException;
	/**
	 * 删除ec_core_orderstatus_sms中的订单信息
	 * @param list
	 * @return
	 * @throws DataAccessException
	 */
	public void batchDeleteOrd(List<WaiteSendSMSInfo> list) throws DataAccessException;
}
