/**
 * 2012-5-7上午09:57:28
 * wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;

/**
 * 消息用户关联dao
 * @author wangyong
 * 2012-5-7
 */
public interface MessageUserDao<T> {

	/**
	 * 根据消息id和用户id删除记录
	 * @param entity
	 * @return 
	 */
	public boolean deleteByUserAndMsgId(T entity);
	
	/**
	 * 新增数据
	 * @param entity
	 */
	public boolean add(T entity);
	
	/**
	 * 根据消息id和用户id标记消息
	 * @param entity
	 */
	public boolean markMessage(T entity);
	
	/**
	 * 根据消息id查数据
	 * @param messageId
	 * @return
	 */
	public List<T> getByMessageId(int messageId);
	
	/**
	 * 根据消息状态和用户id查询
	 * @param messageId
	 * @return
	 */
	public List<T> getByStatusAndUser(T entity);

	/**
	 * 根据用户Id查询数据
	 * @param userId
	 * @return
	 */
	public List<T> getByUserId(Integer userId);
	
}
