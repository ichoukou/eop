package net.ytoec.kernel.service.impl;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dao.AppProviderDao;
import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.service.AppProviderService;

import org.springframework.stereotype.Service;
import javax.inject.Inject;

@Service
public class AppProviderServiceImpl<T extends AppProvider> implements AppProviderService<T> {
	
	@Inject
	private AppProviderDao<T> appProviderDao;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		return appProviderDao.add(object);
	}

	@Override
	public T get(T entriy){
		// TODO Auto-generated method stub
		return appProviderDao.get(entriy);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		return appProviderDao.edit(object);
	}

	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(T object){
		// TODO Auto-generated method stub
		return appProviderDao.remove(object);
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return appProviderDao.getAll(param);
	}

	@Override
	public T getByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return appProviderDao.getByUserId(userId);
	}
}