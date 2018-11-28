package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.SMSInfoExpand;

/**
 * 短信的详细信息
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSInfoDao<T> {
	
	/**
	 * 根据以下参数查看短信是否开通
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean isNotSendByParams(Map<String, Object> params) throws Exception;
	
	 /**
 	 * 根据查询条件获取短信信息数量 
 	 * @param params
 	 *  	 userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id							
	 *	 smsTypeId 			(比如发货通知等)
	 *	 smsTemplateId		(此服务对应的模版Id)
	 *	 shopName        	(店铺名称)
	 *	 sendTimeStart     	(发送时间始)
	 *	 sendTimeEnd  	    (发送时间末)
	 *	 buyName       		(姓名)
	 *	 buyMobile     		(电话)
	 *	 mailNo				(运单号)
	 *	 loginName			(旺旺名)   [未查询,通过buyName统一或者性查询]
	 *	 status				(短信状态： 0 ：发送成功, 1 : 等待发送, 2 :发送失败, 3 : 发送失败,再次发送中)
	 * @return
	 */
	public Integer getSMSInfoCountByPamams(Map<String, Object> params);
	
	/**
	 * 添加数据
	 * @param smsInfo
	 * @return
	 */
	public boolean addSMSInfo(T smsInfo);
	
	/**
	 * 删除数据
	 * @param id
	 * @return
	 */
	public boolean deleteSMSInfo(Integer id);
	
	/**
	 * 更新数据
	 * @param smsInfo
	 * @return
	 */
	public boolean updateSMSInfo(T smsInfo);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSInfoById(Integer id);

	/**
	 * 发送短信成功时修改短信信息表
	 * @param info1
	 */
	public boolean updateSMSInfoById(SMSInfo info1);

	/**
	 * 问题件通知发送失败时修改短信信息
	 * @param info1
	 * @return
	 */
	public boolean updateErrorSendById(SMSInfo info1);
	
	/**
	 * 获取主账户ID,以及年月日,获取短信数量
	 * @param params
	 *        userId 主账户ID
	 *        year 年份
	 *        month 月份
	 *        day 日
	 * @return
	 */
	public Integer getCountByToday(Map<String, Object> params);
	
	/**
 	* 根据查询条件获取短信信息列表
 	* @param params
 	*  	 userIds 			List<Integer> 	(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id							
	*	 smsTypeId 			(比如发货通知等)
	*	 smsTemplateId		(此服务对应的模版Id)
	*	 shopName        	(店铺名称)
	*	 sendTimeStart     	(发送时间始)
	*	 sendTimeEnd  	    (发送时间末)
	*	 buyName       		(姓名)
	*	 buyMobile     		(电话)
	*	 mailNo				(运单号)
	*	 loginName			(旺旺名)
	*	 status				(短信状态： 0 ：发送成功, 1 : 等待发送, 2 :发送失败, 3 : 发送失败,再次发送中)
	* @return
	*/
	public List<SMSInfo> getSMSInfoByPamams(Map<String, Object> params);
}
