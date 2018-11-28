package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.service.BaseService;

public interface AppProviderService<T extends AppProvider> extends BaseService<T>{
	
	public List<T> getAll(Map<String, Object> param);
	
	public T getByUserId(Integer userId);
	
}