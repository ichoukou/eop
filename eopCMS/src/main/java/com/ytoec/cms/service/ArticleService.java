package com.ytoec.cms.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.service.BaseService;

import com.ytoec.cms.bean.Article;

public interface ArticleService<T extends Article> extends BaseService<T>{
	
	public List<T> getAll(Map<String, Object> param);
	
	public Pagination<T> getPageList(Pagination<T> page,Map<String, Object> params);

	public boolean delete(Integer id);

	public T get(Integer id);
	
	public Integer getMaxId();
	
	/**
	 * 取某一栏目下的所有文章
	 * @param columnId 栏目ID
	 * @return
	 */
	public List<T> getArticlesByCol(Integer columnId);
	
	/**
	 * 从特定栏目中取一定数量的文章
	 * @param columnId 栏目ID
	 * @param size 所取的数量。如果参数size<=0，则取所有。
	 * @return
	 */
	public List<T> getArticlesByCol(Integer columnId,Integer size);
	
	/**
	 * 从特定栏目取允许特定类型用户查看的文章
	 * @param columnId 栏目ID
	 * @param readerType 允许的用户类型。如果为null或为空，则取所有类型
	 * @return
	 */
	public List<T> getArticlesByCol(Integer columnId,String readerType);
	
	/**
	 * 从特定栏目取一定数量的允许特定类型用户查看的文章
	 * @param columnId 栏目ID
	 * @param readerType 允许的用户类型。如果为null或为空，则取所有类型
	 * @param size 所取的数量。如果参数size<=0，则取所有。
	 * @return
	 */
	public List<T> getArticlesByCol(Integer columnId,String readerType,Integer size);

	/**
	 * 从特定栏目取一定数量的允许特定类型用户查看的文章
	 * @param columnId 栏目ID
	 * @param readerType 允许的用户类型。如果为null或为空，则取所有类型
	 * @param size 所取的数量。如果参数size<=0，则取所有。
	 * @return
	 */
	public List<T> getArticlesByCol(Integer columnId,String readerType,String status,Integer size);

	/**
	 * 取某一栏目下的所有文章
	 * @param columnCode 栏目Code
	 * @return
	 */
	public List<T> getArticlesByCol(String columnCode);
	
	/**
	 * 从特定栏目中取一定数量的文章
	 * @param columnCode 栏目Code
	 * @param size 所取的数量。如果参数size<=0，则取所有。
	 * @return
	 */
	public List<T> getArticlesByCol(String columnCode,Integer size);
	
	/**
	 * 从特定栏目取允许特定类型用户查看的文章
	 * @param columnCode 栏目Code
	 * @param readerType 允许的用户类型。如果为null或为空，则取所有类型
	 * @return
	 */
	public List<T> getArticlesByCol(String columnCode,String readerType);
	
	/**
	 * 从特定栏目取一定数量的允许特定类型用户查看的文章
	 * @param columnCode 栏目Code
	 * @param readerType 允许的用户类型。如果为null或为空，则取所有类型
	 * @param size 所取的数量。如果参数size<=0，则取所有。
	 * @return
	 */
	public List<T> getArticlesByCol(String columnCode,String readerType,Integer size);
	
	/**
	 * 从特定栏目取一定数量的允许特定类型用户查看的文章
	 * @param columnCode 栏目Code
	 * @param readerType 允许的用户类型。如果为null或为空，则取所有类型
	 * @param status 
	 * @param size 所取的数量。如果参数size<=0，则取所有。
	 * @return
	 */
	public List<T> getArticlesByCol(String columnCode,String readerType,String status,Integer size);
	
}