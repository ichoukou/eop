package net.ytoec.kernel.action.sms;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.apache.struts2.json.annotations.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.AlipayConfigMessage;
import net.ytoec.kernel.service.AccountUserService;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.ConfigUtilSingle;

@Controller
@Scope("prototype")
public class SmsServicePackageAction extends AbstractActionSupport{
	
	@Inject
	private AccountUserService<AccountUser> accountUserService;
	@Inject
	private PaymentService<Payment> paymentService;
	@Inject
	private PayServiceService<PayService> payServiceService;
	@Inject
	private DredgeServiceService<DredgeService> dredgeServiceService;
	@Inject
	private UserService<User> userService;
	private AccountUser accountUser;
	private Payment payment = new Payment();
	
	Map<String, String> params = new HashMap<String, String>();

	private Integer userId;//用户Id
	private Double useBalance;//用户余额
	private Double balance = 0.0;//平台用户余额
    private Integer serviceId;//服务Id
    private Double money;//支付宝应付金额
    
    private Integer smsCount;//短信条数

    private String flag; // 表示购买哪种短信套餐
    
    public String getPackageByFlag() {
    	
    	if (StringUtils.equals(flag, "1")) {
	    	//params.put("portName", "50元超值套餐");
	    	params.put("portName", "购买600条短信");	    	
	    	params.put("portNum", "600");
	    	params.put("portPrice", "50");
    	} else if (StringUtils.equals(flag, "2")){
    		//params.put("portName", "100元超值套餐");
    		params.put("portName", "购买1220条短信");
	    	params.put("portNum", "1220");
	    	params.put("portPrice", "100");
    	} else if (StringUtils.equals(flag, "3")){
    		//params.put("portName", "200元超值套餐");
    		params.put("portName", "购买2500条短信");
	    	params.put("portNum", "2500");
	    	params.put("portPrice", "200");
    	} else if (StringUtils.equals(flag, "4")){
    		//params.put("portName", "500元超值套餐");
    		params.put("portName", "购买6500条短信");
	    	params.put("portNum", "6500");
	    	params.put("portPrice", "500");
    	} else if (StringUtils.equals(flag, "5")){
    		//params.put("portName", "1000元超值套餐");
    		params.put("portName", "购买15000条短信");
	    	params.put("portNum", "15000");
	    	params.put("portPrice", "1000");
    	} else if (StringUtils.equals(flag, "6")){
    		//params.put("portName", "2000元超值套餐");
    		params.put("portName", "购买32000条短信");
	    	params.put("portNum", "32000");
	    	params.put("portPrice", "2000");
    	} else if (StringUtils.equals(flag, "7")){
    		//params.put("portName", "5000元超值套餐");
    		params.put("portName", "购买85000条短信");
	    	params.put("portNum", "85000");
	    	params.put("portPrice", "5000");
    	} else {
    		params.put("portName", "");
	    	params.put("portNum", "0");
	    	params.put("portPrice", "0");
    	}
    	return "packageParams";
    }
    
