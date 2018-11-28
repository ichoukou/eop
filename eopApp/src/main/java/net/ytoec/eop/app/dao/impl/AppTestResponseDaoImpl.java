package net.ytoec.eop.app.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.eop.app.bean.AppTestResponse;
import net.ytoec.eop.app.dao.AppTestResponseDao;
import net.ytoec.eop.app.mapper.AppTestResponseMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class AppTestResponseDaoImpl<T extends AppTestResponse> implements AppTestResponseDao<T> {
	
	private static Logger logger=LoggerFactory.getLogger(AppTestResponseDaoImpl.class);
	@Inject
	private AppTestResponseMapper<T> appTestResponseMapper;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		try {
			appTestResponseMapper.add(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public T get(Integer id){
		// TODO Auto-generated method stub
		return appTestResponseMapper.get(id);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		try {
			appTestResponseMapper.edit(object);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(Integer id){
		// TODO Auto-generated method stub
		try {
			appTestResponseMapper.remove(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return appTestResponseMapper.getAll(param);
	}

	@Override
	public Integer getTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return appTestResponseMapper.getTotal(map);
	}

	@Override
	public List<T> getPageList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return appTestResponseMapper.getPageList(params);
	}

	@Override
	public T get(T object) {
		// TODO Auto-generated method stub
		return get(object.getId());
	}

	@Override
	public boolean remove(T object) {
		// TODO Auto-generated method stub
		return remove(object.getId());
	}
}