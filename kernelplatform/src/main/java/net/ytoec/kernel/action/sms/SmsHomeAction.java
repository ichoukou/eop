package net.ytoec.kernel.action.sms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.SMSServiceArea;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceAreaService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SMSTemplateService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**短信首页Action
 * @author yuyuezhong
 * @since 2012-07-09
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SmsHomeAction extends AbstractActionSupport{
	private static Logger logger = LoggerFactory.getLogger(SmsHomeAction.class);
	
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private SMSServiceService smsServiceService;
	@Inject
	private SMSTemplateService smsTemplateService;
	@Inject
	private SMSServiceAreaService smsServiceAreaService;
	@Inject
	private DredgeServiceService<DredgeService> dredgeServiceService;// 开通服务service 
	@Inject
	private UserService userService;
	
	private List<SMSService> serviceList;//所有短信服务
	private Integer serviceId;//短信服务ID
	private Integer smsUsecount;//可发短信数
	private Integer todayCount;//今日发送数
	private String monthAverage; //日均发送数
	private Integer monthTotal;//月累计发送
	
	private List<SMSTemplate> templateList;//所有模板
	private Integer defaultID;//默认模板ID
	private String resend;//是否自动重发
	
	private List<SMSServiceArea> serviceAreaList;//所有地区
	private String area;
	
	private String isOn;//是否开通
	
	private boolean openFlg;//不显示使用中的功能模块
	private boolean closeFlg;//不显示未使用的功能模块
	
	private String startTime;//短信查询开始时间
	private String endTime;//短信查询结束时间
	private String menuFlag;
	
	private String pos; // 判断是已使用还是未使用
	
	private Integer smsMessageCount;//短信数目
	
	public Integer getSmsMessageCount() {
		return smsMessageCount;
	}
	public void setSmsMessageCount(Integer smsMessageCount) {
		this.smsMessageCount = smsMessageCount;
	}
	private String enough; // 判断模版是否已经足够5个
	
	private String tips;//判断是点击暂停使用还是立即开启
	private String title;//判断是初次设置还是开启后设置
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	//短信首页显示
	public String homePage() {
		User curUser = super.readCookieUser();
		SMSMessage smsMessage = new SMSMessage();
		menuFlag = "sms_home";
		Integer userId = this.getUserId();
		
		//判断这个服务是否已经开通过
		boolean flag = smsPortService.supplyStatusOpenUp(userId);
		if(!flag) return "smsWelcome";
		
		//可发短信数
		smsMessage = smsPortService.supplySMSMessage(userId);
		if(smsMessage != null){
			smsMessageCount = smsMessage.getSmsUsecount();
			smsUsecount = smsMessageCount;
		}
		//如果改账号为分仓账号，可用短信数未0或者为空，查询平台用户的
		if(smsUsecount == null || smsUsecount == 0){
			if(curUser.getChildType() != null && "1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType())){
				if(curUser.getUserSource() != null){
					smsMessage = smsPortService.supplySMSMessage(Integer.parseInt(curUser.getUserSource()));
					if(smsMessage != null){
						smsUsecount = smsMessage.getSmsUsecount();
					}
				}
			}
		}
		
		List<Integer> subUserIds = getSubUserIds();
		
		//今日发送数
		todayCount = smsInfoService.getCountByToday(subUserIds, "0");
		if(todayCount == null){
			todayCount = 0;
		}
		
		//日均发送数
		Integer curDate = Integer.parseInt(DateUtil.toShortdd(new Date()));
		monthTotal = smsInfoService.getCountByToday(subUserIds, "1");
		if(monthTotal == null){
			monthTotal = 0;
			monthAverage = "0";
		}else{
			if(monthTotal%curDate != 0){
				DecimalFormat df = new DecimalFormat("#.##");
				Double number = (Double.valueOf(String.valueOf(monthTotal)))/curDate;
				monthAverage = df.format(number);
			}else{
				monthAverage = String.valueOf(monthTotal/curDate);
			}
		}
		
		HashMap<String,Object> param = this.getUserIdParten();
		//所有服务
		//如果是分仓账号，先判断平台的短信服务是否开启
		boolean bool = true;
		if(curUser.getChildType() != null && "B".equals(curUser.getChildType().trim())){
			bool = smsPortService.supplyStatusOpenUp((Integer) param.get("userId"));
		}
		
		serviceList = smsServiceService.getSMSService(param,bool);
		openFlg = false; //不显示使用中的功能模块
		closeFlg = false;//不显示未使用的功能模块
		for(SMSService ss: serviceList){
			if("Y".equals(ss.getIsOn())){
				openFlg = true;
			}else if("N".equals(ss.getIsOn())){
				closeFlg = true;
			}
		}
		
		startTime = DateUtil.dateArithmetic(new Date(),6);
	    endTime = DateUtil.dateArithmetic(new Date(),0);
		if("1".equals(curUser.getUserType()) && "B".equals(curUser.getChildType())){
			tips = curUser.getUserSource(); //存放平台账号
		}
	    
		return "home";
	}
	
	//点击"设置"
	public String smsSetting() throws Exception {
		menuFlag = "sms_home";
		User currentUser = super.readCookieUser();
		Integer userId = currentUser.getId();
		//如果是子账号，则用主账号ID
		if(currentUser.getParentId() != null){
			userId = currentUser.getParentId();
		}
		
		//模板的获取
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", this.getUseUserId());
		//根据名称查询
		SMSService service = smsServiceService.getSMSServiceById(serviceId);
		//params.put("smsTypeId", serviceId);
		params.put("name", service.getName());
		params.put("moduleStatus", "Y,S");
		params.put("shopName", null);
		params.put("isAdmin", null);
		templateList = smsTemplateService.getSMSTemplateByUserId(params,null);
		
		defaultID = null;
		for(int i=0; i<templateList.size(); i++){
			if("Y".equals(templateList.get(i).getIsDefault())){
				defaultID = templateList.get(i).getId();
				break;
			}
		}
		
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("userId", this.getUseUserId());
		params1.put("name", service.getName());
		params1.put("moduleStatus", "M,Y,N");
		params1.put("shopName", null);
		params1.put("isAdmin", null);
		List<SMSTemplate> templateList1 = smsTemplateService.getSMSTemplateByUserId(params1,null);
		
		if (templateList1.size() < 5) {
			enough = "yes";
		} else {
			enough = "no";
		}
		//地区的获取
		serviceAreaList = smsServiceAreaService.getSMSServiceAreaByParams(serviceId,userId);
		area = "";
		if(serviceAreaList == null || serviceAreaList.size() == 0){
			serviceAreaList = null;
		}else{
			String[] pros = {"山东","江苏","安徽","浙江","福建","上海","宁夏","新疆","青海","陕西","甘肃","湖北","湖南","河南","江西","四川","云南","贵州","西藏","重庆","北京","天津","河北","山西","内蒙古","广东","广西","海南","辽宁","吉林","黑龙江","香港","澳门","台湾"};
			List<String> proList = Arrays.asList(pros);
			StringBuffer sb = new StringBuffer();
			for(int j=0; j< proList.size(); j++){
				String pro = proList.get(j);
				for(int i=0; i<serviceAreaList.size(); i++){
					String areaName = Resource.getNameById(Integer.parseInt(serviceAreaList.get(i).getCode()));
					if(areaName.indexOf("省") > 0){
						areaName = areaName.split("省")[0];
					}else if(areaName.indexOf("内蒙古") >= 0){
						areaName = areaName.substring(0,3);
					}else if(areaName.indexOf("区") > 0){
						areaName = areaName.substring(0,2);
					}
					//数据库里的省在数组里的时候
					if(pro.equals(areaName)){
						sb.append(areaName);
						
						if(i != serviceAreaList.size() -1 ){
							sb.append("、");
						}
						serviceAreaList.remove(i);
						break;
					}
					
					
				}
			}
			
			area = sb.toString();
		}
		
		//是否自动重发
		SMSService smsService = smsServiceService.getSMSServiceById(serviceId);
		resend = smsService.getIsAutoSend();
		
		return "setting";
	}
	
	//点击"暂停"  或     "立即开通"成功
	public String pauseOrOpenService() throws Exception {
		User currentUser = super.readCookieUser();
		List<SMSService> smsServices  = new ArrayList<SMSService>();
		
		//卖家、卖家子账号、入驻企业账号  
		if(("1".equals(currentUser.getUserType()) || "11".equals(currentUser.getUserType())  
			|| "12".equals(currentUser.getUserType()) || "13".equals(currentUser.getUserType()))
			&& (currentUser.getChildType() == null || "C".equals(currentUser.getChildType()))){
			SMSService smsService = smsServiceService.getSMSServiceById(serviceId);
			smsServices.add(smsService);
		}
		
		//平台账号/平台客服账号
		if("4".equals(currentUser.getUserType()) || "41".equals(currentUser.getUserType())){
			SMSService smsService = smsServiceService.getSMSServiceById(serviceId);
			smsServices.add(smsService);
			
			if("41".equals(currentUser.getUserType())){
				currentUser = userService.getUserById(currentUser.getParentId());
			}
			
			//获取平台账号的分仓账号
			List<User> fencangList = userService.pingTaiSelect(currentUser, 1);
			if(fencangList == null || fencangList.size() <= 0){
				logger.info("平台账号："+currentUser.getUserName()+"没有分仓账号！");
			}else{
				/*开启(tips=1)：判断分仓账号是否存在该服务，如果存在又为关闭时则修改，如果不存在则插入数据  
				 *暂停使用(tips=0)：暂停使用
				 **/
				Map<String,Object> params = new HashMap<String,Object>();
				if("1".equals(tips)){
					for(User user : fencangList){
						params.put("userId", user.getId());
						params.put("name", smsService.getName());
						SMSService serviceFen = smsServiceService.getSMSServiceByServiceNameFenCang(params);
						if(serviceFen != null){
							if("N".equals(serviceFen.getIsOn())){
								//删除
								logger.info("平台账号开启服务时分仓账号有此服务但是没有开启，需要把分仓的服务删除！");
								boolean b = smsServiceService.delSMSServiceById(serviceFen.getId());
								logger.info("删除分仓服务id="+serviceFen.getId()+"成功与否："+b);
							}else{
								logger.info("平台账号的分仓账号已开启");
							}
						}
					}
				}
				if("0".equals(tips)){
					logger.info("平台关闭服务");
				}
			}
		}
		if("1".equals(currentUser.getUserType()) && "B".equals(currentUser.getChildType())){
			/*判断这个服务是平台的还是分仓的，是平台的话则插入分仓短信服务，如果是分仓的话则修改isOn */
			SMSService smsService = smsServiceService.getSMSServiceById(serviceId);
			
			if("1".equals(tips)){
				//服务属平台
				if(currentUser.getUserSource().equals(smsService.getUserId()+"")){
					SMSService service = new SMSService();
					service.setName(smsService.getName());
					service.setImageUrl(smsService.getImageUrl());
					service.setIntroduction(smsService.getIntroduction());
					service.setSendTimeType(smsService.getSendTimeType());
					service.setIsAutoSend(smsService.getIsAutoSend());
					service.setUserId(currentUser.getId());
					service.setCreateTime(new Date());
					service.setCreateUserId(currentUser.getId());
					service.setIsOn("Y");
					
					logger.info("分仓开启服务为平台的服务时，需插入分仓服务数据");
					boolean b = smsServiceService.addSMSService(service);
					logger.info("添加成功与否："+b);
				}
				//服务属分仓
				if(currentUser.getId() == smsService.getUserId()){
					SMSService service = new SMSService();
					service.setId( smsService.getId());
					service.setIsOn("Y");
					service.setUpdateUserId(currentUser.getId());
					
					logger.info("分仓开启服务为分仓的关闭服务时，需修改分仓服务数据");
					boolean b = smsServiceService.updateServiceForFenCang(service);
					logger.info("修改成功与否："+b);
				}
			}
			if("0".equals(tips)){
				boolean b = smsServiceService.delSMSServiceById(smsService.getId());
				logger.info("分仓暂停使用自己的服务，删除服务成功与否："+b);
			}
		}
		
		for(SMSService smsService : smsServices){
			smsService.setIsOn(isOn);
			smsService.setUpdateTime(new Date());
			smsService.setUpdateUserId(currentUser.getId());
			
			try {
				smsServiceService.editSMSService(smsService);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		return "pauseSuccess";
	}
	
	//点击"立即使用"
	public String openService() throws Exception {
		return smsSetting();
	}
	
	public String saveSetting() throws Exception{
		
		User currentUser = super.readCookieUser();
		Integer userId = currentUser.getId();
		//如果是子账号，则用主账号ID
		if(currentUser.getParentId() != null){
			userId = currentUser.getParentId();
		}
		
		//保存默认模板
		smsTemplateService.updateSMSTemplateToDefault(defaultID,serviceId);
		
		
		//查询数据库中的地区保存
		serviceAreaList = smsServiceAreaService.getSMSServiceAreaByParams(serviceId,userId);
		//保存地区
		area = area.replace("\n", "");
		area = area.replace("\r", "");
		area = area.replace("\t", "");
		String[] areas = area.split("、");
		for(int i=0; i<areas.length; i++){
			boolean isSave = true;
			String pro = areas[i];
			if("香港".equals(areas[i]) || "澳门".equals(areas[i])){
				pro = areas[i] + "特别行政区";
			}
			Integer code = Resource.getCodeByName(pro);
			for(SMSServiceArea area:serviceAreaList){
				if(code.equals(Integer.parseInt(area.getCode()))) {
					isSave = false;
					break;
				}
			}
			if(!isSave){
				continue;
			}
			Date date = new Date();
			SMSServiceArea serviceArea = new SMSServiceArea();
			serviceArea.setCode(String.valueOf(code));
			serviceArea.setCreateTime(date);
			serviceArea.setCreateUserId(userId);
			serviceArea.setSmsTypeId(serviceId);
			serviceArea.setUpdateTime(date);
			serviceArea.setUpdateUserId(currentUser.getId());
			smsServiceAreaService.addSMSServiceArea(serviceArea);
		}
		
		for(int i=0; i<serviceAreaList.size(); i++){
			boolean isDelete = true;
			Integer dataCode = Integer.parseInt(serviceAreaList.get(i).getCode());
			for(int j=0; j<areas.length; j++){
				if(dataCode.equals(Resource.getCodeByName(areas[j]))){
					isDelete = false;
					break;
				}
			}
			if(isDelete){
				smsServiceAreaService.deleteSMSServiceArea(serviceAreaList.get(i).getId());
			}
		}
		
		//保存是否自动重发
		SMSService smsService = smsServiceService.getSMSServiceById(serviceId);
		smsService.setIsAutoSend(resend);
		smsService.setUpdateUserId(currentUser.getId());
		smsServiceService.editSMSService(smsService);
		
		//立即开通
		isOn = "Y";
		if("down".equals(pos.trim()))
			tips = "1";
		
		return pauseOrOpenService();
		
		
	}
	
	public String popedomVerdict(){
		User currentUser = super.readCookieUser();
		SMSService smsService = new SMSService();
		try {
			smsService = smsServiceService.getSMSServiceById(serviceId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		//分仓或平台客服或卖家主账号或卖家子账号的
		logger.info(currentUser+"-"+smsService);
		logger.info(smsService.getUserId() +"--"+ currentUser.getId() +"--"+currentUser.getParentId());
		try{
			Integer uid = currentUser.getParentId() == null ? currentUser.getId() : currentUser.getParentId(); 
			if(smsService.getUserId() == uid ){
				putMsg(JsonResponse.INFO_TYPE_MSG,true,"","");
			}
			//平台的
			if(currentUser.getUserSource().equals(smsService.getUserId()+"")){
				putMsg(JsonResponse.INFO_TYPE_MSG,false,"","");
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return "jsonRes";
	}
	
	/**
	 * [方法抽取]根据登陆账号获取主账号ID、分仓获取平台账号ID、入驻企业账号获取本身ID
	 * @return
	 */
	private HashMap<String,Object> getUserIdParten(){
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		User curUser = super.readCookieUser();
		Integer userId = null;
		String flag = "";
		
		if(curUser.getParentId() != null){
			userId = curUser.getParentId(); //卖家账号子账号、平台客服账号
			flag = "N";
		}
		else if("4".equals(curUser.getUserType().trim())){
			userId = curUser.getId(); //平台账号
			flag = "N";
		}
		else if(curUser.getChildType() != null && "B".equals(curUser.getChildType().trim())){
			userId = Integer.valueOf(curUser.getUserSource()); //平台账号创建的分仓账号
			flag = "Y";
			resultMap.put("user", curUser);
		}
		else{
			userId = curUser.getId(); //卖家主账号、入驻企业账号
			flag = "N";
		}
		
		resultMap.put("userId", userId);
		resultMap.put("flag", flag);
		
		return resultMap;
	}
	
	/**
	 * [方法抽取]根据登陆账号获取主账号ID、其它均获取自身ID
	 * @return
	 */
	private Integer getUserId(){
		User curUser = super.readCookieUser();
		Integer userId = null;
		
		if(curUser.getParentId() != null) userId = curUser.getParentId(); //卖家账号子账号-传主账号
		else userId = curUser.getId(); //其他（卖家主账号、平台账号、分仓账号、入驻账号）均传ID
		
		return userId;
	}
	
	/**
	 * [方法抽取]根据登陆账号获取主账号ID
	 * @return
	 */
	/*
	private Integer getUserIdMain(){
		User curUser = super.readCookieUser();
		Integer userId = null;
		
		if(curUser.getParentId() != null) userId = curUser.getParentId(); //卖家账号子账号
		else if("4".equals(curUser.getUserType().trim()))  userId = curUser.getId(); //平台账号
		else if(curUser.getChildType() != null) userId = Integer.valueOf(curUser.getUserSource()); //平台账号创建的卖家子账号
		else userId = curUser.getId(); //卖家主账号
		
		return userId;
	}
	*/

	/**
	 * 获取当前用户下的所有关联子ID集合
	 * 卖家ID  , 平台ID+分仓ID ,其他为自身ID
	 * @return
	 */
	private List<Integer> getSubUserIds() {
		List<Integer> userIds = new ArrayList<Integer>();
		User currentUser = super.readCookieUser();   
		int userType = getUserType();
		
		if(userType==1) {  //卖家,(包括平台下的B:分仓 C:企业 )
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
				}
			}
		}
		if(userType==2) {  //网点
			userIds.add(currentUser.getId());
		}
		if(userType==3) {   //admin
			//userIds.add(currentUser.getId());
		}
		
		return userIds;
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
		}else if("4".equals(userType)) {
			return 4;
		}
		return 0;
	}
	
	/**
	 * 获取查询使用的用户ID
	 * 分3类ID  第一类：主账户ID（父ID）  第二类：子账户ID 第三类：平台ID (分仓使用平台)
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
	
	public List<SMSService> getServiceList() {
		return serviceList;
	}
	public void setServiceList(List<SMSService> serviceList) {
		this.serviceList = serviceList;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getSmsUsecount() {
		return smsUsecount;
	}
	public void setSmsUsecount(Integer smsUsecount) {
		this.smsUsecount = smsUsecount;
	}
	public Integer getTodayCount() {
		return todayCount;
	}
	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}
	public String getMonthAverage() {
		return monthAverage;
	}
	public void setMonthAverage(String monthAverage) {
		this.monthAverage = monthAverage;
	}
	public List<SMSTemplate> getTemplateList() {
		return templateList;
	}
	public void setTemplateList(List<SMSTemplate> templateList) {
		this.templateList = templateList;
	}
	public List<SMSServiceArea> getServiceAreaList() {
		return serviceAreaList;
	}
	public void setServiceAreaList(List<SMSServiceArea> serviceAreaList) {
		this.serviceAreaList = serviceAreaList;
	}
	public Integer getDefaultID() {
		return defaultID;
	}
	public void setDefaultID(Integer defaultID) {
		this.defaultID = defaultID;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getResend() {
		return resend;
	}
	public void setResend(String resend) {
		this.resend = resend;
	}
	public String getIsOn() {
		return isOn;
	}
	public void setIsOn(String isOn) {
		this.isOn = isOn;
	}
	public Integer getMonthTotal() {
		return monthTotal;
	}
	public void setMonthTotal(Integer monthTotal) {
		this.monthTotal = monthTotal;
	}
	public boolean isOpenFlg() {
		return openFlg;
	}
	public void setOpenFlg(boolean openFlg) {
		this.openFlg = openFlg;
	}
	public boolean isCloseFlg() {
		return closeFlg;
	}
	public void setCloseFlg(boolean closeFlg) {
		this.closeFlg = closeFlg;
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
	public String getMenuFlag() {
		return menuFlag;
	}
	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getEnough() {
		return enough;
	}
	public void setEnough(String enough) {
		this.enough = enough;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
	
}
