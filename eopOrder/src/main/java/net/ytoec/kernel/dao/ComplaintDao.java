/**
 * net.ytoec.kernel.dao
 * ComplaintDao.java
 * 2012-7-10下午02:59:37
 * @author wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * @author wangyong
 * 2012-7-10
 */
public interface ComplaintDao<T> {
	
	public boolean addComplaint(T entity) throws DataAccessException;
	
	/**
	 * 根据mailNo查询
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getListByMailNo(String mailNo) throws DataAccessException;
}
