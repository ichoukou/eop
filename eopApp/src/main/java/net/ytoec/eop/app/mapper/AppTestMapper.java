package net.ytoec.eop.app.mapper;

import java.util.List;
import java.util.Map;
import net.ytoec.eop.app.bean.AppTest;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface AppTestMapper<T extends AppTest> extends BaseSqlMapper<T> {
	
	public List<T> getPageList(Map<String, Object> params);

	public List<T> getAll(Map<String, Object> params);

	public Integer getTotal(Map<String, Object> map);

	public T get(Integer id);

	public void remove(Integer id);
	
	public Integer getTimes(Map<String,Object> params);
	
}