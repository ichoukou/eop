package net.ytoec.kernel.dao.impl;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.FailedTaskDao;
import net.ytoec.kernel.dataobject.FailedTask;
import net.ytoec.kernel.mapper.FailedTaskMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 
 * 发送任务数据访问实现类.
 * 
 * @param <T>
 *            failedTask
 */

@Repository
public class FailedTaskDaoImpl<T extends FailedTask> implements FailedTaskDao<T> {

	private static Logger logger = LoggerFactory.getLogger(FailedTaskDaoImpl.class);
	@Autowired
	private FailedTaskMapper<T> mapper;

	@Override
	public void addFailedTask(T failedTask) throws DataAccessException {
		try {
			this.mapper.add(failedTask);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public void editFailedTask(T failedTask) throws DataAccessException {
		try {
			this.mapper.edit(failedTask);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	
	@Override
	public void removeFailedTask(T failedTask) throws DataAccessException {
		try {
			this.mapper.remove(failedTask);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

}
