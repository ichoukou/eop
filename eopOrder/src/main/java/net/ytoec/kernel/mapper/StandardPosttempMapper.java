package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.StandardPosttemp;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * 标准运费模版.
 * 
 */
public interface StandardPosttempMapper<T extends StandardPosttemp> extends BaseSqlMapper<T> {
	
	public List<T> getAllStandardPosttemp();

	public List<T> getStandardPosttempListBySourceId(Map map);

	public List<T> getStandardPosttempByProv(Map map);


}
