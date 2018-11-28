package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;
import net.ytoec.kernel.dao.BaseDao;

public interface AppProviderDao<T> extends BaseDao<T>{
	
	public List<T> getAll(Map<String, Object> param);

	public T getByUserId(Integer userId);
	
}