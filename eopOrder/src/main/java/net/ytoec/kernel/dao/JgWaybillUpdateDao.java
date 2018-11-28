/**
 * JgWaybillDao.java
 * Wangyong
 * 2011-8-18 下午01:08:21
 */
package net.ytoec.kernel.dao;

import org.springframework.dao.DataAccessException;

/**
 * 金刚运单接口信息数据Dao
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.dao
 */
public interface JgWaybillUpdateDao<T> {

	/**
	 * 增加出错信息规范对象数据
	 * @param jgWaybill
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addJgWaybillUpdate(T jgWaybillUpdate) throws DataAccessException;
	
}
