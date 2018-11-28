/**
 * 
 */
package net.ytoec.kernel.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.BranchMailNoDTO;
import net.ytoec.kernel.dto.QuestionnaireResultsUpdateDTO;

/**
 * 问题单service接口
 * 
 * @author Wangyong
 * @2011-8-1 net.ytoec.kernel.service
 */
public interface QuestionnaireService<T> {

	/**
	 * 增加问题单
	 * 
	 * @param questionnaire
	 * @return @
	 */
	public boolean addQuestionnaire(T questionnaire);

	/**
	 * 更新问题单
	 * 
	 * @param questionnaire
	 * @return @
	 */
	public boolean editQuestionnaire(T questionnaire);

	/**
	 * 删除问题单
	 * 
	 * @param questionnaire
	 * @return @
	 */
	public boolean removeQuestionnaire(T questionnaire);

	/**
	 * 根据Id获取问题单
	 * 
	 * @param id
	 * @return @
	 */
	public T getQestionnaireById(Integer id);

	/**
	 * 根据mailNo获取问题单
	 * 
	 * @param id
	 * @return @
	 */
	public List<T> getQestionnaireByMailNo(String mailNo);

	/**
	 * 处理问题单：更新处理状态和处理信息及反馈客户信息
	 * 
	 * @param id
	 *            要处理问题单id号
	 * @param feedbackInfo
	 *            反馈客户信息。用于更新处理信息。
	 * @param dealUserId
	 *            问题单处理人id
	 * @return @
	 */
	public boolean dealQuestionnaire(Integer id, String feedbackInfo,
			Integer dealUserId);

	/**
	 * 获取该网点下的VIP的问题单列表
	 * 
	 * @param map
	 * @return @
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getQuestionnaireByBranchId(Map map);

	/**
	 * 根据搜索条件查询:访问dao层时，将查询参数封装进map对象里，map键对应以下参数名。
	 * 
	 * @param siteUser
	 *            当前网点
	 * @param startTime
	 *            寄件时间起点
	 * @param endTime
	 *            寄件时间终点
	 * @param vipId
	 *            VIP用户ID
	 * @param mailNO
	 *            运单号
	 * @param dealStatus
	 *            处理状态
	 * @param pagination
	 *            分页信息
	 * @param flag
	 *            是否分页 true表示分页
	 * @return
	 */
	// public List<T> getQuestionnaireByMapSearch(User siteUser, String
	// startTime, String endTime,
	// String userCode, String mailNO,
	// String dealStatus, Pagination pagination, boolean flag);

	/**
	 * 查询没有VIPID的问题件
	 * 
	 * @param startTime
	 * @return @
	 */
	public List<T> getNoVipIdList(Date startTime, Date endTime);

	public void updateVipId(T entity);

	/**
	 * 同步问题件<br>
	 * 先根据mailNo去表中查数据，如果存在，就更新所有字段； 否则新增<br>
	 * 
	 * @param qsn
	 * @param nextStartTime
	 *            timer下次启动时查询数据的起始时间
	 */
	public void syncQuestionnaire(T qsn, String nextStartTime);

	/**
	 * 根据运单号查找客户信息<br>
	 * 根据运单号查找订单信息，然后根据订单Id查找交易人信息，再刷选出收件人为客户返回<br>
	 * 否则返回空对象
	 * 
	 * @param mailNo
	 * @return
	 */
	public TraderInfo getCustomerInfo(String mailNo);

	/**
	 * 网点问题件管理查询列表方法
	 * 
	 * @param branchId
	 *            网点编码
	 * @param startTime
	 *            上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param userCode
	 *            界面上直客信息用户编码：在业务处理中需要根据用户编码查找custormerId；这里新增其他客户筛选，
	 *            其他客户userCode值为0
	 * @param mailNO
	 *            运单号
	 * @param issueStatus
	 *            金刚状态
	 * @param feedbackInfo
	 *            问题件类型
	 * @param dealStatus
	 *            网点处理状态 ：默认未通知（1），网点已通知（2）,网点移动到其他（3）
	 * @param wdIsRead
	 *            网点未读（0），网点已读（1）
	 * @param sortType
	 *            排序方式：前端页面按上报时间的排序方式：有1(升序) 2（降序）两种
	 * @param contactWay
	 *            买家联系方式
	 * @param buyerName
	 *            买家姓名
	 * @param orderStatus
	 *            订单状态:1(已签收)；0(未签收)
	 * @param pagination
	 *            分页对象
	 * @return
	 */
	public List<T> siteQueryQuestionnaireManageList(User siteUser,
			String startTime, String endTime, String userCode, String mailNO,
			String issueStatus, List<String> feedbackInfo, String dealStatus,
			String wdIsRead, Integer sortType, String contactWay,
			String buyerName, String orderStatus, Pagination pagination);

