package net.ytoec.kernel.action.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.dataobject.SMSBuyersSearch;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.SMSBuyersSearchService;
import net.ytoec.kernel.service.SMSBuyersService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceMarketService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SMSTemplateService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class SmsServiceMarketAction extends AbstractActionSupport {
	
	private Logger logger=Logger.getLogger(SmsServiceMarketAction.class);
	
	private static final String SMS_TYPE_NAME = "营销定制";
	
	@Inject
	private SMSServiceMarketService smsServiceMarketService;
	
	@Inject
	private SMSPortService smsPortService; 
	
	@Inject
	private SMSTemplateService smsTemplateService;
	
	@Inject
	private SMSBuyersSearchService smsBuyersSearchService;
	
	@Inject
	private SMSBuyersService smsBuyersService;
	
	@SuppressWarnings("rawtypes")
	@Inject
	private UserService userService;
	
	@Inject
	private SMSInfoService smsInfoService;
	
	@Inject
	private SMSServiceService smsServiceService;
	
	@Inject
	private PaymentService<Payment> paymentService;
	
	@Inject
	private MessageService<Message> messageService;
	
	/**
	 * 选择框的搜索器名称
	 */
	private String searchName;
	
	/**
	 * 搜索器名称
	 */
	private String searchName2; 
	
	/**
	 * 上次交易时间
	 */
	private String theLastTradeTime;
	
	/**
	 * 上次活动时间
	 */
	private String theLastMarketTime;
	
	/**
	 * 最小交易额
	 */
	private String tradeAmountMin;
	
	/**
	 * 最大交易额
	 */
	private String tradeAmountMax;
	
	/**
	 * 最小交易量
	 */
	private String tradeCountMin;
	
	/**
	 * 最大交易量
	 */
	private String tradeCountMax;
	
	/**
	 * 用户所在省
	 */
	private String receiverProvince;
	
	/**
	 * 用户所在市
	 */
	private String receiverCity;
	
	/**
	 * 用户所在区
	 */
	private String receiverDistrict;
	
	private String provinceName;
	
	private String cityName;
	
	private String areaName;
	
	/**
	 * 会员等级
	 */
	private String userGrade;
	
	/**
	 * 当前用户发短信总数
	 */
	private Integer sendCount;
	
	/**
	 * 当前用户使用的模板ID
	 */
	private String templateId;

	/**
	 * 当前用户可发短信数
	 */
	private Integer smsUsecount = 0;
	
	/**
	 * 搜索器变量
	 */
	private SMSBuyersSearch smsBuyersSearch = new SMSBuyersSearch();
	
	/**
	 * 会员变量
	 */
	private SMSBuyers smsBuyers = new SMSBuyers();
	
	/**
	 * 当前用户的角色类型
	 */
	private String userType;
	
	/**
	 * 当前用户购买短信套餐返回的结果
	 */
	private Integer portBuy;
	
	/**
	 * 当前用户购买短信套餐的种类
	 */
	private String portName;
	
	/**
	 * 当前用户购买短信套餐的数量
	 */
	public Integer portNum;
	
	/**
	 * 当前用户购买短信套餐的价格
	 */
	public double portPrice;
	
	/**
	 * 存放当前用户的模板
	 */
	private List<SMSTemplate> smsTemplateList = new ArrayList<SMSTemplate>();
	
	/**
	 * 存放当前用户的搜索器
	 */
	private List<SMSBuyersSearch> smsBuyerSearchList = new ArrayList<SMSBuyersSearch>();
	
	/**
	 * 存放当前用户的搜索结果
	 */
	private List<SMSBuyers> smsBuyersList = new ArrayList<SMSBuyers>();
	
	/**
	 * 存放当前用户发送的短信编号以及短信数量
	 */
	private Map<String,Integer> smsInfoMap = new HashMap<String,Integer>();
	
	private List<SMSService> smsTypes;   //短信类型
	
	private String searchInput;   //联系人,手机号,会员名
	
	private String flag;   //判断进入第2步,是否查询,从第一步,跳转到第二步,不做查询,FLAG=0
	
	private String msg;   
	
	private String customerId;  //all 代表所有选中用户，非all即为 选中用户
	
	private String smsTypeId;
	
	//ajax请求返回给页面的信息
	private String jsonStr;
	
	private Integer currentPage = 1;
	private Pagination pagination; //分页对象
	
	private int pageCount; //一页的数量
	private int allCount; //查询总数量
	private String pageChecked; //当前页是否选中
	private int customerNumber = 0; //短信发送的用户数量
	
	/**
	 * 短信服务-设置（开通短信，短信不足，跳转购买，购买完成，返回服务，所需要使用的参数）
	 */
	private String serviceId;  //服务ID
	private String pos;    //POS
	
	/**
	 * 智能查件 查询参数  跳转使用
	 */
    private String currentPage2;
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
    
	/**
	 * 营销定制[页面] 第一步
	 * @return
	 */
	public String smsMarketPageStep1() {
		
		//判断这个服务是否已经开通过
		boolean theFlag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!theFlag){
			return "smsWelcome";
		}

		//登录用户ID
		int userId = getMainUserId();
		
		//根据当前用户的userId查询该用户的可使用短信数
		SMSMessage smsMessage = smsPortService.supplySMSMessage(userId);
		if(smsMessage == null) {
			logger.error("sMSMessage对象为null！");
		} else {
			smsUsecount = smsMessage.getSmsUsecount();
		}
		
		//短信类型ID获取
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name",SMS_TYPE_NAME);
		params.put("userId", userId);
		SMSService smsType = smsServiceService.getSMSServiceByServiceName(params);
		if(smsType!=null) {
			smsTypeId = smsType.getId() + "";
		}else {
			smsTypeId = "";
		}
		
		//获取当前用户所有可用模板
		getAllTemplates();
		
		return "smsMarketPageStep1";
	}
	
	/**
	 * 营销定制[页面] 第二步
	 * @return
	 */
	public String smsMarketPageStep2() {
		
		//判断这个服务是否已经开通过
		boolean theFlag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!theFlag){
			return "smsWelcome";
		}
		
		//获取当前用户可用的搜索器
		getAllSearches(); 

		msg = "请选择查询条件，来查询你的会员记录!";   //提示信息
		//设置短信默认模版
		setSmsTemplate();   //跳转第2步时,执行
	
		//分页参数
		if(pagination == null) pagination = new Pagination(currentPage,pageNum);
		pagination.setTotalRecords(0);
		
		//通过菜单进入,不做查询处理
		if("0".equals(flag)) {
			msg = "请选择查询条件，来查询你的会员记录!";   //提示信息
			return "smsMarketPageStep2";
		}

		//提示信息
		msg = "抱歉，找不到你查询的短信记录，请确认查询条件是否有误!";
		
		Map<String, Object> params = getBuyersParams();
		allCount = smsBuyersService.getSMSBuyersCountByPamams(params);

		//设置分页,并查询结果
		pageCount = pageNum;
		if((currentPage-1)==allCount/pageNum) {
			pageCount = allCount-pagination.getStartIndex();
		}
		pagination.setTotalRecords(allCount);
		params.put("startIndex", pagination.getStartIndex());
		params.put("pageNum", pageNum);	
		smsBuyersList = smsBuyersService.getSMSBuyersByPamams(params);

		return "smsMarketPageStep2";
	}
	
	/**
	 * 营销定制[页面] 第三步
	 * @return
	 */
	public String smsMarketPageStep3() {
		
		//判断这个服务是否已经开通过
		boolean theFlag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!theFlag){
			return "smsWelcome";
		}
		
		//根据当前用户的userId查询该用户的可使用短信数
		SMSMessage smsMessage = smsPortService.supplySMSMessage(getMainUserId());
		if(smsMessage != null) {
			smsUsecount = smsMessage.getSmsUsecount();
		}
		
		//实际所选用户数量
		if("all".equals(customerId)) {
			Map<String, Object> params = getBuyersParams();
			customerNumber = smsBuyersService.getSMSBuyersCountByPamams(params);
		}else {
			customerNumber = customerId.split(",").length;
		}
		
		return "smsMarketPageStep3";
	}
	
	/**
	 * 获取可用短信数量是否足够  AJAX
	 * @return
	 */
	public String hasEnoughSms() {
		
		//根据当前用户的userId查询该用户的可使用短信数
		SMSMessage smsMessage = smsPortService.supplySMSMessage(getMainUserId());
		if(smsMessage != null) {
			smsUsecount = smsMessage.getSmsUsecount();
		}
		
		//短信数量大于用户数量,表示短信足够,否则不够
		if(smsUsecount>customerNumber) {
			jsonStr = "true";
		}else {
			jsonStr = "false";
		}
		
		return "jsondata";
	}
	
	/**
	 * 设置短信默认模版 根据templateId
	 */
	private boolean setSmsTemplate() {
		//短信类型ID获取
		int smsTypeId = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name",SMS_TYPE_NAME);
		params.put("userId", getMainUserId());
		SMSService smsType = smsServiceService.getSMSServiceByServiceName(params);
		if(smsType!=null) {
			smsTypeId = smsType.getId();
		}
		//这个不应该根据模版ID获取 ?  默认为0?
		return smsTemplateService.updateSMSTemplateToDefault(Integer.valueOf(templateId), smsTypeId);
	}
	
	/**
	 * 获取所有可用模版
	 */
	public void getAllTemplates() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", getMainUserId());
		params.put("moduleStatus", "Y,S");
		params.put("smsTypeId", smsTypeId);
		smsTemplateList = smsTemplateService.getSMSTemplateByUserId(params,null);
	}
	
	/**
	 * 给所选用户发送短信[实际流程为,保存数据到数据库,由定时器触发发送]
	 * @return
	 */
	public String sendSms() {
		
		//获取用户选中模版
		SMSTemplate smsTemplate;
		try {
			smsTemplate = smsTemplateService.getSMSTemplateById(Integer.valueOf(templateId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
		
		//判断是选中用户ID发送,还是所有用户ID发送
		boolean isSendOk = false;
		int sendCount = 0;
		if("all".equals(customerId)) {
			Map<String, Object> params = getBuyersParams();
			smsBuyersList = smsBuyersService.getSMSBuyersByPamams(params);
			for(SMSBuyers smsBuyers:smsBuyersList) {	
				isSendOk = smsStackSave(smsBuyers,smsTemplate);
				if(isSendOk) {
					sendCount++;
				}
			}	
		}else {  	//所选用户发送
			String[] ids = customerId.split(",");
			String id;
			for(int i=0;i<ids.length;i++) {
				id = ids[i];
				if(StringUtils.isNotBlank(id)) {
					try {
						smsBuyers = smsBuyersService.getSMSBuyersById(Integer.parseInt(id));
					} catch (NumberFormatException e) {
						logger.error("方法sendSms() -> 无效ID");
					} catch (Exception e) {
						logger.error("方法sendSms() -> 无效ID");
					}
					isSendOk = smsStackSave(smsBuyers,smsTemplate);
					if(isSendOk) {
						sendCount++;
					}
				}
			}
		}
		
		/*
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("id", templateId);
		params.put("sendCount", sendCount);
		
		try {
			smsTemplateService.updateSendCountById(params);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}*/
		
		putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"短信已发送!","");	
		
		return "jsonRes";
	}
	
	/**
	 * 保存短信信息到数据库[由定时器触发发送]
	 * @param smsInfo
	 * @return
	 */
	private boolean smsStackSave(SMSBuyers smsBuyer,SMSTemplate smsTemplate) {
		
		if(smsTemplate==null) {
			return false;
		}
		
		//保存参数
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId",getUseUserId());
		params.put("content", smsTemplate.getContent());
		params.put("shopName", getUserShopName());
		params.put("smsTypeId", smsTemplate.getSmsTypeId());
		params.put("templateId", smsTemplate.getId());
		params.put("buyName", smsBuyer.getReceiverName());
		params.put("mobile", smsBuyer.getReceiverMobile());
		params.put("loginName", smsBuyer.getBuyerAccount());
	
		try {
			smsInfoService.saveInfoToQueueForMarket(params);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			return false;			
		}			
		return true;	
	}

	/**
	 * 获取当前用户可用的搜索器
	 */
	public void getAllSearches() {
		smsBuyerSearchList = smsBuyersSearchService.getSMSBuyersSearchByUserId(getMainUserId());
	}

	
	/**
	 * 当前用户获取搜索会员的条件
	 */
	public Map<String, Object> getBuyersParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		//登录USER
		User currentUser = super.readCookieUser();
		//绑定关联门店,并获取他们的ID集合
		List<Integer> userIds = getBindUserIds(currentUser);
		params.put("userIds", userIds);
		
		//List<Integer> bindedUserIdList = Resource.getBindedUserIdList(super.readCookieUser());
		//if(bindedUserIdList!=null&&bindedUserIdList.size()>0) {
		//	params.put("userIds", bindedUserIdList);
		//}
		
		if(StringUtils.isNotBlank(userGrade)) {
			params.put("userGrade", userGrade);
		}
		if(StringUtils.isNotBlank(theLastTradeTime)) {
			if("0".equals(theLastTradeTime)) {
				params.put("theLastTradeTime", "");
			} else if("1".equals(theLastTradeTime)) {
				params.put("theLastTradeTime", 7);
			} else if("2".equals(theLastTradeTime)) {
				params.put("theLastTradeTime", 14);
			} else {
				params.put("theLastTradeTime", 30);
			}
		}
		if(StringUtils.isNotBlank(theLastMarketTime)) {
			if("0".equals(theLastMarketTime)) {
				params.put("theLastMarketTime", "");
			} else if("1".equals(theLastMarketTime)) {
				params.put("theLastMarketTime", 7);
			} else if("2".equals(theLastMarketTime)) {
				params.put("theLastMarketTime", 14);
			} else {
				params.put("theLastMarketTime", 30);
			}
		}
		//金额
		if(StringUtils.isNotBlank(tradeCountMin)) {
			params.put("tradeCountMin2", tradeCountMin);
		}
		if(StringUtils.isNotBlank(tradeCountMax)) {
			params.put("tradeCountMax2", tradeCountMax);
		}
		if(StringUtils.isNotBlank(tradeAmountMin)) {
			params.put("tradeAmountMin2", tradeAmountMin);
		}
		if(StringUtils.isNotBlank(tradeAmountMax)) {
			params.put("tradeAmountMax2",tradeAmountMax);
		}
		/*
		if(StringUtils.isNotBlank(tradeCountMin) && StringUtils.isNotBlank(tradeCountMax)) {
			if(Integer.parseInt(tradeCountMin) >= 0 && Integer.parseInt(tradeCountMax) > 0) {
				if(Integer.parseInt(tradeCountMin) <= Integer.parseInt(tradeCountMax)) {
					params.put("tradeCountMin", Integer.parseInt(tradeCountMin));
					params.put("tradeCountMax", Integer.parseInt(tradeCountMax));
				} else {
					logger.error("输入的最小交易量必须小于最大交易量！");
				}
			} else {
				logger.error("输入的交易量必须为正数！");
			}
		}
		if(StringUtils.isNotBlank(tradeAmountMin) && StringUtils.isNotBlank(tradeAmountMax)) {
			if(Double.parseDouble(tradeAmountMin) >= 0 && Double.parseDouble(tradeAmountMax) > 0) {
				if(Double.parseDouble(tradeAmountMin) <= Double.parseDouble(tradeAmountMax)) {
					params.put("tradeAmountMin", Double.parseDouble(tradeAmountMin));
					params.put("tradeAmountMax", Double.parseDouble(tradeAmountMax));
				} else {
					logger.error("输入的最小交易额必须小于最大交易额！");
				}
			} else {
				logger.error("输入的交易额必须为正数！");
			}
		}
		*/
		if(StringUtils.isNotBlank(provinceName)) {
			params.put("receiverProvince", provinceName);
		}
		if(StringUtils.isNotBlank(cityName)) {
			params.put("receiverCity", cityName);
		}
		if(StringUtils.isNotBlank(areaName)) {
			params.put("receiverDistrict", areaName);
		} 
		/*
		if(StringUtils.isNotBlank(smsBuyers.getReceiverName())) {
			params.put("receiverName", smsBuyers.getReceiverName());
		} 
		if(StringUtils.isNotBlank(smsBuyers.getReceiverMobile())) {
			params.put("receiverMobile", smsBuyers.getReceiverMobile());
		} 
		if(StringUtils.isNotBlank(smsBuyers.getBuyerAccount())) {
			params.put("buyerAccount", smsBuyers.getBuyerAccount());
		} 
		*/
		
		//modif by wjz
		setMixSearch(params);

		params.put("orderByCol", "");
		return params;
	}
	
	/**
	 * 保存当前用户要保存的搜索器 AJAX
	 */
	public String saveBuyersSearch() {
		int userId = getMainUserId();
		Date date = new Date();
		StringBuffer stringBuffer = saveSearchParams();
		smsBuyersSearch.setSearchCondition(stringBuffer.toString());
		smsBuyersSearch.setSearchName(searchName2);
		smsBuyersSearch.setUpdateUserId(userId);
		smsBuyersSearch.setCreateUserId(userId);
		smsBuyersSearch.setCreateTime(date);
		smsBuyersSearch.setUpdateTime(date);
		
		smsBuyerSearchList = smsBuyersSearchService.saveSMSBuyersSearch(smsBuyersSearch);
		
		//保存对象所得ID
		String id = smsBuyersSearch.getId() + "";
		
		putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,id,"");
		
		return "jsonRes";
	}
	
	/**
	 * 搜索器选择页面AJAX
	 * @return
	 */
	public String smsBuyerSearchSelectPage() { 
		getAllSearches();
		return "smsBuyerSearchSelectPage";
	}
	
	/**
	 * 当前用户获取保存搜索器的条件
	 */
	public StringBuffer saveSearchParams() {
		StringBuffer stringBuffer = new StringBuffer();
		if(StringUtils.isNotBlank(searchName)) {
			stringBuffer.append(searchName);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(theLastTradeTime)) {
			if("0".equals(theLastTradeTime)) {
				stringBuffer.append("");
			} else if("1".equals(theLastTradeTime)) {
				stringBuffer.append("7");
			} else if("2".equals(theLastTradeTime)) {
				stringBuffer.append("14");
			} else {
				stringBuffer.append("30");
			}
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(theLastMarketTime)) {
			if("0".equals(theLastMarketTime)) {
				stringBuffer.append("");
			} else if("1".equals(theLastMarketTime)) {
				stringBuffer.append("7");
			} else if("2".equals(theLastMarketTime)) {
				stringBuffer.append("14");
			} else {
				stringBuffer.append("30");
			}
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(receiverProvince)) {
			stringBuffer.append(receiverProvince);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(receiverCity)) {
			stringBuffer.append(receiverCity);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(receiverDistrict)) {
			stringBuffer.append(receiverDistrict);
		} 
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(userGrade)) {
			stringBuffer.append(userGrade);
		} 
		stringBuffer.append("|");
		//金额
		if(StringUtils.isNotBlank(tradeCountMin)) {
			stringBuffer.append(tradeCountMin);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(tradeCountMax)) {
			stringBuffer.append(tradeCountMax);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(tradeAmountMin)) {
			stringBuffer.append(tradeAmountMin);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(tradeAmountMax)) {
			stringBuffer.append(tradeAmountMax);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(searchInput)) {
			stringBuffer.append(searchInput);
		}
		stringBuffer.append("|");
		if(StringUtils.isNotBlank(searchName2)) {
			stringBuffer.append(searchName2);
		}
		stringBuffer.append("|");
		/*
		if(StringUtils.isNotBlank(tradeCountMin) && StringUtils.isNotBlank(tradeCountMax)) {
			if(Integer.parseInt(tradeCountMin) > 0 && Integer.parseInt(tradeCountMax) > 0) {
				if(Integer.parseInt(tradeCountMin) < Integer.parseInt(tradeCountMax)) {
					stringBuffer.append(tradeCountMin);
					stringBuffer.append(tradeCountMax);
				} else {
					logger.error("输入的最小交易量必须小于最大交易量！");
				}
			} else {
				logger.error("输入的交易量必须为正数！");
			}
		}
		if(StringUtils.isNotBlank(tradeAmountMin) && StringUtils.isNotBlank(tradeAmountMax)) {
			if(Double.parseDouble(tradeAmountMin) > 0 && Double.parseDouble(tradeAmountMax) > 0) {
				if(Double.parseDouble(tradeAmountMin) < Double.parseDouble(tradeAmountMax)) {
					stringBuffer.append(tradeAmountMin);
					stringBuffer.append(tradeAmountMax);
				} else {
					logger.error("输入的最小交易额必须小于最大交易额！");
				}
			} else {
				logger.error("输入的交易额必须为正数！");
			}
		}
		*/

		return stringBuffer;
	}
	
	/**
	 * 当前用户购买短信套餐
	 */
	public String buyPorts() {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = super.readCookieUser();
		Integer userId = null;
		if(("").equals(user.getParentId()) || user.getParentId() == null) {
			params.put("userId", user.getId());
			userId = user.getId();
		} else {
			params.put("userId", user.getParentId());
			userId = user.getParentId();
		}
		params.put("SMSnum", portNum);
		params.put("prince", portPrice);
		params.put("name", portName);
		params.put("createrId", user.getId());//操作人ID
		smsPortService.sendSMSForMoney(userId,portName);//发送余额提醒,交易提醒
		portBuy = smsPortService.supplyRecharge(params);
        //发送消息
		boolean result = sendMessag(userId);
		if(!result){
			logger.error("余额不足消息提醒失败");
		}
		return "portJson";
	}
	
	/**
	 * 当前用户进入购买短信套餐页面
	 */
	public String inBuyPorts() {
		//判断这个服务是否已经开通过
		boolean theFlag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!theFlag){
			return "smsWelcome";
		}
		return "inBuyPorts";
	}

	/**
	 * 当前用户发送短信
	 */
	@SuppressWarnings("unchecked")
	public String sendPorts() {
		User user = userService.getUserById(super.readCookieUser().getId());
		SMSInfo sMSInfo = smsInfoService.getSMSInfoById(super.readCookieUser().getId());
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(user.getMobilePhone()) && StringUtils.isNotBlank(sMSInfo.getContent())) {
			params.put("userId", super.readCookieUser().getId());
			params.put("mobilePhone", user.getMobilePhone());
			params.put("content", sMSInfo.getContent());
		} else {
			logger.error("传入的内容为null！");
		}
		//smsInfoMap = smsPortService.supplySendSMS(params);//不再以此方法发送短信[合作方改变]
		smsInfoMap.put("sendId", sMSInfo.getId());
		smsInfoMap.put("sendSMSCount", sMSInfo.getCount());
		return "sendPorts";
	}

	
	/**
	 * 根据规则验证前台的 searchInput 属于哪种类型  (联系人,会员名),手机号
	 * @param params
	 */
	private void setMixSearch(Map<String, Object> params) {
		if(StringUtils.isNotBlank(searchInput)) {
			if(searchInput.matches("^^(13|14|15|18)[0-9]{9}$")) {  //手机号     电话必须是首位为1的11位数字
				params.put("receiverMobile", searchInput);
			}else { //联系人,会员名                       
				params.put("receiverName", searchInput);
			}
		}
	}
	
	/**
	 * 设置获取的短信类型
	 */
	private void setSmsTypes() {
		int userId = getMainUserId();
		try {	
			smsTypes = smsServiceService.getSMSServicesByUserId(userId);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	//获取单个搜索器对象，返回搜索器内容
	public String findSmsBuyersSearch() throws Exception {
		smsBuyersSearch = smsBuyersSearchService.getSmsBuyersSearchById(Integer.parseInt(searchName));
		//smsBuyersSearch.getId();
		searchInput = smsBuyersSearch.getSearchName();
		jsonStr = smsBuyersSearch.getSearchCondition()+"|"+searchInput;
		return "jsondata";
	}
	
	/**
	 * 获取绑定店铺用户,并且获取  List<Integer> (此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id  主帐号ID为第一位
	 */
	private List<Integer> getBindUserIds(User currentUser) {
		List<Integer> userIds = new ArrayList<Integer>();
		//userIds.add(currentUser.getId());
		for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
			User u = userService.getUserByCustomerId(cur);
			if (u != null) {
				userIds.add(u.getId());				
			}
		}
		return userIds;
	}
	
	/**
	 * 获取查询使用的用户ID
	 * 分3类ID  第一类：卖家ID  第二类：卖家父ID 第三类：平台ID
	 * 分仓使用平台ID,其余类型有父ID,使用父ID
	 * @return
	 */
	private int getUseUserId() {
		//登录用户
		User landUser = super.readCookieUser();
		//登录用户类型
		int userType = getTheUserType();
		String childType = landUser.getChildType();  //B:分仓 C:企业
		if("B".equals(childType)) {  //分仓使用平台ID,获取平台类型
			String userSource = landUser.getUserSource();
			if(StringUtils.isNotBlank(userSource)) {        //若：分仓平台ID为NULL,则使用自身ID,注意：此情况实际属于,错误数据
				return Integer.parseInt(userSource);
			}else {
				return landUser.getId();
			}
		}else {
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
	private int getTheUserType() {
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
	 * 获取登录用户店铺名称
	 * @return
	 */
	private String getUserShopName() {
		User landUser = super.readCookieUser();
		return landUser.getShopName();
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

	
	public SMSServiceMarketService getSmsServiceMarketService() {
		return smsServiceMarketService;
	}

	public void setSmsServiceMarketService(
			SMSServiceMarketService smsServiceMarketService) {
		this.smsServiceMarketService = smsServiceMarketService;
	}

	public SMSPortService getSmsPortService() {
		return smsPortService;
	}

	public void setSmsPortService(SMSPortService smsPortService) {
		this.smsPortService = smsPortService;
	}

	public SMSTemplateService getSmsTemplateService() {
		return smsTemplateService;
	}

	public void setSmsTemplateService(SMSTemplateService smsTemplateService) {
		this.smsTemplateService = smsTemplateService;
	}

	public SMSBuyersSearchService getSmsBuyersSearchService() {
		return smsBuyersSearchService;
	}

	public void setSmsBuyersSearchService(
			SMSBuyersSearchService smsBuyersSearchService) {
		this.smsBuyersSearchService = smsBuyersSearchService;
	}

	public SMSBuyersService getSmsBuyersService() {
		return smsBuyersService;
	}

	public void setSmsBuyersService(SMSBuyersService smsBuyersService) {
		this.smsBuyersService = smsBuyersService;
	}

	@SuppressWarnings("rawtypes")
	public UserService getUserService() {
		return userService;
	}

	@SuppressWarnings("rawtypes")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SMSInfoService getSmsInfoService() {
		return smsInfoService;
	}

	public void setSmsInfoService(SMSInfoService smsInfoService) {
		this.smsInfoService = smsInfoService;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	public String getTheLastTradeTime() {
		return theLastTradeTime;
	}

	public void setTheLastTradeTime(String theLastTradeTime) {
		this.theLastTradeTime = theLastTradeTime;
	}

	public String getTheLastMarketTime() {
		return theLastMarketTime;
	}

	public void setTheLastMarketTime(String theLastMarketTime) {
		this.theLastMarketTime = theLastMarketTime;
	}

	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getSmsUsecount() {
		return smsUsecount;
	}

	public void setSmsUsecount(Integer smsUsecount) {
		this.smsUsecount = smsUsecount;
	}

	public SMSBuyersSearch getSmsBuyersSearch() {
		return smsBuyersSearch;
	}

	public void setSmsBuyersSearch(SMSBuyersSearch smsBuyersSearch) {
		this.smsBuyersSearch = smsBuyersSearch;
	}

	public SMSBuyers getSmsBuyers() {
		return smsBuyers;
	}

	public void setSmsBuyers(SMSBuyers smsBuyers) {
		this.smsBuyers = smsBuyers;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getPortBuy() {
		return portBuy;
	}

	public void setPortBuy(Integer portBuy) {
		this.portBuy = portBuy;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public List<SMSTemplate> getSmsTemplateList() {
		return smsTemplateList;
	}

	public void setSmsTemplateList(List<SMSTemplate> smsTemplateList) {
		this.smsTemplateList = smsTemplateList;
	}

	public List<SMSBuyersSearch> getSmsBuyerSearchList() {
		return smsBuyerSearchList;
	}

	public void setSmsBuyerSearchList(List<SMSBuyersSearch> smsBuyerSearchList) {
		this.smsBuyerSearchList = smsBuyerSearchList;
	}

	public List<SMSBuyers> getSmsBuyersList() {
		return smsBuyersList;
	}

	public void setSmsBuyersList(List<SMSBuyers> smsBuyersList) {
		this.smsBuyersList = smsBuyersList;
	}

	public Map<String, Integer> getSmsInfoMap() {
		return smsInfoMap;
	}

	public void setSmsInfoMap(Map<String, Integer> smsInfoMap) {
		this.smsInfoMap = smsInfoMap;
	}

	public String getTradeAmountMin() {
		return tradeAmountMin;
	}

	public void setTradeAmountMin(String tradeAmountMin) {
		this.tradeAmountMin = tradeAmountMin;
	}

	public String getTradeAmountMax() {
		return tradeAmountMax;
	}

	public void setTradeAmountMax(String tradeAmountMax) {
		this.tradeAmountMax = tradeAmountMax;
	}

	public String getTradeCountMin() {
		return tradeCountMin;
	}

	public void setTradeCountMin(String tradeCountMin) {
		this.tradeCountMin = tradeCountMin;
	}

	public String getTradeCountMax() {
		return tradeCountMax;
	}

	public void setTradeCountMax(String tradeCountMax) {
		this.tradeCountMax = tradeCountMax;
	}

	public String getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}

	public String getSearchInput() {
		return searchInput;
	}

	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}

	public String getSearchName2() {
		return searchName2;
	}

	public void setSearchName2(String searchName2) {
		this.searchName2 = searchName2;
	}

	public List<SMSService> getSmsTypes() {
		return smsTypes;
	}
	public void setSmsTypes(List<SMSService> smsTypes) {
		this.smsTypes = smsTypes;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPortNum() {
		return portNum;
	}

	public void setPortNum(Integer portNum) {
		this.portNum = portNum;
	}

	public double getPortPrice() {
		return portPrice;
	}

	public void setPortPrice(double portPrice) {
		this.portPrice = portPrice;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getPageChecked() {
		return pageChecked;
	}

	public void setPageChecked(String pageChecked) {
		this.pageChecked = pageChecked;
	}

	public int getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getSmsTypeId() {
		return smsTypeId;
	}

	public void setSmsTypeId(String smsTypeId) {
		this.smsTypeId = smsTypeId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
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

	public String getIsCheck2() {
		return isCheck2;
	}

	public void setIsCheck2(String isCheck2) {
		this.isCheck2 = isCheck2;
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
	
}
