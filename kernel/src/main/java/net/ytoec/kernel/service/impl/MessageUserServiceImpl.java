/**
 * 2012-5-7上午10:43:30
 * wangyong
 */
package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.MessageUserDao;
import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.service.MessageUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 * 2012-5-7
 */
@Service
@Transactional
public class MessageUserServiceImpl<T extends MessageUser> implements MessageUserService<T>{
	
	@Inject
	private MessageUserDao<T> messageUserDao;

	@Override
	public boolean deleteByUserAndMsgId(Integer messageId, Integer userId) {
		MessageUser entity = new MessageUser();
		entity.setMessageId(messageId);
		entity.setUserId(userId);
		return messageUserDao.deleteByUserAndMsgId((T)entity);
	}

	@Override
	public boolean add(T entity) {
		return messageUserDao.add(entity);
	}

	@Override
	public boolean markMessage(Integer messageId, Integer userId, Integer messageStatus) {
		MessageUser entity = new MessageUser();
		entity.setMessageId(messageId);
		entity.setUserId(userId);
		entity.setMessageStatus(messageStatus);
		return messageUserDao.markMessage((T)entity);
	}

	@Override
	public List<T> getByMessageId(int messageId) {
		return messageUserDao.getByMessageId(messageId);
	}

	@Override
	public List<T> getByStatusAndUser(Integer userId, Integer messageStatus) {
		if(userId!=null && messageStatus!=null){
			MessageUser entity = new MessageUser();
			entity.setUserId(userId);
			entity.setMessageStatus(messageStatus);
			return messageUserDao.getByStatusAndUser((T)entity);
		}else
			return null;
	}

	@Override
	public List<T> getByUserId(Integer userId) {
		if(userId!=null){
			return messageUserDao.getByUserId(userId);
		} else{
			return null;
		}
	}
	
}
