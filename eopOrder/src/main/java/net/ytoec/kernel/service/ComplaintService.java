/**
 * net.ytoec.kernel.service
 * ComplaintService.java
 * 2012-7-10下午03:05:15
 * @author wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.User;

/**
 * @author wangyong
 * 2012-7-10
 */
public interface ComplaintService<T> {
	
	/**
	 * 新增
	 * @param entity
	 * @return
	 */
	public boolean addComplaint(T entity);
	
	/**
	 * 卖家问题件投诉
	 * @param questionIds
	 * @param complaintContent
	 * @param curUser
	 * @return
	 */
	public String complaintQuesition(Integer[] questionIds, String complaintContent, User curUser);
	
	/**
	 * 根据mailNo查询
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getListByMailNo(String mailNo);
}
