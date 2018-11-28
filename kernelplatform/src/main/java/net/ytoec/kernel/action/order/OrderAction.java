package net.ytoec.kernel.action.order;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.TempOrder;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dto.DtoOrderPrint;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.OrderExpressService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.PosttempService;
import net.ytoec.kernel.service.RegionService;
import net.ytoec.kernel.service.TempOrderService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.apache.struts2.json.annotations.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
/**
 * 订单action类<br>
 * 提供订单页面数据和后台交互的一个接口
 * 
 * @author ChenRen
 * @date 2011-8-1
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class OrderAction extends AbstractActionSupport {

	private Logger logger = LoggerFactory.getLogger(OrderAction.class);
	// === field ===
	@Inject
	private OrderService<Order> service;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private PosttempService<Posttemp> posttempService;
	@Inject
	private OrderExpressService<OrderExpress> orderExpService;
	@Inject
	private RegionService regionService;

	@Inject
	private EccoreSearchService eccoreSearchService;
	
	@Inject
	private TempOrderService<TempOrder> tempOrderService;

	private Order order;
	private List<Order> orderList;
	private List<Order> errorOrderList;	// 异常订单会放在这里返回到页面
	
	private List<Order> ufOrderList;	// 特殊运单
	private Integer uftotalNum = 0;
	private List<Order> thOrderList;	// 退货订单
	private String ufztotalFreight;
	
	private List<User> vipList;
	private List<UserThread> vipThreadList;
	/** 分仓用户-总数 */
	private List<User> fcUserList; 
	/** 分仓用户-已激活的 */
	private List<User> fcActivedUserList; 
	/** 分仓用户-未激活的 */
	private List<User> fcUNActivedUserList; 
	/** 运费模板 */
	private List<Posttemp> postList;
	
	//关键字取当前用户id+系统当前时间数
	private String tempKey;
	
	private User user = null;
	
	private Integer posttempIdJson;

	private static final long serialVersionUID = 4236432083531136430L;
	 private Integer shouNum;// = 10; // 每页显示的记录数 // 2011-09-06/ChenRen
	// 使用父对象的

	private Integer currentPage = 1;
	private Pagination<Order> pagination;
	private Pagination<EccoreSearchResultDTO> paginationDTO;
	/**
	 * 用户类型; 网点用户(1)/VIP用户(2)<br>
	 * 电子对账的页面会根据这个字段判是否生成VIP帐号的标签<br>
	 * 等用户登录session做好后，会给该字段设置值。目前默认1
	 */
	private String userType;
	private String starttime;
	private String endtime;
	/** VIP用户的Id主键 */
	private String vipId;
	private String customerCode;
	private int vipId2;
	/** 省 */
	private String prov;
	/** 市 */
	private String city;
	/** 区/县 */
	private String district;
	/** 首重 */
	private Float firstWeight;
	/** 超重 */
	private Float overWeight;
	/** 总运费 */
	private String totalFreight;
	/**
	 * 电子账单到处的数据大小限制 <br>
	 * 默认-1，表示使用系统自定义的阀值
	 */
	private int numLimit = -1;
	/** 统计方式： 运费模板(byPt); 手动设置地区(byCity) */
	private String calcType;
	/** 运费模板Id */
	private Integer posttempId;
	/** 电子对账/VIP账单导出功能的导出编码;<br>如果前台不传,后台默认UTF-8 */
	private String outputCode;
	
	private String json;
	private String json2;
	/** 打印状态 */
	private String status;
	/** 订单打印 */
	private List<DtoOrderPrint> dtoOrderPrintList;
	/** 订单Ids;","分隔多个 */
	private String oids;
	private String msg;
	/** 打印模板 */
	private List<OrderExpress> orderExpList;
	/** 打印模板的id */
	private String orderExpId;
	private String sendProv;
	private String asdhf;
	/**绑定店铺*/
	private List<User> bindUserList;
	/**绑定用户Id*/
	private Integer bindUserId = 0;
	/** 动作类型 */
	private String action;
	/** 分仓账号的用户id */
	private String fencangUserId;
	
	private String queryCondition; //  请输入买家姓名/买家电话/运单号
	
	// === action ===
	/**
	 * 跳转到电子对账页面
	 */
	public String toECAccount() {
		order = null;
		orderList = null;
		totalFreight = null;
		overWeight = null;
		firstWeight = null;
		vipId = null;
		starttime = DateUtil.dateArithmetic(new Date(),6);
		endtime = DateUtil.dateArithmetic(new Date(),0);
		posttempId = null;
		prov = null;
		city = null;
		district = null;
		calcType = "byPt";	// 默认选中 运费模板
		
		User currentUser = super.readCookieUser();
		Integer userId  = currentUser.getId();
		// 网点/网点财务
		String usertype = currentUser.getUserType();
		if(usertype !=null && ("2".equals(usertype) || "22".equals(usertype) || "23".equals(usertype) ) ) {
			//如果是子帐户取子帐户的父帐户
			if("22".equals(usertype) || "23".equals(usertype) ){
				userId = currentUser.getParentId();
			}
			
			currentUser.setFinancialManager("financialManager");
			vipThreadList = super.getZhiKeUser(currentUser);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		
		if ("12".equals(usertype) || "11".equals(usertype) || "13".equals(usertype) ) {
			userId = currentUser.getParentId();
		}
		
		postList = posttempService.getPosttempByUserId(userId, currentUser.getUserType());
		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		// 获取绑定店铺用户
		bindUserList = new ArrayList<User>();
		List<String> bindString = Resource.getBindedCustomerIdList(currentUser);
		for(String cur : bindString){
			User u = userService.getUserByCustomerId(cur);
			if(u!=null){
				if(StringUtils.isNotEmpty(u.getShopName())) 
					bindUserList.add(u);
			}
		}
		
		// 取分仓账号
		if ("4".equals(usertype) || "41".equals(usertype)) {
			this.buildFCUser(currentUser);
		}
		
		return "toECAccount";
	}
	
	/**
	 * 跳转到电子对账页面
	 */
	public String toECAccountIndex() {
		order = null;
		orderList = null;
		totalFreight = null;
		overWeight = null;
		firstWeight = null;
		vipId = null;
		starttime = DateUtil.dateArithmetic(new Date(),6);
		endtime = DateUtil.dateArithmetic(new Date(),0);
		posttempId = null;
		prov = null;
		city = null;
		district = null;
		calcType = "byPt";	// 默认选中 运费模板
		
		User currentUser = super.readCookieUser();
		Integer userId  = currentUser.getId();
		// 网点/网点财务
		String usertype = currentUser.getUserType();
		if(usertype !=null && ("2".equals(usertype) || "22".equals(usertype) || "23".equals(usertype) ) ) {
			//如果是子帐户取子帐户的父帐户
			if("22".equals(usertype) || "23".equals(usertype) ){
				userId = currentUser.getParentId();
			}
			vipThreadList = super.getZhiKeUser(currentUser);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		
		if ("12".equals(usertype) || "11".equals(usertype) || "13".equals(usertype) ) {
			userId = currentUser.getParentId();
		}
		
		postList = posttempService.getPosttempByUserId(userId, currentUser.getUserType());
		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		// 获取绑定店铺用户
		bindUserList = new ArrayList<User>();
		List<String> bindString = Resource.getBindedCustomerIdList(currentUser);
		for(String cur : bindString){
			User u = userService.getUserByCustomerId(cur);
			if(u!=null){
				if(StringUtils.isNotEmpty(u.getShopName())) 
					bindUserList.add(u);
			}
		}
		
		// 取分仓账号
		this.buildFCUser(currentUser);
		
		return "toECAccountIndex";
	}

	public String ecAccount() {
		// 获取当前登录系统的网点用户的VIP用户列表
		User currentUser = super.readCookieUser();
		// 网点/网点财务
		String usertype = currentUser.getUserType();
		if(usertype !=null && ("2".equals(usertype) || "22".equals(usertype)|| "23".equals(usertype) ) ) {
			currentUser.setFinancialManager("financialManager");
			vipThreadList = super.getZhiKeUser(currentUser);
		} else {
			vipList = super.getClientUser(currentUser);
		}
		postList = posttempService.getPosttempByUserId(currentUser.getId(), currentUser.getUserType());
		
		/**
		 * 获取绑定店铺用户
		 */
		bindUserList = new ArrayList<User>();
		for(String cur : Resource.getBindedCustomerIdList(currentUser)){
			User u = userService.getUserByCustomerId(cur);
			if(u!=null){
				if(u.getShopName()!=null && !("").equals(u.getShopName()))
					bindUserList.add(u);
			}
		}

		// 取分仓账号
		this.buildFCUser(currentUser);
		fcUNActivedUserList = Collections.EMPTY_LIST;	// 统计后不弹层
		request.setAttribute("date_range", null);
		return ecAccountList();
	}
	
	/**
	 * @2012-03-13/ChenRen
	 * <pre>
	 * 电子对账改造：
	 *   1.数据来源改成从缓存查
	 *   2.页面逻辑改动：
	 *   	不区分按地区、按模板对账。统一按模板对账，且不显示 选择模板 的select. 因为卖家只有一个运费模板
	 *   	默认就显示 地区select(不显示首重、超重). 如果选择了地区就只查询某一个区域的订单. 否则查询所有地区
	 * </pre>
	 */
	private String ecAccountList() {
		
		User cuser = super.readCookieUser();
		String usertype = cuser.getUserType();
		Posttemp sysPt = null;
		// 如果用户没有被网点分配模板，系统就会分配一个系统模板。系统模板Id=0
		if(posttempId==null ||posttempId == 0) {
			if("2".equals(usertype) || "22".equals(usertype) || "23".equals(usertype)){
				List<User> userList = userService.searchUsersByCodeTypeState(customerCode, "1", "1");
				if(userList!=null && userList.size()>0)
					vipId = userList.get(0).getId().toString();
				else{
					pagination = new Pagination(currentPage, super.pageNum);
					pagination.setTotalRecords(0);
					return "toECAccount";
				}
			}
			else {
				vipId = cuser.getId().toString();
			}
			// 根据卖家Id读取系统模板(的运费信息)
			sysPt = posttempService.getSysPosttempByVipId(Integer.parseInt(vipId));	
		}
		
		// 对账
		paginationDTO = new Pagination(currentPage, super.pageNum);
		
		Map resultMap = service.ecAccountList(this.buildEcAccountParam(cuser), paginationDTO, posttempId, sysPt);
		
		//设置发件省份
		if(sysPt!=null) {
			sendProv=sysPt.getPostinfoList().get(0).getSrcId();
		}
		else {
			if(!resultMap.isEmpty())
				sendProv=resultMap.get("srcId").toString();
		}
		
		pagination = new Pagination(currentPage, super.pageNum);
		int count = 0;
		if(!resultMap.isEmpty()){
			count = NumberUtils.toInt(ObjectUtils.toString(resultMap.get("totalRecords"), null), 0);
			orderList = (List<Order>) resultMap.get("list");
			totalFreight = ObjectUtils.toString(resultMap.get("totalFreight"), "0");
			Object o = resultMap.get("errorOrderList");
			errorOrderList = o == null ? null : (List<Order>)o ;
			//关键字取当前用户id+系统当前时间数
			tempKey = cuser.getId().toString()+Long.valueOf((new Date().getTime())).toString();
			//将异常订单插入临时库
			addInTempOrder(errorOrderList, 1, tempKey);
			ufOrderList = (List<Order>) resultMap.get("specialOrderList");
			//将特殊订单插入临时库
			addInTempOrder(ufOrderList, 2, tempKey);
			uftotalNum=(Integer) resultMap.get("specialTotalNum");
			thOrderList = (List<Order>) resultMap.get("returnedOrderList");
			//将退货订单插入临时库
			addInTempOrder(thOrderList, 3, tempKey);
			ufztotalFreight = ObjectUtils.toString(resultMap.get("specialTotalFreight"), StringUtils.EMPTY);
		}
		pagination.setTotalRecords(count);
		return "toECAccount";
	}
	
	/**
	 * 分别将异常订单、调整订单和退货订单加入订单临时表，方便用户获取
	 * @param orderList
	 * @param currentUser
	 * @param tempType 1、代表异常订单2、代表调整订单3、代表退货订单
	 */
	private void addInTempOrder(List<Order> orderList, Integer tempType, String tempKey){
		for(Order order : orderList){
			TempOrder to = new TempOrder();
			to.setOrderId(order.getId());
			to.setTempKey(tempKey);
			to.setTempType(tempType);
			if(order.getRemark()!=null)
				to.setBackup(order.getRemark());
			boolean b = tempOrderService.add(to);
			if(!b)
				logger.error("插入异常订单、调整订单和退货订单时报错！插入失败的订单id是"+order.getId());
		}
	}
	
	/**
	 * 电子对账导出
	 * @return
	 */
	public String exportOrder() {
		HttpServletResponse response = null;
		PrintWriter out = null;
		try {
			response = ServletActionContext.getResponse();
			User cuser = super.readCookieUser();
			String usertype = cuser.getUserType();
			Posttemp sysPt = null;
			// 如果用户没有被网点分配模板，系统就会分配一个系统模板。系统模板Id=0
			if(posttempId == 0) {
				if("2".equals(usertype) || "22".equals(usertype) || "23".equals(usertype)){
					List<User> userList = userService.searchUsersByCodeTypeState(customerCode, "1", "1");
					vipId = userList.get(0).getId().toString();
				}
				else {
					vipId = cuser.getId().toString();
				}
				// 根据卖家Id读取系统模板(的运费信息)
				sysPt = posttempService.getSysPosttempByVipId(Integer.parseInt(vipId));	
			}
			// 对账
			String x = service.output2csv3(this.buildEcAccountParam(cuser), posttempId, sysPt,customerCode);
			String fileName = DateUtil.toShortDay(new SimpleDateFormat("yyyy-MM-dd").parse(starttime)) + "-" + DateUtil.toShortDay(new SimpleDateFormat("yyyy-MM-dd").parse(endtime)) +"运费表";
			response.setHeader("Content-Disposition", "attachment; filename="  + new String(fileName.getBytes("GB2312"),"ISO8859-1")+".csv");
			outputCode = StringUtils.defaultIfEmpty(outputCode, CharEncoding.UTF_8);
			response.setContentType("application/ms-excel;charset="+outputCode);
			response.setCharacterEncoding(outputCode); // GBK
			out = response.getWriter();
			out.print(x);
		} catch (Exception e) {
			logger.error("#电子对账 #导出 导出异常", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
		return null;
	}
	
	/**
	 * 查看详情
	 */
	public String orderDetails() {
		order = service.getOrderByMailNo(order);
		return "toOrderDetails";
	}
	
	/**
	 * 查看详情<br> 返回json数据
	 */
	public String orderDetailsJSON() {
		order = service.getOrderByMailNo(order);
		try {
			json = JSONUtil.serialize(order);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "toOrderDetailsJSON";
	}
	
	//运单监控中传入的运单号。
	private String mailNo;
	
	/**
	 * 运单监控中查看订单详情<br>
	 * @return
	 */
	public String orderMonitorJson() {
		// order = service.getOrderByMailNo(mailNo);
		Map params = new HashMap();
		String customerIDs = "";

		User currentUser = super.readCookieUser();
		vipThreadList = super.getZhiKeUser(currentUser);

		for (UserThread uh : vipThreadList) {
			if (StringUtils.isNotBlank(uh.getUserCode())) {
				User user = new User();
				user.setUserCode(uh.getUserCode());
				List<User> user_list = userService.searchUsersByCode(user);

				for (User us : user_list) {
					if (customerIDs.equals("")) {
						customerIDs += us.getTaobaoEncodeKey();
					} else {
						customerIDs += "," + us.getTaobaoEncodeKey();
					}
				}

			}
		}


		params.put("mailNo", mailNo);
		params.put("customerIDs", customerIDs);

		paginationDTO = new Pagination<EccoreSearchResultDTO>(1, pageNum);
		paginationDTO.setParams(params);

		try {
			eccoreSearchService.searchWayBillData(ConfigUtilSingle
					.getInstance().getSolrEccoreUrl(), paginationDTO);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			if (paginationDTO.getRecords().size() > 0) {
				//设置客户编码和客户名称
				EccoreSearchResultDTO result = paginationDTO.getRecords().get(0);
				String customerId = result.getCustomerId();
				User u = Resource.getUserByCustomerId(customerId);
				if(u!=null && StringUtils.isNotEmpty(u.getUserCode()))
					result.setUserCode(u.getUserCode());
				json = JSONUtil.serialize(result);
			} else {
				json = null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "orderMonitorJson";
	}
	
	public String getPosttempByVipJSON() {
		if(customerCode==null||customerCode.equals("")){
		}else{
			UserThread ut=new UserThread();
			ut.setUserCode(customerCode);
			List<UserThread> userList=userThreadService.searchUsersByCode(ut);
			if(userList!=null && userList.size()>0){
				vipId2 = userList.get(0).getId();
			}
		}
		if(vipId2!=0)
			postList = posttempService.getPosttempByUserId(vipId2, "1");
		if(postList!=null && postList.size()>0){
			posttempIdJson = postList.get(0).getId();
		}else
			posttempIdJson = 0;
		
		return "getPosttempByVipJSON";
	}
	
	/**
	 * 查看异常订单<br>
	 * 采用内存分页
	 * @return
	 */
	public String toErrorOrderView() {
		user = super.readCookieUser();
		if(!"4".equals(ObjectUtils.toString(super.readCookie("userType")) ) ) {
			if(sendProv.indexOf("*")>0)sendProv=sendProv.replace("*", "");//如果带有* 则是翻页是传过来的已经翻译的省份名  去掉*后 作为参数传到下一页继续使用
			else{
				Region region=(Region)regionService.getRegionById(Integer.valueOf(sendProv));
				if(region!=null)sendProv=region.getRegionName();
			}
		}
		pagination = new Pagination(currentPage, pageNum);
		//根据tempKey获取当前用户的临时订单列表
		List<TempOrder> tempOrderList = tempOrderService.getTempList(tempKey, 1, pagination);
		int totalRecords = tempOrderService.countTempList(tempKey, 1);
		if(tempOrderList!=null && !tempOrderList.isEmpty()){
			List<Integer> orderId = new ArrayList<Integer>();
			for(TempOrder to : tempOrderList){
				if(to.getOrderId()!=null)
					orderId.add(to.getOrderId());
			}
			errorOrderList = new ArrayList<Order>();
			List<Order> result = service.getOrderEccoreSearchByOrderIds(orderId, pagination, "desc");
			for(Order order : result){
				for(TempOrder to : tempOrderList){
					if(order.getId().equals(to.getOrderId())){
						order.setRemark(to.getBackup());
						break;
					}
				}
				if(order.getSprovCode()!=null){
					String sProvCodeName = Resource.getNameById(Integer.valueOf(order.getSprovCode()));
					order.setScity(sProvCodeName+order.getScity());
				}
				errorOrderList.add(order);
			}
		}
		pagination.setTotalRecords(totalRecords);
		return "toErrorOrderView";
	}
	/**
	 * 查看特殊运单<br>
	 * 采用内存分页
	 * 
	 * @return
	 */
	public String toufOrderView() {
		user = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		//根据tempKey获取当前用户的临时订单列表
		List<TempOrder> tempOrderList = tempOrderService.getTempList(tempKey, 2, pagination);
		int totalRecords = tempOrderService.countTempList(tempKey, 2);
		if(tempOrderList!=null && !tempOrderList.isEmpty()){
			List<Integer> orderId = new ArrayList<Integer>();
			for(TempOrder to : tempOrderList){
				if(to.getOrderId()!=null)
					orderId.add(to.getOrderId());
			}
			ufOrderList = new ArrayList<Order>();
			List<Order> result = service.getOrderEccoreSearchByOrderIds(orderId, pagination, "desc");
			for(Order order : result){
				for(TempOrder to : tempOrderList){
					if(order.getId().equals(to.getOrderId())){
						order.setRemark(to.getBackup());
						break;
					}
				}
				if(order.getSprovCode()!=null){
					String sProvCodeName = Resource.getNameById(Integer.valueOf(order.getSprovCode()));
					order.setScity(sProvCodeName+order.getScity());
				}
				ufOrderList.add(order);
			}
		}
		pagination.setTotalRecords(totalRecords);
		return "toufOrderView";
	}
	
	/**
	 * 查看退货运单<br>
	 * 采用内存分页
	 * 
	 * @return
	 */
	public String tothOrderView() {
		user = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		//根据tempKey获取当前用户的临时订单列表
		List<TempOrder> tempOrderList = tempOrderService.getTempList(tempKey, 3, pagination);
		int totalRecords = tempOrderService.countTempList(tempKey, 3);
		if(tempOrderList!=null && !tempOrderList.isEmpty()){
			List<Integer> orderId = new ArrayList<Integer>();
			for(TempOrder to : tempOrderList){
				if(to.getOrderId()!=null)
					orderId.add(to.getOrderId());
			}
			thOrderList = new ArrayList<Order>();
			List<Order> result = service.getOrderEccoreSearchByOrderIds(orderId, pagination, "desc");
			for(Order order : result){
				for(TempOrder to : tempOrderList){
					if(order.getId().equals(to.getOrderId())){
						order.setRemark(to.getBackup());
						break;
					}
				}
				if(order.getSprovCode()!=null){
					String sProvCodeName = Resource.getNameById(Integer.valueOf(order.getSprovCode()));
					order.setScity(sProvCodeName+order.getScity());
				}
				thOrderList.add(order);
			}
		}
		pagination.setTotalRecords(totalRecords);
		return "tothOrderView";
	}
	
	// 查找运单
	public String toQueryOrder() {
		User currentUser = super.readCookieUser();
		String usertype = currentUser.getUserType();
		// 网点/网点财务/财务&客服
		if(usertype !=null && ("2".equals(usertype) || "22".equals(usertype)|| "23".equals(usertype) ) ) {
			vipThreadList = super.getZhiKeUser(currentUser);	// 客户
		} else {
			vipList = super.getClientUser(currentUser);
		}
		
		Date date = new Date();
		starttime = DateUtil.toDay(DateUtil.previousSevenDate(date));//上7天
		endtime = DateUtil.toDay(date);

		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		
		status = "NOPRINT";	// 默认未打印
		// 打印模板
		orderExpList = orderExpService.getOrderExpressListbyStoreId(currentUser.getId(), null, false);	
		
		return "toQueryOrder";
	}
	
	// 查找运单
	public String queryOrder() {
		User cuser = super.readCookieUser();
		String usertype = cuser.getUserType();
		// 网点/网点财务/财务&客服
		if(usertype !=null && ("2".equals(usertype) || "22".equals(usertype)|| "23".equals(usertype) ) ) {
			vipThreadList = super.getZhiKeUser(cuser);
		} else {
			vipList = super.getClientUser(cuser);
		}

		Map map = new Hashtable();
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		
//		if("NOPRINT".equalsIgnoreCase(status) || "PRINTED".equalsIgnoreCase(status)) {
		if(StringUtils.isNotBlank(status) && !"all".equals(status)) {
			map.put("status", status);
		}
		
		if("1".equals(cuser.getUserType()) || "12".equals(cuser.getUserType()) || "13".equals(cuser.getUserType())) {

			// >>
			/*
			 * @2012-03-10/ChenRen
			 * 电子对账页面(ecAccount.jsp)新增店铺筛选.
			List relatedId=Resource.getBindedCustomerIdList(cuser);
			map.put("relatedId", relatedId);
			*/

			/**
			 * @2012-03-10/ChenRen
			 * 电子对账页面(ecAccount.jsp)新增店铺筛选.
			 * 
			 * 此处需判断如果选择了店铺就不用查询所有的绑定店铺.
			 * 根据用户id获取用户custormerId存于bindedId中
			 */
			List<String> bindedIdList = new ArrayList<String>();
			if(bindUserId != null && bindUserId !=0 ) {
				User bindUser = userService.getUserById(bindUserId);
				if(bindUser != null) {
					bindedIdList.add(bindUser.getTaobaoEncodeKey());
				}
			}else{
				bindedIdList = Resource.getBindedCustomerIdList(cuser);
			}
			map.put("relatedId", bindedIdList);
			//map.put("vipId", vipId);
		}
		if("2".equals(cuser.getUserType()) || "22".equals(cuser.getUserType()) || "23".equals(cuser.getUserType())) {
			map.put("userCode", customerCode);
			
			// 卖家客户编码修改时间
			Date ucUpdateTime = queryUserCodeUpdateTime(customerCode);
			if(ucUpdateTime != null) {
				map.put("ucUpdateTime", ucUpdateTime);
			}
		}
		
		//map = service.queryOrderPrint(map, pagination);
		pagination = new Pagination(currentPage, shouNum);//super.pageNum
		pagination.setTotalRecords(0);
		dtoOrderPrintList = service.queryOrderPrint(map, pagination);
		// 打印模板
		orderExpList = orderExpService.getOrderExpressListbyStoreId(cuser.getId(), null, false);	

		return "toQueryOrder";
	}
	
	// 跳转到订单打印页面
	public String toOrderPrint() {

		Map map = new Hashtable();
		map.put("oids", oids);				// 订单ids
		map.put("orderExpId", orderExpId);	// 模板id

		map = service.queryOrderPrintData(map);
		
		// xml , orderNum,data,bg
		request.setAttribute("xml", map.get("xml"));
		request.setAttribute("orderNum", map.get("orderNum"));
		request.setAttribute("data", map.get("data"));
		request.setAttribute("bg", map.get("bg"));
		
		return "toOrderPrint";
	}
	
	/**
	 * 电子对账({@link OrderAction#ecAccount3()} )和导出({@link OrderAction#output2csv3()} )
	 * 都有相同的创建参数的部分,这里提取出来
	 * @param user
	 */
	private Map buildEcAccountParam(User user) {

		String usertype = user.getUserType();
		Map map = new Hashtable();
		map.put("startDate", starttime); 
		map.put("endDate", endtime);
		map.put("prov", prov);
		map.put("city", city);
		map.put("district", district);
		map.put("usertype", usertype);  
		
		if(StringUtils.isNotEmpty(prov)) {
			map.put("numProv", prov);
		}
		if(StringUtils.isNotEmpty(city)) {
			map.put("numCity", city);
		}
		if(StringUtils.isNotEmpty(district)) {
			map.put("numDistrict", district);
		}
		String mailNO = "",buyerPhone = "",buyerName = "";
		if (queryCondition != null && !("").equals(queryCondition) && !("请输入买家姓名/买家电话/运单号").equals(queryCondition)) {
			if (queryCondition.trim().length() == 10) {
				if (StringUtil.isSingleMail(queryCondition)) {
					mailNO = queryCondition.trim();
				} else {
					buyerPhone = queryCondition.trim();
				}
			} else if (StringUtil.isPhone(queryCondition)
					|| StringUtil.isMobile(queryCondition)) {
				buyerPhone = queryCondition.trim();
			} else
				buyerName = queryCondition.trim();
		}
		if(StringUtils.isNotEmpty(mailNO)) {
			map.put("mailNo", mailNO);
		}
		if(StringUtils.isNotEmpty(buyerPhone)) {
			map.put("phone", buyerPhone);
		}
		if(StringUtils.isNotEmpty(buyerName)) {
			map.put("name", buyerName);
		}
		// 网点
		if("2".equals(usertype) || "21".equals(usertype) || "22".equals(usertype) || "23".equals(usertype)) {
			map.put("userCode", customerCode);
			// 用户编码修改时间
			Date ucUpdateTime = queryUserCodeUpdateTime(customerCode);
			if(ucUpdateTime != null) {
				map.put("ucUpdateTime", ucUpdateTime);
			}
		}
		// 卖家
		if ("1".equals(usertype) || "11".equals(usertype) || "12".equals(usertype) || "13".equals(usertype)) {
			// 店铺刷选
			List<String> customerIdList = new ArrayList<String>();
			// 如果选择了店铺 && 当前操作类型是 shopFitler (店铺过滤, jsp页面点击店铺的时候设置的)
			if(bindUserId != null && bindUserId !=0 && "shopFitler".equalsIgnoreCase(action)) {
				User bindUser = userService.getUserById(bindUserId);	// bindUserId是对应的店铺的用户的id
				if(bindUser != null) {
					customerIdList.add(bindUser.getTaobaoEncodeKey());
				}
			}else{	// 查询所有店铺
				customerIdList = Resource.getBindedCustomerIdList(user);
			}
			if(customerIdList.size() < 1) {
				// @2012-04-03/ChenRen
				// 在solr中查数据的时候是根据时间查询，然后采用customerId过滤. 如果customerId不存在, 就会返回按时间查询的结果集
				// 如果#用户 没有#分仓，就‘进入’了对账.应该返回空结果集
				customerIdList.add("0.0");
			}
			map.put("relatedId", customerIdList);
		}
		// 平台用户
		// 平台用户只查分仓账号的数据. 不查其关联店铺
		// 分仓用户对账的时候要查关联店铺(分仓用户其实就是一个)
		if("4".equals(usertype)) {
			List<User> fcuserList = new ArrayList<User>();
			List<String> customerIdList = new ArrayList<String>();
			// 所有分仓
			if("all".equalsIgnoreCase(fencangUserId)) {
				if(fcActivedUserList == null) {
					this.buildFCUser(user);	// 取分仓账号
				}
				for (User fcUser : fcActivedUserList) {	// fencangUserList 在方法 ecAccount 里有赋值
					if(StringUtils.isEmpty(fcUser.getTaobaoEncodeKey())) {
						logger.warn("分仓用户的customerId/taobaoEncodeKey为空,将不会纳入电子对账. 用户Id:"+fcUser.getId());
						continue;
					}
					fcuserList.add(fcUser);
					customerIdList.add(fcUser.getTaobaoEncodeKey());
				}
			}
			else {
				User dbuser = userService.getUserById(Integer.parseInt(fencangUserId));
				fcuserList.add(dbuser);
				customerIdList.add(dbuser.getTaobaoEncodeKey());
			}
			if(customerIdList.size() < 1) {
				// @2012-04-03/ChenRen
				// 在solr中查数据的时候是根据时间查询，然后采用customerId过滤. 如果customerId不存在, 就会返回按时间查询的结果集
				// 如果#用户 没有#分仓，就‘进入’了对账.应该返回空结果集
				customerIdList.add("0.0");
			}
			map.put("fcuserList", fcuserList);		// 用户对象会用来取用户模板
			map.put("relatedId", customerIdList);	// relatedId用来查数据
		}
		return map;
	}
	
	// 发货
	public String toSendOrder() {
		Map map = new Hashtable();
		map.put("oids", oids);
		
		boolean flag = service.toSendOrder(map);
		msg = flag ? "success" : "error";
//		request.setAttribute("msg", msg);
		
		return "json";
	}
	
	/**
	 * 查询各种类型的分仓用户
	 */
	private void buildFCUser(User currentUser) {
		fcUserList = userService.pingTaiSelect(currentUser, 1);	// 取分仓用户
		
		fcActivedUserList = new ArrayList<User>();
		fcUNActivedUserList = new ArrayList<User>();
		for (User fcUser : fcUserList) {
			if("1".equals((fcUser.getUserState())) ){
				fcActivedUserList.add(fcUser);
			}
			else if("TBA".equalsIgnoreCase((fcUser.getUserState())) ){
				fcUNActivedUserList.add(fcUser);
			}
		}
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	// === getter && setter ===
	@JSON(serialize=false)
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@JSON(serialize=false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@JSON(serialize=false)
	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	@JSON(serialize=false)
	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	@JSON(serialize=false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@JSON(serialize=false)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@JSON(serialize=false)
	public List<Order> getOrderList() {
		return orderList;
	}

	public void setNumLimit(int numLimit) {
		this.numLimit = numLimit;
	}

	@JSON(serialize=false)
	public float getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(float firstWeight) {
		this.firstWeight = firstWeight;
	}

	@JSON(serialize=false)
	public float getOverWeight() {
		return overWeight;
	}

	public void setOverWeight(float overWeight) {
		this.overWeight = overWeight;
	}

	@JSON(serialize=false)
	public String getTotalFreight() {
		return totalFreight;
	}

	public void setTotalFreight(String totalFreight) {
		this.totalFreight = totalFreight;
	}

	@JSON(serialize=false)
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	@JSON(serialize=false)
	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@JSON(serialize=false)
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	@JSON(serialize=false)
	public Pagination<Order> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<Order> pagination) {
		this.pagination = pagination;
	}

	@JSON(serialize=false)
	public List<User> getVipList() {
		return vipList;
	}

	@JSON(serialize=false)
	public List<Posttemp> getPostList() {
		return postList;
	}
	
	@JSON(serialize=false)
	public Integer getPosttempId() {
		return posttempId;
	}

	public void setPosttempId(Integer posttempId) {
		this.posttempId = posttempId;
	}

	public void setOutputCode(String outputCode) {
		this.outputCode = outputCode;
	}

	@JSON(serialize=false)
	public String getCalcType() {
		return calcType;
	}

	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

//	@JSON(serialize=false)
	public String getJson() {
		return json;
	}

	public String getJson2() {
		return json2;
	}

	public int getVipId2() {
		return vipId2;
	}

	public void setVipId2(int vipId2) {
		this.vipId2 = vipId2;
	}

	public List<Order> getErrorOrderList() {
		return errorOrderList;
	}

	public void setErrorOrderList(List<Order> errorOrderList) {
		this.errorOrderList = errorOrderList;
	}

	public List<UserThread> getVipThreadList() {
		return vipThreadList;
	}

	public void setVipThreadList(List<UserThread> vipThreadList) {
		this.vipThreadList = vipThreadList;
	}

	public List<Order> getUfOrderList() {
		return ufOrderList;
	}

	public void setUfOrderList(List<Order> ufOrderList) {
		this.ufOrderList = ufOrderList;
	}

	public Integer getUftotalNum() {
		return uftotalNum;
	}

	public void setUftotalNum(Integer uftotalNum) {
		this.uftotalNum = uftotalNum;
	}

	public List<Order> getThOrderList() {
		return thOrderList;
	}

	public void setThOrderList(List<Order> thOrderList) {
		this.thOrderList = thOrderList;
	}


	public String getUfztotalFreight() {
		return ufztotalFreight;
	}

	public void setUfztotalFreight(String ufztotalFreight) {
		this.ufztotalFreight = ufztotalFreight;
	}

	/**
	 * 查找同一用户编码下已激活的卖家的custormerId
	 * @param userCode
	 * @return
	 */
	private List getCustomerIdByUserCode(String userCode){
		List list = new ArrayList();
		List<User> userList = userService.searchUsersByCodeTypeState(userCode, "1", "1");//查找同一用户编码下已激活的买家
		if(userList!=null && userList.size()>0){
			for(User user : userList){
				if(user.getTaobaoEncodeKey()!=null && !(user.getTaobaoEncodeKey().equals("")))
					list.add(user.getTaobaoEncodeKey());
			}
		}else{
			return null;
		}
		if(list.size()>0)
			return list;
		else
			return null;
	}
	
	/**
	 * 如果当前用户是网点，就返回客户编码修改的时间戳。
	 * 否则返回 null
	 * @param userCode 	选择用户的的用户编码
	 * @return
	 */
	private Date queryUserCodeUpdateTime(String userCode) {
		//User cu = (User) session.get(Constants.SESSION_USER);
		User cu = super.readCookieUser();
		if("2".equals(cu.getUserType() ) 
			|| "21".equals(cu.getUserType() ) 
			|| "22".equals(cu.getUserType() ) 
			|| "23".equals(cu.getUserType() ) 
			) {
			
			if(!StringUtil.isBlank(userCode)){
				UserThread uh = new UserThread();
				uh.setUserCode(userCode);
				List<UserThread> uhList = userThreadService.searchUsersByCode(uh);
				
				if(uhList.size() > 0) return uhList.get(0).getUserCodeUpteTime();
			}
		}
		return null;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DtoOrderPrint> getDtoOrderPrintList() {
		return dtoOrderPrintList;
	}

	public void setDtoOrderPrintList(List<DtoOrderPrint> dtoOrderPrintList) {
		this.dtoOrderPrintList = dtoOrderPrintList;
	}

	public String getOids() {
		return oids;
	}

	public void setOids(String oids) {
		this.oids = oids;
	}

	public Integer getShouNum() {
		return shouNum;
	}

	public void setShouNum(Integer shouNum) {
		this.shouNum = shouNum;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<OrderExpress> getOrderExpList() {
		return orderExpList;
	}

	public void setOrderExpList(List<OrderExpress> orderExpList) {
		this.orderExpList = orderExpList;
	}

	public String getOrderExpId() {
		return orderExpId;
	}

	public void setOrderExpId(String orderExpId) {
		this.orderExpId = orderExpId;
	}

	public String getSendProv() {
		return sendProv;
	}

	public void setSendProv(String sendProv) {
		this.sendProv = sendProv;
	}

	public String getAsdhf() {
		return asdhf;
	}

	public void setAsdhf(String asdhf) {
		this.asdhf = asdhf;
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
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setBindUserId(Integer bindUserId) {
		this.bindUserId = bindUserId;
	}

	public String getFencangUserId() {
		return fencangUserId;
	}

	public void setFencangUserId(String fencangUserId) {
		this.fencangUserId = fencangUserId;
	}

	public List<User> getFcUserList() {
		return fcUserList;
	}

	public void setFcUserList(List<User> fcUserList) {
		this.fcUserList = fcUserList;
	}

	public List<User> getFcActivedUserList() {
		return fcActivedUserList;
	}

	public void setFcActivedUserList(List<User> fcActivedUserList) {
		this.fcActivedUserList = fcActivedUserList;
	}

	public List<User> getFcUNActivedUserList() {
		return fcUNActivedUserList;
	}

	public void setFcUNActivedUserList(List<User> fcUNActivedUserList) {
		this.fcUNActivedUserList = fcUNActivedUserList;
	}

	public String getTempKey() {
		return tempKey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setTempKey(String tempKey) {
		this.tempKey = tempKey;
	}

	public Integer getPosttempIdJson() {
		return posttempIdJson;
	}

	public void setPosttempIdJson(Integer posttempIdJson) {
		this.posttempIdJson = posttempIdJson;
	}
	public String getQueryCondition() {
		return queryCondition;
	}
	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}

}
