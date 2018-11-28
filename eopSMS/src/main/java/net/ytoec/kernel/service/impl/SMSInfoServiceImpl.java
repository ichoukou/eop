package net.ytoec.kernel.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dao.SMSInfoDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dao.impl.SMSServiceAreaDaoImpl;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.SMSInfoExpand;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.SMSServiceArea;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.SendErrCodeSwitch;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.SMSBuyersService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SMSTemplateService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author shitianzeng
 * 2012-07-05
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSInfoServiceImpl implements SMSInfoService {

	@Inject
	private SMSInfoDao dao;
	@Inject
	private UserDao userDao;
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private SMSServiceService  smsServiceService;
	@Inject
	private SMSTemplateService  smsTemplateService;
	@Inject
	private SMSBuyersService  smsBuyersService;
	@Inject
	private SMSInfoDao smsInfoDao;
	@Inject
	private OrderService orderService;
	@Inject
	private DredgeServiceDao dredgeServiceDao ;
	@Inject
	private SMSServiceAreaDaoImpl sMSServiceAreaDao;

	protected final Logger logger = LoggerFactory
			.getLogger(SMSInfoServiceImpl.class);
	
	/**
	@Override
	public void saveInfoToQueue(Map<String, Object> params) throws Exception {
		// 如果是卖家，则是卖家主帐号的userID, 若是分仓帐号，则是平台帐号userId，若是入驻企业则是当前用户ID
		Integer userId = (Integer)params.get("userId");
		
		// 当前用户的userID
		Integer createUserId = (Integer)params.get("createUserId");
		
		//根据用户,判断
		User user = (User)userDao.getUserById(userId); 
		
		// 买家的运单号、电话、姓名
		String mailNo = (String)params.get("mailNo");
		String mobile = (String)params.get("buyMobile");
		String buyerName = (String)params.get("buyName");
		
		// 订单的状态 : 发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
		String status = (String)params.get("status");
		String txLogisticId = (String)params.get("txLogisticId");
		
		logger.error("短信发送进入service：物流号：" + txLogisticId + "用户ID：" + user.getId() + "订单状态:" + status );
		// 判断短信是否已经发送 根据status，mailNo，电话
		boolean isNotSend = smsInfoDao.isNotSendByParams(params);
		logger.error("saveInfoToQueue:isNotSend:"+isNotSend);
		if(isNotSend) {
				
			//查询该用户,获取判读
			/*   入驻企业现使用,自身ID,不需获取平台ID
			User user = (User)userDao.getUserById(userId); 
			if(user!=null) {
				if("C".equals(user.getChildType())) {  //B:分仓 C:企业  企业使用平台ID
					String theUserId = user.getUserSource();
					if(StringUtils.hasText(theUserId)) {
						userId = Integer.parseInt(theUserId);
					}
				}
			}
			
			
			// 获取短信类型、模版和订单
			SMSTemplate template = smsTemplateService.getDefaltTemplate(this.isOpen(params));
			//获取开启的服务
			SMSService service = smsServiceService.getOpeanService(createUserId, status);
			//SMSService service = smsServiceService.getSMSServiceByServiceName(this.isOpen(params));
			Order order = (Order) orderService.getOrderByLogisticId(txLogisticId);
			
			String content = template.getContent();
			String shopName = "";// order.getShopName();
			if(user!=null) {
				shopName = user.getShopName();
			}
			String orderCreateTime = DateUtil.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss");   
			String orderSendTime = order.getType();
			
			if(StringUtils.hasText(content)) {
				if(!StringUtils.hasText(shopName)) {
					shopName = "";
				}
				if(!StringUtils.hasText(orderCreateTime)) {
					orderCreateTime = "";
				}else {
					orderCreateTime = getFormatMonthDay(orderCreateTime);
				}
				if(!StringUtils.hasText(orderSendTime)) {
					orderSendTime = "";
				}else {
					orderSendTime = getFormatMonthDay(orderSendTime);
					//orderSendTime = paserStringDateToString(orderSendTime,"yyyy-MM-dd HH:mm:ss");
				}
				if(!StringUtils.hasText(mailNo)) {
					mailNo = "";
				}
				if(content.contains("[店铺名称]"))content = content.replace("[店铺名称]", shopName);
				if(content.contains("[订单时间]"))content = content.replace("[订单时间]", orderCreateTime);
				if(content.contains("[发货时间]"))content = content.replace("[发货时间]", orderSendTime);
				if(content.contains("[运单号]"))content = content.replace("[运单号]", mailNo);
				if(content.contains("[快递公司]"))content = content.replace("[快递公司]", "圆通");
			}
			
			// 保存短信记录
			SMSInfo info = new SMSInfo();
			if(service!=null) {
				info.setSmsTypeId(service.getId());
			}
			if(template!=null) {
				info.setSmsTemplateId(template.getId());
			}
			info.setMailNo(mailNo);
			info.setBuyName(buyerName);
			
			// 从会员表中获取旺旺号，插入短信表中
			// info.setBuyLoginName(buyerName);
			
			info.setBuyMobile(mobile);
			info.setContent(content);
			info.setStatus("1");
			info.setUserId(service.getUserId());  //谁开启的服务,则使用谁的ID
			info.setCreateTime(new Date());
			info.setSendTime(new Date());
			info.setCreateUserId(createUserId);
			info.setServiceType(status);
			info.setIsDel(0);
			if(this.saveMsg(info)){
				logger.error("saveInfoToQueue:this.saveMsg...........OK...物流号：" + txLogisticId + "用户ID：" + user.getId() + "订单状态:" + status );
				SMSObject smsObject = new SMSObject();
				
				smsObject.setDestMobile(info.getBuyMobile());
				smsObject.setMessageContent(info.getContent());
				smsObject.setSmsType("5");
				smsObject.setSequenceID(info.getId());
				
				boolean isSend = smsPortService.saveSMSInfo(smsObject, info.getUserId());
				logger.error("saveInfoToQueue:this.saveMsg...........OK...物流号：" + txLogisticId + "用户ID：" + user.getId() + "订单状态:" + status );
				if (!isSend) {
					logger.error("插入短信队列失败！");
					info.setStatus("2");
					info.setErrorSend("短信不足！");
					dao.updateErrorSendById(info);
				}
			}
		}
	}
	**/
	
	@Override
	public void saveInfoToQueue(Map<String, Object> params) throws Exception {
		// 如果是卖家，则是卖家主帐号的userID, 若是分仓帐号，则是平台帐号userId，若是入驻企业则是当前用户ID
		Integer userId = (Integer)params.get("userId");
		// 当前用户的userID
		Integer createUserId = (Integer)params.get("createUserId");
		// 订单的状态 : 发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
		String status = (String)params.get("status");
		//根据用户,判断
		User user = (User)userDao.getUserById(userId); 
		// 买家的运单号、电话、姓名
		String mailNo = (String)params.get("mailNo");
		String mobile = (String)params.get("buyMobile");
		String buyerName = (String)params.get("buyName");
		String txLogisticId = (String)params.get("txLogisticId");
		
		logger.error("短信发送进入service：物流号：" + txLogisticId + "用户ID：" + user.getId() + "订单状态:" + status );
		
		// 判断短信是否已经发送 根据status，mailNo，电话
		boolean isNotSend = smsInfoDao.isNotSendByParams(params);
		logger.error("saveInfoToQueue:isNotSend:"+isNotSend);
		if(isNotSend) {
			// 获取短信类型、模版和订单,如果主账号开通了，就用自己的模版，如果没有就用关联店铺的
			SMSTemplate template = null;
			template = smsTemplateService.getDefaltTemplate(this.isOpen(userId, status));
			String shopName = "";// order.getShopName();
			if(user!=null) {
				shopName = user.getShopName();
			}
			Order order = (Order) orderService.getOrderByLogisticId(txLogisticId);
			String orderCreateTime = DateUtil.format(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss");    
			String orderSendTime = order.getType();
			String content = "";
			Integer smsCount = 0;
			//当前用户id、关联店铺id
			Integer[] userIds = null;
			List<SMSServiceArea> smsareas=sMSServiceAreaDao.getSMSServiceAreaByParams(template.getSmsTypeId(), userId);
			boolean isInArea=false;
			for(SMSServiceArea area : smsareas){
				if(area.getCode().equals(params.get("numProv").toString())){
					isInArea=true;
					break;
				}
			}
			if(!isInArea){
				logger.error("收件人不在发送区域范围内");
				return;
			}
			if(template != null && template.getId() != 0){
				//获取短信内容
				content = template.getContent();
				content = getTemplateContent(content, shopName, orderCreateTime, orderSendTime, mailNo);
				smsCount = (int) (Math.ceil(content.length()/67) + 1);//这是自己设置模版的情况下
				userIds = getActualUserId(createUserId,userId,status,smsCount);
			}else {
				List<Integer> listUserId = Resource.getUserRelationUserIdList(user);//得到关联店铺的Id
				 if(CollectionUtils.isNotEmpty(listUserId)) {
					 for(Integer theUserId:listUserId) {  //遍历关联店铺是否开通
						 if(isOpenService(theUserId,status)) { 
							template = smsTemplateService.getDefaltTemplate(this.isOpen(theUserId, status));
							content = template.getContent();
							content = getTemplateContent(content, shopName, orderCreateTime, orderSendTime, mailNo);
							smsCount = (int) (Math.ceil(content.length()/67) + 1);//这是自己设置模版的情况下
							if(isOpenService(theUserId,status)&&isHasSmsCount(theUserId,smsCount)) { 
								userIds = getActualUserId(createUserId,userId,status,smsCount);
								break;
							 }
						 }
					 }
				 }
			}
			if(userIds==null) {  //无任何服务开启,或者是没有任何短信条数充足
				return;
			}
			Integer serviceUserId = userIds[0];//服务所使用的USERID  = userId
			Integer tempUserId = userIds[1];  //关联店铺id
			//如果当前用户(主账号)没有开通，得到关联店铺的id
			if(serviceUserId==null) {
				serviceUserId = tempUserId;
			}
			//获取开启的服务,如果自己开启了就是自己的，如果自己没有开启就是关联店铺的
			SMSService service = smsServiceService.getOpeanService(serviceUserId, status);
			SMSTemplate smTemplate = smsTemplateService.getDefaltTemplate(this.isOpen(userId, status));
			// 保存短信记录
			SMSInfo info = new SMSInfo();
			if(service!=null) {
				info.setSmsTypeId(service.getId());
			}
			if(template!=null) {
				if(userIds[0]!= null &&(userIds[0] == userIds[1])){//如果是自己发自己的
					info.setSmsTemplateId(template.getId());
				}else {//如果是别人代发的
					if(smTemplate != null && smTemplate.getId() != 0){//自己设置了模版
						info.setSmsTemplateId(null);
					}else {//自己没有设置模版
						info.setSmsTemplateId(template.getId());
					}
				}
			}
			info.setMailNo(mailNo);
			info.setBuyName(buyerName);
			
			// 从会员表中获取旺旺号，插入短信表中
			// info.setBuyLoginName(buyerName);
			
			info.setBuyMobile(mobile);
			info.setContent(content);
			info.setStatus("1");
			info.setUserId(service.getUserId());  //谁开启的服务,则使用谁的ID
			info.setCreateTime(new Date());
			info.setSendTime(new Date());
			info.setCreateUserId(createUserId);
			info.setServiceType(status);
			info.setIsDel(0);
			if(this.saveMsg(info)){
				logger.error("saveInfoToQueue:this.saveMsg...........OK...物流号：" + txLogisticId + "用户ID：" + user.getId() + "订单状态:" + status );
				SMSObject smsObject = new SMSObject();
				
				smsObject.setDestMobile(info.getBuyMobile());
				smsObject.setMessageContent(info.getContent());
				smsObject.setSmsType("5");
				smsObject.setSequenceID(info.getId());
				
				boolean isSend = smsPortService.saveSMSInfo(smsObject, info.getUserId());
				logger.error("saveInfoToQueue:this.saveMsg...........OK...物流号：" + txLogisticId + "用户ID：" + user.getId() + "订单状态:" + status );
				if (!isSend) {
					logger.error("插入短信队列失败！");
					info.setStatus("2");
					info.setErrorSend("短信不足！");
					dao.updateErrorSendById(info);
				}
			}
		}
	}
	/**
	 * 卖家-获取代发USERID  （自己/关联店铺）
	 * @param userId 用户主账户ID,status 短信类型、createUserId 当前用户的id
	 * ids[0] 代表着的主账号d ids[1]代表着发短信的id
	 * @return
	 */
	private Integer[] getActualUserId(Integer createUserId,Integer userId,String status,Integer smsCount) {
		/**
		 * 首先判断 短信服务是否开通
		 * 如果自己的短信服务开通、在判断 里面的小服务是否开通
		 * 在判断 自己是否有充足的短信、如果有的话 就自己发送， 
		 * 这个时候需要判断 和他相关联的店铺是否开通短信服务，
		 * 如果没有开通，这替关联店铺发，
		 * 如果开通了，在判断小服务时候开通，如果小服务没有开通
		 * 则替关联店铺发送
		 * 如果小服务开通了，判断短信条数是否充足，如果 不充足
		 * 则替关联店铺发
		 */
		Integer[] ids = new Integer[2];
		ids[0] = userId;
		User user = (User)userDao.getUserById(userId); 
		int type = getUserType(user);
		if(type==1) {   //判断是否是卖家  用的是主账号
			//首先判断当前主账号是否开通了短信服务以及相应的服务
			boolean isOpenService = isOpenService(user.getId(),status);
			//如果开通了短信服务以及相应的服务、短信条数是否充足
			//如果充足，就自己发自己的,并且返回
			//如果不足，就是找自己的关联店铺
			if(isOpenService&&isHasSmsCount(user.getId(),smsCount)) {
				ids[1] = userId;
				return ids;
			}else {
				//这个时候首先判断是否开通了服务
				if(!isOpenService) {
					ids[0] = null;
				}
				if(!isHasSmsCount(user.getId(),smsCount)){
					ids[0] = null;
				}
				 List<Integer> listUserId = Resource.getUserRelationUserIdList(user);//得到关联店铺的Id
				 if(listUserId!=null) {
					 for(Integer theUserId:listUserId) {  //遍历关联店铺是否开通
						 if(isOpenService(theUserId,status)&&isHasSmsCount(theUserId,smsCount)) { 
							ids[1] = theUserId;
							 return ids;
						 }
					 }
				 }
			}
		}else {  //其他类型用户
			if(isOpenService(user.getId(), status)){	
				ids[0] = userId;//当前用户id
				ids[1] = createUserId;
				return ids;
			}
		}
		return null;  //短信服务/短信类型服务未开通
	}
	
	/**
	 * 判断短信服务,以及短信指定类型服务,是否开通
	 * @param userId
	 * @param status
	 * @return
	 */
	private boolean isOpenService(Integer userId,String status) {
		return (smsPortService.supplyStatusOpenUp(userId)&&smsServiceService.isOpeanService(userId, status));
	}
	
	private String getTemplateContent(String content,String shopName,String orderCreateTime,String orderSendTime,String mailNo) {
		if(StringUtils.hasText(content)) {
			if(!StringUtils.hasText(shopName)) {
				shopName = "";
			}
			if(!StringUtils.hasText(orderCreateTime)) {
				orderCreateTime = "";
			}else {
				orderCreateTime = getFormatMonthDay(orderCreateTime);
			}
			if(!StringUtils.hasText(orderSendTime)) {
				orderSendTime = orderCreateTime;
			}else {
				orderSendTime = getFormatMonthDay(orderSendTime);
				//orderSendTime = paserStringDateToString(orderSendTime,"yyyy-MM-dd HH:mm:ss");
			}
			if(!StringUtils.hasText(mailNo)) {
				mailNo = "";
			}
			if(content.contains("[店铺名称]"))content = content.replace("[店铺名称]", shopName);
			if(content.contains("[订单时间]"))content = content.replace("[订单时间]", orderCreateTime);
			if(content.contains("[发货时间]"))content = content.replace("[发货时间]", orderSendTime);
			if(content.contains("[运单号]"))content = content.replace("[运单号]", mailNo);
			if(content.contains("[快递公司]"))content = content.replace("[快递公司]", "圆通速递");
		}
		return content;
	}
	
	/**
	 * 判断短信条数舒服足够
	 * @return
	 */
	private boolean isHasSmsCount(Integer userId,int count) {
		//2:根据当前用户的userId查询该用户的可使用短信数
		SMSMessage smsMessage = smsPortService.supplySMSMessage(userId);
		int hasSmsCount = 0;
		if(smsMessage != null) {
			hasSmsCount = smsMessage.getSmsUsecount();
		}
		if(hasSmsCount>count) {
			return true;
		}
		return false;
	}
	
	public boolean saveInfoToQueueForCustomerSend(User currentUser,String buyerName,String buyerMobile,String bMailno,String content) {
		//发送信息创建人,当前用户
		int createUserId = currentUser.getId();
		//发送信息所属人
		int useUserId = getUseUserId(currentUser);

		// 保存短信记录                                               (该类型,未判断短信服务,且,无短信服务,模版,运单号,店铺名称) 只作为,问题件客户信息通知
		SMSInfo info = new SMSInfo();
		info.setBuyName(buyerName);
		info.setBuyMobile(buyerMobile);
		info.setContent(content);
		info.setUserId(useUserId);  //谁开启的服务,则使用谁的ID
		info.setCreateTime(new Date());
		info.setSendTime(new Date());
		info.setCreateUserId(createUserId);
		info.setMailNo(bMailno);
		info.setIsDel(0);
		info.setStatus("1");
		info.setServiceType("QUEST");
		
		boolean isSend = false;
		if(this.saveMsg(info)){
			SMSObject smsObject = new SMSObject();
			smsObject.setDestMobile(info.getBuyMobile());
			smsObject.setMessageContent(info.getContent());
			smsObject.setSmsType("5");
			smsObject.setSequenceID(info.getId());
			isSend = smsPortService.saveSMSInfo(smsObject, info.getUserId());
			if (!isSend) {
				logger.error("插入短信队列失败！");
				info.setStatus("2");
				info.setErrorSend("短信不足！");
				dao.updateErrorSendById(info);
			}
		}
		return isSend;
	}
	
	public boolean saveInfoToQueue(User currentUser,String buyerName,String buyerMobile,String bMailno,String content,String serviceType) {
		//发送信息创建人,当前用户
		int createUserId = currentUser.getId();
		//发送信息所属人
		int useUserId = getUseUserId(currentUser);

		// 保存短信记录                                               (该类型,未判断短信服务,且,无短信服务,模版,运单号,店铺名称) 只作为,问题件客户信息通知
		SMSInfo info = new SMSInfo();
		info.setBuyName(buyerName);
		info.setBuyMobile(buyerMobile);
		info.setContent(content);
		info.setUserId(useUserId);  //谁开启的服务,则使用谁的ID
		info.setCreateTime(new Date());
		info.setSendTime(new Date());
		info.setCreateUserId(createUserId);
		info.setMailNo(bMailno);
		info.setIsDel(0);
		info.setStatus("1");
		info.setServiceType(serviceType);
		
		boolean isSend = false;
		if(this.saveMsg(info)){
			SMSObject smsObject = new SMSObject();
			smsObject.setDestMobile(info.getBuyMobile());
			smsObject.setMessageContent(info.getContent());
			smsObject.setSmsType("5");
			smsObject.setSequenceID(info.getId());
			isSend = smsPortService.saveSMSInfo(smsObject, info.getUserId());
			if (!isSend) {
				logger.error("插入短信队列失败！");
				info.setStatus("2");
				info.setErrorSend("短信不足！");
				dao.updateErrorSendById(info);
			}
		}
		return isSend;
	}
	
	
	/**
	 * 时效提醒发短信
	 * @param currentUser
	 * @param buyerName
	 * @param buyerMobile
	 * @param content
	 * @return
	 */
	public boolean saveInfoForRemind(User currentUser,String buyerName,String buyerMobile,String bMailno,String content) {
		//发送信息创建人,当前用户
		int createUserId = currentUser.getId();
		//发送信息所属人
		int useUserId = getUseUserId(currentUser);

		// 保存短信记录                                               (该类型,未判断短信服务,且,无短信服务,模版,运单号,店铺名称) 只作为,问题件客户信息通知
		SMSInfo info = new SMSInfo();
		info.setBuyName(buyerName);
		info.setBuyMobile(buyerMobile);
		info.setContent(content);
		info.setUserId(useUserId);  //谁开启的服务,则使用谁的ID
		info.setCreateTime(new Date());
		info.setSendTime(new Date());
		info.setCreateUserId(createUserId);
		info.setMailNo(bMailno);
		info.setIsDel(0);
		info.setStatus("1");
		info.setServiceType("AGING");
		
		boolean isSend = false;
		if(this.saveMsg(info)){
			SMSObject smsObject = new SMSObject();
			smsObject.setDestMobile(info.getBuyMobile());
			smsObject.setMessageContent(info.getContent());
			smsObject.setSmsType("5");
			smsObject.setSequenceID(info.getId());
			isSend = smsPortService.saveSMSInfo(smsObject, info.getUserId());
			if (!isSend) {
				logger.error("插入短信队列失败！");
				info.setStatus("2");
				info.setErrorSend("短信不足！");
				dao.updateErrorSendById(info);
			}
		}
		return isSend;
	}
	
	
	/**
	 * 获取查询使用的用户ID
	 * 分3类ID  第一类：主账户ID（父ID）  第二类：子账户ID（父ID不存在的情况下使用） 第三类：平台ID (分仓使用平台)
	 * @return
	 */
	public int getUseUserId(User currentUser) {
		String childType = currentUser.getChildType();  //B:分仓 C:企业
		if("B".equals(childType)) {  //B:分仓  使用平台ID
			String userSource = currentUser.getUserSource();
			if(StringUtils.hasText(userSource)) {        //若：B:分仓 C:企业  的平台ID为NULL ,则使用自身ID,注意：此情况实际属于,错误数据
				return Integer.parseInt(userSource);
			}else {
				return currentUser.getId();
			}
		}else {                                            //其他类型用户,使用主账户
			Integer parentId = currentUser.getParentId();
			if(parentId!=null) {
				return parentId;
			}else {
				return currentUser.getId();
			}
		}
	}
	
	/**
	 * 获取登录用户类型 0:未知的类型  1：卖家 2：网点 3：admin 4:平台用户
	 * @return
	 */
	private int getUserType(User currentUser) {
		String userType = currentUser.getUserType();
		if("1".equals(userType)||"11".equals(userType)||"12".equals(userType)||"13".equals(userType)) {
			return 1;
		}else if("2".equals(userType)||"21".equals(userType)||"22".equals(userType)||"23".equals(userType)){
			return 2;
		}else if("3".equals(userType)) {
			return 3;
		}else if("4".equals(userType)||"41".equals(userType)||"42".equals(userType)||"43".equals(userType)) {
			return 4;
		}
		return 0;
	}
	
	/**
	 * 解析日期型字符串到字符串
	 * @param date
	 * @param format
	 * @return
	 */
	private String paserStringDateToString(String date,String format) {
		SimpleDateFormat mat = new SimpleDateFormat(format);
		String formatDate = "";
		try {
			formatDate = mat.format(mat.parse(date));
		} catch (ParseException e) {
			logger.error("CLASS:SMSInfoServiceImpl -> METHOD:paserStringDateToString -> PARA:date:"+date+" format:"+format + " has Exception :"+e.getMessage());
		}
		return formatDate;
	}
	
	public void saveInfoToQueueForMarket(Map<String, Object> params) throws Exception {
		//替换模版内容
		Integer smsTypeId = (Integer)params.get("smsTypeId");
		Integer templateId = (Integer)params.get("templateId");
		String buyName = (String)params.get("buyName");
		String loginName = (String)params.get("loginName");
		Integer userId = (Integer)params.get("userId");
		
		String content = (String)params.get("content");
		String shopName = (String)params.get("shopName");
		String mobile = (String)params.get("mobile");
		
		if(content!=null&&content.contains("[店铺名称]")) {
			if(shopName==null) {
				shopName = "";
			}
			content = content.replace("[店铺名称]", shopName);
		}
		
		// 保存短信记录
		SMSInfo info = new SMSInfo();
		info.setSmsTypeId(smsTypeId);
		info.setSmsTemplateId(templateId);
		info.setBuyName(buyName);
		info.setBuyLoginName(loginName);
		info.setBuyMobile(mobile);
		info.setContent(content);
		info.setStatus("1");
		info.setUserId(userId);
		info.setCreateTime(new Date());
		info.setCreateUserId(userId);
		info.setServiceType("MARKET");
		info.setIsDel(0);
		
		if(this.saveMsg(info)){
			SMSObject smsObject = new SMSObject();
			
			smsObject.setDestMobile(info.getBuyMobile());
			smsObject.setMessageContent(info.getContent());
			smsObject.setSmsType("5");
			smsObject.setSequenceID(info.getId());
			
			boolean isSend = smsPortService.saveSMSInfo(smsObject, info.getUserId());
			
			if (!isSend) {
				logger.error("插入短信队列失败！");
				info.setStatus("2");
				info.setErrorSend("短信不足！");
				dao.updateErrorSendById(info);
			}
		}
	}


	/**
	 * [封装]查询条件
	 * @param params
	 *status  发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
	 * sellerId  卖家的主帐号ID
	 * @return
	 */
	private Map isOpen(Integer userId,String status) {
		//如果是卖家，则是卖家主帐号的userID, 若是分仓帐号，则是平台帐号userId，若是入驻企业则是当前用户ID
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		String name = "其他类型暂不查询";
		if("GOT".equals(status))name="发货提醒";
		if("SENT_SCAN".equals(status))name="派件提醒";
		if("SIGNED".equals(status))name="签收提醒";
		map.put("name", name);
		
		return map;
	}


	@Override
	public List<SMSInfoExpand> getSMSInfoByPamams(Map<String, Object> params) {
		/**
		 * 参数 smsTypeId,smsTemplateId,sendTimeStart,sendTimeEnd,status  无业务处理的查询状态
		 */
		
		//参数 shopName的查询  若有值,则根据shopName获取该店铺名称的主账户ID,替代参数中的 List<Integer> userIds  若没值,则使用原userIds
		String shopName = (String) params.get("shopName");
		// shopName = 所属店铺,表示查询全部店铺
		if(StringUtils.hasText(shopName)&&!"所属店铺".equals(shopName)) {
			List<User> users = userDao.getUsersByShopName(shopName);
			//根据店铺名称,查询店铺
			if(users!=null&&users.size()>0) {
				List<Integer> userIds = new ArrayList<Integer>();
				for(User user:users) {
					userIds.add(user.getId());
				}
				params.put("shopUserIds", userIds);
			}else {  //根据店铺名称未能查找到店铺,则不做此userIds查询
				return new ArrayList();  //无网店,不使用该网店作为条件
			}
		}
		
		/**
		 * buyName,buyMobile,mailNo,loginName 此条件,四种中应该只出现一种  buyMobile,mailNo这两参数采取,有值则直接查询原则
		 */
		
		//buyName,loginName 2选1的情况下,得到其中1个有值的,或全部没值
		String buyName = (String) params.get("buyName");
		String loginName = (String) params.get("loginName");
		
		//无论buyName,loginName 谁有值,都放入KEY:buyName中 数据库端,采取 或者处理      [未考虑两者都有值的情况]
		if(StringUtils.hasText(buyName)) {
			params.put("loginName", null);
		}
		if(StringUtils.hasText(loginName)) {
			params.put("buyName", loginName);
			params.put("loginName", null);
		}
		
		List<SMSInfoExpand> list = dao.getSMSInfoByPamams(params);
		switchErrCode(list);
		
		return list;
	}
	
	@Override
	public Integer getCountByToday(List<Integer> userIds, String type) {	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userIds", userIds);
		//获取年月日
		Date nowDate = new Date();
		String year = DateUtil.toShortYear(nowDate);
		String month = DateUtil.toMonth(nowDate);
		String day = DateUtil.toShortdd(nowDate);
		if("0".equals(type)) {  //当天
			params.put("year", year);
			params.put("month", month);
			params.put("day", day);
		}
		if("1".equals(type)) {  //当月
			params.put("year", year);
			params.put("month", month);
		}
		params.put("status", 0);  //发送成功
		return dao.getCountByToday(params);
	}
	
	private void switchErrCode(List<SMSInfoExpand> list) {
		String errorSend = "";
		String errorMsg = "";
		for(SMSInfoExpand info:list) {
			errorSend = info.getErrorSend();
			if(StringUtils.hasText(errorSend)) {
				info.setErrorSend(SendErrCodeSwitch.errCodeSwitch(info.getBuyMobile(), errorSend));
			}
		}
	}
	
	@Override
	public boolean addSMSInfo(SMSInfo smsInfo) {
		boolean flag = false;
		if(smsInfo == null){
			logger.error(SMSInfo.class.getName() + "添加数据异常!");
		}else{
			dao.addSMSInfo(smsInfo);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteSMSInfo(Integer id) {
		boolean flag = false;
		if(id == null || ("").equals(id.toString())){
			logger.error(SMSInfo.class.getName() + "删除数据异常!");
		}else{
			dao.deleteSMSInfo(id);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateSMSInfo(SMSInfo smsInfo) {
		boolean flag = false;
		if(smsInfo == null){
			logger.error(SMSInfo.class.getName() + "更新数据异常!");
		}else{
			dao.updateSMSInfo(smsInfo);
			flag = true;
		}
		return flag;
	}

	@Override
	public SMSInfo getSMSInfoById(Integer id) {
		SMSInfo smsInfo = null;
		if(id == null){
			logger.error(SMSInfo.class.getName() + "查找数据异常!");
		}else{
			smsInfo = (SMSInfo) dao.getSMSInfoById(id);
		}
		return smsInfo;
	}
	@Override
	public boolean saveMsg(SMSInfo info) {
		boolean flag = false;
		if(info == null){
			logger.error("##保存短信详细信息时传入参数为空！");
			return flag;
		}
		
		try{
			boolean bool1 = dao.addSMSInfo(info);
			logger.info("插入短信详细信息成功与否："+bool1);
			flag = true;
		}catch(Exception e){
			logger.error("##保存短信详细信息异常.异常信息："+e);
			flag = false;
		}		
		return flag;
	}

	@Override
	public boolean updateSMSInfoById(SMSInfo info1) {
		boolean flag = false;
		if(info1 == null){
			logger.error("##短信发送成功时跟新短信表时传参为空！");
		}else{
			boolean b = dao.updateSMSInfoById(info1);
			logger.info("短信发送成功时修改短信信息成功与否："+b);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateErrorSendById(SMSInfo info1) {
		boolean flag = false;
		if(info1 == null){
			logger.error("##短信发送失败时跟新短信表时传参为空！");
		}else{
			boolean b = dao.updateErrorSendById(info1);
			logger.info("短信发送失败时修改短信信息成功与否："+b);
			flag = true;
		}
		return flag;
	}

	@Override
	public Integer getSMSInfoCountByPamams(Map<String, Object> params) {
		/**
		 * 参数 smsTypeId,smsTemplateId,sendTimeStart,sendTimeEnd,status  无业务处理的查询状态
		 */
		
		//参数 shopName的查询  若有值,则根据shopName获取该店铺名称的主账户ID,替代参数中的 List<Integer> userIds  若没值,则使用原userIds
		String shopName = (String) params.get("shopName");
		
		// shopName = 所属店铺,表示查询全部店铺
		if(StringUtils.hasText(shopName)&&!"所属店铺".equals(shopName)) {
			List<User> users = userDao.getUsersByShopName(shopName);
			//根据店铺名称,查询店铺
			if(users!=null&&users.size()>0) {
				List<Integer> userIds = new ArrayList<Integer>();
				for(User user:users) {
					userIds.add(user.getId());	
				}
				params.put("shopUserIds", userIds);
			}else {  //根据店铺名称未能查找到店铺,则不做此userIds查询
				return 0;  //无网店,不使用该网店作为条件
			}
		}
		
		/**
		 * buyName,buyMobile,mailNo,loginName 此条件,四种中应该只出现一种  buyMobile,mailNo这两参数采取,有值则直接查询原则
		 */
		
		//buyName,loginName 2选1的情况下,得到其中1个有值的,或全部没值
		String buyName = (String) params.get("buyName");
		String loginName = (String) params.get("loginName");
		
		//无论buyName,loginName 谁有值,都放入KEY:buyName中 数据库端,采取 或者处理      [未考虑两者都有值的情况]
		if(StringUtils.hasText(buyName)) {
			params.put("loginName", null);
		}
		if(StringUtils.hasText(loginName)) {
			params.put("buyName", loginName);
			params.put("loginName", null);
		}
		return dao.getSMSInfoCountByPamams(params);
	}
	
	
	private String getFormatMonthDay(String date) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String theDate = date;
		try {
			Date thisDate = format.parse(date);
			theDate = DateUtil.toMonth(thisDate) + "月";
			theDate += DateUtil.toShortdd(thisDate) + "日";
			theDate += DateUtil.toShortHm(thisDate) + "";
		} catch (ParseException e) {
			logger.error("IN SERVICE:OrderServiceImpl METHOD:getFormatDate() parse date:"+ date + "ERR");
		}
		return theDate;
	}
	
	public static void main(String args[]) {
		SMSInfoServiceImpl o = new SMSInfoServiceImpl();
		//System.out.println(o.getFormatMonthDay("20dddddddddddddddddd30"));
		//System.out.println(o.getFormatMonthDay("2012-03-02 13:22:30").length());
		System.out.println(new Date().getTime());
		System.out.println(new Date().getTime());
		System.out.println(new Date().getTime());
		System.out.println(new Date());
		System.out.println(new Date());
		System.out.println(new Date());
		System.out.println(new Date());
		System.out.println(new Date());
	}
}
