/**
 * WayBillAction.java
 * Wangyong
 * 2011-8-16 上午10:31:35
 */
package net.ytoec.kernel.action.waybill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.helper.WayBillActionHelper;
import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.UnlikeFreight;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.AttentionMailService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.OrderExpressService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.UnlikeFreightService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 核心平台运单查询Action。<br/>
 * 运单数据从金刚接口传入，通过此action进行处理并提交客户。
 * 
 * @author Wangyong
 * @2011-8-16 net.ytoec.kernel.action.waybill
 */
@Controller
@Scope("prototype")
public class WayBillAction extends AbstractActionSupport {

	private static final long serialVersionUID = -7764650873654878107L;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private AttentionMailService<AttentionMail> attenService;

	private Logger logger = Logger.getLogger(WayBillAction.class);
	//运单号
	private String logisticsIds;
	private List<QueryOrder> orderList;

	private List<QueryOrder> timeOrderList;
	/** 打印模板 */
	private List<OrderExpress> orderExpList;
	private List<User> vipList;
	private List<UserThread> vipThreadList;
	private List<User> bindedUserList;
	// 临时存放按单号查询订单的结果集
	private static List<QueryOrder> tempList = new ArrayList<QueryOrder>();

	private Integer currentPage = 1;
	@SuppressWarnings("rawtypes")
	private Pagination pagination; // 分页对象

	private String startTime;
	private String endTime;
	private String startDate;
	private String endDate;
	private String orderFlag;// 是否是新建还是修改

	private String buyerStartTime;// 按买家信息查询时开始时间
	private String buyerEndTime;// 按买家信息查询时结束时间
	private Integer vipName;// 按时间查询是用户id
	private String vipId;// 按买家信息查询时用户id
	private String userCodeForTime;// 按时间查询是用户编码
	private String userCodeForBuyer;// 按买家信息查询时用户编码
	private Integer isCheck;
	private Integer mailNum;

	// 按店铺筛选
	private Integer bindUserId;

	private String buyerName;
	private String buyerPhone;

	private String fromList;

	private UnlikeFreight unlikeFreight;
	private TraderInfo traderInfo;
	private Order order;
	private String managerTab;

	private String response;
	private List<UnlikeFreight> unlikeFreightOrderList;
	@Inject
	private UnlikeFreightService<UnlikeFreight> unlikeFreightService;
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;
	private List<Order> ufOrderList;
	@Inject
	private MessageService<Message> messageService;
	@Inject
	private TraderInfoService<TraderInfo> traderInfoService;

	private OrderExpress orderExpress;
	@Inject
	private OrderExpressService<OrderExpress> orderExpressService;
	@Inject
	private EccoreSearchService eccoreSearchService;
	@Inject
	private OrderExpressService<OrderExpress> orderExpService;
	HttpServletResponse resp;
	/**
	 * 按时间和单号查询的标记：1表示按单号查。2表示按时间查。3按买家信息查。默认按单号查
	 */
	private int flag = 1;

