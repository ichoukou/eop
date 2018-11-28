package net.ytoec.kernel.service.impl;

import net.ytoec.kernel.dao.ServerConfigDao;
import net.ytoec.kernel.dataobject.ServerConfig;
import net.ytoec.kernel.service.ServerConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServerConfigServiceImpl<T extends ServerConfig> implements
		ServerConfigService<T> {

	@Autowired
	ServerConfigDao<T> serverConfigDao;

	@Override
	public T getConfigByServerId(Integer serverId) {
		return serverConfigDao.getConfigByServerId(serverId);
	}

}
