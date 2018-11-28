package net.ytoec.eop.app.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import net.ytoec.eop.app.bean.AppTestResponse;
import net.ytoec.eop.app.dao.AppTestResponseDao;
import net.ytoec.eop.app.service.AppTestResponseService;
import net.ytoec.kernel.common.Pagination;

@Service
public class AppTestResponseServiceImpl<T extends AppTestResponse> implements AppTestResponseService<T> {
	
	@Inject
	private AppTestResponseDao<T> appTestResponseDao;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		return appTestResponseDao.add(object);
	}

	@Override
	public T get(Integer id){
		// TODO Auto-generated method stub
		return appTestResponseDao.get(id);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		return appTestResponseDao.edit(object);
	}

	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(Integer id){
		// TODO Auto-generated method stub
		return appTestResponseDao.remove(id);
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return appTestResponseDao.getAll(param);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pagination getPageList(Pagination pagination,Map<String ,Object> map){
		// TODO Auto-generated method stub
		if(pagination!=null){
			pagination.setTotalRecords(appTestResponseDao.getTotal(map));
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
			pagination.setRecords(appTestResponseDao.getPageList(map));
		}	
		
		return pagination;
	}

	@Override
	public T get(T object) {
		// TODO Auto-generated method stub
		return appTestResponseDao.get(object);
	}

	@Override
	public boolean remove(T object) {
		// TODO Auto-generated method stub
		return appTestResponseDao.remove(object);
	}

}