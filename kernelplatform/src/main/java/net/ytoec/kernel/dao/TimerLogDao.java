package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.TimerLog;

/**
 * @作者：L
 * @描述：timer日志记录
 * */
public interface TimerLogDao<T> {
	
	/**
	 * 
	 * @param log
	 * @return
	 */
	public boolean addTimerLog(TimerLog bean) throws DataAccessException;

	public List<TimerLog> get(Map<String, Object> map);

	public String countNum(Map<String, Object> map);
	public int count(Map<String, Object> map);
}
