package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.StatsMessage;

/**
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface StatsMessageService<T> {


	boolean addStatsMessage(T statsMessage);
	
	boolean editStatsMessage(T statsMessage);

	public List<T> getStatsMessageList(Map map, Pagination pagination,
			boolean flag);
	public StatsMessage getStatsMessageListbySR(String sr, Pagination pagination,
			boolean flag);
	public StatsMessage getStatsMessageListbySR(String sr);
}