	public String getUserBalance(){
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
		}
    	accountUser = accountUserService.getAccountUserByUserId(userId);
       if(accountUser == null){
    	   if(currentUser.getUserType() != null && (("11".equals(currentUser.getUserType())||
    			                                    "12".equals(currentUser.getUserType())||
    			                                    "13".equals(currentUser.getUserType()))
    			                                    ||"41".equals(currentUser.getUserType()))){
    		   if(currentUser.getParentId() != null){
    			   accountUser = accountUserService.getAccountUserByUserId(currentUser.getParentId());
    			   if(accountUser == null){
    				   accountUser = new AccountUser();
    		    	   accountUser.setUserId(currentUser.getParentId());
    				   accountUserService.addUserAndDService(accountUser);
    			   }
    		   }
    		   
    	   }else {
    		   accountUser = new AccountUser();
        	   accountUser.setUserId(userId);
        	   accountUserService.addUserAndDService(accountUser);
    	   }
       }
       if(currentUser.getUserType() != null && (("11".equals(currentUser.getUserType())||
               "12".equals(currentUser.getUserType())||
               "13".equals(currentUser.getUserType()))
               ||"41".equals(currentUser.getUserType()))){
    	   if(currentUser.getParentId() != null){
			   accountUser = accountUserService.getAccountUserByUserId(currentUser.getParentId());
			   useBalance = accountUser.getUseBalance();//得到账户余额
    	   }
       }else {
    	   useBalance = accountUser.getUseBalance();//得到账户余额
       }
       if(currentUser.getChildType() != null && "1".equals(currentUser.getUserType()) && currentUser.getChildType().equals("B")){
    	 if(currentUser.getUserSource() != null){
    		 AccountUser aUser = accountUserService.getAccountUserByUserId(Integer.parseInt(currentUser.getUserSource()));
             if(aUser != null){
            	 balance = aUser.getUseBalance();
             }
    	 }
       }
//       if("11".equals(currentUser.getUserType()) || "12".equals(currentUser.getUserType())
//    		   ||"13".equals(currentUser.getUserType())||"41".equals(currentUser.getUserType())){
//    	   if(currentUser.getParentId() != null){
//    		   AccountUser aUser = accountUserService.getAccountUserByUserId(currentUser.getParentId());
//               if(aUser != null && aUser.getUseBalance() != null){
//              	 balance = aUser.getUseBalance();
//               }
//    	   }
//       }
		accountUser.setUserName(currentUser.getUserName());
		accountUser.setUseBalance(useBalance);//设置可用余额
		accountUser.setBalance(balance);
		return "getUseBalance";
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String toAlipay(){
		User currentuUser = super.readCookieUser();
		if (currentuUser != null) {
			userId = currentuUser.getId();
		}
		
		//根据名称得到serviceId
		PayService ps = new PayService();
		
    	if (StringUtils.equals(flag, "1")) {
	    	//ps.setName("50元超值套餐");
	    	ps.setName("购买600条短信");
	    	smsCount = 600;
	    	money = (double) 50;
	    	//money = (double)1;
	    	//money = (double) 1;
    	} else if (StringUtils.equals(flag, "2")){
    		//ps.setName("100元超值套餐");
    		ps.setName("购买1220条短信");
	    	smsCount = 1220;
	    	money = (double) 100;
    	} else if (StringUtils.equals(flag, "3")){
	    	//ps.setName("200元超值套餐");
    		ps.setName("购买2500条短信");
	    	smsCount = 2500;
	    	money = (double) 200;
    	} else if (StringUtils.equals(flag, "4")){
	     	//ps.setName("500元超值套餐");
    		ps.setName("购买6500条短信");
	    	smsCount = 6500;
	    	money = (double) 500;
    	} else if (StringUtils.equals(flag, "5")){
	       	//ps.setName("1000元超值套餐");
    		ps.setName("购买15000条短信");
	    	smsCount = 15000;
	    	money = (double) 1000;
    	} else if (StringUtils.equals(flag, "6")){
	       	//ps.setName("2000元超值套餐");
    		ps.setName("购买32000条短信");
	    	smsCount = 32000;
	    	money = (double) 2000;
    	} else if (StringUtils.equals(flag, "7")){
	       	//ps.setName("5000元超值套餐");
    		ps.setName("购买85000条短信");
	    	smsCount = 85000;
	    	money = (double) 5000;
    	} else {
    		ps.setName("");
	    	smsCount = 0;
	    	money = (double) 0;
    	}
    	
    	payment.setDealName(ps.getName());
		ps = payServiceService.get(ps);
		if(serviceId == null){
		   serviceId = ps.getId();
		}
		AccountUser acUser = new AccountUser();
		if(currentuUser.getUserType() != null && (("11".equals(currentuUser.getUserType())||
                 "12".equals(currentuUser.getUserType())||
                 "13".equals(currentuUser.getUserType()))
                 ||"41".equals(currentuUser.getUserType()))){
			 if(currentuUser.getParentId() != null){
				 acUser = accountUserService.getAccountUserByUserId(currentuUser.getParentId());
			 }
		}else {
				acUser = accountUserService.getAccountUserByUserId(userId);
		}
		//如果是子账号，得到起开通服务的父账号
		userId = getOepnUserId(currentuUser);
		DredgeService dService = getDredgeServiceByParameter(userId, 5);//得到已开通服务信息(短信服务)
	    if(dService == null){
	    	dService = new DredgeService();
			dService.setUserId(userId);
			dService.setServiceId(5);
			dService.setServiceId(PayEnumConstants.SERVICE.SMS.getValue());
			dService.setAutoRenew(PayEnumConstants.AUTO_FLAG.ISNAUTO.value);//是否自动续费
			dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
			dService.setCost(money);
			dService.setSmsUsecount(smsCount);
			dService.setSmsAllcount(smsCount);	
			dService.setBeginDate(new Date());
			dredgeServiceService.add(dService);//开启短信服务 
	    }
		payment.setDealType(PayEnumConstants.DEALTYPE.SMS.getValue());//短信服务
			
			dService.setFlag(PayEnumConstants.SERVICEFLAG.CREATE.getValue());// 更新到1
			dService.setCost(money);
			dService.setId(dService.getId());
			dService.setUserId(userId);
	
		//两个对象payment 、 dredgeService、serviceHistory
		Map<String, Object> alipay_map=new HashMap<String, Object>();
		    if(acUser.getUseBalance() == null){
		    	acUser.setUseBalance(0.00);
		    }
			payment.setUserId(userId);
			payment.setDealMoney(money-acUser.getUseBalance());//支付宝应付金额
			payment.setAuserId(acUser.getId());
			payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);//出账
			payment.setCreaterId(currentuUser.getId());
			
