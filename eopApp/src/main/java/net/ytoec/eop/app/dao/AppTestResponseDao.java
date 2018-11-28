package net.ytoec.eop.app.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dao.BaseDao;

public interface AppTestResponseDao<T> extends BaseDao<T>{
	
	public List<T> getAll(Map<String, Object> param);

	public Integer getTotal(Map<String, Object> map);

	public List<T> getPageList(Map<String, Object> params);

	public T get(Integer id);

	public boolean remove(Integer id);
	
}