/**
 * 2012-4-17下午01:36:54
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.MessageDao;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.mapper.MessageMapper;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 消息管理dao实现类
 * @author wangyong
 * 2012-4-17
 */
@SuppressWarnings("all")
@Repository
public class MessageDaoImpl<T extends Message> implements MessageDao<T> {
	
	private Logger logger=Logger.getLogger(MessageDaoImpl.class);
	
	@Inject
	private MessageMapper<Message> mapper;

	@Override
	public boolean addMessage(T entity) throws DataAccessException {
		boolean flag = false;
		try{
			mapper.add(entity);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean updateMessage(T entity) throws DataAccessException{
		boolean flag = false;
		try{
			mapper.updateMessage(entity);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}
	
	@Override
	public boolean remove(T entity) throws DataAccessException{
		boolean flag = false;
		try{
			mapper.remove(entity);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}
	
	@Override
	public T getById(Integer id) throws DataAccessException {
		Message message = new Message();
		T entity = null;
		message.setId(id);
		entity = (T)mapper.get(message);
		return entity;
	}

	@Override
	public List<T> getSellerMessageListByMap(Map map) {
		return (List<T>)mapper.getSellerMessageListByMap(map);
	}

	@Override
	public int countSellerMessageListByMap(Map map) {
		return mapper.countSellerMessageListByMap(map);
	}

	@Override
	public List<T> getSiteMessageListByMap(Map map) {
		return (List<T>)mapper.getSiteMessageListByMap(map);
	}

	@Override
	public int countSiteMessageListByMap(Map map) {
		return mapper.countSiteMessageListByMap(map);
	}

	@Override
	public List<T> getAdminMessageListByMap(Map map) {
		return (List<T>)mapper.getAdminMessageListByMap(map);
	}

	@Override
	public int countAdminMessageListByMap(Map map) {
		return mapper.countAdminMessageListByMap(map);
	}

	@Override
	public List<T> getPlatMessageListByMap(Map map) {
		return (List<T>)mapper.getPlatMessageListByMap(map);
	}

	@Override
	public int countPlatMessageListByMap(Map map) {
		return mapper.countPlatMessageListByMap(map);
	}

}
