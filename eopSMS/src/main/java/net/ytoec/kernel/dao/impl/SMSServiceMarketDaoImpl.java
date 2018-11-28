package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSServiceMarketDao;
import net.ytoec.kernel.dataobject.SMSServiceMarket;
import net.ytoec.kernel.mapper.SMSServiceMarketMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 营销活动dao层实现类
 * @author shitianzeng
 * 2012-07-05
 */
@Repository
public class SMSServiceMarketDaoImpl<T extends SMSServiceMarket> implements SMSServiceMarketDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSServiceMarketDaoImpl.class);
	
	@Inject
	private SMSServiceMarketMapper<T> mapper;
	
	@Override
	public boolean addSMSServiceMarket(T smsServiceMarket) {
		boolean flag = false;
		try {
			mapper.addSMSServiceMarket(smsServiceMarket);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean deleteSMSServiceMarket(Integer id) {
		boolean flag = false;
		try {
			mapper.deleteSMSServiceMarket(id);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean updateSMSServiceMarket(T smsServiceMarket) {
		boolean flag = false;
		try {
			mapper.updateSMSServiceMarket(smsServiceMarket);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSServiceMarketById(Integer id) {
		T smsServiceMarket = null;
		try {
			smsServiceMarket = mapper.getSMSServiceMarketById(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return smsServiceMarket;
	}

}
