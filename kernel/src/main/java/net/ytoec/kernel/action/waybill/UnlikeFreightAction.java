/**
 * WayBillAction.java
 * Wangyong
 * 2011-8-16 上午10:31:35
 */
package net.ytoec.kernel.action.waybill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.StepInfo;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 核心平台运单查询Action。<br/>
 * 运单数据从金刚接口传入，通过此action进行处理并提交客户。
 * @author Wangyong
 * @2011-8-16
 * net.ytoec.kernel.action.waybill
 */
@Controller
@Scope("prototype")
public class UnlikeFreightAction extends AbstractActionSupport {
	
	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private OrderService<Order> orderService;
	
	private Logger logger=Logger.getLogger(UnlikeFreightAction.class);
	private String logisticsIds;
	private List<QueryOrder> orderList;
	
	private List<QueryOrder> timeOrderList;
	
	private List<User> vipList;
	private List<UserThread> vipThreadList;
	
	//临时存放按单号查询订单的结果集
	private static List<QueryOrder> tempList = new ArrayList<QueryOrder>();
	//临时存放按时间查询订单的结果集
	private static List<QueryOrder> tempTimeList = new ArrayList<QueryOrder>();
	
	//private static Integer pageNum = 3;
	private Integer currentPage = 1;
	private Pagination pagination; //分页对象
	
	private String startTime;
	private String endTime;
	private String buyerStartTime;//按买家信息查询时开始时间
	private String buyerEndTime;//按买家信息查询时结束时间
	private Integer vipName;//按时间查询是用户id
	private Integer vipId;//按买家信息查询时用户id
	private String userCodeForTime;//按时间查询是用户编码
	private String userCodeForBuyer;//按买家信息查询时用户编码
	private Integer isCheck;
	private Integer mailNum;
	
	private String buyerName;
	private String buyerPhone;
	
	/**
	 * 按时间和单号查询的标记：1表示按单号查。2表示按时间查。3按买家信息查。默认按单号查
	 */
	private int flag = 1;
	
	public String bill(){
		flag = 1;
		logisticsIds = null;
		orderList = null;
		isCheck = mailNum = null;
		pagination = new Pagination(1, pageNum);
		pagination.setTotalRecords(0);
//		User currentUser = (User)session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		String usertype = currentUser.getUserType();
		if(usertype !=null && ("2".equals(usertype) || "21".equals(usertype) || "22".equals(usertype) || "23".equals(usertype) ) ) {
			vipThreadList = super.getZhiKeUser(currentUser);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		if(startTime==null || endTime==null || startTime.equals("") || endTime.equals("")){
		  /**
	       * 默认情况下取当前时间在内的7天内的数据。
	       */
		  startTime = DateUtil.dateArithmetic(new Date(),6);
	      endTime = DateUtil.dateArithmetic(new Date(),0);
	    }
		if(buyerStartTime==null || buyerEndTime==null || startTime.equals("") || buyerEndTime.equals("")){
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			buyerStartTime = DateUtil.dateArithmetic(new Date(),6);
			buyerEndTime = DateUtil.dateArithmetic(new Date(),0);
		}
		return "bill";
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * 按运单号查询物流跟踪信息
	 * @return
	 */
	public String list(){
//		User currentUser = (User)session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		orderList = null;
		if(logisticsIds != null && !(logisticsIds.equals(""))){
			try {
				if(isCheck == 1){
					logisticsIds = createLogisticsIds(logisticsIds,mailNum);
				}
				String[] logisticsId = logisticsIds.split("/");
				/**
				 * 按每页显示数目查询接口订单
				 */
				String[] logistics = substrArray(logisticsId, pagination.getStartIndex(), pageNum);
				TaoBaoOrderAction op = new TaoBaoOrderAction();
				List<QueryOrder> totalList = op.ordersSearch(logistics);
				if(totalList!=null){
					/**
					 * 解析结果列表,删除重复的运单
					 */
					List<QueryOrder> repeatList = new ArrayList<QueryOrder>();
					for(int i=0; i<totalList.size(); i++){
						for(int j=i+1; j<totalList.size(); j++){
							if((totalList.get(i).getMailNo()).equals(totalList.get(j).getMailNo())){
								repeatList.add(totalList.get(j));
							}
						}
					}
					totalList.removeAll(repeatList);
					/**
					 * 倒序stepsList
					 */
					List<QueryOrder> qoList = new ArrayList<QueryOrder>();
					for(QueryOrder qoObj : totalList){
					    List<StepInfo> steps = qoObj.getSteps();
					    if(steps!=null && steps.size()>0){
					        int size = steps.size();
					        List<StepInfo> siList = new ArrayList<StepInfo>();
					        for(int j=size-1; j>=0; j--){
					            StepInfo si = steps.get(j);
					            siList.add(si);
					        }
					        qoObj.setSteps(siList);
					    }
					    qoList.add(qoObj);
					}
					orderList = qoList;
				}
				
				if(logisticsId!=null&&orderList!=null)
					pagination.setTotalRecords(logisticsId.length);
				else
					pagination.setTotalRecords(0);
			} catch (Exception e) {
				logger.error("按运单号查询物流跟踪信息失败", e);
			}
		}else{
			pagination.setTotalRecords(0);
		}
		if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("2") || currentUser.getUserType().equals("21") || currentUser.getUserType().equals("22") || currentUser.getUserType().equals("23")))
			vipThreadList = super.getZhiKeUser(currentUser);
		else
			vipList = super.getClientUser(currentUser);
		if(flag==2||flag==3){
			logisticsIds = "";
		}
		return "list";
	}
	