	    ServiceHistory serviceHistory = new ServiceHistory();
	    serviceHistory.setDserviceId(serviceId);
		serviceHistory.setUserId(userId);
		serviceHistory.setDealType(payment.getDealType());// 交易类型
		serviceHistory.setDealName(payment.getDealName());// 服务名称
		serviceHistory.setCost(money);
		serviceHistory.setPromCost(money);// 促销费用
		serviceHistory.setSmsCount(smsCount);//短信条数
			
		alipay_map.put("payment", payment);
		alipay_map.put("dredgeService", dService);
		alipay_map.put("serviceHistory", serviceHistory);
		AlipayConfigMessage alipayConfigMessage=getAlipayConfig();//支付宝账号的配置信息
		alipay_map.put("alipayConfigMessage", alipayConfigMessage);
		String payURLString =paymentService.dealPayment(alipay_map);
		request.setAttribute("alipayURL", payURLString);
		return "alipay";
	}
	
	// 根据userId 和 serviceId 得到值
	public DredgeService getDredgeServiceByParameter(Integer userId,
			Integer serviceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("serviceId", serviceId);
		map.put("flag", "1");
		List<DredgeService> list = dredgeServiceService.getServiceRecordByUserId(map);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}else {
		  return null;
		}
	}
	
	public Integer getOepnUserId(User user){
		boolean flag = true;
		Map map = new HashMap();
		map.put("userId",user.getId());
		map.put("serviceName", "短信服务");
		//若当前用户已开通短信服务，则返回true;若没有开通，继续判断父账号是否开通短信服务 
		flag = dredgeServiceService.getServiceByUserIdAndServiceName(map);
		if(flag){
			return user.getId();
		}else {
			if(user.getParentId() != null){
				User users = userService.getUserById(user.getParentId());
				if(users!=null){
					return getOepnUserId(users);
				}	
			}
		}
		

//		//平台用户子账号类型：B-平台用户分仓子账号
//		if(user.getChildType() != null && "1".equals(user.getUserType()) && user.getUserSource()!=null && user.getChildType().equals("B"))
//		{
//			User users = userService.getUserById(Integer.parseInt(user.getUserSource()));
//			if(users!=null){
//				return getOepnUserId(users);
//			}	
//		}
		return user.getId();
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
	
	public AccountUserService<AccountUser> getAccountUserService() {
		return accountUserService;
	}

	public void setAccountUserService(
			AccountUserService<AccountUser> accountUserService) {
		this.accountUserService = accountUserService;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUseBalance(Double useBalance) {
		this.useBalance = useBalance;
	}
    @JSON(name="userBanlance")
	public Double getUseBalance() {
		return useBalance;
	}
	
	public PaymentService<Payment> getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService<Payment> paymentService) {
		this.paymentService = paymentService;
	}

	public PayServiceService<PayService> getPayServiceService() {
		return payServiceService;
	}

	public void setPayServiceService(PayServiceService<PayService> payServiceService) {
		this.payServiceService = payServiceService;
	}

	public DredgeServiceService<DredgeService> getDredgeServiceService() {
		return dredgeServiceService;
	}

	public void setDredgeServiceService(
			DredgeServiceService<DredgeService> dredgeServiceService) {
		this.dredgeServiceService = dredgeServiceService;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	
	@JSON
	public AccountUser getAccountUser() {
		return accountUser;
	}

	public void setAccountUser(AccountUser accountUser) {
		this.accountUser = accountUser;
	}
	
	public Integer getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Integer smsCount) {
		this.smsCount = smsCount;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
