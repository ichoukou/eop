package net.ytoec.kernel.action.alipay;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.RechargeSMSFlagEnum;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.AlipayConfigMessage;
import net.ytoec.kernel.service.AccountUserService;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.LogsService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.ServiceHistoryService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.techcenter.Global;
import net.ytoec.kernel.techcenter.api.DeliverReceiver;
import net.ytoec.kernel.techcenter.api.ReportReceiver;
import net.ytoec.kernel.techcenter.api.SMSSEND;
import net.ytoec.kernel.techcenter.api.SMSSender;
import net.ytoec.kernel.techcenter.api.SubmitSender;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.techcenter.client.engine.ClientEngine;
import com.techcenter.client.engine.ConnectorFactory;
import com.techcenter.client.handler.ClientIoHandler;
import com.techcenter.protocol.standard12.Standard_Login;
import com.techcenter.protocol.standard12.Standard_Submit;

/**
 * 支付宝相关的action
 * 
 * @author guoliang.wang
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class AlipayAction extends AbstractActionSupport {

	private static Logger logger = LoggerFactory.getLogger(AlipayAction.class);
	@Inject
	private PaymentService<Payment> paymentService;
	@Inject
	private AccountUserService<AccountUser> accountUserService;

	@Inject
	private DredgeServiceService<DredgeService> dredgeServiceService;

	@Inject
	private ServiceHistoryService<ServiceHistory> serviceHistoryService;
	@Inject
	private LogsService<Logs> logsService;

	@Inject
	private PayServiceService<PayService> payServiceService;
	
	@Inject
	private SMSServiceService smsServiceService;
	
	@Inject
	private UserService<User> userService;
	
	private Payment payment;
    private Double resetPay;//支付宝应付金额
    private Integer serviceId;
    private Map<String, Object> paraMap = new HashMap<String, Object>();
    private List<String> payList = new ArrayList<String>();

	// 充值金额
	private Double rechargeMenoy;

	// 支付方式(目前只有支付宝)
	private String payWay;

	// 交易号
	private Integer id;
    private Integer userId;
    private String menuFlag;
    
    private String smsFlag;//如果是从短信开通的短信服务为1,
    
	private String resFlag="fail";//判断注册是否成功的标志
    
	/**
	 * 注：及时到账支付宝返回调用的方法
	 * 
	 * @return
	 * @throws IOException
	 */
	public String alipayReturn() throws IOException {
		// 获取支付宝的通知返回参数//
		logger.error("及时到账支付宝返回信息处理kaishi");
		//获取支付宝GET过来反馈信息
		// userId = Integer.parseInt(request.getParameter("userId"));
		
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		params.remove("userId");
		boolean bool=true;
		try {
			bool=paymentService.alipayReturn(params);
		} catch (Exception e) {
			logger.error("及时到账支付宝返回信息处理失败",e);
			 for (Entry<String, String> entry: params.entrySet()) {
	                logger.error(entry.getKey()+"="+entry.getValue());
	            }
		}
		return "alipayResult";
	}

	/**
	 * 注：及时到账支付宝返回调用的方法(异步)
	 * 
	 * @return
	 * @throws IOException
	 */
	public void alipayNotify() throws IOException {
		logger.error("及时到账支付宝返回调用的方法(异步)处理kaishi");
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			logger.error("支付宝返回待签名的参数:"+valueStr);
			params.put(name, valueStr);
		}
		Boolean bool=false;
		try {
		 bool=	paymentService.alipayNotify(params);		
		} catch (Exception e) {			
			logger.error("及时到账(异步)支付宝返回信息处理失败");
		}
		if(bool){
			logger.error("及时到账(异步)支付宝返回信息处理success");
			print("success");
		}else{
			logger.error("及时到账(异步)支付宝返回信息处理fail");
			print("fail");
		}
	//	return "alipayResult";
	}

	/**
	 * 充值 注：这里会跳到支付宝界面(在线充值)
	 * 
	 * @return
	 * @throws IOException
	 */
	public String onlineDealPayment() throws IOException {	
	     
		Map<String, Object> map = new HashMap<String, Object>();
		// 1)封装 payment对象的值
		Payment payment=new Payment();
		User currentUser = super.readCookieUser();
		payment.setUserName(currentUser.getUserName());
		
		//获取主账号ID
		Integer mainAccountId = -1;//主账号的id
		if(currentUser.getParentId()!=null) {
			mainAccountId = currentUser.getParentId();
		}
		else {
			mainAccountId = currentUser.getId();
		}
		
		AccountUser accountUser = accountUserService.getAccountUserByUserId(mainAccountId);
		if(accountUser==null){
			accountUser = new AccountUser();
			accountUser.setUserId(mainAccountId);
			accountUserService.addUserAndDService(accountUser);
		}
		
		payment.setUserId(mainAccountId);//修改为主账号ID
		payment.setAuserId(accountUser.getId());
		payment.setCreaterId(currentUser.getId());//添加操作人ID
		payment.setDealMoney(rechargeMenoy);
		//payment.setDealName(RechargeSMSFlagEnum.SPAYMENT.getName());
		payment.setDealName("在线充值"+rechargeMenoy+"元");
		payment.setDealType(PayEnumConstants.DEALTYPE.ONLINE.getValue()); // 在线充值
		payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.IN.value);// 进账
		payment.setDelFlag(PayEnumConstants.DEAL_FLAG.NDEL.value);
		payment.setDealStatus(PayEnumConstants.DEALSTATUS.WAIT.getValue());

		
		AlipayConfigMessage alipayConfigMessage=getAlipayConfig();//支付宝账号的配置信息
		map.put("alipayConfigMessage", alipayConfigMessage);
		map.put("payment", payment);// 在线充值传值
		String alipayURL = paymentService.dealPayment(map);
		
		request.setAttribute("alipayURL", alipayURL);
		return "alipay";
	}
	// 交易（支付）明细表列表 页面 支付 功能
	public String onlinePay() throws IOException {
		
		User currentuUser = super.readCookieUser();		
		Integer mainAccountId = -1;//主账号的id
		if(currentuUser.getParentId()!=null) {
			mainAccountId = currentuUser.getParentId();
		}
		else {
			mainAccountId = currentuUser.getId();
		}
		
		Payment payment = new Payment();
		// 根据交易号id查询交易明细
		if (this.id != null) {
			payment.setId(id);
			payment = paymentService.get(payment);
		}
		if(!StringUtils.equals(payment.getDealType(), PayEnumConstants.DEALTYPE.ONLINE.getValue())){
			List<ServiceHistory>list = new ArrayList<ServiceHistory>();
			Map<String, Object> smap = new HashMap<String, Object>();
			
			smap.put("userId", mainAccountId);////主账号的id
			smap.put("payId", payment.getId());//交易明细ID
			
			list = serviceHistoryService.getServiceHistoryListByDUserId(smap);
			ServiceHistory serviceHistory = new ServiceHistory();
			if(!CollectionUtils.isEmpty(list)){
				serviceHistory = list.get(0);
			}
			
			AccountUser acUser = accountUserService.getAccountUserByUserId(mainAccountId);
			//根据服务ID查找开通服务表记录
		    DredgeService dService = new DredgeService();
		    dService.setId(serviceHistory.getDserviceId());
		    dService = dredgeServiceService.get(dService);
			
			Map<String, Object> map = new HashMap<String, Object>();
			Double money = payment.getDealMoney();// 支付宝应付金额
			boolean result = false;
			if (acUser.getUseBalance() >= money) { // 如果账户充足的话
				// 更新账户表
			    acUser.setUseBalance(acUser.getUseBalance() - money);// 可用余额
				acUser.setBalance(acUser.getBalance() - serviceHistory.getCost());//余额
				acUser.setAllConsume(acUser.getAllConsume() + money);// 总消费增加 这个还有问题 需要改一下的getAllConsume
				map.put("accountUser", acUser);
				map.put("accountParentUser", null);
				// 开通服务表
				if (dService == null) {
					return "exception";
				} else {					
					//这个时候只是更新状态 由 创建 到 启用
					dService.setId(dService.getId());
					dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 更新到1
					map.put("dredgeService", dService);// 更新服务表
					
			        //交易明细表  这个只是更新 付款时间 以及交易状态 ，操作人ID
					Payment pm = new Payment();
					pm.setPayTime(new Date());// 付款时间
					pm.setCreaterId(currentuUser.getId());//操作人ID
					pm.setId(payment.getId());
					pm.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
					
					if(serviceHistory != null){
						map.put("serviceHistory", serviceHistory);// 服务历史表
					}else {
						//插入服务历史记录表
						serviceHistory.setDserviceId(dService.getId());
						serviceHistory.setUserId(mainAccountId);
						serviceHistory.setCircle(dService.getCircle());// 周期
						serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
						serviceHistory.setEndDate(dService.getEndDate());// 到期时间
						serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
						serviceHistory.setDealType(payment.getDealType());// 交易类型
						serviceHistory.setDealName(payment.getDealName());// 服务名称
						serviceHistory.setCost(money);
						serviceHistory.setPromCost(money);// 促销费用
						map.put("serviceHistory", serviceHistory);// 服务历史表
					}
					
					map.put("payment", pm);
					result = dredgeServiceService.openService(map, "3");
					
					if(result){
						paymentService.sendSMSByUserId(mainAccountId,payment.getDealName(),dService, "");//余额不足提醒，交易提醒
				        return "toPayOrder";
					}
					return "exception";
				}
			} else {
				if(currentuUser.getChildType() != null && "1".equals(currentuUser.getUserType()) && currentuUser.getChildType().equals("B")){
					AccountUser aUser = accountUserService.getAccountUserByUserId(Integer.parseInt(currentuUser.getUserSource()));
					 //如果当当前用户是分仓账号，  如果当前用户的金额加上平台用户的金额足够购买服务，
					 if(aUser != null && (acUser.getUseBalance() + aUser.getUseBalance()) >= money){
						    //(如果是分仓用户)更新平台用户金额
							aUser.setUseBalance(aUser.getUseBalance() - (money - acUser.getUseBalance()));// 可用余额
							aUser.setBalance(aUser.getBalance() - (money - acUser.getUseBalance()));//余额
							aUser.setAllConsume(aUser.getAllConsume()+(money-acUser.getUseBalance()));// 总消费增加 这个还有问题 需要改一下的getAllConsume
							aUser.setId(aUser.getId());
							map.put("accountParentUser", aUser);
						    // 更新账户表
							acUser.setAllConsume(acUser.getAllConsume() + acUser.getBalance());// 总消费增加 这个还有问题 需要改一下的getAllConsume
							acUser.setBalance(acUser.getBalance() - acUser.getBalance());//余额
							acUser.setUseBalance(acUser.getUseBalance() - acUser.getUseBalance());// 可用余额
							map.put("accountUser", acUser);
							// 开通服务表
							if (dService == null) {
								return "exception";
							} else {								
								//这个时候只是更新状态 由 创建 到 启用
								dService.setId(dService.getId());
								dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 更新到1
								map.put("dredgeService", dService);// 更新服务表
								
						        //交易明细表  这个只是更新 付款时间 以及交易状态 ，操作人ID
								Payment pm = new Payment();
								pm.setPayTime(new Date());// 付款时间
								pm.setCreaterId(currentuUser.getId());//操作人ID
								pm.setId(payment.getId());
								pm.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
								
								if(serviceHistory != null){
									map.put("serviceHistory", serviceHistory);// 服务历史表
								}else {
									//插入服务历史记录表
									serviceHistory.setDserviceId(dService.getId());
									serviceHistory.setUserId(mainAccountId);
									serviceHistory.setCircle(dService.getCircle());// 周期
									serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
									serviceHistory.setEndDate(dService.getEndDate());// 到期时间
									serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
									serviceHistory.setDealType(payment.getDealType());// 交易类型
									serviceHistory.setDealName(payment.getDealName());// 服务名称
									serviceHistory.setCost(money);
									serviceHistory.setPromCost(money);// 促销费用
									map.put("serviceHistory", serviceHistory);// 服务历史表
								}
								
								map.put("payment", pm);
								result = dredgeServiceService.openService(map, "3");
								//System.out.println(result);
								if(result){
									paymentService.sendSMSByUserId(mainAccountId,payment.getDealName(),dService, "");//余额不足提醒，交易提醒
							        return "toPayOrder";
								}
							}
							return "exception";
					 }
				}
				
				/***
				 *  首先就是关闭这个订单，旧订单做flag=5逻辑删除
				 *  然后就是重新付款
				 *  这个时候需要更新账户表
				 *  插入交易明细表（一条新订单）
				 *  更新服务历史记录表(主要是更新交易明细id payId)
				 *  
				 *  然后把订单传入到支付宝
				 */
				
				//更新交易明细表中该交易的dealMoney
//				payment.setDealMoney(payment.getDealMoney() - acUser.getUseBalance());
//				payment.setId(payment.getId());
//		    	paymentService.updateDealMoneyById(payment);//更新dealMoney
//		    	
//                Map<String, Object> payMap = new HashMap<String, Object>();
//				
//				payMap.put("dredgeService", dService);
//				payMap.put("payment", payment);// 在线充值传值
//				payMap.put("serviceHistory", serviceHistory);
				Map<String, Object> payMap =paymentService.add_Alipay(payment,serviceHistory,dService,acUser,currentuUser.getId());
				
				AlipayConfigMessage alipayConfigMessage=getAlipayConfig();//支付宝账号的配置信息
				map.put("alipayConfigMessage", alipayConfigMessage);
				String alipayURL = paymentService.dealPayment(payMap);
				request.setAttribute("alipayURL", alipayURL);
				return "alipay";

			}
		}else {
			Map<String, Object> payMap = new HashMap<String, Object>();
			payMap.put("payment", payment);//在线充值
			AlipayConfigMessage alipayConfigMessage=getAlipayConfig();//支付宝账号的配置信息
			payMap.put("alipayConfigMessage", alipayConfigMessage);
			String alipayURL = paymentService.dealPayment(payMap);
			request.setAttribute("alipayURL", alipayURL);
			return "alipay";
		}
	}

	// 转到在线充值页面
	public String toRechargeOnline() {
		User currentUser = super.readCookieUser();
		
		//update by guolongchao 20120925 权限修改       
		Integer mainAccountId = -1;//主账号的id
		if(currentUser.getParentId()!=null) {
			mainAccountId = currentUser.getParentId();
		}
		else {
			mainAccountId = currentUser.getId();
		}
				
		AccountUser accountUser = accountUserService.getAccountUserByUserId(mainAccountId);
		if(accountUser==null){
			accountUser = new AccountUser();
			accountUser.setUserId(mainAccountId);
			 accountUserService.addUserAndDService(accountUser);
		}
		return "success";
	}

	
	/**
	 * 从短信那里点击立即使用 当点击立即使用的时候会跳转到开通服务页面(这个不包括短信服务)
	 * 这个时候需要把serviceId页面传到后台
	 * 根据serviceId进行判断是那种服务
	 */
	public String applyService() {
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		List<Integer> list = new ArrayList<Integer>();
		if("1".equals(currentUser.getUserType())){
			List<Integer> listRelation = (ArrayList)Resource.getUserRelationUserIdList(currentUser);
			if(CollectionUtils.isNotEmpty(listRelation)){
				list=(List<Integer>) ((ArrayList)listRelation).clone();
			    list.add(userId);	
			}else {
				list = new ArrayList<Integer>();
				list.add(userId);
			}
				
		}
		
		if(CollectionUtils.isNotEmpty(list)){
			for(Integer ite : list){
				User usr = userService.getUserById(ite);
				AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
				if (acUser == null) {
					acUser = new AccountUser();
					acUser.setUserId(userId);
				    accountUserService.addUserAndDService(acUser);
				}
				if(usr != null && usr.getShopName() != null){
				  payList.add(usr.getShopName());
				}
			}
		}
		PayService ps = new PayService();
		ps.setId(serviceId);
		ps = payServiceService.get(ps);
		if (currentUser != null) {
			userId = currentUser.getId();
		}
		AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
		DredgeService dredgeService = getDredgeServiceByParameter(userId, serviceId);
		boolean result = true;
		if (acUser == null) {
			acUser = new AccountUser();
			acUser.setUserId(userId);
			result = accountUserService.addUserAndDService(acUser);
		}
		  if(acUser.getUseBalance() == null){
			  paraMap.put("useBalance", 0.00);//可用余额 
		  }else {
			paraMap.put("useBalance", acUser.getUseBalance());
		}
		
		  if(currentUser.getShopName() != null){
				paraMap.put("shopName",currentUser.getShopName());//店铺名
			}else {
				paraMap.put("shopName",currentUser.getUserName());//用户名
			}
		paraMap.put("serviceName", ps.getName());//服务名称
		paraMap.put("serviceid", ps.getId());// serviceId
		paraMap.put("remark", ps.getRemark());//服务描述
		if(serviceId == 5){
			paraMap.put("smsFlag", "1");//短信服务开启的来源
		}
		if(dredgeService != null){
			if(StringUtils.equals(dredgeService.getFlag(), "1") && dredgeService.getCircle() != null){
				paraMap.put("circle", dredgeService.getCircle());
				paraMap.put("flag", "1");
			}else {
				paraMap.put("circle", "");
			}
		}else {
			paraMap.put("circle", "");
		}
		
		if(ps.getUnitPrice() == null){
			ps.setUnitPrice(0.00);
		}
		paraMap.put("unitPrice", ps.getUnitPrice());//单价
		
		//将当前日期,一个月,一个季度,半年,一年的日期存入paraMap,在页面显示是要用到-----start
		Date now = new Date();
		paraMap.put("now", DateUtil.getDateStr(now, "yyyy-MM-dd"));
		paraMap.put("month", DateUtil.getDateStr(DateUtil.getCireclDate(now, 1), "yyyy-MM-dd"));
		paraMap.put("season", DateUtil.getDateStr(DateUtil.getCireclDate(now, 3), "yyyy-MM-dd"));
		paraMap.put("half", DateUtil.getDateStr(DateUtil.getCireclDate(now, 6), "yyyy-MM-dd"));
		paraMap.put("year", DateUtil.getDateStr(DateUtil.getCireclDate(now, 12), "yyyy-MM-dd"));		
		//将当前日期,一个季度,半年,一年的日期存入paraMap,在页面显示是要用到-----end
		
		if (result) {
			return "toApplicay";
		}
		return "exception";
	}
	
	/**
     * 开通短信服务
     */
	public String applySmsService(){
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		//得到账户信息
		AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
		PayService ps = new PayService();
		ps.setId(serviceId);
		ps = payServiceService.get(ps);
		boolean result = true;
//		if (acUser == null) {
//			acUser = new AccountUser();
//			acUser.setUserId(userId);
//			result = accountUserService.addUserAndDService(acUser);
//		}else {
//		  if(acUser.getUseBalance() == null){
//			  paraMap.put("useBalance", 0.00);//可用余额 
//		  }else {
//			paraMap.put("useBalance", acUser.getUseBalance());
//		}
//		}
//		paraMap.put("shopName",currentUser.getUserName());//店铺名
//		paraMap.put("serviceName", ps.getName());//服务名称
//		paraMap.put("serviceid", ps.getId());// serviceId
//		if(ps.getUnitPrice() == null){
//			ps.setUnitPrice(0.00);
//		}
//		paraMap.put("unitPrice", ps.getUnitPrice());//单价
		//判断这个服务是否已经开通过
		DredgeService dService = getDredgeServiceByParameter(userId,serviceId);
		Payment pment = new Payment();
		Map<String, Object> map = new HashMap<String, Object>();
		// 插入交易明细 表
		//payment = new Payment();
		pment.setUserId(userId);// 得到用户id
		if(currentUser.getParentId() != null){
			pment.setCreaterId(currentUser.getId());
		}else {
			pment.setCreaterId(userId);
		}
		pment.setAuserId(acUser.getId());// 得到账户id
		pment.setDealMoney(0.0);// 交易额
		pment.setPayTime(new Date());// 付款时间
		pment.setDealName("短信服务");
		pment.setDealType(PayEnumConstants.DEALTYPE.SMSSERVICE.getValue());// 交易类型 短信服务
		pment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
		pment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);// 状态
		//插入服务历史记录表
		ServiceHistory serviceHistory = new ServiceHistory();
		serviceHistory.setDserviceId(serviceId);
		serviceHistory.setUserId(userId);
		serviceHistory.setDealType(pment.getDealType());// 交易类型
		serviceHistory.setDealName(pment.getDealName());// 服务名称
		serviceHistory.setCost(0.0);
		serviceHistory.setPromCost(0.0);// 促销费用
		map.put("payment", pment);
		map.put("serviceHistory", serviceHistory);
		map.put("accountUser", null);
		if(dService == null){
			//插入一条开通服务表
				dService = new DredgeService();
				dService.setUserId(userId);
				dService.setBeginDate(new Date());
				dService.setServiceId(serviceId);
				dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());//启用
				map.put("dredgeService", dService);
				dredgeServiceService.openService(map, "4");
			}else {
				dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
				dService.setId(dService.getId());
				dService.setBeginDate(new Date());
				map.put("dredgeService", dService);
				dredgeServiceService.openService(map, "5");
		}
		
		List<SMSService> list = new ArrayList<SMSService>();
		try{
		  list = smsServiceService.getSMSServiceAll(userId); //取得当前用户id 获取所有的短信服务 
			if(CollectionUtils.isEmpty(list)){
				    //如果是分仓账 ,则查询父账号的，
					if(currentUser.getChildType() != null && "1".equals(currentUser.getUserType()) && "B".equals(currentUser.getChildType())){
						smsServiceService.saveServiceByIsOn(Integer.parseInt(currentUser.getUserSource()), userId);	
					}else {
						smsServiceService.saveServiceByIsOn(userId, userId);
					}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "toOpenSmsFromSms";
	}
	
	/**
	 * 了解详情 
	 * 
	 * @return
	 */
	public String seeDetails() {
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		 //如果是已经存在的未付款的订单，点击开始使用的时候会到交易明细页面
	    List<Payment> list = new ArrayList<Payment>();
       // List<Payment> paymentListNoPay = new ArrayList<Payment>();//查询未付款的交易明细表
        PayService pservice = new PayService();
        pservice.setId(serviceId);
        pservice = payServiceService.get(pservice);
      
		Map<String, Object> paymap = new HashMap<String, Object>();
		if(serviceId == 5){
			paymap.put("smsFlag", smsFlag);
		}
		paymap.put("userId", userId);
		paymap.put("dealStatus","0");//未付款
		paymap.put("delFlag", "0");//未关闭
		paymap.put("dealName", pservice.getName());//交易名称
		list = paymentService.getList(paymap);//根据上面的条件进行查询 之后得到最大的值 就是当前的订单的状态
		if(!CollectionUtils.isEmpty(list)){
			 payment = list.get(0);
		}	
		if(serviceId == 5){
			return "smsServiceDetail";
		}
		return "toServiceDetailFromSms";
	}
	
	/**
	 * 立即使用 当点击立即使用的时候会跳转到开通服务页面
	 * 这个时候需要把serviceId页面传到后台
	 * 根据serviceId进行判断是那种服务
	 */
	public String applyServiceFromDetail() {
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		List<Integer> list = new ArrayList<Integer>();
		if("1".equals(currentUser.getUserType())){
			List<Integer> listRelation = (ArrayList)Resource.getUserRelationUserIdList(currentUser);
			if(CollectionUtils.isNotEmpty(listRelation)){
				list=(List<Integer>) ((ArrayList)listRelation).clone();
			    list.add(userId);	
			}else {
				list = new ArrayList<Integer>();
				list.add(userId);
			}
				
		}
		
		if(CollectionUtils.isNotEmpty(list)){
			for(Integer ite : list){
				User usr = userService.getUserById(ite);
				AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
				if (acUser == null) {
					acUser = new AccountUser();
					acUser.setUserId(userId);
				    accountUserService.addUserAndDService(acUser);
				}
				if(usr != null && usr.getShopName() != null){
				  payList.add(usr.getShopName());
				}
			}
		}
		PayService ps = new PayService();
		ps.setId(serviceId);
		ps = payServiceService.get(ps);
		if (currentUser != null) {
			userId = currentUser.getId();
		}
		AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
		DredgeService dredgeService = getDredgeServiceByParameter(userId, serviceId);
		boolean result = true;
		if (acUser == null) {
			acUser = new AccountUser();
			acUser.setUserId(userId);
			result = accountUserService.addUserAndDService(acUser);
		}
		  if(acUser.getUseBalance() == null){
			  paraMap.put("useBalance", 0.00);//可用余额 
		  }else {
			paraMap.put("useBalance", acUser.getUseBalance());
		}
		  if(serviceId == 5){
				 paraMap.put("smsFlag", "1");
			 }
		
		  if(currentUser.getShopName() != null){
				paraMap.put("shopName",currentUser.getShopName());//店铺名
			}else {
				paraMap.put("shopName",currentUser.getUserName());//店铺名
			}
		paraMap.put("serviceName", ps.getName());//服务名称
		paraMap.put("serviceid", ps.getId());// serviceId
		paraMap.put("remark", ps.getRemark());//服务描述
		if(dredgeService != null){
			if(StringUtils.equals(dredgeService.getFlag(), "1") && dredgeService.getCircle() != null){
				paraMap.put("circle", dredgeService.getCircle());
				paraMap.put("flag", "1");
			}else {
				paraMap.put("circle", "");
			}
		}else {
			paraMap.put("circle", "");
		}
		
		if(ps.getUnitPrice() == null){
			ps.setUnitPrice(0.00);
		}
		paraMap.put("unitPrice", ps.getUnitPrice());//单价
		
		//将当前日期,一个月,一个季度,半年,一年的日期存入paraMap,在页面显示是要用到-----start
		Date now = new Date();
		paraMap.put("now", DateUtil.getDateStr(now, "yyyy-MM-dd"));
		paraMap.put("month", DateUtil.getDateStr(DateUtil.getCireclDate(now, 1), "yyyy-MM-dd"));
		paraMap.put("season", DateUtil.getDateStr(DateUtil.getCireclDate(now, 3), "yyyy-MM-dd"));
		paraMap.put("half", DateUtil.getDateStr(DateUtil.getCireclDate(now, 6), "yyyy-MM-dd"));
		paraMap.put("year", DateUtil.getDateStr(DateUtil.getCireclDate(now, 12+3), "yyyy-MM-dd"));		
		//将当前日期,一个季度,半年,一年的日期存入paraMap,在页面显示是要用到-----end
		
		if (result) {
			return "toApplicayFromDetilSms";
		}
		return "exception";
	}
	
	// 根据userId 和 serviceId 得到值
	public DredgeService getDredgeServiceByParameter(Integer userId,
			Integer serviceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("serviceId", serviceId);
		List<DredgeService> list = dredgeServiceService.getServiceRecordByUserId(map);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}else {
		  return null;
		}
	}
	
	// 在线充值页面
	public String toRechargeOnlineIndex() {
		User currentUser = super.readCookieUser();
		
		//update by guolongchao 20120925 权限修改       
		Integer mainAccountId = -1;//主账号的id
		if(currentUser.getParentId()!=null) {
			mainAccountId = currentUser.getParentId();
		}
		else {
			mainAccountId = currentUser.getId();
		}
		AccountUser accountUser = accountUserService.getAccountUserByUserId(mainAccountId);
		if(accountUser==null){
			accountUser = new AccountUser();
			accountUser.setUserId(mainAccountId);
			 accountUserService.addUserAndDService(accountUser);
		}
		return "success";
	}
	
	public Double getRechargeMenoy() {
		return rechargeMenoy;
	}
   
	/**
	 *无线天利注册
	 * (重启系统,要重新注册)
	 * @return
	 * @throws IOException
	 */
	public String registexTeach() throws IOException {		
		//===================以下东西从配置文件中读取beign
		Global g = Global.getInstance();
		//===========================以下东西从配置文件中读取end
		logger.error("激活无线天利begin");		
		SubmitSender submitSender = new SubmitSender();
		DeliverReceiver deliverReceiver = new DeliverReceiver();
		ReportReceiver reportRecceiver = new ReportReceiver();
		//初始化客户的发送接收类
		g.setDeliverReceiver(deliverReceiver);
		g.setReportRecceiver(reportRecceiver);
		g.setSubmitSender(submitSender);		
		//下行处理handler
		ClientIoHandler mthandler = new ClientIoHandler();
		//设置ClientId
		mthandler.setClientId(g.clientId);
		//设置滑动窗口大小
		mthandler.setControlWindowsSize(g.controlWindowSize);
		//设置登录用户名
		mthandler.setLoginName(g.loginName);
		//设置密码
		mthandler.setPassword(g.password);
		//设置IdleTime
		mthandler.setIdleTime(g.idleTime);
		//接收端口缓冲大小
		mthandler.setInBufferSize(g.inBufferSize);
		//发送端口缓冲大小
		mthandler.setOutBufferSize(g.outBufferSize);
		//设置Decoder列表
		mthandler.setDecoderList(Global.getDecoderList());
		//设置EncoderMap对象
		mthandler.setEncoderMap(Global.getEncoderMap());
		//设置已发送handler列表
		mthandler.setSentHandlerList(Global.getSentHandlerList());
		//设置接收handler列表
		mthandler.setReceivedHandlerList(g.getHandler());
		//设置异常handler列表
		mthandler.setExceptionHandlerList(Global.getExceptionHandlerList());
		//设置登录属性
		mthandler.setSessionType(Standard_Login.SESSION_MT);
		//设置滑动窗口的清理时间
		mthandler.setClearTimeOut(g.clearTimeOut);
		//设置滑动窗口的休眠时间
		mthandler.setClearSleepTime(g.clearSleepTime);
		//设置重发标志
		mthandler.setReSend(false);
		
		//上行处理handler
		ClientIoHandler mohandler = new ClientIoHandler();
		//设置ClientId
		mohandler.setClientId(g.clientId);
		//设置滑动窗口大小
		mohandler.setControlWindowsSize(g.controlWindowSize);
		//设置登录用户名
		mohandler.setLoginName(g.loginName);
		//设置密码./s
		mohandler.setPassword(g.password);
		//设置IdleTime
		mohandler.setIdleTime(g.idleTime);
		//接收端口缓冲大小
		mohandler.setInBufferSize(g.inBufferSize);
		//发送端口缓冲大小
		mohandler.setOutBufferSize(g.outBufferSize);	
		//设置Decoder列表
		mohandler.setDecoderList(g.getDecoderList());
		//设置EncoderMap对象
		mohandler.setEncoderMap(g.getEncoderMap());
		//设置已发送handler列表
		mohandler.setSentHandlerList(g.getSentHandlerList());
		//设置接收handler列表
		mohandler.setReceivedHandlerList(g.getHandler());
		//设置异常handler列表
		mohandler.setExceptionHandlerList(g.getExceptionHandlerList());
		//设置登录属性
		mohandler.setSessionType(Standard_Login.SESSION_MO);
		//设置滑动窗口的清理时间
		mohandler.setClearTimeOut(g.clearTimeOut);
		//设置滑动窗口的休眠时间
		mohandler.setClearSleepTime(g.clearSleepTime);
		//设置重发标志
		mohandler.setReSend(false);		
	
		//handler列表
		List<ConnectorFactory> conFactoryList = new ArrayList<ConnectorFactory>();
		//设置下行handler
		conFactoryList.add(g.getConnectorFactory(mthandler));
		//设置上行handler
		conFactoryList.add(g.getConnectorFactory(mohandler));
		
		//客户端引擎
		ClientEngine ce = new ClientEngine();
		ce.setRunning(true);
		ce.setConFactoryList(conFactoryList);
		//启动客户端引擎
		ce.start();
		//主动发送
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SMSSEND send= new SMSSEND();
		send.send();
		logger.error("激活无线天利end");
		resFlag="success";
		return "register";
	}
	
	//读取[支付宝]相关配置信息
	public AlipayConfigMessage getAlipayConfig(){
		AlipayConfigMessage alipayConfigMessage=new AlipayConfigMessage();
		alipayConfigMessage.setAlipayPartner(ConfigUtilSingle.getInstance().getALIPAY_PARTNER());
		alipayConfigMessage.setAlipayKey(ConfigUtilSingle.getInstance().getALIPAY_KEY());
		alipayConfigMessage.setAlipaySellerEmail(ConfigUtilSingle.getInstance().getALIPAY_SELLER_EMAIL());
		alipayConfigMessage.setReturnUrl(ConfigUtilSingle.getInstance().getRETURN_URL());
		alipayConfigMessage.setNotifyUrl(ConfigUtilSingle.getInstance().getNOTIFY_URL());
		alipayConfigMessage.setAlipayInputCharset(ConfigUtilSingle.getInstance().getALIPAY_INPUT_CHARSET());
		return alipayConfigMessage;
	}
	/**
	 * 将string的内容写入到响应对象中. 注意:此方法仅应该被调用一次,因为方法内部的流对象在写入完成后关闭,
	 * 因此程序中应该去组装string值,然后调用此方法写入响应对象中.
	 * 
	 * @param string
	 *            待写入的字符串.
	 * @throws IOException
	 */
	public void print(String string) throws IOException {
		this.setResponseContentType(response);
		PrintWriter out = this.response.getWriter();
		out.print(string);
		out.flush();
		out.close();

	}
	private void setResponseContentType(HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
	}
	public void setRechargeMenoy(Double rechargeMenoy) {
		this.rechargeMenoy = rechargeMenoy;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public String getResFlag() {
		return resFlag;
	}
	public void setResFlag(String resFlag) {
		this.resFlag = resFlag;
	}

	public Map<String, Object> getParaMap() {
		return paraMap;
	}
	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Double getResetPay() {
		return resetPay;
	}
	public void setResetPay(Double resetPay) {
		this.resetPay = resetPay;
	}
	public String getSmsFlag() {
		return smsFlag;
	}
	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	
	public List<String> getPayList() {
		return payList;
	}

	public void setPayList(List<String> payList) {
		this.payList = payList;
	}
}
