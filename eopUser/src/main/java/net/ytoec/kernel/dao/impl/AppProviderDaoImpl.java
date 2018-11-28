package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.AppProviderDao;
import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.mapper.AppProviderMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class AppProviderDaoImpl<T extends AppProvider> implements AppProviderDao<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(AppProviderDaoImpl.class);
	
	@Inject
	private AppProviderMapper<T> appProviderMapper;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		try {
			appProviderMapper.add(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("添加应用服务商资料时出错："+e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public T get(T entriy){
		// TODO Auto-generated method stub
		return appProviderMapper.get(entriy);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		try {
			appProviderMapper.edit(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("修改应用服务商资料时出错："+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(T entriy){
		// TODO Auto-generated method stub
		try {
			appProviderMapper.remove(entriy);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("删除应用服务商资料时出错："+e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return appProviderMapper.getAll(param);
	}

	@Override
	public T getByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return appProviderMapper.getByUserId(userId);
	}

}