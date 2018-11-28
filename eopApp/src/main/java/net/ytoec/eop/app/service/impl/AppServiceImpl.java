package net.ytoec.eop.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import javax.inject.Inject;
import net.ytoec.eop.app.bean.App;
import net.ytoec.eop.app.dao.AppDao;
import net.ytoec.eop.app.service.AppService;
import net.ytoec.kernel.common.Pagination;


@Service
public class AppServiceImpl<T extends App> implements AppService<T> {
	
	@Inject
	private AppDao<T> appDao;

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		return appDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		return appDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		return appDao.remove(entity);
	}

	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		return appDao.get(entity);
	}

	@Override
	public List<T> getAll(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return appDao.getAll(params);
	}

	@Override
	public T get(Integer id) {
		// TODO Auto-generated method stub
		return appDao.get(id);
	}

	@Override
	public boolean remove(Integer id) {
		// TODO Auto-generated method stub
		return appDao.remove(id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pagination getApplist(Pagination pagination,Map map) {
		// TODO Auto-generated method stub
		if(pagination!=null){
			pagination.setTotalRecords(appDao.getTotal(map));
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
			pagination.setRecords(appDao.getPageList(map));
		}	
		
		return pagination;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> getApplist(Pagination pagination, Integer userId) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("userId", userId);
		if(pagination!=null){
			pagination.setTotalRecords(appDao.getTotal(map));
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());		
		}	
		return appDao.getPageList(map);
	}
	

/*	@Override
	public PageList getPageList(PageList page) throws DataAccessException {
		// TODO Auto-generated method stub
		return appDao.getPageList(page);
	}*/
}