package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface CoreTaskLogDao<T> {

	public boolean addCoreTaskLog(T entity) throws DataAccessException;

	public List<T> getAllCoreTaskLog() throws DataAccessException;

	public T getCoreTaskLogbyId(Integer id) throws DataAccessException;

	public boolean editCoreTaskLog(T entity) throws DataAccessException;

	public boolean removeCoreTaskLog(T entity) throws DataAccessException;
}
