/**
 * 2012-5-7上午10:34:45
 * wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;

/**
 * @author wangyong
 * 2012-5-7
 */
public interface MessageUserService<T> {

	/**
	 * 根据消息id和用户id删除记录
	 * @param messageId
	 * @param userId
	 * @return 
	 */
	public boolean deleteByUserAndMsgId(Integer messageId, Integer userId);
	
	/**
	 * 新增数据
	 * @param entity
	 */
	public boolean add(T entity);
	
	/**
	 * 根据消息id和用户id标记消息
	 * @param messageId
	 * @param userId
	 * @param messageStatus
	 * @return 
	 */
	public boolean markMessage(Integer messageId, Integer userId, Integer messageStatus);
	
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
	public List<T> getByStatusAndUser(Integer userId, Integer messageStatus);
	
	/**
	 * 根据用户ID查询数据
	 * @param userId
	 * @return
	 */
	public List<T> getByUserId(Integer userId);
	
}

