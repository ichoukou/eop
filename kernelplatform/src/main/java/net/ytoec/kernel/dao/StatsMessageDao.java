package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;


public interface StatsMessageDao<T> {
	

	public boolean addStatsMessage(T statsMessage) throws DataAccessException;
	

	public boolean removeStatsMessage(T statsMessage) throws DataAccessException;
	

	public boolean editStatsMessage(T statsMessage) throws DataAccessException;
	

	public T getStatsMessageById(Integer id) throws DataAccessException;
	

	public List<T> getStatsMessageList(Map map) throws DataAccessException;

}
