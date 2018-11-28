package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.TimerLogDao;
import net.ytoec.kernel.dataobject.ApiLog;
import net.ytoec.kernel.dataobject.TimerLog;
import net.ytoec.kernel.mapper.TimerLogMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * @作者：L
 * @描述：timer日志记录
 * */
@Repository
public class TimerLogDaoImpl<T extends ApiLog> implements TimerLogDao<T> {
	
	private Logger logger = LoggerFactory.getLogger(TimerLogDaoImpl.class);
	
    @SuppressWarnings("unused")
	@Inject
    private TimerLogMapper<TimerLog> mapper; 
    
	@Override
	public boolean addTimerLog(TimerLog bean) throws DataAccessException  {
		
		boolean flag = false;
		try {
			mapper.addTimerLog(bean);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}
		return flag;
	}

	@Override
	public List<TimerLog> get(Map<String, Object> map) {
		
		return mapper.getTimerLog(map);
	}

	@Override
	public String countNum(Map<String, Object> map) {
		return mapper.countNum(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return mapper.count(map);
	}

	
}
