/**
 * ServerThreadService.java
 * Wangyong
 * 2011-9-7 下午02:48:03
 */
package net.ytoec.kernel.service;


/**
 * 多线程服务数据对象业务接口
 * @author Wangyong
 * @2011-9-7
 * net.ytoec.kernel.service
 */
public interface ServerThreadService<T> {

	/**
	 * 查询所有多线程服务数据，并获取最近的一条数据
	 * @return
	 */
	public T getLastServerThread();
	
	/**
	 * 增加多线程服务数据
	 * 
	 * @param serverThread
	 * @return @
	 */
	public boolean addServerThread(T serverThread);
	
	/**
	 * 删除多线程服务数据
	 * 
	 * @param serverThread
	 * @return @
	 */
	public boolean removeServerThread(T serverThread);
}
