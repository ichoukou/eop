package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.CachePrefixConstant;
import net.ytoec.kernel.dao.ChannelDao;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.service.ChannelService;
import net.ytoec.kernel.service.MemcacheService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 渠道信息业务实现类
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
@Service
@Transactional
public class ChannelServiceImpl<T extends Channel> implements ChannelService<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ChannelServiceImpl.class);

	@Autowired
	private MemcacheService<Channel> memcacheService;

	@Inject
	private ChannelDao<T> dao;

	@Override
	public boolean addChannel(T entity) {

		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		memcacheService.add(CachePrefixConstant.CHANNEL
				+ StringUtils.upperCase(entity.getClientId()), entity);
		return dao.addChannel(entity);
	}

	@Override
	public T getChannelById(Integer id) {
		return dao.getChannelById(id);
	}

	@Override
	public List<T> getAllChannel(Pagination pagination, boolean flag) {
		Map map = new HashMap();
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return dao.getAllChannel(map);
	}

	@Override
	public boolean editChannel(T entity) {

		if (entity == null) {

			logger.error("entity is empity");
			return false;
		}
		memcacheService.add(CachePrefixConstant.CHANNEL
				+ StringUtils.upperCase(entity.getClientId()), entity);
		return dao.editChannel(entity);
	}

	@Override
	public boolean removeChannel(T entity) {

		if (entity == null) {

			logger.error("entity is empity");
			return false;
		}
		memcacheService.delete(CachePrefixConstant.CHANNEL
				+ StringUtils.upperCase(entity.getClientId()));
		return dao.removeChannel(entity);
	}

	@Override
	public T getChannelByClientId(String clientId) {
		if (StringUtils.isEmpty(clientId)) {
			logger.error("clientid is empty:" + clientId);
		}
		Channel channel = memcacheService.get(clientId);
		if (channel == null) {
			channel = this.dao.getChannelByClientId(clientId);
		}
		return (T) channel;
	}


}
