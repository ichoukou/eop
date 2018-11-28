package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSTempDao;
import net.ytoec.kernel.dataobject.SMSTemp;
import net.ytoec.kernel.mapper.SMSTempMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SMSTempDaoImpl<T extends SMSTemp> implements SMSTempDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSTempDaoImpl.class);

	@Inject
	private SMSTempMapper<T> mapper;
	
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
           
        } catch (Exception e) {
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
	
}
