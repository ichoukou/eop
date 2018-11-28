/**
 * 
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.StatsMessage;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;


public interface StatsMessageMapper<T extends StatsMessage> extends	BaseSqlMapper<T> {


	public List<T> getStatsMessageList(Map map);
	
	
}
