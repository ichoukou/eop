package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface WarnValueDao<T> {
	
	public abstract boolean addWarnValue(T entity)  throws DataAccessException;
	
	public abstract boolean removeWarnValue(T entity)  throws DataAccessException;
	
	public abstract boolean editWarnValue(T entity)  throws DataAccessException;
	
	public abstract List<T> searchWarnValueMore(T entity) throws DataAccessException;
	
	public abstract T searchWarnValueOne(T entity) throws DataAccessException;
	
	public abstract T searchWarnValueOneAdd(T entity) throws DataAccessException;
}
