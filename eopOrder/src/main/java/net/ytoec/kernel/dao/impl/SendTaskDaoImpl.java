package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SendTaskDao;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.mapper.SendTaskMapper;

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
 *            SendTask
 */

@Repository
public class SendTaskDaoImpl<T extends SendTask> implements SendTaskDao<T> {

	private static Logger logger = LoggerFactory.getLogger(SendTaskDaoImpl.class);
	@Autowired
	private SendTaskMapper<T> mapper;

	@Override
	public void addSendTask(T sendTask) {
		try {
			this.mapper.add(sendTask);
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}
	}

	@Override
	public void editSendTask(T sendTask) {
		try {
			this.mapper.edit(sendTask);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public List<T> getAll() {

		List<T> list = null;
		try {
			list = (List<T>) mapper.getAllSendTask();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public T getSendTaskById(Integer sendTaskId) {
		try {
			SendTask st = new SendTask();
			st.setId(sendTaskId);
			return this.mapper.get((T) st);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return null;
	}

	@Override
	public void removeSendTask(T sendTask) {
		try {
			this.mapper.remove(sendTask);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public List<T> getSendTaskListByLimit(Integer limit) {
		List<T> result = null;
		try {
			result = this.mapper.getSendTaskListByLimit(limit);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}

	public List<T> getPaginationSendTaskList(Map map)
 {
		List<T> result = null;
		try {
			result = this.mapper.getPaginationSendTaskList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}

	public List<T> getSendTaskListByflag(Map map) {
		List<T> result = null;
		try {
			result = this.mapper.getSendTaskListByflag(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}

	@Override
	public List<T> getSendTaskListByFlagsAndLimit(Map map) {
		List<T> result = null;
		try {
			result = this.mapper.getSendTaskListByFlagsAndLimit(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}

    @Override
    public Integer countTask() {

        return this.mapper.countTask();
    }

}
