package net.ytoec.kernel.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import net.ytoec.kernel.alipay.services.AlipayService;
import net.ytoec.kernel.alipay.util.AlipayNotify;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dao.AccountUserDao;
import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dao.LogsDao;
import net.ytoec.kernel.dao.PaymentDao;
import net.ytoec.kernel.dao.ServiceHistoryDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.AlipayConfigMessage;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.techcenter.api.SMSSender;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易明细表的service
 * 
 * @author guoliang.wang
 */
@Service
@Transactional
public class PaymentServiceImpl<T extends Payment> implements PaymentService<T> {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	private Properties properties = SMSSender.getPropertiesInstance();
	@Inject
	private PaymentDao<Payment> paymentDao;

	/*
	 * 账户信息
	 */
	@Inject
	private AccountUserDao<AccountUser> accountUserDao;

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
	private UserDao<User> userDao;
	
	@Inject
	 private SMSObjectService<SMSObject> SMSObjectService;
	/*
	 * 日志
	 */
	@Inject
	private LogsDao<Logs> logsDao;

	@Override
	public boolean add(T entity) {
		return paymentDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		return paymentDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return paymentDao.remove(entity);
	}

	@Override
	@SuppressWarnings("all")
	public T get(T entity) {
		return (T) paymentDao.get(entity);
	}

	@Override
	@SuppressWarnings("all")
	public List<T> getPaymentList(Map map, Pagination pagination, boolean flag) {
		List<T> list = null;
		if (flag) {
			map.put("startIndex", pagination.getStartIndex()); // 设置分页参数
			map.put("pageNum", pagination.getPageNum());
		} else {
			// 去掉分页参数
			if (map.containsKey("startIndex"))
				map.remove("startIndex");
			if (map.containsKey("pageNum"))
				map.remove("pageNum");
		}
		list = (List<T>) paymentDao.getPaymentList(map);
		
		return list;
	}

	@Override
	public boolean updateRemarkById(T payment) {
		return paymentDao.updateRemarkById(payment);
	}

	@Override
	public boolean updateDeleteFlagById(T payment) {
		return paymentDao.updateDeleteFlagById(payment);
	}

	/**注：充值 与 付款 (余额不足情况) 调用此方法[支付宝的账号信息传过来]
	 * 充值(:map里面需传payment对象的值过来)付款(：map里面要传三个对象过来)
	 * 对订单进行账户充值 第一步调用add方法生成一条订单
	 *  第二步调支付宝接口
	 * (充值成功后支付宝返回后) 第三步修改账户余额
	 * @param map 里面存三个对象([payment 、 dredgeService、serviceHistory]alipayConfigMessage)
	 * 对象：map.put("payment",Payment) map.put("dredgeService",DredgeService)
	 * map.put("alipayConfigMessage",AlipayConfigMessage) AlipayConfigMessage是支付宝账号的配置信息
	 * Payment 对象的值(userId、auserId、dealMoney、dealType、dealName)这些值必须传
	 * dredgeService、serviceHistory 尽量多传值
	 * dredgeService([第一种情况数据库存在把整条记录传入][第二种情况把界面输入的值传入其中const费用为界面服务的费用])尽量多传值
	 * @return 生成请求URL
	 */
	@Override
	@SuppressWarnings("all")
	public String dealPayment(Map<String, Object> map){
		if (map.get("payment") == null) {
			logger.error("充值付款传来的对象为null!");
			return null;
		}
		AlipayConfigMessage alipayConfigMessage = null;
		if(map.get("alipayConfigMessage") != null){
			alipayConfigMessage = (AlipayConfigMessage) map.get("alipayConfigMessage");
			logger.error("传过来的支付宝的账号信息为："+alipayConfigMessage.getAlipaySellerEmail());
		}
		Payment payment = (Payment) map.get("payment");		
		
		Payment trade_payment=null;
	
		//1)判断订单是否存在
		if(payment.getId() == null){
			//1.1)不存在时生成订单
			logger.error("====================payment.getId()为空=================================");
			payment.setUserId(payment.getUserId());
			payment.setDealTime(new Date());
			payment.setDealStatus(PayEnumConstants.DEALSTATUS.WAIT.getValue());
			payment.setDelFlag(PayEnumConstants.DEAL_FLAG.NDEL.value);
			try {
				paymentDao.add(payment);
			} catch (Exception e) {
				// 记日志
				Logs log = new Logs();
				log.setOperName("充值插入支付表时出错");
				log.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
				log.setUserId(payment.getUserId());
				log.setRemark("服务记录被更改");
				logsDao.add(log);
				logger.error("充值插入支付表时出错");
			}
			// 第二步把生成的订单信息查询出来
			 System.out.print(payment.getId());
			 trade_payment = payment;
		}else{//1.2)订单存在
			logger.error("====================payment.getId()不为空================================="+payment.getId());
		    trade_payment = paymentDao.get(payment);
		}
		
		//2)是否是在线充值
		if (!StringUtils.equals(PayEnumConstants.DEALTYPE.ONLINE.getValue(),
				payment.getDealType())) {
			
			// 不是在线充值[执行业务操作]开始
			  boolean bool=isNOnlinePay(map,trade_payment);//业务处理
			  if (BooleanUtils.isFalse(bool)) {
				  Log.error("不是在线充值[执行业务操作]失败[严重请检查]");
				  return null;
			  }
			//不是在线充值[执行业务操作]结束		
		   }
			//3) 第三步调用支付宝 (生成请求URL)
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("payment_type", "1");
		//String out_trade_no="ytoxl0";//订单号全网站唯一
		/*DateFormat df_time=new SimpleDateFormat("yyyyMMddHHmmss");
		String out_trade_no=df_time.format(new Date());*/
		/*if(trade_payment.getId() != null){
			sParaTemp.put("out_trade_no", trade_payment.getId().toString());
		}else{
			sParaTemp.put("out_trade_no", out_trade_no+trade_payment.getId().toString());
		}*/
		sParaTemp.put("out_trade_no", trade_payment.getId().toString());
		sParaTemp.put("subject", trade_payment.getDealName().trim());
		//sParaTemp.put("body", trade_payment.getUserId().toString());//备注里面暂时放userId
		if ((map.get("dredgeService") != null)) {
			DredgeService dredgeService = (DredgeService) map
					.get("dredgeService");
			sParaTemp.put("body", dredgeService.getServiceId().toString().trim());// 开通充值服务表-ec_core_dredgeService(周期也好放进去)
																			// 有相应的记录,返回时根据此记录到开通服务表中拿
		}
		DecimalFormat df = new DecimalFormat("#0.00"); 
		sParaTemp.put("total_fee", df.format(trade_payment.getDealMoney()));
		sParaTemp.put("show_url", null);
		sParaTemp.put("paymethod", "");
		sParaTemp.put("defaultbank", "");
		sParaTemp.put("anti_phishing_key", "");
		sParaTemp.put("exter_invoke_ip", "");// 待值
		sParaTemp.put("extra_common_param", "");
		sParaTemp.put("buyer_email", "");
		sParaTemp.put("royalty_parameters", "");
		sParaTemp.put("userId", trade_payment.getUserId() + "");
		String sHtmlText="";
		
		//4) 构造函数，生成请求URL
		try{
			sHtmlText =AlipayService.create_direct_pay_by_user(sParaTemp,alipayConfigMessage);
			logger.error("调用支付宝的路径: "+sHtmlText);
		}catch(Exception e){
			logger.error("生成支付宝URL出错!");
		}
		// 记日志
		Logs log = new Logs();
		log.setOperName("[调用支付宝]订单号"+trade_payment.getId().toString());
		log.setOperType(PayEnumConstants.OPERTYPE.ALIPAY.getValue());
		log.setUserId(payment.getUserId());
		log.setRemark("useId[paymentId]"+payment.getUserId()+"["+trade_payment.getId()+"]");
		logsDao.add(log);	
		//5) 返回URL(action 跳转)
		return sHtmlText;
	}

