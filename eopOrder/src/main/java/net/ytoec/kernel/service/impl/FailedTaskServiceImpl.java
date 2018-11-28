package net.ytoec.kernel.service.impl;

import net.ytoec.kernel.dao.FailedTaskDao;
import net.ytoec.kernel.dataobject.FailedTask;
import net.ytoec.kernel.service.FailedTaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 任务发送服务接口实现类.
 * 
 * @param <T>
 *            FailedTask
 */
@Service
@Transactional
public class FailedTaskServiceImpl<T extends FailedTask> implements
		FailedTaskService<T> {
	
	private static Logger logger = LoggerFactory.getLogger(FailedTaskServiceImpl.class);
	
	@Autowired
	private FailedTaskDao<T> dao;

	@Override
	public boolean addFailedTask(T task) {

		try {
			this.dao.addFailedTask(task);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}

}
