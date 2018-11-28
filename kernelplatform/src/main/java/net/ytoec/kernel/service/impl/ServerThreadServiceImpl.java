/**
 * ServerThreadServiceImpl.java
 * Wangyong
 * 2011-9-7 下午03:03:06
 */
package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ServerThreadDao;
import net.ytoec.kernel.dataobject.ServerThread;
import net.ytoec.kernel.service.ServerThreadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Wangyong
 * @2011-9-7
 * net.ytoec.kernel.service.impl
 */
@Service
@Transactional
public class ServerThreadServiceImpl<T extends ServerThread> implements ServerThreadService<T> {

	private static Logger logger = LoggerFactory.getLogger(ServerThreadServiceImpl.class);
	@Inject
	private ServerThreadDao<T> dao;
	
	/* (non-Javadoc)
	 * @see net.ytoec.kernel.service.ServerThreadService#getLastServerThread()
	 */
	@Override
	public T getLastServerThread() {
		List<T> list = dao.getLastServerThread();

		T entity = null;
		if(list.size() > 0 ){			
			entity = list.get(0);
		}		
		return entity;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.service.ServerThreadService#addServerThread(java.lang.Object)
	 */
	@Override
	public boolean addServerThread(T serverThread) {
		if (serverThread == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.addServerThread(serverThread);
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.service.ServerThreadService#removeServerThread(java.lang.Object)
	 */
	@Override
	public boolean removeServerThread(T serverThread) {
		if (serverThread == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.removeServerThread(serverThread);
	}

}
