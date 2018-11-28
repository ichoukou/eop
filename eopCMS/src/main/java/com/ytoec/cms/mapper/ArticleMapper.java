package com.ytoec.cms.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;

import com.ytoec.cms.bean.Article;

public interface ArticleMapper<T extends Article> extends BaseSqlMapper<T> {
	
	public List<T> getPageList(Map<String, Object> params) throws DataAccessException;

	public List<T> getAll(Map<String, Object> params) throws DataAccessException;
	
	public void delete(Integer id) throws DataAccessException;

	public Integer getTotal(Map<String, Object> map) throws DataAccessException;
	
	public Integer getMaxId() throws DataAccessException;
}