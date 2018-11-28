package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSServiceMarketDao;
import net.ytoec.kernel.dataobject.SMSServiceMarket;
import net.ytoec.kernel.service.SMSServiceMarketService;

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
public class SMSServiceMarketServiceImpl implements SMSServiceMarketService {

	@Inject
	private SMSServiceMarketDao dao;

	protected final Logger logger = LoggerFactory
			.getLogger(SMSServiceMarketServiceImpl.class);
	
	@Override
	public boolean addSMSServiceMarket(SMSServiceMarket smsServiceMarket) {
		boolean flag = false;
		if(smsServiceMarket == null){
			logger.error(SMSServiceMarket.class.getName() + "添加数据异常!");
		}else{
			dao.addSMSServiceMarket(smsServiceMarket);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteSMSServiceMarket(Integer id) {
		boolean flag = false;
		if(id == null || ("").equals(id.toString())){
			logger.error(SMSServiceMarket.class.getName() + "删除数据异常!");
		}else{
			dao.deleteSMSServiceMarket(id);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateSMSServiceMarket(SMSServiceMarket smsServiceMarket) {
		boolean flag = false;
		if(smsServiceMarket == null){
			logger.error(SMSServiceMarket.class.getName() + "更新数据异常!");
		}else{
			dao.updateSMSServiceMarket(smsServiceMarket);
			flag = true;
		}
		return flag;
	}

	@Override
	public SMSServiceMarket getSMSServiceMarketById(Integer id) {
		SMSServiceMarket smsServiceMarket = null;
		if(id == null){
			logger.error(SMSServiceMarket.class.getName() + "查询数据异常!");
		}else{
			smsServiceMarket = (SMSServiceMarket) dao.getSMSServiceMarketById(id);
		}
		return smsServiceMarket;
	}

}
