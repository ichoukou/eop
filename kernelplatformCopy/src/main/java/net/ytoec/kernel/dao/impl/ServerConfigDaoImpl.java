package net.ytoec.kernel.dao.impl;

import net.ytoec.kernel.dao.ServerConfigDao;
import net.ytoec.kernel.dataobject.ServerConfig;
import net.ytoec.kernel.mapper.ServerConfigMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServerConfigDaoImpl<T extends ServerConfig> implements
		ServerConfigDao<T> {

	@Autowired
	ServerConfigMapper<T> serverConfigMapper;

	@Override
	public T getConfigByServerId(Integer serverId) {
		// TODO Auto-generated method stub
		return serverConfigMapper.getConfigByServerId(serverId);
	}

}
