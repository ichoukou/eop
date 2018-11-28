package net.ytoec.kernel.dao;

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
	public boolean addTimerLog(TimerLog bean) throws DataAccessException;;
}
