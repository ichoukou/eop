package com.ytoec.cms.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.service.BaseService;

import com.ytoec.cms.bean.Column;

public interface ColumnService<T extends Column> extends BaseService<T>{
	
	public List<T> getAll(Map<String, Object> param);
	
	public Pagination<T> getPageList(Pagination<T> page,Map<String, Object> params);

	public T retrive(String columnCode);
	
	public T get(Integer id);
	
	public List<T> sortColumn(List<T> columnList);
	
	public List<T> getColTopLineTree(Integer columnId);
	
	public List<T> getChildList(Integer parentColId);
	
	public boolean delete(Integer columnId);
	
}