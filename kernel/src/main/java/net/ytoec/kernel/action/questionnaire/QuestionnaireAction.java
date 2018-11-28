/**
 * 
 */
package net.ytoec.kernel.action.questionnaire;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Complaint;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.dataobject.QuestionaireRemark;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.QuestionnaireTag;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.dto.DtoQuestion;
import net.ytoec.kernel.dto.QuestionnaireResultsUpdateDTO;
import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.ComplaintService;
import net.ytoec.kernel.service.FilterRuleService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.QuestionDtoService;
import net.ytoec.kernel.service.QuestionRemarkService;
import net.ytoec.kernel.service.QuestionaireExchangeService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.QuestionnaireTagService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadContractService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

/**
 * 问题单action层
 * @author Wangyong
 * @2011-8-1
 * net.ytoec.kernel.action.questionnaire
 */
@Controller
@Scope("prototype")
public class QuestionnaireAction extends AbstractActionSupport {
	
	private static Logger logger = Logger.getLogger(QuestionnaireAction.class);
	
	private static final long serialVersionUID = 1L;
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;
	@Inject
	private QuestionDtoService<DtoQuestion> dtoQuestionService;
	@Inject
	private QuestionaireExchangeService<QuestionaireExchange> questionaireExchangeService;
	@Inject
	private QuestionRemarkService<QuestionaireRemark> questionaireRemarkService;
	@Inject
	private UserCustomService userCustomService;	
	@Inject
	private SMSPortService smsPortService; 
	@Inject
	private UserService<User> userService;
	@Inject
	private FilterRuleService  filterRuleService;
	@Inject
	private ComplaintService<Complaint> complaintService;
	@Inject
	private QuestionnaireTagService<QuestionnaireTag> questionnaireTagService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private MessageService<Message> messageService;
	
	private Map<String, String> dealMap = new HashMap<String, String>();
	
	private TraderInfo traderInfo;
	private static Integer pageNum = 20;
	
	private String starttime,endtime;
	private Integer vipName;
	private String mailNO;
	/** 问题状态. 直接从金刚同步过来的数据.PD10/未处理；PD20/处理中；PD30/处理完成；PD40/取消； */
	private String issueStatus;
	//网点通知状态或者卖家处理状态
	private String tabStatus = "1";
	//过滤是否读取状态的问题件
	private String isRead = null;
	private String dealStatus;
	
	//表示是否显示已签收问题件：1（是） 0（否）卖家问题件中默认显示未签收
	private Integer isShowSigned;
	
	private String response;
	
	private List<DtoQuestion> questionnaireList;
	private List<User> vipList;
	private List<User> unActUserList;//未激活分仓用户列表
	private List<UserThread> vipThreadList;
	private String vipCode;
	
	private Integer questionnaireId;
	private String mailNo;
	private String feedbackInfo;
	private String feedbackInfoStr;
	private String[] questionTypes;
	private String questionTypesString;
	
	private boolean flag;
	private String questionnaireIds;
	private String feedbackInfos;
	
	private Integer currentPage = 1;
	private Pagination<Questionnaire> pagination;
	
	private Integer noneAdvisedNum;//网点未通知、卖家未处理问题件数目
	private Integer advisedNum;//网点已通知、卖家已处理问题件数目
	private Integer elseNum;//网点其他问题件数目
	private Integer handingNum;//卖家处理中问题件数目
	
	private Integer tagId;//问题件标签状态
	private String bindUserCustomerId;//关联店铺customerId
	private List<User> bindUserList;//关联店铺
	private List<QuestionnaireTag> questionnaireTagList;//标签list
	
	private int userTagType;  //-1 表示初始问题件   1表示新建过的问题件
	
	private String instruction;
	
	private String feedbackJinGangContent;//同步给金刚的信息
	private Integer statusJinGang;//0处理中，1 处理完毕
	
	//是否过滤未读或者已读：默认0表示否，1表示是
	private Integer isFilterReadStatus=0;
	
	private int index=0;
	
	private Integer autoSkip = 0;//自动跳转到有数据的列表   
	
	private Integer autoNotify = 0;//自动通知客户功能的开启：0：关闭；1：开启
	
	private String contactWay;//买家联系方式：手机或者电话
	
	private String buyerName;//买家姓名
	
	private String conditionString;//按运单号、姓名、联系方式的复杂查询条件
	
	private User lanjianSite;
	
	private String complaintContent;//投诉内容
	
	private Map jsons = new HashMap();  //页面JSON集合
	
	private String tagName;    //标签名称
	
	private String tagHeaderName = "处理中";//页面上当前处理中状态，默认是“处理中”；
	
	private String sendBuyerName;   //通知客户   发送买家名称
	
	private String sendBuyerMobile; //通知客户   发送买家手机号码
	
	private String sendMailno;
	
	private int sendCount; //发送数量
	
	private String serviceType;  //短信类型
	
	/** 导出功能的导出编码;<br>如果前台不传,后台默认UTF-8 */
	private String outputCode;
	
