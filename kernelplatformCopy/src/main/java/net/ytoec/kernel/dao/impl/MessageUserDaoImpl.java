/**
 * 2012-5-7上午10:00:32
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.MessageUserDao;
import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.mapper.MessageUserMapper;

import org.springframework.stereotype.Repository;

/**
 * 消息用户表dao实现类
 * @author wangyong
 * 2012-5-7
 */
@Repository
public class MessageUserDaoImpl<T extends MessageUser> implements
		MessageUserDao<T> {
	
	@Inject
	private MessageUserMapper<T> messageUserMapper;

	/* 
	 * 根据消息id和用户id删除数据
	 * @see net.ytoec.kernel.dao.MessageUserDao#deleteByUserAndMsgId(java.lang.Object)
	 */
	@Override
	public boolean deleteByUserAndMsgId(T entity) {
		boolean flag = false;
		try{
			messageUserMapper.deleteByUserAndMsgId(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	/* 
	 * 新增数据
	 * @see net.ytoec.kernel.dao.MessageUserDao#add(java.lang.Object)
	 */
	@Override
	public boolean add(T entity) {
		boolean flag = false;
		try{
			messageUserMapper.add(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
	
	/* 
	 * 标记消息(根据消息Id和用户id)
	 * @see net.ytoec.kernel.dao.MessageUserDao#add(java.lang.Object)
	 */
	@Override
	public boolean markMessage(T entity) {
		boolean flag = false;
		try{
			messageUserMapper.markMessage(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> getByMessageId(int messageId) {
		return messageUserMapper.getByMessageId(messageId);
	}

	@Override
	public List<T> getByStatusAndUser(T entity) {
		return messageUserMapper.getByStatusAndUser(entity);
	};

	@Override
	public List<T> getByUserId(Integer userId){
		return messageUserMapper.getByUserId(userId);
	}
	
}
