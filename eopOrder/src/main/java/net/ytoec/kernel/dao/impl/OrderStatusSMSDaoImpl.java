package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.WaiteSendSMSInfo;
import net.ytoec.kernel.dao.OrderStatusSMSDao;
import net.ytoec.kernel.mapper.OrderStatusSMSMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 订单状态用于发送短信
 * create by hufei
 * create timer 2013-04-08
 *
 */
@Repository
@SuppressWarnings("all")
public class OrderStatusSMSDaoImpl implements OrderStatusSMSDao{
	
	private static Logger logger = LoggerFactory.getLogger(OrderStatusSMSDaoImpl.class);
	@Inject
	private OrderStatusSMSMapper<WaiteSendSMSInfo> mapper;
	@Override
	public boolean batchAddOrdForSMS(List<WaiteSendSMSInfo> list) {
		boolean flag=false;
		try {
			mapper.batchAddOrdForSMS(list);
			flag=true;
		} catch (Exception e) {
			flag=false;
			logger.error("批量添加数据到ec_core_orderstatus_sms失败"+e.getMessage());
		}
		return flag;
	}
	@Override
	public List<WaiteSendSMSInfo> batchSearchOrdForSMS(Map<String, Object> map) {
		return mapper.batchSearchOrdForSMS(map);
	}
	@Override
	public boolean batchDeleteOrd(List<WaiteSendSMSInfo> list){
		boolean flag=false;
		try {
			mapper.batchDeleteOrd(list);
			flag=true;
		} catch (Exception e) {
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}

}
