package net.ytoec.eop.app.mapper;

import java.util.List;
import java.util.Map;
import net.ytoec.eop.app.bean.AppTestResponse;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface AppTestResponseMapper<T extends AppTestResponse> extends BaseSqlMapper<T> {
	
	public List<T> getPageList(Map<String, Object> params);

	public List<T> getAll(Map<String, Object> params);

	public Integer getTotal(Map<String, Object> map);

	public T get(Integer id);

	public void remove(Integer id);
	
}