	@SuppressWarnings("rawtypes")
	public String bill() {
		flag = 1;
		logisticsIds = null;
		orderList = null;
		isCheck = mailNum = null;
		pagination = new Pagination(1, pageNum);
		pagination.setTotalRecords(0);
		User currentUser = super.readCookieUser();

		if (User.BUYER.equals(currentUser.getUserAuthority())) {
			bindedUserList = new ArrayList<User>();
			for (String cusId : Resource.getBindedCustomerIdList(currentUser)) {
				bindedUserList.add(userService.getUserByCustomerId(cusId));
			}
		} else if (User.SITE.equals(currentUser.getUserAuthority())) {
			vipThreadList = super.getZhiKeUser(currentUser);
		} else if (User.LARGE_SELLER.equals(currentUser.getUserAuthority())) {
			// 分仓和入住商家账户
			vipList = userService.pingTaiSelect(currentUser, 0);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		if (startTime == null || endTime == null || startTime.equals("")
				|| endTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			startTime = DateUtil.dateArithmetic(new Date(), 6);
			endTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		if (buyerStartTime == null || buyerEndTime == null
				|| startTime.equals("") || buyerEndTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			buyerStartTime = DateUtil.dateArithmetic(new Date(), 6);
			buyerEndTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		return "bill";
	}

	@SuppressWarnings("rawtypes")
	public String nonList() {
		flag = 1;
		logisticsIds = null;
		orderList = null;
		isCheck = mailNum = null;
		pagination = new Pagination(1, pageNum);
		pagination.setTotalRecords(0);
		User currentUser = super.readCookieUser();

		if (User.BUYER.equals(currentUser.getUserAuthority())) {
			bindedUserList = new ArrayList<User>();
			for (String cusId : Resource.getBindedCustomerIdList(currentUser)) {
				bindedUserList.add(userService.getUserByCustomerId(cusId));
			}
		} else if (User.SITE.equals(currentUser.getUserAuthority())) {
			vipThreadList = super.getZhiKeUser(currentUser);
		} else if (User.LARGE_SELLER.equals(currentUser.getUserAuthority())) {
			// 分仓和入住商家账户
			vipList = userService.pingTaiSelect(currentUser, 0);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		if (startTime == null || endTime == null || startTime.equals("")
				|| endTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			startTime = DateUtil.dateArithmetic(new Date(), 6);
			endTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		if (buyerStartTime == null || buyerEndTime == null
				|| startTime.equals("") || buyerEndTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			buyerStartTime = DateUtil.dateArithmetic(new Date(), 6);
			buyerEndTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		return "nonList";
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@SuppressWarnings("rawtypes")
	public String unlikefreight() {
		managerTab = "0";
		flag = 0;
		logisticsIds = null;
		orderList = null;
		isCheck = mailNum = null;
		pagination = new Pagination(1, pageNum);
		pagination.setTotalRecords(0);
		// User currentUser = (User)session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		String usertype = currentUser.getUserType();
		if (usertype != null
				&& ("2".equals(usertype) || "21".equals(usertype)
						|| "22".equals(usertype) || "23".equals(usertype))) {
			currentUser.setFinancialManager("financialManager");
			currentUser.setUnlikefreight("unlikefreight");
			vipThreadList = super.getZhiKeUser(currentUser);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		if (startTime == null || endTime == null || startTime.equals("")
				|| endTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			startTime = DateUtil.dateArithmetic(new Date(), 6);
			endTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		if (buyerStartTime == null || buyerEndTime == null
				|| startTime.equals("") || buyerEndTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			buyerStartTime = DateUtil.dateArithmetic(new Date(), 6);
			buyerEndTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		// //System.out.println("--unlikefreight-response--"+response);
		return "unlikefreight";
	}

	@SuppressWarnings("rawtypes")
	public String mjunlikefreight() {
		flag = 1;
		logisticsIds = null;
		orderList = null;
		isCheck = mailNum = null;
		pagination = new Pagination(1, pageNum);
		pagination.setTotalRecords(0);
		User currentUser = super.readCookieUser();
		String usertype = currentUser.getUserType();

		if (usertype != null && ("4".equals(usertype))) {// 平台用户
			buildFCUser(currentUser);
		}
		if (startTime == null || endTime == null || startTime.equals("")
				|| endTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			startTime = DateUtil.dateArithmetic(new Date(), 6);
			endTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		if (buyerStartTime == null || buyerEndTime == null
				|| startTime.equals("") || buyerEndTime.equals("")) {
			/**
			 * 默认情况下取当前时间在内的7天内的数据。
			 */
			buyerStartTime = DateUtil.dateArithmetic(new Date(), 6);
			buyerEndTime = DateUtil.dateArithmetic(new Date(), 0);
		}
		return "mjunlikefreight";
	}

	/**
	 * 按运单号查询物流跟踪信息
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String unlikefreightlist() {
		managerTab = "1";
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		unlikeFreightOrderList = null;
		ufOrderList = new ArrayList<Order>();

		Map map = new HashMap();
		if (userCodeForTime != null && !("".equals(userCodeForTime))) {
			map.put("userCode", userCodeForTime);
		}
		if (logisticsIds != null && !(logisticsIds.equals(""))) {
			String[] logisticsIdArray = logisticsIds.split("/");
			map.put("mailNo", mailNolist(logisticsIdArray));
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);

		int totalRecord = (unlikeFreightService.getUnlikeFreightList(map,
				pagination, false)).size();
		unlikeFreightOrderList = (List<UnlikeFreight>) unlikeFreightService
				.getUnlikeFreightList(map, pagination, true);
		if (totalRecord > 0)
			pagination.setTotalRecords(totalRecord);
		else
			pagination.setTotalRecords(0);

		if (currentUser.getUserType() != null
				&& (currentUser.getUserType().equals("2")
						|| currentUser.getUserType().equals("21")
						|| currentUser.getUserType().equals("22") || currentUser
						.getUserType().equals("23"))) {
			currentUser.setFinancialManager("financialManager");
			currentUser.setUnlikefreight("unlikefreight");
			vipThreadList = super.getZhiKeUser(currentUser);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		return "unlikefreightlist";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String mjunlikefreightlist() {
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		unlikeFreightOrderList = null;
		ufOrderList = new ArrayList<Order>();
		if (logisticsIds != null && !(logisticsIds.equals(""))) {
			try {
				String[] logisticsIdArray = logisticsIds.split("/");
				/**
				 * 按每页显示数目查询接口订单
				 */
				String[] logistics = substrArray(logisticsIdArray,
						pagination.getStartIndex(), pageNum);
				if (flag == 1 && logistics != null) {
					Order kk = orderService.getOrderByMailNo(logistics[0]);
					if (kk != null) {
						User ufuser = userService.getUserByCustomerId(kk
								.getCustomerId());
						UserThread uh = new UserThread();
						uh.setUserCode(ufuser.getUserCode());
						if (ufuser != null && uh != null) {
							uh = userThreadService.searchUsersByCode(uh).get(0);
							// 存储直客名称
							kk.setVersion(uh.getUserName());
							// 存储直客id
							kk.setOrderType(uh.getId());
							// 存储用户编码
							kk.setTradeNo(ufuser.getUserCode());
							// 存储网点编码
							kk.setTxLogisticId(ufuser.getSite());
						}

						TraderInfo traderInfo = questionnaireService
								.getCustomerInfo(kk.getMailNo());
						kk.setRemark(traderInfo.getProv()
								+ traderInfo.getCity()
								+ traderInfo.getDistrict()
								+ traderInfo.getAddress());
						ufOrderList.add((Order) kk);
					}
				} else {
					Map map = new HashMap();
					map.put("mailNo", mailNolist(logisticsIdArray));
					if(vipName != null && vipName != 0){
						  User uUser = userService.getUserById(vipName);
						  map.put("userCode", uUser.getUserCode());
					}else {
						  map.put("userCode", currentUser.getUserCode());
					}
					map.put("startTime", startTime);
					map.put("endTime", endTime);

					// 如果是值客子账户只能看自己的数据
					if (currentUser.getUserType().equals("11")
							|| currentUser.getUserType().equals("12")
							|| currentUser.getUserType().equals("13")) {
						List<String> customerIdList = new ArrayList<String>();
						customerIdList.add(currentUser.getTaobaoEncodeKey());
						map.put("customerId", customerIdList);
					}
					List<UnlikeFreight> newunlikeFreightOrderList = (List<UnlikeFreight>) unlikeFreightService
							.getUnlikeFreightList(map, pagination, false);
					if (newunlikeFreightOrderList != null) {
						for (UnlikeFreight newUnlikeFreight : newunlikeFreightOrderList) {
							List<User> userlist = userService
									.searchUsersByCodeTypeState(
											newUnlikeFreight.getSiteCode(),
											"2", "1");
							if (userlist.size() > 0) {
								newUnlikeFreight.setRemark(userlist.get(0)
										.getUserNameText());
							}
						}
					}
					unlikeFreightOrderList = newunlikeFreightOrderList;
				}

				if (logisticsIdArray != null && unlikeFreightOrderList != null)
					pagination.setTotalRecords(unlikeFreightOrderList.size());
				else
					pagination.setTotalRecords(0);
			} catch (Exception e) {
				logger.error("按运单号查询物流跟踪信息失败", e);
			}

		} else {
			if (flag == 2) {
				Map map = new HashMap();
				if(vipName != null && vipName != 0){
				  User uUser = userService.getUserById(vipName);
				  map.put("userCode", uUser.getUserCode());
				}else {
					map.put("userCode", currentUser.getUserCode());
				}
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				// 如果是值客子账户只能看自己的数据
				if (currentUser.getUserType().equals("11")
						|| currentUser.getUserType().equals("12")
						|| currentUser.getUserType().equals("13")) {
					List<String> customerIdList = new ArrayList<String>();
					customerIdList.add(currentUser.getTaobaoEncodeKey());
					map.put("customerId", customerIdList);
				}

				List<UnlikeFreight> newunlikeFreightOrderList = (List<UnlikeFreight>) unlikeFreightService
						.getUnlikeFreightList(map, pagination, false);
				for (UnlikeFreight newUnlikeFreight : newunlikeFreightOrderList) {
					List<User> userlist = userService
							.searchUsersByCodeTypeState(
									newUnlikeFreight.getSiteCode(), "2", "1");
					if (userlist.size() > 0) {
						newUnlikeFreight.setRemark(userlist.get(0)
								.getUserNameText());
					}
				}
				unlikeFreightOrderList = newunlikeFreightOrderList;
				if (unlikeFreightOrderList != null)
					pagination.setTotalRecords(unlikeFreightOrderList.size());
				else
					pagination.setTotalRecords(0);
			} else {
				pagination.setTotalRecords(0);
			}

		}
		if (currentUser.getUserType() != null
				&& ("4".equals(currentUser.getUserType()))) {// 平台用户
			this.buildFCUser(currentUser);
		}
		if (flag == 2 || flag == 3) {
			ufOrderList = null;
		} else {
			unlikeFreightOrderList = null;
		}
		return "mjunlikefreightlist";
	}

	/*
	 * 增加渠道信息数据
	 */
	public String add() {
		User currentUser = super.readCookieUser();
		String str_address = "";
		if (orderFlag != null && orderFlag.equals("1")) {// 如果是存在的定单修改不用拼地址
			if (unlikeFreight != null)
				str_address = unlikeFreight.getAddress();
		} else {
			if (unlikeFreight != null)
				str_address = traderInfo.getProv() + traderInfo.getCity()
						+ traderInfo.getDistrict() + unlikeFreight.getAddress();
		}
		response = "0";
		/**
		 * 取 UnlikeFreight 表数据如果存在修改 UnlikeFreight表 不存在新建 UnlikeFreight order
		 * tradeinfo表
		 */
		UnlikeFreight _UnlikeFreight = unlikeFreightService
				.getUnlikeFreightByMailNo(unlikeFreight.getMailNo());
		if (_UnlikeFreight != null && _UnlikeFreight.getMailNo() != null) {

			// 修改记录
			_UnlikeFreight.setReason(unlikeFreight.getReason());
			_UnlikeFreight.setPrice(unlikeFreight.getPrice());
			if (unlikeFreightService.editUnlikeFreight(_UnlikeFreight)) {
				
				List<User> ss = userService.searchUsersByCodeTypeState(
						unlikeFreight.getUserCode(), "1", "1");
				if (ss != null && ss.size() > 0) {
					unlikeFreight.setMobile(ss.get(0).getMobilePhone());
					unlikeFreight.setCustomerId(ss.get(0).getTaobaoEncodeKey());
				} else {
					response = "添加失败";
					return "add";
				}

				String messageContent = "<P>尊敬的客户：</P>"
						+ "<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 您有一笔运费调整记录，请你及时核对，有问题请随时联系，谢谢您的支持！</P>"
						+ "<P>运单号：" + _UnlikeFreight.getMailNo() + "</P>"
						+ "<P>寄往：" + str_address + "</P>" + "<P>调整原因："
						+ _UnlikeFreight.getReason() + "</P>" + "<P>重量："
						+ _UnlikeFreight.getWeight() + "</P>" + "<P>调整后价格："
						+ _UnlikeFreight.getPrice() + "元</P>";
				sendPriceMessage(ss.get(0).getId(), currentUser.getId(),
						messageContent);

				response = "修改成功";
			} else {
				response = "修改失败";
				return "add";
			}
		} else {

			if (unlikeFreight != null) {

				unlikeFreight.setAddress(str_address);
				Order kk = orderService.getOrderByMailNo(unlikeFreight
						.getMailNo());
				if ("0".equals(unlikeFreight.getType())) {
					// System.out.println("kk=unlikeFreight.getType()==="+unlikeFreight.getType());
					// System.out.println("kk===="+kk);
					if (kk != null) {
						unlikeFreight.setCustomerId(kk.getCustomerId());
						User ufuser = userService.getUserByCustomerId(kk
								.getCustomerId());
						UserThread uh = new UserThread();
						uh.setUserCode(ufuser.getUserCode());
						// System.out.println(uh.getUserCode());
						if (ufuser != null) {
							uh = userThreadService.searchUsersByCode(uh).get(0);
							// 存储直客名称
							unlikeFreight.setUserName(uh.getUserName());
							// kk.setVersion(uh.getUserName());
							// 存储直客id
							unlikeFreight.setUserthreadId(uh.getId());
							// kk.setOrderType(uh.getId());
							// 存储用户编码
							unlikeFreight.setUserCode(ufuser.getUserCode());
							// kk.setTradeNo(ufuser.getUserCode());
							// 存储网点编码
							unlikeFreight.setSiteCode(ufuser.getSite());
							// kk.setTxLogisticId(ufuser.getSite());

							unlikeFreight.setMobile(ufuser.getMobilePhone());
						}
						unlikeFreight.setReason(buyerEndTime);
						if (unlikeFreight.getMailNo() != null) {
							orderService.updateOrderByMailNo(
									unlikeFreight.getMailNo(), "0",
									unlikeFreight.getPrice());
						}

						String messageContent = "<P>尊敬的客户：</P>"
								+ "<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 您有一笔运费调整记录，请你及时核对，有问题请随时联系，谢谢您的支持！</P>"
								+ "<P>运单号：" + unlikeFreight.getMailNo()
								+ "</P>" + "<P>寄往：" + str_address + "</P>"
								+ "<P>调整原因：" + unlikeFreight.getReason()
								+ "</P>" + "<P>重量：" + unlikeFreight.getWeight()
								+ "kg</P>" + "<P>调整后价格："
								+ unlikeFreight.getPrice() + "元</P>";
						sendPriceMessage(ufuser.getId(), currentUser.getId(),
								messageContent);
					}
				} else {
					unlikeFreight.setType("1");
					if (unlikeFreight.getUserCode() == null) {
						unlikeFreight.setUserCode(userCodeForBuyer);
					}
					unlikeFreight.setGoodsTime(DateUtil.valueof(buyerStartTime,
							"yyyy-MM-dd"));
					unlikeFreight.setReason(buyerEndTime);

					UserThread uh = new UserThread();
					uh.setUserCode(unlikeFreight.getUserCode());
					if (unlikeFreight.getUserCode() != null) {
						uh = userThreadService.searchUsersByCode(uh).get(0);
						// 存储直客名称
						unlikeFreight.setUserName(uh.getUserName());
						// 存储直客id
						unlikeFreight.setUserthreadId(uh.getId());
						// 存储用户编码
						unlikeFreight.setUserCode(uh.getUserCode());
						// 存储网点编码
						unlikeFreight.setSiteCode(uh.getSiteCode());
					}

					List<User> ss = userService.searchUsersByCodeTypeState(
							unlikeFreight.getUserCode(), "1", "1");
					if (ss != null && ss.size() > 0) {
						unlikeFreight.setMobile(ss.get(0).getMobilePhone());
						unlikeFreight.setCustomerId(ss.get(0)
								.getTaobaoEncodeKey());
					} else {
						response = "添加失败";
						return "add";
					}
					String messageContent = "<P>尊敬的客户：</P>"
							+ "<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 您有一笔运费调整记录，请你及时核对，有问题请随时联系，谢谢您的支持！</P>"
							+ "<P>运单号：" + unlikeFreight.getMailNo() + "</P>"
							+ "<P>寄往：" + str_address + "</P>" + "<P>调整原因："
							+ unlikeFreight.getReason() + "</P>" + "<P>重量："
							+ unlikeFreight.getWeight() + "</P>" + "<P>调整后价格："
							+ unlikeFreight.getPrice() + "元</P>";
					sendPriceMessage(ss.get(0).getId(), currentUser.getId(),
							messageContent);
				}

				boolean ff = unlikeFreightService
						.addUnlikeFreight(unlikeFreight);
				if (ff) {
					response = "添加成功";
					if(kk != null && StringUtils.isNotEmpty(kk.getMailNo())){
						//更新
						kk.setTrimFreight(unlikeFreight.getPrice());
						kk.setRemark(unlikeFreight.getReason());
						//数据库更新status不能是中文的
						String status = kk.getStatus();
						if("等待确认".equals(status)){
							kk.setStatus("CONFIRM");
						}else if("接单中".equals(status)){
							kk.setStatus("0");
						}else if("派件扫描".equals(status)){
							kk.setStatus("SENT_SCAN");
						}else if("流转信息".equals(status)){
							kk.setStatus("TRACKING");
						}else if("接单".equals(status)){
							kk.setStatus("ACCEPT");
						}else if("不接单".equals(status)){
							kk.setStatus("UNACCEPT");
						}else if("揽收成功".equals(status)){
							kk.setStatus("GOT");
						}else if("揽收失败".equals(status)){
							kk.setStatus("NOT_SEND");
						}else if("失败".equals(status)){
							kk.setStatus("FAILED");
						}else if("已签收".equals(status)){
							kk.setStatus("SIGNED");
						}else if("订单取消".equals(status)){
							kk.setStatus("WITHDRAW");
						}
						orderService.updateOrder(kk);
					} else{
						//新增
						Order uforder = new Order();
						if ("1".equals(unlikeFreight.getType())) {
							uforder.setMailNo(unlikeFreight.getMailNo());
							uforder.setStatus("SIGNED");
							uforder.setWeight((float) unlikeFreight.getWeight());
							uforder.setLineType("1");
							uforder.setToAddr(unlikeFreight.getAddress());
							uforder.setNetFreight((float) Math.round(unlikeFreight
									.getPrice() * 100) / 100);
							uforder.setFreightType("0");
							uforder.setRemark(unlikeFreight.getReason());
							uforder.setBuyName(unlikeFreight.getUserName());
							uforder.setBuyMobile(unlikeFreight.getMobile());
							// uforder.setUpdateTime(unlikeFreight.getCreateTime());
							uforder.setOrderType(99);
							uforder.setTxLogisticId(unlikeFreight.getId() + "");
							uforder.setCustomerId(unlikeFreight.getCustomerId());
							uforder.setTrimFreight(unlikeFreight.getPrice());
							orderService.addOrder(uforder);
	
							User currUser = super.readCookieUser();
							TraderInfo sender = new TraderInfo();
							sender.setOrderId(uforder.getId());
	
							sender.setProv(currUser.getAddressProvince());
							sender.setCity(currUser.getAddressCity());
							sender.setDistrict(currUser.getAddressDistrict());
	
							sender.setTradeType("0");
							sender.setAddress(currentUser.getAddressProvince()
									+ currentUser.getAddressCity()
									+ currentUser.getAddressDistrict()
									+ currentUser.getAddressStreet());
							traderInfoService.addTraderInfo(sender);
	
							TraderInfo sender1 = new TraderInfo();
							sender1.setOrderId(uforder.getId());
							sender1.setTradeType("1");
							sender1.setProv(traderInfo.getProv());
							sender1.setCity(traderInfo.getCity());
							sender1.setDistrict(traderInfo.getDistrict());
							sender1.setNumProv(traderInfo.getNumProv());
							sender1.setNumCity(traderInfo.getNumCity());
							sender1.setNumDistrict(traderInfo.getNumDistrict());
	
							sender1.setAddress(str_address);
							traderInfoService.addTraderInfo(sender1);
							unlikeFreight.setSiteId(uforder.getId());
							unlikeFreightService.editUnlikeFreight(unlikeFreight);
						}
					}
				} else {
					response = "添加失败";
					return "add";
				}
			}
		}
		unlikeFreightOrderList = null;
		return SUCCESS;
	}

	public TraderInfo getTraderInfo() {
		return traderInfo;
	}

	public void setTraderInfo(TraderInfo traderInfo) {
		this.traderInfo = traderInfo;
	}

	public String editUnlikeFreight() {
		response = null;
		if (unlikeFreight != null) {
			// User currentUser = (User)session.get(Constants.SESSION_USER);
			User currentUser = super.readCookieUser();
			if ("0".equals(unlikeFreight.getType())) {
				UnlikeFreight uFreight = unlikeFreightService
						.getUnlikeFreightById(unlikeFreight.getId());
				// System.out.println("=editUnlikeFreight==="+buyerEndTime);
				// System.out.println("=editUnlikeFreight=unlikeFreight.getReason()=="+unlikeFreight.getReason());
				// uFreight.setReason(unlikeFreight.getReason());
				// if("".equals(unlikeFreight.getReason())||
				// unlikeFreight.getReason()==null){
				// unlikeFreight.setReason(endTime);
				uFreight.setReason(buyerEndTime);
				// }
				uFreight.setPrice(unlikeFreight.getPrice());
				unlikeFreight = uFreight;
				User ufuser = userService.getUserByCustomerId(unlikeFreight
						.getCustomerId());
				if (unlikeFreight.getMailNo() != null) {
					orderService.updateOrderByMailNo(unlikeFreight.getMailNo(),
							"0", unlikeFreight.getPrice());
				}
				String messageContent = "<P>尊敬的客户：</P>"
						+ "<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 您有一笔运费调整记录，请你及时核对，有问题请随时联系，谢谢您的支持！</P>"
						+ "<P>运单号：" + unlikeFreight.getMailNo() + "</P>"
						+ "<P>寄往：" + unlikeFreight.getAddress() + "</P>"
						+ "<P>调整原因：" + unlikeFreight.getReason() + "</P>"
						+ "<P>重量：" + unlikeFreight.getWeight() + "</P>"
						+ "<P>调整后价格：" + unlikeFreight.getPrice() + "元</P>";
				sendPriceMessage(ufuser.getId(), currentUser.getId(),
						messageContent);

			} else {
				if (unlikeFreight.getUserCode() == null) {
					// unlikeFreight.setUserCode(userCodeForTime);
					unlikeFreight.setUserCode(userCodeForBuyer);
				}
				String[] sd = unlikeFreight.getUserCode().split("_");
				// System.out.println(sd[0]);
				// System.out.println("----"+sd[1]);
				// System.out.println("--=====adress========--"+unlikeFreight.getAddress());
				/*
				 * unlikeFreight.setGoodsTime(DateUtil.valueof(startTime,
				 * "yyyy-MM-dd")); unlikeFreight.setReason(endTime);
				 */
				unlikeFreight.setGoodsTime(DateUtil.valueof(buyerStartTime,
						"yyyy-MM-dd"));
				unlikeFreight.setReason(buyerEndTime);

				unlikeFreight.setUserCode(sd[0]);
				unlikeFreight.setUserName(sd[1]);
				List<User> ss = userService.searchUsersByCodeTypeState(
						unlikeFreight.getUserCode(), "1", "1");

				// unlikeFreight.getUserCode();
				UserThread uh = new UserThread();
				uh.setUserCode(sd[0]);
				// System.out.println(sd[0]);
				if (sd[0] != null) {
					uh = userThreadService.searchUsersByCode(uh).get(0);
					// 存储直客名称
					unlikeFreight.setUserName(uh.getUserName());
					// kk.setVersion(uh.getUserName());
					// 存储直客id
					unlikeFreight.setUserthreadId(uh.getId());
					// kk.setOrderType(uh.getId());
					// 存储用户编码
					unlikeFreight.setUserCode(uh.getUserCode());
					// kk.setTradeNo(ufuser.getUserCode());
					// 存储网点编码
					unlikeFreight.setSiteCode(uh.getSiteCode());
					// kk.setTxLogisticId(ufuser.getSite());
				}
				// System.out.println("--===:"+ss.size());
				if (ss.size() > 0) {
					unlikeFreight.setMobile(ss.get(0).getMobilePhone());
					unlikeFreight.setCustomerId(ss.get(0).getTaobaoEncodeKey());
				}
				if (unlikeFreight.getMailNo() != null) {
					orderService.updateOrderByMailNo(unlikeFreight.getMailNo(),
							"0", unlikeFreight.getPrice());
				}
				String messageContent = "<P>尊敬的客户：</P>"
						+ "<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 您有一笔运费调整记录，请你及时核对，有问题请随时联系，谢谢您的支持！</P>"
						+ "<P>运单号：" + unlikeFreight.getMailNo() + "</P>"
						+ "<P>寄往：" + unlikeFreight.getAddress() + "</P>"
						+ "<P>调整原因：" + unlikeFreight.getReason() + "</P>"
						+ "<P>重量：" + unlikeFreight.getWeight() + "</P>"
						+ "<P>调整后价格：" + unlikeFreight.getPrice() + "元</P>";
				sendPriceMessage(ss.get(0).getId(), currentUser.getId(),
						messageContent);
			}

			if (unlikeFreightService.editUnlikeFreight(unlikeFreight)) {
				response = "修改成功";
			} else
				response = "修改失败";
		}
		return "editUnlikeFreight";
	}

	public String deleteUnlikeFreight() {
		response = null;
		UnlikeFreight uFreight = new UnlikeFreight();
		// System.out.println("ppppppppppppppppp");
		if (unlikeFreight != null) {
			// User currentUser = (User)session.get(Constants.SESSION_USER);
			User currentUser = super.readCookieUser();
			uFreight = unlikeFreightService.getUnlikeFreightById(unlikeFreight
					.getId());
			User ufuser = userService.getUserByCustomerId(uFreight
					.getCustomerId());
			if ("0".equals(unlikeFreight.getType())) {
				// System.out.println("=====ppppppppppppppppp:"+unlikeFreight.getType());
				if (uFreight.getMailNo() != null) {
					orderService.updateOrderByMailNo(uFreight.getMailNo(), "1",
							uFreight.getOidPrice());
				}
			} else {
				Order uforder = new Order();
				uforder.setId(uFreight.getSiteId());
				orderService.delOrder(uforder);
			}
			String messageContent = "<P>尊敬的客户：</P>"
					+ "<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 您有一笔运费调整记录，请你及时核对，有问题请随时联系，谢谢您的支持！</P>"
					+ "<P>运单号：" + uFreight.getMailNo() + "</P>" + "<P>寄往："
					+ uFreight.getAddress() + "</P>" + "<P>调整价格已取消</P>";
			sendPriceMessage(ufuser.getId(), currentUser.getId(),
					messageContent);

			if (unlikeFreightService.deleteUnlikeFreight(unlikeFreight)) {
				response = "取消成功";
			} else
				response = "取消失败";
		}
		return "delete";
	}

	public String deleteOrderExpress() {
		if (orderExpress != null) {
			if (orderExpressService.deleteOrderExpress(orderExpress)) {
				response = "删除成功";
			} else
				response = "删除失败";
		}
		return "Express";
	}

	public String orderExpress() {
		if (isCheck == null) {
			isCheck = 0;
		}

		User currentUser = super.readCookieUser();
		// 打印模板列表
		orderExpList = orderExpService.getOrderExpressListbyStoreId(
				currentUser.getId(), null, false);

		OrderExpress sorderExpress = new OrderExpress();
		if (isCheck != null && isCheck == 1) {
			orderExpress = orderExpressService.getOrderExpressById(orderExpress
					.getId());
			return "orderExpress";
		} else if (isCheck != null && isCheck != 0) {
			if (mailNum != null) {
				sorderExpress = orderExpressService
						.getOrderExpressById(orderExpress.getId());
				sorderExpress.setOrderexpressname(startTime);
				sorderExpress.setBackgroundimageurl(logisticsIds);
				sorderExpress.setTemplatedata(orderExpress.getTemplatedata());
				sorderExpress.setStatus(orderExpress.getStatus());
				sorderExpress.setSortorder(orderExpress.getSortorder());
				sorderExpress.setStoreid(currentUser.getId());
				orderExpressService.editOrderExpress(sorderExpress);
			} else {
				orderExpress = orderExpressService.getOrderExpressById(isCheck);
			}

		} else {
			if (mailNum != null) {
				orderExpress.setOrderexpressname(startTime);
				orderExpress.setBackgroundimageurl(logisticsIds);
				orderExpress.setStoreid(currentUser.getId());
				orderExpressService.addOrderExpress(orderExpress);
			} else {
				orderExpress = new OrderExpress();
				orderExpress
						.setBackgroundimageurl("images/printtemp/template_a.jpg");
				orderExpress
						.setTemplatedata("%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Eship_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E487%3A114%3A73%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Eship_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E607%3A171%3A172%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u7535%u8BDD%3C/name%3E%3Cucode%3Eship_tel%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E665%3A114%3A105%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Eship_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E693%3A229%3A88%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Eship_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E501%3A172%3A80%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Edly_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E127%3A98%3A81%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Edly_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E229%3A159%3A162%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Edly_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E134%3A161%3A68%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Edly_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E307%3A228%3A93%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u624B%u673A%3C/name%3E%3Cucode%3Edly_mobile%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E142%3A223%3A108%3A20%3C/position%3E%3C/item%3E%3C/printer%3E");
				orderExpress.setStatus(1);
				orderExpress.setSortorder(0);
			}

		}

		return "Express";
	}

	public List<OrderExpress> getOrderExpList() {
		return orderExpList;
	}

	public void setOrderExpList(List<OrderExpress> orderExpList) {
		this.orderExpList = orderExpList;
	}

	/**
	 * 输入日期时间段，不能超过一个月 数据中心模块下的 每日订单走势 2012-3-23 zhaotiejun
	 */
	public String everydayOrderChart() {
		User currentUser = super.readCookieUser();
		/**
		 * 平台用户获取所有业务账号和入驻企业
		 */
		if (currentUser.getUserType() != null
				&& (currentUser.getUserType().equals("4") || currentUser
						.getUserType().equals("41"))) {
			if ((startDate == null || startDate.equals(""))
					&& (endDate == null || endDate.equals(""))) {
				startDate = DateUtil.dateArithmetic(new Date(), 6);
				endDate = DateUtil.dateArithmetic(new Date(), 0);
			}
			vipList = userService.pingTaiSelect(currentUser, 0);
		}
		return "everydayOrderChart";
	}

	/**
	 * 发送一条消息，告知客户
	 * 
	 * @param receiveUserId
	 * @return
	 */
	public boolean sendPriceMessage(Integer receiveUserId, Integer sendUserId,
			String messageContent) {
		boolean flag = false;

		String messageTheme = "运费调整通知";
		/** 获取管理员用户ID */
		User receiveUser = userService.getUserById(receiveUserId);
		flag = messageService.sendSystemMessage(receiveUser, messageTheme, messageContent);
		return flag;
	}
	
	/**
	 * 智能查询
	 * 
	 * @return
	 */
	@SuppressWarnings("all")
	public String list() {
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, 20);
		int totalCount = 0;
		// 定义当前页的运单号数组`
		String[] logisticsIdArray = new String[0];
		orderList = null;
		//根据当前用户相关的customerId 卖家的从缓存里获取关联账号
		List<String> bindedId = this.getCustomerIds(currentUser);
		List<EccoreSearchResultDTO> eccoreSearchList=new ArrayList<EccoreSearchResultDTO>();
		
		//运单号查询，保存订单信息
		Map<String, Order> orderInfoMap=new HashMap<String, Order>();
		// 非运单号查询
		if (!StringUtil.isMailNo(logisticsIds)) {

			if (currentUser.BUYER.equals(currentUser.getUserAuthority())) {
				if (bindUserId != null && bindUserId != 0) {
					User bindUser = userService.getUserById(bindUserId);
					if (bindUser != null) {
						bindedId.add(bindUser.getTaobaoEncodeKey());
					}
				}
			}
			if (StringUtil.isMobile(logisticsIds)
					|| StringUtil.isPhone(logisticsIds)) {
				buyerPhone = logisticsIds;
			} else {
				buyerName = logisticsIds;
			}
			/**
			 * 默认读取一个月的数据
			 * */
			startTime = monthArithmetic(new Date(),1);
			endTime = monthArithmetic(new Date(),0);
			pagination = buildSearchPage(startTime, endTime, buyerName,
					buyerPhone, 1, pagination, bindedId, currentUser);
			if (pagination == null) {
				pagination = new Pagination(currentPage, 20);
				pagination.setTotalRecords(0);
			}
			
			try {
				// slor改造 根据时间和卖家电话 姓名查/找一律走此方法 不再调用queryByTime,queryByBuyer查询运单号
				if (pagination != null) {
					eccoreSearchService.searchWayBillData(ConfigUtilSingle
							.getInstance().getSolrEccoreUrl(), pagination);
					if (pagination.getRecords().size() > 0) {
						 eccoreSearchList = pagination
								.getRecords(); 
						 
						logisticsIdArray = new String[eccoreSearchList.size()];
						for (int i = 0; i < eccoreSearchList.size(); i++) {
							EccoreSearchResultDTO dto = eccoreSearchList.get(i);
							logisticsIdArray[i] = dto.getMailNo();
						}
						searchOderSteps(logisticsIdArray, currentUser);
					}
					totalCount = pagination.getTotalRecords();
					if (totalCount == 0) {
						pagination.setTotalRecords(0);
					}
				} else
					pagination.setTotalRecords(0);

				// 对订单结果进行排序，按照发货时间倒序排列
				if (CollectionUtils.isNotEmpty(orderList)
						&& orderList.size() > 1) {

					SortList<QueryOrder> sortList = new SortList<QueryOrder>();

					for (QueryOrder order : orderList) {
						if (CollectionUtils.isNotEmpty(order.getSteps())) {

							String acceptTime = order.getSteps().get(0)
									.getAcceptTime();
							order.setAcceptTime(acceptTime);
						} else {
							order.setAcceptTime("");
						}
					}
					// 定义排序方法，进行排序
					sortList.Sort(orderList, "getAcceptTime", "desc");
				}
			} catch (Exception e) {
				logger.error("按条件查询物流跟踪信息失败", e);
			}
		} else {
			// 运单号查询
			try {

				User bindUser = null;
				if (currentUser.BUYER.equals(currentUser.getUserAuthority())) {
					if (bindUserId != null && bindUserId != 0) {
						bindUser = userService.getUserById(bindUserId);
					}
				}
				if (!StringUtil.isBlank(logisticsIds)) {
					logisticsIds = logisticsIds.trim();
					if (isCheck!=null&&isCheck == 1) {
						// 自动生成连续的运单号
						logisticsIds = createLogisticsIds(logisticsIds, mailNum);
					}
					if(logisticsIds.contains("/")){
						logisticsIdArray = logisticsIds.split("/");
					}else if(logisticsIds.contains(" ")){
						logisticsIdArray = logisticsIds.split(" ");
					}else if(logisticsIds.contains("\r\n")){
						logisticsIdArray = logisticsIds.split("\r\n");
					}
					else{
						logisticsIdArray = logisticsIds.split("/");
					}
					
					if (bindUser != null) {
						StringBuffer queryString = new StringBuffer("");
						for (int i = 0; i < logisticsIdArray.length; i++) {
							String mailNo = logisticsIdArray[i];
							Order order = orderService.getOrderByMailNo(mailNo);
							if (order!=null) {
							    orderInfoMap.put(mailNo,order);
	                            if ((bindUser.getTaobaoEncodeKey()).equals(order
	                                    .getCustomerId())) {
	                                queryString.append(mailNo).append(",");
	                            } 
                            }
							
						}
						logisticsIdArray = queryString.toString().split(",");
						// bindUser = null;
					}
					/**
					 * 按每页显示数目查询接口订单
					 */
					String[] logistics = substrArray(logisticsIdArray,
							pagination.getStartIndex(), 20);
					searchOderSteps(logistics, currentUser);
					if (orderList != null && orderList.size() > 0)
						totalCount = logisticsIdArray.length;
					totalCount = orderList != null ? orderList.size() : 0;
					// 设置总数
					if (orderList != null && orderList.size() > 0)
						pagination.setTotalRecords(logisticsIdArray.length);
					else
						pagination.setTotalRecords(0);
				} else
					pagination.setTotalRecords(0);
			} catch (Exception e) {
				logger.error("按运单号查询物流跟踪信息失败", e);
			}
		}
		// 如果是卖家角色需要查找店铺名称
		if (currentUser.BUYER.equals(currentUser.getUserAuthority())
				&& orderList != null && orderList.size() > 0) {
			setShopName(orderList,WayBillActionHelper.ListEccoreSearchResultDTOToMap(eccoreSearchList));
		}

		if (User.BUYER.equals(currentUser.getUserAuthority())) {
			bindedUserList = new ArrayList<User>();
			for (String cusId : Resource.getBindedCustomerIdList(currentUser)) {
				bindedUserList.add(userService.getUserByCustomerId(cusId));
			}
		} else if (User.SITE.equals(currentUser.getUserAuthority()))
			vipThreadList = super.getZhiKeUser(currentUser);
		else if (User.LARGE_SELLER.equals(currentUser.getUserAuthority()))
			vipList = userService.pingTaiSelect(currentUser, 0);
		else
			vipList = super.getClientUser(currentUser);

		if (flag == 2 || flag == 3) {
			logisticsIds = "";
		}
		
		//封装重量和联系人信息
		setWeightAndBuyerInfo(orderList,WayBillActionHelper.ListEccoreSearchResultDTOToMap(eccoreSearchList),orderInfoMap);
		//清楚引用，尽早回收
		eccoreSearchList=null;
		setAttention(orderList, currentUser);
		return "list";
	}
	
	/**
	 * 设置是否需要关注或者是否已关注（卖家只关注属于自己或者关联店铺的订单，网点只关注属于自己客户的订单）
	 * @param orderList
	 * @param curUser
	 */
	private void setAttention(List<QueryOrder> orderList, User curUser) {
		if(curUser.getUserType().equals("1") || curUser.getUserType().equals("11") || curUser.getUserType().equals("12") || curUser.getUserType().equals("13")) {
			List<String> bindedId = Resource.getBindedCustomerIdList(curUser);
			if(orderList!=null) {
				for(QueryOrder qo : orderList) {
					List<AttentionMail> alist = attenService.searchByMailNoAndCustomerId(qo.getMailNo(), curUser.getTaobaoEncodeKey());
					if(!alist.isEmpty()) {
						qo.setIsAttention(1);
					} else {
						List<String> mailNo = new ArrayList<String>();
						mailNo.add(qo.getMailNo());
						List<Order> list = orderService.getOrderbyMailNoCustomerId(mailNo, bindedId);
						if(!list.isEmpty()) {
							qo.setIsAttention(2);
						} else {
							qo.setIsAttention(0);
						}
					}
				}
			}
		} else if(curUser.getUserType().equals("2") || curUser.getUserType().equals("21") || curUser.getUserType().equals("22") || curUser.getUserType().equals("23")) {
			if(orderList!=null) {
				List<User> userList = userService.searchUsersBySiteAndUserType(curUser.getSite(), "1");
				if(!userList.isEmpty()) {
					List<String> bindedId = new ArrayList<String>();
					for(User u : userList) {
						if(StringUtils.isNotEmpty(u.getTaobaoEncodeKey())) {
							bindedId.add(u.getTaobaoEncodeKey());
						}
					}
					if(bindedId.size() > 0) {
						for(QueryOrder qo : orderList) {
							String userId = curUser.getId().toString();
							if(!curUser.getUserType().equals("2"))
								userId = curUser.getParentId().toString();
							List<AttentionMail> alist = attenService.searchByMailNoAndCustomerId(qo.getMailNo(), userId);
							if(!alist.isEmpty()) {
								qo.setIsAttention(1);
							} else {
								List<String> mailNo = new ArrayList<String>();
								mailNo.add(qo.getMailNo());
								List<Order> list = orderService.getOrderbyMailNoCustomerId(mailNo, bindedId);
								if(!list.isEmpty()) {
									qo.setIsAttention(2);
								} else {
									qo.setIsAttention(0);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 封装重量和联系人信息 ，如果使用电话或姓名查询 map的值不为空，如果使用运单号查询，map的值是空的，所有2中逻辑
	 * @param orderList
	 * @param map
	 */
	private void setWeightAndBuyerInfo(List<QueryOrder> orderList,Map<String, EccoreSearchResultDTO> map,Map<String, Order> orderInfoMap){
	    
	    if (orderList==null||orderList.isEmpty()) {
            return;
        }
	    EccoreSearchResultDTO eccoreSearchResultDTO=new EccoreSearchResultDTO();
	    
	    User cuser = this.readCookieUser();
	    if(cuser.getParentId() != null){
	    	cuser = userService.getUserById(cuser.getParentId());
		}
	    List<String> bindedId = new ArrayList<String>();
	    if("4".equals(cuser.getUserType()) || "41".equals(cuser.getUserType())){//平台，平台客服登陆进来，获得下面所有业务账号的taobaokey
	    	List<User> vipList = userService.pingTaiSelect(cuser,0);
	    	for(User u : vipList){
	    		bindedId.add(u.getTaobaoEncodeKey());
	    	}
	    }else{
	    	bindedId = this.getCustomerIds(cuser);
	    }
	    
	    
	    if(map!=null&&!map.isEmpty()){
	        for(QueryOrder queryOrder:orderList){
	                eccoreSearchResultDTO=map.get(queryOrder.getMailNo());
                    TraderInfo buyerInfo=new TraderInfo();
                    queryOrder.setWeight(eccoreSearchResultDTO.getWeight()==null? "0":eccoreSearchResultDTO.getWeight().toString());
                    buyerInfo.setName(eccoreSearchResultDTO.getName());
                    buyerInfo.setPhone(eccoreSearchResultDTO.getPhone());
                    queryOrder.setTraderInfo(buyerInfo);   
                    if("4".equals(cuser.getUserType()) || "41".equals(cuser.getUserType()) || "2".equals(cuser.getUserType()) || "21".equals(cuser.getUserType()) || "22".equals(cuser.getUserType()) || "23".equals(cuser.getUserType())){
                    	if(!bindedId.contains(eccoreSearchResultDTO.getCustomerId())){
                    		queryOrder.setTraderInfo(null); 
                    	}
                    }
                    if("1".equals(cuser.getUserType()) || "11".equals(cuser.getUserType()) || "12".equals(cuser.getUserType()) || "13".equals(cuser.getUserType())){
                    	if(!bindedId.contains(eccoreSearchResultDTO.getCustomerId())){
                    		queryOrder.setTraderInfo(null); 
                    	}
                    }
                }
	    }else if(orderInfoMap!=null&&!orderInfoMap.isEmpty()){
	        for(QueryOrder queryOrder:orderList){
                order = orderInfoMap.get(queryOrder.getMailNo());
                if(order.getId() != null){
                    //订单号 
                    int  orderId = order.getId();
                    //重量
                    String weight = "0.0";
                        if(order.getWeight() != null){
                            weight = order.getWeight().toString();
                        }
                    TraderInfo buyerInfo=new TraderInfo();
                    buyerInfo = traderInfoService.getTraderInfoByIdAndByTraderType(orderId, 1);
                    queryOrder.setWeight(weight);
                    queryOrder.setTraderInfo(buyerInfo);   
                    if("4".equals(cuser.getUserType()) || "41".equals(cuser.getUserType()) || "2".equals(cuser.getUserType()) || "21".equals(cuser.getUserType()) || "22".equals(cuser.getUserType()) || "23".equals(cuser.getUserType())){
                    	if(!bindedId.contains(order.getCustomerId())){
                    		queryOrder.setTraderInfo(null); 
                    	}
                    } 
                    if("1".equals(cuser.getUserType()) || "11".equals(cuser.getUserType()) || "12".equals(cuser.getUserType()) || "13".equals(cuser.getUserType())){
                    	if(!bindedId.contains(order.getCustomerId())){
                    		queryOrder.setTraderInfo(null); 
                    	}
                    }
                }
            }
	    }
	    else {
	            for(QueryOrder queryOrder:orderList){
                order = orderService.getOrderByMailNo(queryOrder.getMailNo());
                if(order.getId() != null){
                    //订单号 
                    int  orderId = order.getId();
                    //重量
                    String weight = "0.0";
                        if(order.getWeight() != null){
                            weight = order.getWeight().toString();
                        }
                    TraderInfo buyerInfo=new TraderInfo();
                    buyerInfo = traderInfoService.getTraderInfoByIdAndByTraderType(orderId, 1);
                    queryOrder.setWeight(weight);
                    queryOrder.setTraderInfo(buyerInfo);   
                    if("4".equals(cuser.getUserType()) || "41".equals(cuser.getUserType()) || "2".equals(cuser.getUserType()) || "21".equals(cuser.getUserType()) || "22".equals(cuser.getUserType()) || "23".equals(cuser.getUserType())){
                    	if(!bindedId.contains(order.getCustomerId())){
                    		queryOrder.setTraderInfo(null); 
                    	}
                    }
                    if("1".equals(cuser.getUserType()) || "11".equals(cuser.getUserType()) || "12".equals(cuser.getUserType()) || "13".equals(cuser.getUserType())){
                    	if(!bindedId.contains(order.getCustomerId())){
                    		queryOrder.setTraderInfo(null); 
                    	}
                    }
                }
            }
        }
            
       
	}
	/**
	 * 根据当前用户相关的customerId 卖家的从缓存里获取关联账号
	 * 
	 * @param user
	 * @return
	 */
	private List<String> getCustomerIds(User user) {
		List<String> customerIds = new ArrayList<String>();
		if (User.BUYER.equals(user.getUserAuthority())) {
			if (StringUtil.isBlank(vipId))
				// 卖家关联账号
				customerIds = Resource.getBindedCustomerIdList(user);
			else
				customerIds.add(vipId);
		} else if (User.SITE.equals(user.getUserAuthority())) {
			List<User> userList = new ArrayList<User>();
			// 如果是按手机号码查询的话不需要限制只看到该网点的卖家的订单，按买家姓名查询时需要客户与网点绑定
			if (StringUtils.isNotEmpty(logisticsIds)) {
				List<UserThread> threadList = super.getZhiKeUser(user);
				if (threadList != null && threadList.size() > 0) {
					for (UserThread ut : threadList) {
						List<User> uList = userService
								.searchUsersByCodeTypeState(ut.getUserCode(),
										"1", "1");
						if (uList != null && uList.size() > 0)
							userList.addAll(uList);
					}
				}
			}
			for (User u : userList) {
				if (u.getTaobaoEncodeKey() != null
						&& !("").equals(u.getTaobaoEncodeKey()))
					customerIds.add(u.getTaobaoEncodeKey());
			}

		} else if (User.LARGE_SELLER.equals(user.getUserAuthority())) {
			// 平台用户
			if ("all".equals(vipId)) {
				List<User> list = userService.pingTaiSelect(user, 0);
				for (User u : list) {
					customerIds.add(u.getTaobaoEncodeKey());
				}
			} else
				customerIds.add(vipId);
		}
		return customerIds;
	}

	// 设置店铺名称
	private void setShopName(List<QueryOrder> list,Map<String, EccoreSearchResultDTO> map) {
		//不属于当前用户关联且个性化配置中的用户不需要看到店铺名称
		User curUser = super.readCookieUser();
		List<String> customerIdList = Resource.getBindedCustomerIdList(curUser);
		String customerId=null;
	    String mailNo=null;
	    EccoreSearchResultDTO eccoreSearchResultDTO=new EccoreSearchResultDTO();
	    if (map!=null&&!map.isEmpty()) {
            for (QueryOrder qo : list) {
                mailNo=qo.getMailNo();
                eccoreSearchResultDTO=map.get(mailNo);
                customerId=eccoreSearchResultDTO.getCustomerId();
                User user = Resource.getUserByCustomerId(customerId);
                if (user != null) {
                	if(user.getId()!=null && customerIdList.contains(customerId))
                		qo.setShopName(user.getShopName());
                	else
                		qo.setShopName("未知");
                } else {
            		qo.setShopName("未知");
                }
            }
        }else {
            for (QueryOrder qo : list) {
                mailNo=qo.getMailNo();
                Order order = orderService.getOrderByMailNo(qo.getMailNo());
                if (order != null) {
                	if(StringUtils.isNotEmpty(order.getCustomerId())) {
                		User user = Resource.getUserByCustomerId(order.getCustomerId());
                        if (user != null) {
                        	if(user.getId()!=null && customerIdList.contains(order.getCustomerId()))
                        		qo.setShopName(user.getShopName());
                        	else
                        		qo.setShopName("未知");
                        } else {
                    		qo.setShopName("未知");
                        }
                	}
                }
            }
        }
		
	}

	// 倒序走件信息,并且标记问题件
	private List<QueryOrder> reverseSteps(List<QueryOrder> list,
			User currentUser) {
		List<QueryOrder> qoList = new ArrayList<QueryOrder>();
		for (QueryOrder qoObj : list) {
			/**
			 * 2012-05-16按需求调整为和圆通官网一样，按时间顺序排列。
			 */
			// List<StepInfo> steps = qoObj.getSteps();
			// if (steps != null && steps.size() > 0) {
			// int size = steps.size();
			// List<StepInfo> siList = new ArrayList<StepInfo>();
			// for (int j = size - 1; j >= 0; j--) {
			// StepInfo si = steps.get(j);
			// siList.add(si);
			// }
			// qoObj.setSteps(siList);
			// }
			// List<Questionnaire> ques =
			// questionnaireService.getQestionnaireByMailNo(qoObj.getMailNo());
			String ques =null;
			if (currentUser.getUserType().equals("2")
					|| currentUser.getUserType().equals("21")
					|| currentUser.getUserType().equals("22")
					|| currentUser.getUserType().equals("23"))
				ques = questionnaireService.siteWayQuestionnaire(currentUser, qoObj.getMailNo());
			else if (currentUser.getUserType().equals("1")
					|| currentUser.getUserType().equals("11")
					|| currentUser.getUserType().equals("12")
					|| currentUser.getUserType().equals("13"))
				ques = questionnaireService.vipWayQuestionnaire(currentUser,
						qoObj.getMailNo());
			if (ques != null) {
				qoObj.setIsQuestion(true);
			}
			qoList.add(qoObj);
		}
		return qoList;
	}

	// 根据运单号查询走件信息
	private void searchOderSteps(String[] mailNo, User currentUser) {
		TaoBaoOrderAction op = new TaoBaoOrderAction();
		List<QueryOrder> sortList;
		List<QueryOrder> totalList = new ArrayList<QueryOrder>();
		
		try {
			//按照用户输入的顺序进行排序
			sortList = op.ordersSearch(mailNo);
			for(String str:mailNo){
				for(QueryOrder order:sortList){
					if(order.getMailNo().equals(str)){
						totalList.add(order);
					}	
				}
		  }
			if (totalList != null) {
				//
				orderList = reverseSteps(totalList, currentUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 按时间查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * 
	 * @return
	 * @throws Exception
	 */
	// public String queryByTime() throws Exception {
	// boolean ff = false;
	// if (vipId == null)
	// ff = true;
	// StringBuffer resultParameters = new StringBuffer("");
	// // User currentUser = (User)session.get(Constants.SESSION_USER);
	// User currentUser = super.readCookieUser();
	// List bindedId = null;
	// // if(vipId==null)bindedId=currentUser.getBindedCustomerIdList();
	// if (vipId == null)
	// bindedId = Resource.getBindedCustomerIdList(currentUser);
	// List<String> result = new ArrayList<String>();
	// if (currentUser.getUserType().equals("1")) {// 如果是VIP用户情况下,只获取该用户的订单的运单号
	// if (vipId == null)
	// vipId = currentUser.getId();
	// } else if ("11".equals(currentUser.getUserType())
	// || "13".equals(currentUser.getUserType())) {
	// vipId = currentUser.getParentId();
	// }
	// if (currentUser.getUserType().equals("2")
	// || "21".equals(currentUser.getUserType())
	// || "22".equals(currentUser.getUserType())
	// || "23".equals(currentUser.getUserType())) {
	// Date ucUpdateTime = queryUserCodeUpdateTime(userCodeForBuyer);
	// result = orderService.relatedOrderMailNoListByTime(startTime,
	// endTime, userCodeForBuyer, ucUpdateTime);// FIXME
	// // 是否需要添加关联账号功能
	// } else {
	// result = orderService.getOrderMailNoListByTime(startTime, endTime,
	// vipId, bindedId);
	// }
	// if (result != null && result.size() > 0) {
	// for (String str : result) {
	// if (str != null && str.length() == 10)
	// resultParameters.append(str).append("/");
	// }
	// }
	// logisticsIds = resultParameters.toString();
	// if (ff)
	// vipId = null;
	// return list();
	// }

	// /**
	// * 按买家信息查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	// *
	// * @return
	// */
	// public String queryByBuyer() {
	// StringBuffer resultParameters = new StringBuffer("");
	// // User curUser = (User)session.get(Constants.SESSION_USER);
	// User curUser = super.readCookieUser();
	// List bindedId = null;
	// if (vipId == null)
	// bindedId = Resource.getBindedCustomerIdList(curUser);
	// List<String> result = new ArrayList<String>();
	// if (curUser.getUserType() != null && curUser.getUserType().equals("1")) {
	// if (vipId == null)
	// vipId = curUser.getId();
	// }
	// if ("11".equals(curUser.getUserType())
	// || "13".equals(curUser.getUserType())) {
	// vipId = curUser.getParentId();
	// }
	// if (curUser.getUserType() != null
	// && (curUser.getUserType().equals("2")
	// || "21".equals(curUser.getUserType()) || "23"
	// .equals(curUser.getUserType()))) {
	// Date ucUpdateTime = queryUserCodeUpdateTime(userCodeForBuyer);
	// result = orderService.relatedOrderMailNoListByBuyerInfo(startTime,
	// endTime, userCodeForBuyer, buyerName, buyerPhone,
	// ucUpdateTime);// FIXME 是否需要添加关联账号功能
	// } else {
	// result = orderService.getOrderMailNoListByBuyerInfo(startTime,
	// endTime, vipId, buyerName, buyerPhone, bindedId);
	// }
	// if (result != null && result.size() > 0) {
	// for (String str : result) {
	// if (str != null && str.length() == 10) {
	// resultParameters.append(str).append("/");
	// }
	// }
	// }
	// logisticsIds = resultParameters.toString();
	// return list();
	// }

	private int homeFlag;

	public int getHomeFlag() {
		return homeFlag;
	}

	public void setHomeFlag(int homeFlag) {
		this.homeFlag = homeFlag;
	}

	public String homeQuery() {
		// startTime = DateUtil.dateArithmetic(new Date(), 6);
		// endTime = DateUtil.dateArithmetic(new Date(), 0);
		if (homeFlag == 1)
			flag = 3;
		else
			flag = 1;
		return list();
	}

	public String paginationList() {
		// User currentUser = (User)session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		if (tempList != null)
			pagination.setTotalRecords(tempList.size());
		else
			pagination.setTotalRecords(0);
		orderList = this.getPagination(tempList, pagination);
		vipList = userService.searchUsersBySiteAndUserType(
				currentUser.getSite(), "1");
		if (userService.getUserListByUserType("4") != null) {
			// 获取大商家.如果该大商家的网点编码为当前网点，则加入该用户
			List<User> list = userService.getUserListByUserType("4");
			for (User user : list) {
				if (user.getSite() != null
						&& (user.getSite()).equals(currentUser.getSite()))
					vipList.add(user);
			}
		}
		return "list";
	}

	private List<QueryOrder> getPagination(List<QueryOrder> totalList,
			Pagination pagination) {
		List<QueryOrder> result = new ArrayList<QueryOrder>();
		if (totalList != null) {
			if (pagination.getCurrentPage() == pagination.getTotalPages()) {
				for (int i = pagination.getStartIndex(); i < totalList.size(); i++) {
					result.add(totalList.get(i));
				}
			} else {
				for (int i = pagination.getStartIndex(); i < pagination
						.getStartIndex() + pagination.getPageNum(); i++) {
					result.add(totalList.get(i));
				}
			}
		}
		logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
		return result;
	}

	/**
	 * 根据传入的参数logisticsIds和mailNum创建运单号以“/”分隔的字符串
	 * 
	 * @param logisticsIds
	 *            单运单号
	 * @param mailNum
	 *            连续查询的运单数目
	 * @return
	 */
	private String createLogisticsIds(String logisticsIds, Integer mailNum) {
		String result = logisticsIds.substring(0, 10) + "/";
		String queryStr = logisticsIds.substring(0, 10);
		String firstStr = logisticsIds.substring(0, 1);
		if (!Character.isDigit(queryStr.charAt(0))) {
			// 如果第一个是字母
			queryStr = logisticsIds.substring(1, 10);
			Long i = Long.parseLong(queryStr);
			for (int j = 1; j < mailNum; j++) {
				result += firstStr + createStr(i + j, 9) + "/";
			}
		} else {
			// 如果第一个是数字
			Long i = Long.parseLong(queryStr);
			for (int j = 1; j < mailNum; j++) {
				result += createStr(i + j, 10) + "/";
			}
		}

		return result;
	}

	/**
	 * 根据传入的整形参数a，将a转化为b位字符串形式的数字，如果不足b位的在前面补0
	 * 
	 * @param a
	 *            需要转换的数字
	 * @param b
	 *            字符串的长度
	 * @return
	 */
	private static String createStr(Long a, Integer b) {
		String s = a.toString();
		if (s.length() < b) {
			int i = s.length();
			String temp = "";
			for (int j = 0; j < b - i; j++) {
				temp += "0";
			}
			s = temp + s;
		}
		return s;
	}

	/**
	 * 截取字符串数组
	 * 
	 * @param sourceArray
	 * @param startIndex
	 * @param length
	 * @return
	 */
	private static String[] substrArray(String[] sourceArray, int startIndex,
			int length) {
		List<String> result = new ArrayList<String>();
		for (int i = startIndex; i < startIndex + length; i++) {
			if (i < sourceArray.length) {
				result.add(sourceArray[i]);
			}
		}
		String[] str = new String[result.size()];
		for (int j = 0; j < result.size(); j++) {
			str[j] = result.get(j);
		}
		return str;
	}
	/**
	 * 日期处理的方法
	 * */
	private static String monthArithmetic(Date originalDate, int countMonth) {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 在当前日期上加-countDay天数
		cal.add(Calendar.MONTH, -countMonth);
		return sdm.format(cal.getTime());
	}

	private static List mailNolist(String[] sourceArray) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < sourceArray.length; i++) {
			result.add(sourceArray[i]);
			// System.out.println("===sourceArray[i]===="+sourceArray[i]);
		}
		return result;
	}

	private List<String> getCustomerIdByUserCode(String userCode) {
		List<String> list = new ArrayList<String>();
		List<User> userList = userService.searchUsersByCodeTypeState(userCode,
				"1", "1");// 查找同一用户编码下已激活的买家
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null
						&& !(user.getTaobaoEncodeKey().equals("")))
					list.add(user.getTaobaoEncodeKey());
				// System.out.println("=userList==sourceArray[i]===="+user.getTaobaoEncodeKey());
			}
		} else {
			return null;
		}
		if (list.size() > 0)
			return list;
		else
			return null;
	}

	/**
	 * 如果当前用户是网点，就返回客户编码修改的时间戳。 否则返回 null
	 * 
	 * @param userCode
	 *            选择用户的的用户编码
	 * @return
	 */
	private Date queryUserCodeUpdateTime(String userCode) {
		if (!StringUtil.isBlank(userCode)) {
			UserThread uh = new UserThread();
			uh.setUserCode(userCode);
			List<UserThread> uhList = userThreadService.searchUsersByCode(uh);

			if (uhList.size() > 0)
				return uhList.get(0).getUserCodeUpteTime();
		}
		return null;
	}

	/**
	 * solr搜索条件
	 * 
	 * @param startTime
	 * @param endTime
	 * @param buyerName
	 * @param buyerPhone
	 * @param orderBy
	 * @param pagination
	 * @param bindedId
	 * @return
	 */
	private Pagination<EccoreSearchResultDTO> buildSearchPage(String startTime,
			String endTime, String buyerName, String buyerPhone,
			Integer orderBy, Pagination<EccoreSearchResultDTO> pagination,
			List<String> bindedId, User curUser) {
		Map<String, String> map = new HashMap<String, String>();
		/**
		 * 网点在按手机号码查询时不需要关联bindedId.卖家在按姓名或者手机号查询时需要关联bindedId
		 */
		if (User.SITE.equals(curUser.getUserAuthority())) {
			if (!StringUtils.isEmpty(buyerPhone)) {
			}// 按电话号码查询时不需要bindedId
			else {
				if (bindedId == null || bindedId.isEmpty())
					return null;
				else {
					StringBuffer customerIDs = new StringBuffer();
					for (int i = 0; i < bindedId.size(); i++) {
						if (i != 0)
							customerIDs.append(",");
						customerIDs.append(bindedId.get(i));
					}
					map.put("customerIDs", customerIDs.toString());
				}
			}
		} else if (User.ADMIN.equals(curUser.getUserAuthority())) {
		} else {
			if (bindedId == null || bindedId.isEmpty())
				return null;
			else {
				StringBuffer customerIDs = new StringBuffer();
				for (int i = 0; i < bindedId.size(); i++) {
					if (i != 0)
						customerIDs.append(",");
					customerIDs.append(bindedId.get(i));
				}
				map.put("customerIDs", customerIDs.toString());
			}
		}
		if (StringUtils.isNotBlank(logisticsIds)) {
			map.put("all", logisticsIds);
		}
		if (StringUtils.isNotEmpty(startTime)
				&& StringUtils.isNotEmpty(endTime)) {
			map.put("startDate", startTime);
			map.put("endDate", endTime);
		}
		if (!StringUtils.isEmpty(buyerName))
			map.put("name", buyerName);
		if (!StringUtils.isEmpty(buyerPhone))
			map.put("phone", buyerPhone);
		if (orderBy == 1) {
			map.put("sortType", "asc");
		} else
			map.put("sortType", "desc");
		pagination.setParams(map);
		return pagination;
	}

	private List<User> getActiveUserList(List<User> vipList) {
		List<User> unActiveUser = new ArrayList<User>();
		for (User u : vipList) {
			if (u.getUserState() != "1")
				unActiveUser.add(u);
		}
		if (unActiveUser.size() > 0)
			vipList.removeAll(unActiveUser);
		return vipList;
	}

	/**
	 * 查询各种类型的分仓用户
	 */
	private void buildFCUser(User currentUser) {
		List<User> fcUserList = userService.pingTaiSelect(currentUser, 1); // 取分仓用户
		vipList = new ArrayList<User>();
		for (User fcUser : fcUserList) {
			if ("1".equals((fcUser.getUserState()))) {
				vipList.add(fcUser);
			}
		}
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

	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
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

	public UnlikeFreight getUnlikeFreight() {
		return unlikeFreight;
	}

	public void setUnlikeFreight(UnlikeFreight unlikeFreight) {
		this.unlikeFreight = unlikeFreight;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<UnlikeFreight> getUnlikeFreightOrderList() {
		return unlikeFreightOrderList;
	}

	public void setUnlikeFreightOrderList(
			List<UnlikeFreight> unlikeFreightOrderList) {
		this.unlikeFreightOrderList = unlikeFreightOrderList;
	}

	public List<Order> getUfOrderList() {
		return ufOrderList;
	}

	public void setUfOrderList(List<Order> ufOrderList) {
		this.ufOrderList = ufOrderList;
	}

	public List<User> getBindedUserList() {
		return bindedUserList;
	}

	public void setBindedUserList(List<User> bindedUserList) {
		this.bindedUserList = bindedUserList;
	}

	public OrderExpress getOrderExpress() {
		return orderExpress;
	}

	public void setOrderExpress(OrderExpress orderExpress) {
		this.orderExpress = orderExpress;
	}

	public String getFromList() {
		return fromList;
	}

	public void setFromList(String fromList) {
		this.fromList = fromList;
	}

	public String getOrderFlag() {
		return orderFlag;
	}

	public void setOrderFlag(String orderFlag) {
		this.orderFlag = orderFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getBindUserId() {
		return bindUserId;
	}

	public void setBindUserId(Integer bindUserId) {
		this.bindUserId = bindUserId;
	}

	public String getManagerTab() {
		return managerTab;
	}

	public void setManagerTab(String managerTab) {
		this.managerTab = managerTab;
	}
	
}
