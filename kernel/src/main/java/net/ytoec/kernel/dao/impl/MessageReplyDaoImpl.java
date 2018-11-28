/**
 * 2012-5-7上午09:48:56
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.MessageReplyDao;
import net.ytoec.kernel.dataobject.MessageReply;
import net.ytoec.kernel.mapper.MessageReplyMapper;

import org.springframework.stereotype.Repository;

/**
 * @author wangyong
 * 2012-5-7
 */
@Repository
public class MessageReplyDaoImpl<T extends MessageReply> implements MessageReplyDao<T> {
	
	@Inject
	private MessageReplyMapper<T> messageReplyMapper;

	/* 
	 * 在某条消息上新增一条回复
	 * @see net.ytoec.kernel.dao.MessageReplyDao#addMessageReply(java.lang.Object)
	 */
	@Override
	public boolean addMessageReply(T entity) {
		boolean flag = false;
		try{
			messageReplyMapper.add(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	/* 
	 * 获取消息的回复记录
	 * @see net.ytoec.kernel.dao.MessageReplyDao#getReplyListByMap(java.util.Map)
	 */
	@Override
	public List<T> getReplyListByMap(Map map) {
		List<T> list = messageReplyMapper.getReplyListByMap(map);
		return list;
	}

	/* 
	 * 统计获取的消息回复记录数目
	 * @see net.ytoec.kernel.dao.MessageReplyDao#countReplyListByMap(java.util.Map)
	 */
	@Override
	public int countReplyListByMap(Map map) {
		int sum = 0;
		sum = messageReplyMapper.countReplyListByMap(map);
		return sum;
	}

}
