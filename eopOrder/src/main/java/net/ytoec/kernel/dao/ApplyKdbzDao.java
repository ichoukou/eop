package net.ytoec.kernel.dao;

import org.springframework.dao.DataAccessException;

public interface ApplyKdbzDao<T> {
	
	public boolean addApplyKdbz(T entity) throws DataAccessException;
	
	public  boolean editApplyKdbz(T entity) throws DataAccessException;
}
