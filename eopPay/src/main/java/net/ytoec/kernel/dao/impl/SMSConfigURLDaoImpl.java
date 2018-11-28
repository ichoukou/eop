package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSConfigURLDao;
import net.ytoec.kernel.dataobject.SMSConfigURL;
import net.ytoec.kernel.mapper.SMSConfigURLMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class SMSConfigURLDaoImpl<T extends SMSConfigURL> implements SMSConfigURLDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSConfigURLDaoImpl.class);

	@Inject
	private SMSConfigURLMapper<T> mapper;
	
	@Override
	public boolean add(T entity) {
		 boolean flag = true;
		 try {
			mapper.add(entity);
		 }
		 catch(Exception e){
			 logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);			 
			 flag = false;
		 }
		 return flag;
	}

	@Override
	public boolean remove(T entity) {
		 boolean flag = true;
		 try {
			mapper.remove(entity);
		 }
		 catch(Exception e){
			 logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);			 
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
	public T get(T entity) {
		// TODO Auto-generated method stub
		return mapper.get(entity);
	}
	
	@Override
	public boolean removeByLimit(int limit) throws DataAccessException {
		// TODO Auto-generated method stub
		 boolean flag = true;
		 try {
			mapper.removeByLimit(limit);
		 }
		 catch(Exception e){
			 logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);			 
			 flag = false;
		 }
		 return flag;
	}
}
