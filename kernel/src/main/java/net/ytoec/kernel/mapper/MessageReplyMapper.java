/**
 * 2012-4-17上午11:33:35
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.MessageReply;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 消息回复映射器
 * @author wangyong
 * 2012-4-17
 */
public interface MessageReplyMapper<T extends MessageReply> extends BaseSqlMapper<T> {

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
