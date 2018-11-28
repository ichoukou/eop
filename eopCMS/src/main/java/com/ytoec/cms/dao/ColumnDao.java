package com.ytoec.cms.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.BaseDao;

//import com.fulan.common.page.PageList;

public interface ColumnDao<T> extends BaseDao<T>{
	
	public List<T> getAll(Map<String, Object> param);
	
	public Pagination<T> getPageList(Pagination<T> page,Map<String, Object> params);

	public T retrive(String columnCode);
	
	public boolean delete(Integer columnId);
	
}