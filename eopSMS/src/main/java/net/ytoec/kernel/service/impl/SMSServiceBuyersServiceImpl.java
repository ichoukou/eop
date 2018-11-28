package net.ytoec.kernel.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSServiceBuyersDao;
import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.dataobject.SMSServiceBuyers;
import net.ytoec.kernel.service.SMSServiceBuyersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shitianzeng
 * 2012-07-05
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSServiceBuyersServiceImpl implements SMSServiceBuyersService {

	@Inject
	private SMSServiceBuyersDao dao;

	protected final Logger logger = LoggerFactory.getLogger(SMSServiceBuyersServiceImpl.class);
	

	@Override
	public List<SMSBuyers> getSMSBuyersByMarketId(Integer marketId) {
		return null;
	}
	
	@Override
	public boolean addSMSServiceBuyers(SMSServiceBuyers smsServiceBuyers) {
		boolean flag = false;
		if(smsServiceBuyers == null){
			logger.error(SMSServiceBuyers.class.getName() + "添加数据异常!");
		}else{
			dao.addSMSServiceBuyers(smsServiceBuyers);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteSMSServiceBuyers(Integer id) {
		boolean flag = false;
		if(id == null || ("").equals(id.toString())){
			logger.error(SMSServiceBuyers.class.getName() + "删除数据异常!");
		}else{
			dao.deleteSMSServiceBuyers(id);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateSMSServiceBuyers(SMSServiceBuyers smsServiceBuyers) {
		boolean flag = false;
		if(smsServiceBuyers == null){
			logger.error(SMSServiceBuyers.class.getName() + "更新数据异常!");
		}else{
			dao.updateSMSServiceBuyers(smsServiceBuyers);
			flag = true;
		}
		return flag;
	}

	@Override
	public SMSServiceBuyers getSMSServiceBuyersById(Integer id) {
		SMSServiceBuyers smsServiceBuyers = null;
		if(id == null){
			logger.error(SMSServiceBuyers.class.getName() + "查询数据异常!");
		}else{
			smsServiceBuyers = (SMSServiceBuyers) dao.getSMSServiceBuyersById(id);
		}
		return smsServiceBuyers;
	}
}
