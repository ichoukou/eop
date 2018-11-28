package net.ytoec.kernel.mapper.base;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * @author huaiwen
 * @package net.ytoec.kernel
 * @project kernel
 * @email huhuaiwen@gmail.com
 * @version 1.0
 */
public interface BaseSqlMapper<T> extends SqlMapper {

	public void add(T entity) throws DataAccessException;

	public void edit(T entity) throws DataAccessException;

	public void remove(T entity) throws DataAccessException;

	public T get(T entity) throws DataAccessException;

	public List<T> getList(T entity) throws DataAccessException;
}
