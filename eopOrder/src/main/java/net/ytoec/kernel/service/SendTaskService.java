package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.Pagination;

/**
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface SendTaskService<T> {

	/**
	 * 新增一条任务
	 * 
	 * @param task
	 * @return
	 */
	boolean addSendTask(T task);

	/**
	 * 执行task成功<br>
	 * 1.将task删除<Br>
	 * 2.同时将数据写入TASK_LOG表中
	 * 
	 * @param task
	 * @return
	 */
	boolean finishedSendTask(T task);

	boolean errfinishedSendTask(T task);

	/**
	 * 更新task表，最后一 次发送时间，通常是执行任务失败时需要更新操作
	 * 
	 * @param task
	 * @return
	 */
	boolean updateSendTaskLastSendTime(T task);

	/**
	 * 按照时间倒序，取n条sendTask,总数小于n时，全部提取
	 * 
	 * @param number
	 * @return
	 */
	List<T> getSendTaskList(Integer n);

	/**
	 * 获取发送任务的分页数据列表。分页的参数currentPage（当前页码）和pageNum（每页显示记录数）封装在pagination对象中。
	 * 
	 * @param pagination
	 *            分页对象
	 * @param flag
	 *            分页标识，true表示分页，false表示不分页。
	 * @return @
	 */
	public List<T> getPaginationSendTaskList(Pagination pagination, boolean flag);

	public List<T> getSendTaskListByflag(int taskFlagId, String taskFlag,
			Pagination pagination, boolean flag);

	public List<T> getSendTaskListByflag(Map map, Pagination pagination,
			boolean flag);

	public boolean sendTask(T task, XmlSender xmlSender);
	public boolean sendTaskJG(T task, XmlSender xmlSender);

	public List<T> getSendTaskListByFlagsAndLimit(List<Integer> flags,
			Integer limit);

    /**
     * 统计task表的数量
     * 
     * @return
     */
    public Integer countTask();
    
}
