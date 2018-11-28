package net.ytoec.kernel.dao;

import org.springframework.dao.DataAccessException;

/**
 * 
 * 发送任务数据访问接口.
 *
 * @param <T> FailedTask.
 */
public interface FailedTaskDao<T> {
	/**
	 * 增加发送失败任务对象.
	 * @param sendTask
	 * @throws DataAccessException
	 */
	public void addFailedTask(T failedTask) throws DataAccessException;
	/**
	 * 修改发送失败任务对象.
	 * @param sendTask
	 * @throws DataAccessException
	 */
	public void editFailedTask(T failedTask) throws DataAccessException;
	/**
	 * 删除发送失败任务对象.
	 * @param sendTask
	 * @throws DataAccessException
	 */
	public void removeFailedTask(T failedTask) throws DataAccessException;


}
