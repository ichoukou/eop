package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSInfoDao;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.mapper.SMSInfoMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author shitianzeng
 * 2012-07-05
 */
@Repository
public class SMSInfoDaoImpl<T extends SMSInfo> implements SMSInfoDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSInfoDaoImpl.class);
	
	@Inject
	private SMSInfoMapper<T> mapper;

	@Override
	public boolean addSMSInfo(T smsInfo) {
		boolean flag = false;
		try {
			mapper.addSMSInfo(smsInfo);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean deleteSMSInfo(Integer id) {
		boolean flag = false;
		try {
			mapper.deleteSMSInfo(id);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean updateSMSInfo(T smsInfo) {
		boolean flag = false;
		try {
			mapper.updateSMSInfo(smsInfo);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSInfoById(Integer id) {
		T smsInfo = null;
		try {
			smsInfo = mapper.getSMSInfoById(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return smsInfo;
	}

	@Override
	public boolean updateSMSInfoById(SMSInfo info1) {
		boolean flag = false;
		try {
			mapper.updateSMSInfoById(info1);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
		
	}

	@Override
	public boolean updateErrorSendById(SMSInfo info1) {
		boolean flag = false;
		try {
			mapper.updateErrorSendById(info1);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public Integer getCountByToday(Map<String, Object> params) {
		return mapper.getCountByToday(params);
	}

	@Override
	public List<SMSInfo> getSMSInfoByPamams(Map<String, Object> params) {
		try {
			return mapper.getSMSInfoByPamams(params);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return new ArrayList<SMSInfo>();
	}

	@Override
	public Integer getSMSInfoCountByPamams(Map<String, Object> params) {
		int count = 0;
		try {
			count = mapper.getSMSInfoCountByPamams(params);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public boolean isNotSendByParams(Map<String, Object> params)
			throws Exception {
		boolean flag = false;
		try {
			SMSInfo smsInfo = mapper.isNotSendByParams(params);
			if (smsInfo == null) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}
}
