package net.ytoec.eop.app.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import net.ytoec.eop.app.bean.AppTest;
import net.ytoec.eop.app.dao.AppTestDao;
import net.ytoec.eop.app.service.AppTestService;
import net.ytoec.kernel.common.Pagination;

@Service
public class AppTestServiceImpl<T extends AppTest> implements AppTestService<T> {
	
	@Inject
	private AppTestDao<T> appTestDao;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		return appTestDao.add(object);
	}

	@Override
	public T get(Integer id){
		// TODO Auto-generated method stub
		return appTestDao.get(id);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		return appTestDao.edit(object);
	}

	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(Integer id){
		// TODO Auto-generated method stub
		return appTestDao.remove(id);
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return appTestDao.getAll(param);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Pagination getPageList(Pagination pagination,Map<String ,Object> map){
		// TODO Auto-generated method stub
		if(pagination!=null){
			pagination.setTotalRecords(appTestDao.getTotal(map));
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
			pagination.setRecords(appTestDao.getPageList(map));
		}	
		
		return pagination;
	}

	@Override
	public T get(T object) {
		// TODO Auto-generated method stub
		return appTestDao.get(object);
	}

	@Override
	public boolean remove(T object) {
		// TODO Auto-generated method stub
		return appTestDao.remove(object);
	}

	@Override
	public Integer getTimes(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return appTestDao.getTimes(params);
	}

}