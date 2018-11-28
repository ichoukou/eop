/**
 * 2012-4-17上午11:35:54
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 消息用户关联表映射器
 * @author wangyong
 * 2012-4-17
 */
public interface MessageUserMapper<T extends MessageUser> extends
		BaseSqlMapper<T> {

	/**
	 * 根据消息id和用户id删除记录
	 * @param entity
	 */
	public void deleteByUserAndMsgId(T entity);
	
	/**
	 * 根据消息id和用户id标记消息
	 * @param entity
	 */
	public void markMessage(T entity);
	
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