	@SuppressWarnings("all")
	@Override
	public List<T> getPaymentListByUserId(Map map) {

		return (List<T>) paymentDao.getPaymentListByUserId(map);
	}

	/**
	 * 自动续费的步骤：1）修改账户表-ec_core_accountUser 2）修改开通充值服务表-ec_core_dredgeService
	 * 3）修改交易（支付）明细表-ec_core_payment 插入一条记录 4)插入服务记录历史表-ec_core_serviceHistory
	 */
	@SuppressWarnings("all")
	@Override
	public Boolean autoPayment(ServiceHistory serviceHistory,DredgeService dredgeService) {
	
		// 1)修改账户表-ec_core_accountUser(修改前先查询余额是否足够)
		AccountUser accountUser = accountUserDao
				.getAccountUserByUserId(serviceHistory.getUserId());
		// 1.1)修改余额
		Map user_map = new HashMap();
		user_map.put("userId", accountUser.getUserId());
		user_map.put("money", serviceHistory.getPromCost());
		user_map.put("updateTime", accountUser.getUpdateTime());// 通过updateTime判定记录是否修改了
		Boolean bool = accountUserDao
					.updateAccountUserBalanceLowerByUserId(user_map);		

		if (BooleanUtils.isFalse(bool)) {
			//[自动续费失败时是否再重新续费一次,看后期日志后再决定]
			// 记日志
			Logs shistotrylogs = new Logs();
			shistotrylogs.setOperName("自动续费账号扣钱失败[检查严重]");
			shistotrylogs.setOperType(PayEnumConstants.OPERTYPE.AUTOFAIL
					.getValue());
			shistotrylogs.setUserId(serviceHistory.getUserId());
			shistotrylogs.setRemark("服务记录[时间不同步],钱为:"+ serviceHistory.getPromCost()+"serviceId:"+serviceHistory.getDserviceId());
			logsDao.add(shistotrylogs);
			Log.error("定时器修改账户表报异常[估计时间更改了]userId:"+ accountUser.getUserId());
			return false;
		} else {
			// 2）修改开通充值服务表-ec_core_dredgeService
			// 先查下 后修改，根据updateTime
			if (dredgeService !=null) {
				//DredgeService dredgeService = new DredgeService();
				dredgeService.setUserId(serviceHistory.getUserId());
				//dredgeService.setServiceId(serviceHistory.getDserviceId());// 系统在初始化服务的时候，服务表里面的数据。5代表短信服务
				dredgeService.setCost(serviceHistory.getPromCost()+dredgeService.getCost());
				dredgeService
						.setRemindFlag(PayEnumConstants.REMIND_FLAG.ISNREMIND.value);
				/**
				 * 返回日期的月份的天数 周期后的日期
				 */
				serviceHistory.getBeginDate();// 修改到期日期
				int month = 1;
				if (StringUtils.equals("0", serviceHistory.getCircle())) {
					month = 1;
				} else if (StringUtils.equals("1", serviceHistory.getCircle())) {
					month = 3;
				} else if (StringUtils.equals("2", serviceHistory.getCircle())) {
					month = 6;
				} else if (StringUtils.equals("3", serviceHistory.getCircle())) {
					month = 12+3;
				}
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.MONTH, month);
				dredgeService.setEndDate(roundToDay(gc.getTime()));// 周期后的天数
				/*Boolean dredge_bool = dredgeServiceDao
							.updateDredgeserviceBySMS(dredgeService);*/
				Boolean dredge_bool = dredgeServiceDao.edit(dredgeService);
				if (BooleanUtils.isFalse(dredge_bool)) {
					//[自动续费失败时是否再重新续费一次,看后期日志后再决定]
					// 记日志
					Logs shistotrylogs = new Logs();
					shistotrylogs.setOperName("定时器修改[开通充值服务表]失败");
					shistotrylogs.setOperType(PayEnumConstants.OPERTYPE.AUTOFAIL
							.getValue());
					shistotrylogs.setUserId(serviceHistory.getUserId());
					shistotrylogs.setRemark("请检查数据[严重]dseriveId:"+serviceHistory.getDserviceId());
					logsDao.add(shistotrylogs);
					logger.error("定时器修改修改开通充值服务表失败!updateTime的时间不一致");
					return false;
				}else{
					//自动续费成功后执行其他的业务[操作]
					// 3)插入交易（支付）明细表-ec_core_payment
					Payment payment = new Payment();
					payment.setUserId(serviceHistory.getUserId());
					payment.setAuserId(accountUser.getId());
					payment.setDealMoney(serviceHistory.getPromCost());
					//payment.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());//续费服务
					if(serviceHistory.getDealName().indexOf("时效提醒")>0){
						payment.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());
					}
					else if(serviceHistory.getDealName().indexOf("购买短信")>0){
						payment.setDealType(PayEnumConstants.DEALTYPE.SMS.getValue());
					}
					else {
						payment.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());//续费服务
					}
					
					payment.setDealName(serviceHistory.getDealName());
					payment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS
							.getValue());
					payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);
					payment.setDelFlag(PayEnumConstants.DEAL_FLAG.NDEL.value);
					payment.setRemark("自动续费插入");
					paymentDao.add(payment);					
					// 4）插入服务记录历史表-ec_core_serviceHistory
					ServiceHistory insert_serviceHistory = new ServiceHistory();
					insert_serviceHistory.setUserId(serviceHistory.getUserId());
					insert_serviceHistory.setDserviceId(serviceHistory.getDserviceId());
					//serviceHistory.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());//续费服务
					if(serviceHistory.getDealName().indexOf("时效提醒")>0){
						insert_serviceHistory.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());
					}
					else if(serviceHistory.getDealName().indexOf("购买短信")>0){
						insert_serviceHistory.setDealType(PayEnumConstants.DEALTYPE.SMS.getValue());
					}
					else {
						insert_serviceHistory.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());//续费服务
					}
					
					insert_serviceHistory.setDealName(serviceHistory.getDealName());
					insert_serviceHistory.setSmsCount(serviceHistory.getSmsCount());
					insert_serviceHistory.setCost(serviceHistory.getCost());
					insert_serviceHistory.setPromCost(serviceHistory.getPromCost());
					insert_serviceHistory.setBeginDate(new Date());
					insert_serviceHistory
							.setAutoRenew(PayEnumConstants.AUTO_FLAG.ISNAUTO.value);
					insert_serviceHistory.setCircle(serviceHistory.getCircle());

					/**
					 * 返回日期的月份的天数 周期后的日期
					 */					
					insert_serviceHistory.setEndDate(roundToDay(gc.getTime()));// 周期后的天数
					insert_serviceHistory.setRemark("自动续费");
					try {
						serviceHistoryDao.add(insert_serviceHistory);
					} catch (Exception e) {
						Log.error("自动续费插入服务记录历史表报异常userid:"+serviceHistory.getUserId()
								+"dserviceId"+serviceHistory.getDserviceId());
					}
				}
			}

			

			// 记日志
			Logs logs = new Logs();
			logs.setOperName("自动续费操作");
			logs.setOperType(PayEnumConstants.OPERTYPE.AUTOFEE
					.getValue());
			logs.setUserId(serviceHistory.getUserId());
			logs.setRemark("自动续费改账号表修改开通充值服务表与插入服务历史表,dserviceId:"
					+ serviceHistory.getDserviceId()+ "价格："
					+ serviceHistory.getCost());
			logsDao.add(logs);
             
			//[服务提醒不再放到这里,交易、余额提醒放到公共的方法里面]			
			return true;
		}
	}

	/**
	 * 自动续费 批量修改交易状态为：等待付款（交易（支付）明细表-ec_core_payment）
	 */
	@Override
	@SuppressWarnings("all")
	public boolean updateBatchByDealStatus(Map map) {
		return paymentDao.updateBatchByDealStatus(map);
	}

	public static Date roundToDay(Date date) {
		date = roundToHour(date);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		return gc.getTime();
	}

	public static Date roundToHour(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	/*
	 * 交易提醒 余额不足提醒
	 */
	@Override
	@SuppressWarnings("all")
	public boolean sendSMSByUserId(Integer userId,String serviceName,DredgeService dredgeService, String isSend) {
		logger.error("调用免费发送开始==============userId:"+userId+"serviceName:"+serviceName+"dredgeService:"+dredgeService.getEndDate());
		serviceName=serviceName.replaceAll("开通", "");
		serviceName=serviceName.replaceAll("续费", "");
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(userId);
		String cellPhone = getCellPhone(accountUser);
		// 1.1)是否开通了交易提醒
		Map less_map = new HashMap();
		less_map.put("userId", userId);
		less_map.put("serviceId", PayEnumConstants.SERVICE.TRANSACTION.getValue());// 交易提醒
		less_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> transaction_dredgeServiceList = dredgeServiceDao
				.getServiceRecordByUserId(less_map);
		if (CollectionUtils.isNotEmpty(transaction_dredgeServiceList)&&(cellPhone!=null&&cellPhone.length()>0)) {
			// 发送短信 [交易提醒]
			SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日");	
			String temp ="";
			if(dredgeService.getEndDate()!=null)
			     temp = "，服务截至时间为"+sf.format(dredgeService.getEndDate())+"。";
			
			String smsContent="亲，你已在易通上成功订购开通["+serviceName+"]"+temp+"【易通】";
			
			//发送短信
			SMSObject sMSObject=new SMSObject();
			sMSObject.setMessageContent(smsContent);
			sMSObject.setUserId(accountUser.getUserId());
			//sMSObject.setDestMobile(accountUser.getCellPhone());
			sMSObject.setDestMobile(cellPhone);
			sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
			sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.TRANSACTION.getValue()));
		    SMSObjectService.add(sMSObject);
		}

		// 1.2)判断用户是否开启了余额不足提醒
		Map dservice_map = new HashMap();
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.BALANCELESS.getValue());// 系统在初始化服务的时候，服务表里面的数据。5代表短信服务
		dservice_map.put("flag",
				PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> dredgeServiceList = dredgeServiceDao
				.getServiceRecordByUserId(dservice_map);
		if (CollectionUtils.isNotEmpty(dredgeServiceList)) {
			DredgeService remind_dredgeService = dredgeServiceList.get(0);
			// 1.3)开启了余额不足提醒
			if ((accountUser.getUseBalance()) < remind_dredgeService.getBalance() && !"isSend".equals(isSend)) {
				if ((cellPhone!=null&&cellPhone.length()>0)) {
					
					logger.error("调用免费发送开始==============userId:"+userId+"serviceName:"+serviceName+"remind_dredgeService.getBalance():"+remind_dredgeService.getBalance());
					SimpleDateFormat sf=new SimpleDateFormat("yyyy'年'MM'月'dd'日' HH'时'");	
					DecimalFormat df = new DecimalFormat("#0.00"); 
					String smsContent="亲，截止"+sf.format(new Date())+"，你的易通账户余额不足"+df.format(remind_dredgeService.getBalance())+"元，为不影响你的使用，请及时充值。【易通】";
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
		//1.3)判断用户是否开启了短信不足提醒		
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
			if ((sms.getSmsUsecount()- smsLess.getSmsLess())==0) {
				if(cellPhone!=null&&cellPhone.length()>0) {
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

		return true;
	}

	/*
	 * 注：支付返回 调用易通 params 的参数包括： trade_no（支付宝交易号） out_trade_no（获取订单号）说明此订单号
	 * 与交易（支付）明细表 的id 对应 total_fee（获取总金额） subject（商品名称、订单名称） body（商品描述、订单备注、描述）
	 * buyer_email（买家支付宝账号） trade_status（交易状态）	
	 */
	@Override
	@SuppressWarnings("all")
	public boolean alipayReturn(Map<String,String> params){		
		boolean bool=AlipayNotify.verify(params);		
		if(bool){//验证成功	
			logger.error("===========================================");
			String trade_no = params.get("trade_no"); // 支付宝交易号
			String order_no = params.get("out_trade_no"); // 获取订单号
			String total_fee = params.get("total_fee"); // 获取总金额
			String subject = params.get("subject");// 商品名称、订单名称
			String body = params.get("body");// 商品描述、订单备注、描述(放值的时候存了dserviceId)
			String buyer_email = params.get("buyer_email"); // 买家支付宝账号
			String trade_status = params.get("trade_status"); // 交易状态
			Payment payment = new Payment();
			payment.setId(Integer.parseInt(order_no));		
			Payment alipment = paymentDao.get(payment);
			//0)判断是否已经处理过此记录
			 if(StringUtils.equals(alipment.getDealStatus(), PayEnumConstants.DEALSTATUS.SUCCESS
				.getValue())){
				 //如果已经处理,就直接返回(无需操作)
				    Logs logs = new Logs();
					logs.setOperName("(及时)支付宝返回已经处理" );
					logs.setOperType(PayEnumConstants.OPERTYPE.ALIPAY.getValue());
					logs.setUserId(alipment.getUserId());
					logs.setRemark("支付宝返回已经处理(无需在处理),支付宝交易号:" + trade_no + "订单号:"
							+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
							+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
							+ buyer_email);
					logsDao.add(logs);
				 return bool;
			 }
			 //1)判断记录是否已经关闭[]
			 if((StringUtils.equals(alipment.getDelFlag(), PayEnumConstants.DEAL_FLAG.ALERADYDEL.value))
						||(StringUtils.equals(alipment.getDelFlag(), PayEnumConstants.DEAL_FLAG.SYSTEMCLOSE.value))){
					//1)此条订单已经关闭
					//1.1)如果订单已经关闭(支付宝却已经扣钱到我公司账号)[修改不在处理上面的流程]
					Logs logs = new Logs();
					logs.setOperName("(及时)订单关闭");
					logs.setOperType(PayEnumConstants.OPERTYPE.CLOSE.getValue());
					logs.setUserId(alipment.getUserId());
					logs.setRemark("支付宝扣钱返回订单客户却已经关闭了[如果客户要退回钱可以检查]交易id:"+alipment.getId());
					logsDao.add(logs);
					
				}
			//2)步修改账户信息、交易明细记录modifyUserandPayment(params,alipment)方法
			if (BooleanUtils.isFalse(modifyUserandPayment(params,alipment))){			
				logger.error("支付宝修改账户信息、交易明细记录失败[请检查]订单号为：" + order_no);
				return false;
			}
			// 3)修改开通充值服务表-ec_core_dredgeService			
			if(!StringUtils.equals(alipment.getDealType(), PayEnumConstants.DEALTYPE.ONLINE.getValue())){
				
				//3.1)如果是在线充值则不会进入此方法
				bool=dealAlipay(params,alipment);//当不是在线充值时,执行此方法				
		       
				if (BooleanUtils.isFalse(bool)){					
						Logs logs = new Logs();
						logs.setOperName("(及时)业务处理失败");
						logs.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
						logs.setUserId(alipment.getUserId());
						logs.setRemark("支付宝调用,支付宝交易号:" + trade_no + "订单号:"
								+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
								+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
								+ buyer_email);
						try{
							logsDao.add(logs);
						}catch(Exception e){
							e.printStackTrace();
						}						
					    logger.error("支付宝返回[业务处理失败抛出异常],业务处理失败!");
					
				}
			}
			// 5)记下日志
			/*Logs logs = new Logs();
			logs.setOperName("[支付宝及时成功]");
			logs.setOperType(PayEnumConstants.OPERTYPE.ALIPAY.getValue());
			logs.setUserId(alipment.getUserId());
			logs.setRemark("支付宝返回,支付宝交易号:" + trade_no + "订单号:" + order_no
					+ "获取总金额:" + total_fee + "商品名称、订单名称:" + subject
					+ "商品描述、订单备注、描述:" + body + "买家支付宝账号:" + buyer_email);
			try{
				logsDao.add(logs);
			}catch(Exception e){
				logger.error("支付宝返回,支付宝交易号:" + trade_no + "订单号:" + order_no
						+ "获取总金额:" + total_fee + "商品名称、订单名称:" + subject
						+ "商品描述、订单备注、描述:" + body + "买家支付宝账号:" + buyer_email);
				e.printStackTrace();
			}*/
			// 6)提醒[放到相应的位置]
			return bool;
		}
		//记下错误日志
		Logs logs = new Logs();
		logs.setOperName("支付宝验证失败[及时]");
		logs.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
		logs.setUserId(02);
		logs.setRemark("userId为02代表支付宝[及时到账验证失败]"+params.get("out_trade_no"));
		logsDao.add(logs);
		logger.error("验证失败");
		return bool;
	}

	/**
	 * 注：支付返回 调用易通(异步) params 的参数包括： trade_no（支付宝交易号） out_trade_no（获取订单号）说明此订单号
	 * 与交易（支付）明细表 的id 对应 total_fee（获取总金额） subject（商品名称、订单名称） body（商品描述、订单备注、描述）
	 * buyer_email（买家支付宝账号） trade_status（交易状态）
	 */
	@Override
	@SuppressWarnings("all")
	public boolean alipayNotify(Map<String, String> params) {
		boolean ver_bool=AlipayNotify.verify(params);
		if(ver_bool){//验证成功
			logger.error("===========================================");
			String trade_no = params.get("trade_no"); // 支付宝交易号
			String order_no = params.get("out_trade_no"); // 获取订单号
			String total_fee = params.get("total_fee"); // 获取总金额
			String subject = params.get("subject");// 商品名称、订单名称
			String body = params.get("body");// 商品描述、订单备注、描述
			String buyer_email = params.get("buyer_email"); // 买家支付宝账号
			String trade_status = params.get("trade_status"); // 交易状态
			Payment payment = new Payment();
			payment.setId(Integer.parseInt(order_no));
			Payment alipment = paymentDao.get(payment);
		
		//0)判断是否已经处理过此记录
		 if(StringUtils.equals(alipment.getDealStatus(), PayEnumConstants.DEALSTATUS.SUCCESS.getValue())){
			 //如果已经处理,就直接返回(无需操作)
			    Logs logs = new Logs();
				logs.setOperName("(异步)支付宝返回已经处理" );
				logs.setOperType(PayEnumConstants.OPERTYPE.ALIPAY.getValue());
				logs.setUserId(alipment.getUserId());
				logs.setRemark("支付宝异步返回已经处理(无需在处理),支付宝交易号:" + trade_no + "订单号:"
						+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
						+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
						+ buyer_email);
				logsDao.add(logs);
				return ver_bool;
			 }else{
			 //还没有处理.处理此单子begin==============
				 //1)判断记录是否已经关闭[]
				 if((StringUtils.equals(alipment.getDelFlag(), PayEnumConstants.DEAL_FLAG.ALERADYDEL.value))
							||(StringUtils.equals(alipment.getDelFlag(), PayEnumConstants.DEAL_FLAG.SYSTEMCLOSE.value))){
						//1)此条订单已经关闭
						//1.1)如果订单已经关闭(支付宝却已经扣钱到我公司账号)[修改不在处理上面的流程]
						Logs logs = new Logs();
						logs.setOperName("(异步)订单关闭");
						logs.setOperType(PayEnumConstants.OPERTYPE.CLOSE.getValue());
						logs.setUserId(alipment.getUserId());
						logs.setRemark("支付宝扣钱返回订单客户却已经关闭了异步[如果客户要退回钱可以检查]交易id:"+alipment.getId());
						logsDao.add(logs);
						
					}
				//1)2)步修改账户信息、交易明细记录modifyUserandPayment(params,alipment)方法
				if (BooleanUtils.isFalse(modifyUserandPayment(params,alipment))){
					logger.error("支付宝修改账户信息、交易明细记录失败[请检查]订单号为：" + order_no);		
					return false;
					
				}
				// 3)修改开通充值服务表-ec_core_dredgeService			
				if(!StringUtils.equals(alipment.getDealType(), PayEnumConstants.DEALTYPE.ONLINE.getValue())){
					//3.1)如果是在线充值则不会进入此方法
					dealAlipay(params,alipment);//当不是在线充值时,执行此方法
				}
				// 5)记下日志
				/*Logs logs = new Logs();
				logs.setOperName("[支付宝异步成功]");
				logs.setOperType(PayEnumConstants.OPERTYPE.ALIPAY.getValue());
				logs.setUserId(alipment.getUserId());
				logs.setRemark("支付宝返回,支付宝交易号:" + trade_no + "订单号:" + order_no
						+ "获取总金额:" + total_fee + "商品名称、订单名称:" + subject
						+ "商品描述、订单备注、描述:" + body + "买家支付宝账号:" + buyer_email);
				logsDao.add(logs);*/

				// 6)提醒
				return ver_bool;
			 
			 //处理此单子end=============================
		  }		
			
		}
		//记下错误日志
		Logs logs = new Logs();
		logs.setOperName("支付宝验证失败[异步]");
		logs.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
		logs.setUserId(02);
		logs.setRemark("userId为02代表支付宝[异步到账验证失败]");
		logsDao.add(logs);
		logger.error("验证失败"+ver_bool);
		return ver_bool;
	}
	
	
	@SuppressWarnings("all")
	//修改账号表已经交易明细表
	public boolean modifyUserandPayment(Map<String, String> params,Payment alipment) {
		boolean bool=true;
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String order_no = params.get("out_trade_no"); // 获取订单号
		String total_fee = params.get("total_fee"); // 获取总金额
		String subject = params.get("subject");// 商品名称、订单名称
		String body = params.get("body");// 商品描述、订单备注、描述(放值的时候存了dserviceId)
		String buyer_email = params.get("buyer_email"); // 买家支付宝账号
		String trade_status = params.get("trade_status"); // 交易状态
		
		
		// 1)修改 账户表-ec_core_accountUser
		AccountUser accountUser =accountUserDao
				.getAccountUserByUserId(alipment.getUserId());
		double moneyVal=accountUser.getUseBalance();
		if (accountUser != null ) {
			Map map = new HashMap();
			map.put("userId", accountUser.getUserId());
			map.put("updateTime", accountUser.getUpdateTime());
			map.put("money", total_fee);
			map.put("payAccount", buyer_email);
			Boolean update_bool = true;
			update_bool = accountUserDao.updateAccountUserBalanceAddByUserId(map);			
			if (BooleanUtils.isFalse(update_bool)) {	
				Logs logs = new Logs();
				logs.setOperName("交易失败");
				logs.setOperType(PayEnumConstants.OPERTYPE.DEALFAIL.getValue());
				logs.setUserId(alipment.getUserId());
				logs.setRemark("支付宝交易号:" + trade_no + "订单号:"
						+ order_no + "交易金额:" + total_fee+"交易前的金额:"+moneyVal );
				logsDao.add(logs);
				return false;
			}else{
				    Logs logs = new Logs();
					logs.setOperName("交易成功");
					logs.setOperType(PayEnumConstants.OPERTYPE.DEALSUCCESS.getValue());
					logs.setUserId(alipment.getUserId());
					logs.setRemark("支付宝交易号:" + trade_no + "订单号:"
							+ order_no + "交易金额:" + total_fee+"交易前的金额:"+moneyVal );
					logsDao.add(logs);
			}
		}
		
		// 2)修改 交易（支付）明细表-ec_core_payment（交易状态）
	
		accountUser = accountUserDao.getAccountUserByUserId(alipment.getUserId());
		alipment.setPayTime(new Date());
		alipment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());		
		paymentDao.edit(alipment);
		// 1.1)是否开通了交易提醒
		Map less_map = new HashMap();
		less_map.put("userId", alipment.getUserId());
		less_map.put("serviceId", PayEnumConstants.SERVICE.TRANSACTION.getValue());// 交易提醒
		less_map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> transaction_dredgeServiceList = dredgeServiceDao
				.getServiceRecordByUserId(less_map);
		if (CollectionUtils.isNotEmpty(transaction_dredgeServiceList)&&((accountUser.getCellPhone() !=null)&& (accountUser.getCellPhone() !=""))) {
			// 发送短信 [交易提醒]
			if(StringUtils.equals(alipment.getDealType(), PayEnumConstants.DEALTYPE.ONLINE.getValue())){
				//在线充值发送短信
				DecimalFormat df = new DecimalFormat("#0.00"); 			
				String smsContent="亲，你已成功充值"+alipment.getDealMoney()+"元，账户余额为"+df.format(accountUser.getUseBalance())+"元。【易通】";
				
				//发送短信
				SMSObject sMSObject=new SMSObject();
				sMSObject.setUserId(accountUser.getUserId());
				sMSObject.setMessageContent(smsContent);
				sMSObject.setDestMobile(accountUser.getCellPhone());
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
				sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.TRANSACTION.getValue()));
				SMSObjectService.add(sMSObject);
			}
			
		}
		return bool;
	}
	
	@SuppressWarnings("all")
	//支付宝返回处理
	public boolean dealAlipay(Map<String, String> params,Payment alipment) {
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String order_no = params.get("out_trade_no"); // 获取订单号
		String total_fee = params.get("total_fee"); // 获取总金额
		String subject = params.get("subject");// 商品名称、订单名称
		String body = params.get("body");// 商品描述、订单备注、描述(放值的时候存了dserviceId)
		String buyer_email = params.get("buyer_email"); // 买家支付宝账号
		String trade_status = params.get("trade_status"); // 交易状态
		boolean deal_bool=true;
		    //3.0)如果记录是在线充值则不会进入(根据alipment 存入是的状态)
			Map history_map = new HashMap();
			history_map.put("userId", alipment.getUserId());
			history_map.put("payId", alipment.getId());
			//serviceId 后面通过 服务历史记录表来查询[DredgeService]
			List<ServiceHistory> serviceHistoryList=serviceHistoryDao.
			                           getServiceHistoryListByDUserId(history_map);
			Integer dSeriveId=0;
			if(CollectionUtils.isNotEmpty(serviceHistoryList)){
				dSeriveId=serviceHistoryList.get(0).getDserviceId();
			}else{
				dSeriveId=Integer.parseInt(body);
			}
			DredgeService dredgeService=new DredgeService();
			dredgeService.setId(dSeriveId);
			DredgeService businessDredgeService = dredgeServiceDao.get(dredgeService);
		
			if (businessDredgeService !=null) {
				
				//3.0.1)开通服务表中存在记录[处理业务]
				deal_bool=dealAlipayBusiness(params,businessDredgeService,alipment);//业务处理
			}else{
				//3.0.2)开通服务表中不存在记录[缺少记录说明数据不正确]
				Logs logs = new Logs();
				logs.setOperName(order_no);
				logs.setOperType(PayEnumConstants.OPERTYPE.ERROR
						.getValue());
				logs.setUserId(alipment.getUserId());
				logs.setRemark("支付宝返回处理失败,开通服务表中不存在记录[严重错误]");
				logsDao.add(logs);
				logger.error("支付宝返回处理失败,开通服务表中不存在记录[严重错误],订单号:" + order_no);
				deal_bool=false;
			}		      
		return deal_bool;
	}
	/*
	 * 支付宝返回处理业务
	 * 1)修改服务历史表的时间
	 * 2)修改开通服务表
	 * 3)修改账户表
	 */
	@SuppressWarnings("all")
	public boolean dealAlipayBusiness(Map<String, String> params,DredgeService businessDredgeService,Payment alipment){
		//==================================begin==================
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String order_no = params.get("out_trade_no"); // 获取订单号
		String total_fee = params.get("total_fee"); // 获取总金额
		String subject = params.get("subject");// 商品名称、订单名称
		String body = params.get("body");// 商品描述、订单备注、描述
		String buyer_email = params.get("buyer_email"); // 买家支付宝账号
		String trade_status = params.get("trade_status"); // 交易状态
		boolean deal_bool=true;
		if(!((StringUtils.equals(alipment.getDelFlag(), PayEnumConstants.DEAL_FLAG.ALERADYDEL.value))
				||(StringUtils.equals(alipment.getDelFlag(), PayEnumConstants.DEAL_FLAG.SYSTEMCLOSE.value)))){
			//2)订单没有关闭
			deal_bool=isNClosedPayment(params,businessDredgeService,alipment);
		}
			//========================end===========================	
		return deal_bool;
	}
	
	
	@SuppressWarnings("all")
	public boolean isNOnlinePay(Map<String, Object> map,Payment payment){
		boolean bool=true;
		//===================begin=================
		if(map.get("dredgeService") == null  ||
				map.get("serviceHistory") == null){
			Log.error("不是在线充值传过来的对象为null");
			return false;
		}
		DredgeService dredgeService = (DredgeService) map.get("dredgeService");
		ServiceHistory serviceHistory = (ServiceHistory) map.get("serviceHistory");
		// 1)查询开通服务表是否存在记录
		// 1.1)不存在往服务历史记录表插入一条记录
		if (dredgeService.getId() == null) {
			dredgeService.setFlag(PayEnumConstants.SERVICEFLAG.CREATE
					.getValue());
			dredgeServiceDao.add(dredgeService);
			
		}//[不再引入临时状态]
		
		//2)服务记录历史表-ec_core_serviceHistory		
		//2.1)判断服务历史记录表中是否存在
		Map history_map = new HashMap();
		history_map.put("userId", payment.getUserId());
		history_map.put("dserviceId", dredgeService.getId());
		history_map.put("payId", payment.getId());
		List<ServiceHistory> serviceHistoryList =
			serviceHistoryDao.getServiceHistoryListByDUserId(history_map);
		//设值
		serviceHistory.setDserviceId( dredgeService.getId());
		serviceHistory.setPayId(payment.getId());
		
		if (CollectionUtils.isEmpty(serviceHistoryList)) {
		  serviceHistoryDao.add(serviceHistory);			
		}
		//3)修改可用余额ec_core_accountUser
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(payment.getUserId());		
		if (accountUser != null) {
			Map account_map = new HashMap();
			account_map.put("userId", accountUser.getUserId());
			account_map.put("updateTime", accountUser.getUpdateTime());
			if(accountUser.getUseBalance()>serviceHistory.getPromCost()){
				//3.1)如果账户充了钱
				account_map.put("money", (accountUser.getUseBalance()-(serviceHistory.getPromCost()-payment.getDealMoney())));
			}else{
				//3.2)跳转支付宝付款[一般情况下可用余额为0]
				account_map.put("money", 0);
			}
			
			Boolean update_bool = false;
			update_bool = accountUserDao.updateUseBalanceByUserId(account_map);			
			if (BooleanUtils.isFalse(update_bool)) {
				Logs logs = new Logs();
				logs.setOperName("修改可用余额失败");
				logs.setOperType(PayEnumConstants.OPERTYPE.MUSTCHECK.getValue());
				logs.setUserId(accountUser.getUserId());
				logs.setRemark("跳转支付宝前修改可用余额失败[请检查]交易id:"+payment.getId());
				logsDao.add(logs);
				bool=false;
			}
		}
		//===================end===================
		
		return bool;
	}
	
	@SuppressWarnings("all")
	public boolean isNClosedPayment(Map<String, String> params,DredgeService businessDredgeService,Payment alipment){
		//1)没有人工、系统关闭begin
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String order_no = params.get("out_trade_no"); // 获取订单号
		String total_fee = params.get("total_fee"); // 获取总金额
		String subject = params.get("subject");// 商品名称、订单名称
		String body = params.get("body");// 商品描述、订单备注、描述
		String buyer_email = params.get("buyer_email"); // 买家支付宝账号
		String trade_status = params.get("trade_status"); // 交易状态
		boolean deal_bool=true;		
		if(StringUtils.equals(alipment.getDealType(), PayEnumConstants.DEALTYPE.SMS.getValue())){
			//1.1)短信服务
			
			// 2)修改开通服务表
			//1.0)查询
			Map historyMap=new HashMap();
			historyMap.put("userId", alipment.getUserId());
			historyMap.put("payId", alipment.getId());
			List<ServiceHistory> historyList=
				serviceHistoryDao.getServiceHistoryListByDUserId(historyMap);
			if(CollectionUtils.isNotEmpty(historyList)){
				//1.0.1)修改短信相关的信息
				ServiceHistory serviceHistory=historyList.get(0);
				if(StringUtils.equals(businessDredgeService.getFlag(),
						PayEnumConstants.SERVICEFLAG.ENABLED.getValue())){
					//2.1)如果是启动状态,说明此次操作为续费[服务还在使用中]
					
					//2.1.1)修改短信相关的信息
					businessDredgeService.setSmsUsecount(serviceHistory.getSmsCount()
							+businessDredgeService.getSmsUsecount());//可使用的短信数					
					businessDredgeService.setCost(businessDredgeService.getCost()+
							serviceHistory.getPromCost());//费用
				}else{
					
					//2.2)其它状态[价格为开通此次服务的价格]				
					businessDredgeService.setSmsUsecount(serviceHistory.getSmsCount()
							+businessDredgeService.getSmsUsecount());//可使用的短信数					
					businessDredgeService.setCost(serviceHistory.getPromCost());//费用
				}
				//公共属性的设置
				businessDredgeService.setSmsAllcount(businessDredgeService.getSmsAllcount()+
						serviceHistory.getSmsCount());//累计购买短信量
				
					dredgeServiceDao.edit(businessDredgeService);
				   Log.error("支付宝返回修改开通充值服务表报异常支付宝调用订单号为：" + order_no);
				//[短信不需要休息服务历史记录]
				
				 //3)============================修改账户表begin==============
				AccountUser accountUser =  accountUserDao
				.getAccountUserByUserId(alipment.getUserId());
					if (accountUser !=null) {
						Map map = new HashMap();
						map.put("userId", accountUser.getUserId());
						map.put("updateTime", accountUser.getUpdateTime());
						map.put("money", accountUser.getUseBalance()-Double.parseDouble(total_fee));
						map.put("balance", accountUser.getBalance()-serviceHistory.getPromCost());
						map.put("allConsume", accountUser.getAllConsume()+serviceHistory.getPromCost());
						//map.put("allRecharge", accountUser.getAllRecharge()+Double.parseDouble(total_fee));
						Boolean update_bool = false;
						
							//修改余额(可用余额把以前锁定的余额退回来)
							update_bool = accountUserDao.updateUseBalanceByUserId(map);
						
						if (BooleanUtils.isFalse(update_bool)) {
							//出现这样的日志,必须去查看数据.检查
							Logs logs = new Logs();
							logs.setOperName(trade_no);
							logs.setOperType(PayEnumConstants.OPERTYPE.MUSTCHECK.getValue());
							logs.setUserId(alipment.getUserId());
							logs.setRemark("支付宝返回结果修改账户失败,支付宝交易号:" + trade_no + "订单号:"
									+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
									+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
									+ buyer_email);
							Log.error("支付宝返回修改余额(可用余额把以前锁定的余额退回来)失败用户：" + accountUser.getUserId()
									+ "订单号：" + order_no);							
							logsDao.add(logs);
							deal_bool=false;
						}
					}
				 //===================================修改账户表end==================
				
			}else{
				deal_bool=false;
				logger.error("短信服务,支付宝返回,服务历史记录表中不存在记录");
			}			
			
		}else{
			//1.2)其他服务
			int month = 1;		
			//1)修改服务历史表的时间(修改开始时间结束时间)
			
			//1.0)查询
			Map historyMap=new HashMap();
			historyMap.put("userId", alipment.getUserId());
			historyMap.put("payId", alipment.getId());
			List<ServiceHistory> historyList=
				serviceHistoryDao.getServiceHistoryListByDUserId(historyMap);
			if(CollectionUtils.isNotEmpty(historyList)){
				//=============业务处理开始begin==========================
				ServiceHistory serviceHistory=historyList.get(0);
				if (StringUtils.equals("0",businessDredgeService.getCircle())) {
					month = 1;
				} else if (StringUtils.equals("1", serviceHistory.getCircle())) {
					month = 3;
				} else if (StringUtils.equals("2", serviceHistory.getCircle())) {
					month = 6;
				} else if (StringUtils.equals("3", serviceHistory.getCircle())) {
					month = 12+3;
				}
				serviceHistory.setBeginDate(new Date());//开始时间算支付宝处理后的时间
				serviceHistory.setEndDate(DateUtil.getCireclDate(
						new Date(), month));// 周期后的天数
				
				//1.1)修改
					serviceHistoryDao.edit(serviceHistory);	
					logger.error("支付宝返回调用,修改服务历史记录表异常,订单号:" + order_no);
				
			   //2)修改开通服务表
				if(StringUtils.equals(businessDredgeService.getFlag(),
						PayEnumConstants.SERVICEFLAG.ENABLED.getValue())){
					//2.1)如果是启动状态,说明此次操作为续费[服务还在使用中]
					businessDredgeService.setEndDate(DateUtil.getCireclDate(
							businessDredgeService.getEndDate(), month));// 周期后的天数
					businessDredgeService.setCost(businessDredgeService.getCost()+
							serviceHistory.getPromCost());
					
				}else{
					//2.2)其它状态,服务开始时间为今天[价格为开通此次服务的价格]
					businessDredgeService.setBeginDate(new Date());
					businessDredgeService.setEndDate(DateUtil.getCireclDate(
							new Date(), month));// 周期后的天数
					businessDredgeService.setCost(serviceHistory.getPromCost());
					businessDredgeService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
					
				}
				//公共属性的设置
				businessDredgeService.setCircle(serviceHistory.getCircle());
				businessDredgeService.setAutoRenew(serviceHistory.getAutoRenew());
				businessDredgeService.setRemindFlag(PayEnumConstants.REMIND_FLAG.ISNREMIND.value);
				
				dredgeServiceDao.edit(businessDredgeService);
				Log.error("支付宝返回修改开通充值服务表报异常支付宝调用订单号为：" + order_no);
				
				 //3)============================修改账户表begin==============
				AccountUser accountUser =  accountUserDao
				.getAccountUserByUserId(alipment.getUserId());
					if (accountUser !=null) {
						Map map = new HashMap();
						map.put("userId", accountUser.getUserId());
						map.put("updateTime", accountUser.getUpdateTime());
						map.put("money", accountUser.getUseBalance()-Double.parseDouble(total_fee));
						map.put("balance", accountUser.getBalance()-serviceHistory.getPromCost());
						map.put("allConsume", accountUser.getAllConsume()+serviceHistory.getPromCost());
						Boolean update_bool = false;
						
						//修改余额(可用余额把以前锁定的余额退回来)
						update_bool = accountUserDao.updateUseBalanceByUserId(map);
						
						if (BooleanUtils.isFalse(update_bool)) {
							//出现这样的日志,必须去查看数据.检查
							Logs logs = new Logs();
							logs.setOperName(trade_no);
							logs.setOperType(PayEnumConstants.OPERTYPE.MUSTCHECK.getValue());
							logs.setUserId(alipment.getUserId());
							logs.setRemark("支付宝返回结果修改账户失败,支付宝交易号:" + trade_no + "订单号:"
									+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
									+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
									+ buyer_email);
							logsDao.add(logs);
							Log.error("支付宝返回修改账户,用户：" + accountUser.getUserId()
									+ "订单号：" + order_no);
							deal_bool=false;
						}else{
							//提醒
							 sendSMSByUserId(accountUser.getUserId(),alipment.getDealName(),businessDredgeService, "isSend");
						}
					}
				 //===================================修改账户表end==================
			}
		}
				return deal_bool;
		//没有人工、系统关闭end
	}

	@SuppressWarnings("all")
	public boolean isClosedPayment(Map<String, String> params,DredgeService businessDredgeService,Payment alipment){
		//1)没有人工、系统关闭begin
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String order_no = params.get("out_trade_no"); // 获取订单号
		String total_fee = params.get("total_fee"); // 获取总金额
		String subject = params.get("subject");// 商品名称、订单名称
		String body = params.get("body");// 商品描述、订单备注、描述
		String buyer_email = params.get("buyer_email"); // 买家支付宝账号
		String trade_status = params.get("trade_status"); // 交易状态
		boolean deal_bool=true;		
		if(StringUtils.equals(alipment.getDealType(), PayEnumConstants.DEALTYPE.SMS.getValue())){
			//1.1)短信服务
			
			// 2)修改开通服务表
			//1.0)查询
			Map historyMap=new HashMap();
			historyMap.put("userId", alipment.getUserId());
			historyMap.put("payId", alipment.getId());
			List<ServiceHistory> historyList=
				serviceHistoryDao.getServiceHistoryListByDUserId(historyMap);
			if(CollectionUtils.isEmpty(historyList)){
				//1.0.1)修改短信相关的信息
				ServiceHistory serviceHistory=historyList.get(0);
				if(StringUtils.equals(businessDredgeService.getFlag(),
						PayEnumConstants.SERVICEFLAG.ENABLED.getValue())){
					//2.1)如果是启动状态,说明此次操作为续费[服务还在使用中]
					
					//2.1.1)修改短信相关的信息
					businessDredgeService.setSmsUsecount(serviceHistory.getSmsCount()
							+businessDredgeService.getSmsUsecount());//可使用的短信数					
					businessDredgeService.setCost(businessDredgeService.getCost()+
							serviceHistory.getPromCost());//费用
				}else{
					
					//2.2)其它状态[价格为开通此次服务的价格]				
					businessDredgeService.setSmsUsecount(serviceHistory.getSmsCount());//可使用的短信数					
					businessDredgeService.setCost(serviceHistory.getPromCost());//费用
				}
				//公共属性的设置
				businessDredgeService.setSmsAllcount(businessDredgeService.getSmsAllcount()+
						businessDredgeService.getSmsUsecount());//累计购买短信量
				try{
					dredgeServiceDao.edit(businessDredgeService);
				}catch(Exception e){
					deal_bool=false;
					Log.error("支付宝返回修改开通充值服务表报异常支付宝调用订单号为：" + order_no);
					e.printStackTrace();
				}
				//[短信不需要休息服务历史记录]
				
				 //3)============================修改账户表begin==============
				AccountUser accountUser =  accountUserDao
				.getAccountUserByUserId(alipment.getUserId());
					if (accountUser !=null) {
						Map map = new HashMap();
						map.put("userId", accountUser.getUserId());
						map.put("updateTime", accountUser.getUpdateTime());
						map.put("money", accountUser.getUseBalance()-serviceHistory.getPromCost());
						map.put("balance", accountUser.getBalance()-serviceHistory.getPromCost());
						map.put("allConsume", accountUser.getAllConsume()+serviceHistory.getPromCost());
						Boolean update_bool = false;
						//修改余额(可用余额把以前锁定的余额退回来)
						update_bool = accountUserDao.updateUseBalanceByUserId(map);
						
				
						if (BooleanUtils.isFalse(update_bool)) {
							//出现这样的日志,必须去查看数据.检查
							Logs logs = new Logs();
							logs.setOperName(trade_no);
							logs.setOperType(PayEnumConstants.OPERTYPE.MUSTCHECK.getValue());
							logs.setUserId(alipment.getUserId());
							logs.setRemark("支付宝返回结果修改账户失败,支付宝交易号:" + trade_no + "订单号:"
									+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
									+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
									+ buyer_email);
							logsDao.add(logs);
							deal_bool=false;
						}
					}
				 //===================================修改账户表end==================
				
			}else{
				deal_bool=false;
				logger.error("短信服务,支付宝返回,服务历史记录表中不存在记录");
			}			
			
		}else{
			//1.2)其他服务
			int month = 1;		
			//1)修改服务历史表的时间(修改开始时间结束时间)
			
			//1.0)查询
			Map historyMap=new HashMap();
			historyMap.put("userId", alipment.getUserId());
			historyMap.put("payId", alipment.getId());
			List<ServiceHistory> historyList=
				serviceHistoryDao.getServiceHistoryListByDUserId(historyMap);
			if(CollectionUtils.isNotEmpty(historyList)){
				//=============业务处理开始begin==========================
				ServiceHistory serviceHistory=historyList.get(0);
				if (StringUtils.equals("0",businessDredgeService.getCircle())) {
					month = 1;
				} else if (StringUtils.equals("1", serviceHistory.getCircle())) {
					month = 3;
				} else if (StringUtils.equals("2", serviceHistory.getCircle())) {
					month = 6;
				} else if (StringUtils.equals("3", serviceHistory.getCircle())) {
					month = 12+3;
				}
				serviceHistory.setBeginDate(new Date());//开始时间算支付宝处理后的时间
				serviceHistory.setEndDate(DateUtil.getCireclDate(
						new Date(), month));// 周期后的天数
				try{
					//1.1)修改
					serviceHistoryDao.edit(serviceHistory);	
				}catch(Exception e){
					deal_bool=false;
					logger.error("支付宝返回调用,修改服务历史记录表异常,订单号:" + order_no);
					e.printStackTrace();
				}
			   //2)修改开通服务表
				if(StringUtils.equals(businessDredgeService.getFlag(),
						PayEnumConstants.SERVICEFLAG.ENABLED.getValue())){
					//2.1)如果是启动状态,说明此次操作为续费[服务还在使用中]
					businessDredgeService.setEndDate(DateUtil.getCireclDate(
							businessDredgeService.getEndDate(), month));// 周期后的天数
					businessDredgeService.setCost(businessDredgeService.getCost()+
							serviceHistory.getPromCost());
					
				}else{
					//2.2)其它状态,服务开始时间为今天[价格为开通此次服务的价格]
					businessDredgeService.setBeginDate(new Date());
					businessDredgeService.setEndDate(DateUtil.getCireclDate(
							new Date(), month));// 周期后的天数
					businessDredgeService.setCost(serviceHistory.getPromCost());
					businessDredgeService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
					
				}
				//公共属性的设置
				businessDredgeService.setCircle(serviceHistory.getCircle());
				businessDredgeService.setAutoRenew(serviceHistory.getAutoRenew());
				businessDredgeService.setRemindFlag(PayEnumConstants.REMIND_FLAG.ISNREMIND.value);
				try{
					dredgeServiceDao.edit(businessDredgeService);
				}catch(Exception e){
					deal_bool=false;
					Log.error("支付宝返回修改开通充值服务表报异常支付宝调用订单号为：" + order_no);
					e.printStackTrace();
				}
				 //3)============================修改账户表begin==============
				AccountUser accountUser =  accountUserDao
				.getAccountUserByUserId(alipment.getUserId());
					if (accountUser !=null) {
						Map map = new HashMap();
						map.put("userId", accountUser.getUserId());
						map.put("updateTime", accountUser.getUpdateTime());
						map.put("money", accountUser.getUseBalance()-serviceHistory.getPromCost());
						map.put("balance", accountUser.getBalance()-serviceHistory.getPromCost());
						map.put("allConsume", accountUser.getAllConsume()+serviceHistory.getPromCost());
						Boolean update_bool = false;
						try {
							//修改余额(可用余额把以前锁定的余额退回来)
							update_bool = accountUserDao.updateUseBalanceByUserId(map);
						} catch (Exception e) {
							Log.error("支付宝返回修改账户,用户：" + accountUser.getUserId()
									+ "订单号：" + order_no);
							deal_bool=false;
						}
				
						if (BooleanUtils.isFalse(update_bool)) {
							//出现这样的日志,必须去查看数据.检查
							Logs logs = new Logs();
							logs.setOperName(trade_no);
							logs.setOperType(PayEnumConstants.OPERTYPE.MUSTCHECK.getValue());
							logs.setUserId(alipment.getUserId());
							logs.setRemark("支付宝返回结果修改账户失败,支付宝交易号:" + trade_no + "订单号:"
									+ order_no + "获取总金额:" + total_fee + "商品名称、订单名称:"
									+ subject + "商品描述、订单备注、描述:" + body + "买家支付宝账号:"
									+ buyer_email);
							logsDao.add(logs);
							deal_bool=false;
						}
					}
				 //===================================修改账户表end==================
			}
		}
				return deal_bool;
		//没有人工、系统关闭end
	}
	
	/**
	 * 根据id降序排列
	 * @param map(userId,dealStatus,delFlag,dealName)
	 * @return
	 */
	@Override
	@SuppressWarnings("all")
	public List<T> getList(Map map) {
		return (List<T>) paymentDao.getList(map);
	}

	@Override
	public boolean updateDealMoneyById(Payment payment) {
		
		return paymentDao.updateDealMoneyById(payment);
	}

	@Override
	public Integer getPaymentListCount(Map map) {
		return paymentDao.getPaymentListCount(map);
	}
	
	
	/***
	 *  首先就是关闭这个订单，旧订单做flag=5逻辑删除
	 *  然后就是重新付款
	 *  这个时候需要更新账户表
	 *  插入交易明细表（一条新订单）
	 *  更新服务历史记录表(主要是更新交易明细id payId)
	 *  然后把订单传入到支付宝
	 *  作用：处理支付宝交易金额不一致的情况
	 *  
	 *  
	 *  例如：同一条订单第一次提交到支付宝的付款金额是50，
	 *        但是在交易明细支付页面第二次提交时的金额是30，
	 *        主要是由于在第一次提交订单之后又对账户进行在线充值20所导致的
	 */
	public Map<String, Object> add_Alipay(Payment payment,ServiceHistory serviceHistory,DredgeService dredgeService,AccountUser accountUser,Integer createrId)
	{
		 boolean flag=true;
		 Map<String, Object> payMap = new HashMap<String, Object>();		 
		 //1.关闭这个订单，旧订单做dealStatus=5逻辑删除
		 payment.setDealStatus(PayEnumConstants.DEALSTATUS.LOGDEL.getValue());
		 flag = paymentDao.edit(payment);
		 if(flag==false) {
			 logger.error("逻辑删除交易明细表id="+payment.getId()+"失败.....");
		 }
		 accountUser.setUseBalance(accountUser.getUseBalance()+(serviceHistory.getPromCost()-payment.getDealMoney()));
		 flag = accountUserDao.edit(accountUser);
		 if(flag==false) {
			 logger.error("逻辑删除将冻结的余额退回时，更新账户表id="+accountUser.getId()+"失败.....");
		 }
		 
		 
		 //2.添加一条新订单，插入交易明细表
		 Payment paymentNew= new Payment();
		 paymentNew.setUserId(payment.getUserId());// 得到用户id
		 paymentNew.setAuserId(payment.getAuserId());// 得到账户id
		 paymentNew.setDealMoney(serviceHistory.getPromCost()-accountUser.getUseBalance());  //交易额
		 paymentNew.setDealTime(payment.getDealTime());
		 paymentNew.setPayTime(payment.getPayTime());   //付款时间
		 paymentNew.setDealType(payment.getDealType()); //交易类型 1 订购服务
		 paymentNew.setDealName(payment.getDealName());
		 paymentNew.setDealStatus(PayEnumConstants.DEALSTATUS.WAIT.getValue());//交易状态  未支付
		 paymentNew.setFlag(payment.getFlag());            //状态
		 paymentNew.setDelFlag(payment.getDelFlag());
		 paymentNew.setReason(payment.getReason());
		 paymentNew.setRemark(payment.getRemark());
		 paymentNew.setCreaterId(createrId);
		 
		 flag = paymentDao.add(paymentNew);
		 if(flag==false) {
			 logger.error("添加一条新订单，插入交易明细表失败.....");
		 }
		 
		 if(accountUser.getUseBalance()<serviceHistory.getPromCost()) {
			 accountUser.setUseBalance(0.0);
			 flag = accountUserDao.edit(accountUser);
			 if(flag==false) {
				 logger.error("添加一条新订单时，更新账户表id="+accountUser.getId()+"失败.....");
			 }
		 }	 
		 ServiceHistory old = new ServiceHistory();
		 old.setPayId(payment.getId());
		 serviceHistory = serviceHistoryDao.get(old);
		 serviceHistory.setPayId(paymentNew.getId());		 
		 flag = serviceHistoryDao.edit(serviceHistory);
		 if(flag==false) {
			 logger.error("插入服务历史记录表id="+serviceHistory.getId()+"失败...");
		 }
		 
		 Payment paymentADD = new Payment();
		 paymentADD.setId(paymentNew.getId());
		 paymentADD = paymentDao.get(paymentADD);
		 
		 payMap.put("dredgeService", dredgeService);
		 payMap.put("payment", paymentADD);
		 payMap.put("serviceHistory", serviceHistory);
		 
		 return payMap;		 
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
	/**
	 * 判断余额是否充足
	 * @param userId
	 * @param serviceName
	 * @param dredgeService
	 * @return
	 */
	public Double sendMessageByUserId(Integer userId) {
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(userId);
		Map<String,Object> dservice_map = new HashMap<String,Object>();
		dservice_map.put("userId", userId);
		dservice_map.put("serviceId", PayEnumConstants.SERVICE.BALANCELESS.getValue());// 系统在初始化服务的时候，服务表里面的数据。5代表短信服务
		dservice_map.put("flag",PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> dredgeServiceList = dredgeServiceDao.getServiceRecordByUserId(dservice_map);
		Double result = null;
		if (CollectionUtils.isNotEmpty(dredgeServiceList)) {
			DredgeService remind_dredgeService = dredgeServiceList.get(0);
			if ((accountUser.getUseBalance()) < remind_dredgeService.getBalance()) {
				result = remind_dredgeService.getBalance();
			}
		}
       return result;
	}
}
