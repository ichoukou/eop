package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSServiceBuyersDao;
import net.ytoec.kernel.dataobject.SMSServiceBuyers;
import net.ytoec.kernel.mapper.SMSServiceBuyersMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author shitianzeng
 * 2012-07-05
 */
@Repository
public class SMSServiceBuyersDaoImpl<T extends SMSServiceBuyers> implements SMSServiceBuyersDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSServiceBuyersDaoImpl.class);
	
	@Inject
	private SMSServiceBuyersMapper<T> mapper;
	
	@Override
	public boolean addSMSServiceBuyers(T smsServiceBuyers) {
		boolean flag = false;
		try {
			mapper.addSMSServiceBuyers(smsServiceBuyers);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean deleteSMSServiceBuyers(Integer id) {
		boolean flag = false;
		try {
			mapper.deleteSMSServiceBuyers(id);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean updateSMSServiceBuyers(T smsServiceBuyers) {
		boolean flag = false;
		try {
			mapper.updateSMSServiceBuyers(smsServiceBuyers);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSServiceBuyersById(Integer id) {
		T smsServiceBuyers = null;
		try {
			smsServiceBuyers = mapper.getSMSServiceBuyersById(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return smsServiceBuyers;
	}

}
