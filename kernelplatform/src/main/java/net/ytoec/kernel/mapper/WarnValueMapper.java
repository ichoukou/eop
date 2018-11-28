package net.ytoec.kernel.mapper;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface WarnValueMapper<T>  extends BaseSqlMapper<T> {
	public List<T> getAllList(T entity) throws DataAccessException;
	public T getForAdd(T entity) throws DataAccessException;
}
