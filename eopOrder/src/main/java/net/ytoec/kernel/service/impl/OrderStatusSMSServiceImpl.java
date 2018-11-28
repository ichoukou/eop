package net.ytoec.kernel.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.WaiteSendSMSInfo;
import net.ytoec.kernel.dao.OrderStatusSMSDao;
import net.ytoec.kernel.service.OrderStatusSMSService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 订单状态用于发送短信
 * create by hufei
 * create timer 2013-04-08
 *
 */
@Service
@Transactional
public class OrderStatusSMSServiceImpl implements OrderStatusSMSService{

	@Inject
	private OrderStatusSMSDao orderStatusSMSDao;
	@Override
	public boolean batchAddOrdForSMS(List<WaiteSendSMSInfo> list) {
		
		return orderStatusSMSDao.batchAddOrdForSMS(list);
	}
	@Override
	public List<WaiteSendSMSInfo> batchSearchOrdForSMS(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return orderStatusSMSDao.batchSearchOrdForSMS(map);
	}
	@Override
	public boolean batchDeleteOrd(List<WaiteSendSMSInfo> list) {
		// TODO Auto-generated method stub
		return orderStatusSMSDao.batchDeleteOrd(list);
	}

}
