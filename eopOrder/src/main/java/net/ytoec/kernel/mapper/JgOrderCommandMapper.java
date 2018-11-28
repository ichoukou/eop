package net.ytoec.kernel.mapper;


import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface JgOrderCommandMapper<T extends JGOrderDTO> extends BaseSqlMapper<T> {
	
	
	public List<T> getJGOrderByLimit(int limit);
	
	public List<T> getJGOrder(Map<String ,Object> map);
	
	public int count(Map<String ,Object> map);
	
	public Boolean removeJGOrderById(Map<String ,Integer> mapId);

}
