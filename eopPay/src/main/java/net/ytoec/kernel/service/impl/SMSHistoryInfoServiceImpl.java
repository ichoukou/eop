package net.ytoec.kernel.service.impl;


import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSHistoryInfoDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSHistoryInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("sMSHistoryInfoServiceImpl")
@Transactional
@SuppressWarnings("all")
public class SMSHistoryInfoServiceImpl<T extends SMSObject> implements SMSHistoryInfoService<T> {

	@Inject
	private SMSHistoryInfoDao<SMSObject> sMSHistoryInfoDao;

	private static final Logger logger = LoggerFactory.getLogger(SMSHistoryInfoServiceImpl.class);

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		if(entity==null) {
			logger.error("实体对象不能为null...");
			return false;
		}
		return sMSHistoryInfoDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
