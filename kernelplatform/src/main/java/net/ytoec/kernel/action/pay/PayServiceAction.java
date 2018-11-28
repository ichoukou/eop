package net.ytoec.kernel.action.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.CommonUtil;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.AlipayConfigMessage;
import net.ytoec.kernel.service.AccountUserService;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.ServiceHistoryService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PayServiceAction extends AbstractActionSupport {
	private static Logger logger = LoggerFactory.getLogger(PayServiceAction.class);
	@Inject
	private DredgeServiceService<DredgeService> dredgeServiceService;// 开通服务service
	@Inject
	private PayServiceService<PayService> payServiceService;// 服务service
	@Inject
	private AccountUserService<AccountUser> accountUserService;// 账户service
	@Inject
	private PaymentService<Payment> paymentService;// 支付明细
	@Inject
	private ServiceHistoryService<ServiceHistory> serviceHistoryService;// 服务历史
	@Inject
	private SMSServiceService smsServiceService;
	@Inject
    private SMSPortService smsPortService;
	@Inject
    private SMSObjectService<SMSObject> SMSObjectService;
	@Inject
	private UserService<User> userService;
	@Inject
	private MessageService<Message> messageService;

	private AccountUser accountUser;// 账户
	private Payment payment = new Payment();
	private List<DredgeService> dredgeList;// 已经开通的服务列表
	private List<PayService> payServicesList;// 开通的服务列表
	private List<AccountUser> accountUserList;// 账户信息
	private String response;
    
	private List<String> payList = new ArrayList<String>();
	private List<DredgeService> payFreeList;//免费的服务
    
	private Integer userId;
	private Double useBalance;//用户的可用余额
	private String phone;// 手机号
	private boolean flag;//用于处理特殊号码
	private boolean update_flag;//用于处理特殊号码
	private String phoneCode;// 验证码
	private Integer serviceId;// 服务id
    private Map<String, Object> paraMap = new HashMap<String, Object>();
	private Set<Integer> serviceIdsChecked = new HashSet<Integer>();// 由未选中到选中
	private Set<Integer> serviceIdsNoChecked = new HashSet<Integer>();// 有选中到未选中
	private static final String allChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Double money;// 金额
	private String dealName;//交易名称
	private String startTime = DateUtil.getDateStr(DateUtil.getCireclDate(new Date(), -3), "yyyy-MM-dd");
	private Pagination<Payment> pagination;
	private String endTime = DateUtil.toDay(new Date());//查询截止时间
	private Integer currentPage=1; //当前页
	private Integer pageNum = 20;
	private String menuFlag;
	private Integer id;//交易号
	private boolean serveExpir;//服务到期提醒是否被选中
	private boolean smsInadequate;//短信不足提醒
	private boolean balanceRemind;//余额不足提醒
	private boolean transactionRemind;//交易提醒
	private Integer smsRemindCount;//短信不足条数
	private Double balanceCound;//余额不足提醒
	private String isAutoRenew;
	private String circle;//周期
	private Double resetPay;//支付宝应付金额
    private Boolean isSaveSuccess;//提醒是否保存成功
    private Boolean isMatch;//手机号是否匹配
    private Integer isCheck;//是否是验证手机,获取校验码(0 未绑定时;1修改的时候)
    private String smsFlag;
    private String source;
    
	/**
     * 跳转区分使用 
     */
    private String pos;
    /**
     * 分页数,返回页面使用
     */
    private String currentPage2;
    
	/**
	 * 智能查件 查询参数  跳转返回使用
	 */
    private String logisticsIds;
    private String num;
    private String isCheck2;
    
    /**
     * 问题件 查询参数  跳转返回使用
     */
    private String backStratDate;
    private String backEndDate;
    private String backShopName;
    private String backQType;
    private String backQTypeVal;
    private String backIsShowSigned;
    private String backInput;
    private String backTabStatus;
    

    private String dealType="-1";//交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信       -1 请选择--

	
	/**
	 * 初始化查询 根据userId 以及 serviceId 查询列表信息
	 * 
	 * @return
	 */
	public String index() {
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		
		/**
		 * 判断是平台用户，还是分仓账号
		 * 如果是分仓账号，判断是否已经开通，如果没有开通，
		 * 判断父账号是否开通
		 */
		List<DredgeService> dList = new ArrayList<DredgeService>();//得到子账号已经开通的
		List<DredgeService> pdList = new ArrayList<DredgeService>();//得到父账号已经开通的
	    List<PayService> pcList = new ArrayList<PayService>();//得到父账号未开通的服务
	    List<Integer> nList = new ArrayList<Integer>();//存放服务的id
		dredgeList = new ArrayList<DredgeService>();//已开通服务列表
		dList = dredgeServiceService.getOpenserviceList(userId);
		//如果是分仓账号
		if(currentUser.getChildType() != null && currentUser.getUserType().equals("1") && currentUser.getChildType().equals("B")){
			if(!CollectionUtils.isEmpty(dList)){
				for(DredgeService ds : dList){
					PayService ps =new PayService();
					ps.setId(ds.getServiceId());
			        ps = payServiceService.get(ps);
			        ds.setServiceName(ps.getName());
			        ds.setRemark(ps.getRemark());
			        ds.setOpenFromFlag("0");
			        if(ds.getBeginDate() != null && ds.getEndDate() !=  null){
			        	 ds.setDays(DateUtil.getDays(new Date(), ds.getEndDate()));
			        }
			        dredgeList.add(ds);
			        nList.add(ds.getServiceId());
				}
				//查询平台账号中除了公司分仓账号里面开通的服务还有没有其他的服务
				
				List<DredgeService> ptdList = new ArrayList<DredgeService>();
				if(currentUser.getUserSource() != null && currentUser.getUserSource() != ""){
					ptdList = dredgeServiceService.getOpenserviceListByName(Integer.parseInt(currentUser.getUserSource()), nList);
				}
				if(CollectionUtils.isNotEmpty(ptdList)){
					for(DredgeService dres : ptdList){
						PayService ps =new PayService();
						ps.setId(dres.getServiceId());
				        ps = payServiceService.get(ps);
				        dres.setServiceName(ps.getName());
				        dres.setRemark(ps.getRemark());
				        dres.setOpenFromFlag("1");
				        if(dres.getBeginDate() != null && dres.getEndDate() !=  null){
				        dres.setDays(DateUtil.getDays(new Date(), dres.getEndDate()));
				        }
				        dredgeList.add(dres);
					}
					//dredgeList.addAll(ptdList);
				}
			}else {
				if(currentUser.getUserSource() != null && currentUser.getUserSource() != ""){
					pdList = dredgeServiceService.getOpenserviceList(Integer.parseInt(currentUser.getUserSource()));
				}
				if(CollectionUtils.isNotEmpty(pdList)){
					for(DredgeService ds : pdList){
						PayService ps =new PayService();
						ps.setId(ds.getServiceId());
				        ps = payServiceService.get(ps);
				        ds.setServiceName(ps.getName());
				        ds.setRemark(ps.getRemark());
				        ds.setOpenFromFlag("1");
				        if(ds.getBeginDate() != null && ds.getEndDate() !=  null){
				            ds.setDays(DateUtil.getDays(new Date(), ds.getEndDate()));
				        }
				        dredgeList.add(ds);
					}
				}
			}
			payServicesList = payServiceService.getNOpenserviceList(userId);//得到未开通的服务
			pcList = payServiceService.getNOpenserviceList(Integer.parseInt(currentUser.getUserSource()));//得到父账号未开通的账号
			//取两个集合的交集
			payServicesList.retainAll(pcList);
		}else {
			if(!CollectionUtils.isEmpty(dList)){
				for(DredgeService ds : dList){
					PayService ps =new PayService();
					ps.setId(ds.getServiceId());
			        ps = payServiceService.get(ps);
			        ds.setRemark(ps.getRemark());
			        ds.setServiceName(ps.getName());
			        if(ds.getBeginDate() != null && ds.getEndDate() !=  null){
			            ds.setDays(DateUtil.getDays(new Date(), ds.getEndDate()));
			        }
			        dredgeList.add(ds);
				}
			}
			payServicesList = payServiceService.getNOpenserviceList(userId);//得到未开通的服务
		}
		//List<DredgeService> dList = dredgeServiceService.getOpenserviceList(userId);//得到已开通
		//payServicesList = payServiceService.getNOpenserviceList(userId);//得到未开通的服务
		Integer[] serviceInt = {3,4,1,2};//serviceId 1、2、3、4 为4个免费服务
		payFreeList = new ArrayList<DredgeService>();
		for(int i=0;i<serviceInt.length;i++){
			DredgeService dredgs = new DredgeService();
			dredgs = getDredgeServiceByParameter(userId, serviceInt[i]);
			if(dredgs != null){
				payFreeList.add(dredgs);//免费服务
			}
		}
		//得到免费的服务
        List<Payment> list = new ArrayList<Payment>();
        List<PayService> nameList = new ArrayList<PayService>();//存取付费服务(短信服务除外)
        nameList = payServiceService.getNFreeserviceList();
        List<Payment> paymentListNoPay = new ArrayList<Payment>();//查询未付款的交易明细表
		if(!CollectionUtils.isEmpty(nameList)){
			for(PayService ps:nameList){
				Payment pm = new Payment();
				Map<String, Object> paymap = new HashMap<String, Object>();
				paymap.put("userId", userId);
				paymap.put("dealStatus","0");//未付款
				paymap.put("delFlag", "0");//未关闭
				paymap.put("dealName", ps.getName());//交易名称
				list = paymentService.getList(paymap);//根据上面的条件进行查询 之后得到最大的值 就是当前的订单的状态
				if(!CollectionUtils.isEmpty(list)){
					 pm = list.get(0);
					 //获取服务名称：交易名称（如：开通时效提醒，续订时效提醒）截取后4个字符（这是由于后期修改了交易名称，不得不这样做）
					 pm.setDealName(pm.getDealName().substring(2));
					payList.add(pm.getDealName());//得到交易名称
				}
			}
		}
		accountUser = accountUserService.getAccountUserByUserId(userId);// 根据userId得到用户信息
		if(accountUser != null){
			if(StringUtils.isNotEmpty(accountUser.getCellPhone())){
				String cellPhone = accountUser.getCellPhone();
				accountUser.setCellPhone(cellPhone.substring(0,3)+"****"+cellPhone.substring((cellPhone.length()-4)));
			}
		}
		return "toIndex";
	}

	public String indexService() {
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		/**
		 * 判断是平台用户，还是分仓账号
		 * 如果是分仓账号，判断是否已经开通，如果没有开通，
		 * 判断父账号是否开通
		 */
		List<DredgeService> dList = new ArrayList<DredgeService>();//得到子账号已经开通的
		List<DredgeService> pdList = new ArrayList<DredgeService>();//得到父账号已经开通的
	    List<PayService> pcList = new ArrayList<PayService>();//得到父账号未开通的服务
	    List<Integer> nList = new ArrayList<Integer>();//存放服务的名称
		dredgeList = new ArrayList<DredgeService>();//已开通服务列表
		dList = dredgeServiceService.getOpenserviceList(userId);
		//如果是分仓账号
		if(currentUser.getChildType() != null && currentUser.getUserType().equals("1") && currentUser.getChildType().equals("B")){
			if(!CollectionUtils.isEmpty(dList)){
				for(DredgeService ds : dList){
					PayService ps =new PayService();
					ps.setId(ds.getServiceId());
			        ps = payServiceService.get(ps);
			        ds.setServiceName(ps.getName());
			        ds.setOpenFromFlag("0");
			        ds.setRemark(ps.getRemark());
			        if(ds.getBeginDate() != null && ds.getEndDate() !=  null){
			        	 ds.setDays(DateUtil.getDays(new Date(), ds.getEndDate()));
			        }
			        dredgeList.add(ds);
			        nList.add(ds.getServiceId());
				}
				//查询平台账号中除了公司分仓账号里面开通的服务还有没有其他的服务
				
				List<DredgeService> ptdList = new ArrayList<DredgeService>();
				if(currentUser.getUserSource() != null && currentUser.getUserSource() != ""){
					ptdList = dredgeServiceService.getOpenserviceListByName(Integer.parseInt(currentUser.getUserSource()), nList);
				}
				if(CollectionUtils.isNotEmpty(ptdList)){
					for(DredgeService dres : ptdList){
						PayService ps =new PayService();
						ps.setId(dres.getServiceId());
				        ps = payServiceService.get(ps);
				        dres.setServiceName(ps.getName());
				        dres.setOpenFromFlag("1");
				        dres.setRemark(ps.getRemark());
				        if(dres.getBeginDate() != null && dres.getEndDate() !=  null){
				        	dres.setDays(DateUtil.getDays(new Date(), dres.getEndDate()));
				        }
				        dredgeList.add(dres);
					}
					//dredgeList.addAll(ptdList);
				}
			}else {
				if(currentUser.getUserSource() != null && currentUser.getUserSource() != ""){
					pdList = dredgeServiceService.getOpenserviceList(Integer.parseInt(currentUser.getUserSource()));
				}
				if(CollectionUtils.isNotEmpty(pdList)){
					for(DredgeService ds : pdList){
						PayService ps =new PayService();
						ps.setId(ds.getServiceId());
				        ps = payServiceService.get(ps);
				        ds.setServiceName(ps.getName());
				        ds.setOpenFromFlag("1");
				        ds.setRemark(ps.getRemark());
				        ds.setServiceName(ps.getName());
				        if(ds.getBeginDate() != null && ds.getEndDate() !=  null){
				             ds.setDays(DateUtil.getDays(new Date(), ds.getEndDate()));
				        }
				        dredgeList.add(ds);
					}
				}
			}
			payServicesList = payServiceService.getNOpenserviceList(userId);//得到未开通的服务
			pcList = payServiceService.getNOpenserviceList(Integer.parseInt(currentUser.getUserSource()));//得到父账号未开通的账号
			//取两个集合的交集
			payServicesList.retainAll(pcList);
		}else {
			if(!CollectionUtils.isEmpty(dList)){
				for(DredgeService ds : dList){
					PayService ps =new PayService();
					ps.setId(ds.getServiceId());
			        ps = payServiceService.get(ps);
			        ds.setRemark(ps.getRemark());
			        ds.setServiceName(ps.getName());
			        if(ds.getBeginDate() != null && ds.getEndDate() !=  null){
			             ds.setDays(DateUtil.getDays(new Date(), ds.getEndDate()));
			        }
			        dredgeList.add(ds);
				}
			}
			payServicesList = payServiceService.getNOpenserviceList(userId);//得到未开通的服务
		}
		//List<DredgeService> dList = dredgeServiceService.getOpenserviceList(userId);//得到已开通
		//payServicesList = payServiceService.getNOpenserviceList(userId);//得到未开通的服务
		Integer[] serviceInt = {3,4,1,2};//serviceId 1、2、3、4 为4个免费服务
		payFreeList = new ArrayList<DredgeService>();
		for(int i=0;i<serviceInt.length;i++){
			DredgeService dredgs = new DredgeService();
			dredgs = getDredgeServiceByParameter(userId, serviceInt[i]);
			if(dredgs != null){
				payFreeList.add(dredgs);//免费服务
			}
		}
		//得到免费的服务
        List<Payment> list = new ArrayList<Payment>();
        List<PayService> nameList = new ArrayList<PayService>();//存取付费服务(短信服务除外)
        nameList = payServiceService.getNFreeserviceList();
        List<Payment> paymentListNoPay = new ArrayList<Payment>();//查询未付款的交易明细表
		if(!CollectionUtils.isEmpty(nameList)){
			for(PayService ps:nameList){
				Payment pm = new Payment();
				Map<String, Object> paymap = new HashMap<String, Object>();
				paymap.put("userId", userId);
				paymap.put("dealStatus","0");//未付款
				paymap.put("delFlag", "0");//未关闭
				paymap.put("dealName", ps.getName());//交易名称
				list = paymentService.getList(paymap);//根据上面的条件进行查询 之后得到最大的值 就是当前的订单的状态
				if(!CollectionUtils.isEmpty(list)){
					 pm = list.get(0);
					payList.add(pm.getDealName());//得到交易名称
				}
			}
		}
		accountUser = accountUserService.getAccountUserByUserId(userId);// 根据userId得到用户信息
		if(accountUser != null){
			if(StringUtils.isNotEmpty(accountUser.getCellPhone())){
				String cellPhone = accountUser.getCellPhone();
				accountUser.setCellPhone(cellPhone.substring(0,3)+"****"+cellPhone.substring((cellPhone.length()-4)));
			}
		}
		return "toIndex";
	}
	/**
	 * 手机号绑定 手机号的修改 更新手机号之前首先判断检验码是否和输入的一致
	 * @returnR
	 */
	public String phoneBind() {
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		AccountUser aUser = accountUserService.getAccountUserByUserId(userId);// 得到账户信息
		boolean result = false;
		isMatch = false;
		if (aUser != null && (!StringUtils.isBlank(aUser.getPhoneCode()))) {
			if((!StringUtils.isEmpty(phone)) && aUser.getPhoneCode() != null){
				if (aUser.getPhoneCode().equalsIgnoreCase(phoneCode) && initStartDate >=0) {
					if(flag){
						phone="18"+phone.substring(2,phone.length());
					}
					aUser.setUserId(userId);
					aUser.setCellPhone(phone);//更新手机号
					result = accountUserService.updateAccountUserMobileByUserId(aUser);
					isMatch = true;
				}
			}
		} 
		return "phoneBind";
	}
	/**
	 * 第一步 核对手机号,以及校验码输入的是否正确 
	 * @return
	 */
	public String checkPhone(){
		
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		AccountUser aUser = accountUserService.getAccountUserByUserId(userId);// 得到账户信息
		boolean result = false;
		isMatch = false;
		if (aUser != null && (!StringUtils.isBlank(aUser.getPhoneCode()))) {
			if(aUser.getCellPhone() != null && aUser.getPhoneCode() != null){
				if (phoneCode.equalsIgnoreCase(aUser.getPhoneCode()) && StringUtils.equals(aUser.getCellPhone(), phone)&&initStartDate >= 0) {
					isMatch = true;
				}
			}
		} 
		return "checkPhone";
	}
	
	/**
	 * 手机号取消绑定
	 * @return
	 */
	public String cancelPhoneBind(){
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		AccountUser aUser = accountUserService.getAccountUserByUserId(userId);// 得到账户信息
		boolean result = true;
		isMatch = false;
		if(aUser != null){
			if(aUser.getCellPhone() != null){
				aUser.setCellPhone(null);
				aUser.setUserId(aUser.getUserId());
				result = accountUserService.cancelMobileBindByUserId(aUser);
			}
			if(result){
				isMatch = true;
				return "cancelPhoneBind";
			}
		}
		return "exception";
	}
	
    /**
     * 比较输入的手机号和原来的手机号是否相同
     * @return
     */
	public String phoneCompare(){
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		isMatch = false;
		accountUser = accountUserService.getAccountUserByUserId(userId);//得到账户信息 
		if(accountUser != null){
		  if(StringUtils.equals(accountUser.getCellPhone(), phone)){
			  isMatch = true;
		  }
		  return "phoneCompare";
		}
		return "exception";
	}
	/**
	 * 核对输入的手机校验码和手机上的校验码是否一致
	 * @return
	 */
	public String phoneCodeCompare(){
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		isMatch = false;
		accountUser = accountUserService.getAccountUserByUserId(userId);//得到账户信息 
		if(accountUser != null){
		  if(StringUtils.equalsIgnoreCase(accountUser.getPhoneCode(), phoneCode)){
			  isMatch = true;
		  }
		  return "phoneCodeCompare";
		}
		return "exception";
	}
	
	/**
	 * 如果超出时间（60秒）
	 * @return
	 */
	public String overTimePhoneCode(){
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		isMatch = false;
		accountUser = accountUserService.getAccountUserByUserId(userId);//得到账户信息 
		if(accountUser != null){
		  if(StringUtils.equalsIgnoreCase(accountUser.getPhoneCode(), phoneCode) && initStartDate < 0){
			  isMatch = true;
		  }
		  return "overTimePhoneCode";
		}
		return "exception";
	}
	
	/**
	 * 手机号校验手机校验码
	 */
	public void validatePhoneBind() {
		boolean result = CommonUtil.isMobilePhone(phone);//判断是否是手机号
		if(result == false){
			this.addActionError("手机号不符合规范");
		}
	}
  
	/**
	 * 得到校验码
	 * 
	 * @return
	 */
	public static String generateString() {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			str += allChar.charAt(random.nextInt(allChar.length()));
		}
		return str;
	}

	/**
	 * 更新账户表中的校验码
	 * 
	 * @return
	 */
	public void updatePhoneCode() {
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		AccountUser acUser = accountUserService.getAccountUserByUserId(userId);// 得到账户信息
		Map<String, Object> map = new HashMap<String, Object>();
		String[] mobiles = new String[]{phone};
		if (acUser == null) {
			acUser = new AccountUser();
			acUser.setUserId(userId);
			 accountUserService.addUserAndDService(acUser);
		}
		String code = generateString();// 获取手机检验码
		String smsContent;
		if(isCheck != 0){
		if(StringUtils.equals(acUser.getCellPhone(), phone)){
			logger.error("验证码发送开始=================begin==============");
			logger.error("验证码发送开始=================phone=============="+phone);
			smsContent = "申请修改绑定手机校验码" + code+"您于"+DateUtil.getDateStr(new Date(), "MM月dd日 HH:mm")+"进行操作。易通客服021-64703131";
	        SMSObject sMSObject=new SMSObject();
			sMSObject.setCreateTime(new Date());			 
			sMSObject.setMessageContent(smsContent);
			sMSObject.setDestMobile(phone);
			sMSObject.setSequenceID(0);
			sMSObject.setSmsType("0");
			sMSObject.setStatus("8");
			sMSObject.setUserId(acUser.getUserId());
	        SMSObjectService.add(sMSObject);
	        logger.error("验证码发送开始=================end==============");
			boolean result = false;
			if (!StringUtils.isBlank(code) && !StringUtils.isBlank(phone)) {
			  result = accountUserService.updateSendPhoneCode(userId, phone, code);
			}
		}
	  }else {
		  logger.error("验证码发送开始=================begin==============");
			logger.error("验证码发送开始=================phone=============="+phone);
			smsContent = "申请绑定手机校验码" + code+"您于"+DateUtil.getDateStr(new Date(), "MM月dd日 HH:mm")+"进行操作。易通客服021-64703131";
			  SMSObject sMSObject=new SMSObject();
				sMSObject.setCreateTime(new Date());			 
				sMSObject.setMessageContent(smsContent);
				sMSObject.setDestMobile(phone);
				sMSObject.setSequenceID(0);
				sMSObject.setSmsType("0");
				sMSObject.setStatus("8");
				sMSObject.setUserId(acUser.getUserId());
		        SMSObjectService.add(sMSObject);
	        logger.error("验证码发送开始=================end==============");
			boolean result = false;
			if (!StringUtils.isBlank(code) && !StringUtils.isBlank(phone)) {
			  result = accountUserService.updateSendPhoneCode(userId, phone, code);
			}
	  }
	}

	/**
	 * 更新提醒操作 首先要判断账户是否存在，如果不存在的话 要首先创建账户 已开通服务列表中添加4条记录 需要根据传入的 有勾选 到不勾选
	 * 这个时候状态是由1到4 这个需要放入到一个数组中 ; 如果由不勾选到勾选的话 是状态由 4到1的 这个需要放入到数组中 
	 * 更新的时候只是更新变化的东西
	 * @return
	 */
	public String updateRemindService() {
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		AccountUser au = accountUserService.getAccountUserByUserId(userId);
		if(au == null){
			au = new AccountUser();
			au.setUserId(userId);
			accountUserService.addUserAndDService(au);
		}
		List<DredgeService> list = new ArrayList<DredgeService>();
//		//首先要查询出来
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		Integer[] serviceids = {1,2,3,4};
		for(int i=0;i<serviceids.length;i++){
			DredgeService bdService = new DredgeService();
		     map.put("serviceId", serviceids[i]);
		     bdService = dredgeServiceService.getServiceRecordByUserId(map).get(0);
		     /**
		      * 如果之前是被选中的，说明状态是启用状态1
		      * 如果传入的值 是false 说明没有被选中，这个时候是要更新状态的4
		      */
		     if("1".equals(bdService.getFlag())){
		    	 if(serveExpir == false){serviceIdsNoChecked.add(3);}
		    	 if (smsInadequate == false) {serviceIdsNoChecked.add(4);} 
		    	 if (balanceRemind == false) {serviceIdsNoChecked.add(1);}
		    	 if (transactionRemind == false) {serviceIdsNoChecked.add(2);}
		    	 /**
		    	  * 如果之前是没有选中的，说明状态时删除状态 4
		    	  * 如果传入的值是true的话，说明这个被选中了，这个时候更新状态 为1
		    	  */
		     }else if ("4".equals(bdService.getFlag())) {
				if(serveExpir){serviceIdsChecked.add(3);}
				if(smsInadequate){serviceIdsChecked.add(4);}
				if(balanceRemind){serviceIdsChecked.add(1);}
				if(transactionRemind){serviceIdsChecked.add(2);}
			}
		}
		boolean result = true;
		if (!CollectionUtils.isEmpty(serviceIdsChecked)) {
			for (Integer serveId : serviceIdsChecked) {
				DredgeService ds = new DredgeService();
				ds.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 设置为1
				ds.setServiceId(serveId);
				ds.setUserId(userId);
				//ds.setBalance(balanceCound);//余额不足数目
				//ds.setSmsLess(smsRemindCount);//短信不足条数
				if(serveId == 1){
					ds.setBalance(balanceCound);//余额不足数据
				}else if (serveId == 4) {
					ds.setSmsLess(smsRemindCount);//短信不足条数
				}
				list.add(ds);
			}
		}else {
			if(CollectionUtils.isEmpty(serviceIdsNoChecked)){
				for(int i=0;i<serviceids.length;i++){
					 DredgeService bdService = new DredgeService();
				     map.put("serviceId", serviceids[i]);
				     bdService = dredgeServiceService.getServiceRecordByUserId(map).get(0);
				     if(i == 0){//余额不足提醒
				    	 if(balanceCound != bdService.getBalance()){bdService.setBalance(balanceCound); list.add(bdService);}
				     }else if (i==3) {
					     if(smsRemindCount !=bdService.getSmsLess()){bdService.setSmsLess(smsRemindCount);list.add(bdService);}
					}
				}     
			}
		}
		if (!CollectionUtils.isEmpty(serviceIdsNoChecked)) {
			for (Integer serveId:serviceIdsNoChecked) {
				DredgeService dService = new DredgeService();
				dService.setFlag(PayEnumConstants.SERVICEFLAG.USERDEL.getValue());// 设置为4
				dService.setServiceId(serveId);
				dService.setUserId(userId);
				if(serveId == 1){
					dService.setBalance(balanceCound);//余额不足数据
				}else if (serveId == 4) {
					dService.setSmsLess(smsRemindCount);//短信不足条数
				}
				list.add(dService);
			}
		}else {
			if(CollectionUtils.isEmpty(serviceIdsChecked)){
				for(int i=0;i<serviceids.length;i++){
					 DredgeService bdService = new DredgeService();
				     map.put("serviceId", serviceids[i]);
				     bdService = dredgeServiceService.getServiceRecordByUserId(map).get(0);
				     if(i == 0){//余额不足提醒
				    	 if(balanceCound != bdService.getBalance()){bdService.setBalance(balanceCound); list.add(bdService);}
				     }else if (i==3) {
					     if(smsRemindCount !=bdService.getSmsLess()){bdService.setSmsLess(smsRemindCount);list.add(bdService);}
					}
				}     
			}
		}				
		
		result = dredgeServiceService.updateBatch(list);
		if (result) {
			isSaveSuccess = true;
		}else{
			isSaveSuccess = false;
		}
		return "saveSuccess";
	}

	/**
	 * 立即使用 当点击立即使用的时候会跳转到开通服务页面(这个不包括短信服务)
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
		currentUser.setId(userId);
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
		/**
		 * 这个地方需要改动(有可能删除 )
		 */
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
			 if(smsFlag != null){
				paraMap.put("smsFlag", smsFlag); 
			 }else {
				 paraMap.put("smsFlag", "0");
			}
		 }
		if(currentUser.getShopName() != null){
			paraMap.put("shopName",currentUser.getShopName());//店铺名
		}else {
			paraMap.put("shopName",currentUser.getUserName());//用户名
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
			return "toApplicay";
		}
		return "exception";
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
		currentUser.setId(userId);
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
				if(usr != null && usr.getShopName() != null){
				  payList.add(usr.getShopName());
				}
			}
		}
		PayService ps = new PayService();
		ps.setId(serviceId);
		ps = payServiceService.get(ps);
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
				 paraMap.put("smsFlag", "0");
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
			return "toApplicayFromDetil";
		}
		return "exception";
	}
	
	/**
	 * 开通短信服务,并且立刻跳转到购买页面
	 * @return
	 */
	public String openSmsServiceAndToBuy() {
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		//1:开通短信服务
		serviceId = 5;  //短信服务类型ID
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("serviceId", serviceId);
		map.put("flag", "1");
		List<DredgeService> dredgeServiceList = dredgeServiceService.getServiceRecordByUserId(map);
		if(CollectionUtils.isEmpty(dredgeServiceList)){
			applySmsService();
		}
		//跳转购买页面
		return "toBuyPorts";
	}
  
	/**
     * 开通短信服务
     * 需要增加到交易明细中
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
		pment.setDealName(dealName);
		pment.setUserId(userId);// 得到用户id
		pment.setCreaterId(currentUser.getId());//操作人ID
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
				//dredgeServiceService.add(dService);
				map.put("dredgeService", dService);
				dredgeServiceService.openService(map, "4");//插入开通服务、交易明细表
			}else {
				dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
				dService.setId(dService.getId());
				dService.setBeginDate(new Date());
				map.put("dredgeService", dService);
			dredgeServiceService.openService(map, "5");
			
				
				//dredgeServiceService.updateFlagById(dService);
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
		return "toOpenSms";
	}
	
	/**
	 * 这个时候可能是续费，也可能是立即使用 当点击付款的时候 如果是立即使用的话
	 * 
	 * @return
	 */
	public String openService() {
		User currentuUser = super.readCookieUser();
		if (currentuUser != null) {
			userId = currentuUser.getId();
			if(currentuUser.getParentId() != null){
				userId = currentuUser.getParentId();
			}
		}
		AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
		DredgeService dService = getDredgeServiceByParameter(userId, serviceId);// 判断是否开通
		ServiceHistory serviceHistory = new ServiceHistory();
		/**
		 * 判断可用余额和服务要花费的费用进行比较 如果大于 直接付款，如果小于根据用户进行判断 如果大于余额的话 ，可以直接付款，这个时候需要更新
		 * 用户表 中的金额 更新 已开通服务 插入 交易明细表 插入 开通服务历史表
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		Date beginDate = new Date();// 得到当天的日期
		int month=1;
		if(StringUtils.equals(PayEnumConstants.CIRCLE.MONTH.getValue(),circle)){//月
			money =50.0;
			//money = 1.0;测试用。。。。。
			month=1;
		}else if(StringUtils.equals(PayEnumConstants.CIRCLE.SEASON.getValue(), circle)){//季
			money = 140.0;
			month=3;
		}else if(StringUtils.equals(PayEnumConstants.CIRCLE.HALFYEAR.getValue(), circle)){//半年
			money = 260.0;
			month=6;
		}else if(StringUtils.equals(PayEnumConstants.CIRCLE.YEAR.getValue(), circle)){//年
			money = 500.0;
			month=12+3;
		}

		Date enDate = DateUtil.getCireclDate(beginDate, month);
		boolean result = false;
		if (acUser.getUseBalance() >= money) { // 如果账户充足的话
			// 更新账户表
			acUser.setUseBalance(acUser.getUseBalance() - money);// 可用余额
			acUser.setBalance(acUser.getBalance() - money);//余额
			acUser.setAllConsume(money+acUser.getAllConsume());// 总消费增加 这个还有问题 需要改一下的getAllConsume
			acUser.setId(acUser.getId());
			map.put("accountUser", acUser);
			map.put("accountParentUser", null);
			// 开通服务表
			if (dService == null) {
				dService = new DredgeService();
				dService.setServiceId(serviceId);
				dService.setCircle(circle);//周期
				dService.setAutoRenew(isAutoRenew);//是否自动续费
				dService.setUserId(userId);
				dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 启用 1
				dService.setBeginDate(beginDate);// 开始日期
				dService.setEndDate(enDate);// 到期日期
				dService.setCost(money);// 费用
				
				map.put("dredgeService", dService);// 开通服务表

				// 插入交易明细 表
				//payment = new Payment();
				payment.setCreaterId(currentuUser.getId());
				payment.setDealName("开通"+dealName);
				payment.setUserId(userId);// 得到用户id
				payment.setAuserId(acUser.getId());// 得到账户id
				payment.setDealMoney(money);// 交易额
				payment.setPayTime(new Date());// 付款时间
				if(serviceId == 6){
					payment.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());// 交易类型 2 实效提醒
				}else {
					payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//订购服务
				}
				payment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
				payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);// 状态
				
				//插入服务历史记录表
				serviceHistory = new ServiceHistory();
				serviceHistory.setDserviceId(dService.getId());
				serviceHistory.setUserId(userId);
				serviceHistory.setCircle(dService.getCircle());// 周期
				serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
				serviceHistory.setEndDate(dService.getEndDate());// 到期时间
				serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
				serviceHistory.setDealType(payment.getDealType());// 交易类型
				serviceHistory.setDealName(payment.getDealName());// 服务名称
				serviceHistory.setCost(money);
				serviceHistory.setPromCost(money);// 促销费用
				
				map.put("payment", payment);
				map.put("serviceHistory", serviceHistory);// 服务历史表
				result = dredgeServiceService.openService(map, "2");
			} else {
				// 如果这个服务已经开通 这个时候更新数据(关闭之后再打开的情况下会用到,续费的时候也会用到的)
				if (PayEnumConstants.SERVICEFLAG.ENABLED.getValue().equals(dService.getFlag())) {// 续费 状态还是1
					dService.setEndDate(DateUtil.getCireclDate(dService.getEndDate(), month));// 这个需要在原来的到期日期加上周期
					dService.setCost(money+dService.getCost());
					//payment.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());//续费服务
					payment.setDealName("续费"+dealName);
				}else {
					dService.setBeginDate(beginDate);
					dService.setEndDate(enDate);
					//payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());// 订购服务
					payment.setDealName("开通"+dealName);
				}
				dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 更新到1
				
				dService.setCircle(circle);
				dService.setAutoRenew(isAutoRenew);
				map.put("dredgeService", dService);// 更新服务表
				// 插入交易明细 表
				payment.setCreaterId(currentuUser.getId());
				if(serviceId == 6){
					payment.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());//时效提醒
				}else {
					payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//订购服务
				}
				payment.setUserId(userId);// 得到用户id
				payment.setAuserId(acUser.getId());// 得到账户id
				payment.setDealMoney(money);// 交易额
				payment.setPayTime(new Date());// 付款时间
				payment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
				payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);// 状态  0 出账、1进账
				
				//插入服务历史记录表
				serviceHistory = new ServiceHistory();
				serviceHistory.setDserviceId(serviceId);
				serviceHistory.setUserId(userId);
				serviceHistory.setCircle(dService.getCircle());// 周期
				serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
				serviceHistory.setEndDate(dService.getEndDate());// 到期时间
				serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
				serviceHistory.setDealType(payment.getDealType());// 交易类型
				serviceHistory.setDealName(payment.getDealName());// 服务名称
				serviceHistory.setCost(money);
				serviceHistory.setPromCost(money);// 促销费用
				
				map.put("payment", payment);
				map.put("serviceHistory", serviceHistory);// 服务历史表
				result = dredgeServiceService.openService(map, "1");
			}
		} else {
			  //如果是分仓账号，账户余额不足，查询 平台用户的金额
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
					// 更新账户表(当前账户)
					 	acUser.setBalance(acUser.getBalance() - acUser.getUseBalance());//余额
					 	acUser.setAllConsume(acUser.getUseBalance()+acUser.getAllConsume());// 总消费增加 这个还有问题 需要改一下的getAllConsume
						acUser.setUseBalance(acUser.getUseBalance() - acUser.getUseBalance());// 可用余额
						acUser.setId(acUser.getId());
						map.put("accountUser", acUser);
						// 开通服务表
						if (dService == null) {
							dService = new DredgeService();
							dService.setServiceId(serviceId);
							dService.setCircle(circle);//周期
							dService.setAutoRenew(isAutoRenew);//是否自动续费
							dService.setUserId(userId);
							dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 启用 1
							dService.setBeginDate(beginDate);// 开始日期
							dService.setEndDate(enDate);// 到期日期
							dService.setCost(money);// 费用
							
							map.put("dredgeService", dService);// 开通服务表

							// 插入交易明细 表
							//payment = new Payment();
							payment.setCreaterId(currentuUser.getId());
							payment.setDealName("开通"+dealName);
							payment.setUserId(userId);// 得到用户id
							payment.setAuserId(acUser.getId());// 得到账户id
							payment.setDealMoney(money);// 交易额
							payment.setPayTime(new Date());// 付款时间
							if(serviceId == 6){
								payment.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());// 交易类型 2 实效提醒
							}else {
								payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//订购服务
							}
							payment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
							payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);// 状态
							
							//插入服务历史记录表
							serviceHistory = new ServiceHistory();
							serviceHistory.setDserviceId(dService.getId());
							serviceHistory.setUserId(userId);
							serviceHistory.setCircle(dService.getCircle());// 周期
							serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
							serviceHistory.setEndDate(dService.getEndDate());// 到期时间
							serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
							serviceHistory.setDealType(payment.getDealType());// 交易类型
							serviceHistory.setDealName(payment.getDealName());// 服务名称
							serviceHistory.setCost(money);
							serviceHistory.setPromCost(money);// 促销费用
							
							map.put("payment", payment);
							map.put("serviceHistory", serviceHistory);// 服务历史表
							result = dredgeServiceService.openService(map, "2");
						} else {
							// 如果这个服务已经开通 这个时候更新数据(关闭之后再打开的情况下会用到,续费的时候也会用到的)
							if (PayEnumConstants.SERVICEFLAG.ENABLED.getValue().equals(dService.getFlag())) {// 续费 状态还是1
								dService.setEndDate(DateUtil.getCireclDate(dService.getEndDate(), month));// 这个需要在原来的到期日期加上周期
								dService.setCost(money+dService.getCost());
								//payment.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());//续费服务
								payment.setDealName("续费"+dealName);
							}else {
								dService.setBeginDate(beginDate);
								dService.setEndDate(enDate);
								//payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());// 订购服务
								payment.setDealName("开通"+dealName);
							}
							dService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());// 更新到1
							
							dService.setCircle(circle);
							dService.setAutoRenew(isAutoRenew);
							map.put("dredgeService", dService);// 更新服务表
							// 插入交易明细 表
							payment.setCreaterId(currentuUser.getId());
							payment.setUserId(userId);// 得到用户id
							payment.setAuserId(acUser.getId());// 得到账户id
							payment.setDealMoney(money);// 交易额
							payment.setPayTime(new Date());// 付款时间
							if(serviceId == 6){
								payment.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());// 交易类型 2 实效提醒
							}else {
								payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//订购服务
							}
							payment.setDealStatus(PayEnumConstants.DEALSTATUS.SUCCESS.getValue());// 交易状态 成功
							payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);// 状态  0 出账、1进账
							
							//插入服务历史记录表
							serviceHistory = new ServiceHistory();
							serviceHistory.setDserviceId(serviceId);
							serviceHistory.setUserId(userId);
							serviceHistory.setCircle(dService.getCircle());// 周期
							serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
							serviceHistory.setEndDate(dService.getEndDate());// 到期时间
							serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
							serviceHistory.setDealType(payment.getDealType());// 交易类型
							serviceHistory.setDealName(payment.getDealName());// 服务名称
							serviceHistory.setCost(money);
							serviceHistory.setPromCost(money);// 促销费用
							
							map.put("payment", payment);
							map.put("serviceHistory", serviceHistory);// 服务历史表
							result = dredgeServiceService.openService(map, "1");
						}
				 }
				 else
				 {
					 return toAliPay(); 
				 }
			}else {
				return toAliPay(); 
			}
		}
		if (result) {
			//index();
			paymentService.sendSMSByUserId(userId,payment.getDealName(),dService, "");//发送余额提醒,交易提醒
            boolean messageResult = sendMessag(userId);
            if(!messageResult){
            	logger.error("余额不足消息提醒");
            }
			//若当前菜单是时效提醒，则跳转到时效提醒页面；否则跳转到服务管理页面
			if("chajian_passManage_warn".equals(this.source)||serviceId==6) {
				return "passManage_warn";
			}
			else {
				return "toOpen";
			}
			
		} 
			return "exception";
	}
	/**
	 * 去支付宝
	 * 
	 * @return
	 */
	public String toAliPay() {
		User currentuUser = super.readCookieUser();
		if (currentuUser != null) {
			userId = currentuUser.getId();
			if(currentuUser.getParentId() != null){
				userId = currentuUser.getParentId();
			}
		}
		//根据名称得到serviceId
		PayService ps = new PayService();
		ps.setName(dealName);
		ps = payServiceService.get(ps);
		if(serviceId == null){
		   serviceId = ps.getId();
		}
		AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
		DredgeService dService = getDredgeServiceByParameter(userId, serviceId);//得到已开通服务信息
		Date beginDate =new Date();// 得到当天的日期
		int month=1;
		if(StringUtils.equals(PayEnumConstants.CIRCLE.MONTH.getValue(),circle)){//月
			 money =50.0;
			 month=1;
		}else if(StringUtils.equals(PayEnumConstants.CIRCLE.SEASON.getValue(), circle)){//季
			money = 140.0;
			month=3;
		}else if(StringUtils.equals(PayEnumConstants.CIRCLE.HALFYEAR.getValue(), circle)){//半年
			money = 260.0;
			month=6;
		}else if(StringUtils.equals(PayEnumConstants.CIRCLE.YEAR.getValue(), circle)){//年
			money = 500.0;
			month=12+3;
		}

		Date enDate = DateUtil.getCireclDate(beginDate, month);
		if(dService == null){
			dService = new DredgeService();
			dService.setServiceId(serviceId);
			dService.setCircle(circle);//周期
			dService.setAutoRenew(isAutoRenew);//是否自动续费
			dService.setUserId(userId);
			dService.setFlag(PayEnumConstants.SERVICEFLAG.CREATE.getValue());// 创建
			dService.setBeginDate(beginDate);// 开始日期
			dService.setEndDate(enDate);// 到期日期
			dService.setCost(money);// 费用
			payment.setDealName("开通"+dealName);
			//payment.setFlag(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//是订购服务
			//payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//订购服务
		}else {
			 if (PayEnumConstants.SERVICEFLAG.ENABLED.getValue().equals(dService.getFlag())) {// 续费 状态还是1
				dService.setEndDate(DateUtil.getCireclDate(dService.getEndDate(), month));// 这个需要在原来的到期日期加上周期
				//payment.setDealType(PayEnumConstants.DEALTYPE.DISABLED.getValue());
				payment.setDealName("续费"+dealName);
			}else {
				payment.setDealName("开通"+dealName);
			}
			dService.setFlag(PayEnumConstants.SERVICEFLAG.CREATE.getValue());// 更新到1
			dService.setCost(money);
			dService.setId(dService.getId());
			dService.setCircle(circle);
			dService.setAutoRenew(isAutoRenew);
			dService.setUserId(userId);
		}
		//两个对象payment 、 dredgeService、serviceHistory
		Map<String, Object> alipay_map=new HashMap<String, Object>();
		    if(acUser.getUseBalance() == null){
		    	acUser.setUseBalance(0.00);
		    }
		    payment.setCreaterId(currentuUser.getId());
			payment.setUserId(userId);
			if(serviceId == 6){
				payment.setDealType(PayEnumConstants.DEALTYPE.REMAIND.getValue());// 交易类型 2 实效提醒
			}else {
				payment.setDealType(PayEnumConstants.DEALTYPE.ORDERSERVICE.getValue());//订购服务
			}
			payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.OUT.value);//出账
			payment.setDealMoney(money-acUser.getUseBalance());//支付宝应付金额
			payment.setAuserId(acUser.getId());
			
	    ServiceHistory serviceHistory = new ServiceHistory();
	    serviceHistory.setDserviceId(serviceId);
		serviceHistory.setUserId(userId);
		serviceHistory.setCircle(dService.getCircle());// 周期
		serviceHistory.setBeginDate(dService.getBeginDate());// 开始时间
		serviceHistory.setEndDate(dService.getEndDate());// 到期时间
		serviceHistory.setAutoRenew(dService.getAutoRenew());// 是否自动续费
		serviceHistory.setDealType(payment.getDealType());// 交易类型
		serviceHistory.setDealName(payment.getDealName());// 服务名称
		serviceHistory.setCost(money);
		serviceHistory.setPromCost(money);// 促销费用
		alipay_map.put("payment", payment);
		alipay_map.put("dredgeService", dService);
		alipay_map.put("serviceHistory", serviceHistory);
		AlipayConfigMessage alipayConfigMessage=getAlipayConfig();//支付宝账号的配置信息
		alipay_map.put("alipayConfigMessage", alipayConfigMessage);
		String payURLString =paymentService.dealPayment(alipay_map);
		request.setAttribute("alipayURL", payURLString);
		return "alipay";
	}

	/**
	 * 了解详情 
	 * 
	 * @return
	 */
	public String seeDetails() {
		User currentuUser = super.readCookieUser();
		if (currentuUser != null) {
			userId = currentuUser.getId();
			if(currentuUser.getParentId() != null){
				userId = currentuUser.getParentId();
			}
		}
		 //如果是已经存在的未付款的订单，点击开始使用的时候会到交易明细页面
	    List<Payment> list = new ArrayList<Payment>();
       // List<Payment> paymentListNoPay = new ArrayList<Payment>();//查询未付款的交易明细表
        PayService pservice = new PayService();
        pservice.setId(serviceId);
        pservice = payServiceService.get(pservice);
      
		Map<String, Object> paymap = new HashMap<String, Object>();
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
		return "toServiceDetail";
	}

	/**
	 * 关闭服务
	 * 
	 * @return
	 */
	public String closeService() {
		User currentUser = super.readCookieUser();
		if (currentUser != null) {
			userId = currentUser.getId();
			if(currentUser.getParentId()!= null){
				userId = currentUser.getParentId();
			}
		}
		boolean result = true;
		DredgeService dService = getDredgeServiceByParameter(userId, serviceId);
		AccountUser aUser = accountUserService.getAccountUserByUserId(userId);
		DredgeService service = new DredgeService();
		if (dService != null) {
			service.setId(dService.getId());
			service.setFlag(PayEnumConstants.SERVICEFLAG.USERDEL.getValue());// 用户关闭
			result = dredgeServiceService.updateFlagById(service);
		}
		String smsContent = "亲，你开通的服务于"+DateUtil.getDateStr(new Date(), "MM月dd日 ")+"关闭，请登录易通查看确认。【易通】";
		if(result && (aUser != null && aUser.getCellPhone() != null)){
	        SMSObject sMSObject=new SMSObject();
			sMSObject.setCreateTime(new Date());			 
			sMSObject.setMessageContent(smsContent);
			sMSObject.setDestMobile(aUser.getCellPhone());
			sMSObject.setSequenceID(0);
			sMSObject.setSmsType("0");
			sMSObject.setStatus("8");
			sMSObject.setUserId(userId);
	      boolean re =   SMSObjectService.add(sMSObject);
			return "closeService";
		}
		return "exception";
		//index();
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
	
	/**
	 * 关闭交易明细功能
	 * 交易状态	0等待支付、1正在充值、2 已支付、3失败、4已关闭 
	 * (关闭触发的事件)
	 * 注：需修改账户的可用余额
	 * @return
	 */
	@SuppressWarnings("all")
	public String closePayment()
	{
		boolean flag=true;		
		if(this.id!=null) {
			Payment payment=new Payment();
			payment.setId(this.id);
		  	payment=paymentService.get(payment);
		  	if(!StringUtils.equals(payment.getDealType(), PayEnumConstants.DEALTYPE.ONLINE.getValue())){
		  		//2)其他操作的关闭[即不是在线充值]
		     	//===================begin===================	
		  		Map historyMap=new HashMap();
				historyMap.put("userId", payment.getUserId());
				historyMap.put("payId", payment.getId());
				List<ServiceHistory> historyList= serviceHistoryService.getServiceHistoryListByDUserId(historyMap);
				if(CollectionUtils.isNotEmpty(historyList)){
					ServiceHistory serviceHistory=historyList.get(0);
					//关闭把可用余额[退回来]
					//3)修改可用余额ec_core_accountUser
					AccountUser accountUser = accountUserService.getAccountUserByUserId(payment.getUserId());		
					if (accountUser != null) {
						Map account_map = new HashMap();
						account_map.put("userId", accountUser.getUserId());
						account_map.put("updateTime", accountUser.getUpdateTime());
						//可用余额=可用余额+冻结的余额
						account_map.put("money",(accountUser.getUseBalance()+(serviceHistory.getPromCost()-payment.getDealMoney())));
						
						try {
							flag = accountUserService.updateUseBalanceByUserId(account_map);
						} catch (Exception e) {
							Log.error("关闭交易明细表失败：id=" +this.id);
							flag=false;
						}						
					}
				}
				//===================end===================	
		  	}
		  	if (BooleanUtils.isTrue(flag)) {
				//成功才修改状态
				payment.setDealStatus(PayEnumConstants.DEALSTATUS.CLOSED.getValue());
				payment.setDelFlag(PayEnumConstants.DEAL_FLAG.ALERADYDEL.value);
				paymentService.edit(payment);
			}
		  	    
		}
		if(flag)
		{
			response="关闭成功";
			return "closeOrder";
		}					
		else	
		{
			response="关闭失败";
			return "exception";		
		}	
	}
	
	/**
	 * 查询交易（支付）明细表列表
	 * @return
	 */
	public String getPaymentList(){
		Map<String,Object> map=new HashMap<String, Object>();	
		if(startTime!=null&&endTime!=null);
		{
			map.put("startTime", DateUtil.valueof(this.startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss"));
			map.put("endTime",  DateUtil.valueof(this.endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
		}
		
		User currentUser = super.readCookieUser();	
		
		List<Integer> userIdArray=new ArrayList<Integer>();
		//获取与user用户有关联关系的用户主账号id（不包括自己）
		if(currentUser.getUserType().equals("1")
				||currentUser.getUserType().equals("11")
				||currentUser.getUserType().equals("12")
				||currentUser.getUserType().equals("13"))
		{
			List<Integer>  userIdList= Resource.getUserRelationUserIdList(currentUser);
			if(userIdList!=null)
			{
				userIdArray = (List<Integer>)((ArrayList)userIdList).clone();
				if(currentUser.getParentId()!=null) {
					userIdArray.add(currentUser.getParentId());
				}
				else {
					userIdArray.add(currentUser.getId());
				}	
			}
		}
			 
		if(userIdArray!=null&&userIdArray.size()>0)//若存在关联店铺
		{
			map.put("userIdArray", userIdArray);
		}
		else
		{
			//若没有关联店铺，查询条件为主张号
			if(currentUser.getParentId()!=null) {
				map.put("userId", currentUser.getParentId());
			}
			else {
				map.put("userId", currentUser.getId());
			}		
		}
		
		if(pagination==null)
			pagination = new Pagination<Payment>(currentPage, pageNum);
		
		//添加交易类型
		if(!this.dealType.equals("-1")){
			map.put("dealType", this.dealType);
		}
		
		//获取总记录数
		List<Payment>  recordCount = paymentService.getPaymentList(map, pagination, false);
		if(recordCount!=null&&recordCount.size()>0)
		{
		    this.pagination.setTotalRecords(recordCount.size());
		}
		
		//获取分页显示的记录数
		List<Payment>  records = paymentService.getPaymentList(map, pagination, true);
		
		this.pagination.setRecords(records);	
		
		if(id!=null) {//用来判断当前此条交易明细是否已付款
			Payment p=new Payment();
			p.setId(id);
			p = paymentService.get(p);
			if(p.getDealStatus().equals("2")){//若已经付款，id设置为null
				id = null;
			}
		}		
		
		return "success";
	}
	
	/**
	 * 充值
	 *  这个时候需要跳转到支付页面
	 * @return
	 */
	public String toSupplement(){
	  User currentUser = super.readCookieUser();
	  boolean result = true;
//	  if(currentUser != null){
//		  userId = currentUser.getId();
//	  }
	  Integer mainAccountId = -1;//主账号的id
	  if(currentUser.getParentId()!=null) {
		 mainAccountId = currentUser.getParentId();
	  }
	  else {
		 mainAccountId = currentUser.getId();
	  }
		
	  //AccountUser acUser = accountUserService.getAccountUserByUserId(userId);
	  AccountUser acUser = accountUserService.getAccountUserByUserId(mainAccountId);
	  if(acUser == null){
		  acUser = new AccountUser();
		  //acUser.setUserId(userId);
		  acUser.setUserId(mainAccountId);
		  result = accountUserService.addUserAndDService(acUser);
	  }
	  if(result){
		  return "toSupplement";
	  }
		return "exception";
	}
	
	/**
	 * 支付或者关闭
	 * @return
	 */
	public String payOrClose(){
		User currentUser = super.readCookieUser();
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId()!=null)
				userId = currentUser.getParentId();
		}
		PayService pService = new PayService();
		pService.setId(serviceId);		
		pService = payServiceService.get(pService);
		
		Payment pm = new Payment();
		Map<String, Object> paymap = new HashMap<String, Object>();
		List<Payment> list = new ArrayList<Payment>();
		paymap.put("userId", userId);
		paymap.put("dealStatus","0");//未付款
		paymap.put("delFlag", "0");//未关闭
		paymap.put("dealName",pService.getName());//交易名称
		list = paymentService.getList(paymap);//根据上面的条件进行查询 之后得到最大的值 就是当前的订单的状态
		if(!CollectionUtils.isEmpty(list)){
			pm = list.get(0);
			paraMap.put("id", pm.getId());
		}
		
		if(pagination==null)
			pagination = new Pagination<Payment>(currentPage, pageNum);
	    
		paraMap.put("userId", userId);
		
		//获取总记录数
		List<Payment>  recordCount = paymentService.getPaymentList(paraMap, pagination, false);
		if(recordCount!=null&&recordCount.size()>0)
		{
		    this.pagination.setTotalRecords(recordCount.size());
		}
		//获取分页显示的记录数
		List<Payment>  records = paymentService.getPaymentList(paraMap, pagination, true);
		this.pagination.setRecords(records);		
		//根据paymentId查询交易明细
		Payment pment = new Payment();
		pment.setId(id);
		pment = paymentService.get(pment);
		if(pment != null){
		   paraMap.put("paymentId", pment.getId());
		}
		return "success";
	}
	
	/**
	 * 得到可用余额
	 * @return
	 */
	public String getBalance(){
		User currentUser = super.readCookieUser();
		Double balance = 0.0;
		if(currentUser != null){
			userId = currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
    	accountUser = accountUserService.getAccountUserByUserId(userId);
		useBalance = accountUser.getUseBalance();//得到账户余额
		if(currentUser.getUserName()==null){
			currentUser.setUserName("");
		}
		if(currentUser.getChildType() != null && "1".equals(currentUser.getUserType()) && currentUser.getChildType().equals("B")){
	    	   AccountUser aUser = accountUserService.getAccountUserByUserId(Integer.parseInt(currentUser.getUserSource()));
	         if(aUser != null && aUser.getUseBalance() != null){
	        	 balance = aUser.getUseBalance();
	         }
	     }
		accountUser.setUseBalance(useBalance);
		accountUser.setBalance(balance);
		accountUser.setUserName(currentUser.getUserName());
		return "getUseBalance";
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
	
	//跳转到支付帮助页面
	public String rechargeHelp()
	{
		return "toRechargeHelp";
	}
	
	/**
	 * 短信注册
	 * @return
	 */
	public String registerSms(){
		logger.error("============== 短信引擎连接初始开始 =========");
		/*int i = smsPortService.registEx(ConfigUtilSingle.getInstance().getEMAY_SOFTWARESERIALNO(),
				                  ConfigUtilSingle.getInstance().getEMAY_KEY(),
				                  ConfigUtilSingle.getInstance().getEMAY_PASSOWRD());*/
		return "registerSms";
	}
	
	/**
	 * 发送消息
	 * @param userId 主账号ID
	 */
	public boolean sendMessag(Integer userId){
		String messageTheme ="余额不足提醒";
		Double balanceResult = paymentService.sendMessageByUserId(userId);
		boolean result = false;
		if(balanceResult != null){
			String messageContent = "亲，截止"+DateUtil.getDateStr(new Date(), "yyyy年MM月dd日HH时 ")+"，你的易通账户余额不足"+balanceResult+"元，为了你购买服务支付便捷，请及时充值。去这<a href='alipay_toRechargeOnline.action?menuFlag=caiwu_alipay'>[充值]</a>";
			User curUser = userService.getUserById(userId);
			if("1".equals(curUser.getUserType())){
				User adminUser =  userService.getUserByUserName("admin");
				List<String> receList = new ArrayList<String>();
				if(curUser.getUserCode() != null && curUser.getUserCode() != ""){
					receList.add(curUser.getUserCode());
					result = messageService.sendMessage(adminUser, messageTheme, messageContent, receList, 1);
				}
			}
		}
		return result;
	}

	public AccountUser getAccountUser() {
		return accountUser;
	}

	public void setAccountUser(AccountUser accountUser) {
		this.accountUser = accountUser;
	}

	public List<DredgeService> getDredgeList() {
		return dredgeList;
	}

	public void setDredgeList(List<DredgeService> dredgeList) {
		this.dredgeList = dredgeList;
	}

	public List<PayService> getPayServicesList() {
		return payServicesList;
	}

	public void setPayServicesList(List<PayService> payServicesList) {
		this.payServicesList = payServicesList;
	}

	public List<AccountUser> getAccountUserList() {
		return accountUserList;
	}

	public void setAccountUserList(List<AccountUser> accountUserList) {
		this.accountUserList = accountUserList;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
    
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Pagination<Payment> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<Payment> pagination) {
		this.pagination = pagination;
	}
	
	public Map<String, Object> getParaMap() {
		return paraMap;
	}

	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}
	
	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
	
	public boolean isServeExpir() {
		return serveExpir;
	}

	public void setServeExpir(boolean serveExpir) {
		this.serveExpir = serveExpir;
	}

	public boolean isSmsInadequate() {
		return smsInadequate;
	}

	public void setSmsInadequate(boolean smsInadequate) {
		this.smsInadequate = smsInadequate;
	}

	public boolean isBalanceRemind() {
		return balanceRemind;
	}

	public void setBalanceRemind(boolean balanceRemind) {
		this.balanceRemind = balanceRemind;
	}

	public boolean isTransactionRemind() {
		return transactionRemind;
	}

	public void setTransactionRemind(boolean transactionRemind) {
		this.transactionRemind = transactionRemind;
	}

	public Integer getSmsRemindCount() {
		return smsRemindCount;
	}

	public void setSmsRemindCount(Integer smsRemindCount) {
		this.smsRemindCount = smsRemindCount;
	}

	public Double getBalanceCound() {
		return balanceCound;
	}

	public void setBalanceCound(Double balanceCound) {
		this.balanceCound = balanceCound;
	}

	public String getIsAutoRenew() {
		return isAutoRenew;
	}

	public void setIsAutoRenew(String isAutoRenew) {
		this.isAutoRenew = isAutoRenew;
	}
	
	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}
	
	public List<DredgeService> getPayFreeList() {
		return payFreeList;
	}

	public void setPayFreeList(List<DredgeService> payFreeList) {
		this.payFreeList = payFreeList;
	}

	public void setUseBalance(Double useBalance) {
		this.useBalance = useBalance;
	}

	public Double getUseBalance() {
		return useBalance;
	}

	public Double getResetPay() {
		return resetPay;
	}

	public void setResetPay(Double resetPay) {
		this.resetPay = resetPay;
	}
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
    
	public Boolean getIsSaveSuccess() {
		return isSaveSuccess;
	}

	public void setIsSaveSuccess(Boolean isSaveSuccess) {
		this.isSaveSuccess = isSaveSuccess;
	}
	
	public List<String> getPayList() {
		return payList;
	}

	public void setPayList(List<String> payList) {
		this.payList = payList;
	}

	public Boolean getIsMatch() {
		return isMatch;
	}
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	public void setIsMatch(Boolean isMatch) {
		this.isMatch = isMatch;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public boolean isUpdate_flag() {
		return update_flag;
	}

	public void setUpdate_flag(boolean update_flag) {
		this.update_flag = update_flag;
	}
	
	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	private Integer initStartDate;//开始时间
    
	public Integer getInitStartDate() {
		return initStartDate;
	}

	public void setInitStartDate(Integer initStartDate) {
		this.initStartDate = initStartDate;
	}
	
	public String getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(String smsFlag) {
		this.smsFlag = smsFlag;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getDealType() {
		return dealType;
	}


	public String getCurrentPage2() {
		return currentPage2;
	}

	public void setCurrentPage2(String currentPage2) {
		this.currentPage2 = currentPage2;
	}

	public String getLogisticsIds() {
		return logisticsIds;
	}

	public void setLogisticsIds(String logisticsIds) {
		this.logisticsIds = logisticsIds;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getIsCheck2() {
		return isCheck2;
	}

	public void setIsCheck2(String isCheck2) {
		this.isCheck2 = isCheck2;
	}


	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getBackStratDate() {
		return backStratDate;
	}

	public void setBackStratDate(String backStratDate) {
		this.backStratDate = backStratDate;
	}

	public String getBackEndDate() {
		return backEndDate;
	}

	public void setBackEndDate(String backEndDate) {
		this.backEndDate = backEndDate;
	}

	public String getBackShopName() {
		return backShopName;
	}

	public void setBackShopName(String backShopName) {
		this.backShopName = backShopName;
	}

	public String getBackQTypeVal() {
		return backQTypeVal;
	}

	public void setBackQTypeVal(String backQTypeVal) {
		this.backQTypeVal = backQTypeVal;
	}

	public String getBackIsShowSigned() {
		return backIsShowSigned;
	}

	public void setBackIsShowSigned(String backIsShowSigned) {
		this.backIsShowSigned = backIsShowSigned;
	}

	public String getBackInput() {
		return backInput;
	}

	public void setBackInput(String backInput) {
		this.backInput = backInput;
	}

	public String getBackTabStatus() {
		return backTabStatus;
	}

	public void setBackTabStatus(String backTabStatus) {
		this.backTabStatus = backTabStatus;
	}

	public String getBackQType() {
		return backQType;
	}

	public void setBackQType(String backQType) {
		this.backQType = backQType;
	}
	public String getDealName() {
		return dealName;
	}

	public void setDealName(String dealName) {
		this.dealName = dealName;
	}
   
	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}
}
