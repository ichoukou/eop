package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * 发送任务数据映射器.
 * 
 */
public interface SendTaskToTBMapper<T extends SendTaskToTB> extends BaseSqlMapper<T> {
	/**
	 * 获取所有发送任务的对象列表.
	 * @return
	 */
	public List<T> getAllSendTaskToTB();
	/**
	 * 获取top小于等于limit记录数的对象列表.
	 * @param limit
	 * @return
	 */
	public List<T> getSendTaskToTBListByLimit(int limit);
	
	/**
	 * 获取发送任务的分页数据列表。分页的参数startIndex（开始索引）和pageNum（每次查询数目）封装在Map中。
	 * @param limit
	 * @return
	 */
	public List<T> getPaginationSendTaskToTBList(Map map);
	public List<T> getSendTaskToTBListByflag(Map map);

	public List<T> getSendTaskListByFlagsAndLimit(Map map);
	
	public Integer batchDelTask(Map map);

    public Integer countTask();
    
    /**
     * 批量添加 
     * @param list
     * @return
     */
    public void batchAdd(List<T> list);
}