	/**
	 * 按时间查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * @return
	 */
	public String queryByTime(){
		StringBuffer resultParameters = new StringBuffer("");
//		User currentUser = (User)session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		List<String> result = new ArrayList<String>();
		if(currentUser.getUserType().equals("1") ){//如果是VIP用户情况下,只获取该用户的订单的运单号
			vipName = currentUser.getId();
		}
		else if("11".equals(currentUser.getUserType()) ||"13".equals(currentUser.getUserType())){
			vipName = currentUser.getParentId();
		}
		if(currentUser.getUserType().equals("2")
			|| "21".equals(currentUser.getUserType() )
			|| "22".equals(currentUser.getUserType() ) 
			|| "23".equals(currentUser.getUserType() ) 
			) {
			Date ucUpdateTime = queryUserCodeUpdateTime(userCodeForTime);
			result = orderService.relatedOrderMailNoListByTime(startTime, endTime, userCodeForTime, ucUpdateTime);
		}else{
			result = orderService.getOrderMailNoListByTime(startTime, endTime, vipName, null);
		}
		if(result!=null && result.size()>0){
			for(String str : result){
				if(str!=null && str!= "")
					resultParameters.append(str).append("/");
			}
		}
		logisticsIds = resultParameters.toString();
		return list();
	}
	
	/**
	 * 按买家信息查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * @return
	 */
	public String queryByBuyer(){
		StringBuffer resultParameters = new StringBuffer("");
//		User curUser = (User)session.get(Constants.SESSION_USER);
		User curUser = super.readCookieUser();
		List<String> result = new ArrayList<String>();
		if(curUser.getUserType()!=null && curUser.getUserType().equals("1")){
			vipId = curUser.getId();
		}
		if("11".equals(curUser.getUserType() ) || "13".equals(curUser.getUserType() ) ) {
			vipId = curUser.getParentId();
		}
		if(curUser.getUserType()!=null && (curUser.getUserType().equals("2") || "21".equals(curUser.getUserType()) || "23".equals(curUser.getUserType()))){
			Date ucUpdateTime = queryUserCodeUpdateTime(userCodeForTime);
			result = orderService.relatedOrderMailNoListByBuyerInfo(buyerStartTime, buyerEndTime, userCodeForBuyer, buyerName, buyerPhone, ucUpdateTime);
		}else{
			result = orderService.getOrderMailNoListByBuyerInfo(buyerStartTime, buyerEndTime, vipId, buyerName, buyerPhone, null);	
		}
		if(result!=null && result.size()>0){
			for(String str : result){
				if(str!=null&&str!=""){
					resultParameters.append(str).append("/");
				}
			}
		}
		logisticsIds = resultParameters.toString();
		return list();
	}
	
	
	public String paginationList(){
//		User currentUser = (User)session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		if(tempList!=null)
			pagination.setTotalRecords(tempList.size());
		else
			pagination.setTotalRecords(0);
		orderList = this.getPagination(tempList, pagination);
		vipList = userService.searchUsersBySiteAndUserType(currentUser.getSite(),"1");
		if(userService.getUserListByUserType("4")!=null){
			//获取大商家.如果该大商家的网点编码为当前网点，则加入该用户
			List<User> list = userService.getUserListByUserType("4");
			for(User user : list){
				if(user.getSite()!=null && (user.getSite()).equals(currentUser.getSite()))
					vipList.add(user);
			}
		}
		return "list";
	}
	