	public String questionIndex() throws DataAccessException, ParseException{
		isFilterReadStatus = 0;
		index = 1;
		//默认获取当前登录系统的网点用户的VIP问题单列表和VIP用户列表
		User currentUser = super.readCookieUser();
		if(currentUser.getUserType()!=null 
				&& (currentUser.getUserType().equals("2") 
						|| currentUser.getUserType().equals("21") 
						|| currentUser.getUserType().equals("22") 
						|| currentUser.getUserType().equals("23"))){
			vipThreadList = super.getZhiKeUser(currentUser);
			autoNotify = this.judge(currentUser);
		}
		currentPage = 1;
		/**
		 * 默认情况下取当前时间在内的3天内的数据。
		 */
		starttime = dateArithmetic(new Date(),2);
		endtime = dateArithmetic(new Date(),0);
		dealStatus = null;
		mailNO = null;
		vipCode = null;
		pagination = new Pagination(currentPage,pageNum);
		pagination.setTotalRecords(0);
		noneAdvisedNum = advisedNum = elseNum = 0;
		if(currentUser.getUserType().equals("4") ){
			vipList = userService.pingTaiSelect(currentUser, 1,"1");
			unActUserList = userService.pingTaiSelect(currentUser, 1, "TBA");
		} else if(currentUser.getUserType().equals("41")){
			User pingTaiUser = userService.getUserById(currentUser.getParentId());
			vipList = userService.pingTaiSelect(pingTaiUser, 1,"1");
			unActUserList = userService.pingTaiSelect(pingTaiUser, 1, "TBA");
		}
		//卖家问题件管理
		if(currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") 
				|| currentUser.getUserType().equals("12") || currentUser.getUserType().equals("13")){
			tagHeaderName = "处理中";//默认处理中
			/**
			 * 设置揽件网点
			 */
			List<User> userList = userService.searchUsersBySiteAndUserType(currentUser.getSite(), "2");
			if(userList!=null && userList.size()>0){
				lanjianSite = userList.get(0);
				if(lanjianSite.getParentId()!=null){//如果是承包区的话就显示主账号网点的数据
					lanjianSite = userService.getUserById(lanjianSite.getParentId());
				}
			}
			/**
			 * 获取绑定店铺用户
			 */
			bindUserList = new ArrayList<User>();
			for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
				User u = userService.getUserByCustomerId(cur);
				if (u != null) {
					if (u.getShopName() != null && !("").equals(u.getShopName()))
						bindUserList.add(u);
				}
			}
			/** 获取当前用户的标签 */
			questionnaireTagList = questionnaireTagService.getQestionnaireTagsByUserCodeAndCreateDef(currentUser.getUserCode());
			userTagType = questionnaireTagService.getQuestionnaireTagType(currentUser.getUserCode());
			
			return "list_seller";
		}
		return "questionIndex";
	}
	
	public String index() throws DataAccessException, ParseException{
		isFilterReadStatus = 0;
		index = 1;
		//默认获取当前登录系统的网点用户的VIP问题单列表和VIP用户列表
		User currentUser = super.readCookieUser();
		if(currentUser.getUserType()!=null 
				&& (currentUser.getUserType().equals("2") 
						|| currentUser.getUserType().equals("21") 
						|| currentUser.getUserType().equals("22") 
						|| currentUser.getUserType().equals("23"))){
			vipThreadList = super.getZhiKeUser(currentUser);
			autoNotify = this.judge(currentUser);
		}
		currentPage = 1;
		/**
		 * 默认情况下取当前时间在内的3天内的数据。
		 */
		starttime = dateArithmetic(new Date(),2);
		endtime = dateArithmetic(new Date(),0);
		dealStatus = null;
		mailNO = null;
		vipCode = null;
		pagination = new Pagination(currentPage,pageNum);
		pagination.setTotalRecords(0);
		noneAdvisedNum = advisedNum = elseNum = 0;
		if(currentUser.getUserType().equals("4") ){
			vipList = userService.pingTaiSelect(currentUser, 1,"1");
			unActUserList = userService.pingTaiSelect(currentUser, 1, "TBA");
		}
		else if(currentUser.getUserType().equals("41")){
			User pingTaiUser = userService.getUserById(currentUser.getParentId());
			vipList = userService.pingTaiSelect(pingTaiUser, 1,"1");
			unActUserList = userService.pingTaiSelect(pingTaiUser, 1, "TBA");
		}
		//卖家问题件管理
		if(currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") 
				|| currentUser.getUserType().equals("12") || currentUser.getUserType().equals("13")){
			tagHeaderName = "处理中";//默认处理中
			/**
			 * 设置揽件网店
			 */
			List<User> userList = userService.searchUsersBySiteAndUserType(currentUser.getSite(), "2");
			if(userList!=null && userList.size()>0){
				lanjianSite = userList.get(0);
				if(lanjianSite.getParentId()!=null){//如果是承包区的话就显示主账号网点的数据
					lanjianSite = userService.getUserById(lanjianSite.getParentId());
				}
			}
			/**
			 * 获取绑定店铺用户
			 */
			bindUserList = new ArrayList<User>();
			for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
				User u = userService.getUserByCustomerId(cur);
				if (u != null) {
					if (u.getShopName() != null && !("").equals(u.getShopName()))
						bindUserList.add(u);
				}
			}
			/** 获取当前用户的标签 */
			questionnaireTagList = questionnaireTagService.getQestionnaireTagsByUserCodeAndCreateDef(currentUser.getUserCode());
			userTagType = questionnaireTagService.getQuestionnaireTagType(currentUser.getUserCode());
			return "list_seller";
		}
		return "index";
	}
	
	/**
	 * 网点或者卖家问题件查询方法
	 * @return
	 * @throws ParseException 
	 * @throws DataAccessException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String list() throws DataAccessException, ParseException{
		User currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
		if(StringUtils.isBlank(starttime) || StringUtils.isBlank(endtime)){
			/**
			 * 默认情况下取当前时间在内的10天内的数据。
			 */
			starttime = dateArithmetic(new Date(),9);
			endtime = dateArithmetic(new Date(),0);
		}
		List<String> fbi = new ArrayList<String>();
		if(feedbackInfo!=null && StringUtils.isNotBlank(feedbackInfo)){
			String[] fbiArray = feedbackInfo.split(",");
			questionTypes = fbiArray;
			dealMap = this.getDealMap();
			questionTypesString = "";
			for(String fbiEle : fbiArray){
				fbi.add(fbiEle);
				questionTypesString += (dealMap.get(fbiEle)+",");
			}
			questionTypesString = questionTypesString.substring(0,questionTypesString.length()-1);
		}
		pagination = new Pagination(currentPage,pageNum);
		int sum = 0;
		if (conditionString != null && !("").equals(conditionString.trim()) && !("请输入运单号/买家电话/买家姓名").equals(conditionString.trim())) {
			if (conditionString.trim().length() == 10) {
				Pattern pattern = Pattern.compile(Constants.SINGLEMAIL);
				Matcher matcher = pattern.matcher(conditionString.trim());
				if (matcher.matches()) {
					mailNO = conditionString.trim();
				} else {
					contactWay = conditionString.trim();
				}
			} else if (StringUtil.isPhone(conditionString)
					|| StringUtil.isMobile(conditionString)) {
				contactWay = conditionString.trim();
			} else
				buyerName = conditionString.trim();
		}
		/**
		 * 卖家和网点默认查询未签收的问题件
		 */
		if(isShowSigned==null){
			isShowSigned=0;
		}
		//网点:对于类型为2的用户要区分网点和承包区；网点查看所有属于该网点或者其承包区的用户；
		//承包区只查询属于自己承包下的客户
		if(currentUser.getUserType()!=null
			&& (currentUser.getUserType().equals("2")
					|| currentUser.getUserType().equals("21")
					|| currentUser.getUserType().equals("22")
					|| currentUser.getUserType().equals("23")
				)
		){
			long begin = System.currentTimeMillis();  
			autoNotify = this.judge(currentUser);
			vipThreadList = super.getZhiKeUser(currentUser);
			long end1 = System.currentTimeMillis();
			logger.error("vipThreadList方法运行时间： "+(end1-begin)+"毫秒");
			
			long begin2 = System.currentTimeMillis();
			sum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, mailNO, issueStatus, fbi, tabStatus, isRead, contactWay, buyerName, isShowSigned);
			advisedNum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, mailNO, issueStatus, fbi, "2", isRead, contactWay, buyerName, isShowSigned);
			noneAdvisedNum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, mailNO, issueStatus, fbi, "1", isRead, contactWay, buyerName, isShowSigned);
			elseNum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, mailNO, issueStatus, fbi, "3", isRead, contactWay, buyerName, isShowSigned);
			sum = autoSkip(sum,2);
			questionnaireList = dtoQuestionService.siteGetQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, mailNO, issueStatus, fbi, tabStatus, isRead, null, contactWay, buyerName, isShowSigned, pagination);
		
		} else if(currentUser.getUserType().equals("41") ||currentUser.getUserType().equals("4") || currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("12") || currentUser.getUserType().equals("13")){
			String customerId = currentUser.getTaobaoEncodeKey();
			if(currentUser.getUserType().equals("4") ){
				vipList = userService.pingTaiSelect(currentUser, 1,"1");
			} else if(currentUser.getUserType().equals("41")){
				User pingTaiUser = userService.getUserById(currentUser.getParentId());
				vipList = userService.pingTaiSelect(pingTaiUser, 1,"1");
			}
			List<String> bindedId = new ArrayList<String>();
			if(currentUser.getUserType().equals("41") ||currentUser.getUserType().equals("4")){
				if(StringUtils.isNotBlank(vipCode)){
					customerId = vipCode;
				} else{
					customerId = "";
					for(User user : vipList){
						if(user.getTaobaoEncodeKey()!=null)
							bindedId.add(user.getTaobaoEncodeKey());
					}
				}
			} else if(currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("12") || currentUser.getUserType().equals("13")){
				/**
				 * 获取绑定店铺用户
				 */
				bindUserList = new ArrayList<User>();
				for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
					User u = userService.getUserByCustomerId(cur);
					if (u != null) {
						if (u.getShopName() != null && !("").equals(u.getShopName()))
							bindUserList.add(u);
					}
				}
				if(StringUtils.isBlank(bindUserCustomerId))//查询所有店铺
					bindedId=Resource.getBindedCustomerIdList(currentUser);
				else {
					bindedId.add(bindUserCustomerId);
				}
				/** 获取当前用户的标签 */
				questionnaireTagList = questionnaireTagService.getQestionnaireTagsByUserCodeAndCreateDef(currentUser.getUserCode());
				userTagType = questionnaireTagService.getQuestionnaireTagType(currentUser.getUserCode());
			}
			sum = dtoQuestionService.vipCountQuestionList(bindedId, customerId, starttime, stringToDate(endtime,-1), mailNO, fbi, tabStatus, isRead, contactWay, buyerName, isShowSigned, tagId);
			noneAdvisedNum = dtoQuestionService.vipCountQuestionList(bindedId, customerId, starttime, stringToDate(endtime,-1), mailNO, fbi, "1", isRead, contactWay, buyerName, isShowSigned, null);
			handingNum = dtoQuestionService.vipCountQuestionList(bindedId, customerId, starttime, stringToDate(endtime,-1), mailNO, fbi, "2", isRead, contactWay, buyerName, isShowSigned, tagId);
			advisedNum = dtoQuestionService.vipCountQuestionList(bindedId, customerId, starttime, stringToDate(endtime,-1), mailNO, fbi, "3", isRead, contactWay, buyerName, isShowSigned, null);
			sum = autoSkip(sum,1);
			questionnaireList = dtoQuestionService.vipGetQuestionList(bindedId, customerId, starttime, stringToDate(endtime,-1), mailNO, fbi, tabStatus, isRead, null, contactWay, buyerName, isShowSigned, tagId, pagination);
			if(tagId==null || tagId==0){
				tagHeaderName = "处理中";//默认处理中
			} else{
				for(QuestionnaireTag tag : questionnaireTagList){
					if(tag.getId().equals(tagId))
						tagHeaderName = tag.getTagName();
				}
			}
			/**
			 * 设置揽件网点
			 */
			List<User> userList = userService.searchUsersBySiteAndUserType(currentUser.getSite(), "2");
			if(userList!=null && userList.size()>0){
				lanjianSite = userList.get(0);
				if(lanjianSite.getParentId()!=null){//如果是承包区的话就显示主账号网点的数据
					lanjianSite = userService.getUserById(lanjianSite.getParentId());
				}
			}
		}
		pagination.setTotalRecords(sum);
		//卖家问题件管理
		if(currentUser.getUserType().equals("41") || currentUser.getUserType().equals("4") || currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("12") || currentUser.getUserType().equals("13")){
			return "list_seller";
		}
		//网点问题件管理
		return "list";
	}
	
	/**
	 * 获取未读问题件列表接口
	 * @return
	 * @throws ParseException
	 */
	public String unReadList() throws ParseException{
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage,pageNum);
		int sum = 0;
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			/**
			 * 默认情况下取当前时间在内的10天内的数据。
			 */
			starttime = dateArithmetic(new Date(),9);
			endtime = dateArithmetic(new Date(),0);
		}
		//网点
		if(currentUser.getUserType()!=null
			&& (currentUser.getUserType().equals("2")
					|| currentUser.getUserType().equals("21")
					|| currentUser.getUserType().equals("23")
				)
		){
			autoNotify = this.judge(currentUser);
			tabStatus="2";
			vipThreadList = super.getZhiKeUser(currentUser);
			questionnaireList = dtoQuestionService.siteGetQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, null, issueStatus, null, tabStatus, isRead, null, null, null, 0, pagination);
			sum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, null, issueStatus, null, tabStatus, isRead, null, null, 0);
			noneAdvisedNum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, null, issueStatus, null, "1", isRead, null, null, 0);
			advisedNum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, null, issueStatus, null, "2", isRead, null, null, 0);
			elseNum = dtoQuestionService.siteCountQuestionList(currentUser, starttime, stringToDate(endtime,-1), vipCode, null, issueStatus, null, "3", isRead, null, null, 0);
		}
		else if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("13"))){
			/**
			 * 获取绑定店铺用户
			 */
			bindUserList = new ArrayList<User>();
			for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
				User u = userService.getUserByCustomerId(cur);
				if (u != null) {
					if (u.getShopName() != null && !("").equals(u.getShopName()))
						bindUserList.add(u);
				}
			}
			/** 获取当前用户的标签 */
			questionnaireTagList = questionnaireTagService.getQestionnaireTagsByUserCodeAndCreateDef(currentUser.getUserCode());
			userTagType = questionnaireTagService.getQuestionnaireTagType(currentUser.getUserCode());
			List<String> bindedId=Resource.getBindedCustomerIdList(currentUser);
			noneAdvisedNum = dtoQuestionService.vipCountQuestionList(bindedId, currentUser.getTaobaoEncodeKey(), starttime, stringToDate(endtime,-1), null, null, "1", isRead, null, null, 0, null);
			handingNum = dtoQuestionService.vipCountQuestionList(bindedId, currentUser.getTaobaoEncodeKey(), starttime, stringToDate(endtime,-1), null, null, "2", isRead, null, null, 0, null);
			advisedNum = dtoQuestionService.vipCountQuestionList(bindedId, currentUser.getTaobaoEncodeKey(), starttime, stringToDate(endtime,-1), null, null, "3", isRead, null, null, 0, null);
			if(noneAdvisedNum == 0){
				if(handingNum == 0){
					if(advisedNum == 0){
						tabStatus = "1";
					} else{
						tabStatus = "3";
					}
				} else{
					tabStatus = "2";
				}
			} else{
				tabStatus = "1";
			}
			questionnaireList = dtoQuestionService.vipGetQuestionList(bindedId, currentUser.getTaobaoEncodeKey(), starttime, stringToDate(endtime,-1), null, null, tabStatus, isRead, null, null, null, 0, null, pagination);
			sum = dtoQuestionService.vipCountQuestionList(bindedId, currentUser.getTaobaoEncodeKey(), starttime, stringToDate(endtime,-1), null, null, tabStatus, isRead, null, null, 0, null);
			/**
			 * 设置揽件网店
			 */
			List<User> userList = userService.searchUsersBySiteAndUserType(currentUser.getSite(), "2");
			if(userList!=null && userList.size()>0){
				lanjianSite = userList.get(0);
				if(lanjianSite.getParentId()!=null){//如果是承包区的话就显示主账号网点的数据
					lanjianSite = userService.getUserById(lanjianSite.getParentId());
				}
			}
		}
		
		pagination.setTotalRecords(sum);
		//卖家问题件管理
		if(currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("13")){
			return "list_seller";
		}
		//网点问题件管理
		return "list";
	}
	
	private static String dateArithmetic(Date originalDate, int countDay){
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		//在当前日期上加-countDay天数
		cal.add(Calendar.DAY_OF_MONTH, -countDay);
		return sdm.format(cal.getTime());
	}
	
	private static String stringToDate(String str,int days) throws ParseException{
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		String result = null;
		if(str!=null && !str.equals("")){
			Date next = sdm.parse(str);
			result = dateArithmetic(next,days);
		}
		return result;
	}
	
	/**
	 * 单条问题处理发送 
	 * @return
	 */
	public String send(){
		if(StringUtils.isNotBlank(feedbackInfo)){
			User currentUser = super.readCookieUser();
			QuestionaireExchange questionaireExchange = new QuestionaireExchange();
			questionaireExchange.setMsgContent(feedbackInfo);
			questionaireExchange.setOperatorName(StringUtils.isNotBlank(currentUser.getUserNameText()) ? currentUser.getUserNameText() : currentUser.getUserName());
			questionaireExchange.setQuestionaireId(questionnaireId);
			questionaireExchange.setUserId(currentUser.getId());
			flag = questionaireExchangeService.sendExchange(currentUser, questionaireExchange);
		}
		else{
			response = "消息内容不能为空！";
			return "send";
		}
		if(flag){
			response = "反馈客户信息发送成功";
		}else
			response = "反馈客户信息发送失败";
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
	 * 通知客户
	 * @return
	 */
	public String sendCustomer() {
		//当前登录用户
		User currentUser = super.readCookieUser();
		
		//判断这个服务是否已经开通过
		/* 发送前,已判断
		boolean flag = smsPortService.supplyStatusOpenUp(smsInfoService.getUseUserId(currentUser));
		if(!flag){
			response = "发送失败,请开启短信服务!";
			return "send";
		}*/

		//根据当前用户的userId查询该用户的可使用短信数
		/*
		SMSMessage smsMessage = smsPortService.supplySMSMessage(smsInfoService.getUseUserId(currentUser));
		int hasSmsCount = 0;
		if(smsMessage != null) {
			hasSmsCount = smsMessage.getSmsUsecount();
		}*/
		/*
		response = "通知客户信息已发送!";
		//短信数量大于等于发送数量,表示短信足够,否则不够
		if(hasSmsCount>=sendCount) {
			//短信发送
			boolean isSend = smsInfoService.saveInfoToQueueForCustomerSend(currentUser, sendBuyerName, sendBuyerMobile, feedbackInfo);
			if(!isSend) {
				response = "通知客户信息发送失败!";
			}
		}else {
			response = "发送失败,短信数量只有"+hasSmsCount+"条,发送需要"+sendCount+"条!";
		}*/
		
		
		//短信发送
		response = "通知买家信息已发送!";
		boolean isSend = smsInfoService.saveInfoToQueueForCustomerSend(currentUser, sendBuyerName, sendBuyerMobile,sendMailno, feedbackInfo);
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
			response = "通知买家信息发送失败!";
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
	 * 单条问题写备注 :如果该问题件备注存在，则修改，否则添加新备注
	 * @return
	 */
	public String remark(){
		if(StringUtils.isNotBlank(feedbackInfo)){
			User currentUser = super.readCookieUser();
			QuestionaireRemark questionaireRemark;
			int sendFlag = 0;//0表示添加新备注
			List<QuestionaireRemark> questionaireRemarkList = questionaireRemarkService.findByQuestionId(questionnaireId);
			if(questionaireRemarkList!=null && questionaireRemarkList.size()>0){
				questionaireRemark = questionaireRemarkList.get(0);
				sendFlag = 1;//1表示修改备注
			}else{
				questionaireRemark = new QuestionaireRemark();
			}
			questionaireRemark.setHadRead("0");
			questionaireRemark.setOperatorName(currentUser.getUserNameText());
			questionaireRemark.setQuestionaireId(questionnaireId);
			questionaireRemark.setRemakContent(feedbackInfo);
			questionaireRemark.setUserId(currentUser.getId());
			flag = questionaireRemarkService.sendRemark(questionaireRemark, sendFlag);
		}
		else{
			response = "备注内容不能为空";
			return "send";
		}
		if(flag){
			response = "填写备注操作已成功";
		}else
			response = "填写备注操作失败";
		return "send";
	}
	
	//未处理（未通知）移动到“其它”
	public String move(){
		if(StringUtils.isNotBlank(questionnaireIds) && StringUtils.isNotBlank(tabStatus)){
			User currentUser = super.readCookieUser();
			if(questionnaireIds.contains(",")){
				String [] queIdArray = questionnaireIds.split(",");
				for(String id : queIdArray){
					questionnaireId = Integer.parseInt(id);
					flag = questionnaireService.moveQuestion(currentUser, questionnaireId, Integer.parseInt(tabStatus) ,null);
					if(!flag){
						break;
					}
				}
			}
			else{
				questionnaireId = Integer.parseInt(questionnaireIds);
				flag = questionnaireService.moveQuestion(currentUser, questionnaireId, Integer.parseInt(tabStatus), null);
			}
		}
		if(flag)
			response = "问题件移动操作已成功!";
		else
			response = "问题件移动操作失败!";
		return "move";
	}
	
	//标记问题件
	public String mark(){
		if(StringUtils.isNotBlank(questionnaireIds) && StringUtils.isNotBlank(tabStatus)){
			User currentUser = super.readCookieUser();
			if(questionnaireIds.contains(",")){
				String [] queIdArray = questionnaireIds.split(",");
				for(String id : queIdArray){
					questionnaireId = Integer.parseInt(id);
					flag = questionnaireService.markQuestion(currentUser, questionnaireId, tabStatus);
					if(!flag){
						break;
					}
				}
			}
			else{
				questionnaireId = Integer.parseInt(questionnaireIds);
				flag = questionnaireService.markQuestion(currentUser, questionnaireId, tabStatus);
			}
		}
		if(flag)
			response = "问题件标记操作已成功!";
		else
			response = "问题件标记操作失败!";
		return "move";
	}
	
	/** 查看客户信息 */
	public String getCustomerInfo() {
		traderInfo = questionnaireService.getCustomerInfo(mailNo);
		return "json";
	}
	
	public String updateJinGangQuestion(){
		User currentUser = super.readCookieUser();
		Questionnaire question = questionnaireService.getQestionnaireById(questionnaireId);
		if(question!=null){
			QuestionnaireResultsUpdateDTO questionnaireRDTO = new QuestionnaireResultsUpdateDTO();
			questionnaireRDTO.setIssueId(question.getIssueId());
			questionnaireRDTO.setIssueCreateTime(DateUtil.format(question.getIssueCreateTime(), "yy-MM-dd HH:mm:ss.sss"));
			questionnaireRDTO.setCreateOrgCode(currentUser.getUserCode());
			questionnaireRDTO.setCreateUserCode(currentUser.getId().toString());
			questionnaireRDTO.setCreateUserName(currentUser.getUserNameText());
			//处理结果 :  保存调整为DEALING处理中，完成调整为DEALED 处理完毕
			if(statusJinGang.equals(0))
				questionnaireRDTO.setStatus("DEALING");
			else
				questionnaireRDTO.setStatus("DEALED");
			questionnaireRDTO.setDealContent(feedbackJinGangContent);
			QuestionnaireResultsUpdateDTO result = questionnaireService.updateQuestionnaireResults(questionnaireRDTO,ConfigUtilSingle.getInstance().getCONTRALSYNCHRONIZED());
			if(result!=null){
				response = result.getReason();
				if(response==null || ("").equals(response))
					response = "发送失败";
				if(("true").equals(result.getSuccess()))
					response = "发送成功";
			}else
				response = "系统繁忙，请稍后再试";

			
		}
		return "updateJinGangQuestion";
	}
	
	/**
	 * 是否开启自动通知客户功能：autoNotify=0关闭；1开启<br>
	 * 0时删除数据库userCustom表中用户名存在的记录。1的时候添加数据。
	 * @return
	 */
	public String openAutoNotify(){
		User currentUser = super.readCookieUser();
		UserCustom userCustom = new UserCustom();
		String userName = currentUser.getUserName();
		/**
		 * 子账号保存主账号的useName
		 */
		if(currentUser.getUserType().equals("21") || currentUser.getUserType().equals("22") || currentUser.getUserType().equals("23")){
			User site = userService.getUserById(currentUser.getParentId());
			if(site!=null)
				userName = site.getUserName();
		}
		userCustom.setUserName(userName);
		userCustom.setType(UserCustom.OPENNOTIFY);
		List<UserCustom> ucList = userCustomService.searchUserCustom(userCustom);
		if(autoNotify.equals(0)){
			if(ucList!=null){
				for(UserCustom uc : ucList){
					boolean flag = userCustomService.remove(uc);
					if(flag)
						response = "关闭成功";
				}
			}
		}
		if(autoNotify.equals(1)){
			if(ucList!=null){
				for(UserCustom uc : ucList){
					userCustomService.remove(uc);
				}
			}
			boolean flag = userCustomService.add(userCustom);
			if(flag)
				response = "开启成功";
		}
		return "openAutoNotify";
	}
	
	/**
	 * 批量通知客户：feedbackInfos：通知客户内容，以"##"分隔。
	 * questionnaireIds:问题件id，以","分隔
	 * @return
	 */
	public String sendAll(){
		if(StringUtils.isNotBlank(feedbackInfos)){
			String sendResult = "";
			int j=0;
			User currentUser = super.readCookieUser();
			String[] questionIdArr = questionnaireIds.split(",");
			String[] feedbackInfoArr = feedbackInfos.split("##");
			for(int i=0; i<questionIdArr.length; i++){
				if(questionIdArr[i]!=null && !("").equals(questionIdArr[i]) && feedbackInfoArr[i]!=null && !("").equals(feedbackInfoArr[i])){
					QuestionaireExchange questionaireExchange = new QuestionaireExchange();
					questionaireExchange.setMsgContent(feedbackInfoArr[i]);
					questionaireExchange.setOperatorName(StringUtils.isNotBlank(currentUser.getUserNameText()) ? currentUser.getUserNameText() : currentUser.getUserName());
					questionaireExchange.setQuestionaireId(Integer.valueOf(questionIdArr[i]));
					questionaireExchange.setUserId(currentUser.getId());
					flag = questionaireExchangeService.sendExchange(currentUser, questionaireExchange);
					j++;
					if(!flag){
						sendResult += feedbackInfoArr[i]+"通知失败。";
					}
				}
			}
			if(sendResult.equals("")&&j>0)
				response = "反馈客户信息发送成功";
			else if(j==0)
				response = "通知客户内容不能为空";
			else
				response = sendResult;
		}else{
			response = "通知客户内容不能为空";
		}
		return "sendAll";
	}
	
	/**
	 * 判断是否开启了自动通知客户功能
	 */
	private Integer judge(User currentUser){
		UserCustom userCustom = new UserCustom();
		String userName = currentUser.getUserName();
		/**
		 * 子账号保存主账号的useName
		 */
		if(currentUser.getUserType().equals("21") || currentUser.getUserType().equals("22") || currentUser.getUserType().equals("23")){
			User site = userService.getUserById(currentUser.getParentId());
			if(site!=null)
				userName = site.getUserName();
		}
		userCustom.setUserName(userName);
		userCustom.setType(UserCustom.OPENNOTIFY);
		List<UserCustom> ucList = userCustomService.searchUserCustom(userCustom);
		if(ucList!=null && ucList.size()>0)
			return 1;
		else
			return 0;
	}
	
	/**
	 * 卖家问题件投诉
	 * @return
	 */
	public String complaint(){
		User currentUser = super.readCookieUser();
		if(currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("12") || currentUser.getUserType().equals("13")){
			if(StringUtils.isNotEmpty(complaintContent) && questionnaireIds!=null){
				String[] questionIdsArr = questionnaireIds.split(",");
				Integer[] questionIds = new Integer[questionIdsArr.length];
				for(int i=0; i<questionIdsArr.length; i++){
					questionIds[i] = Integer.valueOf(questionIdsArr[i]);
				}
				response = complaintService.complaintQuesition(questionIds, complaintContent, currentUser);
			}else{
				response = "请选择问题件并输入投诉内容！";
			}
		}else{
			response = "对不起，您不是卖家，不能投诉问题件！";
		}
		return "complaint";
	}
	
	/**
	 * 问题件标签移动
	 * @return
	 */
	public String tagMove(){
		User currentUser = super.readCookieUser();
		if(StringUtils.isNotEmpty(questionnaireIds)){
			String[] questionIdStr = questionnaireIds.split(",");
			Integer[] questionIds = new Integer[questionIdStr.length];
			for(int i=0; i<questionIdStr.length; i++){
				questionIds[i] = Integer.valueOf(questionIdStr[i]);
			}
			if(tagId!=null && tagId>0){
				boolean result = questionnaireService.moveQuestion(currentUser, questionIds, tagId);
				if(result){
					QuestionnaireTag tag = questionnaireTagService.getQestionnaireTag(tagId);
					if(tag.getTagName().equals("已处理"))
						response = "移动成功，请到\"已处理\"查看!";
					else
						response = "移动成功，请到\"处理中\"查看!";
				} else{
					response = "移动失败!";
				}
			} else{
				response = "标签不存在";
			}
		} else{
			response = "请先选择要操作的问题件!";
		}
		return "tagMove";
	}

	/**
	 * 获取标签
	 * @return
	 */
	public String getTags() {
		User currentUser = super.readCookieUser();
		String userCode = currentUser.getUserCode();
		List<QuestionnaireTag> tags = questionnaireTagService.getQestionnaireTagsByUserCodeAndCreateDef(userCode);
		jsons.put("tags", tags);
		return "json";
	}
	
	/**
	 * 保存标签
	 * @return
	 */
	public String saveTag() {
		User currentUser = super.readCookieUser();
		String userCode = currentUser.getUserCode();
		Map<String, Object> resMap = null;
		QuestionnaireTag tagBean = null;
		String err = "";
		if(tagId==null) {  //新增
			resMap = questionnaireTagService.addQuestionnaireTag(userCode, tagName);		
		}else {            //修改
			resMap = questionnaireTagService.editQuestionnaireTagName(userCode, tagId, tagName);
		}
		err = resMap.get("err")==null?"":resMap.get("err").toString();
		tagBean = (QuestionnaireTag)resMap.get("res");
		jsons.put("err", err);
		jsons.put("tag", tagBean);
		return "json";
	}
	
	/**
	 * 删除标签
	 * @return
	 */
	public String deleteTag() {		
		int isDelete = questionnaireTagService.removeQuestionnaireTag(tagId);
		jsons.put("hasItems", isDelete);
		return "json";
	}
	
	/**
	 * 问题件导出
	 * @return
	 */
	public String exportQuestion(){
		HttpServletResponse response = null;
		PrintWriter out = null;
		try {
			response = ServletActionContext.getResponse();
			User cuser = super.readCookieUser();
			String usertype = cuser.getUserType();
			if(usertype.equals("1") || usertype.equals("11") || usertype.equals("12") || usertype.equals("13")){
				List<String> bindedId = new ArrayList<String>();
				if(StringUtils.isBlank(bindUserCustomerId))//查询所有店铺
					bindedId=Resource.getBindedCustomerIdList(cuser);
				else {
					bindedId.add(bindUserCustomerId);
				}
				List<String> fbi = new ArrayList<String>();
				if(feedbackInfo!=null && StringUtils.isNotBlank(feedbackInfo)){
					String[] fbiArray = feedbackInfo.split(",");
					questionTypes = fbiArray;
					dealMap = this.getDealMap();
					questionTypesString = "";
					for(String fbiEle : fbiArray){
						fbi.add(fbiEle);
						questionTypesString += (dealMap.get(fbiEle)+",");
					}
					questionTypesString = questionTypesString.substring(0,questionTypesString.length()-1);
				}
				if (conditionString != null && !("").equals(conditionString.trim()) && !("请输入运单号/买家电话/买家姓名").equals(conditionString.trim())) {
					if (conditionString.trim().length() == 10) {
						Pattern pattern = Pattern
								.compile("(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}");
						Matcher matcher = pattern.matcher(conditionString.trim());
						if (matcher.matches()) {
							mailNO = conditionString.trim();
						} else {
							contactWay = conditionString.trim();
						}
					} else if (StringUtil.isPhone(conditionString)
							|| StringUtil.isMobile(conditionString)) {
						contactWay = conditionString.trim();
					} else
						buyerName = conditionString.trim();
				}
				String result = questionnaireService.export(bindedId, starttime, stringToDate(endtime,-1), mailNO, fbi, tabStatus, contactWay, buyerName, isShowSigned, tagId);
				String fileName = starttime + "~" + endtime;
				if(tabStatus.equals("1"))
					fileName += "新问题件";
				else if(tabStatus.equals("2"))
					fileName += "处理中问题件";
				else if(tabStatus.equals("3"))
					fileName += "已处理问题件";
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"),"ISO8859-1") + ".csv");
				outputCode = StringUtils.defaultIfEmpty(outputCode, CharEncoding.UTF_8);
				response.setContentType("application/ms-excel;charset="+outputCode);
				response.setCharacterEncoding(outputCode);
				out = response.getWriter();
				out.print(result);
			} else{
				return null;
			}
		} catch(Exception e){
			logger.error("问题件导出有误："+e);
		}
		return null;
	}
	
	public Map getJsons() {
		return jsons;
	}

	public void setJsons(Map jsons) {
		this.jsons = jsons;
	}
	
	
	/**
	 * 自动切换tab,并设置总数sum
	 * @param sum
	 * @param type 1表示卖家；2表示网点
	 */
	private int autoSkip(int sum, int type){
		if(sum==0 && autoSkip==1){
			if(type==2){
				if(tabStatus.equals("1")){
					if(advisedNum!=0){
						sum = advisedNum;
						tabStatus = "2";
					}else if(elseNum!=0){
						sum = elseNum;
						tabStatus = "3";
					}
				}else if(tabStatus.equals("2")){
					if(noneAdvisedNum!=0){
						sum = noneAdvisedNum;
						tabStatus = "1";
					}else if(elseNum!=0){
						sum = elseNum;
						tabStatus = "3";
					}
				}else if(tabStatus.equals("3")){
					if(noneAdvisedNum!=0){
						sum = noneAdvisedNum;
						tabStatus = "1";
					}else if(advisedNum!=0){
						sum = advisedNum;
						tabStatus = "2";
					}
				}
			} else if(type==1){
				if(tabStatus.equals("1")){
					if(handingNum!=0){
						sum = handingNum;
						tabStatus = "2";
					}else if(advisedNum!=0){
						sum = advisedNum;
						tabStatus = "3";
					}
				}else if(tabStatus.equals("2")){
					if(noneAdvisedNum!=0){
						sum = noneAdvisedNum;
						tabStatus = "1";
					}else if(advisedNum!=0){
						sum = advisedNum;
						tabStatus = "3";
					}
				}else if(tabStatus.equals("3")){
					if(noneAdvisedNum!=0){
						sum = noneAdvisedNum;
						tabStatus = "1";
					}else if(handingNum!=0){
						sum = handingNum;
						tabStatus = "2";
					}
				}
			}
		}
		return sum;
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
	

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public List<DtoQuestion> getQuestionnaireList() {
		return questionnaireList;
	}

	public void setQuestionnaireList(List<DtoQuestion> questionnaireList) {
		this.questionnaireList = questionnaireList;
	}

	public List<User> getVipList() {
		return vipList;
	}

	public void setVipList(List<User> vipList) {
		this.vipList = vipList;
	}


	public String getStarttime() {
		return starttime;
	}


	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}


	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Integer getVipName() {
		return vipName;
	}

	public void setVipName(Integer vipName) {
		this.vipName = vipName;
	}

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public Integer getIsShowSigned() {
		return isShowSigned;
	}

	public void setIsShowSigned(Integer isShowSigned) {
		this.isShowSigned = isShowSigned;
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

	public void setFeedbackInfoStr(String feedbackInfoStr) {
        this.feedbackInfoStr = feedbackInfoStr;
    }

    public String getFeedbackInfoStr() {
        return feedbackInfoStr;
    }

    public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}

	public String getQuestionnaireIds() {
		return questionnaireIds;
	}

	public void setQuestionnaireIds(String questionnaireIds) {
		this.questionnaireIds = questionnaireIds;
	}

	public String getFeedbackInfos() {
		return feedbackInfos;
	}

	public String[] getQuestionTypes() {
		return questionTypes;
	}

	public void setQuestionTypes(String[] questionTypes) {
		this.questionTypes = questionTypes;
	}

	public String getQuestionTypesString() {
		return questionTypesString;
	}

	public void setQuestionTypesString(String questionTypesString) {
		this.questionTypesString = questionTypesString;
	}

	public void setFeedbackInfos(String feedbackInfos) {
		this.feedbackInfos = feedbackInfos;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination<Questionnaire> getPagination() {
		return pagination;
	}

	public TraderInfo getTraderInfo() {
		return traderInfo;
	}

	public void setPagination(Pagination<Questionnaire> pagination) {
		this.pagination = pagination;
	}

	public List<UserThread> getVipThreadList() {
		return vipThreadList;
	}

	public void setVipThreadList(List<UserThread> vipThreadList) {
		this.vipThreadList = vipThreadList;
	}

	public String getVipCode() {
		return vipCode;
	}

	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}

	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}

	public String getIssueStatus() {
		return issueStatus;
	}

	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}

	public String getTabStatus() {
		return tabStatus;
	}

	public Map<String, String> getDealMap() {
		dealMap.put("PR11","快件到我公司已破损");
		dealMap.put("PR100","有进无出");
		dealMap.put("PR110","有出无进");
		dealMap.put("PR120","有单无货");
		dealMap.put("PR130","有货无单");
		dealMap.put("PR210","收件客户拒收");
		dealMap.put("PR211","地址不详电话联系不上");
		dealMap.put("PR212","收件客户已离职");
		dealMap.put("PR213","收件客户要求改地址");
		dealMap.put("PR214","地址不详且电话为传真或无人接听");
		dealMap.put("PR215","地址不详电话与收件客户本人不符");
		dealMap.put("PR216","地址不详无收件人的电话");
		dealMap.put("PR220","错发");
		dealMap.put("PR230","延误");
		dealMap.put("PR240","遗失");
		dealMap.put("PR250","违禁品");
		dealMap.put("PR260","快件污染");
		dealMap.put("PR50","到付费");
		dealMap.put("PR60","代收款");
		dealMap.put("PR270","其它原因");
		dealMap.put("PR001","无头件上报");
		dealMap.put("PR002","面单填写不规范");
		dealMap.put("R70","节假日客户休息");
		dealMap.put("PR10","破损");
		dealMap.put("PR20","超区");
		dealMap.put("PR30","内件短少");
		dealMap.put("PR40","超重");
		dealMap.put("PR140","签收失败（自动上报）");
		return dealMap;
	}
	
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public Integer getNoneAdvisedNum() {
		return noneAdvisedNum;
	}

	public void setNoneAdvisedNum(Integer noneAdvisedNum) {
		this.noneAdvisedNum = noneAdvisedNum;
	}

	public Integer getAdvisedNum() {
		return advisedNum;
	}

	public void setAdvisedNum(Integer advisedNum) {
		this.advisedNum = advisedNum;
	}

	public Integer getElseNum() {
		return elseNum;
	}

	public void setElseNum(Integer elseNum) {
		this.elseNum = elseNum;
	}

	public Integer getHandingNum() {
		return handingNum;
	}

	public void setHandingNum(Integer handingNum) {
		this.handingNum = handingNum;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public List<User> getBindUserList() {
		return bindUserList;
	}

	public void setBindUserList(List<User> bindUserList) {
		this.bindUserList = bindUserList;
	}

	public String getBindUserCustomerId() {
		return bindUserCustomerId;
	}

	public void setBindUserCustomerId(String bindUserCustomerId) {
		this.bindUserCustomerId = bindUserCustomerId;
	}

	public Integer getIsFilterReadStatus() {
		return isFilterReadStatus;
	}

	public void setIsFilterReadStatus(Integer isFilterReadStatus) {
		this.isFilterReadStatus = isFilterReadStatus;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Integer getAutoSkip() {
		return autoSkip;
	}

	public void setAutoSkip(Integer autoSkip) {
		this.autoSkip = autoSkip;
	}

	public void setUnActUserList(List<User> unActUserList) {
		this.unActUserList = unActUserList;
	}

	public List<User> getUnActUserList() {
		return unActUserList;
	}

	public String getFeedbackJinGangContent() {
		return feedbackJinGangContent;
	}

	public void setFeedbackJinGangContent(String feedbackJinGangContent) {
		this.feedbackJinGangContent = feedbackJinGangContent;
	}

	public Integer getStatusJinGang() {
		return statusJinGang;
	}

	public void setStatusJinGang(Integer statusJinGang) {
		this.statusJinGang = statusJinGang;
	}

	public Integer getAutoNotify() {
		return autoNotify;
	}

	public void setAutoNotify(Integer autoNotify) {
		this.autoNotify = autoNotify;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getConditionString() {
		return conditionString;
	}

	public void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}

	public User getLanjianSite() {
		return lanjianSite;
	}

	public void setLanjianSite(User lanjianSite) {
		this.lanjianSite = lanjianSite;
	}

	public String getComplaintContent() {
		return complaintContent;
	}

	public void setComplaintContent(String complaintContent) {
		this.complaintContent = complaintContent;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public List<QuestionnaireTag> getQuestionnaireTagList() {
		return questionnaireTagList;
	}

	public void setQuestionnaireTagList(List<QuestionnaireTag> questionnaireTagList) {
		this.questionnaireTagList = questionnaireTagList;
	}

	public String getOutputCode() {
		return outputCode;
	}

	public void setOutputCode(String outputCode) {
		this.outputCode = outputCode;
	}

	public String getTagHeaderName() {
		return tagHeaderName;
	}

	public void setTagHeaderName(String tagHeaderName) {
		this.tagHeaderName = tagHeaderName;
	}

	public int getUserTagType() {
		return userTagType;
	}

	public void setUserTagType(int userTagType) {
		this.userTagType = userTagType;
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

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
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
