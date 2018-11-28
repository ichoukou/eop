/**
 * 2012-5-7上午09:32:43
 * wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * 消息回复Dao
 * @author wangyong
 * 2012-5-7
 */
public interface MessageReplyDao<T> {

	/**
	 * 增加一条新回复
	 * @param entity
	 */
	public boolean addMessageReply(T entity);
	
	/**
	 * 根据消息id获取回复信息列表（支持分页）
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getReplyListByMap(Map map);
	
	/**
	 * 根据消息id统计回复信息数
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countReplyListByMap(Map map);
}
