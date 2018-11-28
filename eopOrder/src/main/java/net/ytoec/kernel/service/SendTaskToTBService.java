package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.SendTaskToTB;

/**
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface SendTaskToTBService<T> {

	/**
	 * 新增一条任务
	 * 
	 * @param taskToTB
	 * @return
	 */
	boolean addSendTaskToTB(T taskToTB);

	/**
	 * 执行taskToTB成功<br>
	 * 1.将taskToTB删除<Br>
	 * 2.同时将数据写入TASK_LOG表中
	 * 
	 * @param taskToTB
	 * @return
	 */
	boolean finishedSendTaskToTB(T taskToTB);

	boolean errfinishedSendTaskToTB(T taskToTB);

	/**
	 * 更新taskToTB表，最后一 次发送时间，通常是执行任务失败时需要更新操作
	 * 
	 * @param taskToTB
	 * @return
	 */
	boolean updateSendTaskToTBLastSendTime(T taskToTB);

	/**
	 * 按照时间倒序，取n条sendTaskToTB,总数小于n时，全部提取
	 * 
	 * @param number
	 * @return
	 */
	List<T> getSendTaskToTBList(int n);

	/**
	 * 获取发送任务的分页数据列表。分页的参数currentPage（当前页码）和pageNum（每页显示记录数）封装在pagination对象中。
	 * 
	 * @param pagination
	 *            分页对象
	 * @param flag
	 *            分页标识，true表示分页，false表示不分页。
	 * @return @
	 */
	public List<T> getPaginationSendTaskToTBList(Pagination pagination,
			boolean flag);

	public List<T> getSendTaskToTBListByflag(int taskFlagId, String taskFlag,
			Pagination pagination, boolean flag);

	public List<T> getSendTaskToTBListByflag(Map map, Pagination pagination,
			boolean flag);

	public boolean sendTaskToTB(SendTaskToTB sendTask, XmlSender xmlSender);

	public List<SendTaskToTB> getSendTaskListByFlagsAndLimit(
			List<Integer> flags, Integer limit);

	public boolean sendTaskToTB(T taskToTB, XmlSender xmlSender);

    /**
     * 统计task表的数量
     * 
     * @return
     */
    public Integer countTask();
    
    /**
     * 批量添加发送淘宝任务记录
     * @param sendTaskToTBList
     * create by wangmindong
     * create timer 2301-03-28
     * @return
     */
    public boolean batchAddSendTaskToTB(List<T> sendTaskToTBList); 
}
