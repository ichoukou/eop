/**
 * 
 */
package net.ytoec.kernel.service;

import java.text.ParseException;
import java.util.List;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.User;

/**
 * 问题件管理综合处理业务接口
 * @author wangyong
 */
public interface QuestionDtoService<T> {

	/**
	 * 网点问题件管理查询列表方法
	 * @param siteUser 网点用户
	 * @param startTime 上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param userCode 界面上直客信息用户编码：在业务处理中需要根据用户编码查找custormerId
	 * @param mailNO 运单号
	 * @param issueStatus 金刚状态
	 * @param feedbackInfo 问题件类型
	 * @param dealStatus 网点处理状态 ：默认未通知（1），网点已通知（2）,网点移动到其他（3）
	 * @param wdIsRead 网点未读（0），网点已读（1）
	 * @param sortType 排序方式：前端页面按上报时间的排序方式：有 1(升序) 2（降序）两种
	 * @param contactWay 买家联系方式
	 * @param buyerName 买家姓名
	 * @param isShowSigned 表示是否显示已签收问题件：1（是） 0（否）
	 * @param pagination 分页对象
	 * @return
	 */
	public List<T> siteGetQuestionList(User siteUser, String startTime, String endTime,
			 String userCode, String mailNO, String issueStatus, List<String> feedbackInfo, String dealStatus, String wdIsRead, Integer sortType, String contactWay, String buyerName, Integer isShowSigned, Pagination pagination);
	
	/**
	 * 网点问题件管理统计列表方法
	 * @param siteUser 网点用户
	 * @param startTime 上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param userCode 界面上直客信息用户编码：在业务处理中需要根据用户编码查找custormerId
	 * @param mailNO 运单号
	 * @param issueStatus 金刚状态
	 * @param feedbackInfo 问题件类型
	 * @param dealStatus 网点处理状态 ：默认未通知（1），网点已通知（2）,网点移动到其他（3）
	 * @param wdIsRead 网点未读（0），网点已读（1）
	 * @param contactWay 买家联系方式
	 * @param buyerName 买家姓名
	 * @param isShowSigned 表示是否显示已签收问题件：1（是） 0（否）
	 * @return
	 */
	public int siteCountQuestionList(User siteUser, String startTime, String endTime,
			String userCode, String mailNO, String issueStatus, List<String> feedbackInfo, String dealStatus, String wdIsRead, String contactWay, String buyerName, Integer isShowSigned);
	
	/**
	 * 卖家问题件管理查询列表方法
	 * @param bindeId 关联customerId：在使用关联账号查询时如果要查询所有该用户下的问题件时用这个条件查找所有的customerId.不使用这个条件时该条件设置为null
	 * @param customerId 单个customerId查询条件.userCode和customerId每次传参的时候有且仅有一个必须为null
	 * @param startTime 上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param mailNO 运单号
	 * @param feedbackInfo 问题件类型
	 * @param vipStatus 卖家未处理（1），卖家已处理（2），卖家移动到其他（3）（注：卖家只关注网点已通知状态下的问题件）
	 * @param mjIsRead 卖家未读（0），卖家已读（1）
	 * @param sortType 排序方式：前端页面按上报时间的排序方式：有 1(升序) 2（降序）两种
	 * @param contactWay 买家联系方式
	 * @param buyerName 买家姓名
	 * @param pagination 分页对象
	 * @param isShowSigned 表示是否显示已签收问题件：1（是） 0（否）
	 * @param tagId 标签标示
	 * @return
	 */
	public List<T> vipGetQuestionList(List bindeId, String customerId, String startTime, String endTime, String mailNO, List<String> feedbackInfo, String vipStatus, String mjIsRead, Integer sortType, String contactWay, String buyerName, Integer isShowSigned, Integer tagId, Pagination pagination);
	
	/**
	 * 卖家问题件管理统计列表方法
	 * @param bindeId 关联customerId：在使用关联账号查询时如果要查询所有该用户下的问题件时用这个条件查找所有的customerId.不使用这个条件时该条件设置为null
	 * @param customerId 单个customerId查询条件.userCode和customerId每次传参的时候有且仅有一个必须为null
	 * @param startTime 上报时间对应金刚同步过来的字段issue_create_time
	 * @param endTime
	 * @param mailNO 运单号
	 * @param feedbackInfo 问题件类型
	 * @param vipStatus 卖家未处理（1），卖家已处理（2），卖家移动到其他（3）（注：卖家只关注网点已通知状态下的问题件）
	 * @param mjIsRead 卖家未读（0），卖家已读（1）
	 * @param contactWay 买家联系方式
	 * @param buyerName 买家姓名
	 * @param isShowSigned 表示是否显示已签收问题件：1（是） 0（否）
	 * @param tagId 标签标示
	 * @return
	 */
	public int vipCountQuestionList(List bindeId, String customerId, String startTime, String endTime,String mailNO, List<String> feedbackInfo, String vipStatus, String mjIsRead, String contactWay, String buyerName, Integer isShowSigned, Integer tagId);
	
	/**
	 * 根据该值查找金刚那边的数据库获取网点沟通记录
	 * @param issueId 问题id, 直接同步金刚表的值
	 * @return 返回是DtoQuestion对象，从DtoQuestion对象的属性quesDealList中获取具体的记录
	 */
	public T getQuesDealList(String issueId);
	
	/**
	 * 统计readStatus状态下的问题件数量:网点的未读问题件在已通知状态里;
	 * 卖家获取未读问题件（在已处理中,默认显示未签收问题件）；
	 * wdIsRead 网点未读（0），网点已读（1）；
	 * mjIsRead 卖家未读（0），卖家已读（1）。
	 * @param curUser
	 * @param readStatus 读取状态
	 * @return
	 */
	public int noneReadQuestion(User curUser, String readStatus) throws ParseException;
	
	/**
	 * 统计卖家或者网点status状态下的问题件数量，默认显示未签收问题件
	 * @param curUser
	 * @param stauts
	 * @return
	 * @throws ParseException
	 */
	public int noneHandleQuestion(User curUser, String stauts) throws ParseException;
	
}
