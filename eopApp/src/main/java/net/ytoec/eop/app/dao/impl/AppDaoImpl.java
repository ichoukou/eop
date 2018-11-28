package net.ytoec.eop.app.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.eop.app.bean.App;
import net.ytoec.eop.app.dao.AppDao;
import net.ytoec.eop.app.mapper.AppMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class AppDaoImpl<T extends App> implements AppDao<T> {

	private static Logger logger = LoggerFactory.getLogger(AppDaoImpl.class);
	@Inject
	private AppMapper<T> appMapper;

	@Override
	public List<T> getAll(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return appMapper.getAll(param);
	}

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		try {
			appMapper.add(entity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("添加应用时出错：" + e);
			return false;
		}
		return true;
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		try {
			appMapper.edit(entity);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("修改应用时出错：" + e);
			return false;
		}
		return true;
	}

	@Override
	public boolean remove(Integer id) {
		// TODO Auto-generated method stub
		if (id == null) {
			return false;
		}
		try {
			appMapper.remove(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("删除应用时出错：" + e);
			return false;
		}
		return true;
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		if (entity == null || entity.getId() == null) {
			return false;
		}
		return remove(entity.getId());
	}

	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		if (entity == null || entity.getId() == null) {
			return null;
		}
		return get(entity.getId());
	}

	@Override
	public T get(Integer id) {
		// TODO Auto-generated method stub
		return appMapper.get(id);
	}

	@Override
	public List<T> getPageList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return appMapper.getPageList(params);
	}

	@Override
	public Integer getTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return appMapper.getTotal(map);
	}

}