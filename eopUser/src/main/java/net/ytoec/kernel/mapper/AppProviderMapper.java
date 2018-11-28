package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface AppProviderMapper<T extends AppProvider> extends BaseSqlMapper<T> {
	
	public List<T> getPageList(Map<String, Object> params);

	public List<T> getAll(Map<String, Object> params);

	public Integer getTotal(Map<String, Object> map);

	public T getByUserId(Integer userId);
	
}