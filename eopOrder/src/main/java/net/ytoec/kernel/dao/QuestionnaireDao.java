package net.ytoec.kernel.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;


import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.mapper.QuestionnaireMapper;

import org.springframework.dao.DataAccessException;

/**
 * 问题单dao层接口
 * @author Wangyong
 * @2011-8-1
 * net.ytoec.kernel.dao
 * @param <T>
 */
public interface QuestionnaireDao<T> {
	
	/**
	 * 更新淘宝号到问题件表中
	 * @param params
	 */
	public void updateBuyerNickToQuestionaire(Map<String, Object> params);

	/**
	 * 增加问题单
	 * @param questionnaire
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addQuestionnaire(T questionnaire) throws DataAccessException;
	
	/**
	 * 删除问题单
	 * @param questionnaire
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeQuestionnaire(T questionnaire) throws DataAccessException;
	
	/**
	 * 更新问题单
	 * @param questionnaire
	 * @return
	 * @throws DataAccessException
	 */
	public boolean editQuestionnaire(T questionnaire) throws DataAccessException;
	
	/**
	 * 根据问题单Id查询问题单对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getQuestionnaireById(Integer id) throws DataAccessException;
	
	/**
	 * 标记处理问题单
	 * @param questionnaire
	 * @return
	 * @throws DataAccessException
	 */
	public boolean dealQuestionnaire(T questionnaire) throws DataAccessException;
	
	/**
	 * 根据网点用户Id查询问题单列表
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getQuestionnaireByBranchId(Map map) throws DataAccessException;
	
	/**
	 * 查询所有问题单列表
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllQuestionnaire() throws DataAccessException;
	
	/**
	 * 查询没有VIPID的问题件
	 * @param map
	 * @return
	 */
	public List<T> getNoVipIdList(Map map) throws DataAccessException;
	
	public void updateVipId(T entity);

	/**
	 * 根据运单号查询问题件
	 * 
	 * @param mailNO
	 * @return	返回对象列表
	 */
	public List<T> getQsnByMailNo(String mailNO) throws DataAccessException;

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
	 * 批量更新问题件
	 */
	public void batchUpdateQuestionnaire(List<Questionnaire> questionnaires);
	
	/**
	 * 根据dealtime查询
	 */
	public List<Questionnaire> getQuestionnaireByDealTime(Date dealTime, Integer limit);
	/**
	 * 根据ID
	 */
	public List<Questionnaire> getQestionnaireById(Integer id, Integer limit);
	
	/**
     * 更新问题件的金刚状态
     */
    public Integer updateIssueStatusByissueId(Questionnaire questionnaire);

	/**
	 * 更新orderStatus字段
	 */
	public Integer updateOrderStatusByMailNo(Map<String, Object> map);
	
	/**
	 * 问题件 智能查件问题件标示
	 * @param map
	 * @return
	 */
	public String wayQuestionnaireManageList(Map map);
	
	/**
	 * 根据customerId查询问题件
	 * @param map
	 * @return
	 */
	public List<Questionnaire> selectQuesByCustomerId(Map map);
	
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
