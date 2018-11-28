package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.CoreTaskLogToTBDao;
import net.ytoec.kernel.dataobject.CoreTaskLogToTB;
import net.ytoec.kernel.mapper.CoreTaskLogToTBMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class CoreTaskLogToTBDaoImpl<T extends CoreTaskLogToTB> implements
		CoreTaskLogToTBDao<T> {

	private static Logger logger = LoggerFactory.getLogger(CoreTaskLogToTBDaoImpl.class);
	@Inject
	private CoreTaskLogToTBMapper<CoreTaskLogToTB> mapper;

	public boolean addCoreTaskLog(T entity) throws DataAccessException {
		// long t1=System.currentTimeMillis();
		boolean flag = false;
		try {
			mapper.add(entity);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
			flag = false;
			throw e;
		} catch (Exception e) {
			logger.error(LogInfoEnum.RUNTIME_EXCEPTION.getValue(),e);
			throw new RuntimeException(e);
		}
		// logger.error("insert 2tb log:"+(System.currentTimeMillis()-t1));
		return flag;
	}

	public T getCoreTaskLogbyId(Integer id) throws DataAccessException {

		CoreTaskLogToTB acc = new CoreTaskLogToTB();
		T entity = null;
		try {
			acc.setId(id);
			entity = (T) mapper.get(acc);
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
			throw e;
		} catch (Exception e) {
			logger.error(LogInfoEnum.RUNTIME_EXCEPTION.getValue(),e);
			throw new RuntimeException(e);
		}
		return entity;
	}

	public boolean editCoreTaskLog(T entity) throws DataAccessException {

		boolean flag = false;
		try {
			mapper.edit(entity);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
			flag = false;
			throw e;
		} catch (Exception e) {
			logger.error(LogInfoEnum.RUNTIME_EXCEPTION.getValue(),e);
			throw new RuntimeException(e);
		}
		return flag;
	}

	public List<T> getAllCoreTaskLog() throws DataAccessException {
		logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue());


		List<T> list = null;
		try {
			list = (List<T>) mapper.getAllCoreTaskLog();
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
			throw e;
		} catch (Exception e) {
			logger.error(LogInfoEnum.RUNTIME_EXCEPTION.getValue(),e);
			throw new RuntimeException(e);
		}
		return list;
	}

	public boolean removeCoreTaskLog(T entity) throws DataAccessException {

		boolean flag = false;
		try {
			mapper.remove(entity);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
			flag = false;
			throw e;
		} catch (Exception e) {
			logger.error(LogInfoEnum.RUNTIME_EXCEPTION.getValue(),e);
			throw new RuntimeException(e);
		}
		return flag;
	}

}
