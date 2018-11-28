/**
 * ServerThreadDaoImpl.java
 * Wangyong
 * 2011-9-7 下午02:43:50
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ServerThreadDao;
import net.ytoec.kernel.dataobject.ServerThread;
import net.ytoec.kernel.mapper.ServerThreadMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
/**
 * 多线程服务数据对象DB接口实现
 * @author Wangyong
 * @2011-9-7
 * net.ytoec.kernel.dao.impl
 */
@Repository
public class ServerThreadDaoImpl<T extends ServerThread> implements ServerThreadDao<T> {
	
	private Logger logger=Logger.getLogger(ServerThreadDaoImpl.class);
	@Inject
	private ServerThreadMapper<ServerThread> mapper;

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.ServerThreadDao#getLastServerThread()
	 */
	@Override
	public List<T> getLastServerThread() {

		List<T> list = null;
		try{
			list = (List<T>)(List<T>) this.mapper.getLastServerThread();
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.ServerThreadDao#addServerThread(java.lang.Object)
	 */
	@Override
	public boolean addServerThread(T serverThread) {

		boolean flag = false;
		try{
			mapper.add(serverThread);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.ServerThreadDao#removeServerThread(java.lang.Object)
	 */
	@Override
	public boolean removeServerThread(T serverThread) {

		boolean flag = false;
		try{
			mapper.remove(serverThread);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

}
