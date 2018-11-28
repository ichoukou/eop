package net.ytoec.kernel.action.passIssueManage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.StepInfo;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.ReportIssue;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WarnUpOper;
import net.ytoec.kernel.dataobject.WarnValue;
import net.ytoec.kernel.dto.DtoBranch;
import net.ytoec.kernel.dto.DtoQuestion;
import net.ytoec.kernel.dto.PassIssueDTO;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.FilterRuleService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.PassManageService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.QuestionDtoService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.RegionService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.WarnValueService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.json.annotations.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.TimeZone;

@Controller
@Scope("prototype")
public class PassManageAction extends AbstractActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(PassManageAction.class);
	
	@Inject
	private PassManageService passManageService;
	@Inject
	private UserService<User> userService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private WarnValueService<WarnValue> warnValueService;
	@Inject
	private EccoreSearchService eccoreSearchService;
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;
	@Inject
	private DredgeServiceService dredgeServiceService;
	@Inject
	private PayServiceService payServiceService;
	@Inject
	private QuestionDtoService<DtoQuestion> dtoQuestionService;
	@Inject
	private RegionService<Region> regionService;
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private FilterRuleService filterRuleService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private PaymentService<Payment> paymentService;
	@Inject
	private MessageService<Message> messageService;

	private ReportIssue report; /*上报问题件*/
	private WarnUpOper oper = new WarnUpOper();/*上报处理信息*/
	private AttentionMail attentionMail;/*保存我的关注*/
	
	private Integer currentPage = 1;