	/**
	 * 网点问题件管理统计列表方法
	 * 
	 * @param siteUser
	 *            网点
	 * @param startTime
	 *            上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param userCode
	 *            界面上直客信息用户编码：在业务处理中需要根据用户编码查找custormerId；这里新增其他客户筛选，
	 *            其他客户userCode值为0
	 * @param mailNO
	 *            运单号
	 * @param issueStatus
	 *            金刚状态
	 * @param feedbackInfo
	 *            问题件类型
	 * @param dealStatus
	 *            网点处理状态 ：默认未通知（1），网点已通知（2）,网点移动到其他（3）
	 * @param wdIsRead
	 *            网点未读（0），网点已读（1）
	 * @param contactWay
	 *            买家联系方式
	 * @param buyerName
	 *            买家姓名
	 * @param orderStatus
	 *            订单状态:1(已签收)；0(未签收)
	 * @return
	 */
	public int siteCountQuestionnaireManageList(User siteUser,
			String startTime, String endTime, String userCode, String mailNO,
			String issueStatus, List<String> feedbackInfo, String dealStatus,
			String wdIsRead, String contactWay, String buyerName,
			String orderStatus);

	/**
	 * 卖家问题件管理查询列表方法
	 * 
	 * @param userCode
	 *            用户编码：在使用关联账号查询时如果要查询所有该用户下的问题件时用这个条件查找所有的customerId.
	 *            不使用这个条件时该条件设置为null
	 * @param customerId
	 *            单个customerId查询条件.userCode和customerId每次传参的时候有且仅有一个必须为null
	 * @param startTime
	 *            上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param mailNO
	 *            运单号
	 * @param feedbackInfo
	 *            问题件类型
	 * @param vipStatus
	 *            卖家未处理（1），卖家已处理（2），卖家移动到其他（3）（注：卖家只关注网点已通知状态下的问题件）
	 * @param mjIsRead
	 *            卖家未读（0），卖家已读（1）
	 * @param sortType
	 *            排序方式：前端页面按上报时间的排序方式：有 1(升序) 2（降序）两种
	 * @param contactWay
	 *            买家联系方式
	 * @param buyerName
	 *            买家姓名
	 * @param pagination
	 *            分页对象
	 * @param orderStatus
	 *            订单状态:1(已签收)；0(未签收)
	 * @param tagId
	 *            标签标示
	 * @return
	 */
	public List<T> vipQueryQuestionnaireManageList(List userCode,
			String customerId, String startTime, String endTime, String mailNO,
			List<String> feedbackInfo, String vipStatus, String mjIsRead,
			Integer sortType, String contactWay, String buyerName,
			String orderStatus, Integer tagId, Pagination pagination);

	/**
	 * 卖家问题件管理统计列表方法
	 * 
	 * @param userCode
	 *            用户编码：在使用关联账号查询时如果要查询所有该用户下的问题件时用这个条件查找所有的customerId.
	 *            不使用这个条件时该条件设置为null
	 * @param customerId
	 *            单个customerId查询条件.userCode和customerId每次传参的时候有且仅有一个必须为null
	 * @param startTime
	 *            上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param mailNO
	 *            运单号
	 * @param feedbackInfo
	 *            问题件类型
	 * @param vipStatus
	 *            卖家未处理（1），卖家已处理（2），卖家移动到其他（3）（注：卖家只关注网点已通知状态下的问题件）
	 * @param mjIsRead
	 *            卖家未读（0），卖家已读（1）
	 * @param contactWay
	 *            买家联系方式
	 * @param buyerName
	 *            买家姓名
	 * @param orderStatus
	 *            订单状态:1(已签收)；0(未签收)
	 * @param tagId
	 *            标签标示
	 * @return
	 */
	public int vipCountQuestionnaireManageList(List userCode,
			String customerId, String startTime, String endTime, String mailNO,
			List<String> feedbackInfo, String vipStatus, String mjIsRead,
			String contactWay, String buyerName, String orderStatus,
			Integer tagId);

