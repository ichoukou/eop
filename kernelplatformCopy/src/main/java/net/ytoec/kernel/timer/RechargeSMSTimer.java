package net.ytoec.kernel.timer;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.AccountUserService;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.LogsService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.ServiceHistoryService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 充值短信的定时器
 * @author guoliang.wang 
 */
public class RechargeSMSTimer extends QuartzJobBean{
	
	private DredgeServiceService<DredgeService> dredgeServiceService;
	private AccountUserService<AccountUser> accountUserService;
	private PaymentService<Payment> paymentService;
	private SMSObjectService<SMSObject> SMSObjectService;
	private ServiceHistoryService<ServiceHistory> serviceHistoryService;
	private PayServiceService<PayService> payServiceService;
	private LogsService<Logs> logsService;
	private UserService<User> userService;

	private static boolean isRunning = false;
	private Logger logger = LoggerFactory.getLogger(RechargeSMSTimer.class);
	
   // private SMSObjectService<SMSObject> SMSObjectServices=SMSSender.getSMSObjectServiceInstance();
	
	/*
	 * //作用2（服务到期提醒）:	服务到期前一个星期提醒,到期后改变状态（只提醒一次，到期时提醒一次）
	 * //作用3（未付款的订单超过时间自动删除）：有效期保留7天
	 * //作用4（自动续费）	
	 * //作用5（服务到期）	
	 * //作用6（当天的日志）	
	 * (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	@SuppressWarnings("all")
	protected void executeInternal(JobExecutionContext jobExecutionContext)
			throws JobExecutionException {
		try
		{
		     if(isRunning)  return;		 
			
		     isRunning = true;
			
			Long beginTimeMIllis=System.currentTimeMillis();
			
			//3)未付款的订单(超过7天删除) 
			 Map pay_map=new HashMap();
			 List<AccountUser> accountUserList=
				 accountUserService.getAccountUserListUseTime(pay_map);
			 if(CollectionUtils.isNotEmpty(accountUserList)){
				 for(AccountUser accountUser:accountUserList){		
					 //==========begin user for ===============
					     pay_map.put("delFlag", PayEnumConstants.DEAL_FLAG.NDEL.value);
						 pay_map.put("createTime", DateUtil.getDateAfter(new Date(),7));//7天前的日期 --关闭测试下
						 pay_map.put("dealStatus", PayEnumConstants.DEALSTATUS.WAIT.getValue());//0 等待付款 
						 pay_map.put("userId", accountUser.getUserId());//测试
						List<Payment> paymentList= paymentService.getPaymentListByUserId(pay_map);
						Boolean uUser_bool = false;
						StringBuffer sb=new StringBuffer();
						 if(CollectionUtils.isNotEmpty(paymentList)){
								List<Integer> payIdList=new ArrayList<Integer>();//存放已经开通服务的id[非在线充值]	
								List<Integer> onlineIdList=new ArrayList<Integer>();//存放已经开通服务的id[在线充值]
								Double backMoney=accountUser.getUseBalance();
							    for(Payment payment:paymentList){						
									 sb.append(payment.getId()+"|");
									 if(StringUtils.equals(payment.getDealType(), PayEnumConstants.DEALTYPE.ONLINE.getValue())){
										//在线充值,没有余额退回	
										 onlineIdList.add(payment.getId());
										 continue;
									  }								 
									    payIdList.add(payment.getId());
									    
									    Map historyMap=new HashMap();
										historyMap.put("userId", payment.getUserId());
										historyMap.put("payId", payment.getId());
										
										List<ServiceHistory> historyList=
											serviceHistoryService.getServiceHistoryListByDUserId(historyMap);
										if(CollectionUtils.isNotEmpty(historyList)){
											ServiceHistory serviceHistory=historyList.get(0);
											//关闭把可用余额[退回来]
											backMoney=backMoney+serviceHistory.getPromCost()-payment.getDealMoney();									  
										   }
									}//payment for end
							 
							   //3)修改可用余额ec_core_accountUser
							   if ((accountUser != null)&&CollectionUtils.isNotEmpty(payIdList)) {
									Map account_map = new HashMap();
									account_map.put("userId", accountUser.getUserId());
									account_map.put("updateTime", accountUser.getUpdateTime());
									//可用余额=可用余额+冻结的余额
									account_map.put("money",backMoney);								
									uUser_bool = accountUserService.updateUseBalanceByUserId(account_map);								
									if (BooleanUtils.isFalse(uUser_bool)) {
										Logs logs = new Logs();
										logs.setOperName("定时器修改可用余额");
										logs.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
										logs.setUserId(accountUser.getUserId());
										logs.setRemark("定时器修改到期的可用余额失败出险这样的日志[请检查]userId："+accountUser.getUserId());
										logsService.add(logs);
									}
								}
							    //调用servic 批量处理onlineIdList(在线充值)			    
								Map payId_map = new HashMap();
								//payId_map.put("dealStatus", PayEnumConstants.DEALSTATUS.CLOSED.getValue());
								payId_map.put("delFlag", PayEnumConstants.DEAL_FLAG.SYSTEMCLOSE.value);
								if(CollectionUtils.isNotEmpty(onlineIdList)){
									payId_map.put("payIdList", onlineIdList);				
									paymentService.updateBatchByDealStatus(payId_map);		
								}
								
							
								if(BooleanUtils.isTrue(uUser_bool)&&CollectionUtils.isNotEmpty(payIdList)){
									payId_map.put("payIdList", payIdList);
									paymentService.updateBatchByDealStatus(payId_map);	
								}
							
						 }
					 
					 //==========end user for ===============
				 }
			}
			
			 List<DredgeService> dredgeServiceList=dredgeServiceService.getOpenserviceTimeList();
			 String cellPhone = "";
			 if(CollectionUtils.isNotEmpty(dredgeServiceList)){
				 for(DredgeService dredgeService:dredgeServiceList){
						if(dredgeService.getServiceId()==PayEnumConstants.SERVICE.BALANCELESS.getValue()||
								dredgeService.getServiceId()==PayEnumConstants.SERVICE.TRANSACTION.getValue()||
								dredgeService.getServiceId()==PayEnumConstants.SERVICE.MATURITY.getValue()||
								dredgeService.getServiceId()==PayEnumConstants.SERVICE.SMSLESS.getValue()||
								dredgeService.getServiceId()==PayEnumConstants.SERVICE.SMS.getValue()){
							//免费服务与短信服务不做处理
							continue;				
						}else{
							//其它服务
							
							//1)是否开通了服务到期提醒
							Map map=new HashMap();
							map.put("userId", dredgeService.getUserId());
							map.put("serviceId", PayEnumConstants.SERVICE.MATURITY.getValue());//服务到期提醒
							map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
							 List<DredgeService> expire_dredgeServiceList=dredgeServiceService.getServiceRecordByUserId(map);
							 if(CollectionUtils.isNotEmpty(expire_dredgeServiceList)&&(dredgeService.getEndDate() !=null)){
								 //开通的服务到期提醒
								// DredgeService expire_dredgeService=expire_dredgeServiceList.get(0);
								   if(DateUtil.isSameDay(dredgeService.getEndDate(), new Date())){
									   //如果是今天到期 ，则发生短信提醒
									// 发送短信 [服务到期]
									   AccountUser accountUser = accountUserService.getAccountUserByUserId(dredgeService.getUserId());
									   cellPhone = getCellPhone(accountUser);
									   if((cellPhone !=null)&& (cellPhone !="")){
										   PayService payService=new PayService();
										   payService.setId(dredgeService.getServiceId());
										   payService= payServiceService.get(payService);
										   if(payService !=null){
											   SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日");	
											   String smsContent="亲，你在易通订购的【"+payService.getName()+"】已于"+sf.format(dredgeService.getEndDate())+"到期失效。如想继续使用，请登录易通订购。【易通】";
												//发送短信
												SMSObject sMSObject=new SMSObject();
												sMSObject.setUserId(dredgeService.getUserId());
												sMSObject.setMessageContent(smsContent);
												sMSObject.setDestMobile(cellPhone);
												sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
												sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.MATURITY.getValue()));
												SMSObjectService.add(sMSObject);
										   }
										  
									   }
											
										
										
								   }else{
									   if(dredgeService.getEndDate() !=null){
										   Double diff_day=  DateUtil.dayInterval(dredgeService.getEndDate(),new Date());
											 if(diff_day<=7){
												 //提前一个星期提醒 (如果已经提醒了不再提醒，没有提醒则提醒，提醒后 改变状态)
												 if(StringUtils.equals(PayEnumConstants.REMIND_FLAG.ISNREMIND.value, dredgeService.getRemindFlag())){
													 //没有提醒（调用短信提醒）等待短信
													   AccountUser accountUser = accountUserService.getAccountUserByUserId(dredgeService.getUserId());
													   cellPhone = getCellPhone(accountUser);
													   if((cellPhone !=null)&& (cellPhone !="")){
														   PayService payService=new PayService();
														   payService.setId(dredgeService.getServiceId());
														   payService= payServiceService.get(payService);
														   SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日");
														   
														   String smsContent="亲，你在易通订购的【"+payService.getName()+"】将于"+sf.format(dredgeService.getEndDate())+"失效。如想继续使用，请登录易通订购。【易通】";
														   //发送短信
															SMSObject sMSObject=new SMSObject();
															sMSObject.setUserId(dredgeService.getUserId());
															sMSObject.setMessageContent(smsContent);
															sMSObject.setDestMobile(cellPhone);
															sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
															sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.MATURITY.getValue()));
															SMSObjectService.add(sMSObject);
													   }
													 //修改状态已经提醒updateDredgeserviceBalanceById
													 dredgeService.setRemindFlag(PayEnumConstants.REMIND_FLAG.ISREMIND.value);
													 dredgeServiceService.edit(dredgeService);
													 
													 Logs logs=new Logs();
													 logs.setOperName("定时器修改提醒状态");
													 logs.setOperType(PayEnumConstants.OPERTYPE.TIMER.getValue());
													 logs.setUserId(dredgeService.getUserId());
													 logs.setRemark("修改的开通充值服务id"+dredgeService.getId());
													 logsService.add(logs);
												 }
											 }
										   }
									   }
									
							 }
							
						/*	 //2)是否自动续费(到期自动续费)
							 if(StringUtils.equals(PayEnumConstants.AUTO_FLAG.ISAUTO.value, dredgeService.getAutoRenew())){
							     //[时间就是今天的,如果当天续费失败.后面的程序就会改为到期了]
								 if(DateUtil.isSameDay(dredgeService.getEndDate(), new Date())){
									 //if((new Date()).getTime()>=(dredgeService.getEndDate()).getTime()){
								 
								 //自动续费
								 Map sHistory_map=new HashMap();
								 sHistory_map.put("dserviceId", dredgeService.getId());
								 sHistory_map.put("userId", dredgeService.getUserId());
								 List<ServiceHistory> serviceHistoryList= serviceHistoryService.getServiceHistoryListByDUserId(sHistory_map);
								 if(CollectionUtils.isNotEmpty(serviceHistoryList)){
									 ServiceHistory serviceHistory=serviceHistoryList.get(0);
									 //判断余额是否充足
									// <AccountUser> accountUserService
									 AccountUser accountUser = accountUserService.getAccountUserByUserId(dredgeService.getUserId());
									 if(accountUser.getUseBalance()<serviceHistory.getPromCost()){
										//（注：如果余额不足，不续费发送短信通知） 
										 //等待短信
										 if((accountUser.getCellPhone() !=null)&& (accountUser.getCellPhone() !="")){
											   PayService payService=new PayService();
											   payService.setId(dredgeService.getServiceId());
											   payService= payServiceService.get(payService);
											   String smsContent="亲，您在易通的余额不足，无法给【"+payService.getName()+"】服务续费，如需继续续费，请充值。【易通】";
												//发送短信
												SMSObject sMSObject=new SMSObject();
												sMSObject.setUserId(dredgeService.getUserId());
												sMSObject.setMessageContent(smsContent);
												sMSObject.setDestMobile(accountUser.getCellPhone());
												sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
												sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.BALANCELESS.getValue()));
												SMSObjectService.add(sMSObject);
										   }
										 
										 //修改开通充值服务表-ec_core_dredgeService 不再自动续费
										 dredgeService.setAutoRenew(PayEnumConstants.AUTO_FLAG.ISNAUTO.value);
											  try{
												 dredgeServiceService.updateDredgeserviceBalanceById(dredgeService);
												}catch(Exception e){
													Log.error("自动续费修改开通充值服务表[续费状态]报异常");
												}
										 Logs logs=new Logs();
										 logs.setOperName("定时器自动续费");
										 logs.setOperType(PayEnumConstants.OPERTYPE.TIMER.getValue());
										 logs.setUserId(dredgeService.getUserId());
										 logs.setRemark("定时器自动续费余额不足[不再续费]可用余额: "+accountUser.getUseBalance() +"续费费用: "+serviceHistory.getPromCost());
										 logsService.add(logs);
									 }else{
										 //自动续费的步骤：1）修改账户表-ec_core_accountUser 2）修改开通充值服务表-ec_core_dredgeService
										 //3）修改交易（支付）明细表-ec_core_payment 插入一条记录
										 //4)插入服务记录历史表-ec_core_serviceHistory									
										 
										 Boolean bool= paymentService.autoPayment(serviceHistory,dredgeService);//整个操作放到支付的service中实现
										 
										 //[5]业务处理完后,判断是否开通了提醒
										  if(BooleanUtils.isTrue(bool)){
											  
											   //5.1)是否开通了交易提醒 5.2)判断用户是否开启了余额不足提醒																
												paymentService.sendSMSByUserId(dredgeService.getUserId(),serviceHistory.getDealName(),dredgeService);//发送余额提醒,交易提醒
	
											  // 6）记下整个日志  
											    Logs logs=new Logs();
												 logs.setOperName("定时器自动续费提醒");
												 logs.setOperType(PayEnumConstants.OPERTYPE.AUTOFEE.getValue());
												 logs.setUserId(dredgeService.getUserId());
												 logs.setRemark("定时器自动续费费用为: "+serviceHistory.getPromCost());
												 logsService.add(logs);
										  }else{
											     Logs logs=new Logs();
												 logs.setOperName("定时器自动续费失败");
												 logs.setOperType(PayEnumConstants.OPERTYPE.AUTOFAIL.getValue());
												 logs.setUserId(dredgeService.getUserId());
												 logs.setRemark("定时器自动续费失败 ,开通服务id:"+dredgeService.getId());
												 logsService.add(logs);
										  }
										
									 }
									
								 }
							   }
							 }*/
							
