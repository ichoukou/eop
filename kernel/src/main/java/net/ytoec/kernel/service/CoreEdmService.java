package net.ytoec.kernel.service;

/**
 * 邮件营销统计
 * @author mabo
 *
 * @param <T>
 */
public interface CoreEdmService<T> {

	/**
	 * 增加一条记录
	 * @param entity
	 * @return
	 */
	public boolean addCoreEdm(T entity);
}
