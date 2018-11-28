package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface JgOrderCommandDao<T> {

	public List<T> getJGOrderByLimit(Integer limit)
			throws DataAccessException;
	
	public List<T> getJGOrder(Map<String, Object> map);
	
	public int count(Map<String, Object> map);

	public Boolean removeJGOrderById(Map<String ,Integer> mapId)
			throws DataAccessException;
	
	public Boolean addJGOrder(T bean);
}
