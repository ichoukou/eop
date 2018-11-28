package net.ytoec.kernel.action.sms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.SMSInfoExpand;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.FilterRuleService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SMSTemplateService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**
 * 短信查询Action 
 * @author wangjianzhong
 * @since 2012-07-18
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SmsSearchAction extends AbstractActionSupport{
	/*
	 * LOG日志
	 */
	private static Logger logger = LoggerFactory.getLogger(SmsSearchAction.class);
	
	@Inject
	private SMSTemplateService smsTemplateService;
	@Inject
	private SMSServiceService smsServiceService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private UserService<User> userService;
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private UserDao userDao;
	@Inject
	private FilterRuleService  filterRuleService;
	@Inject
	private MessageService<Message> messageService;
	
	private Integer currentPage = 1;
	private Pagination pagination; //分页对象
	
	/**
	 * 查询条件
	 */
	private String smsTypeId;     //短信类型ID
	 
	private String smsTemplateId;  //短信模版ID
	
	private String sendStatus;   //发送状态
	
	private String dateStart = startDate();   //开始日期
	
	private String dateEnd = DateUtil.format(new Date(), "yyyy-MM-dd");      //结束日期
	
	private String searchInput;   //运单号,手机号,姓名,会员名
	
	private String shopName;  //关联店铺名称
	
	/**
	 * 执行参数
	 */
	private Integer smsId;    //短信信息ID
	
	private List<SMSTemplate> templates;
	
	private List<SMSService> smsTypes;
	
	private List<SMSInfoExpand> smsInfos;
	
	private List<User> bindUserList;// 绑定店铺
	
	private String menuFlag;  //菜单高亮
	
	private String isSearch;  //判读是否查询 1：查询
	
	private String msg;
	
	/**管理员*/
	private String noQuery; //不查询，只进行时间，短信类型的赋值
	
	//绑定管理店铺标记
	private String bindUserTag = "0";
	
	/**
	 * type = 0 当天  type = 1 当月 
	 */
	private String type;
	
	private String response;
	
	private String feedbackInfo;
	
	private String sendBuyerName;   //通知客户   发送买家名称
	
	private String sendBuyerMobile; //通知客户   发送买家手机号码
	
	private String sendMailno;
	
	private int sendCount; //发送数量
	
	private String serviceType;  //短信类型

	public static String startDate() {
		String dateStartTemp = "";
		try {
			dateStartTemp = DateUtil.stringToDate(DateUtil.format(new Date(), "yyyy-MM-dd"), 6);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStartTemp;
	}
	/**
	 * 短信查询[页面]
	 * @return
	 */
	public String searchPage() {
		
		//判断这个服务是否已经开通过
		boolean flag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!flag){
			return "smsWelcome";
		}
		
		//登录USER
		User currentUser = super.readCookieUser();
		
		//设置获取的短信类型
		//setSmsTypes();
	
		//发送时间
		if("0".equals(type)) {  //当天
			sendStatus = "0";
			dateStart = DateUtil.dateArithmetic(new Date(),0);
			dateEnd = DateUtil.dateArithmetic(new Date(),0);
		}
		if("1".equals(type)) {  //当月
			sendStatus = "0";
			dateStart = DateUtil.format(DateUtil.getFirstDayOfMonth(new Date()), "yyyy-MM-dd");
			dateEnd = DateUtil.dateArithmetic(new Date(),0);
		}
		//绑定关联门店,并获取他们的ID集合
		List<Integer> userIds = setBindUserListAndGetIds(currentUser);
		
		//分页参数
		if(pagination == null) pagination = new Pagination(currentPage,pageNum);
		pagination.setTotalRecords(0);
		
		//通过菜单进入,不做查询处理
		if(!"1".equals(isSearch)) {
			msg = "请选择查询条件，来查询你的短信记录!";   //提示信息
			return "searchPage";
		}
		
		//提示信息
		msg = "抱歉，找不到你查询的短信记录，请确认查询条件是否有误!";
		//查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIds",userIds);  
		//params.put("smsTypeId", smsTypeId); 
		params.put("serviceType", smsTypeId);
		params.put("smsTemplateId", smsTemplateId);
		params.put("status", sendStatus);
		params.put("sendTimeStart", dateStart);  
		params.put("sendTimeEnd", dateEnd);
		params.put("shopName", shopName);
		params.put("isDel", "0");   //逻辑删除条件
		
		//设置混合查询区分,查询类型   运单号，手机号，（姓名，会员名）
		setMixSearch(params);
		
		//设置分页,并查询结果
		pagination.setTotalRecords(smsInfoService.getSMSInfoCountByPamams(params));
		params.put("startIndex", pagination.getStartIndex());
		params.put("pageNum", pageNum);
		smsInfos = smsInfoService.getSMSInfoByPamams(params);
		
		// menuFlag = "sms_home";
		return "searchPage";
	}
	
	/**
	 * 管理员短信查询[页面]
	 * @return
	 */
	public String smsSearchAdmin() {
		
		//判断这个服务是否已经开通过
		/*boolean flag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!flag){
			return "smsWelcome";
		}*/
		
		User currentUser = super.readCookieUser();
		
		//设置获取的短信类型
		//setSmsTypes();
		
		//发送时间
		if(StringUtils.isBlank(dateStart)) {
			dateStart = DateUtil.dateArithmetic(new Date(),6);
		}
		if(StringUtils.isBlank(dateEnd)) {
			dateEnd = DateUtil.dateArithmetic(new Date(),0);
		}
		
		//绑定关联门店,并获取他们的ID集合
		//List<Integer> userIds = setBindUserListAndGetIds(currentUser);
		if(StringUtils.isNotBlank(shopName)) {
			User tempUser = (User) userDao.getUserByShopNameOnly(shopName);  //为了获取店铺名称
			//关联店铺
			bindUserList = new ArrayList<User>();
			bindUserList(bindUserList,tempUser);
		}

		//分页参数
		if(pagination == null) pagination = new Pagination(currentPage,pageNum);
		pagination.setTotalRecords(0);
		
		//通过菜单进入,不做查询处理
		if("1".equals(noQuery)) {
			msg = "请选择查询条件，来查询你的短信记录!";   //提示信息
			return "smsSearchAdmin";
		}
		
		//提示信息
		msg = "抱歉，找不到你查询的短信记录，请确认查询条件是否有误!";
		//查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIds",null);         //ADMIN查询所有人             
		//params.put("smsTypeId", smsTypeId);                    
		params.put("smsTemplateId", smsTemplateId);
		params.put("status", sendStatus);
		params.put("sendTimeStart", dateStart);
		params.put("sendTimeEnd", dateEnd);
		params.put("shopName", shopName);
		params.put("serviceType", smsTypeId);
		
		//设置混合查询区分,查询类型   运单号，手机号，（姓名，会员名）
		setMixSearch(params);
		
		//设置分页,并查询结果
		pagination.setTotalRecords(smsInfoService.getSMSInfoCountByPamams(params));
		params.put("startIndex", pagination.getStartIndex());
		params.put("pageNum", pageNum);
		smsInfos = smsInfoService.getSMSInfoByPamams(params);
		
		return "smsSearchAdmin";
	}
	
	
	/**
	 * 删除操作  AJAX
	 * @return
	 */
	public String deleteSmsInfo() {
		//逻辑删除,修改状态
		SMSInfo smsInfo = smsInfoService.getSMSInfoById(smsId);
		smsInfo.setIsDel(1);
		boolean isSuccess = smsInfoService.updateSMSInfo(smsInfo);
		//boolean isSuccess = smsInfoService.deleteSMSInfo(smsId);
		/*
		if(isSuccess) {
			putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"删除成功！","");  //页面无删除提示
		}else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"删除失败！","");
		}
		*/
		return searchPage();
	}
	
	/**
	 * 获取的短信模版 AJAX
	 */
	public String getSmsTemplates() {
		
		//短信模版查询条件
		Map<String, Object> params = new HashMap<String, Object>();

		//使用的ID
		params.put("userId",getUseUserId());  ////主ID,子ID,平台(分仓使用平台,平台使用平台自身)
		
		String smsName = "";
		List<String> names = new ArrayList<String>();
		if(StringUtils.isBlank(smsTypeId)) {
			names.add("发货提醒");
			names.add("派件提醒");
			names.add("签收提醒");
		}
		if("GOT".equals(smsTypeId)) {
			smsName = "发货提醒";
		}
		if("SENT_SCAN".equals(smsTypeId)) {
			smsName = "派件提醒";
		}
		if("SIGNED".equals(smsTypeId)) {
			smsName = "签收提醒";
		}
		if("QUEST".equals(smsTypeId)) {
			smsName = "问题件提醒";
		}
		if("AGING".equals(smsTypeId)) {
			smsName = "时效提醒";
		}
		if("AGENT".equals(smsTypeId)) {
			smsName = "智能查件";
		}
		//params.put("smsTypeId", smsTypeId);
		params.put("name", smsName);
		params.put("names", names);
		params.put("moduleStatus", "Y,S");
		try {
			templates = smsTemplateService.getSMSTemplateByUserId(params,null);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "smsTemplates";
	}
	
	/**
	 * 是否允许短信发送  1:短信服务是否开通    2  ：短信数量,是否足够,  (开通或者足够,显示为-1) 不足够，显示当前短信数量   [-1=true else = false]
	 * @return	
	 */
	public String isAllowSend() {
		//当前登录用户
		User currentUser = super.readCookieUser();
		
		//1:判断这个服务是否已经开通过   
		response = "true";
		boolean flag = smsPortService.supplyStatusOpenUp(smsInfoService.getUseUserId(currentUser));
		if(!flag){
			response = "0";  // false 【如果没开通 提示 亲，你的可发短信数为 0，需要先去购买短信! 】
			return "send";
		}
		
		//2:根据当前用户的userId查询该用户的可使用短信数
		SMSMessage smsMessage = smsPortService.supplySMSMessage(smsInfoService.getUseUserId(currentUser));
		int hasSmsCount = 0;
		if(smsMessage != null) {
			hasSmsCount = smsMessage.getSmsUsecount();
		}
		if(hasSmsCount>=sendCount) {
			response = "-1"; //true
		}else {
			response = hasSmsCount+"";
		}
		
		return "send";
	}
	
	/**
	 * 过滤模版文字
	 * @return
	 */
	public String toQuestionnaireFilter() {
		FilterResult filter = filterRuleService.filter(feedbackInfo,FilterRule.TYPE_ISSUEREPORT);
		boolean isvalid = filter.isvalid();
		//String invalidWordsStr = filter.getInvalidWordsStr();
		response = "false";
		if(isvalid) {
			response = "true";
		}
		return "send";
	}
	
	/**
	 * 短信通知
	 * @return
	 */
	public String sendSms() {
		//当前登录用户
		User currentUser = super.readCookieUser();
	
		//短信发送
		response = "短信通知已发送!";
		boolean isSend = smsInfoService.saveInfoToQueue(currentUser, sendBuyerName, sendBuyerMobile,sendMailno, feedbackInfo,serviceType);
		Integer userId = null;
		if(currentUser != null){
			userId =currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		boolean result = sendMessag(userId);//发送消息
		if(!result){
			logger.error("发送消息失败");
		}
		if(!isSend) {
			response = "短信通知发送失败!";
		}
		
		return "send";
	}
	
	/**
	 * 发送消息
	 * @param userId 主账号ID
	 */
	public boolean sendMessag(Integer userId){
		String messageTheme ="短信不足提醒";
		Integer smsResult = smsPortService.smsLessForRemaind(userId);
		boolean result = false;
		if(smsResult != null){
			String messageContent = "亲，截止"+DateUtil.getDateStr(new Date(), "yyyy年MM月dd日HH时 ")+"，你的易通可发短信数不足"+smsResult+"条，为不影响你的使用，请及时购买短信。去这<a href='smsServiceMarket_inBuyPorts.action?menuFlag=sms_package'>[购买短信]</a>";
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
	
	/**
	 * 根据规则验证前台的 searchInput 属于哪种类型  运单号，手机号，（姓名，会员名）
	 * @param params
	 */
	private void setMixSearch(Map<String, Object> params) {
		if(StringUtils.isNotBlank(searchInput)) {
			if(searchInput.matches("^[A-Za-z0-9]{1}\\d{9}$")) {  //运单号      必须是由首位为数字或字母与9位数字自由组成
				params.put("mailNo", searchInput);
			}else if(searchInput.matches("^^(13|14|15|18)[0-9]{9}$")) { //手机号     电话必须是首位为1的11位数字
				params.put("buyMobile", searchInput);
			}else {   //（姓名，会员名）
				params.put("buyName", searchInput);
			}
		}
	}
	
	/**
	 * 获取绑定店铺用户,并且获取  List<Integer> (此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id  主帐号ID为第一位
	 */
	private List<Integer> setBindUserListAndGetIds(User currentUser) {
		
		//关联店铺
		bindUserList = new ArrayList<User>();
		
		//绑定当前登录分仓
		bindUserList(bindUserList,currentUser);

		//查询ID集合
		List<Integer> userIds = new ArrayList<Integer>();
		int userType = getUserType();
		if(userType==1) {  //卖家,(包括平台下的B:分仓 C:企业  各自查询自己关联的店铺)
			String childType = currentUser.getChildType();  //B:分仓 C:企业
			Integer parentId = currentUser.getParentId();
			//判断是否使用父ID查询
			if(parentId!=null) {
				userIds.add(parentId);
			}else {
				userIds.add(currentUser.getId());
			}
		}
		if(userType==4) {  //平台账户,查看下面所有的分仓用户
			userIds.add(currentUser.getId());
			List<User> users = userService.pingTaiSelect(currentUser,1);
			if(users!=null) {
				for(User user:users) {
					userIds.add(user.getId());  
					bindUserList(bindUserList,user);
				}
			}
		}
		//userType = 3 网店无查询功能  userType = 4 管理员，查询所有用户
		
		return userIds;
	}
	
	/**
	 * 绑定用户的关联店铺
	 * @param list
	 * @param user
	 */
	private void bindUserList(List list,User user) {
		if(user!=null) {
			for (String cur : Resource.getBindedCustomerIdList(user)) {
				User u = userService.getUserByCustomerId(cur);
				if (u != null) {
					if(StringUtils.isNotBlank(u.getShopName())) {
						bindUserList.add(u);
					}				
				}
			}
		}
	}

	/**
	 * 设置获取的短信类型
	 */
	private void setSmsTypes() {
		try {	
			//User landUser = super.readCookieUser();
			smsTypes = smsServiceService.getSMSServiceAll(getMainUserId());
			//smsTypes = smsServiceService.getSMSServicesByUserId(getMainUserId());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	

	/**
	 * 获取用户的主账户ID
	 * @return
	 */
	private int getMainUserId() {
		User landUser = super.readCookieUser();
		Integer parentId = landUser.getParentId();
		if(parentId!=null) {
			return parentId;
		}else {
			return landUser.getId();
		}	
	}
	
	/**
	 * 获取查询使用的用户ID
	 * 分3类ID  第一类：主账户ID（父ID）  第二类：子账户ID（父ID不存在的情况下使用） 第三类：平台ID (分仓使用平台)
	 * @return
	 */
	private int getUseUserId() {
		//登录用户
		User landUser = super.readCookieUser();
		//登录用户类型
		int userType = getUserType();
		String childType = landUser.getChildType();  //B:分仓 C:企业
		if("B".equals(childType)) {  //B:分仓  使用平台ID
			String userSource = landUser.getUserSource();
			if(StringUtils.isNotBlank(userSource)) {        //若：B:分仓 C:企业  的平台ID为NULL ,则使用自身ID,注意：此情况实际属于,错误数据
				return Integer.parseInt(userSource);
			}else {
				return landUser.getId();
			}
		}else {                                            //其他类型用户,使用主账户
			Integer parentId = landUser.getParentId();
			if(parentId!=null) {
				return parentId;
			}else {
				return landUser.getId();
			}
		}
	}
	
	/**
	 * 获取登录用户类型 0:未知的类型  1：卖家 2：网点 3：admin 4:平台用户
	 * @return
	 */
	private int getUserType() {
		User landUser = super.readCookieUser();
		String userType = landUser.getUserType();
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
	
	public String getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}

	public String getSmsTypeId() {
		return smsTypeId;
	}

	public void setSmsTypeId(String smsTypeId) {
		this.smsTypeId = smsTypeId;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	public String getSearchInput() {
		return searchInput;
	}

	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}
	
	public List<SMSTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<SMSTemplate> templates) {
		this.templates = templates;
	}

	public List<SMSInfoExpand> getSmsInfos() {
		return smsInfos;
	}

	public void setSmsInfos(List<SMSInfoExpand> smsInfos) {
		this.smsInfos = smsInfos;
	}

	public SMSInfoService getSmsInfoService() {
		return smsInfoService;
	}

	public void setSmsInfoService(SMSInfoService smsInfoService) {
		this.smsInfoService = smsInfoService;
	}
	
	public Integer getSmsId() {
		return smsId;
	}

	public void setSmsId(Integer smsId) {
		this.smsId = smsId;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	
	public List<SMSService> getSmsTypes() {
		return smsTypes;
	}

	public void setSmsTypes(List<SMSService> smsTypes) {
		this.smsTypes = smsTypes;
	}

	public List<User> getBindUserList() {
		return bindUserList;
	}

	public void setBindUserList(List<User> bindUserList) {
		this.bindUserList = bindUserList;
	}
	
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getNoQuery() {
		return noQuery;
	}

	public void setNoQuery(String noQuery) {
		this.noQuery = noQuery;
	}
	public String getBindUserTag() {
		return bindUserTag;
	}
	public void setBindUserTag(String bindUserTag) {
		this.bindUserTag = bindUserTag;
	}
	public String getIsSearch() {
		return isSearch;
	}
	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getFeedbackInfo() {
		return feedbackInfo;
	}
	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public String getSendBuyerName() {
		return sendBuyerName;
	}
	public void setSendBuyerName(String sendBuyerName) {
		this.sendBuyerName = sendBuyerName;
	}
	public String getSendBuyerMobile() {
		return sendBuyerMobile;
	}
	public void setSendBuyerMobile(String sendBuyerMobile) {
		this.sendBuyerMobile = sendBuyerMobile;
	}
	public String getSendMailno() {
		return sendMailno;
	}
	public void setSendMailno(String sendMailno) {
		this.sendMailno = sendMailno;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}

