package net.ytoec.eop.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import javax.inject.Inject;
import net.ytoec.eop.app.bean.AppTest;
import net.ytoec.eop.app.dao.AppTestDao;
import net.ytoec.eop.app.mapper.AppTestMapper;

@Repository
public class AppTestDaoImpl<T extends AppTest> implements AppTestDao<T> {
	
	private static Logger logger=LoggerFactory.getLogger(AppTestDaoImpl.class);
	@Inject
	private AppTestMapper<T> appTestMapper;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		try {
			appTestMapper.add(object);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public T get(Integer id){
		// TODO Auto-generated method stub
		return appTestMapper.get(id);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		try {
			appTestMapper.edit(object);
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
		if(id == null){
			return false;
		}
		try {
			appTestMapper.remove(id);
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
		return appTestMapper.getAll(param);
	}

	@Override
	public List<T> getPageList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return appTestMapper.getPageList(params);
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

	@Override
	public Integer getTotal(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return appTestMapper.getTotal(map);
	}

	@Override
	public Integer getTimes(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return appTestMapper.getTimes(params);
	}

}