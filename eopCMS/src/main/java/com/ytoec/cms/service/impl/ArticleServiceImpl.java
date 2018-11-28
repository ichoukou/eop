package com.ytoec.cms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.bean.Column;
import com.ytoec.cms.dao.ArticleDao;
import com.ytoec.cms.dao.ColumnDao;
import com.ytoec.cms.service.ArticleService;

@Service
@Transactional
public class ArticleServiceImpl<T extends Article> implements ArticleService<T> {
	
	@Inject
	private ArticleDao<T> articleDao;
	@Inject
	private ColumnDao<Column> columnDao;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		return articleDao.add(object);
	}

	@Override
	public T get(T object){
		// TODO Auto-generated method stub
		return articleDao.get(object);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		return articleDao.edit(object);
	}

	/**
	*物理删除（数据将从数据库中删除）
	*/
	@Override
	public boolean  remove(T object){
		// TODO Auto-generated method stub
		return articleDao.remove(object);
	}
	
	/**
	*逻辑删除
	*/
	@Override
	public boolean delete(Integer id){
		// TODO Auto-generated method stub
		return articleDao.delete(id);
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return articleDao.getAll(param);
	}

	@Override
	public Pagination<T> getPageList(Pagination<T> page,Map<String,Object> params){
		// TODO Auto-generated method stub
		return articleDao.getPageList(page,params);
	}

	@Override
	public Integer getMaxId(){
		// TODO Auto-generated method stub
		return articleDao.getMaxId();
	}

	@Override
	public List<T> getArticlesByCol(Integer columnId){
		// TODO Auto-generated method stub
		return getArticlesByCol(columnId,null,0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Integer id) {
		// TODO Auto-generated method stub
		T art = (T) new Article();
		art.setArticleId(id);
		return get(art);
	}

	@Override
	public List<T> getArticlesByCol(Integer columnId, Integer size) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnId,null,null,size);
	}

	@Override
	public List<T> getArticlesByCol(Integer columnId, String readerType) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnId,readerType,null,0);
	}

	@Override
	public List<T> getArticlesByCol(Integer columnId, String readerType,
			Integer size) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnId,readerType,null,size);
	}
	
	@Override
	public List<T> getArticlesByCol(Integer columnId, String readerType,String status,Integer size) {
		// TODO Auto-generated method stub
		List<T> artList = new ArrayList<T>();
		if(columnId != null){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("columnId", columnId);
			if(StringUtils.isNotBlank(readerType)){
				params.put("readerType", readerType);
			}
			if(StringUtils.isNotBlank(status)){
				params.put("status", status);
			}
			//如果size<=0，则取所有。
			if(size > 0){
				params.put("limit", size);
			}
			artList = getAll(params);
		}
		return artList;
	}

	@Override
	public List<T> getArticlesByCol(String columnCode) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnCode,null,0);
	}

	@Override
	public List<T> getArticlesByCol(String columnCode, Integer size) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnCode,null,null,size);
	}

	@Override
	public List<T> getArticlesByCol(String columnCode, String readerType) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnCode,readerType,null,0);
	}

	@Override
	public List<T> getArticlesByCol(String columnCode, String readerType,
			Integer size) {
		// TODO Auto-generated method stub
		return getArticlesByCol(columnCode,readerType,null,size);
	}
	
	@Override
	public List<T> getArticlesByCol(String columnCode, String readerType,String status,Integer size) {
		// TODO Auto-generated method stub
		List<T> artList = new ArrayList<T>();
		if(StringUtils.isNotBlank(columnCode)){
			Column col = columnDao.retrive(columnCode);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("columnId", col.getColumnId());
			if(StringUtils.isNotBlank(readerType)){
				params.put("readerType", readerType);
			}
			if(StringUtils.isNotBlank(status)){
				params.put("status", status);
			}
			//如果size<=0，则取所有。
			if(size > 0){
				params.put("limit", size);
			}
			artList = getAll(params);
		}
		return artList;
	}

}