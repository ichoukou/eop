package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;


public interface UnlikeFreightDao<T> {
	

	public boolean addUnlikeFreight(T unlikeFreight) throws DataAccessException;
	

	public boolean removeUnlikeFreight(T unlikeFreight) throws DataAccessException;
	

	public boolean editUnlikeFreight(T unlikeFreight) throws DataAccessException;
	

	public T getUnlikeFreightById(Integer id) throws DataAccessException;
	
	public T getUnlikeFreightByMailNo(String mailNo) throws DataAccessException;
	

	public List<T> getUnlikeFreightList(Map map) throws DataAccessException;

}
