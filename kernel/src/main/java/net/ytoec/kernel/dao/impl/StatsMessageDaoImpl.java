/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.StatsMessageDao;
import net.ytoec.kernel.dataobject.StatsMessage;
import net.ytoec.kernel.mapper.StatsMessageMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;


@Repository
public class StatsMessageDaoImpl<T extends StatsMessage> implements StatsMessageDao<T> {

	private Logger logger=Logger.getLogger(StatsMessageDaoImpl.class);
	@Inject
	private StatsMessageMapper<StatsMessage> mapper;
	
	@Override
	public boolean addStatsMessage(T statsMessage) {

		boolean flag = false;
		try{
			mapper.add(statsMessage);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getStatsMessageById(Integer id) {

		StatsMessage errMsg = new StatsMessage();
		T entity = null;
		try{
			errMsg.setId(id);
			entity = (T)mapper.get(errMsg);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public List<T> getStatsMessageList(Map map) {

		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getStatsMessageList(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean removeStatsMessage(T statsMessage) {

		boolean flag = false;
		try{
			mapper.remove(statsMessage);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editStatsMessage(T statsMessage) {

		boolean flag = false;
		try{
			mapper.edit(statsMessage);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

}
