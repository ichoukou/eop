package net.ytoec.eop.app.service;

import java.util.List;
import java.util.Map;
import net.ytoec.eop.app.bean.AppTestResponse;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.service.BaseService;

public interface AppTestResponseService<T extends AppTestResponse> extends BaseService<T>{
	
	public List<T> getAll(Map<String, Object> param);

	@SuppressWarnings("rawtypes")
	public Pagination getPageList(Pagination pagination,Map<String, Object> params);

	public T get(Integer id);

	public boolean remove(Integer id);
	
}