package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.LogsDao;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.mapper.LogsMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class LogsDaoImpl<T extends Logs> implements LogsDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(LogsDaoImpl.class);

	@Inject
	private LogsMapper<T> mapper;
	
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
		return false;
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
		return null;
	}

}
