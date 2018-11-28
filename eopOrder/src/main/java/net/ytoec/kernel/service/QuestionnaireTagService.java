/**
 * 
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * 问题件标签service接口
 * @author wangjianzhong
 * @2012-8-2
 * net.ytoec.kernel.service
 */
public interface QuestionnaireTagService<T> {

	/**
	 * 新增问题件标签
	 * @param questionnaireTag
	 * @return @
	 */
	public boolean addQuestionnaireTag(T questionnaireTag);
	
	/**
	 * 修改问题件标签
	 * @param questionnaireTag
	 * @return @
	 */
	public boolean editQuestionnaireTag(T questionnaireTag);
	
	/**
	 * 根据标签对象查询标签
	 * @param entity
	 * @return
	 */
	public List<T> getQestionnaireTags(T entity);
	
	/**
	 * 根据Id获取问题单标签
	 * @param id
	 * @return @
	 */
	public T getQestionnaireTag(Integer id);
	
	/**
	 * 获取指定用户的所有问题件标签
	 * @param userCode  
	 * @return
	 */
	public List<T> getQestionnaireTagsByUserThreadId(Integer userThreadId);
	
	/**
	 * 获取指定用户的所有问题件标签
	 * @param userCode  
	 * @return
	 */
	public List<T> getQestionnaireTagsByUserCode(String userCode);
	
	/**
	 * 获取指定用户的所有问题件标签,若,默认标签不存在,并且创建默认标签
	 * @param userCode  
	 * @return
	 */
	public List<T> getQestionnaireTagsByUserCodeAndCreateDef(String userCode);
	
	/**
	 * 获取指定用户的所有问题件标签数量
	 * @param userCode  
	 * @return
	 */
	public Integer getQestionnaireTagsCountByUserCode(String userCode);
	/**
	 * 获取指定用户的所有问题件标签数量
	 * @param userCode  
	 * @return
	 */
	public Integer getQestionnaireTagsCountByUserThreadId(Integer userThreadId);
	
	/**
	 * 新增问题件标签
	 * @param userCode 用户ID tagName 标签名称
	 * @return @  map:err=1 数量超过最大数  err=2 名称重复  err=3 保存错误 err=4 userCode查询错误  err=null 保存成功 res = 保存的对象
	 */
	public Map<String,Object> addQuestionnaireTag(String userCode,String tagName); 
	
	/**
	 * 问题件标签名称是否唯一
	 * resMap 验证返回参数
	 * @param userCode 用户ID tagName 标签名称 quondamId 原ID,如修改验证需要传修改对的ID
	 * @return @
	 */
	public boolean isUniquenessTagName(Map<String,Object> resMap,Integer userThreadId,String tagName,Integer quondamId); 
	
	/**
	 * 删除问题件标签
	 * @param id
	 * @return @ 0:删除成功 1：删除失败  2：该问题件标签,被其他标签使用中
	 */
	public int removeQuestionnaireTag(Integer id);
	
	/**
	 * 修改问题件标签
	 * @param userCode , id  , tagName
	 * @return map:err=1 数量超过最大数  err=2 名称重复  err=3 保存错误 err=4 userCode查询错误  err=null 修改成功 res = 修改的对象
	 */
	public Map<String,Object> editQuestionnaireTagName(String userCode,Integer id,String tagName);
		
	/**
	 * 交换问题件标签位置
	 * @param beforeTagId,afterTagId 需要交换的两个标签ID
	 * @return @
	 */
	public boolean swapQuestionnaireTag(Integer beforeTagId,Integer afterTagId);
	
	/**
	 * 移动问题件标签位置
	 * @param id 标签ID  
	 * @param step 移动步数   0为不动, -1,-2,-3 分别代表上移动1,2,3    1,2,3 分别代表下移动1,2,3 [若移动步数超过最大,则为最上,或最下]
	 * @return @
	 */
	//public boolean moveQuestionnaireTag(Integer id,Integer step);
	
	/**
	 * 获取问题件类型（tagType=-1的已处理问题件  该问题件不可删除,属于每个用户的唯一不可删除不可修改问题件）
	 */
	public int getQuestionnaireTagType(String userCode);
}
