package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSHistoryInfoDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.SMSHistoryInfoMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 发送短信历史记录表
 * @author guolongchao
 *   20120917
 */
@Repository
public class SMSHistoryInfoDaoImpl <T extends SMSObject> implements SMSHistoryInfoDao<T> {

	@Inject
	private SMSHistoryInfoMapper<T> mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(SMSHistoryInfoDaoImpl.class);

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		boolean flag = true;
		 try {
			mapper.add(entity);
		 }
		 catch(Exception e){
			 logger.error("插入到ec_core_paysms_info时出错...id="+entity.getId());			 
			 flag = false;
		 }
		 return flag;
		
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
