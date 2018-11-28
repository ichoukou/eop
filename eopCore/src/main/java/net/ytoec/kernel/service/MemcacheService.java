package net.ytoec.kernel.service;

public interface MemcacheService<T> {

	/**
	 * 添加一个对象
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public boolean add(String key, Object object);

	/**
	 * 获取一个对象，无对象返回null
	 * 
	 * @param key
	 * @return
	 */

	public T get(String key);

	/**
	 * 删除对象
	 * 
	 * @param key
	 * @return
	 */

	public boolean delete(String key);

	/**
	 * 取代一个对象
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public boolean replace(String key, Object object);

	/**
	 * 打印memchace状态
	 */
	public void printStatus();

}
