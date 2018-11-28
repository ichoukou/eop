/**
 * 2012-5-7上午10:12:21
 * wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.common.Pagination;

/**
 * 消息回复业务逻辑处理
 * @author wangyong
 * 2012-5-7
 */
public interface MessageReplyService<T> {

	/**
	 * 增加一条新回复
	 * @param entity
	 */
	public boolean addMessageReply(T entity);
	
	/**
	 * 根据消息id获取回复信息列表（支持分页）
	 * @param msgId
	 * @param pagination
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getReplyListByMap(Integer msgId, Pagination pagination);
	
	/**
	 * 根据消息id统计回复信息数
	 * @param msgId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countReplyListByMap(Integer msgId);
}
