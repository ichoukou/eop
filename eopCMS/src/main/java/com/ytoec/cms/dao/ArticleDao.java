package com.ytoec.cms.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.BaseDao;

public interface ArticleDao<T> extends BaseDao<T>{
	
	public List<T> getAll(Map<String, Object> param);
	
	public Pagination<T> getPageList(Pagination<T> page,Map<String, Object> params);
	
	public boolean delete(Integer id);
	
	public Integer getMaxId();
	
}