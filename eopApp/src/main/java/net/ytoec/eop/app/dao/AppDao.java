package net.ytoec.eop.app.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dao.BaseDao;

public interface AppDao<T> extends BaseDao<T>{
	
	public List<T> getPageList(Map<String, Object> params);

	public List<T> getAll(Map<String, Object> params);

	public Integer getTotal(Map<String, Object> map);

	public T get(Integer id);

	public boolean remove(Integer id);
	
}