	/**
	 * 移动问题件：业务逻辑是网点将问题件移动至其他，网点的状态是dealStatus；卖家将问题件移动至标签，卖家状态是vipStatus;
	 * 
	 * @param curUser
	 * @param questionId
	 * @param quesStatus
	 *            :移动至的状态：网点处理状态
	 *            ：默认未通知（1），网点已通知（2）,网点移动到其他（3）；卖家未处理（1），卖家处理中（2），卖家已处理（3）
	 * @return
	 */
	public boolean moveQuestion(User curUser, Integer questionId,
			Integer quesStatus, Integer tagId);

	/**
	 * 标记为已读或者未读:wdIsRead 网点未读（0），网点已读（1）；mjIsRead 卖家未读（0），卖家已读（1）。
	 * 
	 * @param curUser
	 * @param questionId
	 * @param readStatus
	 * @return
	 */
	public boolean markQuestion(User curUser, Integer questionId,
			String readStatus);

	public void getQuestionnaireFromJIinGang(ResultSet resultSet, String strdate)
			throws SQLException;

	public void getQuestionnaireFromJIinGang(List<Questionnaire> list)
			throws SQLException;

	/**
	 * 根据dealTime查询问题件
	 * 
	 * @param dealTime
	 * @param limit
	 * @throws SQLException
	 */
	public List<Questionnaire> getQuestionnaireByDealTime(Date dealTime,
			Integer limit) throws SQLException;

	/**
	 * 根据Id获取问题单
	 * 
	 * @param id
	 * @return @
	 */
	public List<Questionnaire> getQestionnaireById(Integer id, Integer limit);

	/**
	 * 更新问题件
	 * 
	 * @param questionnairesList
	 */
	public void updateQuestionnaire(List<Questionnaire> questionnairesList);

	/**
	 * 用于将问题件的处理结果同步给金刚
	 * 
	 * @param questionnaireResultsUpdateDTO
	 */
	public QuestionnaireResultsUpdateDTO updateQuestionnaireResults(
			QuestionnaireResultsUpdateDTO ques, boolean isSys);

	/**
	 * 根据运单号查询网点信息
	 * 
	 * @param mailNo
	 * @return xml
	 */
	public List<BranchMailNoDTO> searchOrgCodeByMailNo(String[] mailNo);

	/**
	 * 网点用户根据当前用户和运单号获取问题件
	 * 
	 * @param curUser
	 * @param mailNO
	 * @return
	 */
	public List<T> siteQueryQuestionnaire(User curUser, String mailNO);

	/**
	 * 卖家用户根据当前用户和运单号获取问题件
	 * 
	 * @param vipUser
	 * @param mailNO
	 * @return
	 */
	public List<T> vipQueryQuestionnaire(User vipUser, String mailNO);

	/**
	 * 将问题件移动到指定的标签下,如果当前tagId对应的标签是“已处理”的话则将问题件移动为已处理状态下
	 * 
	 * @param curUser
	 * @param questionIds
	 * @param tagId
	 * @return
	 */
	public boolean moveQuestion(User curUser, Integer[] questionIds,
			Integer tagId);

	/**
	 * 问题件导出功能
	 * 
	 * @param bindedId
	 * @param starttime
	 * @param endtime
	 * @param mailNO
	 * @param questionType
	 * @param tabStatus
	 * @param contactWay
	 * @param buyerName
	 * @param isShowSigned
	 * @param tagId
	 * @return
	 */
	public String export(List<String> bindedId, String starttime,
			String endtime, String mailNO, List<String> questionType,
			String tabStatus, String contactWay, String buyerName,
			Integer isShowSigned, Integer tagId);

	/**
	 * 智能查件 网点用户获取问题件
	 * 
	 * @param siteUser
	 *            网点
	 * @param mailNO
	 * @return
	 */
	public String siteWayQuestionnaire(User siteUser, String mailNO);

	/**
	 * 卖家用户根据当前用户和运单号获取问题件
	 * 
	 * @param vipUser
	 * @param mailNO
	 * @return
	 */
	public String vipWayQuestionnaire(User vipUser, String mailNO);
}
