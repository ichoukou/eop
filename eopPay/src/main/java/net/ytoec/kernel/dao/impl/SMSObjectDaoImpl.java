package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSObjectDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.SMSObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class SMSObjectDaoImpl<T extends SMSObject> implements SMSObjectDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSObjectDaoImpl.class);

	@Inject
	private SMSObjectMapper<T> mapper;
	
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
	public boolean edit(T entity) {
		 boolean flag = true;
		 try {
			mapper.edit(entity);
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
	public T get(T entity) {
		return mapper.get(entity);
	}

	@Override
	public List<T> getList(Map map) throws DataAccessException {
		return mapper.getList(map);
	}

	@Override
	public Integer getRecordsCount(Map map) {
		return mapper.getRecordsCount(map);
	}

	@Override
	public boolean removeByLimit(int limit) throws DataAccessException {
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

	@Override
	public List<T> getStatusList(Map map) {
		// TODO Auto-generated method stub
		return mapper.getStatusList(map);
	}
}
