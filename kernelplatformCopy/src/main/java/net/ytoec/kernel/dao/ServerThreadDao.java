/**
 * ServerThreadDao.java
 * Wangyong
 * 2011-9-7 下午02:40:21
 */
package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * 多线程服务数据对象DB接口
 * @author Wangyong
 * @2011-9-7
 * net.ytoec.kernel.dao
 */
public interface ServerThreadDao<T> {

	/**
	 * 查询所有多线程服务数据，并获取最近的一条数据
	 * @return
	 */
	public List<T> getLastServerThread() throws DataAccessException;
	
	/**
	 * 增加多线程服务数据
	 * @param serverThread
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addServerThread(T serverThread) throws DataAccessException;
	
	/**
	 * 删除多线程服务数据
	 * @param serverThread
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeServerThread(T serverThread) throws DataAccessException;
	
}