//	private Pagination<EccoreSearchResultDTO> pagination1; // 卖家以上报分页
	private Pagination pagination; // 卖家以上报分页
	
	private List<ReportIssue> reportIssueList; /*页面列表*/
	private List<PassIssueDTO> passIssueDTOList;/*超时件列表*/
	private List<WarnValue> warnValueList = new ArrayList<WarnValue>(); /*预警值列表*/
	
	private String queryCondition;/*查询条件  、收集上报问题件层中的数据集*/ 
	private String flag;//标示工具变量[设置预警值列表 0-上一页/1-下一页、网点未回复/已回复]
	private String tips;//标记变量：0-筛选；无-默认和切换加载、[设置预警值中初始化进入层_0还是点击上/下页_1]
	private String tabNum;//控制页面tab
	private String mailNo;//上报问题件时需传参mailNo查询网点信息
	/*预警值设置*/
	private Integer id;
	private String sellerId;
	private String province;
	private String warnValue;
	private String addressCity;//商家的发货地址
	private String operMsg;
	private String issueId;
	private String returnStr;/*显示上报问题件层隐藏域*/
	private String buyerPhone;//客户手机
	private String buyerName;//买家姓名
	private String paramInsert;//插入预警值
	private String paramUpdate;//更新预警值
	private String paramDelete;//删除预警值
	private Payment payment;//交易明细

	// 层分页数据
	/*private Integer divPageSize = 5;//每页显示几条
	private Integer totalNum;*/
	private static PassManageAction passManageAction = new PassManageAction();
	
	private String resultString;//预警值层提示信息
	private List<User> shopNames;//关联店铺
	private Integer userId;//根据店铺筛选
	private String notWarnValueFlag;//是否存在预警值
    private Boolean isSuccess = true;
	private Boolean response = true;
    private Integer sendMessageCount;//需要发送的短信条数
    private String resMessage;//给客户发送信息，反馈的信息
	//是否开通使用
	private Integer serviceId;
	
	/**
	 * 判断用户是否开通实效提醒
	 * @param user  
	 * @return  若当前用户已开通，则返回true；否则继续判断父账号是否开通
	 */
	public boolean isOpeningService(User user)
	{
		boolean flag=false;
		Map map = new HashMap();
		map.put("userId", user.getId());
		map.put("serviceName", "时效提醒");
		//若当前用户已开通时效提醒服务，则返回true;若没有开通（入驻企业账号除外），继续判断父账号是否开通实效提醒
		flag = dredgeServiceService.getServiceByUserIdAndServiceName(map);
		
		if(flag==true || (user.getUserType().equals("1")&&"C".equals(user.getChildType()))) {
			return flag;
		}
		if(user.getParentId()!=null)
		{
			User users = userService.getUserById(user.getParentId());
			if(users!=null){
				return isOpeningService(users);
			}			     
		}
		
		//平台用户子账号类型：B-分仓账号；
		if(user.getUserType().equals("1") && user.getUserSource()!=null && "B".equals(user.getChildType()))
		{
			User users = userService.getUserById(Integer.parseInt(user.getUserSource()));
			if(users!=null){
				return isOpeningService(users);
			}	
		}
		return flag;
	}
	
	/**
	 * 加载搜索引擎中超时件数据
	 */
	public String warnningIndex(){
		logger.info("加载搜索引擎中超时件数据start");
		User currentUser = super.readCookieUser();
		
//		Map map = new HashMap();
//		map.put("userId", currentUser.getParentId() == null ? currentUser.getId() : currentUser.getParentId());
//		map.put("serviceName", "时效提醒");
		//判断是否开启时效提醒
//		if(!dredgeServiceService.getServiceByUserIdAndServiceName(map)){
		//若是网点登陆，不需要广告页面
		if(currentUser!=null&&currentUser.getUserType().equals("2")&&currentUser.getParentId()==null) {
		  
		} 
		else if(!isOpeningService(currentUser) && !ConfigUtilSingle.getInstance().isFreeAgingTime(new Date())){ //若没有开通实效提醒且不属于免费服务时间
			PayService payService = new PayService();
			payService.setName("时效提醒");
			payService = (PayService) payServiceService.get(payService);
			serviceId = payService.getId();
			Integer usId = null;
			if(currentUser != null){
				usId = currentUser.getId();
				if(currentUser.getParentId()!=null)
					usId = currentUser.getParentId();
			}
			Map<String, Object> paymap = new HashMap<String, Object>();
			paymap.put("userId", usId);
			paymap.put("dealStatus","0");//未付款
			paymap.put("delFlag", "0");//未关闭
			paymap.put("dealName", payService.getName());//交易名称
			List<Payment> list = paymentService.getList(paymap);//根据上面的条件进行查询 之后得到最大的值 就是当前的订单的状态
			if(!CollectionUtils.isEmpty(list)){
				 payment = list.get(0);
			}	
			
			return "toWelcome";
		}
		
		User curUser = this.getCurUser(currentUser);
		
		/* ################################################################################ */
		/*需要判断权限 卖家、卖家子账号、平台账号、分仓账号、平台客服子账号*/
		/*User curUser = new User();
		//卖家非平台业务账号
		if("1".equals(currentUser.getUserType()) && (currentUser.getChildType() == null || "".equals(currentUser.getChildType()))){
			curUser = this.getCurUser(currentUser);
		}
		//平台账号、平台客服账号
		if("4".equals(currentUser.getUserType()) || "41".equals(currentUser.getUserType())){
			curUser = currentUser;
		}
		//分仓账号:需要判断时效提醒服务是平台开启的还是分仓开启的
		if("1".equals(currentUser.getUserType()) && "B".equals(currentUser.getChildType())){
			
		}*/
		
		/* ################################################################################ */
		
		Map<String, String> params = new HashMap<String,String>();
		
		//设置预警值_默认现有表中数据查询
		
		StringBuffer overTimeMap = new StringBuffer();
		WarnValue w = new WarnValue();
		w.setSellerId(curUser.getUserName());
		
		try {
			warnValueList = warnValueService.searchWarnValueBySellerId(w);
		} catch (Exception e) {
			logger.info("查询预警值列表出现错误"+e.getMessage());
		}
		if(warnValueList != null && warnValueList.size() > 0) {
			String value = "";
			String code = "";
			for(int i = 0 ; i < warnValueList.size() ; i++){
				WarnValue warn = warnValueList.get(i);
				value = warn.getWarnValue();
				code = Resource.getCodeByName(warn.getDestination()) + "";
				if(i < warnValueList.size()-1) {
					overTimeMap.append(code).append(",").append(value).append(";");
				} else {
					overTimeMap.append(code).append(",").append(value);
				}
			}
			// 设置预警值
			params.put("overTimeMap", overTimeMap.toString());
		} else {
			notWarnValueFlag = "trueStr";
			if(pagination == null) {
				pagination = new Pagination(currentPage,10);
			}
			pagination.setTotalRecords(0);
			return "warnningIndex";
		}
		
		List<String>  bindString = new ArrayList<String>();
		try{
			bindString = Resource.getBindedCustomerIdList(curUser);
		}catch(Exception e){
			logger.error("获取绑定用户异常："+e);
		}
		StringBuffer customIds = new StringBuffer();
		shopNames = new ArrayList<User>();
		User user = null;
		User user1 = null;
		if (bindString != null && bindString.size() > 0) {
			for(int i = 0; i < bindString.size(); i ++){
				if(i < bindString.size() - 1) {
					customIds.append(bindString.get(i)).append(",");
				} else {
					customIds.append(bindString.get(i));
				}
				// 关联店铺
			    user = new User();
			    user1= userService.getUserByCustomerId(bindString.get(i));
				user.setId(user1.getId());
				user.setShopName(user1.getShopName());
				user.setTaobaoEncodeKey(user1.getTaobaoEncodeKey());
				shopNames.add(user);
			}
		}
		
		if (userId != null) { //根据店铺筛选
			String customerId = userService.getUserById(userId).getTaobaoEncodeKey();
			params.put("customerIDs", customerId);
		} else {
			params.put("customerIDs", customIds.toString());
		}	
		logger.info("用户customerIDs"+params.get("customerIDs"));
		String endDate = DateUtil.format(new Date(),"yyyy-MM-dd");
		try {
			params.put("startDate", DateUtil.stringToDate(endDate, 100));
			params.put("endDate", endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		/*筛选按钮*/
		if("0".equals(tips) && queryCondition != null && !"买家姓名/电话/运单号".equals(queryCondition)){
			logger.info("[卖家未上报]==筛选条件：" + queryCondition);
			if(Pattern.matches("[a-zA-Z0-9]\\d{9}", queryCondition)){
			    params.put("mailNo", queryCondition);/*运单号*/
			} else if(StringUtil.isMobile(queryCondition)){
			    params.put("phone", queryCondition);/*买家手机*/
			} else {
			    params.put("name", queryCondition);/*买家姓名*/
			}
		}
		
		params.put("sortType", "asc");
		
		//状态过滤
//		params.put("statusList", "SIGNED,ACCEPT,UNACCEPT,CREATE");
		
		Pagination<EccoreSearchResultDTO> pagination1 = new Pagination<EccoreSearchResultDTO>(currentPage, 10);
		
		pagination1.setParams(params);
		
		logger.info("####" + ConfigUtilSingle.getInstance().getSolrEccoreUrl());
		logger.info("查询引擎数据params=" + params);
		
		try { 
			passIssueDTOList = passManageService.searPassIssue(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),
																pagination1, shopNames, userId, warnValueList, bindString);
		} catch (Exception e2) {
			logger.error("##查询搜索引擎异常,参数="+params + ",异常信息：",e2);
		}
		
		//查数量
		if(pagination == null) {
			pagination = new Pagination(currentPage, 10);
		}
		logger.info("统计引擎数据params=" + params);
		pagination1 = new Pagination<EccoreSearchResultDTO>(1,Integer.MAX_VALUE);
		pagination1.setParams(params);
		Integer totalRecards = 0;
		try {
			totalRecards = passManageService.searPassIssueByTotalCount(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), pagination1);
		} catch (Exception e2) {
			logger.error("##查询搜索引擎[总数量]异常,参数=" + params + ",异常信息：", e2);
		}
		pagination.setRecords(passIssueDTOList);
		pagination.setTotalRecords(totalRecards);
		
		tabNum="0";
		logger.info("加载搜索引擎中超时件数据end");
		return "warnningIndex";
	}
	
	/**
	 * 加载搜索引擎中超时件数据
	 */
	public String warnningIndex2(){
		User currentUser = super.readCookieUser();
		if(pagination == null)
			pagination = new Pagination(currentPage,10);
		pagination.setTotalRecords(0);
		if(currentUser!=null&&currentUser.getUserType().equals("2")&&currentUser.getParentId()==null) {
			  
		} 
		else if(!isOpeningService(currentUser) && !ConfigUtilSingle.getInstance().isFreeAgingTime(new Date())){ //若没有开通实效提醒且不属于免费服务时间
			PayService payService = new PayService();
			payService.setName("时效提醒");
			payService = (PayService) payServiceService.get(payService);
			serviceId = payService.getId();
			return "toWelcome";
		}
		
		return "warnningIndex";
	}

	/**
	 * 通过mailNo调用金刚，获取接收网点信息，现实上报问题件层信息
	 * 先判断最近接受网点是否已经激活，如果没有机会则获取揽收网点
	 * @return
	 */
	public String getBranchInfo(){
		
		String[] mailNos = {mailNo}; 
		TaoBaoOrderAction op = new TaoBaoOrderAction();
		List<QueryOrder> queryOrderList = null;
		try{
			queryOrderList = op.ordersSearch(mailNos);
		}catch(Exception e){
			logger.error("mailNo查询订单信息异常");
		}
		if(queryOrderList == null){
			logger.error("mailNo查询订单信息返回为空");
		}
		logger.info("上报问题件时金刚返回queryOrderList size="+queryOrderList.size());
		QueryOrder queryOrder = queryOrderList.get(0);
		List<StepInfo> steps = queryOrder.getSteps();
		logger.info("上报问题件时金刚返回steps size="+steps.size());
		
		String siteCode = steps.get(steps.size()-1).getSiteCode(); //网点编码
		String siteName = "";
		String customerId = "";
		String acceptTime = "";
		logger.info("##last site="+siteCode);
		List<User> userList = null;
		try {
			userList = userService.searchUsersBySite(siteCode);//返回激活的网点信息
		} catch (Exception e) {
			logger.error(e.getMessage()+e.getStackTrace());
		}
		if(userList != null && userList.size()>0){
			siteName = userList.get(0).getUserNameText();
			customerId = userList.get(0).getUserCode();
			acceptTime = steps.get(steps.size()-1).getAcceptTime();
		}else{
			siteCode = steps.get(0).getSiteCode();
			userList = null;
			try {
				userList = userService.searchUsersBySite(siteCode);
			} catch (Exception e) {
				logger.error(e.getMessage()+e.getStackTrace());
			}
			siteName = userList.get(0).getUserNameText();
			customerId = userList.get(0).getUserCode();
			acceptTime = steps.get(0).getAcceptTime();
		}
		logger.info("##siteCode="+siteCode+"-userNameText="+siteName+"-"+customerId+"-"+acceptTime);
		
		/*查询订单->运单发货时间 、运单预计到达时间、线上或线下 -> 添加我的关注*/
		Order order = orderService.getOrderByMailNo(mailNo);
		String createTime = DateUtil.format(order.getCreateTime(), "yyyy-MM-dd hh:mm:ss");
		//attentionMail.setArriveTime();//预计到达时间
		String lineType = order.getLineType();
		
		returnStr = siteCode+","+siteName+","+customerId+","+acceptTime+","+createTime+","+lineType;
		logger.info("##"+returnStr);
		
		return "getBranchInfo";
	}
	
	/**
	 * 上报超时件
	 * @return
	 */
	public String addReportIssue(){
		
		logger.info("上报层提交数据："+queryCondition);
		
		User currentUser = super.readCookieUser();
		HashMap<String,Object> params = this.getParamsForReport(currentUser);
		
		boolean b = false;
		try {
			b = passManageService.reportIssue(queryCondition,params,userService);
		} catch (Exception e) {
			logger.error("##"+e.getMessage()+e);
		}
		logger.info("上报问题件是否成功："+b);
		return null;
	}
	
	/**
	 * 判断当前登录用户是否是主账号
	 * @param currentUser
	 * @return
	 */
	public HashMap<String,Object> getParamsForReport(User currentUser){
		HashMap<String,Object> params = new HashMap<String,Object>();
		if(currentUser.getParentId() == null || "".equals(currentUser.getParentId())){
			params.put("isMain", "Y");
			params.put("curUser", currentUser);
		}else{
			params.put("isMain", "N");
			User user = userService.getUserById(currentUser.getParentId());
			params.put("mianUser", user);
			params.put("curUser", currentUser);
		}
		
		return params;
	}
	 
	/**      
	 * 卖家切换已上报/按条件筛选，加载近一个月内并满足一定条件的上报网点信息[tips:标示默认还是筛选]
	 * @return
	 */
	public String list_seller(){
		reportIssueList = new ArrayList<ReportIssue>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		String startTime = (this.getTime()).split("=")[0];
		String endTime = (this.getTime()).split("=")[1];
		
		User currentUser = super.readCookieUser();
		User curUser = this.getCurUser(currentUser);
		if(pagination == null){
			pagination = new Pagination<ReportIssue>(currentPage, 10);
		}
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("reportUserId", curUser.getUserName());
		
		logger.info("[卖家]startTime="+startTime+"-endTime="+endTime+"-reportUserId="+curUser.getUserName()+"-tips="+tips);
		
		/*0-筛选；无-切换默认加载*/
		if("0".equals(tips) && (queryCondition != null && !"买家姓名/电话/运单号".equals(queryCondition))){
			logger.info("====筛选条件："+queryCondition);
			if(Pattern.matches("[a-zA-Z0-9]\\d{9}", queryCondition)){ 
				params.put("mailNo", queryCondition);/*运单号*/
			}else if(StringUtil.isMobile(queryCondition)){
				params.put("buyerMobile", queryCondition);/*买家手机*/
			}else if(StringUtil.isPhone(queryCondition)){
				params.put("buyerPhone", queryCondition);/*买家电话*/
			}else{
				params.put("buyerName", queryCondition);/*买家姓名*/
			}
		}
		
		reportIssueList = passManageAction.getReportIssueListByParams(passManageService,params, pagination);
		
		this.operMsg(23,9);
		
		tabNum = "1";
		return "warnningIndex";
	}
	
	/**
	 * 网点查看未回复/已回复列表[默认切换未/已上报、按条件筛选：区别标示为tips + flag标记为未回复/已回复（默认）+ 1个月]
	 * @return
	 */
	public String list_site(){
		User user = super.readCookieUser();
		Map<String, Object> params = new HashMap<String, Object>();
		if(pagination == null){
			pagination = new Pagination(currentPage, 10);
		}
		
		String startTime = (this.getTime()).split("=")[0];
		String endTime = (this.getTime()).split("=")[1];
		
		logger.info("[网点]startTime="+startTime+"-endTime="+endTime);
		
		params.put("receiveBranchId", user.getSite());
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		/*0-网点未回复；1-网点已回复*/
		if("0".equals(flag)){
			params.put("msgStatus", "0");
			tabNum = "0";
		}
		if("1".equals(flag)){
			params.put("msgStatus", "1");
			tabNum = "1";
			flag = "1";
		}
		
		/*0-"筛选"操作；无-默认和切换加载*/
		if("0".equals(tips) && (queryCondition != null && !"买家姓名/电话/运单号".equals(queryCondition))){
			logger.info("====筛选条件："+queryCondition);
			if(Pattern.matches("[a-zA-Z0-9]\\d{9}", queryCondition)){ 
				params.put("mailNo", queryCondition);/*运单号*/
			}else if(StringUtil.isMobile(queryCondition)){
				params.put("buyerMobile", queryCondition);/*买家手机*/
			}else if(StringUtil.isPhone(queryCondition)){
				params.put("buyerPhone", queryCondition);/*买家电话*/
			}else{
				params.put("buyerName", queryCondition);/*买家姓名*/
			}
		}
		
		reportIssueList = passManageAction.getReportIssueListByParams(passManageService,params, pagination);
		
		this.operMsg(48,8);
		
		return "passIssue";
	}
	
	/**
	 * 权限控制：
	 * 		1、卖家/卖家子账号
	 * 		2、平台
	 * @return
	 */
	public String rightControl(){
		User curUser = super.readCookieUser();
		
		
		return "jsonRes";
	}
	
	/**
	 * 卖家/网点 发送给 网点/卖家 消息[公用]
	 * @return
	 */
	public String sendToSite(){
		logger.info("发送信息:"+operMsg+"-"+issueId);
		User user = super.readCookieUser();
		
		//卖家主账号和子账号记录
		if("1".equals(flag)){
			HashMap<String,Object> params = this.getParamsForReport(user);
			String tips = (String) params.get("isMain");
			User curUser = (User) params.get("curUser");
			if("Y".equals(tips)){
				oper.setCreateId(curUser.getUserName());
				oper.setUserId(curUser.getUserName());
			}
			if("N".equals(tips)){
				User mainUser = (User) params.get("mianUser");
				oper.setCreateId(curUser.getUserName());
				oper.setUserId(mainUser.getUserName());
			}
		}
		if("2".equals(flag)){
			//网点主账号
			if(user.getParentId() == null || "".equals(user.getParentId())){
				oper.setCreateId(user.getUserName());
				oper.setUserId(user.getUserName());
			}else{
				oper.setCreateId(user.getUserName());
				User site = userService.getUserById(user.getParentId());
				oper.setUserId(site.getUserName());
			}
		}
		
		oper.setCreateTime(new Date());
		oper.setOperMsg(operMsg);
		oper.setIssueId(Integer.valueOf(issueId));
		boolean bool = passManageService.sendMsg(oper,flag);
		if(bool){
			String forward="";
			if("1".equals(flag))forward="warnningIndex";
			if("2".equals(flag))forward="passIssue";
			return forward;
		}else{
			logger.error("##发送信息失败，无法更新超时件状态！");
			return ERROR;
		}
	}

	/**
	 * 获取卖家的预警值列表
	 * @return
	 */
	public String searchWarnValueList(){
		User currentUser = super.readCookieUser();
		User curUser = this.getCurUser(currentUser);
		WarnValue w = new WarnValue();
		w.setSellerId(curUser.getUserName());
		
		//List<WarnValue> all = new ArrayList<WarnValue>();
		try {
			/*//分页代码
			 * all = warnValueService.searchWarnValueBySellerId(w);
			totalNum = all.size()%divPageSize == 0 ? (all.size()/divPageSize) : (all.size()/divPageSize)+1;
			
			if("1".equals(flag))currentPage = currentPage > totalNum ? totalNum : currentPage ;
			if("0".equals(flag))currentPage = currentPage < 1 ? 1 : currentPage ;
			
			w.setBgin((currentPage-1)*divPageSize);
			w.setEnd(divPageSize);*/
			warnValueList = warnValueService.searchWarnValueBySellerId(w);
		} catch (Exception e) {
			logger.error(e.getMessage()+":"+e);
		}
		
		String forward = "";
		if("0".equals(tips))forward = "divMainPage";
		if("1".equals(tips))forward = "divPage";
		
		return  forward;
	}
	
	/**
	 * 设置预警值 
	 * @return
	 */
	public String setWarnValue(){
		User currentUser = super.readCookieUser();
		WarnValue w = new WarnValue();
		w.setSellerId(currentUser.getUserName());
		List<Region> list = new ArrayList<Region>();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(710000);//台湾省
		idList.add(810000);//香港 
		idList.add(820000);//澳门
	    list = regionService.getAllProvince(idList);
	    addressCity=currentUser.getAddressCity();
	    warnValueList = new ArrayList<WarnValue>();
	    List<WarnValue> listValues = new ArrayList<WarnValue>();//得到warnValue
	    try {
	    	listValues = warnValueService.searchWarnValueBySellerId(w);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(Region region : list){
			WarnValue wv = new WarnValue();
			wv.setDestination(region.getRegionName());
			wv.setWarnValue("0");
			warnValueList.add(wv);
		}
		for(WarnValue warnValue : warnValueList){
			for(int i =0;i<listValues.size();i++){
				WarnValue wv= listValues.get(i);
				if(warnValue.getDestination().equals(wv.getDestination())){
					warnValue.setWarnValue(wv.getWarnValue());
					warnValue.setId(wv.getId());				
				}
			}
		}
		return "getWarnValue";
	}
	
	/**
	 * 保存预警值设置
	 * @return
	 */
	public String saveWarnValue(){
		User currentUser = super.readCookieUser();
		List<WarnValue> paramInsertList = new ArrayList<WarnValue>();//插入预警值的list
		List<WarnValue> paramUpdateList = new ArrayList<WarnValue>();//更新预警值的list
		List<WarnValue> paramDeleteList = new ArrayList<WarnValue>();//删除预警值的list
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = true;
		 //插入预警值
		if(paramInsert != null && paramUpdate != ""){
		 String[] strInsert = paramInsert.split(";");
		 for(String str : strInsert){
			 WarnValue warnValue = new WarnValue();
			 String[] str_warn = str.split(",");
			 warnValue.setSellerId(currentUser.getUserName());
			 warnValue.setDestination(str_warn[0]);
			 warnValue.setWarnValue(str_warn[1]);
			 warnValue.setCreateTime(new Date()); 
			 paramInsertList.add(warnValue);
		 }
		 map.put("paramInsertList", paramInsertList);
	  } 
		//更新预警值
	  if(paramUpdate != null && paramUpdate != ""){
			//首先截取字符串
			 String[] strInsert = paramUpdate.split(";");
			 for(String str : strInsert){
				 WarnValue warnValue = new WarnValue();
				 String[] str_warn = str.split(",");
				 warnValue.setSellerId(currentUser.getUserName());
				 warnValue.setDestination(str_warn[0]);
				 warnValue.setWarnValue(str_warn[1]);
				 warnValue.setUpdateTime(new Date());
				 if(!StringUtil.isBlank(str_warn[2])){
					 warnValue.setId(Integer.parseInt(str_warn[2]));
				 }
				 paramUpdateList.add(warnValue);
			 }
			 map.put("paramUpdateList", paramUpdateList);
	  }
	  //删除预警值
	  if(paramDelete != null && paramDelete != ""){
			//首先截取字符串
			 String[] strInsert = paramDelete.split(";");
			 for(String str : strInsert){
				 WarnValue warnValue = new WarnValue();
				 String[] str_warn = str.split(",");
				 warnValue.setDestination(str_warn[0]);
				 warnValue.setSellerId(currentUser.getUserName());
				 paramDeleteList.add(warnValue);
			 }
			 map.put("paramDeleteList", paramDeleteList);
	  }
	  result = warnValueService.operatorWarnValue(map);
	  if(result){
		return "saveSuccessWarnValue";
	  }
		return "exception";
	}
	
	/**
	 * 设置预警值返回
	 * @return
	 */
	public String warnBack(){
		
		return "backWarn";
	}
	
	/**
	 * 过滤模版文字
	 * @return
	 */
	public String toQuestionnaireFilter() {
		FilterResult filter = filterRuleService.filter(operMsg,FilterRule.TYPE_ISSUEREPORT);
		boolean isvalid = filter.isvalid();
		if(isvalid) {
			response = true;
		}else {
			response = false;
		}
		return "send";
	}
	
	/**
	 * 发信息给客户
	 * @return
	 */
	public String sendMessageToCustomer(){
		User currentUser = super.readCookieUser();
		
		if(currentUser != null){
			userId =currentUser.getId();
			if(currentUser.getParentId() != null){
				userId = currentUser.getParentId();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
		map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> list = dredgeServiceService.getServiceRecordByUserId(map);
		DredgeService dredgeService = new DredgeService();
		if(CollectionUtils.isEmpty(list)){//说明没有开通短信服务,提示短信服务没有开通
			isSuccess = false;
			return "sendMessageToCustomer";
		}else {
			//如果开通了短信服务,查看短信条数时候充足,如果不充足，提示短信不足
			SMSMessage smsMessage = new SMSMessage();
			smsMessage = smsPortService.supplySMSMessage(userId);
			int messageCount = 0;
			if(smsMessage != null){
				messageCount = smsMessage.getSmsUsecount();
			}
			if(messageCount < sendMessageCount){//如果短信条数不够
				isSuccess = false;
				return "sendMessageToCustomer";
			}
		}
		
		boolean result = true;
		result = smsInfoService.saveInfoForRemind(currentUser, buyerName, buyerPhone,mailNo, operMsg);
		if(result){
		result = sendMessag(userId);//发送消息
		if(!result){
			logger.error("发送消息失败");
		}
			isSuccess = true;
			return "sendMessageToCustomer";
		}
		return "exception";
	}
	
	public void validateSendMessageToCustomer(){
		if(operMsg.trim().equals("") || operMsg == null){
		    this.addFieldError("errorMessage", "发送短信内容不能为空!");
		}
	}
	
	/**
	 * 子账号均用主账号的
	 * @param currentUser
	 * @return
	 */
	private User getCurUser(User currentUser){
		
		Integer userId = currentUser.getParentId() == null ? currentUser.getId() : currentUser.getParentId();
		User curUser = userService.getUserById(userId);
		return curUser;
	}
	
	/**
	 * 删除预警值
	 * @return
	 */
	public String removeWarnValue(){
		WarnValue w = new WarnValue();
		w.setSellerId(sellerId);
		w.setDestination(province);
		
		boolean b = warnValueService.remove(w);
		System.out.println("是否删除成功："+b);
		if(b)return "divPage";
		else return ERROR;
		
	}
	
	/**
	 * 添加预警值
	 * @return
	 */
	public String addWarnValue(){
		User currentUser = super.readCookieUser();
		User curUser = this.getCurUser(currentUser);
		WarnValue w = new WarnValue();
		w.setSellerId(curUser.getUserName());
		w.setWarnValue(warnValue);
		w.setDestination(province);
		w.setCreateTime(new Date()); 
		resultString = warnValueService.addWarnValue(w);
		return "addWarnValue";
	}
	
	/**
	 * 修改预警值
	 * @return
	 */
	public String editWarnValue(){
			
		WarnValue w = new WarnValue();
		w.setId(id);
		w.setSellerId(sellerId);
		w.setDestination(province);
		w.setWarnValue(warnValue);
		w.setUpdateTime(new Date());
		
		resultString = warnValueService.edit(w);
		return "addWarnValue";
		
	}
	
	/**
	 * 
	 * 处理超时件操作信息：拼接html格式String
	 * @param lineSize  jsp页面显示div每行字数
	 * @param lineNum   jsp页面显示div共几行
	 */
	public void operMsg(int lineSize,int lineNum){
		//当字数超过*时需要显示展开 （每行23  11行）
		for(ReportIssue report : reportIssueList){
			List<WarnUpOper> operList = report.getOperList();
			//StringBuffer showOper = new StringBuffer();
			StringBuffer allOper = new StringBuffer();
			double line = 0;
			for(WarnUpOper oper : operList){
				//StringBuffer one = new StringBuffer();
				//卖家
				if(report.getReportUserId().equals(oper.getUserId())){
					try{
						allOper.append(report.getSeller().getShopName()+":");
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
				//网点
				if(report.getReceiveBranchId().equals(oper.getUserId())){
					User site = userService.getUserByUserName(oper.getCreateId());
					allOper.append(site.getUserNameText()+":");
				}
				allOper.append(oper.getOperMsg()+"<br/>");
				/*
				int d = one.toString().length() % lineSize;
				int i = 0;
				if(d == 0) i = one.toString().getBytes().length / lineSize;
				else i = (one.toString().length() / lineSize) + 1;
				line += i;
				
				if(line < lineNum){
					showOper.append(one.toString());
					allOper.append(one.toString());
				}else{
					allOper.append(one.toString());
				}*/
			}
			report.setAllOper(allOper.toString());
			/*report.setShowOper(showOper.toString());
			//超过层的最大行数
			if(allOper.toString().length() > showOper.toString().length()){
				report.setAllOper(allOper.toString());
			}*/
		}
	}
	
	/**
	 * [方法抽取]默认/根据条件查询超时件及管理超时件相关处理信息返回
	 * @param passManageService 
	 * @param params
	 * @param pagination
	 * @return
	 */
	public List<ReportIssue> getReportIssueListByParams(PassManageService passManageService, Map<String, Object> params,Pagination pagination){
		
		int total = passManageService.searchReportIssueList(params,pagination,false).size();
		logger.info("==查询上报问题件总条数="+total);
		reportIssueList = passManageService.searchReportIssueList(params,pagination,true);
		pagination.setTotalRecords(total);
		
		//把处理信息关联
		for(ReportIssue report : reportIssueList){
			
			//关联网点信息
			DtoBranch branch = Resource.getDtoBranchByCode(report.getReceiveBranchId());
			report.setBranch(branch);
			
			//关联卖家信息
			User user = Resource.getUserByUserName(report.getReportUserId());
			report.setSeller(user);
			
			params.put("issueId", report.getId());
			List<WarnUpOper> operList = passManageService.getOperByIssueId(params);
			report.setOperList(operList);
		}
		return reportIssueList;
	}
	
	/**
	 * [方法抽取]获取当前时间和一个月前时间
	 * @return
	 */
	public String getTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		TimeZone t = TimeZone.getTimeZone("Asia/Shanghai");
		format.setTimeZone(t);//更改时区
		
		String endTime = format.format(new Date());
		Calendar cal=Calendar.getInstance();
		
		cal.add(Calendar.MONTH, -1);
		long date = cal.getTimeInMillis();
		Date date1 = null;
		try {
			date1=format.parse(format.format(new Date(date)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String startTime = format.format(date1);
		return startTime+"="+endTime;
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
	
	public List<ReportIssue> getReportIssueList() {
		return reportIssueList;
	}
	public void setReportIssueList(List<ReportIssue> reportIssueList) {
		this.reportIssueList = reportIssueList;
	}
	public String getTabNum() {
		return tabNum;
	}
	public void setTabNum(String tabNum) {
		this.tabNum = tabNum;
	}
	public String getQueryCondition() {
		return queryCondition;
	}
	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public WarnUpOper getOper() {
		return oper;
	}
	public void setOper(WarnUpOper oper) {
		this.oper = oper;
	}
	public AttentionMail getAttentionMail() {
		return attentionMail;
	}
	public void setAttentionMail(AttentionMail attentionMail) {
		this.attentionMail = attentionMail;
	}
	public ReportIssue getReport() {
		return report;
	}
	public void setReport(ReportIssue report) {
		this.report = report;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<WarnValue> getWarnValuelist() {
		return warnValueList;
	}
	public void setWarnValuelist(List<WarnValue> warnValuelist) {
		this.warnValueList = warnValuelist;
	}
	public String getOperMsg() {
		return operMsg;
	}
	public void setOperMsg(String operMsg) {
		this.operMsg = operMsg;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public List<PassIssueDTO> getPassIssueDTOList() {
		return passIssueDTOList;
	}
	public void setPassIssueDTOList(List<PassIssueDTO> passIssueDTOList) {
		this.passIssueDTOList = passIssueDTOList;
	}
	public List<WarnValue> getWarnValueList() {
		return warnValueList;
	}
	public void setWarnValueList(List<WarnValue> warnValueList) {
		this.warnValueList = warnValueList;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	@JSON(name="returnStr")
	public String getReturnStr() {
		return returnStr;
	}
	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
	}
	/*public Integer getDivPageSize() {
		return divPageSize;
	}
	public void setDivPageSize(Integer divPageSize) {
		this.divPageSize = divPageSize;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}*/
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getWarnValue() {
		return warnValue;
	}
	public void setWarnValue(String warnValue) {
		this.warnValue = warnValue;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Pagination<ReportIssue> getPagination() {
		return pagination;
	}
	public void setPagination(Pagination<ReportIssue> pagination) {
		this.pagination = pagination;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public List<User> getShopNames() {
		return shopNames;
	}
	public void setShopNames(List<User> shopNames) {
		this.shopNames = shopNames;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getNotWarnValueFlag() {
		return notWarnValueFlag;
	}
	public void setNotWarnValueFlag(String notWarnValueFlag) {
		this.notWarnValueFlag = notWarnValueFlag;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getParamInsert() {
		return paramInsert;
	}

	public void setParamInsert(String paramInsert) {
		this.paramInsert = paramInsert;
	}

	public String getParamUpdate() {
		return paramUpdate;
	}

	public void setParamUpdate(String paramUpdate) {
		this.paramUpdate = paramUpdate;
	}

	public String getParamDelete() {
		return paramDelete;
	}

	public void setParamDelete(String paramDelete) {
		this.paramDelete = paramDelete;
	}
	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}
	
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean getResponse() {
		return response;
	}

	public void setResponse(Boolean response) {
		this.response = response;
	}
	public Integer getSendMessageCount() {
		return sendMessageCount;
	}

	public void setSendMessageCount(Integer sendMessageCount) {
		this.sendMessageCount = sendMessageCount;
	}
	public String getResMessage() {
		return resMessage;
	}

	public void setResMessage(String resMessage) {
		this.resMessage = resMessage;
	}
	public SMSInfoService getSmsInfoService() {
		return smsInfoService;
	}

	public void setSmsInfoService(SMSInfoService smsInfoService) {
		this.smsInfoService = smsInfoService;
	}
	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
}