	private List<QueryOrder> getPagination(List<QueryOrder> totalList, Pagination pagination){
		List<QueryOrder> result = new ArrayList<QueryOrder>();
		if(totalList != null){
			if(pagination.getCurrentPage()==pagination.getTotalPages()){
				for(int i=pagination.getStartIndex(); i<totalList.size(); i++){
					result.add(totalList.get(i));
				}
			}else{
				for(int i=pagination.getStartIndex(); i<pagination.getStartIndex()+pagination.getPageNum(); i++){
					result.add(totalList.get(i));
				}
			}
		}
		logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
		return result;
	}
	
	/**
	 * 根据传入的参数logisticsIds和mailNum创建运单号以“/”分隔的字符串
	 * @param logisticsIds 单运单号
	 * @param mailNum 连续查询的运单数目
	 * @return
	 */
	private String createLogisticsIds(String logisticsIds, Integer mailNum){
		String result = logisticsIds.substring(0,10)+"/";
		String queryStr = logisticsIds.substring(0,10);
		Long i = Long.parseLong(queryStr);
		for(int j=1; j<mailNum; j++){
			result += createStr(i+j, 10)+"/";
		}
		return result;
	}
	
	/**
	 * 根据传入的整形参数a，将a转化为b位字符串形式的数字，如果不足b位的在前面补0
	 * @param a 需要转换的数字
	 * @param b 字符串的长度
	 * @return
	 */
	private static String createStr(Long a, Integer b){
		String s = a.toString();
		if(s.length()<b){
			int i = s.length();
			String temp = "";
			for(int j=0; j<b-i; j++){
				temp += "0";
			}
			s = temp + s;
		}
		return s;
	}
	
	/**
	 * 截取字符串数组
	 * @param sourceArray
	 * @param startIndex
	 * @param length
	 * @return
	 */
	private static String[] substrArray(String[] sourceArray,int startIndex,int length){
		List<String> result = new ArrayList<String>();
		for(int i=startIndex; i<startIndex+length; i++){
			if(i<sourceArray.length){
				result.add(sourceArray[i]);
			}
		}
		String[] str = new String[result.size()];
		for(int j=0; j<result.size(); j++){
			str[j] = result.get(j);
		}
		return str;
	}

	/**
	 * 如果当前用户是网点，就返回客户编码修改的时间戳。
	 * 否则返回 null
	 * @param userCode 	选择用户的的用户编码
	 * @return
	 */
	private Date queryUserCodeUpdateTime(String userCode) {
		if(!StringUtil.isBlank(userCode)){
			UserThread uh = new UserThread();
			uh.setUserCode(userCode);
			List<UserThread> uhList = userThreadService.searchUsersByCode(uh);
			
			if(uhList.size() > 0) return uhList.get(0).getUserCodeUpteTime();
		}
		return null;
	}

	public String getLogisticsIds() {
		return logisticsIds;
	}

	public void setLogisticsIds(String logisticsIds) {
		this.logisticsIds = logisticsIds;
	}

	public List<QueryOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<QueryOrder> orderList) {
		this.orderList = orderList;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public Integer getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	public Integer getMailNum() {
		return mailNum;
	}

	public void setMailNum(Integer mailNum) {
		this.mailNum = mailNum;
	}

	public List<User> getVipList() {
		return vipList;
	}

	public void setVipList(List<User> vipList) {
		this.vipList = vipList;
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

	public Integer getVipName() {
		return vipName;
	}

	public void setVipName(Integer vipName) {
		this.vipName = vipName;
	}

	public List<QueryOrder> getTimeOrderList() {
		return timeOrderList;
	}

	public void setTimeOrderList(List<QueryOrder> timeOrderList) {
		this.timeOrderList = timeOrderList;
	}

	public Integer getVipId() {
		return vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public String getBuyerStartTime() {
		return buyerStartTime;
	}

	public void setBuyerStartTime(String buyerStartTime) {
		this.buyerStartTime = buyerStartTime;
	}

	public String getBuyerEndTime() {
		return buyerEndTime;
	}

	public void setBuyerEndTime(String buyerEndTime) {
		this.buyerEndTime = buyerEndTime;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getUserCodeForTime() {
		return userCodeForTime;
	}

	public void setUserCodeForTime(String userCodeForTime) {
		this.userCodeForTime = userCodeForTime;
	}

	public String getUserCodeForBuyer() {
		return userCodeForBuyer;
	}

	public void setUserCodeForBuyer(String userCodeForBuyer) {
		this.userCodeForBuyer = userCodeForBuyer;
	}

	public List<UserThread> getVipThreadList() {
		return vipThreadList;
	}

	public void setVipThreadList(List<UserThread> vipThreadList) {
		this.vipThreadList = vipThreadList;
	}
	
}
