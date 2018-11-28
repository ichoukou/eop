package net.ytoec.eop.app.service;

import java.util.List;
import java.util.Map;
import net.ytoec.eop.app.bean.App;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.service.BaseService;


public interface AppService<T extends App> extends BaseService<T>{

//	public List<T> getPageList(Map<String, Object> params);

	public List<T> getAll(Map<String, Object> params);
	
	@SuppressWarnings("rawtypes")
	public Pagination getApplist(Pagination pagination,Map map);
	
	@SuppressWarnings("rawtypes")
	public List<T> getApplist(Pagination pagination,Integer id);

//	public Integer getTotal(Map<String, Object> map);

	public T get(Integer id);

	public boolean remove(Integer id);
}