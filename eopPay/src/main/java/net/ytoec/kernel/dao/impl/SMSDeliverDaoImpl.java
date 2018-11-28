package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSDeliverDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.SMSDeliverMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class SMSDeliverDaoImpl<T extends SMSObject> implements SMSDeliverDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSDeliverDaoImpl.class);

	@Inject
	private SMSDeliverMapper<T> mapper;
	
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
	public List<T> getList(Map map) throws DataAccessException {
		// TODO Auto-generated method stub
		return mapper.getList(map);
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
	public boolean removeByIDs(List<T> list) throws DataAccessException {
		// TODO Auto-generated method stub
		 boolean flag = true;
		 try {
			mapper.removeByIDs(list);
		 }
		 catch(Exception e){
			 logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);			 
			 flag = false;
		 }
		 return flag;
	}
}
