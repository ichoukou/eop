package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.WarnValueDao;
import net.ytoec.kernel.dataobject.WarnValue;
import net.ytoec.kernel.mapper.WarnValueMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class WarnValueDaoImpl<T extends WarnValue> implements WarnValueDao<T> {

	private static Logger logger = LoggerFactory.getLogger(WarnValueDaoImpl.class);
	
	@Inject
	private WarnValueMapper<T> mapper;
	
	@Override
	public boolean addWarnValue(T entity) throws DataAccessException {
		System.out.print("添加预警值");
		
		boolean b = false;
		if(entity.getDestination() == null || entity.getWarnValue() == null)return b;
		
		try {
			mapper.add(entity);
			b = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			b = false;
		}
		return b;
	}

	@Override
	public boolean removeWarnValue(T entity) throws DataAccessException {
		logger.info("单个删除预警值");
		boolean b = false;
		if(entity.getDestination() == null || entity.getSellerId() == null)return b;
		try{
			mapper.remove(entity);
			b = true;
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			b = false;
		}
		
		return b;
	}

	@Override
	public boolean editWarnValue(T entity) throws DataAccessException {
		logger.info("修改卖家预警值");
		boolean b = false;
		try{
			mapper.edit(entity);
			b = true;
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			b = false;
		}
		
		return b;
	}

	@Override
	public List<T> searchWarnValueMore(T entity) throws DataAccessException {
		logger.info("根据条件查询批量预警值");
		
		List<T> list =  null;
		try{
			//if(entity.getBegin() == null || entity.getEnd() == null)list = mapper.getAllList(entity);
			//else 
				list = mapper.getList(entity);
			
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public T searchWarnValueOne(T entity) throws DataAccessException {
		logger.info("根据条件查询单个预警值_update");
		
		T t = null;
		try{
			t = mapper.get(entity);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return t;
	}

	@Override
	public T searchWarnValueOneAdd(T entity) throws DataAccessException {
		logger.info("根据条件查询单个预警值_add");
		
		T t = null;
		try{
			t = mapper.getForAdd(entity);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return t;
	}

	

}
