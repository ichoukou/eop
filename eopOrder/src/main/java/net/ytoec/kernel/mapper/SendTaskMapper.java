package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * 发送任务数据映射器.
 * 
 */
public interface SendTaskMapper<T extends SendTask> extends BaseSqlMapper<T> {
	/**
	 * 获取所有发送任务的对象列表.
	 * 
	 * @return
	 */
	public List<T> getAllSendTask();

	/**
	 * 获取top小于等于limit记录数的对象列表.
	 * 
	 * @param limit
	 * @return
	 */
	public List<T> getSendTaskListByLimit(int limit);

	/**
	 * 获取发送任务的分页数据列表。分页的参数startIndex（开始索引）和pageNum（每次查询数目）封装在Map中。
	 * 
	 * @param limit
	 * @return
	 */
	public List<T> getPaginationSendTaskList(Map map);

	public List<T> getSendTaskListByflag(Map map);


	public List<T> getSendTaskListByFlagsAndLimit(Map map);

    public Integer countTask();

}
