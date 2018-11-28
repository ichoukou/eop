package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 
 * 发送任务数据访问接口.
 *
 * @param <T> SendTask.
 */
public interface SendTaskDao<T> {
	/**
	 * 增加发送任务对象.
	 * @param sendTask
	 * @throws DataAccessException
	 */
	public void addSendTask(T sendTask) throws DataAccessException;
	/**
	 * 修改发送任务对象.
	 * @param sendTask
	 * @throws DataAccessException
	 */
	public void editSendTask(T sendTask) throws DataAccessException;
	/**
	 * 删除发送任务对象.
	 * @param sendTask
	 * @throws DataAccessException
	 */
	public void removeSendTask(T sendTask) throws DataAccessException;
	/**
	 * 根据发送任务id获取发送任务对象.
	 * @param sendTaskId
	 * @return
	 * @throws DataAccessException
	 */
	public T getSendTaskById(Integer sendTaskId) throws DataAccessException;
	/**
	 * 获取发送任务列表对象.
	 * @return
	 * @throws DataAccessException
	 */
	public  List<T> getAll() throws DataAccessException;
	/**
	 * 获取指定记录数对象列表,当实际记录数小于limit值,返回实际的对象列表.
	 * @param limit
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getSendTaskListByLimit(Integer limit)
			throws DataAccessException;
	
	/**
	 * 获取发送任务的分页数据列表。分页的参数startIndex（开始索引）和pageNum（每次查询数目）封装在Map中。
	 * @param limit
	 * @return
	 */
	public List<T> getPaginationSendTaskList(Map map) throws DataAccessException;

	public List<T> getSendTaskListByflag(Map map) throws DataAccessException;

	
	public List<T> getSendTaskListByFlagsAndLimit(Map map);
		

    /**
     * 统计任务表的数量
     * 
     * @return
     */
    public Integer countTask();
}
