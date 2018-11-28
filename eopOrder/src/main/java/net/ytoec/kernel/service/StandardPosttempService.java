package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

/**
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface StandardPosttempService<T> {

	/**
	 * 新增一条任务
	 * 
	 * @param task
	 * @return 
	 */
	boolean addStandardPosttemp(T task);

	/**
	 * 获取发送任务的分页数据列表。分页的参数currentPage（当前页码）和pageNum（每页显示记录数）封装在pagination对象中。
	 * 
	 * @param pagination
	 *            分页对象
	 * @param flag
	 *            分页标识，true表示分页，false表示不分页。
	 * @return @
	 */
	
	public List<T> getStandardPosttempListBySourceId(int SourceId,
			Pagination pagination, boolean flag);
	
	public List<T> getStandardPosttempList(Map map, Pagination pagination,
			boolean flag);

	public T getStandardPosttempByProv(int sourceId, int destId);
}
