package net.ytoec.eop.app.service;

import java.util.List;
import java.util.Map;
import net.ytoec.eop.app.bean.AppTest;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.service.BaseService;

public interface AppTestService<T extends AppTest> extends BaseService<T>{
	
	public List<T> getAll(Map<String, Object> param);

	@SuppressWarnings("rawtypes")
	public Pagination getPageList(Pagination pagination,Map<String, Object> params);

	public T get(Integer id);

	public boolean remove(Integer id);
	
	/**
	 * 获取今天剩余的测试次数
	 * @return
	 */
	public Integer getTimes(Map<String,Object> params);
	
}