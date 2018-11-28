package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSServiceAreaDao;
import net.ytoec.kernel.dataobject.SMSServiceArea;
import net.ytoec.kernel.mapper.SMSServiceAreaMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author shitianzeng
 * 2012-07-05
 */
@Repository
public class SMSServiceAreaDaoImpl<T extends SMSServiceArea> implements SMSServiceAreaDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSServiceAreaDaoImpl.class);
	
	@Inject
	private SMSServiceAreaMapper<T> mapper;
	
	@Override
	public boolean addSMSServiceArea(T smsServiceArea) {
		boolean flag = false;
		try {
			mapper.addSMSServiceArea(smsServiceArea);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean deleteSMSServiceArea(Integer id) {
		boolean flag = false;
		try {
			mapper.deleteSMSServiceArea(id);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean updateSMSServiceArea(T smsServiceArea) {
		boolean flag = false;
		try {
			mapper.updateSMSServiceArea(smsServiceArea);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSServiceAreaById(Integer id) {
		T smsServiceArea = null;
		try {
			smsServiceArea = mapper.getSMSServiceAreaById(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return smsServiceArea;
	}

	@Override
	public List<T> getSMSServiceAreaByParams(Integer smsTypeId, Integer userId) {
		if (smsTypeId == null || userId == null ) {
			logger.error("smsTypeId或者userId为空！");
			return new ArrayList<T>();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("smsTypeId", smsTypeId);
		params.put("userId", userId);
		return mapper.getSMSServiceAreaByParams(params);
	}

}
