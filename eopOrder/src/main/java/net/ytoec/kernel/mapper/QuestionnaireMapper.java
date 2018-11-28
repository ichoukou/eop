/**
 * 
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 问题单处理映射器
 * @author Wangyong
 * @2011-8-1
 * net.ytoec.kernel.mapper
 */
public interface QuestionnaireMapper<T extends Questionnaire> extends BaseSqlMapper<T> {

	/**
	 * 更新淘宝号到问题件表中
	 * @param params
	 */
	public void updateBuyerNickToQuestionaire(Map<String, Object> params);
	
	/**
	 * 处理问题单：
	 * @param entity
	 */
	public void dealQuestionnaire(T entity);
	
	/**
	 * 查询某个网点下VIP的问题单列表
	 * @param map
	 * @return
	 */
	public List<T> getQuestionnaireListByBranchId(Map map);
	
	/**
	 * 查询所有问题单
	 * @return
	 */
	public List<T> getAllQuestionnaire();
	
	/**
	 * 获取没有VIPID的记录
	 * @param map
	 * @return
	 */
	public List<T> getNoVipIdListByMapSearch(Map map);
	
	public void updateVipId(T entity);

	/**
	 * 根据运单号查询问题件
	 * 
	 * @param mailNO
	 * @return	问题件列表
	 */
	public List<T> getQsnByMailNo(String mailNO);

	/**
	 * 根据问题件Id(金刚同步过来的issueId)查询问题件
	 * 
	 * @param issueId
	 * @return	返回单个对象
	 */
	public T getQsnByIssueId(String issueId);
	
	/**
	 * 问题件新改动模块列表查询
	 * @param map
	 * @return
	 */
	public List<T> queryQuestionnaireManageList(Map map);
	
	/**
	 * 对问题件新改动模块列表查询统计总数
	 * @param map
	 * @return
	 */
	public int countQuestionnaireManageList(Map map);
	
	/**
	 * 根据ID更新issue_status
	 */
	public Integer updateQuestionnaireIssueStatus(Questionnaire questionnaire);
	
	/**
	 * 根据dealtime查询
	 * 
	 */
	public List<T> getQuestionnaireByDealTime(Map map);
	/**
	 * 根据ID查询
	 */
	public List<T> getQuestionnaireById(Map map);
	/**
	 * 更新问题件的金刚状态
	 */
	public Integer updateIssueStatusByissueId(Questionnaire questionnaire);
	
	/**
	 * 更新orderStatus字段
	 */
	public Integer updateOrderStatusByMailNo(Map map);
	
	/**
	 * 问题件新改动模块列表查询
	 * @param map
	 * @return
	 */
	public String wayQuestionnaireManageList(Map map);
	
	/**
	 * 根据customerId查询问题件
	 * @param map
	 * @return
	 */
	public List<T> selectQuesByCustomerId(Map map);
	
	/**
	 * 根据问题件Id更新淘宝号
	 * @param questionnaire
	 * @return
	 */
	public Integer updateQuesById(Map<String, Object> params);
	
	/**
	 * 更新问题件的金刚状态，根据Id来更新
	 * @param questionnaire
	 * @return
	 */
	public Integer updateIssueStatusById(Questionnaire questionnaire);
}
