package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface BaseMapperDao<T> {

	@SuppressWarnings("unchecked")
	public void setMapperClass(Class<? extends BaseSqlMapper> mapperClass);

	public BaseSqlMapper<T> getMapper();

	public boolean add(T entity) throws Exception;

	public boolean edit(T entity) throws Exception;

	public boolean remove(T entity) throws Exception;

	public T get(T entity) throws Exception;

	public List<T> getAll(T entity) throws Exception;
	
}
