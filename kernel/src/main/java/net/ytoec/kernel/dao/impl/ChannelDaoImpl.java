package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ChannelDao;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.mapper.ChannelMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * 渠道信息表 Dao操作类
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
@Repository
public class ChannelDaoImpl<T extends Channel> implements ChannelDao<T> {

	private Logger logger=Logger.getLogger(ChannelDaoImpl.class);
	@Inject
	private ChannelMapper<Channel> mapper;

	@Override
	public boolean addChannel(T entity) {

		boolean flag = false;

		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public T getChannelById(Integer id) {

		Channel channel = new Channel();
		T entity = null;

		try {
			channel.setId(id);
			entity = (T) mapper.get(channel);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public List<T> getAllChannel(Map map) {

		List<T> list = null;
		try {
			list =(List<T>) mapper.getAllChannel(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public boolean editChannel(T entity) {

		boolean flag = false;

		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return flag;
	}

	@Override
	public boolean removeChannel(T entity) {

		boolean flag = false;

		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return flag;
	}

	@Override
	public T getChannelByClientId(String clientId) {

		T result = null;
		try {
			result = (T)((ChannelMapper) mapper.getChannelByClientId(clientId));
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return result;
		
	}
	
	

}
