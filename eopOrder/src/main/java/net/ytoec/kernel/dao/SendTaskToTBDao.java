package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 
 * 发送任务数据访问接口.
 *
 * @param <T> SendTaskToTB.
 */
public interface SendTaskToTBDao<T> {
	/**
	 * 增加发送任务对象.
	 * @param sendTaskToTB
	 * @throws DataAccessException
	 */
	public void addSendTaskToTB(T sendTaskToTB) throws DataAccessException;
	/**
	 * 修改发送任务对象.
	 * @param sendTaskToTB
	 * @throws DataAccessException
	 */
	public void editSendTaskToTB(T sendTaskToTB) throws DataAccessException;
	/**
	 * 删除发送任务对象.
	 * @param sendTaskToTB
	 * @throws DataAccessException
	 */
	public void removeSendTaskToTB(T sendTaskToTB) throws DataAccessException;
	/**
	 * 根据发送任务id获取发送任务对象.
	 * @param sendTaskToTBId
	 * @return
	 * @throws DataAccessException
	 */
	public T getSendTaskToTBById(Integer sendTaskToTBId) throws DataAccessException;
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
	public List<T> getSendTaskToTBListByLimit(int limit) throws DataAccessException;
	
	/**
	 * 获取发送任务的分页数据列表。分页的参数startIndex（开始索引）和pageNum（每次查询数目）封装在Map中。
	 * @param limit
	 * @return
	 */
	public List<T> getPaginationSendTaskToTBList(Map map) throws DataAccessException;
	public List<T> getSendTaskToTBListByflag(Map map) throws DataAccessException;

	public List<T> getSendTaskListByFlagsAndLimit(Map map);
	
	public Integer batchDelTask(Map map);

    /**
     * 统计任务表的数量
     * 
     * @return
     */
    public Integer countTask();
    
    /**
     * 批量添加发送任务
     * @author wmd
     * @return
     */
    public boolean batchAddSendTask2TB(List<T> sendTaskList);
    
   
    /**
     * <p>Description: 获取指定客户ID任务</p>
     * @param map 查询参数MAP
     * @return 任务列表
     * @throws DataAccessException
     */
    public List<T> getSendTaskByClientId(Map map) throws DataAccessException;
}
