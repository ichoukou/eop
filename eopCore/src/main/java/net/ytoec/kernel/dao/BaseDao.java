package net.ytoec.kernel.dao;

public interface BaseDao<T> {

    /**
     * 增加数据
     * @param entity
     * @return
     */
	public boolean add(T entity);

	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public boolean edit(T entity);

	/**
	 * 物理删除
	 * @param entity
	 * @return
	 */
	public boolean remove(T entity);

	/**
	 * 根据ID获取对象
	 * @param entity
	 * @return
	 */
	public T get(T entity);

}
