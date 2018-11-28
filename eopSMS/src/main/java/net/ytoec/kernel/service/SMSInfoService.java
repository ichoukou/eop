package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.SMSInfoExpand;
import net.ytoec.kernel.dataobject.User;

/**
 * 短信的详细信息
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSInfoService {

	/**
	 * SERVICE_TYPE 目前短信查询的6种类型
	 */
	public static final String SERVICE_TYPE_GOD = "GOT"; //发货提醒
	public static final String SERVICE_TYPE_SENT_SCAN = "SENT_SCAN";  //派件提醒
	public static final String SERVICE_TYPE_SIGNED = "SIGNED";   //签收提醒
	public static final String SERVICE_TYPE_QUEST = "QUEST";   //问题件提醒
	public static final String SERVICE_TYPE_AGING = "AGING";   //时效提醒
	public static final String SERVICE_TYPE_AGENT = "AGENT";   //智能查件
	
	/**
	 * 往短信表中和短信队列表中插入记录
	 * params : 
	 * 		status  发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
	 * 		userId  卖家的主帐号ID, 如果是分仓帐号，则是平台帐号id，如果是入驻企业，则是当前用户的userID
	 * 		createUserId   当前用户的userID
	 * 		buyMobile  买家的手机号码
	 * 		mailNo     运单号
	 * 	    buyName    买家姓名 
	 * 		txLogisticId  物流号
	 * @throws Exception 
	 */
	public void saveInfoToQueue(Map<String, Object> params) throws Exception; 
	
	/**
	 *  往短信表中和短信队列表中插入记录[问题件,通知客户专用]
	 * @param currentUser  当前用户
	 * @param buyerName    卖家姓名
	 * @param buyerMobile  卖家手机号
	 * @param bMailno  	          运单号
	 * @param content      发送内容
	 * @return
	 */
	public boolean saveInfoToQueueForCustomerSend(User currentUser,String buyerName,String buyerMobile,String bMailno,String content);
	
	/**
	 *  往短信表中和短信队列表中插入记录
	 * @param currentUser  当前用户
	 * @param buyerName    卖家姓名
	 * @param buyerMobile  卖家手机号
	 * @param bMailno  	          运单号
	 * @param content      发送内容
	 * @param serviceName   服务名称
	 * @return
	 */
	public boolean saveInfoToQueue(User currentUser,String buyerName,String buyerMobile,String bMailno,String content,String serviceName);
		
	/**
	 * 往短信表中和短信队列表中插入记录[短信营销专用]
	 * 		   userId:卖家主ID
	 *        content:模版内容
	 *       shopName:网店名称
	 *		smsTypeId:短信类型ID
	 *     templateId:短信模版ID
	 *        buyName:买家姓名
	 *        mobile:买家手机号
	 *      loginName:买家登录名  
	 * @throws Exception 
	 */
	public void saveInfoToQueueForMarket(Map<String, Object> params) throws Exception;
	
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
	public List<SMSInfoExpand> getSMSInfoByPamams(Map<String, Object> params);
	
	/**
	 * 获取短信数量  (发送成功)
	 * @param userIds 			List<Integer> (此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id				
	 * @param type		当天是0，当月是1
	 * @return
	 */
	public Integer getCountByToday(List<Integer> userIds, String type);
	
	/**
	 * 添加数据
	 * @param smsInfo
	 * @return
	 */
	public boolean addSMSInfo(SMSInfo smsInfo);
	
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
	public boolean updateSMSInfo(SMSInfo smsInfo);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public SMSInfo getSMSInfoById(Integer id);

	/**
	 * 保存短信记录
	 * @param info 短信详细信息
	 * @author yangqinghua
	 * @return
	 */
	public boolean saveMsg(SMSInfo info);

	/**
	 * 问题件通知发送成功后修改短息表状态和数量
	 * @param info1
	 * @return
	 */
	public boolean updateSMSInfoById(SMSInfo info1);

	/**
	 * 问题件通知发送失败时修改短信信息
	 * @param info1
	 * @return
	 */
	public boolean updateErrorSendById(SMSInfo info1);
	
	/**
	 * 获取查询使用的用户ID
	 * 分3类ID  第一类：主账户ID（父ID）  第二类：子账户ID（父ID不存在的情况下使用） 第三类：平台ID (分仓使用平台)
	 * @return
	 */
	public int getUseUserId(User currentUser);
	
	/**
	 *  往短信表中和短信队列表中插入记录[时效提醒,通知客户专用]
	 * @param currentUser  当前用户
	 * @param buyerName    卖家姓名
	 * @param buyerMobile  卖家手机号
	 * @param bMailno      运单号
	 * @param content      发送内容
	 * @return
	 */
	public boolean saveInfoForRemind(User currentUser,String buyerName,String buyerMobile,String bMailno,String content);
}