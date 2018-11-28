package com.ytoec.cms.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.dao.ArticleDao;
import com.ytoec.cms.mapper.ArticleMapper;

@Repository
public class ArticleDaoImpl<T extends Article> implements ArticleDao<T> {
	private static Logger logger=LoggerFactory.getLogger(ArticleDaoImpl.class);
	@Inject
	private ArticleMapper<T> articleMapper;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		try {
			articleMapper.add(object);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("添加文章时出错："+e);
			return false;
		}
		return true;
	}

	@Override
	public T get(T object){
		// TODO Auto-generated method stub
		return articleMapper.get(object);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		try {
			articleMapper.edit(object);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("修改文章时出错："+e);
			return false;
		}
		return true;
	}
	
	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(T object){
		// TODO Auto-generated method stub
		try {
			articleMapper.remove(object);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("删除文章时出错："+e);
			return false;
		}
		return true;
	}
	
	/**
	*逻辑删除
	*/
	@Override
	public boolean delete(Integer id){
		// TODO Auto-generated method stub
		try {
			articleMapper.delete(id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error("删除文章时出错："+e);
			return false;
		}
		return true;
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return articleMapper.getAll(param);
	}

	@Override
	public Pagination<T> getPageList(Pagination<T> page,Map<String, Object> params){
		// TODO Auto-generated method stub
		if (page != null && page.getPageNum() != null) {
			int curPage = page.getCurrentPage() < 1 ? 1 : page.getCurrentPage();
			params.put("start",(curPage-1)*page.getPageNum());
			params.put("limit", page.getPageNum());
			int total = articleMapper.getTotal(params);
			page.setTotalRecords(total);
			List<T> dateList = (List<T>) articleMapper.getPageList(params);
			page.setRecords(dateList);
		}
		return page;
	}

	@Override
	public Integer getMaxId(){
		// TODO Auto-generated method stub
		return articleMapper.getMaxId();
	}

}