/**
 * QuestionnaireMonitorAction.java
 * Wangyong
 * 2011-8-8 下午03:44:51
 */
package net.ytoec.kernel.action.questionnaire;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.passIssueManage.PassManageAction;
import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.MonitorBean;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderProductBean;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.AttentionMailService;
import net.ytoec.kernel.service.MonitorService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.ProductService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 卖家对运单监控管理Action
 * 
 * @author Wangyong
 * @2011-8-8 net.ytoec.kernel.action.questionnaire
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class QuestionnaireMonitorAction extends AbstractActionSupport {
	
	private Logger logger = LoggerFactory.getLogger(QuestionnaireMonitorAction.class);

	@Inject
	private MonitorService<MonitorBean> monitorService;
	@Inject
	private UserService<User> userService;
	@Inject
    private TraderInfoService<TraderInfo> traderInfoService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private ProductService<Product> productService;

	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;

	@Inject
	private AttentionMailService<AttentionMail> attentionMailService;
	@Inject
	private UserCustomService userCustomService;

	private List<MonitorBean> monitoreList = new ArrayList<MonitorBean>();
	private List<Questionnaire> questioinnaireList;
	private List<User> vipList;
	private List<UserThread> threadList;

	// 检索条件
	private String starttime, endtime;
	private String buyerName;// 买家姓名
	private String buyerPhone;// 买家电话
	private String mailNO;
	private String dealStatus; // 处理状态
	private Integer orderType = 2; // 按时间排序方式 ：1表示升序，2表示降序
	private Integer status = 5;// 1(成功订单)、2(正在派送)、3(走件中)、4(退货单) 5(接单中)
	private String conditionString;// 综合查询条件
	private List<User> bindUserList;// 绑定店铺
	private Integer bindUserId = 0;// 绑定用户Id
	private Integer numProv;
	private Integer numCity;
	private Integer numDistrict;
	private String mailNoOrderType; // 筛选条件增加运单类型（所有运单、常规运单、退货运单）。

	/**
	 * @date 2012-2-8 需求变更：去掉问题件
	 */
	private int tabFlag = 1;// 1(订单)、2(关注运单)
	private Integer vipId;
	private int isShow;
	private int autoSkip = 0;// 自动跳转标示。1标示自动跳转。在用户输入条件进行查询时，如果查询的当前列表为空，则自动检测下一列表。以此类推。默认为0.

	private Integer currentPage = 1;
	private Pagination<Questionnaire> pagination;

	private Integer totalOrderSuccess = null;// 成功订单总数
	private Integer totalOrderFail = null;// 走件中订单总数
	private Integer totalOrderScan = null;// 派送中订单总数
	private Integer totalOrderCancel = null;// 退货订单总数
	private Integer totalAttention = null;// 关注运单总数
	private Integer totalOrderCreate = null;// 接单中订单总数

	private String instruction;// 说明页面参数
	
	private String otherUserId;  //其他客户,网点ID
	
	private Integer pageNum = 20;
	
	//网点选择的客户编码
	private String vipCode;

	/**
	 * 如果当前用户是大商家的话，获取问题件中VIP来源是淘宝的用户的所有的问题件。<br>
	 * 订单监控也是如此。根据VIP来源是否是淘宝获取。 默认不显示信息
	 * 
	 * @return
	 * @throws ParseException
	 * @throws DataAccessException
	 */
	public String index() {
		User currentUser = super.readCookieUser();
		/**
		 * 平台用户获取所有业务账号
		 */
		if (currentUser.getUserType() != null
				&& (currentUser.getUserType().equals("4") || currentUser
						.getUserType().equals("41"))) {
			vipList = userService.pingTaiSelect(currentUser, 1);
		}else if(currentUser.getUserType()!=null 
				&& (currentUser.getUserType().equals("2") 
						|| currentUser.getUserType().equals("21") 
						|| currentUser.getUserType().equals("22") 
						|| currentUser.getUserType().equals("23"))){
			threadList = super.getZhiKeUser(currentUser);
		}
		/**
		 * 默认情况下取当前时间在内的7天内的数据。
		 */
		starttime = dateArithmetic(new Date(), 6);
		endtime = dateArithmetic(new Date(), 0);
		pagination = new Pagination(currentPage, pageNum);
		pagination.setTotalRecords(0);
		isShow = 0;
		monitoreList = new ArrayList<MonitorBean>();
		
		/**
		 * 获取绑定店铺用户
		 */
		if(currentUser.getUserType()!=null 
				&& (currentUser.getUserType().equals("1") 
						|| currentUser.getUserType().equals("11") 
						|| currentUser.getUserType().equals("12") 
						|| currentUser.getUserType().equals("13"))){
			bindUserList = new ArrayList<User>();
			for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
				User u = userService.getUserByCustomerId(cur);
				if (u != null) {
					if (u.getShopName() != null && !("").equals(u.getShopName()))
						bindUserList.add(u);
				}
			}
		}
		
		return "list";
	}

	/**
	 * 根据查询条件查询并返回卖家或网点运单监控列表
	 * @return
	 * @throws ParseException
	 * @throws DataAccessException
	 */
	public String list() throws DataAccessException, ParseException {
		long QuestionMonitorActionTimeStart=System.currentTimeMillis();
		isShow = 1;
		boolean isAll = false;
		request.setAttribute("date_range", null);
		User currentUser = super.readCookieUser();
		String userType=currentUser.getUserType();
		List<String> bindedId = new ArrayList<String>();
		pagination = new Pagination(currentPage, pageNum);
		if (StringUtils.isNotEmpty(conditionString)) {
			if (conditionString.trim().length() == 10) {
				if (StringUtil.isSingleMail(conditionString)) {
					mailNO = conditionString.trim();
				} else {
					buyerPhone = conditionString.trim();
				}
			} else if (StringUtil.isPhone(conditionString)
					|| StringUtil.isMobile(conditionString)) {
				buyerPhone = conditionString.trim();
			} else
				buyerName = conditionString.trim();
		}
		if(userType.equals("1") || userType.equals("11") || userType.equals("13")) {
			/**
			 * 此处需判断如果选择了店铺就不用查询所有的绑定店铺. 根据用户id获取用户custormerId存于bindedId中
			 */
			if (bindUserId != null && bindUserId != 0) {
				User bindUser = userService.getUserById(bindUserId);
				if (bindUser != null) {
					bindedId.add(bindUser.getTaobaoEncodeKey());
				}
			} else {
				bindedId = Resource.getBindedCustomerIdList(currentUser);
			}
			/**
			 * 获取绑定店铺用户
			 */
			bindUserList = new ArrayList<User>();
			for (String cur : Resource.getBindedCustomerIdList(currentUser)) {
				User u = userService.getUserByCustomerId(cur);
				if (u != null) {
					if (StringUtils.isNotEmpty(u.getShopName()))
						bindUserList.add(u);
				}
			}
			totalAttention = monitorService.countSellerMonitorAttentionList(starttime, endtime, mailNO, buyerName, buyerPhone, numProv, numCity, numDistrict, bindedId);
		} else if(userType.equals("2") || userType.equals("21") || userType.equals("23")) {//网点根据客户查询
			if(StringUtils.isNotEmpty(vipCode)) {
				if(vipCode.equals("-1")) {//其他客户 ：customerId为网点或者承包区id
					if(userType.equals("2")) 
						bindedId.add(currentUser.getId().toString());
					else
						bindedId.add(currentUser.getParentId().toString());
				} else {
					bindedId = this.getCustomerIdByUserCode(vipCode);
				}
			}
			threadList = super.getZhiKeUser(currentUser);
			int userId = currentUser.getId();
			if(!"2".equals(userType)) 
				userId = currentUser.getParentId();
			totalAttention = monitorService.countSiteMonitorAttentionList(userId, starttime, endtime, mailNO, buyerName, buyerPhone, numProv, numCity, numDistrict);
		} else if(userType.equals("4") || userType.equals("41")) {
			//平台用户没有关注
			vipList = userService.pingTaiSelect(currentUser, 1);
			if(vipId==0) {
				isAll = true;
				if(!userType.equals("4")) 
					vipId = currentUser.getParentId();
				vipId = currentUser.getId();
			} else {
				User curFCUser = userService.getUserById(vipId);
				if(StringUtils.isNotEmpty(curFCUser.getTaobaoEncodeKey())) {
					bindedId.add(curFCUser.getTaobaoEncodeKey());
				}
			}
			totalAttention = 0;
		}
		Map<String, Integer> countOrderMap=new HashMap<String, Integer>();
		countOrderMap= monitorService.countOrderMonitorListNew(isAll, vipId,starttime, endtime, mailNO, buyerName, buyerPhone, bindedId,
				numProv, numCity, numDistrict, pagination, mailNoOrderType);
		totalOrderSuccess=countOrderMap.get("1");
		totalOrderScan=countOrderMap.get("2");
		totalOrderFail=countOrderMap.get("3");
		totalOrderCancel=countOrderMap.get("4");
		totalOrderCreate=countOrderMap.get("5");
		/**
		 * 此处做一判断，如果统计列表中只有某一个列表中存在满足条件的列时，则自动跳转到所在列表。
		 */
		if (autoSkip == 1) {
			autoSkip();
		}
		if(tabFlag==2)//关注不分页,每页最多50条
			pagination = new Pagination(1,50);
		//卖家运单监控
		if(userType.equals("1") || userType.equals("11") || userType.equals("13")) {
			monitoreList = monitorService.getSellerMonitorList(tabFlag, status, currentUser.getTaobaoEncodeKey(), starttime, endtime, mailNO, buyerName, buyerPhone, orderType, pagination, bindedId, numProv, numCity, numDistrict, mailNoOrderType);
		}
		//网点运单监控
		else if(userType.equals("2") || userType.equals("21") || userType.equals("23")) {//网点根据客户查询
			int userId = currentUser.getId();
			if(!userType.equals("2")) 
				userId = currentUser.getParentId();
			monitoreList = monitorService.getSiteMonitorList(tabFlag, status, userId, starttime, endtime, mailNO, buyerName, buyerPhone, orderType, pagination, bindedId, numProv, numCity, numDistrict, mailNoOrderType);
		}
		//平台用户运单监控
		else if(userType.equals("4") || userType.equals("41")) {
			monitoreList = monitorService.getPlatMonitorList(isAll, tabFlag, status, vipId, starttime, endtime, mailNO, buyerName, buyerPhone, orderType, pagination, numProv, numCity, numDistrict, mailNoOrderType);
		}
		pagination.setTotalRecords(setTotalSum(tabFlag, status,
				totalOrderSuccess, totalOrderScan, totalOrderFail,
				totalOrderCancel, totalAttention, totalOrderCreate));
		//-- rizhi
		long QuestionMonitorActionTimeEnd=System.currentTimeMillis();
		long QuestionMonitorActionTime=QuestionMonitorActionTimeStart-QuestionMonitorActionTimeEnd;
		logger.error("运单监控 QuestionnaireMonitorAction 总用时毫秒数:"+QuestionMonitorActionTime);
		return "list";
	}

	/**
	 * 根据查询条件查询并返回我的关注列表
	 * @return
	 * @throws ParseException
	 * @throws DataAccessException
	 */
	public String attentionlist() throws DataAccessException, ParseException {
		User currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
		//查最近三个月的数据
		starttime = DateUtil.format(DateUtil.getDateBefore(90), "yyyy-MM-dd");
		endtime = DateUtil.format(new Date(), "yyyy-MM-dd");
		pagination = new Pagination(currentPage, 50);
		if(currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("13")) {
			List bindedId = Resource.getBindedCustomerIdList(currentUser);
			monitoreList = monitorService.getSellerMonitorList(tabFlag, status, currentUser.getTaobaoEncodeKey(), starttime, endtime, mailNO, buyerName, buyerPhone, orderType, pagination, bindedId, numProv, numCity, numDistrict, mailNoOrderType);
		} else if(currentUser.getUserType().equals("2") || currentUser.getUserType().equals("21") || currentUser.getUserType().equals("23")) {//网点根据客户查询
			int userId = currentUser.getId();
			if(!currentUser.getUserType().equals("2")) 
				userId = currentUser.getParentId();
			monitoreList = monitorService.getSiteMonitorList(tabFlag, status, userId, starttime, endtime, mailNO, buyerName, buyerPhone, orderType, pagination, null, numProv, numCity, numDistrict, mailNoOrderType);
		}
		if(monitoreList!=null)
			pagination.setTotalRecords(monitoreList.size());
		else 
			pagination.setTotalRecords(0);
		return "attentionlist";
	}

	/**
	 * 此方法用户判断当前所查询的列表统计为0时，则自动跳转到下一列表
	 */
	private void autoSkip() {
		/**
		 * @date 2012-2-8 需求变更：去掉问题件          1(成功订单)、2(正在派送)、3(走件中)、4(退货单) 5(接单中)
		 */
		if (tabFlag == 1) {
			if (status == 5 && totalOrderCreate == 0) {
				if (totalOrderFail > 0)
					status = 3;
				else if (totalOrderScan > 0)
					status = 2;
				else if (totalOrderSuccess > 0)
					status = 1;
				else if (totalAttention > 0)
					tabFlag = 2;
			} else if (status == 3 && totalOrderFail == 0) {
				if (totalOrderCreate > 0)
					status = 5;
				else if (totalOrderScan > 0)
					status = 2;
				else if (totalOrderSuccess > 0)
					status = 1;
				else if (totalAttention > 0)
					tabFlag = 2;
			} else if (status == 2 && totalOrderScan == 0) {
				if (totalOrderCreate > 0)
					status = 5;
				else if (totalOrderFail > 0)
					status = 3;
				else if (totalOrderSuccess > 0)
					status = 1;
				else if (totalAttention > 0)
					tabFlag = 2;
			} else if (status == 1 && totalOrderSuccess == 0) {
				if (totalOrderCreate > 0)
					status = 5;
				else if (totalOrderFail > 0)
					status = 3;
				else if (totalOrderScan > 0)
					status = 2;
				else if (totalAttention > 0)
					tabFlag = 2;
			}
		} else if (tabFlag == 2 && totalAttention == 0) {
			if (totalOrderCreate > 0){
				tabFlag = 1;
				status = 5;
			} else if (totalOrderFail > 0) {
				tabFlag = 1;
				status = 3;
			} else if (totalOrderScan > 0) {
				tabFlag = 1;
				status = 2;
			} else if (totalOrderSuccess > 0) {
				tabFlag = 1;
				status = 1;
			}
		}
	}

	/**
	 * 设置分页记录总数
	 * 
	 * @param tabFlag
	 *            tab标示：1(订单)、2(关注运单)
	 * @param status
	 *            订单中子标示：1(成功订单)、2(正在派送)、3(走件中)、4(退货单)
	 * @param totalOrderSuccess
	 * @param totalOrderScan
	 * @param totalOrderFail
	 * @param totalOrderCancel
	 * @param totalAttention
	 * @return
	 */
	private int setTotalSum(int tabFlag, int status, int totalOrderSuccess,
			int totalOrderScan, int totalOrderFail, int totalOrderCancel,
			int totalAttention, int totalOrderCreate) {
		if (tabFlag == 1) {
			if (status == 1)
				return totalOrderSuccess;
			if (status == 2)
				return totalOrderScan;
			if (status == 3)
				return totalOrderFail;
			if (status == 4)
				return totalOrderCancel;
			if (status == 5)
				return totalOrderCreate;
		}
		if (tabFlag == 2)
			return totalAttention;
		return 0;
	}

	// 运单监控中传入的运单号。
	private String mailNo;

	private OrderProductBean jsonBean;
	private int checkLogistic = 0;

	/**
	 * 运单监控中查看订单详情<br>
	 * 是问题件监控的时候，还需显示该运单的物流信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String orderMonitorJson() throws Exception {
		Order order = orderService.getOrderByMailNo(mailNo);
		jsonBean = new OrderProductBean();
		if (order != null) {
			List<Product> productList = productService
					.getProductListByOrder(order.getId());
			jsonBean.setOrder(order);
			jsonBean.setProduct(productList);
		}
		if (checkLogistic == 1) {
			TaoBaoOrderAction op = new TaoBaoOrderAction();
			String[] logisticsId = { mailNo };
			List<QueryOrder> logisticList = op.ordersSearch(logisticsId);// 运单物流信息
			jsonBean.setLogisticList(logisticList);
		}
		return "orderMonitorJson";
	}

	public List<MonitorBean> getMonitoreList() {
		return monitoreList;
	}

	private static String dateArithmetic(Date originalDate, int countDay) {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 在当前日期上加-countDay天数
		cal.add(Calendar.DAY_OF_MONTH, -countDay);
		return sdm.format(cal.getTime());
	}
	
	/**
     * 查找同一用户编码下已激活的卖家
     * 
     * @param userCode
     * @return
     */
    private List<String> getCustomerIdByUserCode(String userCode) {
        List<String> list = new ArrayList<String>();
        List<User> userList = userService.searchUsersByCodeTypeState(userCode, "1", "1");// 查找同一用户编码下已激活的买家
        if (userList != null && userList.size() > 0) {
            for (User user : userList) {
                if (user.getTaobaoEncodeKey() != null && !(user.getTaobaoEncodeKey().equals(""))) 
                	list.add(user.getTaobaoEncodeKey());
            }
        } else {
            return null;
        }
        if (list.size() > 0) return list;
        else return null;
    }

	public void setMonitoreList(List<MonitorBean> monitoreList) {
		this.monitoreList = monitoreList;
	}

	public List<Questionnaire> getQuestioinnaireList() {
		return questioinnaireList;
	}

	public void setQuestioinnaireList(List<Questionnaire> questioinnaireList) {
		this.questioinnaireList = questioinnaireList;
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

	public String getMailNO() {
		return mailNO;
	}

	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
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

	public void setPagination(Pagination<Questionnaire> pagination) {
		this.pagination = pagination;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getTabFlag() {
		return tabFlag;
	}

	public void setTabFlag(int tabFlag) {
		this.tabFlag = tabFlag;
	}

	public int getCheckLogistic() {
		return checkLogistic;
	}

	public void setCheckLogistic(int checkLogistic) {
		this.checkLogistic = checkLogistic;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public OrderProductBean getJsonBean() {
		return jsonBean;
	}

	public void setJsonBean(OrderProductBean jsonBean) {
		this.jsonBean = jsonBean;
	}

	public List<User> getVipList() {
		return vipList;
	}

	public void setVipList(List<User> vipList) {
		this.vipList = vipList;
	}

	public List<UserThread> getThreadList() {
		return threadList;
	}

	public void setThreadList(List<UserThread> threadList) {
		this.threadList = threadList;
	}

	public Integer getVipId() {
		return vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
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

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public int getAutoSkip() {
		return autoSkip;
	}

	public void setAutoSkip(int autoSkip) {
		this.autoSkip = autoSkip;
	}

	public String getConditionString() {
		return conditionString;
	}

	public void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}

	public List<User> getBindUserList() {
		return bindUserList;
	}

	public void setBindUserList(List<User> bindUserList) {
		this.bindUserList = bindUserList;
	}

	public Integer getBindUserId() {
		return bindUserId;
	}

	public void setBindUserId(Integer bindUserId) {
		this.bindUserId = bindUserId;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Integer getNumProv() {
		return numProv;
	}

	public void setNumProv(Integer numProv) {
		this.numProv = numProv;
	}

	public Integer getNumCity() {
		return numCity;
	}

	public void setNumCity(Integer numCity) {
		this.numCity = numCity;
	}

	public Integer getNumDistrict() {
		return numDistrict;
	}

	public void setNumDistrict(Integer numDistrict) {
		this.numDistrict = numDistrict;
	}

	public String getMailNoOrderType() {
		return mailNoOrderType;
	}

	public void setMailNoOrderType(String mailNoOrderType) {
		this.mailNoOrderType = mailNoOrderType;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getVipCode() {
		return vipCode;
	}

	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}

	public Integer getTotalOrderSuccess() {
		return totalOrderSuccess;
	}

	public void setTotalOrderSuccess(Integer totalOrderSuccess) {
		this.totalOrderSuccess = totalOrderSuccess;
	}

	public Integer getTotalOrderFail() {
		return totalOrderFail;
	}

	public void setTotalOrderFail(Integer totalOrderFail) {
		this.totalOrderFail = totalOrderFail;
	}

	public Integer getTotalOrderScan() {
		return totalOrderScan;
	}

	public void setTotalOrderScan(Integer totalOrderScan) {
		this.totalOrderScan = totalOrderScan;
	}

	public Integer getTotalOrderCancel() {
		return totalOrderCancel;
	}

	public void setTotalOrderCancel(Integer totalOrderCancel) {
		this.totalOrderCancel = totalOrderCancel;
	}

	public Integer getTotalAttention() {
		return totalAttention;
	}

	public void setTotalAttention(Integer totalAttention) {
		this.totalAttention = totalAttention;
	}

	public Integer getTotalOrderCreate() {
		return totalOrderCreate;
	}

	public void setTotalOrderCreate(Integer totalOrderCreate) {
		this.totalOrderCreate = totalOrderCreate;
	}

	public String getOtherUserId() {
		return otherUserId;
	}

	public void setOtherUserId(String otherUserId) {
		this.otherUserId = otherUserId;
	}
	
}
