/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.UnlikeFreightDao;
import net.ytoec.kernel.dataobject.UnlikeFreight;
import net.ytoec.kernel.mapper.UnlikeFreightMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository
public class UnlikeFreightDaoImpl<T extends UnlikeFreight> implements UnlikeFreightDao<T> {

	private static Logger logger = LoggerFactory.getLogger(UnlikeFreightDaoImpl.class);
	@Inject
	private UnlikeFreightMapper<UnlikeFreight> mapper;
	
	@Override
	public boolean addUnlikeFreight(T unlikeFreight) {

		boolean flag = false;
		try{
			mapper.add(unlikeFreight);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getUnlikeFreightById(Integer id) {

		UnlikeFreight errMsg = new UnlikeFreight();
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
	public T getUnlikeFreightByMailNo(String mailNo) {

		UnlikeFreight errMsg = new UnlikeFreight();
		T entity = null;
		try{
			errMsg.setMailNo(mailNo);
			entity = (T)mapper.getUnlikeFreightByMailNo(errMsg);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}
	
	
	@Override
	public List<T> getUnlikeFreightList(Map map) {

		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getUnlikeFreightList(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean removeUnlikeFreight(T unlikeFreight) {

		boolean flag = false;
		try{
			mapper.remove(unlikeFreight);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editUnlikeFreight(T unlikeFreight) {

		boolean flag = false;
		try{
			mapper.edit(unlikeFreight);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

}
