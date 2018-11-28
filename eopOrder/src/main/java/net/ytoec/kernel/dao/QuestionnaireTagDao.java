/**
 * 
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * 问题件标签DAO接口
 * @author wangjianzhong
 * @2012-8-2
 * net.ytoec.kernel.service
 */
public interface QuestionnaireTagDao<T> {
	/**
	 * 新增问题件标签
	 * @param questionnaireTag
	 * @return @
	 */
	public boolean addQuestionnaireTag(T entity);
	
	/**
	 * 查询问题件标签数量,在问题件表中
	 * @param id
	 * @return @
	 */
	public Integer getTagCountInQuestionnaire(Integer id);
	
	/**
	 * 删除问题件标签
	 * @param id
	 * @return @
	 */
	public boolean removeQuestionnaireTag(Integer id);
	
	/**
	 * 修改问题件标签
	 * @param questionnaireTag
	 * @return @
	 */
	public boolean editQuestionnaireTag(T entity);
	
	/**
	 * 根据Id获取问题单标签
	 * @param id
	 * @return @
	 */
	public T getQestionnaireTag(Integer id);
	
	/**
	 * 获取指定用户的所有问题件标签
	 * @param userId  
	 * @return
	 */
	public List<T> getQestionnaireTagsByUserThreadId(Integer userThreadId);
	
	/**
	 * 获取指定用户的所有问题件标签数量  
	 * @param userId  
	 * @return
	 */
	public Integer getQestionnaireTagsCountByUserThreadId(Integer userThreadId);	
	
	/**
	 * 根据标签对象查询标签
	 * @param entity
	 * @return
	 */
	public List<T> getQestionnaireTags(T entity);
	
	/**
	 * 获取标签位置的最大值
	 * @param userId
	 * @return
	 */
	public Integer getQestionnaireTagMaxPos(Integer userThreadId);
	
	/**
	 * 获取标签在问题件表的使用数量
	 * @param params
	 * 	      customerId  客户ID 
	 * 		  tagName     标签名称
	 * @return
	 */
	//public Integer getTagCountInQestionnaire(Map params);
}
