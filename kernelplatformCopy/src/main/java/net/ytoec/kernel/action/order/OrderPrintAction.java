package net.ytoec.kernel.action.order;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.dataobject.OrderPrint;
import net.ytoec.kernel.dataobject.PrintTemp;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.OrderExpressService;
import net.ytoec.kernel.service.OrderPrintService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 面单打印中订单action类
 * 
 * @author wusha
 * @date 2012-05-25
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class OrderPrintAction extends AbstractActionSupport {

	/**
	 * 记录日志
	 */
	private Logger logger = LoggerFactory.getLogger(OrderPrintAction.class);

	@Inject
	private OrderPrintService<OrderPrint> orderPrintService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private UserService<User> userService;

	@Inject
	private OrderService<Order> orderService;

	@Inject
	private OrderExpressService<OrderExpress> orderExpService;

	/**
	 * 批量打印列表
	 */
	private List<OrderPrint> batchOrderPrintList = new ArrayList<OrderPrint>();

	/**
	 * 点按钮打印快递单后的列表
	 */
	private List<OrderPrint> clickBatchOrderPrintList = new ArrayList<OrderPrint>();

	/**
	 * 点按钮打印发货单后的列表
	 */
	private List<OrderPrint> clickBatchOrderPrintSendList = new ArrayList<OrderPrint>();

	/**
	 * 代发货列表
	 */
	List<OrderPrint> undeliverOrderPrintList = new ArrayList<OrderPrint>();

	/**
	 * 已发货列表
	 */
	List<OrderPrint> deliveredOrderPrintList = new ArrayList<OrderPrint>();

	/**
	 * 网点登录时可以帮打印的卖家列表
	 */
	List<UserThread> vipThreadList = new ArrayList<UserThread>();

	/**
	 * 保存淘宝参数
	 */
	private Map<String, Object> sessionMap = new HashMap<String, Object>();

	/**
	 * 标识选中今天，昨天，还是近三天 批量发货
	 */
	private String dateSign = "0";

	/**
	 * 标识选中今天，昨天，还是近三天 已发货
	 */
	private String dateSignDelivered = "0";

	/**
	 * 标识选中今天，昨天，还是近三天 代发货
	 */
	private String dateSignUnelivered = "0";

	/**
	 * 标识当前tab页
	 */
	private String curTab;

	/**
	 * 自动合并和取消合并订单的ID
	 */
	private String joinOrClearOrderIdsAuto = "";

	/**
	 * 手动合并和取消合并订单的ID
	 */
	private String joinOrClearOrderIdsMaunal = "";

	/**
	 * 订单类型
	 */
	private String orderType = "0";

	/**
	 * 根据下单时间查找的起始时间 批量打印界面
	 */
	private String startDate = DateUtil.format(new Date(), "yyyy-MM-dd");

	/**
	 * 根据下单时间查找的结束时间 批量打印
	 */
	private String endDate = DateUtil.format(new Date(), "yyyy-MM-dd");

	/**
	 * 根据下单时间查找的起始时间 //代发货界面
	 */
	private String startDateUndeliver = DateUtil.format(new Date(),
			"yyyy-MM-dd");

	/**
	 * 根据下单时间查找的结束时间 // 代发货界面
	 */
	private String endDateUndeliver = DateUtil.format(new Date(), "yyyy-MM-dd");

	/**
	 * 根据下单时间查找的起始时间 //已发货界面
	 */
	private String startDateDelivered = DateUtil.format(new Date(),
			"yyyy-MM-dd");

	/**
	 * 根据下单时间查找的结束时间 // 已发货界面
	 */
	private String endDateDelivered = DateUtil.format(new Date(), "yyyy-MM-dd");

	/**
	 * 打印预览中点击打印快递单时保存订单ids
	 */
	private String markPrint = "";

	/**
	 * 打印预览中点击打印发货单时保存订单ids
	 */
	private String markPrintSend = "";

	/**
	 * 运单号或者订单号查询
	 */
	private String mailNoOrOrderNo;

	/**
	 * 代发货页面中运单号或者订单号查询
	 */
	private String mailNoOrOrderNoUndeliver;

	/**
	 * 已发货页面中运单号或者订单号查询
	 */
	private String mailNoOrOrderNoDelivered;

	/**
	 * 批量发货、代发货页面中保存的订单号和对应的订单ID
	 */
	private String saveShipNum = "";

	/**
	 * 当前页数
	 */
	private Integer currentPage = 1;

	/**
	 * 每页显示多少条记录
	 */
	private Integer numOfPage = 50;

	private String json;

	private JSONObject jsonObject;

	private String isPrintFlag;

	/**
	 * 网点选中的直客编码批量打印
	 */
	private String customerCode = "";

	/**
	 * 网点选中的直客编码 代发货
	 */
	private String customerCode2 = "";
	
	/**
	 * 网点选中的直客编码已发货
	 */
	private String customerCode3 = "";
	
	/**
	 * 模板ID
	 */
	private Integer OrderExpressId;

	private String orderPrintIds;

	private Integer UserThreadId;

	/**
	 * 允许打印是否勾选
	 */
	private String checked;
	/**
	 * 面单模版string
	 */
	private String moduleString = "";

	/**
	 * 批量打印列表排序
	 */
	private String orderByCol = "createDateDown";

	/**
	 * 代发货列表排序
	 */
	private String undeliverOrderByCol = "createDateDown";

	/**
	 * 已发货列表排序
	 */
	private String deliveredOrderByCol = "createDateDown";

	/**
	 * 代发货中不想看到它功能所选中的orderId
	 */
	private String delOpIds = "";

	/**
	 * 批量打印封装的分页对象
	 */
	private Pagination<OrderPrint> paginationOrderPrint;

	/**
	 * 代发货封装的分页对象
	 */
	private Pagination<OrderPrint> undeliverpaginationOrderPrint;

	/**
	 * 已发货封装的分页对象
	 */
	private Pagination<OrderPrint> deliveredpaginationOrderPrint;

	private User user;

	/**
	 * 批量打印分页List
	 */
	private List<String> pageList = new ArrayList<String>();

	/**
	 * 代发货打印分页List
	 */
	private List<String> undeliverpageList = new ArrayList<String>();

	/**
	 * 已发货打印分页List
	 */
	private List<String> deliveredpageList = new ArrayList<String>();

	private Integer flag;//打印指南标示
	
	/**
	 * 记录淘宝发货是否成功
	 */
	private String isSuccessToTaobao = "";
	
	private Boolean bindBranch;//绑定客户编号
	
	private String showMessage;
	
	/**
	 * 打印模版下拉列表
	 */
	private String moduleExpressString = "";
	
	/**
	 * 批量 或者单个保存运单号
	 */
	public void saveMailNo() {
		if (!"".equals(saveShipNum)) {
			Map<String, Object> params;
			String[] singleLog = saveShipNum.split("@");
			if (singleLog != null && singleLog.length > 0) {
				OrderPrint orderPrint = null;
				for (int i = 0; i < singleLog.length; i++) {
					String[] elements = singleLog[i].split(",");
					if (elements != null && elements.length > 0) {
						params = new HashMap<String, Object>();
						params.put("id", Integer.parseInt(elements[0]));
						params.put("mailNo", elements[1]);
						orderPrintService.updateOrderNo(params);
					}
				}
			}
		}
	}

	/**
	 * 判断批量打印界面日期是否符合今天，昨天，近三天的
	 */
	public String chooseSign(String startDate, String endDate) {
		String sign = "0";
		try {
			if (endDate.equals(DateUtil.format(new Date(), "yyyy-MM-dd"))) {
				// 今天
				if (startDate.equals(DateUtil.format(new Date(), "yyyy-MM-dd"))) {
					sign = "0";
				} else if (DateUtil.stringToDate(startDate, -2).equals(
						DateUtil.format(new Date(), "yyyy-MM-dd"))) {
					sign = "2";
				} else {
					sign = "3";
				}
			} else if (endDate.equals(DateUtil.stringToDate(DateUtil.format(new Date(), "yyyy-MM-dd"), 1)) && startDate.equals(DateUtil.stringToDate(DateUtil.format(new Date(), "yyyy-MM-dd"), 1))) {
				sign = "1";
			} else {
				sign = "3";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 点击面单打印时直接调至orderPrint.jsp界面
	 * 
	 * @return
	 */
	public String orderPrint() {
		User currentUser = super.readCookieUser();
       
		// 如果是网点或者承包区，取客户列表
		getUserThreadOrPingtai();		

		// 卖家和业务账号
		if (currentUser.getUserType().equals("1")
				|| currentUser.getUserType().equals("11")
				|| currentUser.getUserType().equals("12")
				|| currentUser.getUserType().equals("13")
				|| currentUser.getUserType().equals("41")) {
			UserThread userThread = new UserThread();
			userThread.setUserCode(currentUser.getUserCode());
			// userCode为NULL ,就是没有绑定网点
			if (currentUser.getUserCode() != null) {
				List<UserThread> _newUserThreadList = userThreadService.searchUsersByCode(userThread);
				if (_newUserThreadList != null && _newUserThreadList.size() > 0) {
					if (StringUtils.equals(_newUserThreadList.get(0).getIsPrint(), "1")) {
						checked = "checked";
					}
				}	
			}
		}
		
		// 批量打印
		if (paginationOrderPrint == null) {
			paginationOrderPrint = new Pagination(currentPage, numOfPage);
		}
		paginationOrderPrint.setTotalPages(0);
		paginationOrderPrint.setTotalRecords(0);
		// 代发货
		if (undeliverpaginationOrderPrint == null) {
			undeliverpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		undeliverpaginationOrderPrint.setTotalPages(0);
		undeliverpaginationOrderPrint.setTotalRecords(0);
		// 已发货
		if (deliveredpaginationOrderPrint == null) {
			deliveredpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		deliveredpaginationOrderPrint.setTotalPages(0);
		deliveredpaginationOrderPrint.setTotalRecords(0);
		return "orderPrint";
	}

	
	/**
	 * 不想看到它
	 */
	public String delOrderPrintFromUndeliver() {
		curTab = "1";
		if (!"".equals(delOpIds)) {
			// 查询条件
			String[] idsArray = null;
			Map<String, Object> params = new HashMap<String, Object>();
			List<Integer> ids = new ArrayList<Integer>();
			idsArray = delOpIds.split(",");
			// 转成List
			if (idsArray != null) {
				for (int i = 0; i < idsArray.length; i++) {
					ids.add(Integer.parseInt(idsArray[i]));
				}
			}
			params.put("delOpIds", ids);
			orderPrintService.updateOrderPrintByIds(params);
		}
		getUndeliverGoodsList();
		return "orderPrint";
	}

	/**
	 * 获取直客信息 或者平台帐号下的子帐号
	 */
	public void getUserThreadOrPingtai() {
		User currentUser = super.readCookieUser();
		// 如果是网点或者承包区，取客户列表
		if (currentUser.getUserType().equals("2")
				|| currentUser.getUserType().equals("21")
				|| currentUser.getUserType().equals("22")
				|| currentUser.getUserType().equals("23")) {
			getUserThreadBySiteId();
		}
	}

	/**
	 * 获取批量打印界面的List
	 * 
	 * @return
	 */
	public String getBatchPrintList() {
		getUserThreadOrPingtai();
		
		// 标志当前页
		//curTab = "0";
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();

		if (paginationOrderPrint == null) {
			paginationOrderPrint = new Pagination(currentPage, numOfPage);
		}

		// 根据直客编码获取卖家的customID
		User currentUser = super.readCookieUser();
		// 网点用户
		if (currentUser.getUserType().equals("2")
				|| currentUser.getUserType().equals("21")
				|| currentUser.getUserType().equals("22")
				|| currentUser.getUserType().equals("23")) {
			if (!"".equals(customerCode)) {
				List<String> customerIds = new ArrayList<String>();
				User user = new User();
				user.setUserCode(customerCode);
				List<User> user_list = userService.searchUsersByCode(user);
				if (user_list != null && user_list.size() > 0) {
					for (User us : user_list) {
						customerIds.add(us.getTaobaoEncodeKey());
					}
					params.put("customerIds", customerIds);
				} else {
					params.put("flag", "nodata");
				}
			}
		} else {
			// 如果是平台，取分仓列表 获取customId 或者获取卖家的customId
			if (currentUser.getTaobaoEncodeKey() != null
					&& !"".equals(currentUser.getTaobaoEncodeKey())
					&& !"(NULL)".equals(currentUser.getTaobaoEncodeKey())) {
				params.put("customerId", currentUser.getTaobaoEncodeKey());
			}
		}

		if (StringUtils.isNotBlank(startDate)
				&& StringUtils.isNotBlank(endDate)) {
			params.put("startDate", startDate);
			// 对结束日期进行处理因between不包括后边的 所以在此基础上加一天
			try {
				params.put("endDate", DateUtil.stringToDate(endDate, -1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 判断前台日期单选框选中哪个
			dateSign = chooseSign(startDate, endDate);
		}
		if (StringUtils.isNotBlank(mailNoOrOrderNo)) {
			// 判断mailNoAndOrderNo是运单号还是订单号
			if (mailNoOrOrderNo.matches("^[A-Za-z0-9]{1}\\d{9}$")) {
				// 运单号
				params.put("mailNo", mailNoOrOrderNo);
			} else if (mailNoOrOrderNo.matches("^[0-9]*$")) {
				// 订单号
				params.put("tradeNo", mailNoOrOrderNo);
			}
		}
		params.put("orderType", orderType);
		params.put("orderByCol", orderByCol);
		params.put("startIndex", paginationOrderPrint.getStartIndex());
		params.put("pageNum", paginationOrderPrint.getPageNum());

		Integer countNum = orderPrintService.getTotal(params);
		List<OrderPrint> batchOrderPrintListTemp = orderPrintService
				.getAllOrderPrint(params);

		if (batchOrderPrintListTemp != null
				&& batchOrderPrintListTemp.size() > 0) {
			for (OrderPrint op : batchOrderPrintListTemp) {
				String newProductName = "";
				String productNam = op.getProductName();
				String[] singleGoods = productNam.split("</dd>");
				if (singleGoods != null && singleGoods.length > 0) {
					for (int g = 0; g < singleGoods.length; g++) {
						String[] elements = singleGoods[g].split(",,,");
						if (elements.length >= 1) {
							newProductName += elements[0] + "(";
							newProductName += elements[1] + ")</dd>";
						}
					}
				}
				op.setProductName(newProductName);
				
				if (op.getTxLogisticId() != null && op.getTxLogisticId().contains("REDBABY")) {
					op.setTxLogisticId(op.getTxLogisticId().replaceAll("REDBABY", ""));
				} 
				batchOrderPrintList.add(op);
			}
		}

		// 设置分页参数
		int countPage = 0;
		if (countNum % numOfPage == 0) {
			countPage = countNum / numOfPage;
		} else {
			countPage = countNum / numOfPage + 1;
		}
		for (int num = 1; num <= countPage; num++) {
			pageList.add(num + "");
		}
		paginationOrderPrint.setTotalRecords(countNum);
		paginationOrderPrint.setCurrentPage(currentPage);
		paginationOrderPrint.setRecords(batchOrderPrintList);

		// 代发货
		if (undeliverpaginationOrderPrint == null) {
			undeliverpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		undeliverpaginationOrderPrint.setTotalPages(0);
		undeliverpaginationOrderPrint.setTotalRecords(0);
		// 已发货
		if (deliveredpaginationOrderPrint == null) {
			deliveredpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		deliveredpaginationOrderPrint.setTotalPages(0);
		deliveredpaginationOrderPrint.setTotalRecords(0);
		//====================== update by guoliang.wang
		//解决jira YITONG-1957 待发货，已发货默认进来为空 不是一句话
		if(CollectionUtils.isEmpty(batchOrderPrintList)){
			this.showMessage ="0";
		}
		if(StringUtils.equals("1", this.curTab)){
			getUndeliverGoodsList();
		}else if(StringUtils.equals("2", this.curTab)){
			getDeliveredGoodsList();
		}
		
         // 打印按钮是否选中
		 checked=this.getisPrintFlag();
		return "orderPrint";
	}

	/**
	 * 代发货列表
	 * 
	 * @return
	 */
	public String getUndeliverGoodsList() {
		getUserThreadOrPingtai();
		// 标志当前页
		curTab = "1";

		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();

		if (undeliverpaginationOrderPrint == null) {
			undeliverpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		// 根据直客编码获取卖家的customID
		User currentUser = super.readCookieUser();
		// 网点用户
		if (currentUser.getUserType().equals("2")
				|| currentUser.getUserType().equals("21")
				|| currentUser.getUserType().equals("22")
				|| currentUser.getUserType().equals("23")) {
			if (!"".equals(customerCode2)) {
				List<String> customerIds = new ArrayList<String>();
				User user = new User();
				user.setUserCode(customerCode2);
				List<User> user_list = userService.searchUsersByCode(user);
				if (user_list != null && user_list.size() > 0) {
					for (User us : user_list) {
						customerIds.add(us.getTaobaoEncodeKey());
					}
					params.put("customerIds", customerIds);
				} else {
					params.put("flag", "nodata");
				}
			}
		} else {
			// 如果是平台，取分仓列表 获取customId 或者获取卖家的customId
			if (currentUser.getTaobaoEncodeKey() != null
					&& !"".equals(currentUser.getTaobaoEncodeKey())
					&& !"(NULL)".equals(currentUser.getTaobaoEncodeKey())) {
				params.put("customerId", currentUser.getTaobaoEncodeKey());
			}
		}

		if (StringUtils.isNotBlank(startDateUndeliver)
				&& StringUtils.isNotBlank(endDateUndeliver)) {
			params.put("startDate", startDateUndeliver);
			// 对结束日期进行处理因between不包括后边的 所以在此基础上加一天
			try {
				params.put("endDate",
						DateUtil.stringToDate(endDateUndeliver, -1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 判断前台日期单选框选中哪个
			dateSignUnelivered = chooseSign(startDateUndeliver,
					endDateUndeliver);
		}
		if (StringUtils.isNotBlank(mailNoOrOrderNoUndeliver)) {
			// 判断mailNoOrOrderNoUndeliver是运单号还是订单号
			if (mailNoOrOrderNoUndeliver.matches("^[A-Za-z0-9]{1}\\d{9}$")) {
				// 运单号
				params.put("mailNo", mailNoOrOrderNoUndeliver);
			} else  if (mailNoOrOrderNoUndeliver.matches("^[0-9]*$")) {
				// 订单号
				params.put("tradeNo", mailNoOrOrderNoUndeliver);
			}
		}
		params.put("orderByCol", undeliverOrderByCol);
		params.put("startIndex", undeliverpaginationOrderPrint.getStartIndex());
		params.put("pageNum", undeliverpaginationOrderPrint.getPageNum());

		// 获取分页数据和符合条件的记录数
		Integer countNum = orderPrintService.getUndeliverTotal(params);
		List<OrderPrint> undeliverOrderPrintListTemp = orderPrintService
				.getUndeliverOrderPrint(params);

		if (undeliverOrderPrintListTemp != null
				&& undeliverOrderPrintListTemp.size() > 0) {
			for (OrderPrint op : undeliverOrderPrintListTemp) {
				String newProductName = "";
				String productNam = op.getProductName();
				String[] singleGoods = productNam.split("</dd>");
				if (singleGoods != null && singleGoods.length > 0) {
					for (int g = 0; g < singleGoods.length; g++) {
						String[] elements = singleGoods[g].split(",,,");
						if (elements.length >= 1) {
							newProductName += elements[0] + "(";
							newProductName += elements[1] + ")</dd>";
						}
					}
				}
				op.setProductName(newProductName);
				
				if (op.getTxLogisticId() != null && op.getTxLogisticId().contains("REDBABY")) {
					op.setTxLogisticId(op.getTxLogisticId().replaceAll("REDBABY", ""));
				} 
				undeliverOrderPrintList.add(op);
			}
		}

		// 设置分页参数
		int countPage = 0;
		if (countNum % numOfPage == 0) {
			countPage = countNum / numOfPage;
		} else {
			countPage = countNum / numOfPage + 1;
		}
		for (int num = 1; num <= countPage; num++) {
			undeliverpageList.add(num + "");
		}
		undeliverpaginationOrderPrint.setTotalRecords(countNum);
		undeliverpaginationOrderPrint.setCurrentPage(currentPage);
		undeliverpaginationOrderPrint.setRecords(undeliverOrderPrintList);

		// 批量打印
		if (paginationOrderPrint == null) {
			paginationOrderPrint = new Pagination(currentPage, numOfPage);
		}
		paginationOrderPrint.setTotalPages(0);
		paginationOrderPrint.setTotalRecords(0);
		// 已发货
		if (deliveredpaginationOrderPrint == null) {
			deliveredpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		deliveredpaginationOrderPrint.setTotalPages(0);
		deliveredpaginationOrderPrint.setTotalRecords(0);


        // 打印按钮是否选中
		checked=this.getisPrintFlag();
		//解决jira YITONG-1957 待发货，已发货默认进来为空 不是一句话
		if(CollectionUtils.isEmpty(undeliverOrderPrintList)){
			this.showMessage ="1";
		}
		return "orderPrint";
	}

	/**
	 * 已发货列表
	 * 
	 * @return
	 */
	public String getDeliveredGoodsList() {
		getUserThreadOrPingtai();
		// 标志当前页
		curTab = "2";

		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();

		if (deliveredpaginationOrderPrint == null) {
			deliveredpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		
		// 根据直客编码获取卖家的customID
		User currentUser = super.readCookieUser();
		// 网点用户
		if (currentUser.getUserType().equals("2")
				|| currentUser.getUserType().equals("21")
				|| currentUser.getUserType().equals("22")
				|| currentUser.getUserType().equals("23")) {
			if (!"".equals(customerCode3)) {
				List<String> customerIds = new ArrayList<String>();
				User user = new User();
				user.setUserCode(customerCode3);
				List<User> user_list = userService.searchUsersByCode(user);
				if (user_list != null && user_list.size() > 0) {
					for (User us : user_list) {
						customerIds.add(us.getTaobaoEncodeKey());
					}
					params.put("customerIds", customerIds);
				} else {
					params.put("flag", "nodata");
				}
			}
		} else {
			// 如果是平台，取分仓列表 获取customId 或者获取卖家的customId
			if (currentUser.getTaobaoEncodeKey() != null
					&& !"".equals(currentUser.getTaobaoEncodeKey())
					&& !"(NULL)".equals(currentUser.getTaobaoEncodeKey())) {
				params.put("customerId", currentUser.getTaobaoEncodeKey());
			}
		}
		
		if (StringUtils.isNotBlank(startDateDelivered)
				&& StringUtils.isNotBlank(endDateDelivered)) {
			params.put("startDate", startDateDelivered);
			// 对结束日期进行处理因between不包括后边的 所以在此基础上加一天
			try {
				params.put("endDate",
						DateUtil.stringToDate(endDateDelivered, -1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dateSignDelivered = chooseSign(startDateDelivered, endDateDelivered);
		}
		if (StringUtils.isNotBlank(mailNoOrOrderNoDelivered)) {
			// 判断mailNoOrOrderNoUndeliver是运单号还是订单号
			if (mailNoOrOrderNoDelivered.matches("^[A-Za-z0-9]{1}\\d{9}$")) {
				// 运单号
				params.put("mailNo", mailNoOrOrderNoDelivered);
			} else  if (mailNoOrOrderNoDelivered.matches("^[0-9]*$")) {
				// 订单号
				params.put("tradeNo", mailNoOrOrderNoDelivered);
			}
		}
		params.put("orderByCol", deliveredOrderByCol);
		params.put("startIndex", deliveredpaginationOrderPrint.getStartIndex());
		params.put("pageNum", deliveredpaginationOrderPrint.getPageNum());

		// 获取分页数据和符合条件的记录数
		Integer countNum = orderPrintService.getDeliveredTotal(params);
		List<OrderPrint> deliveredOrderPrintListTemp = orderPrintService
				.getDeliveredOrderPrint(params);

		if (deliveredOrderPrintListTemp != null
				&& deliveredOrderPrintListTemp.size() > 0) {
			for (OrderPrint op : deliveredOrderPrintListTemp) {
				String newProductName = "";
				String productNam = op.getProductName();
				String[] singleGoods = productNam.split("</dd>");
				if (singleGoods != null && singleGoods.length > 0) {
					for (int g = 0; g < singleGoods.length; g++) {
						String[] elements = singleGoods[g].split(",,,");
						if (elements.length >= 1) {
							newProductName += elements[0] + "(";
							newProductName += elements[1] + ")</dd>";
						}
					}
				}
				op.setProductName(newProductName);
				
				if (op.getTxLogisticId() != null && op.getTxLogisticId().contains("REDBABY")) {
					op.setTxLogisticId(op.getTxLogisticId().replaceAll("REDBABY", ""));
				} 
				deliveredOrderPrintList.add(op);
			}
		}

		// 设置分页参数
		int countPage = 0;
		if (countNum % numOfPage == 0) {
			countPage = countNum / numOfPage;
		} else {
			countPage = countNum / numOfPage + 1;
		}
		for (int num = 1; num <= countPage; num++) {
			deliveredpageList.add(num + "");
		}
		deliveredpaginationOrderPrint.setTotalRecords(countNum);
		deliveredpaginationOrderPrint.setCurrentPage(currentPage);
		deliveredpaginationOrderPrint.setRecords(deliveredOrderPrintList);

		// 批量打印
		if (paginationOrderPrint == null) {
			paginationOrderPrint = new Pagination(currentPage, numOfPage);
		}
		paginationOrderPrint.setTotalPages(0);
		paginationOrderPrint.setTotalRecords(0);
		// 代发货
		if (undeliverpaginationOrderPrint == null) {
			undeliverpaginationOrderPrint = new Pagination(currentPage,
					numOfPage);
		}
		undeliverpaginationOrderPrint.setTotalPages(0);
		undeliverpaginationOrderPrint.setTotalRecords(0);
		

        // 打印按钮是否选中
		 checked=this.getisPrintFlag();
		//解决jira YITONG-1957 待发货，已发货默认进来为空 不是一句话
		if(CollectionUtils.isEmpty(deliveredOrderPrintList)){
			this.showMessage ="2";
		}
		return "orderPrint";
	}

	/**
	 * 网点登录时获取可以帮忙打印的卖家列表
	 */
	public void getUserThreadBySiteId() {

		User currentUser = super.readCookieUser();
		currentUser.setFinancialManager("financialManager");
		List<UserThread> vipThreadListAll = super.getZhiKeUserAtOrderPrint(currentUser); // 网点下的所有直客信息
		if(!CollectionUtils.isEmpty(vipThreadListAll)){ // add by guoliang.wang  这句话为了解决404
			for (UserThread userThread : vipThreadListAll) {
				// 过滤掉没有授权的卖家
				if ("1".equals(userThread.getIsPrint())) {
					vipThreadList.add(userThread);
				}
			}
		}
		if ("".equals(customerCode) && vipThreadList != null
				&& vipThreadList.size() > 0) {
			customerCode = vipThreadList.get(0).getUserCode();
		}
		if ("".equals(customerCode2) && vipThreadList != null
				&& vipThreadList.size() > 0) {
			customerCode2 = vipThreadList.get(0).getUserCode();
		}
		if ("".equals(customerCode3) && vipThreadList != null
				&& vipThreadList.size() > 0) {
			customerCode3 = vipThreadList.get(0).getUserCode();
		}
	}

	/**
	 * 合并和取消订单
	 */
	public String joinOrClearOrder() {
		// 标志当前页
		curTab = "0";

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		String[] idsArray = null;
		if (!"".equals(joinOrClearOrderIdsAuto)) {
			// 转换ids字符串为数组
			idsArray = joinOrClearOrderIdsAuto.split(",");

		} else if (!"".equals(joinOrClearOrderIdsMaunal)) {
			idsArray = joinOrClearOrderIdsMaunal.split(",");
		}
		// 转成List
		if (idsArray != null) {
			for (int i = 0; i < idsArray.length; i++) {
				ids.add(Integer.parseInt(idsArray[i]));
			}
		}
		params.put("joinOrClearOrderIds", ids);
		params.put("orderByCol", orderByCol);
		if (ids.size() > 0) {
			List<OrderPrint> batchOrderPrintListTemp = orderPrintService
					.getServalOrderPrintLists(params);
			if (batchOrderPrintListTemp != null
					&& batchOrderPrintListTemp.size() > 0) {
				// 未合并订单， 执行的是合并订单操作
				if ("0".equals(batchOrderPrintListTemp.get(0).getParentId()
						+ "")) {
					joinOrder(batchOrderPrintListTemp);
				} else {
					// 取消合并订单操作
					clearOrder(batchOrderPrintListTemp);
				}
			}
		}
		// 在未合并订单底下 做合并订单操作之后跳转到已合并订单
		if ("6".equals(orderType)) {
			orderType = "7";
		}
		// 在合并订单下做取消订单操作后跳转成未合并
		if ("7".equals(orderType)) {
			orderType = "6";
		}
		// 获取合并后的数据
		getBatchPrintList();

		return "orderPrint";
	}

	/**
	 * 取消合并订单
	 * 
	 * @param batchOrderPrintListTemp
	 */
	public void clearOrder(List<OrderPrint> batchOrderPrintListTemp) {
		// 次订单
		OrderPrint orderPrint = null;
		// 手动取消合并和手动取消合并是一样的规则
		for (OrderPrint orderPrintTemp : batchOrderPrintListTemp) {

			// 获取此订单下的子订单
			Integer idAndParentId = orderPrintTemp.getId();
			List<OrderPrint> orderPrintList = orderPrintService
					.getOrderPrintByParentId(idAndParentId);
			if (orderPrintList != null && orderPrintList.size() > 0) {
				for (OrderPrint op : orderPrintList) {
					op.setParentId(0);
					op.setMailNo("");
					orderPrintService.updateOrderPrint(op);
				}
			}
			// 修改主订单中的内容
			orderPrintTemp.setIsCombine("N");
			orderPrintTemp.setParentId(0);
			orderPrintTemp.setProductName(orderPrintTemp.getRemark());
			orderPrintTemp.setFirstProductName(orderPrintTemp.getRemark()
					.split("</dd>")[0].substring(4));
			orderPrintTemp.setGoodsValue(orderPrintTemp.getOldPrice());
			orderPrintTemp.setJoinNum(0);
			orderPrintService.updateOrderPrint(orderPrintTemp);
		}
	}

	/**
	 * 合并订单
	 */
	public void joinOrder(List<OrderPrint> batchOrderPrintListTemp) {
		// 更改List为Map提高查询效率
		Map<Integer, OrderPrint> orderPrintMapTemp = changeListToMap(batchOrderPrintListTemp);

		// 次订单
		OrderPrint orderPrint = null;

		// 主订单
		OrderPrint orderPrintTemp = null;
		if (!"".equals(joinOrClearOrderIdsMaunal)) {

			// 手动合并订单
			orderPrintTemp = batchOrderPrintListTemp.get(0);

			String productNames = orderPrintTemp.getRemark();
			// 修改次订单parent_id,mailNo; 和主订单的价格
			for (int orderNum = 1; orderNum < batchOrderPrintListTemp.size(); orderNum++) {
				orderPrint = batchOrderPrintListTemp.get(orderNum);
				orderPrint.setMailNo(null);
				orderPrint.setParentId(orderPrintTemp.getId());
				// 更新次订单进入数据库
				orderPrintService.updateOrderPrint(orderPrint);
				// 更新主订单价格
				orderPrintTemp.setGoodsValue(orderPrintTemp.getGoodsValue()
						+ orderPrint.getGoodsValue());

				// 获取所有合并订单的商品信息
				productNames += orderPrint.getRemark();
			}

			// 标识由几个订单合并
			orderPrintTemp.setJoinNum(batchOrderPrintListTemp.size());

			// 对合并订单中的商品排序并设置排序后的第一个商品
			List<String> productList = new ArrayList<String>();
			String[] productArray = productNames.split("</dd>");
			for (int i = 0; i < productArray.length; i++) {
				productList.add(productArray[i]);
			}
			// 拼凑排序后的商品信息
			if (productList != null && productList.size() > 0) {
				String newProductNames = "";
				// 商品排序
				Collections.sort(productList);
				for (String p : productList) {
					newProductNames += p + "</dd>";
				}
				// 修改商品信息
				orderPrintTemp.setProductName(newProductNames);
				// 设置排序后的第一个商品信息
				orderPrintTemp.setFirstProductName(productList.get(0)
						.substring(4));
			}
			orderPrintTemp.setParentId(1);
			// 设置合并标识
			orderPrintTemp.setIsCombine("Y");
			// 更新主订单进数据库
			orderPrintService.updateOrderPrint(orderPrintTemp);

		} else if (!"".equals(joinOrClearOrderIdsAuto)) {
			// 自动合并订单
			for (int m = 0; m < batchOrderPrintListTemp.size(); m++) {
				orderPrintTemp = batchOrderPrintListTemp.get(m);
				for (int j = batchOrderPrintListTemp.size() - 1; j > m; j--) {
					orderPrint = batchOrderPrintListTemp.get(j);
					// 自动合并必需满足条件 即： 卖家姓名，电话，地址必须一致
					if (orderPrintTemp.getBuyName().equals(
							orderPrint.getBuyName())
							&& orderPrintTemp.getBuyTelphone().equals(
									orderPrint.getBuyTelphone())
							&& orderPrintTemp.getBuyMobile().equals(
									orderPrint.getBuyMobile())
							&& orderPrintTemp.getBuyFulladdress().equals(
									orderPrint.getBuyFulladdress())) {

						// 对合并订单中的商品排序并设置排序后的第一个商品
						List<String> productList = new ArrayList<String>();
						String productNames = orderPrintTemp.getProductName()
								+ orderPrint.getRemark();
						String[] productArray = productNames.split("</dd>");
						for (int i = 0; i < productArray.length; i++) {
							productList.add(productArray[i]);
						}
						// 拼凑排序后的商品信息
						if (productList != null && productList.size() > 0) {
							String newProductNames = "";
							// 商品排序
							Collections.sort(productList);
							for (String p : productList) {
								newProductNames += p + "</dd>";
							}
							// 修改商品信息
							orderPrintTemp.setProductName(newProductNames);
							// 设置排序后的第一个商品信息
							orderPrintTemp.setFirstProductName(productList.get(
									0).substring(4));
						}

						// 订单价格修改
						// 保存老的订单价格为所要合并订单的价格总和
						orderPrintTemp.setGoodsValue(orderPrintTemp
								.getGoodsValue() + orderPrint.getGoodsValue());

						// 主订单合并的个数加一；
						if (orderPrintTemp.getJoinNum() == 0) {
							orderPrintTemp.setJoinNum(2);
						} else {
							orderPrintTemp.setJoinNum(orderPrintTemp
									.getJoinNum() + 1);
						}

						// 设置父订单ID并改运单号为空
						orderPrint.setMailNo("");
						orderPrint.setParentId(orderPrintTemp.getId());

						// 去除batchOrderPrintListTemp中的orderPrint对象
						batchOrderPrintListTemp.remove(orderPrint);

						orderPrintTemp.setParentId(1);
						// 更改状态为已合并
						orderPrintTemp.setIsCombine("Y");

						// 保存主订单和次订单到数据库
						orderPrintService.updateOrderPrint(orderPrintTemp);
						orderPrintService.updateOrderPrint(orderPrint);
					}
				}
			}
		}
	}

	/**
	 * 转化List为Map
	 */
	public Map<Integer, OrderPrint> changeListToMap(
			List<OrderPrint> orderPrintList) {
		Map<Integer, OrderPrint> orderPrintMap = new HashMap<Integer, OrderPrint>();
		for (OrderPrint orderPrint : orderPrintList) {
			orderPrintMap.put(orderPrint.getId(), orderPrint);
		}
		return orderPrintMap;
	}

	/**
	 * 允许网点打印
	 * 
	 * @return
	 */
	public String isPrint() {
		User currentUser = super.readCookieUser();
		if(StringUtils.isBlank(currentUser.getUserCode())){
			bindBranch=false;
			return "isPrint";
		}
		boolean flag = false;
		try {
			// 卖家 和 业务账号
			UserThread userThread = new UserThread();
			userThread.setUserCode(currentUser.getUserCode());
			userThread.setIsPrint(isPrintFlag);
			// 修改是否允许网点(承包区)打印功能
			flag = userThreadService.updateUserThread(userThread);

			if (flag) {
				json = JSONUtil.serialize("允许网点打印设置成功！");
			} else {
				json = JSONUtil.serialize("允许网点打印设置失败！");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (org.apache.struts2.json.JSONException e) {
			e.printStackTrace();
		}
		bindBranch=flag;
		return "isPrint";
	}

	
	/**
	 *允许网点打印  是否选中  true 选中  false 未选中
	 *add by  guoliang.wang
	 */
	public String getisPrintFlag(){
		User currentUser = super.readCookieUser();
		
		try {
			// 如果是卖家
			if (currentUser.getUserType().equals("1")
					|| currentUser.getUserType().equals("12")
					|| currentUser.getUserType().equals("13")) {
				UserThread userThread = new UserThread();
				userThread.setUserCode(currentUser.getUserCode());
				
				// 修改是否允许网点(承包区)打印功能
				List<UserThread> userThreadTemp= userThreadService.getisPrintFlag(userThread);
				if(!CollectionUtils.isEmpty(userThreadTemp)){
					if(StringUtils.equals("1", userThreadTemp.get(0).getIsPrint())){
						return "checked";
					}
				}
			}

			// 如果是分仓用户
			if (currentUser.getUserType().equals("41")) {
				Map map = new HashMap();
				map.put("id", currentUser.getId());
			
				List<User> userListTemp = userService.getisPrintFlag(map);
				if(!CollectionUtils.isEmpty(userListTemp)){
					if(StringUtils.equals("0", userListTemp.get(0).getIsPrint())){
						return "checked";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 在待发货列表页点批量发货功能 把 合并，取消合并订单信息 同时同步到EC_CORE_ORDER表
	 */
	public void synchronizedOrder(OrderPrint orderPrint) {
		String mailNo = orderPrint.getMailNo();
		Double goodsValue = new Double(orderPrint.getGoodsValue());

		Order _Order = new Order();
		_Order.setId(orderPrint.getId());
		_Order.setMailNo(mailNo);
		_Order.setGoodsValue(goodsValue);
		// 同步主单
		orderService.updateMailNoValue(_Order);

		if (orderPrint.equals("Y")) {
			// 查找是否有合并的运单，如果有把合并前的分单也同步修改掉
			List<OrderPrint> list = orderPrintService
					.getOrderPrintByParentId(orderPrint.getId());
			if (list != null) {
				for (OrderPrint myOrderPrint : list) {
					Order newOrder = new Order();
					newOrder.setId(myOrderPrint.getId());
					_Order.setMailNo(mailNo);
					_Order.setGoodsValue(goodsValue);
					// 同步主单
					orderService.updateMailNoValue(newOrder);
				}
			}
		}
	}

	/**
	 * 点击 打印快递单 按钮时取列表数据
	 */
	public String getPrintOrderPrintList() {

		Integer count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		String[] idsArray = null;
		if (!"".equals(orderPrintIds)) {
			// 转换ids字符串为数组
			idsArray = orderPrintIds.split(",");

		}
		// 转成List
		if (idsArray != null) {
			for (int i = 0; idsArray != null && i < idsArray.length; i++) {
				ids.add(Integer.parseInt(idsArray[i]));
			}
		}

		params.put("joinOrClearOrderIds", ids);
		params.put("orderByCol", orderByCol);
		clickBatchOrderPrintList = orderPrintService
				.getServalOrderPrintLists(params);
		// 拼凑模版
		
		OrderExpress orderExpress = orderExpService
				.getOrderExpressById(OrderExpressId);
		String decodeXML = orderPrintService.unescape(orderExpress
				.getTemplatedata());
		
		String backgroundimageurl = orderExpress.getBackgroundimageurl();

		if (backgroundimageurl.indexOf("template_a") > -1)
			backgroundimageurl = "template_a";
		if (backgroundimageurl.indexOf("template_b") > -1)
			backgroundimageurl = "template_b";
		if (backgroundimageurl.indexOf("template_c") > -1)
			backgroundimageurl = "template_c";
		if (backgroundimageurl.indexOf("template_d") > -1)
			backgroundimageurl = "template_d";
		if (backgroundimageurl.indexOf("template_e") > -1)
			backgroundimageurl = "template_e";
		if (backgroundimageurl.indexOf("yto_2013") > -1)
			backgroundimageurl = "yto_2013";
		decodeXML = decodeXML.replace(":", ",");

		decodeXML = decodeXML.replace("<position>", "<position>[");
		decodeXML = decodeXML.replace("</position>", "]</position>");

		json = orderPrintService.convertXMLToJson(decodeXML);
		
		json = json.replace("@", "");
		json = json.substring(0, json.length() - 1) + ",\"background\":\""
				+ backgroundimageurl + "\"}";
        
		InputStream iStream = new ByteArrayInputStream(decodeXML.getBytes());
		// 解析xml
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dm = (Document) db.parse(iStream);
			NodeList names = ((org.w3c.dom.Document) dm)
					.getElementsByTagName("name");
			NodeList ucodes = ((org.w3c.dom.Document) dm)
					.getElementsByTagName("ucode");

			if (clickBatchOrderPrintList != null
					&& clickBatchOrderPrintList.size() > 0) {
			    Date date= new Date();
			    StringBuffer moduleBuffer=new StringBuffer();
			    Calendar cal=Calendar.getInstance();//使用日历类
				for (int i = 0; i < clickBatchOrderPrintList.size(); i++) {
					// 转化orderprint对象为printTemp对象并构造前台数据；
				 // 解析xml
			        Long start01=System.currentTimeMillis();
					OrderPrint orderPrint = clickBatchOrderPrintList.get(i);
					PrintTemp pt = new PrintTemp();
					pt.setDateDay(cal.get(Calendar.DAY_OF_MONTH) + "");
					int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
					pt.setDateMoth(month + "");
					pt.setDateYear(cal.get(Calendar.YEAR) + "");
					pt.setDlyAddress(orderPrint.getSaleFulladdress());
					pt.setDlyMobile(orderPrint.getSaleMobile());
					pt.setDlyName(orderPrint.getSaleName());
					pt.setDlyRegionFullName(orderPrint.getSaleProv() + " " + orderPrint.getSaleCity());
					pt.setDlyTel(orderPrint.getSaleTelphone());
					pt.setDlyZip("");
					if (orderPrint.getTxLogisticId() != null && orderPrint.getTxLogisticId().contains("REDBABY")) {
						pt.setMailNo(orderPrint.getTxLogisticId().replaceAll("REDBABY", ""));
					} else {
						pt.setMailNo(orderPrint.getTxLogisticId());
					}
					pt.setTick("√");
					pt.setText("");
					if (StringUtils.isNotEmpty(orderPrint.getCustomerId())) {
						User userTemp = Resource.getUserByCustomerId(orderPrint.getCustomerId());
						pt.setStoreName(userTemp.getShopName() == null ? "" : userTemp.getShopName());
					} else {
						pt.setStoreName("店铺名称");
					}
					pt.setShipZip(orderPrint.getBuyPostcode());
					pt.setShipAddress(orderPrint.getBuyFulladdress());
					pt.setShipmentItemCount(orderPrint.getProductName().split(
							"</dd>").length
							+ "");
					// 货运单号改为商品名称
					String newProductName = "";
					String productNam = orderPrint.getProductName();
					String[] singleGoods = productNam.split("</dd>");
					if (singleGoods != null && singleGoods.length > 0) {
						for (int g = 0; g < singleGoods.length; g++) {
							String[] elements = singleGoods[g].split(",,,");
							if (elements.length >= 1) {
								newProductName += elements[0] + "</dd>";
							}
						}
					}
					pt.setShipmentNo(newProductName);
					pt.setShipName(orderPrint.getBuyName());
					pt.setShipRegionFullName(orderPrint.getBuyProv() + " " + orderPrint.getBuyCity());
					if (StringUtils.isEmpty(orderPrint.getBuyMobile())) {
						pt.setShipTel(orderPrint.getBuyTelphone());
					} else {
						pt.setShipTel(orderPrint.getBuyMobile());
					}
					
					pt.setOrderNo(orderPrint.getTradeNo());
					
					if (i == 0) {
					 
					    moduleBuffer.append("<table><tr><td><div class=\"template\">");
						for (int j = 0; j < ucodes.getLength(); j++) {
							String ucode = ucodes.item(j).getFirstChild()
									.getNodeValue();
							String name = names.item(j).getFirstChild()
									.getNodeValue();
							if ("text".equals(ucode)) {
							    moduleBuffer.append("<div class=\"" + ucode
										+ " item\">" + name + "</div>");
							} else {
								String methodName = "";
								String[] codes = ucode.split("_");
								for (int m = 0; m < codes.length; m++) {
									methodName += changeUpperFirst(codes[m]);
								}
								if (pt.getClass()
										.getMethod("get" + methodName, null)
										.invoke(pt, null) == null) {
								    moduleBuffer.append( "<div class=\"" + ucode
											+ " item\"></div>");
								} else {
								    moduleBuffer.append ("<div class=\""
											+ ucode
											+ " item\">"
											+ pt.getClass()
													.getMethod(
															"get" + methodName,
															null)
													.invoke(pt, null)
											+ "</div>");
								}
							}
						}
						moduleBuffer.append ( "</div></td></tr></table>");
					} else {
					    moduleBuffer.append( "<table style=\"display:none;\"><tr><td><div class=\"template\">");
						for (int j = 0; j < ucodes.getLength(); j++) {
							String ucode = ucodes.item(j).getFirstChild()
									.getNodeValue();
							String name = names.item(j).getFirstChild()
									.getNodeValue();
							if ("text".equals(ucode)) {
							    moduleBuffer.append ( "<div class=\"" + ucode
										+ " item\">" + name + "</div>");
							} else {
								String methodName = "";
								String[] codes = ucode.split("_");
								for (int m = 0; m < codes.length; m++) {
									methodName += changeUpperFirst(codes[m]);
								}
								if (pt.getClass()
										.getMethod("get" + methodName, null)
										.invoke(pt, null) == null) {
								    moduleBuffer.append( "<div class=\"" + ucode
											+ " item\"></div>");
								} else {
								    moduleBuffer.append ( "<div class=\""
											+ ucode
											+ " item\">"
											+ pt.getClass()
													.getMethod(
															"get" + methodName,
															null)
													.invoke(pt, null)
											+ "</div>");
								}
							}
						}
						moduleBuffer.append  ( "</div></td></tr></table>");
					}
				}
				moduleString=moduleBuffer.toString();
				logger.error("moduleString:"+moduleString);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("e-moduleString:"+moduleString);
		}
		
		return "printOrderPrintList";
	}

	/**
	 * 点击预览当中的打印快递单后改变订单状态为已打印快递单
	 */
	public void updateIsPrintBatch() {
		String[] idsArray = null;
		if (!"".equals(markPrint)) {
			// 转换ids字符串为数组
			idsArray = markPrint.split(",");
		}
		// 转成List
		if (idsArray != null) {
			for (int i = 0; idsArray != null && i < idsArray.length; i++) {
				// 根据ID 修改打印快速单状态
				Map map = new HashMap();
				map.put("isPrint", "Y");
				map.put("id", Integer.parseInt(idsArray[i]));
				orderPrintService.updateOrderPrintIsPrint(map);
			}
		}
	}

	/**
	 * 点击打印发货单预览界面后改变订单状态为已打印发货单
	 */
	public void updateIsPrintSendBatch() {
		String[] idsArray = null;
		if (!"".equals(markPrintSend)) {
			idsArray = markPrintSend.split(",");

		}
		// 转成List
		if (idsArray != null) {
			for (int i = 0; idsArray != null && i < idsArray.length; i++) {
				// 根据ID 修改打印发货单状态
				Map map = new HashMap();
				map.put("isPrintSend", "Y");
				map.put("id", Integer.parseInt(idsArray[i]));
				orderPrintService.updateOrderPrintIsSendPrint(map);
			}
		}
	}

	/**
	 * 字符串第一个字母大写
	 */
	public String changeUpperFirst(String s) {
		byte[] items = s.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}

	/**
	 * 点击 打印发货单 按钮时取列表数据
	 */
	public String getSendPrintOrderPrintList() {

		Integer count = 0;
		String ProductName;
		int items = 10;// 每10个商品分页打页

		Map<String, Object> params = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		String[] idsArray = null;
		if (!"".equals(orderPrintIds)) {
			// 转换ids字符串为数组
			idsArray = orderPrintIds.split(",");

		}
		// 转成List
		if (idsArray != null) {
			for (int i = 0; idsArray != null && i < idsArray.length; i++) {
				ids.add(Integer.parseInt(idsArray[i]));
			}
		}
		params.put("joinOrClearOrderIds", ids);
		params.put("orderByCol", orderByCol);

		List<OrderPrint> tmpList = new ArrayList<OrderPrint>();
		tmpList = orderPrintService.getServalOrderPrintLists(params);

		for (OrderPrint _OrderPrint : tmpList) {
			ProductName = _OrderPrint.getProductName();
			String[] arr = ProductName.split("</dd>");
			int page = (int) Math.ceil((float) arr.length / items);
			// 循环每页发货单数据
			for (int i = 1; i <= page; i++) {
				// 每10条商品进行一次打印
				String myProductName = "";
				for (int j = (i - 1) * items; arr != null && j < i * items; j++) {
					if (j < arr.length) {
						myProductName += arr[j] + "</dd>";
					}
				}

				OrderPrint newOrderPrint = new OrderPrint();
				BeanUtils.copyProperties(_OrderPrint, newOrderPrint);
				newOrderPrint.setProductName(myProductName);

				clickBatchOrderPrintSendList.add(newOrderPrint);
			}
		}

		return "sendPrintOrderPrintList";
	}

	/**
	 * 点击 批量发货 按钮 后数据处理 同时同步到EC_CORE_ORDER
	 */

	public String batchOrderPrintSend() {
		curTab = "1";
		boolean flag = false;
		try {
		    Object topSession=session.get("sessionKey");
		    if (topSession==null) {
		    	User currentUser = super.readCookieUser();
				if (currentUser.getUserType().equals("1") && !StringUtils.equals(currentUser.getChildType(), "B") && !StringUtils.equals(currentUser.getChildType(), "C")) {
					 json = JSONUtil.serialize("false");
				        logger.error("topSession is null");
				        getUndeliverGoodsList();
				        isSuccessToTaobao = "overTime";
				        return "orderPrint";
				}
				topSession = "";
            } 
            	
          Map<String, Object> resultMap=orderPrintService.batchOrderPrintSend(orderPrintIds, topSession.toString());
          Object isSuccess=resultMap.get("isSuccess");
          if (isSuccess!=null && StringUtils.equals("true",isSuccess.toString() )) {
              json = JSONUtil.serialize("true");
              isSuccessToTaobao = "success";
              //FIXME 发货失败的订单
           }else {
               json = JSONUtil.serialize("false");
               isSuccessToTaobao = "error";
        }
			

		} catch (JSONException e) {
			try {
				json = JSONUtil.serialize("false");
			} catch (org.apache.struts2.json.JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (org.apache.struts2.json.JSONException e) {
			try {
				json = JSONUtil.serialize("false");
			} catch (org.apache.struts2.json.JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		getUndeliverGoodsList();
		return "orderPrint";
	}

	/**
	 *  网点进入面单打印之后根据直客信息获取卖家和自己的模版信息
	 */
	public String getOrderExpress() {
		
		List<OrderExpress> orderExpList = null;
		User currentUser = super.readCookieUser();
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"".equals(customerCode)) {
			// 网点登录
			List<String> userIds = new ArrayList<String>();
			User user = new User();
			user.setUserCode(customerCode);
			List<User> user_list = userService.searchUsersByCode(user);
			if (user_list != null && user_list.size() > 0) {
				for (User us : user_list) {
					userIds.add(us.getId() + "");
				}
			}
			userIds.add(currentUser.getId() + "");
			params.put("storeIds", userIds);
			orderExpList = orderExpService.getOrderExpressByStoreIds(params);
			if (orderExpList != null && orderExpList.size() > 0) {
				for (OrderExpress orderExpress : orderExpList) {
					moduleExpressString += orderExpress.getId() + ":";
					moduleExpressString += orderExpress.getOrderexpressname() + ":";
					if ((currentUser.getId() + "").equals((orderExpress.getStoreid() + ""))) {
						// 网点
						moduleExpressString += "2";
					} else {
						// 卖家
						moduleExpressString += "1";
					}
					moduleExpressString += ",";
				}
				moduleExpressString = moduleExpressString.substring(0, moduleExpressString.length() - 1);
			} else {
				moduleExpressString += "0:暂无模版:2";
			}
		} else {
			// 卖家或者 业务帐号
			params.put("storeid", currentUser.getId());
			orderExpList = orderExpService.getOrderExpressByStoreId(params);
			if (orderExpList != null && orderExpList.size() > 0) {
				for (OrderExpress orderExpress : orderExpList) {
					moduleExpressString += orderExpress.getId() + ":";
					moduleExpressString += orderExpress.getOrderexpressname() + ":";
					moduleExpressString += "2";
					moduleExpressString += ",";
				}
				moduleExpressString = moduleExpressString.substring(0, moduleExpressString.length() - 1);
			} else {
				moduleExpressString += "0:暂无模版:2";
			}
		}
		
		return "moduleExpressString";
	}

	
	/**
	 * 跳转打印指南
	 * @return
	 */
	public String toPrintGuide(){
		
		return "toPrintGuide";
	}
	/**
	 * 打印指南界面
	 * @return
	 */
    public String printGuide(){
		if((flag !=null) &&(1==flag)){
			return "printNews";
		}
		return "printGuide";
	}
	public OrderPrintService<OrderPrint> getOrderPrintService() {
		return orderPrintService;
	}

	public void setOrderPrintService(
			OrderPrintService<OrderPrint> orderPrintService) {
		this.orderPrintService = orderPrintService;
	}

	public List<OrderPrint> getBatchOrderPrintList() {
		return batchOrderPrintList;
	}

	public void setBatchOrderPrintList(List<OrderPrint> batchOrderPrintList) {
		this.batchOrderPrintList = batchOrderPrintList;
	}

	public List<OrderPrint> getUndeliverOrderPrintList() {
		return undeliverOrderPrintList;
	}

	public void setUndeliverOrderPrintList(
			List<OrderPrint> undeliverOrderPrintList) {
		this.undeliverOrderPrintList = undeliverOrderPrintList;
	}

	public List<OrderPrint> getDeliveredOrderPrintList() {
		return deliveredOrderPrintList;
	}

	public void setDeliveredOrderPrintList(
			List<OrderPrint> deliveredOrderPrintList) {
		this.deliveredOrderPrintList = deliveredOrderPrintList;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
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

	public String getStartDateDelivered() {
		return startDateDelivered;
	}

	public void setStartDateDelivered(String startDateDelivered) {
		this.startDateDelivered = startDateDelivered;
	}

	public String getEndDateDelivered() {
		return endDateDelivered;
	}

	public void setEndDateDelivered(String endDateDelivered) {
		this.endDateDelivered = endDateDelivered;
	}

	public String getMailNoOrOrderNo() {
		return mailNoOrOrderNo;
	}

	public void setMailNoOrOrderNo(String mailNoOrOrderNo) {
		this.mailNoOrOrderNo = mailNoOrOrderNo;
	}

	public String getMailNoOrOrderNoUndeliver() {
		return mailNoOrOrderNoUndeliver;
	}

	public void setMailNoOrOrderNoUndeliver(String mailNoOrOrderNoUndeliver) {
		this.mailNoOrOrderNoUndeliver = mailNoOrOrderNoUndeliver;
	}

	public String getMailNoOrOrderNoDelivered() {
		return mailNoOrOrderNoDelivered;
	}

	public void setMailNoOrOrderNoDelivered(String mailNoOrOrderNoDelivered) {
		this.mailNoOrOrderNoDelivered = mailNoOrOrderNoDelivered;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getNumOfPage() {
		return numOfPage;
	}

	public void setNumOfPage(Integer numOfPage) {
		this.numOfPage = numOfPage;
	}

	public Pagination<OrderPrint> getPaginationOrderPrint() {
		return paginationOrderPrint;
	}

	public void setPaginationOrderPrint(
			Pagination<OrderPrint> paginationOrderPrint) {
		this.paginationOrderPrint = paginationOrderPrint;
	}

	public String getOrderByCol() {
		return orderByCol;
	}

	public void setOrderByCol(String orderByCol) {
		this.orderByCol = orderByCol;
	}

	public List<UserThread> getVipThreadList() {
		return vipThreadList;
	}

	public void setVipThreadList(List<UserThread> vipThreadList) {
		this.vipThreadList = vipThreadList;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getIsPrint() {
		return isPrintFlag;
	}

	public void setIsPrint(String isPrintFlag) {
		this.isPrintFlag = isPrintFlag;
	}

	public String getIsPrintFlag() {
		return isPrintFlag;
	}

	public void setIsPrintFlag(String isPrintFlag) {
		this.isPrintFlag = isPrintFlag;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getJoinOrClearOrderIdsAuto() {
		return joinOrClearOrderIdsAuto;
	}

	public void setJoinOrClearOrderIdsAuto(String joinOrClearOrderIdsAuto) {
		this.joinOrClearOrderIdsAuto = joinOrClearOrderIdsAuto;
	}

	public String getJoinOrClearOrderIdsMaunal() {
		return joinOrClearOrderIdsMaunal;
	}

	public void setJoinOrClearOrderIdsMaunal(String joinOrClearOrderIdsMaunal) {
		this.joinOrClearOrderIdsMaunal = joinOrClearOrderIdsMaunal;
	}

	public List<OrderPrint> getClickBatchOrderPrintList() {
		return clickBatchOrderPrintList;
	}

	public void setClickBatchOrderPrintList(
			List<OrderPrint> clickBatchOrderPrintList) {
		this.clickBatchOrderPrintList = clickBatchOrderPrintList;
	}

	public List<OrderPrint> getClickBatchOrderPrintSendList() {
		return clickBatchOrderPrintSendList;
	}

	public void setClickBatchOrderPrintSendList(
			List<OrderPrint> clickBatchOrderPrintSendList) {
		this.clickBatchOrderPrintSendList = clickBatchOrderPrintSendList;
	}

	public String getCurTab() {
		return curTab;
	}

	public void setCurTab(String curTab) {
		this.curTab = curTab;
	}

	public String getUndeliverOrderByCol() {
		return undeliverOrderByCol;
	}

	public void setUndeliverOrderByCol(String undeliverOrderByCol) {
		this.undeliverOrderByCol = undeliverOrderByCol;
	}

	public String getDelOpIds() {
		return delOpIds;
	}

	public void setDelOpIds(String delOpIds) {
		this.delOpIds = delOpIds;
	}

	public String getDeliveredOrderByCol() {
		return deliveredOrderByCol;
	}

	public void setDeliveredOrderByCol(String deliveredOrderByCol) {
		this.deliveredOrderByCol = deliveredOrderByCol;
	}

	public List<String> getPageList() {
		return pageList;
	}

	public void setPageList(List<String> pageList) {
		this.pageList = pageList;
	}

	public Integer getOrderExpressId() {
		return OrderExpressId;
	}

	public void setOrderExpressId(Integer orderExpressId) {
		OrderExpressId = orderExpressId;
	}

	public List<String> getUndeliverpageList() {
		return undeliverpageList;
	}

	public void setUndeliverpageList(List<String> undeliverpageList) {
		this.undeliverpageList = undeliverpageList;
	}

	public List<String> getDeliveredpageList() {
		return deliveredpageList;
	}

	public void setDeliveredpageList(List<String> deliveredpageList) {
		this.deliveredpageList = deliveredpageList;
	}

	public Pagination<OrderPrint> getUndeliverpaginationOrderPrint() {
		return undeliverpaginationOrderPrint;
	}

	public void setUndeliverpaginationOrderPrint(
			Pagination<OrderPrint> undeliverpaginationOrderPrint) {
		this.undeliverpaginationOrderPrint = undeliverpaginationOrderPrint;
	}

	public Pagination<OrderPrint> getDeliveredpaginationOrderPrint() {
		return deliveredpaginationOrderPrint;
	}

	public void setDeliveredpaginationOrderPrint(
			Pagination<OrderPrint> deliveredpaginationOrderPrint) {
		this.deliveredpaginationOrderPrint = deliveredpaginationOrderPrint;
	}

	public String getSaveShipNum() {
		return saveShipNum;
	}

	public void setSaveShipNum(String saveShipNum) {
		this.saveShipNum = saveShipNum;
	}

	public String getOrderPrintIds() {
		return orderPrintIds;
	}

	public void setOrderPrintIds(String orderPrintIds) {
		this.orderPrintIds = orderPrintIds;
	}

	public Integer getUserThreadId() {
		return UserThreadId;
	}

	public void setUserThreadId(Integer userThreadId) {
		UserThreadId = userThreadId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getStartDateUndeliver() {
		return startDateUndeliver;
	}

	public void setStartDateUndeliver(String startDateUndeliver) {
		this.startDateUndeliver = startDateUndeliver;
	}

	public String getEndDateUndeliver() {
		return endDateUndeliver;
	}

	public void setEndDateUndeliver(String endDateUndeliver) {
		this.endDateUndeliver = endDateUndeliver;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getModuleString() {
		return moduleString;
	}

	public void setModuleString(String moduleString) {
		this.moduleString = moduleString;
	}

	public String getDateSign() {
		return dateSign;
	}

	public void setDateSign(String dateSign) {
		this.dateSign = dateSign;
	}

	public String getMarkPrint() {
		return markPrint;
	}

	public void setMarkPrint(String markPrint) {
		this.markPrint = markPrint;
	}

	public String getMarkPrintSend() {
		return markPrintSend;
	}

	public void setMarkPrintSend(String markPrintSend) {
		this.markPrintSend = markPrintSend;
	}

	public String getDateSignDelivered() {
		return dateSignDelivered;
	}

	public void setDateSignDelivered(String dateSignDelivered) {
		this.dateSignDelivered = dateSignDelivered;
	}

	public String getDateSignUnelivered() {
		return dateSignUnelivered;
	}

	public void setDateSignUnelivered(String dateSignUnelivered) {
		this.dateSignUnelivered = dateSignUnelivered;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Boolean getBindBranch() {
		return bindBranch;
	}

	public void setBindBranch(Boolean bindBranch) {
		this.bindBranch = bindBranch;
	}

	public String getModuleExpressString() {
		return moduleExpressString;
	}

	public void setModuleExpressString(String moduleExpressString) {
		this.moduleExpressString = moduleExpressString;
	}

	public String getIsSuccessToTaobao() {
		return isSuccessToTaobao;
	}

	public void setIsSuccessToTaobao(String isSuccessToTaobao) {
		this.isSuccessToTaobao = isSuccessToTaobao;
	}

	public String getCustomerCode2() {
		return customerCode2;
	}

	public void setCustomerCode2(String customerCode2) {
		this.customerCode2 = customerCode2;
	}

	public String getCustomerCode3() {
		return customerCode3;
	}

	public void setCustomerCode3(String customerCode3) {
		this.customerCode3 = customerCode3;
	}
}
