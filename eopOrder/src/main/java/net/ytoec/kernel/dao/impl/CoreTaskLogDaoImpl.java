package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.CoreTaskLogDao;
import net.ytoec.kernel.dataobject.CoreTaskLog;
import net.ytoec.kernel.mapper.CoreTaskLogMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CoreTaskLogDaoImpl<T extends CoreTaskLog> implements
		CoreTaskLogDao<T> {

	private static Logger logger = LoggerFactory
	.getLogger(CoreTaskLogDaoImpl.class);
	@Inject
	private CoreTaskLogMapper<CoreTaskLog> mapper;

	public boolean addCoreTaskLog(T entity) {
		long t1=System.currentTimeMillis();
		boolean flag = false;
		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		logger.info("insert log:" + (System.currentTimeMillis() - t1));
		return flag;
	}

	public T getCoreTaskLogbyId(Integer id) {

		CoreTaskLog acc = new CoreTaskLog();
		T entity = null;
		try {
			acc.setId(id);
			entity = (T) mapper.get(acc);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	public boolean editCoreTaskLog(T entity) {

		boolean flag = false;
		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	public List<T> getAllCoreTaskLog() {
		logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue());


		List<T> list = null;
		try {
			list = (List<T>) mapper.getAllCoreTaskLog();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	public boolean removeCoreTaskLog(T entity) {

		boolean flag = false;
		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

}