							//4)如果服务到期,则修改服务状态 改为：3到期
							 if(StringUtils.equals(PayEnumConstants.AUTO_FLAG.ISNAUTO.value, dredgeService.getAutoRenew())){
								// if(DateUtil.isSameDay(dredgeService.getEndDate(), new Date())){//今天
								 if((new Date()).getTime()>=(dredgeService.getEndDate()).getTime()){
									   //只要时间超过
									 dredgeService.setFlag(PayEnumConstants.SERVICEFLAG.MATURITY.getValue());
									 try{
										 dredgeServiceService.edit(dredgeService) ; 
									 }catch(Exception e){
										     Logs logs=new Logs();
											 logs.setOperName("定时器修改到期状态失败");
											 logs.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
											 logs.setUserId(dredgeService.getUserId());
											 logs.setRemark("定时器修改到期状态失败 服务Id"+dredgeService.getServiceId());
											 logsService.add(logs);
									  }							
								   } 
							 }						
						  }
						
					 }	
				 	   
			 }	
			 			
			  //3)记下当天定时器的日志
		   /*  Long endTimeMIllis=System.currentTimeMillis();
		     Logs logs=new Logs();
			 logs.setOperName("定期器当天执行");
			 logs.setOperType(PayEnumConstants.OPERTYPE.TIMER.getValue());
			 logs.setUserId(0);//0 代表当天日志
			 logs.setRemark("userID为0代表当天定时器给的默认值,所用时间为(毫秒):"+(endTimeMIllis-beginTimeMIllis));
			 logsService.add(logs);*/
			 isRunning = false;
		}
		catch(Exception e) {
			isRunning = false;
			logger.error("RechargeSMSTimer   运行出现异常...."+e.getMessage());
		}
	}

	public DredgeServiceService<DredgeService> getDredgeServiceService() {
		return dredgeServiceService;
	}

	public void setDredgeServiceService(
			DredgeServiceService<DredgeService> dredgeServiceService) {
		this.dredgeServiceService = dredgeServiceService;
	}

	public AccountUserService<AccountUser> getAccountUserService() {
		return accountUserService;
	}

	public void setAccountUserService(
			AccountUserService<AccountUser> accountUserService) {
		this.accountUserService = accountUserService;
	}

	public PaymentService<Payment> getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService<Payment> paymentService) {
		this.paymentService = paymentService;
	}

	public ServiceHistoryService<ServiceHistory> getServiceHistoryService() {
		return serviceHistoryService;
	}

	public void setServiceHistoryService(
			ServiceHistoryService<ServiceHistory> serviceHistoryService) {
		this.serviceHistoryService = serviceHistoryService;
	}

	public LogsService<Logs> getLogsService() {
		return logsService;
	}

	public void setLogsService(LogsService<Logs> logsService) {
		this.logsService = logsService;
	}

	

	public PayServiceService<PayService> getPayServiceService() {
		return payServiceService;
	}

	public void setPayServiceService(PayServiceService<PayService> payServiceService) {
		this.payServiceService = payServiceService;
	}

	public SMSObjectService<SMSObject> getSMSObjectService() {
		return SMSObjectService;
	}

	public void setSMSObjectService(SMSObjectService<SMSObject> sMSObjectService) {
		SMSObjectService = sMSObjectService;
	}
	
	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public String getCellPhone(AccountUser accountUser){
		String cellPhone= "";
        cellPhone = accountUser.getCellPhone();
		if(cellPhone==null) {
		   User user = userService.getUserById(accountUser.getUserId());
		   if(user != null && user.getMobilePhone() != null && user.getMobilePhone() != ""){
			  cellPhone = user.getMobilePhone();
		   }	
		}
		return cellPhone;
	}
	
	
}
