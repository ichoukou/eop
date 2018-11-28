package net.ytoec.kernel.service.impl;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.AccountUserDao;
import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dao.LogsDao;
import net.ytoec.kernel.dao.PayServiceDao;
import net.ytoec.kernel.dao.PaymentDao;
import net.ytoec.kernel.dao.SMSObjectDao;
import net.ytoec.kernel.dao.ServiceHistoryDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSPortService;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提供给短信模块的service
 * @author guoliang.wang 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSPortServiceImpl implements SMSPortService {
	
	private static final Logger logger = LoggerFactory.getLogger(SMSPortServiceImpl.class);

	/*
	 * 账户信息
	 */
	@Inject
	private AccountUserDao<AccountUser> accountUserDao;

	/*
	 * 交易（支付）明细
	 */
	@Inject
	private PaymentDao<Payment> paymentDao;

	/*
	 * 开通充值服务
	 */
	@Inject
	private DredgeServiceDao<DredgeService> dredgeServiceDao;

	/*
	 * 服务记录历史
	 */
	@Inject
	private ServiceHistoryDao<ServiceHistory> serviceHistoryDao;
	
	@Inject
	private SMSObjectDao<SMSObject> smsDao;
	
	@Inject
    private UserDao<User> UserDao;
	@Inject
	private PayServiceDao<PayService> payServiceDao;
	/*
	 * 日志
	 */
	@Inject
	private LogsDao<Logs> logsDao;
	
	@Inject
	private SMSObjectService<SMSObject> SMSObjectService;
	
	@Inject
	private UserDao<User> userDao;
	
	/**
	 * 提供短信是否开启接口：
	 * 返回Boolean（查询开通服务表是否有记录） 
	 * @param userId
	 * 注：userId 为主账号id(分仓账号可以使用平台)
	 * @return boolean
	 */
	@Override
	@SuppressWarnings("all")
	public boolean supplyStatusOpenUp(Integer userId) {
	boolean bool=false;
		if(userId==null){	
			logger.error("传来的userId为空,方法名：supplyStatusOpenUp");
			return false;			
			
		}
		Map map=new HashMap();
		map.put("userId", userId);
		map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//系统在初始化服务的时候，服务表里面的数据。5代表短信服务
		map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
        List<DredgeService> dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(map);		
		if(CollectionUtils.isNotEmpty(dredgeServiceList)){
			bool= true;
			return bool;
		}
		
		//若是分仓账户，企业账户
		User user = UserDao.getUserById(userId);
		if(StringUtils.equals("1", user.getUserType())||
				 StringUtils.equals("2", user.getUserType())||
				StringUtils.equals("3", user.getUserType())||
				StringUtils.equals("4", user.getUserType())){
			if("1".equals(user.getUserType()) && ("B".equals(user.getChildType())))
			{
				if(BooleanUtils.isFalse(bool)){
					//平台账号是否开通
					bool=sourceStatusOpenUp(userId);
				}
			}		
		}else{
				bool=parentStatusOpenUp(userId);
				if(BooleanUtils.isFalse(bool)&&("B".equals(user.getChildType()))){
					//平台账号是否开通
					bool=sourceStatusOpenUp(user.getParentId());
				}
		}
		return bool;
	}
	/**
	 * 根据userId 查询主账号
	 * @param userId
	 * @return
	 */
	public User getUserById(Integer userId){
		return userDao.getUserById(userId);
	}
	
	/**
	 * 提供短信是否开启接口：hufei
	 * 返回Boolean（查询开通服务表是否有记录） 
	 * @param userId
	 * 注：userId 为主账号id(分仓账号可以使用平台)
	 * @return boolean
	 */
	public boolean SMSStatusOpenUp(Integer userId) {
		boolean bool=false;
			if(userId==null){	
				logger.error("传来的userId为空,方法名：supplyStatusOpenUp");
				return false;			
			}
			Map map=new HashMap();
			map.put("userId", userId);
			map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//系统在初始化服务的时候，服务表里面的数据。5代表短信服务
			map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
	        List<DredgeService> dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(map);		
			if(CollectionUtils.isNotEmpty(dredgeServiceList)){
				bool= true;
			}
			return bool;
		}
	

	/**
	 * 提供短信相关的信息接口：
	 * 可用短信数量、开始时间、结束时间 返回一个小的对象（查询开通的服务表记录）
	 * @param Integer userId,String userType
	 * @return SMSMessage
	 */
	@Override
	@SuppressWarnings("all")
	public SMSMessage supplySMSMessage(Integer userId) {
		SMSMessage result_SMSMessage=new SMSMessage();
			if(userId==null){		
				logger.error("传来的userId为空,方法名：supplySMSMessage");
				return null;			
				
			}
			Map map=new HashMap();
			map.put("userId", userId);
			map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//系统在初始化服务的时候，服务表里面的数据。5代表短信服务
			map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
			 List<DredgeService> dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(map);
			 if(CollectionUtils.isNotEmpty(dredgeServiceList)){
				 //自己已经开启
				 SMSMessage SMSMessage=new SMSMessage();
				 DredgeService dredgeService=dredgeServiceList.get(0);
				 try {
					PropertyUtils.copyProperties(SMSMessage, dredgeService);
				} catch (Exception e) {
					//记日志
					Logs logs=new Logs();
					logs.setOperName("短信调用转化对象");
					logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
					logs.setUserId(userId);
					logs.setRemark("转化给短信的对象出错了");
					logsDao.add(logs);
					logger.error("转化给短信的对象出错了SmsUsecount:"+dredgeService.getSmsUsecount()
							+"BeginDate:"+dredgeService.getBeginDate()+"EndDate:"+dredgeService.getEndDate());
					e.printStackTrace();
				}
				result_SMSMessage.setSmsUsecount(SMSMessage.getSmsUsecount());
				result_SMSMessage.setBeginDate(SMSMessage.getBeginDate());
				//	return SMSMessage;
			}/*else{
				//父账号
				SMSMessage sMSMessage=parentSMSMessage(userId);
				if(sMSMessage == null){
					//平台账号
					sMSMessage=sourceSMSMessage(userId);	
				}
				if(sMSMessage != null){
					return sMSMessage;
				}
			}*/
			User user=	UserDao.getUserById(userId);
			//若是分仓账号或入驻企业账号则显示自己购买的短信数量
			if("1".equals(user.getUserType()) && ("B".equals(user.getChildType())||"C".equals(user.getChildType())))
			{
				SMSMessage pare_SMSMessage=parentSMSMessage(userId);
				if(pare_SMSMessage != null){
					result_SMSMessage.setSmsUsecount(result_SMSMessage.getSmsUsecount()+pare_SMSMessage.getSmsUsecount());
				}
				 SMSMessage source_SMSMessage=sourceSMSMessage(userId);	
				if(source_SMSMessage != null){
					result_SMSMessage.setSmsUsecount(result_SMSMessage.getSmsUsecount()+source_SMSMessage.getSmsUsecount());
				}
			}		 
	
		return result_SMSMessage;
	}

	/**
	 * 提供短信相关的信息接口：
	 * @param Map 的值：（Integer）userId、(Integer) SMSnum,(double) prince,(String) name
	 * name 名称:短信体验套餐\短信初级短信套餐\短信中级套餐\短信高级套餐\ 其它
	  * 第一步：修改账号表余额，可用余额 。第二步：修改开通服务表 短信数量。第三步：插入支付表
	 * 第四步：插入服务历史记录表
	 * @return Integer(0 成功、1失败、2用户不存在、3 余额不足4 代表请稍后重试（记录已经被更改） 5、其它 )
	 *注：userId 用户id
	 */
	@Override
	@SuppressWarnings("all")
	public Integer supplyRecharge(Map map) {
		if(map.get("userId")==null||
				map.get("SMSnum")==null||
				map.get("prince")==null||
				map.get("name")==null){		
			logger.error("传来的数据不完整,方法名：supplyRecharge");
			return 5;		
			
		}		
		Integer userId=Integer.valueOf(map.get("userId").toString());		
		//1)修改账户表-ec_core_accountUser(修改前先查询余额是否足够)
		AccountUser accountUser=accountUserDao.getAccountUserByUserId(userId);
		if(accountUser == null){
			User user=	UserDao.getUserById(userId);
			//1.1)子账号
			if(user.getParentId() ==null){
				//主账号则插入
				accountUser=addAccountUserMessage(userId);
			}else{
				accountUser=accountUserDao.getAccountUserByUserId(user.getParentId());
				if(accountUser==null){
					//accountUser=addAccountUserMessage(user.getParentId());
					return 2;
				}
			}			
		}
		return dealBuss(accountUser,map,userId);
		
	    //[业务处理完后 发送短信]
		//是否开通了(交易提醒|余额不足提醒)公用方法			
		//sendSMSByUserId(userId,map.get("SMSnum").toString(),dredgeService);
		
	}
	
//	/*
//	 * 交易提醒 余额不足提醒
//	 */
//	@SuppressWarnings("all")
//	public boolean sendSMSByUserId(Integer userId,String serviceName,DredgeService dredgeService) {
//		AccountUser accountUser = accountUserDao.getAccountUserByUserId(userId);		
//		// 1.1)是否开通了交易提醒
//		Map less_map = new HashMap();
//		less_map.put("userId", userId);
//		less_map.put("serviceId", PayEnumConstants.SERVICE.TRANSACTION.getValue());// 交易提醒
//		less_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
//		List<DredgeService> transaction_dredgeServiceList = dredgeServiceDao
//				.getServiceRecordByUserId(less_map);
//		if (CollectionUtils.isNotEmpty(transaction_dredgeServiceList)&&((accountUser.getCellPhone() !=null)&& (accountUser.getCellPhone() !=""))) {
//			// 发送短信 [交易提醒]
//			SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日");		
//			String smsContent="亲，你已在易通上成功订购开通["+serviceName+"]，服务截至时间为"+sf.format(dredgeService.getEndDate())+"。【易通】";
//			//发送短信
//			SMSObject sMSObject=new SMSObject();
//			sMSObject.setMessageContent(smsContent);
//			sMSObject.setUserId(accountUser.getUserId());
//			sMSObject.setDestMobile(accountUser.getCellPhone());
//			sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
//			sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.TRANSACTION.getValue()));
//			smsDao.add(sMSObject);
//		}
//
//		// 1.2)判断用户是否开启了余额不足提醒
//		Map dservice_map = new HashMap();
//		dservice_map.put("userId", userId);
//		dservice_map.put("serviceId", PayEnumConstants.SERVICE.BALANCELESS.getValue());// 系统在初始化服务的时候，服务表里面的数据。5代表短信服务
//		dservice_map.put("flag",
//				PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
//		List<DredgeService> dredgeServiceList = dredgeServiceDao
//				.getServiceRecordByUserId(dservice_map);
//		if (CollectionUtils.isNotEmpty(dredgeServiceList)) {
//			DredgeService remind_dredgeService = dredgeServiceList.get(0);
//			// 1.3)开启了余额提醒
//			if ((accountUser.getUseBalance()) < remind_dredgeService
//					.getBalance()) {
//				if ((StringUtils.equals(dredgeService.getRemindFlag(), PayEnumConstants.REMIND_FLAG.ISNREMIND.value))&&
//						((accountUser.getCellPhone() !=null)&& (accountUser.getCellPhone() !=""))) {
//					// 1.3.0)是否已经提醒了，提醒了就不要再提醒了。
//					// 发送短信 [余额提醒]
//					SimpleDateFormat sf=new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH'时'");	
//					DecimalFormat df = new DecimalFormat("#0.00"); 
//					String smsContent="亲，截止"+sf.format(new Date())+"，你的易通账户余额不足"+df.format(remind_dredgeService.getBalance())+"元，为不影响你的使用，请及时充值。【易通】";
//					//发送短信
//					SMSObject sMSObject=new SMSObject();
//					sMSObject.setMessageContent(smsContent);
//					sMSObject.setDestMobile(accountUser.getCellPhone());
//					sMSObject.setUserId(accountUser.getUserId());
//					sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
//					sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.BALANCELESS.getValue()));
//					smsDao.add(sMSObject);
//				}
//			}
//		}
//
//		return true;
//	}

	
	@Override
	public boolean saveSMSInfo(SMSObject obj) {
		if(obj==null)  return false;	
		
		if(obj.getUserId()==null){
			logger.error("userId不能为空");
			return false;
		}
		
		if(obj.getMessageContent()==null){
			logger.error("短信发送内容不能为空");
			return false;
		}
		if(obj.getSequenceID()==null){
			logger.error("sequenceID不能为空");
			return false;
		}
		if(obj.getDestMobile()==null){
			logger.error("目标手机不能为空");
			return false;
		}
		if(obj.getSmsType()==null){
			logger.error("smsType不能为空");
			return false;
		}
		obj.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
		
		//可用短信数
		//SMSMessage smsMessage = this.supplySMSMessage(obj.getUserId());		
		//若当前日期不在开始日期，截止日期区间内，则返回false
	/*	if(DateUtil.compareDay(new Date(),smsMessage.getBeginDate())||DateUtil.compareDay(smsMessage.getEndDate(),new Date()))
			return false;*/
		
		//int smsUsecount = smsMessage.getSmsUsecount();
		
		//计算实际发送的短信数
		int count = obj.getMessageContent().trim().length();
		if(count<=70) {
			count = 1;
		}else{
			count = count/67+1;
		} 
		 User user= UserDao.getUserById(obj.getUserId());
		 if(StringUtils.equals("1", user.getUserType())||
				 StringUtils.equals("2", user.getUserType())||
				StringUtils.equals("3", user.getUserType())||
				StringUtils.equals("4", user.getUserType())){
			 if(smsUseCount(obj.getUserId())<count){
				 logger.error("========================短信不足,不发(主账号)============================");
					//短信不足,不发(主账号)
					return false;
				}
		 }else{
			 if(smsUseCount(user.getParentId())<count){
					//短信不足,不发(子账号)
				 logger.error("========================短信不足,不发(子账号)======================");
					return false;
				}
		 }
		
		//2)短信业务处理
		  DredgeService dredgeService=null;
		  DredgeService source_dredgeService=null;
		 
		  if(StringUtils.equals("1", user.getUserType())||
				 StringUtils.equals("2", user.getUserType())||
				StringUtils.equals("3", user.getUserType())||
				StringUtils.equals("4", user.getUserType())){
			  Map map=new HashMap();		  
			  map.put("userId", obj.getUserId());
			  map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
			  map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
			  List<DredgeService> dredgeServiceList=
				  dredgeServiceDao.getServiceRecordByUserId(map);
				   if(CollectionUtils.isNotEmpty(dredgeServiceList)){
				     dredgeService=dredgeServiceList.get(0);			   
				}
			   
			   Map pt_map=new HashMap();		  
			   pt_map.put("userId", user.getUserSource());
			   pt_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
			   pt_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
			   List<DredgeService> source_dredgeServiceList=
				   dredgeServiceDao.getServiceRecordByUserId(pt_map);
				if(CollectionUtils.isNotEmpty(source_dredgeServiceList)){
						   source_dredgeService=source_dredgeServiceList.get(0);			   
				}
		  }else{
			  Map map=new HashMap();		  
			  map.put("userId", user.getParentId());
			  map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
			  map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
			  List<DredgeService> dredgeServiceList=
				  dredgeServiceDao.getServiceRecordByUserId(map);
				   if(CollectionUtils.isNotEmpty(dredgeServiceList)){
				     dredgeService=dredgeServiceList.get(0);			   
				}
				obj.setUserId(user.getParentId());  //保存主账号的id
				   
			   User source_user= UserDao.getUserById(user.getParentId());
			   Map pt_map=new HashMap();		  
			   pt_map.put("userId", source_user.getUserSource());
			   pt_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
			   pt_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
			   List<DredgeService> source_dredgeServiceList=
				   dredgeServiceDao.getServiceRecordByUserId(pt_map);
				if(CollectionUtils.isNotEmpty(source_dredgeServiceList)){
						   source_dredgeService=source_dredgeServiceList.get(0);			   
				} 
		  }
			  
		  //2.0)在使用短信时,平台用户与分仓账号都有短信的时候。
		  if(dredgeService != null && source_dredgeService !=null){
			
			//若是入驻企业账号则使用自己的短信
			if("1".equals(user.getUserType()) && "C".equals(user.getChildType()))
			{
				  if(dredgeService.getSmsUsecount()>=count){
					  logger.error("=========入驻企业使用自己短信足============="+count);	
					  updateSmscount(obj.getUserId(),dredgeService,count);
				  }
			}
			else
			{
				//2.1)是谁开启了,就从谁的可发短信数扣除短信。
				  if(dredgeService.getBeginDate().before(source_dredgeService.getBeginDate())){
					 //2.1.1.0)使用 自己
					  
					  //先判断短信是否充足
					  if(dredgeService.getSmsUsecount()>=count){
						  //短信足
						  logger.error("=========使用自己短信足============="+count);	
						  updateSmscount(obj.getUserId(),dredgeService,count);
					  }else{
						  //短信不足,先使用自己然后使用平台
						  Integer sms_co=dredgeService.getSmsUsecount();
						  logger.error("=========使用自己短信不足============="+count+"dredgeService.getSmsUsecount():"+dredgeService.getSmsUsecount());	
						  updateSmscount(obj.getUserId(),dredgeService,dredgeService.getSmsUsecount());
						  logger.error("============(count-dredgeService.getSmsUsecount())=========="+(count-sms_co));	
						  updatePTSmscount(obj.getUserId(),source_dredgeService,(count-sms_co));
					  }
					  
				  }else{
					//2.1.1.1)使用 平台
					  
					  //先判断短信是否充足
					  if(source_dredgeService.getSmsUsecount()>=count){
						  //短信足
						  logger.error("=========使用平台短信足============="+count);	
						  updatePTSmscount(obj.getUserId(),source_dredgeService,count);
					  }else{
						  Integer sms_co=source_dredgeService.getSmsUsecount();
						  logger.error("=========使用平台短信不足============="+count+"source_dredgeService.getSmsUsecount():"+source_dredgeService.getSmsUsecount());	
						  updatePTSmscount(obj.getUserId(),source_dredgeService,source_dredgeService.getSmsUsecount());
						  updateSmscount(obj.getUserId(),dredgeService,(count-sms_co));
						 
					  }
				  }			  
			}
			
		  }else if(dredgeService != null && source_dredgeService ==null){
			  //2.2)只有自己开启
			  updateSmscount(obj.getUserId(),dredgeService,count);
		  }else if(dredgeService == null && source_dredgeService !=null){			  
			  //2.3)只有平台开启
			  updatePTSmscount(obj.getUserId(),source_dredgeService,count);
		  }else{
			  logger.error("=========短信模块短信数据不正确!=============");	
		  }
		boolean flag = false;  
		flag = smsDao.add(obj);
		Integer userId = user.getParentId()!=null? user.getParentId():user.getId();
		smsLessRemaind(userId);//添加短信不足提醒
		return flag;
	}	
	
	@Override
	public boolean saveSMSInfo(SMSObject obj,int user_id) {
		logger.error("===================短信平台调用==================================obj="+obj+"  user_id="+user_id);
		if(obj==null)  return false;	
		if(user_id<=0)  return false;
		
		obj.setUserId(user_id);
		return saveSMSInfo(obj);
	}	
	
	public boolean parentStatusOpenUp(int userId) {
		User user=	UserDao.getUserById(userId);
		Map  pare_map=new HashMap();
		pare_map.put("userId", user.getParentId());
		pare_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//平台账号
		pare_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
        List<DredgeService> pare_dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(pare_map);
        if(CollectionUtils.isNotEmpty(pare_dredgeServiceList)){
        	return true;
        }
       return false;
	}	
	public boolean sourceStatusOpenUp(int userId) {
		User user=	UserDao.getUserById(userId);
		Map  source_map=new HashMap();
		source_map.put("userId", user.getUserSource());
		source_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//平台账号
		source_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
        List<DredgeService> source_dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(source_map);
        if(CollectionUtils.isNotEmpty(source_dredgeServiceList)){
        	return true;
        }
       return false;
	}	
	public SMSMessage parentSMSMessage(Integer userId) {
		User user=	UserDao.getUserById(userId);
		Map  pare_map=new HashMap();
		pare_map.put("userId", user.getParentId());
		pare_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//平台账号
		pare_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
        List<DredgeService> pare_dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(pare_map);
        if(CollectionUtils.isNotEmpty(pare_dredgeServiceList)){
        	 SMSMessage SMSMessage=new SMSMessage();
			 DredgeService dredgeService=pare_dredgeServiceList.get(0);
			 try {
				PropertyUtils.copyProperties(SMSMessage, dredgeService);
			} catch (Exception e) {
				//记日志
				Logs logs=new Logs();
				logs.setOperName("短信调用转化对象");
				logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
				logs.setUserId(userId);
				logs.setRemark("转化给短信的对象出错了");
				logsDao.add(logs);
				logger.error("转化给短信的对象出错了SmsUsecount:"+dredgeService.getSmsUsecount()
						+"BeginDate:"+dredgeService.getBeginDate()+"EndDate:"+dredgeService.getEndDate());
				e.printStackTrace();
			}
				return SMSMessage;
        }
        return null;
	}
	
	public SMSMessage sourceSMSMessage(Integer userId) {
		User user=	UserDao.getUserById(userId);
		Map  source_map=new HashMap();
		source_map.put("userId", user.getUserSource());
		source_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//平台账号
		source_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
        List<DredgeService> source_dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(source_map);
        if(CollectionUtils.isNotEmpty(source_dredgeServiceList)){
        	 SMSMessage SMSMessage=new SMSMessage();
			 DredgeService dredgeService=source_dredgeServiceList.get(0);
			 try {
				PropertyUtils.copyProperties(SMSMessage, dredgeService);
			} catch (Exception e) {
				//记日志
				Logs logs=new Logs();
				logs.setOperName("短信调用转化对象");
				logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
				logs.setUserId(userId);
				logs.setRemark("转化给短信的对象出错了");
				logsDao.add(logs);
				logger.error("转化给短信的对象出错了SmsUsecount:"+dredgeService.getSmsUsecount()
						+"BeginDate:"+dredgeService.getBeginDate()+"EndDate:"+dredgeService.getEndDate());
				e.printStackTrace();
			}
				return SMSMessage;
        }
        return null;
	}
	
	public AccountUser addAccountUserMessage(Integer userId){
		AccountUser accountUser = new AccountUser();
		accountUser.setUserId(userId);
		accountUser.setRemark("购买短信");
		// 1)创建账户
		if (accountUser == null || accountUser.getUserId() == null) {
			logger.info("用户ID：" + LogInfoEnum.PARAM_EMPTY.getValue());
		}
		accountUserDao.add(accountUser);

		// 2)插入免费的服务

		List<PayService> pay_list = payServiceDao.getFreeServiceList();// 查询免费的服务
		if (CollectionUtils.isEmpty(pay_list)) {
			logger.info("没有免费的服务");
		} else {
			List<DredgeService> dredgeFreeServices = new ArrayList<DredgeService>();
			for (PayService payService : pay_list) {
				DredgeService dredgeService = new DredgeService();
				dredgeService.setRemark(payService.getName());
				dredgeService.setFlag(PayEnumConstants.PAY_FLAG.ISFLAG.value);
				dredgeService.setServiceId(payService.getId());
				dredgeService.setUserId(accountUser.getUserId());
				if(1==payService.getId()){
					//2.1)余额不足提醒
					dredgeService.setBalance(5.0);
				}else if(4==payService.getId()){
					//短信不足提醒
					dredgeService.setSmsLess(50);
				}
				dredgeFreeServices.add(dredgeService);
			}
			dredgeServiceDao.insertBatch(dredgeFreeServices);
		}
		return accountUser;
	}
	
	public Integer dealBuss(AccountUser accountUser,Map map,Integer userId){
		if(accountUser.getUseBalance()<Double.parseDouble(map.get("prince").toString())){
			//如果是分仓账号
			if(accountUser != null){
				User user = UserDao.getUserById(accountUser.getUserId());
				if((user.getChildType() != null && "1".equals(user.getUserType()) && user.getChildType().equals("B")) ||"41".equals(user.getUserType())){
					AccountUser aUser = new AccountUser();
					if(user.getChildType() != null && "1".equals(user.getUserType()) && user.getChildType().equals("B")){
						aUser = accountUserDao.getAccountUserByUserId(Integer.parseInt(user.getUserSource()));
					}
					if ("41".equals(user.getUserType())) {
						aUser = accountUserDao.getAccountUserByUserId(user.getParentId());
					}
					
					if(aUser != null && (accountUser.getUseBalance() + aUser.getUseBalance()) < Double.parseDouble(map.get("prince").toString())){
						//记日志
						Logs logs=new Logs();
						logs.setOperName(map.get("name").toString());
						logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
						logs.setUserId(Integer.valueOf(map.get("userId").toString()));
						logs.setRemark("短信调用余额不足");
						logsDao.add(logs);
						return 3;
					}
					
					//1.1)修改当前账户余额
					Map user_map=new HashMap();
					user_map.put("userId", accountUser.getUserId());
					user_map.put("money", accountUser.getUseBalance());
					user_map.put("updateTime", accountUser.getUpdateTime());//通过updateTime判定记录是否修改了
					Boolean  bool=accountUserDao.updateAccountUserBalanceLowerByUserId(user_map);
					Boolean  bool_parent = true;
					if(bool){
						//1.1)修改平台用户账号的余额
						Map user_parent_map=new HashMap();
						user_parent_map.put("userId", aUser.getUserId());
						user_parent_map.put("money", Double.parseDouble(map.get("prince").toString())-accountUser.getUseBalance());
						user_parent_map.put("updateTime", aUser.getUpdateTime());//通过updateTime判定记录是否修改了
						bool_parent =accountUserDao.updateAccountUserBalanceLowerByUserId(user_parent_map);
						if(bool_parent){
							//记日志
							Logs logs=new Logs();
							logs.setOperName(map.get("name").toString());
							logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
							logs.setUserId(aUser.getUserId());
							logs.setRemark("短信调用修改的平台账户的余额:"+(Double.parseDouble(map.get("prince").toString())-accountUser.getUseBalance()));
							logsDao.add(logs);
							logger.error("短信调用,修改用户账户不成功!");
						}
					}
				  
				   if(BooleanUtils.isFalse(bool) || BooleanUtils.isFalse(bool_parent)){
					 //记日志
						Logs logs=new Logs();
						logs.setOperName(map.get("name").toString());
						logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
						logs.setUserId(Integer.valueOf(map.get("userId").toString()));
						logs.setRemark("短信调用修改账号余额失败原因可能服务记录被更改,价格:"+Double.parseDouble(map.get("prince").toString()));
						logsDao.add(logs);
						logger.error("短信调用,修改用户账户不成功!");
						return 4;
					}
				}else {
					//记日志
					Logs logs=new Logs();
					logs.setOperName(map.get("name").toString());
					logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
					logs.setUserId(Integer.valueOf(map.get("userId").toString()));
					logs.setRemark("短信调用余额不足");
					logsDao.add(logs);
					return 3;
				}
			}
		}else {
			//1.1)修改余额
			Map user_map=new HashMap();
			user_map.put("userId", accountUser.getUserId());
			user_map.put("money", Double.parseDouble(map.get("prince").toString()));
			user_map.put("updateTime", accountUser.getUpdateTime());//通过updateTime判定记录是否修改了
			Boolean  bool=accountUserDao.updateAccountUserBalanceLowerByUserId(user_map);
			 if(BooleanUtils.isFalse(bool)){
				 //记日志
					Logs logs=new Logs();
					logs.setOperName(map.get("name").toString());
					logs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
					logs.setUserId(Integer.valueOf(map.get("userId").toString()));
					logs.setRemark("短信调用修改账号余额失败原因可能服务记录被更改,价格:"+Double.parseDouble(map.get("prince").toString()));
					logsDao.add(logs);
					logger.error("短信调用,修改用户账户不成功!");
					return 4;
				}
		}
	
	   Map dservice_map=new HashMap();
		dservice_map.put("userId", accountUser.getUserId());
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//系统在初始化服务的时候，服务表里面的数据。5代表短信服务
		//dservice_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		 List<DredgeService> dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		 if(CollectionUtils.isEmpty(dredgeServiceList)){
			 DredgeService dService = new DredgeService();
				dService.setUserId(accountUser.getUserId());
				dService.setServiceId(PayEnumConstants.SERVICE.SMS.getValue());
				dService.setAutoRenew(PayEnumConstants.AUTO_FLAG.ISNAUTO.value);//是否自动续费
				dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
				dService.setCost(Double.parseDouble(map.get("prince").toString()));
				dService.setSmsUsecount(Integer.parseInt(map.get("SMSnum").toString()));
				dService.setSmsAllcount(Integer.parseInt(map.get("SMSnum").toString()));	
				dService.setBeginDate(new Date());
				dredgeServiceDao.add(dService);
		 }else{
			//2)修改开通充值服务表-ec_core_dredgeService
				DredgeService dredgeService=new DredgeService();
				dredgeService.setUserId(accountUser.getUserId());
				dredgeService.setServiceId(PayEnumConstants.SERVICE.SMS.getValue());//系统在初始化服务的时候，服务表里面的数据。5代表短信服务			
				
				dredgeService.setCost(Double.parseDouble(map.get("prince").toString())+dredgeServiceList.get(0).getCost());
				dredgeService.setSmsUsecount(Integer.parseInt(map.get("SMSnum").toString())+dredgeServiceList.get(0).getSmsUsecount());
				dredgeService.setSmsAllcount(Integer.parseInt(map.get("SMSnum").toString())+dredgeServiceList.get(0).getSmsAllcount());		
				
				if(!StringUtils.equals(PayEnumConstants.SERVICEFLAG.ENABLED.getValue(), dredgeService.getFlag())){
					dredgeService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
					dredgeService.setBeginDate(new Date());
				}
				Boolean dredge_bool=false;
				dredge_bool=dredgeServiceDao.updateDredgeserviceBySMS(dredgeService);		
				if(BooleanUtils.isFalse(dredge_bool)){
					 //记日志
						Logs shistotrylogs=new Logs();
						shistotrylogs.setOperName(map.get("name").toString());
						shistotrylogs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
						shistotrylogs.setUserId(Integer.valueOf(map.get("userId").toString()));
						shistotrylogs.setRemark("服务记录被更改");
						logsDao.add(shistotrylogs);
						logger.error("短信调用，修改开通服务信息不成功!");	
						return 4;
					} 
		 }
		
		
		//3)插入交易（支付）明细表-ec_core_payment
		Payment payment=new Payment();
		payment.setUserId(accountUser.getUserId());
		payment.setAuserId(accountUser.getId());
		payment.setDealMoney(Double.parseDouble(map.get("prince").toString()));
		payment.setDealType(PayEnumConstants.DEALTYPE.SMS.getValue());//3 购买短信
		payment.setDealName(map.get("name").toString());
		payment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());
		payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);
		payment.setDelFlag(PayEnumConstants.DEAL_FLAG.NDEL.value);
		payment.setRemark("短信调用购买短信包");
		payment.setCreaterId((Integer)map.get("createrId"));//操作人ID
		paymentDao.add(payment);
		
		//4）插入服务记录历史表-ec_core_serviceHistory
		ServiceHistory serviceHistory=new ServiceHistory();
		serviceHistory.setUserId(accountUser.getUserId());
		serviceHistory.setDserviceId(5);//系统在初始化服务的时候，服务表里面的数据。5代表短信服务
		serviceHistory.setDealType(PayEnumConstants.DEALTYPE.SMS.getValue());
		serviceHistory.setDealName(map.get("name").toString());
		serviceHistory.setSmsCount(Integer.parseInt(map.get("SMSnum").toString()));
		serviceHistory.setCost(Double.parseDouble(map.get("prince").toString()));
		serviceHistory.setPromCost(Double.parseDouble(map.get("prince").toString()));
		serviceHistory.setBeginDate(new Date());
		serviceHistory.setAutoRenew(PayEnumConstants.AUTO_FLAG.ISNAUTO.value);
		serviceHistory.setRemark("短信调用购买短信包");
		serviceHistory.setPayId(payment.getId());
		serviceHistoryDao.add(serviceHistory);
		//记日志
		Logs shistotrylogs=new Logs();
		shistotrylogs.setOperName(map.get("name").toString());
		shistotrylogs.setOperType(PayEnumConstants.OPERTYPE.SMS.getValue());
		shistotrylogs.setUserId(userId);
		shistotrylogs.setRemark("短信调用parmentId："+payment.getId());
		logsDao.add(shistotrylogs);
	   return 0;
	}
	public Integer smsUseCount(Integer userId) {				
		//AccountUser accountUser=accountUserDao.getAccountUserByUserId(userId);
		Integer count=0;
		Map  map=new HashMap();
		map.put("userId", userId);
		map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
        List<DredgeService> dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(map);
        if(CollectionUtils.isNotEmpty(dredgeServiceList)){
        	//1)可用数量
        	count =count+dredgeServiceList.get(0).getSmsUsecount();
        }
		User user=	UserDao.getUserById(userId);
		
		//若是入驻企业账号则不能使用平台的短信
		if(("1".equals(user.getUserType()) && "C".equals(user.getChildType()))==false)
		{
			Map  source_map=new HashMap();
			source_map.put("userId", user.getUserSource());
			source_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//平台账号
			source_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
	        List<DredgeService> source_dredgeServiceList=dredgeServiceDao.getServiceRecordByUserId(source_map);
	        if(CollectionUtils.isNotEmpty(source_dredgeServiceList)){
	        	//2)平台可用数量
	        	count =count+source_dredgeServiceList.get(0).getSmsUsecount();
	        }
		}		
        return count;
	}
	
	
	public DredgeService  compreDredgeService(Integer userId){
		  DredgeService dredgeService=null;
		  DredgeService source_dredgeService=null;
		  Map map=new HashMap();		  
		  map.put("userId", userId);
		  map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
		  map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		  List<DredgeService> dredgeServiceList=
			  dredgeServiceDao.getServiceRecordByUserId(map);
			   if(CollectionUtils.isNotEmpty(dredgeServiceList)){
			     dredgeService=dredgeServiceList.get(0);			   
			    }
			  return null;
	}
	
	public boolean  updateSmscount(Integer userId, DredgeService dredgeService,Integer smsCount){	
			    //2.1)当前账号是否开通服务
			   	//2.1.1)短信是否充足(在插入短信的时候已经判断)
			    	 if(dredgeService.getSmsUsecount()>=smsCount){
			    		 dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()-smsCount);
						 dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()+smsCount);
						 dredgeServiceDao.edit(dredgeService); 
						return true;
			           }
			   return false;
	}
	
	public boolean  updatePTSmscount(Integer userId,DredgeService dredgeService,Integer smsCount){
				    //2.1)当前账号是否开通服务
				   	//2.1.1)短信是否充足(在插入短信的时候已经判断)
				    	 if(dredgeService.getSmsUsecount()>=smsCount){
				    		 dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()-smsCount);
							 dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()+smsCount);
							 dredgeServiceDao.edit(dredgeService); 
							 return true;
				      }
			  return false;
	}

	/**
	 * 短信不足提醒	
	 * @param userId
	 */
	public void smsLessRemaind(Integer userId)
	{
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(userId);		
		String cellPhone = getCellPhone(accountUser);
		//判断用户是否开启了短信不足提醒	
		Map dservice_map = new HashMap();
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.SMSLESS.getValue());//4代表短信不足提醒
		dservice_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> smsLessList = dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//5代表短信服务
		dservice_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> smsList = dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		
		if (CollectionUtils.isNotEmpty(smsLessList)&&CollectionUtils.isNotEmpty(smsList)) {
			DredgeService  smsLess = smsLessList.get(0);
			DredgeService  sms = smsList.get(0);
			// 开启了短信不足提醒
			if ((sms.getSmsUsecount() - smsLess.getSmsLess())== 0) {
				if(cellPhone !=null && cellPhone !="") {
					// 发送 短信不足提醒
					SimpleDateFormat sf=new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH'时'");	
					DecimalFormat df = new DecimalFormat("#0.00"); 
					String smsContent="亲，截止"+sf.format(new Date())+"，你的易通可发短信数不足"+smsLess.getSmsLess()+"条，为不影响你的使用，请及时购买短信。【易通】";
					//发送短信
					SMSObject sMSObject=new SMSObject();
					sMSObject.setMessageContent(smsContent);
					sMSObject.setDestMobile(cellPhone);
					sMSObject.setUserId(accountUser.getUserId());
					sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
					sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.SMSLESS.getValue()));
				    SMSObjectService.add(sMSObject);
				}
			}
		}
	}
	
	public String getCellPhone(AccountUser accountUser){
		String cellPhone= "";
        cellPhone = accountUser.getCellPhone();
		if(cellPhone==null) {
		   User user = userDao.getUserById(accountUser.getUserId());
		   if(user != null && user.getMobilePhone() != null && user.getMobilePhone() != ""){
			  cellPhone = user.getMobilePhone();
		   }	
		}
		
		return cellPhone;
	}

	@Override
	public Integer smsLessForRemaind(Integer userId) {
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(userId);		
		Integer result = null;
		//判断用户是否开启了短信不足提醒	
		Map dservice_map = new HashMap();
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.SMSLESS.getValue());//4代表短信不足提醒
		dservice_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> smsLessList = dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());//5代表短信服务
		dservice_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> smsList = dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		if (CollectionUtils.isNotEmpty(smsLessList)&&CollectionUtils.isNotEmpty(smsList)) {
			DredgeService  smsLess = smsLessList.get(0);
			DredgeService  sms = smsList.get(0);
			// 开启了短信不足提醒
			if ((sms.getSmsUsecount()- smsLess.getSmsLess())== 0) { 
		      result = smsLess.getSmsLess();
			}
		}
		return result;
	}
	/**
	 * 余额提醒 
	 * @param userId
	 * @param serviceName
	 * @return
	 */
	public boolean sendSMSForMoney(Integer userId,String serviceName) {
		serviceName=serviceName.replaceAll("开通", "");
		serviceName=serviceName.replaceAll("续费", "");
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(userId);		
		// 1.2)判断用户是否开启了余额不足提醒
		// 1.2)判断用户是否开启了余额不足提醒
		Map dservice_map = new HashMap();
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.BALANCELESS.getValue());// 系统在初始化服务的时候，服务表里面的数据。5代表短信服务
		dservice_map.put("flag",PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> dredgeServiceList = dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		String cellPhone = getCellPhone(accountUser);
		if (CollectionUtils.isNotEmpty(dredgeServiceList)) {
			DredgeService remind_dredgeService = dredgeServiceList.get(0);
			// 1.3)开启了余额不足提醒
			if ((accountUser.getUseBalance()) < remind_dredgeService.getBalance()) {
				if ((cellPhone!=null&&cellPhone.length()>0)) {
					SimpleDateFormat sf=new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH'时'");	
					DecimalFormat df = new DecimalFormat("#0.00"); 
					String smsContent="亲，截止"+sf.format(new Date())+"，你的易通账户余额不足"+df.format(remind_dredgeService.getBalance())+"元，为不影响你的使用，请及时充值。ws【易通】";
					//发送短信
					SMSObject sMSObject=new SMSObject();
					sMSObject.setMessageContent(smsContent);
					//sMSObject.setDestMobile(accountUser.getCellPhone());
					sMSObject.setDestMobile(cellPhone);
					sMSObject.setUserId(accountUser.getUserId());
					sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
					sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.BALANCELESS.getValue()));
				    SMSObjectService.add(sMSObject);
				}
			}
		}
		return true;
	}
}
