package net.ytoec.kernel.action.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.FilterRuleService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SMSTemplateService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 短信模版管理
 * @author yangqinghua
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SmsTemplateAction extends AbstractActionSupport {

	private static final long serialVersionUID = -6396325181757062681L;
	
	private Logger logger = LoggerFactory.getLogger(SmsTemplateAction.class);

	@Inject
	private SMSTemplateService smsTemplateService;
	
	@Inject 
	private SMSServiceService smsServiceService;
	
	@Inject
	private SMSPortService smsPortService; 
	
	@Inject
    private UserService<User>             userService;
	
	@Inject
	private FilterRuleService  filterRuleService;
	
	private Map jsons1 = new HashMap();  //页面JSON集合
	private Map jsons2 = new HashMap();  //页面JSON集合

	private Integer id;											//模版ID
	private String name;										//模版标题
	private String content;										//模版内容
	private SMSTemplate template;								//模版对象	
	private SMSService servicedto;									//短信服务对象
	private List<SMSTemplate> templates; 						//模版集合
	private String tabNum;										//jsp页面的tabNum
	private List<SMSService> smsServices;						//服务集合
	private Integer serviceId;									//短信服务ID
	private String resultString;								//异步返回页面
	private String tips; //添加模版成功后返回页面：''-模版管理进入;1-短信首页-设置/立即使用；4-营销定制；
	private String temp; //用来存放第一次进入“添加模版”页面的serviceId
	
	//管理员页面
	private String serviceName;
	private String status;
	private String shopName;
	private Pagination<SMSTemplate> pagination;
	private Integer currentPage = 1;
	private Integer max = Integer.MAX_VALUE;
	private String startTime; //起始时间
	private String endTime;
	private String flag;     //判断审核通过[YES]OR审核不通过[NO]/判断是查询还是分页
	private String remark;
	
	private String smsTypeResult;//默认初始化添加模版页面判断是否可以添加提示
	
	/**
	 * list()
	 * 模版首页
	 * 切换模版类型ID
	 * templates的size会在jsp页面上判断“添加模版”是否可点击
	 * @return
	 */
	public String index(){
		return list();
	}
	
	/**
	 * index()
	 * 模版首页
	 * 切换模版类型ID
	 * templates的size会在jsp页面上判断“添加模版”是否可点击
	 * @return
	 */
	public String list(){
		User curUser = super.readCookieUser();

		//判断这个服务是否已经开通过
		boolean flag = smsPortService.supplyStatusOpenUp(getUserIdService());
		if(!flag){
			return "smsWelcome";
		}
		
		Integer userId = null;
		
		//分仓、平台客服子账号
		if(("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType()))
			|| ("41".equals(curUser.getUserType()))){
			if("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType()))
				userId = Integer.valueOf(curUser.getUserSource());
			if("41".equals(curUser.getUserType()))
				userId = Integer.valueOf(curUser.getParentId());
			
			smsServices = smsServiceService.getSMSServiceAll(userId);
		}else{ //非分仓
			userId = curUser.getParentId() == null ? curUser.getId() : curUser.getParentId();
			smsServices = smsServiceService.getSMSServiceAll(userId);
		}
		
		SMSTemplate info = new SMSTemplate();
		info.setUserId(userId);
		if(serviceId == null || "".equals(serviceId)){
			
			serviceId = smsServices.get(0).getId();
			info.setSmsTypeId(smsServices.get(0).getId()); //点击菜单时不传id时
			tabNum = "0";
		}
		else{
			info.setSmsTypeId(serviceId); //id为sysTypeId点击tab时
		}
		
		try {
			templates = smsTemplateService.getTemplates(info);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		
		return "list";
	}

	/**
	 * [方法抽取]获取分仓的服务 封装条件
	 * @return
	 */
	private HashMap<String,Object> getUserIdParten(){
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		User curUser = super.readCookieUser();
		Integer userId = null;
		String flag = "";
		
		userId = Integer.valueOf(curUser.getUserSource()); 
		flag = "Y";
		resultMap.put("user", curUser);
		
		resultMap.put("userId", userId);
		resultMap.put("flag", flag);
		
		return resultMap;
	}
	
	/**
	 * 跳转至添加页面
	 * @return
	 */
	public String toAdd(){
		User curUser = super.readCookieUser();
		Integer userId = this.getUseUserId();
		
		User user = (User)userService.getUserById(userId); 
		if (user != null) {
			if (user.getShopName() == null) {
				shopName = "";
			} else {
				shopName = user.getShopName();
			}
		}
		if (user.getUserType().equals("1") && user.getUserType() == null) {
			List<String>  bindString = new ArrayList<String>();
			try{
				bindString = Resource.getBindedCustomerIdList(user);
			}catch(Exception e){
				logger.error("获取绑定用户异常："+e);
			}
			List<User> shopNames = new ArrayList<User>();
			User userTemp = null;
			if (bindString != null && bindString.size() > 0) {
				for(int i = 0; i < bindString.size(); i ++) {
					userTemp= userService.getUserByCustomerId(bindString.get(i));
					if (userTemp.getShopName() != null && userTemp.getShopName().length() > shopName.length()) {
						shopName = userTemp.getShopName();
					}
				}
			}
		}
		//加载主账号下的短信服务集合
		//smsServices = smsServiceService.getSMSServiceAllForToAdd(userId);
		
		//分仓、平台客服子账号
		if(("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType()))
			|| ("41".equals(curUser.getUserType()))){
			if("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType()))
				userId = Integer.valueOf(curUser.getUserSource());
			if("41".equals(curUser.getUserType()))
				userId = Integer.valueOf(curUser.getParentId());
			
			smsServices = smsServiceService.getSMSServiceAll(userId);
		} else { //非分仓
			userId = curUser.getParentId() == null ? curUser.getId() : curUser.getParentId();
			smsServices = smsServiceService.getSMSServiceAll(userId);
		}
		
		/*ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for(int i = 0 ; i < smsServices.size() ; i++){
			if("问题件通知".equals(smsServices.get(i).getName()) || "营销定制".equals(smsServices.get(i).getName())){
				ids.add(i);
			}
		}
		
		for(int i = 0 ; i < ids.size() ; i++){
			smsServices.remove(ids.get(i));
		}*/
		
		SMSTemplate info = new SMSTemplate();
		info.setUserId(userId);
		info.setSmsTypeId(serviceId); 
		try {
			templates = smsTemplateService.getTemplates(info);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		Integer templateCount = templates.size();
		for (SMSTemplate template : templates) {
			if (StringUtils.equals("N", template.getStatus())) {
				templateCount--;
			}
		}
		if(templateCount >= 7) { 
			smsTypeResult="false";
		} else {
			smsTypeResult = "true";
		}
		return "toAdd";
	}
	
	/**
	 * 添加短信模版
	 * @return
	 */
	public String add(){
		User curUser = super.readCookieUser();
		SMSTemplate template = new SMSTemplate();
		template.setSmsTypeId(serviceId);
		template.setName(name);
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		content = content.replaceAll("&amp;", "&");
		template.setContent(content);
		template.setIsDefault("N");
		template.setStatus("M");
		template.setSendCount(0);
		template.setUserId(this.getUseUserId());
		template.setCreateUserId(curUser.getId());
		
		try{
			boolean bool = smsTemplateService.addSMSTemplate(template);
			logger.info("添加短信模版成功与否："+bool);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		SMSService service =this.getSMSServiceById(template.getSmsTypeId());
		resultString = service.getName();
		return "add";
	}
	
	/**
	 * 点击查看模版信息_异步调用
	 * @return
	 */
	public String getOneTemplate(){
		
		try {
			template = smsTemplateService.getSMSTemplateById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		SMSService service = this.getSMSServiceById(template.getSmsTypeId());
		
		String updateTime = DateUtil.format(template.getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
		if(template.getUpdateTime() != null)template.setRemark(service.getName()+"="+updateTime);
		else template.setRemark(service.getName());
		
		return "getOneTemplate";
	}

	/**
	 * 删除模版(先判断是否是默认模版)_异步
	 * @return
	 */
	public String delTemplateById(){
		
		boolean b = smsTemplateService.delSMSTemplateById(id);
		logger.info("删除模版成功与否:"+b);
		if(b)resultString = "模版删除成功！";
		
		return "delTemplateById";
	}
	
	/**
	 *设置默认模版
	 * @return
	 */
	public String setDefault(){
		
		User curUser = super.readCookieUser();
		logger.info("设置templateId="+id+"为默认模版");
		
		boolean bool = smsTemplateService.updateSMSTemplateToDefault(id,serviceId);
		logger.info("设置默认模版是否成功："+bool);
		
		if(bool)resultString = "设置默认模版成功！";
		return "setDefault";
	}
	
	/**
	 * to修改模版
	 * @return
	 */
	public String toUpdate(){
		
		Integer userId = this.getUseUserId();
		
		User user = (User)userService.getUserById(userId); 
		if (user != null) {
			if (user.getShopName() == null) {
				shopName = "";
			} else {
				shopName = user.getShopName();
			}
		}
		if (user.getUserType().equals("1") && user.getUserType() == null) {
			List<String>  bindString = new ArrayList<String>();
			try{
				bindString = Resource.getBindedCustomerIdList(user);
			}catch(Exception e){
				logger.error("获取绑定用户异常："+e);
			}
			List<User> shopNames = new ArrayList<User>();
			User userTemp = null;
			if (bindString != null && bindString.size() > 0) {
				for(int i = 0; i < bindString.size(); i ++) {
					userTemp= userService.getUserByCustomerId(bindString.get(i));
					if (userTemp.getShopName() != null && userTemp.getShopName().length() > shopName.length()) {
						shopName = userTemp.getShopName();
					}
				}
			}
		}
		servicedto = this.getSMSServiceById(serviceId);
		try {
			template = smsTemplateService.getSMSTemplateById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return "update";
	}
	
	/**
	 * 修改模版
	 * @return
	 */
	public String update(){
		User curUser = super.readCookieUser();
		SMSTemplate template = new SMSTemplate();
		try {
			template = smsTemplateService.getSMSTemplateById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		template.setName(name);
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		content = content.replaceAll("&amp;", "&");
		template.setContent(content);
		template.setUpdateUserId(curUser.getParentId() == null ? curUser.getId() : curUser.getParentId());
		template.setIsDefault("N");
		template.setStatus("M");
		template.setSendCount(0);
		
		try{
			boolean bool = smsTemplateService.editSMSTemplate(template);
			logger.info("修改短信模版成功与否："+bool);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * 添加模版时选择短信类型下拉框时触发
	 * @return
	 */
	public String checkNum(){
		User curUser = super.readCookieUser();
		Integer userId = this.getUseUserId();
		
		SMSTemplate info = new SMSTemplate();
		info.setUserId(userId);
		info.setSmsTypeId(serviceId);
		
		try {
			templates = smsTemplateService.getTemplates(info);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("[添加模版]用户："+curUser.getId()+",短信模版类型ID："+serviceId+"下模版数量："+templates.size());
		
		Integer templateCount = templates.size();
		for (SMSTemplate template : templates) {
			if (StringUtils.equals("N", template.getStatus())) {
				templateCount--;
			}
		}
		if(templateCount >= 7)resultString = "抱歉！模版数只允许5个，无法添加。";
		else resultString = "";
		return "checkNum";
	}
	
	/**
	 * 查看审核失败原因
	 * @return
	 */
	public String getRemak(){
		
		try {
			template = smsTemplateService.getSMSTemplateById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		resultString = template.getRemark();
		return "getRemak";
	}
	
	/**
	 * 管理员 进入审核主页面
	 * @return
	 */
	public String templateAdmin(){
		resultString = "init";
		return "templateAdmin";
	}

	/**
	 * 管理员  模版页面方法
	 * 按条件查询返回集合
	 * @return
	 */
	public String queryList(){
		User curUser = super.readCookieUser();
		Integer userId = curUser.getParentId() == null ? curUser.getId() : curUser.getParentId();
		
		if("check".equals(flag))currentPage = 1;		
		if(pagination == null) pagination = new Pagination<SMSTemplate>(currentPage,10);
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("isAdmin", "1");
		params.put("name", serviceName);
		if(!"".equals(status))params.put("moduleStatus", status);
		else params.put("moduleStatus", "Y,M,N");
		if(!"".equals(shopName)){
			params.put("shopName", shopName);
		}
		if(startTime != null || !"".equals(startTime))params.put("startTime", startTime);
		if(endTime != null || !"".equals(endTime))params.put("endTime", endTime);
	
		//根据条件查询模版列表，分页
		templates = smsTemplateService.getSMSTemplateByUserId(params,pagination);
		
		//当没有输入店铺名称条件时
		for(SMSTemplate template : templates){
			User user = userService.getUserById(template.getUserId());
			template.setShopName(user.getShopName());
		}
		
		if(!"".equals(status))params.put("moduleStatus", status);
		else params.put("moduleStatus", "Y,M,N");
		
		Integer totalRecords = totalRecords = smsTemplateService.getSMSTemplateCountByUserId(params); 
		
		pagination.setTotalRecords(totalRecords);
		
		return "templateAdmin";
	}
	
	/**
	 * 管理员审核短信模版
	 * @return
	 */
	public String passByAdmin(){
		
		User curUser = super.readCookieUser();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("auditUser", curUser.getId());
		if("YES".equals(flag)){
			params.put("status", "Y");
			params.put("remark", "");
		}
		if("NO".equals(flag)){
			params.put("status", "N");
			params.put("remark", remark);
		}
		
		boolean bool = smsTemplateService.editTemplateOfAdminByParams(params);
		logger.info("管理员审核模版成功与否："+bool);
		
		return "templateAdmin";
	}
	
	/**
	 * [方法抽取]根据主账号id获取已开启服务
	 * @param curUser
	 * @return
	 */
	public List<SMSService> getServiceByUserId(User curUser){
		List<SMSService> smsServices = null;
		try {
			smsServices = smsServiceService.getSMSServicesByUserId(curUser.getParentId() == null ? curUser.getId() : curUser.getParentId());
		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		return smsServices;
	}
	
	/**
	 * [方法抽取]根据短息类型ID查询
	 * @param template
	 * @return
	 */
	private SMSService getSMSServiceById(Integer smsTypeId) {
		SMSService service = null;
		try {
			service = smsServiceService.getSMSServiceById(smsTypeId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return service;
	}

	
	/**
	 * [方法抽取]根据登陆账号获取主账号ID
	 * @return
	 */
	/*
	private Integer getUserId(){
		User curUser = super.readCookieUser();
		Integer userId = null;
		
		if("1".equals(curUser.getUserType()) && curUser.getParentId() != null) userId = curUser.getParentId(); //卖家账号子账号
		else if("4".equals(curUser.getUserType().trim()))  userId = curUser.getId(); //平台账号
		else if("41".equals(curUser.getUserType())) userId = curUser.getParentId(); //平台客服
		else if("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType())) userId = Integer.valueOf(curUser.getUserSource()); //平台账号创建的卖家子账号
		else if("1".equals(curUser.getUserType()) && "C".equals(curUser.getChildType())) userId = curUser.getId();//入驻企业账号
		else userId = curUser.getId(); //卖家主账号
		
		return userId;
	}
	*/
	
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
	
	
	private Integer getUserIdService(){
		User curUser = super.readCookieUser();
		Integer userId = null;
		
		if(("1".equals(curUser.getUserType())||"11".equals(curUser.getUserType())||"12".equals(curUser.getUserType())||"13".equals(curUser.getUserType())) && curUser.getParentId() != null) userId = curUser.getParentId(); //卖家账号子账号
		else if("4".equals(curUser.getUserType().trim()))  userId = curUser.getId(); //平台账号
		else if("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType())) userId = curUser.getId(); //分仓
		else if("1".equals(curUser.getUserType()) && "C".equals(curUser.getChildType())) userId = curUser.getId();//入驻企业账号
		else userId = curUser.getId(); //卖家主账号
		
		return userId;
	}
	
	/**
	 * 过滤模版文字
	 * @return
	 */
	public String toSmsTemplateEditFilter() {
		FilterResult filter = filterRuleService.filter(name+content,FilterRule.TYPE_ISSUEREPORT);
		boolean isvalid = filter.isvalid();
		String invalidWordsStr = filter.getInvalidWordsStr();
		jsons1.put("isvalid",isvalid);
		jsons1.put("invalidWordsStr", invalidWordsStr);
		return "jsonEdit";
	}
	
	/**
	 * 过滤模版文字
	 * @return
	 */
	public String toSmsTemplateAddFilter() {
		FilterResult filter = filterRuleService.filter(name+content,FilterRule.TYPE_ISSUEREPORT);
		boolean isvalid = filter.isvalid();
		String invalidWordsStr = filter.getInvalidWordsStr();
		jsons2.put("isvalid",isvalid);
		jsons2.put("invalidWordsStr", invalidWordsStr);
		return "jsonAddTemplate";
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
	
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public SMSTemplate getTemplate() {
		return template;
	}
	public void setTemplate(SMSTemplate template) {
		this.template = template;
	}
	public List<SMSTemplate> getTemplates() {
		return templates;
	}
	public void setTemplates(List<SMSTemplate> templates) {
		this.templates = templates;
	}
	public String getTabNum() {
		return tabNum;
	}
	public void setTabNum(String tabNum) {
		this.tabNum = tabNum;
	}
	public List<SMSService> getSmsServices() {
		return smsServices;
	}
	public void setSmsServices(List<SMSService> smsServices) {
		this.smsServices = smsServices;
	}

	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public SMSService getServicedto() {
		return servicedto;
	}
	public void setServicedto(SMSService servicedto) {
		this.servicedto = servicedto;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Pagination<SMSTemplate> getPagination() {
		return pagination;
	}
	public void setPagination(Pagination<SMSTemplate> pagination) {
		this.pagination = pagination;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public String getSmsTypeResult() {
		return smsTypeResult;
	}
	public void setSmsTypeResult(String smsTypeResult) {
		this.smsTypeResult = smsTypeResult;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}

	public Map getJsons1() {
		return jsons1;
	}

	public void setJsons1(Map jsons1) {
		this.jsons1 = jsons1;
	}

	public Map getJsons2() {
		return jsons2;
	}

	public void setJsons2(Map jsons2) {
		this.jsons2 = jsons2;
	}
}
