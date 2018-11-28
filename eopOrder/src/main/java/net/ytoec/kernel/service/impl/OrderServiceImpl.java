package net.ytoec.kernel.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.order.QueryOrder;
import net.ytoec.kernel.action.remote.process.XmlBuildProcessor;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.BuildSearchStatusDaoIndexPressure;
import net.ytoec.kernel.dao.BuildSearchStatusVersionDao;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dao.JgWaybillDao;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dao.OrderExpressDao;
import net.ytoec.kernel.dao.OrderLogDao;
import net.ytoec.kernel.dao.OrderPrintDao;
import net.ytoec.kernel.dao.PosttempDao;
import net.ytoec.kernel.dao.PosttempUserDao;
import net.ytoec.kernel.dao.PrintInvoiceDao;
import net.ytoec.kernel.dao.ProductDao;
import net.ytoec.kernel.dao.QuestionnaireDao;
import net.ytoec.kernel.dao.SendTaskDao;
import net.ytoec.kernel.dao.SendTaskMailNoDao;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dao.UnlikeFreightDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.OrderPrint;
import net.ytoec.kernel.dataobject.Postinfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.dataobject.PrintInvoice;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.UnlikeFreight;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dto.DtoOrderPrint;
import net.ytoec.kernel.dto.GoodInfo;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.dto.TabUniteOrder;
import net.ytoec.kernel.mapper.DbExeMapper;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.JgWaybillService;
import net.ytoec.kernel.service.OrderPrintService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.PosttempService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.SendTaskToTBService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.service.helper.EcAccountHelper;
import net.ytoec.kernel.service.helper.OrderOperateHelper;
import net.ytoec.kernel.service.helper.OrderWeightUpdateHelper;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;
import net.ytoec.kernel.util.XmlUtil;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单业务接口实现类
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@Service
@Transactional
@SuppressWarnings("all")
public class OrderServiceImpl<T extends Order> implements OrderService<T>,
		SessionAware {

	private static Logger logger = LoggerFactory
			.getLogger(OrderServiceImpl.class);
	@Inject
	private OrderDao<T> dao;
	@Inject
	private OrderExpressDao<OrderExpress> orderExpressDao;

	@Inject
	private TraderInfoDao<TraderInfo> traderDao;

	@Inject
	private ProductDao<Product> productDao;

	@Inject
	private JgWaybillDao<JgWaybill> jgWaybillDao;

	@Inject
	private QuestionnaireDao<Questionnaire> questionnaireDao;

	@Inject
	private PosttempDao<Posttemp> posttempDao;
	@Inject
	private PosttempService<Posttemp> posttempService; // - - 好乱

	@Inject
	private PosttempUserDao<PosttempUser> posttempUserDao;

	@Inject
	private UserService<User> userService;

	@Inject
	private UserThreadService<UserThread> userThreadService;

	@Inject
	private UnlikeFreightDao<UnlikeFreight> unlikeFreightDao;
	@Inject
	private JgWaybillService<JgWaybill> jgWaybillService;

	@Inject
	private EccoreSearchService eccoreSearchService;
	private Object totalRecords;

	@Inject
	private SendTaskToTBDao<SendTaskToTB> sendTaskToTBDao;
	
	@Inject
	private BuildSearchStatusWeightIndexDao<BuildSearchStatusWeightIndex> BuildSearchStatusWeightIndexDao;
	@Inject
	private SendTaskService<SendTask> sendTaskService;

	@Inject
	private OrderPrintService<OrderPrint> orderPrintService;

	@Inject
	private OrderPrintDao<OrderPrint> orderPrintDao;

	@Inject
	private OrderLogDao orderLogDao;

	@Inject
	private PrintInvoiceDao<PrintInvoice> invoiceDao;

	@Inject
	private BuildSearchDao buildSearchDao;

	@Autowired
	private SendTaskDao<T> sendTask;

	@Inject
	private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;

	@Inject
	private BuildSearchStatusDaoIndexPressure<BuildSearchStatus> buildSearchStatusDaoIndexPressure;

	@Inject
	private BuildSearchStatusVersionDao<BuildSearchStatus> buildSearchStatusVersionDao;

	public static String ORDER_UPDATE = "UPDATE";
	public static String ORDER_ORDERCREATE = "ORDERCREATE";

	// 消息内容
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";

	// 消息签名
	private static final String DATA_DIGEST_PARAM = "data_digest";

	// 消息类型
	private static final String MSG_TYPE = "msg_type";

	// TP编号
	private static final String DATA_LOGISTIC_PROVIDER_PARAM = "logistic_provider_id";
	private static final String DATA_YTO = "YTO";
	private static final String DATA_TAOBAO = "TAOBAO";
	
	// 订单重量字段
	private static final String WEIGHT = "weight";
	
	// 订单面单号字段
	private static final String MAILNO = "mailNo";
	
	// 订单状态
	private static final String STATUS = "status";
	 
	private static final int constant = 2;
	// @Inject
	// private ITransmissionService transmissionService;

	@Inject
	private SendTaskMailNoDao<SendTask> sendTaskMailNoDao;

	private Map<String, Object> sessionMap = new HashMap<String, Object>();

	/** 导出数据量的大小；如果前台有传递该值，并且不是-1；就是用前台传递的值；如果是-1，就使用系统自定义值 */
	private static int numLimit = 10000;

	/**
	 * 订单状态Map<br>
	 * key对应订单在数据库中的状态英文描述，value对应状态的中文显示值
	 */
	private static Map<String, String> orderStatusMap;
	/**
	 * 订单的默认状态：接单<br>
	 * 如果根据状态编码{@link Order#getStatus()}在{@link #orderStatusMap}取不到值，就是用默认状态
	 */
	private static String ORDER_STATUS_DEF = "接单中";
	static {
		orderStatusMap = new HashMap<String, String>();
		orderStatusMap.put("CONFIRM", "等待确认");
		orderStatusMap.put("0", "接单中");
		orderStatusMap.put("SENT_SCAN", "派件扫描");
		orderStatusMap.put("TRACKING", "流转信息");
		orderStatusMap.put("ACCEPT", "接单");
		orderStatusMap.put("UNACCEPT", "不接单");
		orderStatusMap.put("GOT", "揽收成功");
		orderStatusMap.put("NOT_SEND", "揽收失败");
		orderStatusMap.put("FAILED", "失败");
		orderStatusMap.put("SIGNED", "已签收");
		orderStatusMap.put("WITHDRAW", "订单取消");
	}

	@Override
	public boolean addOrder(T entity) {

		if (entity == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return false;
		}

		return dao.addOrder(entity);
	}

	@Override
	public boolean addOrderFormInfo(OrderFormInfo orderFormInfo)
			throws DuplicateKeyException, DataAccessException {
		if (orderFormInfo == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return false;
		}
		try {
			/**
			 * 将订单的收货方和发货方中联系人手机号码的开头和结尾的非数字字符去掉
			 * ADD BY wangpengfei 2013-11-07
			 */
			/**-----------------START------------------------*/
			if(StringUtils.isNotEmpty(orderFormInfo.getSender().getMobile())){
				orderFormInfo.getSender().setMobile(StringUtil.replaceNonNumber(orderFormInfo.getSender().getMobile()));
			}
			if(StringUtils.isNotEmpty(orderFormInfo.getReceiver().getMobile())){
				orderFormInfo.getReceiver().setMobile(StringUtil.replaceNonNumber(orderFormInfo.getReceiver().getMobile()));
			}
			/**-----------------END------------------------*/
			
			
			// 向 ec_core_log标准插入物流号，来判断是否重复
			logger.info("step1");
			this.orderLogDao.addOrderLog(orderFormInfo.getTxLogisticId());
			logger.info("step2");

			// 订单实体类
			Order order = new Order();

			// 收件人实体类
			TraderInfo receiver = orderFormInfo.getReceiver();

			// 发件人实体类
			TraderInfo sender = orderFormInfo.getSender();

			// 获取商品信息
			List<Product> products = orderFormInfo.getItems();

			// 新增订单数据
			logger.info("step3");
			this.addOrderMethod(orderFormInfo, order, sender, receiver,
					products);
			logger.info("step4");

			// 我要发货不走这里
			if (StringUtils.equals(orderFormInfo.getStatus(), "2")) {
				boolean isPrint = Resource.getIsPrint(order.getClientId());
				if (isPrint
						|| Order.ONLINE_TYPE
								.equals(orderFormInfo.getLineType())) {

					// 面单打印操作
					this.addOrderPrintMethod(order, orderFormInfo, receiver,
							sender, products);
				}
			}
			
			// 订单操作
			String lineType = null;
			
				// 电商平台是淘宝的场合 
			if(StringUtils.equalsIgnoreCase(orderFormInfo.getClientId(), DATA_TAOBAO)){
				lineType = orderFormInfo.getLineType();
			}
			else{
				lineType = Resource.getLineType(StringUtils
						.upperCase(orderFormInfo.getClientId()));
				if(StringUtils.isEmpty(lineType)){
						lineType = orderFormInfo.getLineType();
					}else{
						orderFormInfo.setLineType(lineType);
					}
			}
			logger.info("addJgWaybillMethod condition...lineType:" + lineType);
			
			// 线下订单场合
			if (!orderFormInfo.isFromJGCreate()&&Order.OFFLINE_TYPE.equals(lineType)
					&& !StringUtils.equalsIgnoreCase("ytotest88888888",
							orderFormInfo.getRemark())) {
				logger.info("addJgWaybillMethod start...");
				// 仓配通的客户，都使用CANGPEITONG这个渠道和金刚交互
				Channel channel = Resource.initChannel(orderFormInfo
						.getClientId());
				if (channel != null && "1".equals(channel.getUserType())) {
					orderFormInfo.setClientId("CANGPEITONG");
				}
				// jgwaybill表中插入记录
				this.addJgWaybillMethod(order, orderFormInfo);
				logger.info("addJgWaybillMethod end...");
				
			}
			
			// 线上订单场合
			else if(Order.ONLINE_TYPE.equals(lineType) && !StringUtils.equalsIgnoreCase("ytotest88888888",
					orderFormInfo.getRemark())){
				
				// 判断是否面单打印
				boolean isPrint = Resource.getIsPrint(orderFormInfo.getClientId());
				if (isPrint
						|| Order.ONLINE_TYPE.equals(orderFormInfo.getLineType())) {

					// 面单打印操作
					this.addOrderPrintMethod(order, orderFormInfo, receiver,sender, products);
				}
			}

		} catch (DuplicateKeyException e) {
			logger.error("create orderPrint fail 主键重复,物流号："
					+ orderFormInfo.getTxLogisticId());
		}
		return true;
	}

	@Override
	public Order addOrderFormInfoForTB(OrderFormInfo orderFormInfo) throws Exception {

		if (orderFormInfo == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return null;
		}
		try {
			/**
			 * 将订单的收货方和发货方中联系人手机号码的开头和结尾的非数字字符去掉
			 * ADD BY wangpengfei 2013-11-07
			 */
			/**-----------------START------------------------*/
			if(StringUtils.isNotEmpty(orderFormInfo.getSender().getMobile())){
				orderFormInfo.getSender().setMobile(StringUtil.replaceNonNumber(orderFormInfo.getSender().getMobile()));
			}
			if(StringUtils.isNotEmpty(orderFormInfo.getReceiver().getMobile())){
				orderFormInfo.getReceiver().setMobile(StringUtil.replaceNonNumber(orderFormInfo.getReceiver().getMobile()));
			}
			/**-----------------END------------------------*/
			
			// 向 ec_core_log标准插入物流号，来判断是否重复
			logger.info("step1");
			this.orderLogDao.addOrderLog(orderFormInfo.getTxLogisticId());
			logger.info("step2");

			// 订单实体类
			Order order = new Order();

			// 收件人实体类
			TraderInfo receiver = orderFormInfo.getReceiver();

			// 发件人实体类
			TraderInfo sender = orderFormInfo.getSender();

			// 获取商品信息
			List<Product> products = orderFormInfo.getItems();
			
			boolean flag = false;
			
			// 新增订单数据
			logger.info("step3");
			flag = this.addOrderMethod(orderFormInfo, order, sender, receiver,
					products);
			logger.info("step4");

			// 订单易通端数据持久化成功的场合
			if(flag){
				
				// 线下订单场合
				if (Order.OFFLINE_TYPE.equals(orderFormInfo.getLineType())
						&& !StringUtils.equalsIgnoreCase("ytotest88888888",
								orderFormInfo.getRemark())) {
					
					logger.info("addJgWaybillMethod start...");
					
					// 线下订单jgwaybill表中插入记录
					this.addJgWaybillMethod(order, orderFormInfo);
					logger.info("addJgWaybillMethod end...");
				}
				
				// 线上订单场合
				else if(Order.ONLINE_TYPE.equals(orderFormInfo.getLineType()) && !StringUtils.equalsIgnoreCase("ytotest88888888",
						orderFormInfo.getRemark())){
					
					// 判断是否面单打印
					boolean isPrint = Resource.getIsPrint(orderFormInfo.getClientId());
					if (isPrint
							|| Order.ONLINE_TYPE.equals(orderFormInfo.getLineType())) {

						// 订单数据添加到打印表
						this.addOrderPrintMethod(order, orderFormInfo, receiver,sender, products);
					}
				}
				
				// 返回结果集
				order.setSender(sender);
				order.setReceiver(receiver);
				return order;
			}else{
				return null;
			}

		} catch (DuplicateKeyException e) {
			logger.error("create orderPrint fail 主键重复,物流号："
					+ orderFormInfo.getTxLogisticId());
			return null;
		}
	}

	@Override
	public List<T> getAllOrder() {
		return dao.getAllOrder();
	}

	@Override
	public boolean updateOrder(T entity) {

		if (entity == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return false;
		}

		return dao.updateOrder(entity);
	}

	@Override
	public boolean delOrder(T entity) {

		boolean result = false;
		if (entity == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return false;
		}
		result = dao.delOrder(entity);
		result = productDao.removeByorderId(entity.getId());
		result = traderDao.delTraderInfoByOrderId(entity.getId());

		return result;
	}

	@Override
	public T getOrderById(Integer id) {
		return dao.getOrderById(id);
	}

	@Override
	public T getOrderByLogisticId(String logisticId) {
		return dao.getOrderByLogisticId(logisticId);
	}

	@Override
	public T getOrderByMailNo(String mailNo) {
		// @2012-01-11/ChenRen
		// 现在运单号不唯一
		T entity = (T) new Order();
		entity.setMailNo(mailNo);

		return getOrderByMailNo(entity);

		// return dao.getOrderbyMailNo(mailNo);
	}

	@Override
	public T getOrderByTradeNo(String tradeNo) {
		return dao.getOrderBytradeNo(tradeNo);
	}

	@Override
	public boolean updateOrderStatus(String orderStatus) {
		return false;
	}

	@Override
	public boolean updateOrderWeightByMailNo(String mailNo, Float weight,Date starttime,Date endtime) {
		// 是否执行成功
		boolean isSuccess = false;
		
		Long endBuildTime = System.currentTimeMillis();
		isSuccess = dao.updateOrderWeightByMailNo(mailNo, weight,starttime,endtime);
		return isSuccess;
	}

	@Override
	public boolean updateOrderWeightByTradeNo(String tradeNo, Float weight) {
		return dao.updateOrderWeightByTradeNo(tradeNo, weight);
	}

	@Override
	public boolean updateOrderByMailNo(String mailNo, String freightType,
			double trimFreight) {
		Map map = new HashMap();
		map.put("mailNo", mailNo);
		map.put("freightType", freightType);
		// map.put("freight", freight);
		map.put("trimFreight", trimFreight);
		// return false;
		return dao.updateOrderByMailNo(map);
	}

	@Override
	public List<T> getOrderListByVipId(int vipId) {

		return null;
	}

	@Override
	public boolean delOrderByLogisticId(int LogisticId) {

		return false;
	}

	@Override
	public String output2csv(Map map) {

		Integer id = 0;
		if (map.get("userCode") == null || map.get("userCode").equals("")) {
			id = Integer.valueOf(map.get("vipId").toString());
		} else {
			List<User> userList = userService.searchUsersByCodeTypeState(map
					.get("userCode").toString(), "1", "1");
			id = userList.get(0).getId();
		}

		User user = userService.getUserById(id);
		if (user == null) {
			logger.error("数据异常! 用户对象为空! 无法根据id查到对应的用户信息! 参数信息[id:" + id + "]");
			return "";
		}
		String customerId = user.getTaobaoEncodeKey();
		if (StringUtils.isEmpty(customerId)) {
			logger.error("数据异常! 用户的TaobaoEncodeKey为空! 无法进行对账操作! 参数信息[id:" + id
					+ "]");
			return "";
		}

		User branch = userService.getSiteByVipId(id); // 网点
		String numProvBranch = "";
		if (branch == null) {
			logger.error("vip用户的网点对象不存在! 参数信息[vipId:" + id + "]");
			branch = user;
			logger.info("设置网点对象为当前vip对象! 省份Id取值为当前vip对象的值.");
		}
		numProvBranch = branch.getField001(); // 网点的省份Id

		// 卖家userId查询
		if (map.get("vipId") != null && map.get("vipId") != "") {
			map.put("customerId", getCustomerIds(map.get("vipId").toString()));
		}
		// 网点根据用户编码查询
		if (map.get("userCode") != null && map.get("userCode") != "") {
			if (getCustomerIdByUserCode(map.get("userCode").toString()) != null)
				map.put("customerId",
						getCustomerIdByUserCode(map.get("userCode").toString()));
		}

		int num = Integer.valueOf(map.get("num") == null ? "-1" : map
				.get("num").toString());
		numLimit = num == -1 ? numLimit : num;

		StringBuffer sb = new StringBuffer(
				"运单号, 店铺名称, 状态, 重量, 首重价格, 续重单价, 寄件地, 目的地, 实收运费");

		float totalFreight = 0;
		float firstWeightPirce = (Float) map.get("firstWeight");
		float overWeightPirce = (Float) map.get("overWeight");
		float weight = 0;
		float netFreight = 0; // 实际运费

		String prov = map.get("prov").toString();
		String city = map.get("city").toString();
		String district = map.get("district").toString();

		if ("全部".equals(prov)) {
			map.put("prov", "");
		}
		if ("全部".equals(city)) {
			map.put("city", "");
		}
		if ("全部".equals(district)) {
			map.put("district", "");
		}

		List<T> list = null;
		// 收件地
		map.put("addr", "toAddr");
		map.put("tradeType", "1");
		list = dao.ecAccountHalf(map);

		// 寄件地
		map.remove("prov"); // 去掉地区条件
		map.remove("city");
		map.remove("district");
		map.put("addr", "fromAddr");
		map.put("tradeType", "0");
		List<T> listFrom = dao.ecAccountHalf(map);
		Map<Integer, T> fromAddrMap = new HashMap<Integer, T>();
		for (T oFrom : listFrom) {
			fromAddrMap.put(oFrom.getId(), oFrom);
		}
		for (T t : list) {
			// 设置店铺名称
			if (t.getCustomerId() != null) {
				User u = userService.getUserByCustomerId(t.getCustomerId());
				t.setShopName(u.getShopName());
			}
			String statusCode = StringUtil.trim(t.getStatus().toUpperCase());
			String status = null;
			if ("0".equals(statusCode)) {
				status = "接单中";
			} else {
				status = orderStatusMap.get(statusCode);
			}
			status = StringUtils.isEmpty(status) ? ORDER_STATUS_DEF : status;

			weight = t.getWeight();
			// @2012-03-16/ChenRen 需求说了算
			// String weightText = weight <= 1 ? "≤1" : weight+"";
			String weightText = weight == 0 ? "≤1" : weight + "";

			T fromOrder = fromAddrMap.get(t.getId());
			fromOrder = fromOrder == null ? (T) new Order() : fromOrder;
			// 判断网点的地址和订单的发件地是否一致，不一致则认为是异常订单
			String numProvOrder = fromOrder.getNumProv();
			if (!numProvBranch.equals(numProvOrder)) {
				logger.info("订单寄件地和当前网点发货地不一致。" + "参数信息[订单Id:" + t.getId()
						+ "网点的省份Id:" + numProvBranch + "当前用户Id:" + id + ";]");

				sb.append("\r\n")
						.append(t.getMailNo())
						// 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
						.append(",").append(t.getShopName()).append(",")
						.append(status).append(",").append(weightText)
						.append(",").append(0).append(",").append(0)
						.append(",")
						.append(fromOrder.getFromAddr().replace(",", "，"))
						.append(",").append(t.getToAddr().replace(",", "，"))
						.append(",").append("未计算运费。订单寄件地和当前网点发货地不一致。");
			} else {
				weight = weight <= 1 ? 1 : weight;
				netFreight = weight > 1 ? (firstWeightPirce + (weight - 1)
						* overWeightPirce) : weight * firstWeightPirce;
				netFreight = (float) Math.round(netFreight * 100) / 100;

				sb.append("\r\n")
						.append(t.getMailNo())
						// 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
						.append(",").append(t.getShopName()).append(",")
						.append(status).append(",").append(weightText)
						.append(",").append(firstWeightPirce).append(",")
						.append(overWeightPirce).append(",")
						.append(fromOrder.getFromAddr().replace(",", "，"))
						.append(",").append(t.getToAddr().replace(",", "，"))
						.append(",").append(netFreight);
			}

			totalFreight += netFreight;
		}

		sb.append("\r\n")
				// 换行
				.append("\r\n")
				// 空一行
				.append("总运费：").append(",")
				.append((float) Math.round(totalFreight * 100) / 100);

		return sb.toString();
	} // output2csv

	@Override
	public String output2csvByPosttemp(Map map, Integer posttempId,
			Posttemp sysPt) {

		if (posttempId == null) {
			logger.error("参数异常!模板Id为空!");
			return StringUtils.EMPTY;
		}

		// //卖家userId查询
		// if(map.get("vipId")!=null && map.get("vipId")!="") {
		// map.put("customerId", getCustomerIds(map.get("vipId").toString() ));
		// }
		// 卖家
		if (map.get("relatedId") != null
				&& ((List) map.get("relatedId")).size() > 0)
			map.put("customerId", (List) map.get("relatedId"));

		// 网点根据用户编码查询
		if (map.get("userCode") != null && map.get("userCode") != "") {
			if (getCustomerIdByUserCode(map.get("userCode").toString()) != null)
				map.put("customerId",
						getCustomerIdByUserCode(map.get("userCode").toString()));
		}

		// //ADD--MGL
		// List customerIds=(List)map.get("customerId");
		// List relatedId=(List)map.get("relatedId");
		// if(relatedId!=null&&relatedId.size()>0)customerIds.addAll(relatedId);
		// map.put("customerId",customerIds);

		// 把xml解析成运费信息(postinfo)对象集合
		List<Postinfo> pList = null;
		double ow = 0.01;
		if (posttempId == 0 && sysPt != null) {
			pList = sysPt.getPostinfoList();
		} else {
			Posttemp posttemp = posttempDao.getPosttempById(posttempId); // 运费模板
			if (posttemp != null) {
				ow = posttemp.getModule();
			} else {
				posttemp = new Posttemp();
			}
			String xml = posttemp.getPostinfo(); // 运费信息xml字符串
			try {
				pList = XmlUtil.xmlDecoder2List(xml);
			} catch (Exception e) {
				logger.error("数据异常!xml解析无法解析![xml:" + xml + "]");
				return StringUtils.EMPTY;
			}
			if (pList.size() < 1) {
				logger.error("数据异常!运费信息为空![xml:" + xml + "]");
				return StringUtils.EMPTY;
			}
		}

		/** 运费信息Map; 值以<省份id, postinfo>方式存; 计算的时候用省份id取运费信息 */
		Map<String, Postinfo> piMap = new HashMap<String, Postinfo>();
		String srcId = ""; // 运费模板的始发地Id
		if (pList != null && !pList.isEmpty()) {
			srcId = pList.get(0).getSrcId();
		}
		for (Postinfo postinfo : pList) {
			piMap.put(postinfo.getDestId(), postinfo);
		}

		float firstWeightPirce = 0; // 首重
		float overWeightPirce = 0; // 超重
		float weight = 0; // 重量
		float netFreight = 0; // 实际运费
		float totalFreight = 0; // 总运费; 等于订单数据的实收运费累加
		float uftotalFreight = 0; // 特殊运单类型为新增总运费;
		String userType = map.get("userType") == null ? "1" : map.get(
				"userType").toString(); // 为空默认 网点用户
		StringBuffer sbCsvStr = new StringBuffer(
				"运单号, 店铺名称, 状态, 重量, 首重价格, 续重单价, 寄件地, 目的地, 实收运费");

		List<T> list = null;
		// 寄件地
		map.put("addr", "fromAddr");
		map.put("tradeType", "0");
		list = dao.ecAccountHalf(map);
		Map<Integer, T> fromAddrMap = new HashMap<Integer, T>();
		for (T oFrom : list) {
			fromAddrMap.put(oFrom.getId(), oFrom);
		}
		// 收件地
		map.put("addr", "toAddr");
		map.put("tradeType", "1");
		list = dao.ecAccountHalf(map);

		String toAddr = ""; // 目的地
		String status = ""; // 订单状态

		for (T order : list) {
			// 设置店铺名称
			if (order.getCustomerId() != null) {
				User u = userService.getUserByCustomerId(order.getCustomerId());
				order.setShopName(u.getShopName());
			}

			String statusCode = StringUtil
					.trim(order.getStatus().toUpperCase());
			if ("0".equals(statusCode)) {
				status = "接单中";

			} else {
				status = orderStatusMap.get(statusCode);
			}
			status = StringUtils.isEmpty(status) ? ORDER_STATUS_DEF : status;

			if (order.getFreightType() != null
					&& "0".equals(order.getFreightType())) {
				// System.out.print("---------------:"+order.getFreightType());
				// ufOrderList.add(order);
				order.setToAddr(order.getToAddr());
				order.setNetFreight((float) Math.round(order.getTrimFreight() * 100) / 100); // 特殊运费
				// ufztotalFreight +=order.getNetFreight();
				netFreight = (float) Math.round(order.getTrimFreight() * 100) / 100; // 特殊运费

				totalFreight += netFreight; // 累加总运费

				String jj = "特殊运单";
				weight = order.getWeight();
				// String weightText = weight <= 1 ? "≤1" : weight+"";
				String weightText = weight == 0 ? "≤1" : weight + "";
				sbCsvStr.append("\r\n")
						.append(order.getMailNo())
						// 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
						.append(",").append(order.getShopName()).append(",")
						.append(status)
						.append(DateUtil.toSeconds(order.getCreateTime()))
						.append(",").append(weightText).append(",")
						.append(firstWeightPirce).append(",")
						.append(overWeightPirce).append(",").append(jj)
						.append(",").append(order.getToAddr()).append(",")
						.append(order.getNetFreight());
			} else {

				Postinfo postinfo = null;
				String provId = order.getNumProv();
				// 440000 是region表中广东的Id
				// 运费模板木有到广东的运费，分成到广东珠三角(449999)、广东其他地区(448888) 两个运费信息
				// if("440000".equals(provId ) ) {
				if ("440000".equals(provId) && "440000".equals(srcId)) {
					// sql中查出的地区数据是在DB中就拼接好的。格式：省，市，区，详细地址
					String[] toAddrArr = order.getToAddr().split("，");

					// 如果目的地的市属于珠三角，就取珠三角的运费
					boolean isExist = Resource.orderProvinces
							.containsKey(toAddrArr[1]);
					postinfo = isExist ? piMap.get("449999") : piMap
							.get("448888");
				} else {
					postinfo = piMap.get(provId); // 根据目的地省份Id取运费模板中的运费信息
				}

				weight = order.getWeight();
				String weightText = weight == 0 ? "≤1" : weight + "";

				T fromOrder = fromAddrMap.get(order.getId());
				fromOrder = fromOrder == null ? (T) new Order() : fromOrder;

				if (postinfo == null) {
					logger.error("订单中的目的地不在运费模版中!" + "参数信息[" + "订单Id:"
							+ order.getId()
							+ ";订单目的地省份Id:"
							+ order.getNumProv()// 对应tanderinfo表中的收件人信息字段：numProv
							+ ";运费模板Id:" + posttempId
							+ "]。订单目的地省份Id对应tanderinfo表中的收件人信息字段：numProv。");
					String stdate = DateUtil.toSeconds(order.getCreateTime());
					if (order.getAcceptTime() != null) {
						stdate = DateUtil.toSeconds(order.getAcceptTime());
					}

					sbCsvStr.append("\r\n")
							.append(order.getMailNo())
							// 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
							.append(",").append(order.getShopName())
							.append(",").append(stdate).append(",")
							.append(weightText).append(",").append(0)
							.append(",").append(0).append(",")
							.append(fromOrder.getFromAddr().replace(",", "，"))
							.append(",").append(toAddr).append(",")
							.append("未计算运费。订单中的目的地不在运费模版中!");
				} else {
					// 如果寄件地和运费模板的始发地不一致，不计算
					// 如果运费模板的始发地和订单的发件地代码不一致，不计算运费
					if (!postinfo.getSrcId().equals(fromOrder.getNumProv())) {
						logger.error("订单寄件地和运费模板的始发地不一致!" + "参数信息[" + "订单Id:"
								+ order.getId() + ";订单目的地省份Id:"
								+ order.getNumProv()// 对应tanderinfo表中的收件人信息字段：numProv
								+ ";运费模板Id:" + posttempId + "]");

						String stdate = DateUtil.toSeconds(order
								.getCreateTime());
						if (order.getAcceptTime() != null) {
							stdate = DateUtil.toSeconds(order.getAcceptTime());
						}

						sbCsvStr.append("\r\n")
								.append(order.getMailNo())
								// 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
								.append(",")
								.append(order.getShopName())
								.append(",")
								.append(stdate)
								.append(",")
								.append(weightText)
								.append(",")
								.append(0)
								.append(",")
								.append(0)
								.append(",")
								.append(fromOrder.getFromAddr().replace(",",
										"，")).append(",").append(toAddr)
								.append(",").append("未计算运费。订单寄件地和运费模板的始发地不一致!");
					} else {
						// 根据postinfo的运费信息设置Order的运费信息
						firstWeightPirce = postinfo.getFwRealPirce(); // 首重实收价
						overWeightPirce = postinfo.getOwRealPirce(); // 超重实收价

						// @2011-12-01/ChenRen 重量为0，按1计算
						weight = weight <= 1 ? 1 : weight;
						if (ow > 0.01) {
							netFreight = (float) (weight > 1 ? (firstWeightPirce + Math
									.ceil((weight - 1) / ow)
									* ow
									* overWeightPirce) : weight
									* firstWeightPirce);
						} else {
							netFreight = (float) (weight > 1 ? (firstWeightPirce + (weight - 1)
									* overWeightPirce)
									: weight * firstWeightPirce);

						}

						// if(order.getFreightType()!=null&&"0".equals(order.getFreightType())){
						// System.out.print("---------------:"+order.getFreightType());
						// order.setNetFreight((float)
						// Math.round(order.getTrimFreight() * 100) / 100 ); //
						// 特殊运费
						// netFreight=(float) Math.round(order.getTrimFreight()
						// * 100) / 100; // 特殊运费
						// }else{
						netFreight = (float) Math.round(netFreight * 100) / 100; // 实际运费,
																					// 四舍五入
						// }
						totalFreight += netFreight; // 累加总运费

						// 把,转成，;因为导出csv的时候,是换一列
						toAddr = order.getToAddr().replace(",", "，");
						order.setToAddr(toAddr);

						String stdate = DateUtil.toSeconds(order
								.getCreateTime());
						if (order.getAcceptTime() != null) {
							stdate = DateUtil.toSeconds(order.getAcceptTime());
						}

						sbCsvStr.append("\r\n")
								.append(order.getMailNo())
								// 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
								.append(",")
								.append(order.getShopName())
								.append(",")
								.append(stdate)
								.append(",")
								.append(weightText)
								.append(",")
								.append(firstWeightPirce)
								.append(",")
								.append(overWeightPirce)
								.append(",")
								.append(fromOrder.getFromAddr().replace(",",
										"，")).append(",").append(toAddr)
								.append(",").append(netFreight);
					}
				}
			}
		}
		/*
		 * Map ufmap=new HashMap();
		 * //System.out.println("--dzdz-7-:"+map.get("customerId"));
		 * //System.out.println("--dzdz-8-:"+map.get("starttime"));
		 * //System.out.println("--dzdz-9-:"+map.get("endtime"));
		 * ufmap.put("customerId",map.get("customerId"));
		 * ufmap.put("startTime",map.get("starttime"));
		 * ufmap.put("endTime",map.get("endtime")); //
		 * ufmap.put("type","1");//新增特殊运单类型 List<UnlikeFreight>
		 * uflist=unlikeFreightDao.getUnlikeFreightList(ufmap);
		 * //System.out.println("--dzdz-10-:"+uflist.size()); for (UnlikeFreight
		 * unlikeFreight : uflist) { Order uforder= new Order();
		 * if("1".equals(unlikeFreight.getType())){
		 * uforder.setMailNo(unlikeFreight.getMailNo());
		 * uforder.setStatus("已签收"); uforder.setWeight((float)
		 * unlikeFreight.getWeight()); uforder.setLineType("1");
		 * uforder.setToAddr(unlikeFreight.getAddress());
		 * uforder.setNetFreight((float) Math.round(unlikeFreight.getPrice() *
		 * 100) / 100); uforder.setFreightType("2");
		 * uforder.setRemark(unlikeFreight.getReason());
		 * uforder.setBuyName(unlikeFreight.getUserName());
		 * uforder.setBuyMobile(unlikeFreight.getMobile());
		 * uforder.setUpdateTime(unlikeFreight.getCreateTime()); // list.add((T)
		 * uforder); //此类型 暂时不在电子对账列表显示 //
		 * System.out.println("--dzdz uforder-12-:"+uforder); //
		 * uftotalNum=uftotalNum+1; //
		 * System.out.println("--dzdz uftotalNum-12-:"+uftotalNum); //
		 * ufOrderList.add((T)uforder); //特殊运单list uftotalFreight +=
		 * uforder.getNetFreight(); // 累加类型为新增总运费
		 * 
		 * String jj="特殊运单"; weight = uforder.getWeight(); String weightText =
		 * weight <= 1 ? "≤1" : weight+""; sbCsvStr.append("\r\n")
		 * .append(uforder.getMailNo()) // 在Excel中，'可以让当前单元格显示成文本格式. '且不会影响数据显示
		 * .append(",") .append(status)
		 * .append(",").append(weightText).append(",")
		 * .append(firstWeightPirce).append(",")
		 * .append(overWeightPirce).append(",") .append(jj).append(",")
		 * .append(uforder
		 * .getToAddr()).append(",").append(uforder.getNetFreight()); }
		 * 
		 * 
		 * } if(uflist.size()>0){ totalFreight=totalFreight+uftotalFreight; }
		 */
		sbCsvStr.append("\r\n")
				// 换行
				.append("\r\n")
				// 空一行
				.append("总运费：").append(",")
				.append((float) Math.round(totalFreight * 100) / 100);

		return sbCsvStr.toString();
	} // output2csvByPosttemp

	@Override
	public String output2csv3(Map map, Integer posttempId, Posttemp sysPt,
			String customerCode) {
		if (posttempId == null) {
			logger.error("参数异常!模板Id为空!");
			return StringUtils.EMPTY;
		}
		Posttemp temp = sysPt;
		if (temp == null) {
			temp = posttempService.getPosttempById(posttempId);
		}
		StringBuffer sbCsvStr = new StringBuffer();
		int usertypePrefix = -1; // 用户类型前缀
		String ptType = "0"; // 模版类型 目前只在卖家处,进行初始使用
		String usertype = map.get("usertype").toString();

		if ("2".equals(usertype) || "21".equals(usertype)
				|| "22".equals(usertype) || "23".equals(usertype)) {
			// 得到客户名称
			String customerName = "";
			if (!StringUtil.isBlank(customerCode)) {
				UserThread userThread = new UserThread();
				userThread.setUserCode(customerCode);
				List<UserThread> userthreadList = userThreadService
						.searchUsersByCode(userThread);
				customerName = userthreadList.get(0).getUserName();
			}
			sbCsvStr.append("客户名称：" + customerName + "\n");
			sbCsvStr.append("运单号, 重量, 价格, 收件人, 收件地址");
			usertypePrefix = 2;
		} else if ("1".equals(usertype) || "11".equals(usertype)
				|| "12".equals(usertype) || "13".equals(usertype)) {
			// 卖家需要根据模版类型，默认续重价格,若是系统模板也是按续重价格导出
			String defaultSheet = "运单号, 店铺名称, 状态, 重量, 首重重量, 首重价格, 续重价格, 最低收费价格, 收货地址, 实收运费, 收件人";
			if (temp.getCalclateType() != null
					&& temp.getCalclateType().equals("1")) {// 固定价格
				defaultSheet = "运单号, 店铺名称, 状态, 重量, 固定价格, 收货地址, 实收运费, 收件人";
			} else if (temp.getCalclateType() != null
					&& temp.getCalclateType().equals("2")) {// 简单重量
				defaultSheet = "运单号, 店铺名称, 状态, 重量, 重量单价, 最低收费价格, 收货地址, 实收运费, 收件人";
			}
			// 续重统计单位收费和续重价格收费按默认
			sbCsvStr.append(defaultSheet);
			usertypePrefix = 1;
		} else if ("4".equals(usertype)) {
			sbCsvStr.append("运单号, 状态, 重量, 首重价格, 续重单价, 收货地址, 实收运费, 收件人");
			usertypePrefix = 4;
		}

		Pagination<EccoreSearchResultDTO> paginationDTO = new Pagination<EccoreSearchResultDTO>(
				1, Integer.MAX_VALUE);
		// 查询数据
		try {
			eccoreSearchService.searchEccoreData(ConfigUtilSingle.getInstance()
					.getSolrEccoreUrl(), this.buildSearchPage(paginationDTO,
					map));
		} catch (Exception e) {
			logger.error("#电子对账#从缓存查数据出问题", e);
		}

		Map postinfoMap = null;
		String srcProvCode = ""; // 平台用户的srcProvCode在table列表中显示/ 要在order对象中设置
		// 处理平台用户
		if ("4".equals(ObjectUtils.toString(map.get("usertype")))) {
			// 因为所有分仓对账的时候可能有多个不同的分仓用户，会导致有多个不同的运费模板
			// 这里把运费模板存入一个map<costomerId, postinfoMap>;
			// 计算order数据的时候先根据customerId去postinfo对象
			postinfoMap = this.buildPosttempInfo(map);
		} else {
			postinfoMap = this.buildPosttempInfo(posttempId, sysPt);
			srcProvCode = ObjectUtils.toString(postinfoMap
					.get(Constants.SRCPROVCODE)); // 发件地省份
		}

		// 查询结果
		List<EccoreSearchResultDTO> listDTO = paginationDTO.getRecords();

		float totalFreight = 0; // 总运费; 等于订单数据的实收运费累加
		float thtotalFreight = 0; // 退货总费用
		float weight = 0;
		String weightText = StringUtils.EMPTY;
		if (listDTO == null || listDTO.size() == 0) {
			return sbCsvStr.toString();
		}
		for (EccoreSearchResultDTO dto : listDTO) {
			T order = this.buildOrder(dto); // 根据缓存结果封装order对象
			order = this.calculateOrder(order, srcProvCode, postinfoMap); // 计算运费
			order.setToAddr(order.getToAddr()
					.replaceAll(",", StringUtils.EMPTY)); // , 会在csv中换一个单元格
			weight = order.getWeight();
			weightText = weight == 0 ? "≤1" : weight + "";
			// 特殊运单
			if ("0".equals(order.getFreightType())) {
				order.setNetFreight((float) Math.round(order.getTrimFreight() * 100) / 100); // 特殊运费
				totalFreight += order.getNetFreight(); // 累加总运费

				sbCsvStr.append("\r\n").append(order.getMailNo()).append(",");
				if (usertypePrefix == 1) {
					sbCsvStr.append(order.getShopName()).append(",")
							.append(order.getStatus())
							.append(DateUtil.toSeconds(order.getCreateTime()))
							.append(",").append(weightText);
					if (temp.getCalclateType() != null
							&& temp.getCalclateType().equals("1")) {// 固定价格
						sbCsvStr.append(",").append(0); // 固定价格
					} else if (temp.getCalclateType() != null
							&& temp.getCalclateType().equals("2")) {// 简单重量
						sbCsvStr.append(",").append(0); // 重量单价
						sbCsvStr.append(",").append(0); // 最低收费价格
					} else {// 续重统计单位收费和续重价格收费
						sbCsvStr.append(",").append(0); // 首重重量
						sbCsvStr.append(",").append(0); // 首重价格
						sbCsvStr.append(",").append(0); // 续重价格
						sbCsvStr.append(",").append(0); // 最低收费价格
					}
					sbCsvStr.append(",")
							.append(order.getToAddr().replaceAll("\n", ""))
							.append(",").append(order.getNetFreight());
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
				} else if (usertypePrefix == 2) {
					sbCsvStr.append(weightText).append(",")
							.append(order.getNetFreight());
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
					sbCsvStr.append(",").append(
							order.getToAddr().replaceAll("\n", ""));// 收货地址
				} else if (usertypePrefix == 4) {
					sbCsvStr.append(order.getStatus())
							.append(DateUtil.toSeconds(order.getCreateTime()))
							.append(",").append(weightText).append(",")
							.append(0).append(",").append(0).append(",")
							.append(order.getToAddr().replaceAll("\n", ""))
							.append(",").append(order.getNetFreight());
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
				}
			}
			// 异常订单
			else if ("errorPostinfo".equalsIgnoreCase(order.getRemark())
					|| "errorProv".equalsIgnoreCase(order.getRemark())) {
				String errorInfo = "未计算运费。订单中的目的地不在运费模版中!";
				errorInfo = "errorPostinfo".equalsIgnoreCase(order.getRemark()) ? errorInfo
						: "未计算运费。订单寄件地和运费模板的始发地不一致!";
				String stdate = DateUtil.toSeconds(order.getCreateTime());
				if (order.getAcceptTime() != null) {
					stdate = DateUtil.toSeconds(order.getAcceptTime());
				}
				sbCsvStr.append("\r\n").append(order.getMailNo()).append(","); // 运单号
				if (usertypePrefix == 1) {
					sbCsvStr.append(order.getShopName()).append(",")
							.append(order.getStatus()).append(stdate)
							.append(",").append(weightText);
					if (temp.getCalclateType() != null
							&& temp.getCalclateType().equals("1")) {// 固定价格
						sbCsvStr.append(",").append(order.getFixedPirce()); // 固定价格
					} else if (temp.getCalclateType() != null
							&& temp.getCalclateType().equals("2")) {// 简单重量
						sbCsvStr.append(",").append(order.getWeightPirce()); // 重量单价
						sbCsvStr.append(",").append(order.getFloorPirce()); // 最低收费价格
					} else {// 续重统计单位收费和续重价格收费
						sbCsvStr.append(",").append(order.getFirstWeight()); // 首重重量
						sbCsvStr.append(",")
								.append(order.getFirstWeightPirce()); // 首重价格
						sbCsvStr.append(",").append(order.getOverWeightPirce()); // 续重价格
						sbCsvStr.append(",").append(order.getFloorPirce()); // 最低收费价格
					}
					sbCsvStr.append(",")
							.append(order.getToAddr().replaceAll("\n", ""))
							.append(",").append(errorInfo);
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
				} else if (usertypePrefix == 2) {
					sbCsvStr.append(weightText).append(",").append(errorInfo);
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
					sbCsvStr.append(",").append(
							order.getToAddr().replaceAll("\n", ""));// 收货地址
				} else if (usertypePrefix == 4) {
					sbCsvStr.append(order.getStatus()).append(stdate)
							.append(",").append(weightText).append(",")
							.append(order.getFirstWeightPirce()).append(",")
							.append(order.getOverWeightPirce()).append(",")
							.append(order.getToAddr().replaceAll("\n", ""))
							.append(",").append(errorInfo);
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
				}
			} else {
				String stdate = DateUtil.toSeconds(order.getCreateTime());
				if (order.getAcceptTime() != null) {
					stdate = DateUtil.toSeconds(order.getAcceptTime());
				}
				sbCsvStr.append("\r\n").append(order.getMailNo()).append(",");
				if (usertypePrefix == 1) {
					sbCsvStr.append(order.getShopName()).append(",")
							.append(order.getStatus()).append(stdate)
							.append(",").append(weightText);
					if (temp.getCalclateType() != null
							&& temp.getCalclateType().equals("1")) {// 固定价格
						sbCsvStr.append(",").append(order.getFixedPirce()); // 固定价格
					} else if (temp.getCalclateType() != null
							&& temp.getCalclateType().equals("2")) {// 简单重量
						sbCsvStr.append(",").append(order.getWeightPirce()); // 重量单价
						sbCsvStr.append(",").append(order.getFloorPirce()); // 最低收费价格
					} else {// 续重统计单位收费和续重价格收费
						sbCsvStr.append(",").append(order.getFirstWeight()); // 首重重量
						sbCsvStr.append(",")
								.append(order.getFirstWeightPirce()); // 首重价格
						sbCsvStr.append(",").append(order.getOverWeightPirce()); // 续重价格
						sbCsvStr.append(",").append(order.getFloorPirce()); // 最低收费价格
					}
					sbCsvStr.append(",")
							.append(order.getToAddr().replaceAll("\n", ""))
							.append(",").append(order.getNetFreight());
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
				} else if (usertypePrefix == 2) {
					sbCsvStr.append(weightText).append(",")
							.append(order.getNetFreight());
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
					sbCsvStr.append(",").append(
							order.getToAddr().replaceAll("\n", ""));// 收货地址
				} else if (usertypePrefix == 4) {
					sbCsvStr.append(order.getStatus()).append(stdate)
							.append(",").append(weightText).append(",")
							.append(order.getFirstWeightPirce()).append(",")
							.append(order.getOverWeightPirce()).append(",")
							.append(order.getToAddr().replaceAll("\n", ""))
							.append(",") // 去换行符
							.append(order.getNetFreight());
					sbCsvStr.append(",").append(order.getBuyName());// 收件人
				}
				totalFreight += order.getNetFreight(); // 累加总运费
			}
			// 退货订单list
			if (order.getOrderType() != null && order.getOrderType() == 3) {
				order.setRemark("thdd");
				thtotalFreight += order.getNetFreight();
			}
		}

		// 减去退货的钱
		if (totalFreight < thtotalFreight) {
			totalFreight = Math.abs(totalFreight - thtotalFreight);
		} else {
			totalFreight = totalFreight - thtotalFreight;
		}

		return sbCsvStr + "\r\n\r\n总运费：,"
				+ (float) Math.round(totalFreight * 100) / 100;
	}

	@Override
	public T getOrderByMailNo(T entity) {
		T order = (T) new Order();
		if (entity == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return order;
		}
		if (entity.getMailNo() == null) {
			logger.error("运单号为空!");
			return order;
		}

		List<T> list = dao.getOrderByMailNo(entity.getMailNo());
		if (list.size() == 1) {
			order = list.get(0);
		} else if (list.size() > 1) {
			logger.info("根据运单号查到多个订单. 返回第一个." + "参数信息[mailNo:"
					+ entity.getMailNo() + ";第一个订单Id:" + list.get(0).getId()
					+ "]");
			order = list.get(0);
		} else {
			logger.info("运单号没有对应的订单. 返回空对象." + "参数信息[mailNo:"
					+ entity.getMailNo() + "]");
			return order;
		}

		String statusCode = StringUtil.trim(order.getStatus().toUpperCase());
		String status = null;
		if ("0".equals(statusCode)) {
			status = "接单中";
			/*
			 * if ("0".equals(t.getLineType())) { status = "接单中"; } else if
			 * ("1".equals(t.getLineType())) { status = "揽收中"; }
			 */
		} else {
			status = orderStatusMap.get(statusCode);
		}
		status = StringUtils.isEmpty(status) ? ORDER_STATUS_DEF : status;

		order.setStatus(status);
		return order;
	}

	@Override
	public List<T> getOrdersByWeight(String startTime, String endTime,
			int start, int limit) {
		return dao.getOrdersByWeight(startTime, endTime, start, limit);
	}

	@Override
	public List<String> getMailNoByPartitionDate(String startTime,
			String endTime, String customerId) {
		return dao.getMailNoByPartitionDate(startTime, endTime, customerId);
	}

	@Override
	public int countOrdersByWeight(String startTime, String endTime) {
		return dao.countOrdersByWeight(startTime, endTime);
	}

	@Override
	public boolean updateOrderMailNoByLogisticIdAndClientId(String logisticId,
			String clientId, String mailNo, String status) {

		// where条件字段不能为空
		if (StringUtils.isEmpty(logisticId)) {
			logger.error("物流号[logisticId]为空!");
		}
		if (StringUtils.isEmpty(clientId)) {
			logger.error("clientId为空!");
		}

		// Hashtable 里不能存放null值
		// 所以如果tradeNo/status为空，就默认""；但是在OrderMapper.xml中会进行判断，""字段不更新
		Map map = new Hashtable();
		map.put("logisticId", logisticId);
		map.put("clientId", clientId);
		map.put("mailNo", StringUtil.nullValue(mailNo, ""));
		map.put("status", StringUtil.nullValue(status, ""));

		// 此方法用于面单更新的时候，使用物流号更新orderPrint表中的is_send字段
		// 为了保证在一个事务里，所以写在这个同一个service方法中
		// 为了保证代码的合理性，所以在此处直接调用orderPringDao

		orderPrintDao.updateOrderPrintByTxLogisticIdIsSend(logisticId);

		return dao.updateOrderMailNoByLogisticIdAndClientId(map);
	}

	@Override
	public boolean orderStatusNodify(UpdateWaybillInfo updateInfo) {
		Map map = new HashMap();
		map.put("logisticId", updateInfo.getTxLogisticId());
		map.put("clientId", updateInfo.getClientId());
		map.put("mailNo", updateInfo.getMailNo());
		if (StringUtils.isNotBlank(updateInfo.getWeigthStr())) {
			map.put("weight", updateInfo.getWeigthStr());
		}

		map.put("status", updateInfo.getInfoContent());
		map.put("type", updateInfo.getType());
		map.put("acceptTime", DateUtil.valueof(updateInfo.getAcceptTime(),
				"yyyy-MM-dd HH:mm:ss"));
		long t1 = System.currentTimeMillis();

		boolean result = dao.updateOrderMailNoByLogisticIdAndClientId(map);
		logger.info("单号" + updateInfo.getMailNo() + "更新状态用时"
				+ (System.currentTimeMillis() - t1));

		if (result) {
			long t3 = System.currentTimeMillis();
			if (StringUtils.equals(updateInfo.getInfoContent(), "SIGNED")
					&& !StringUtils.isEmpty(updateInfo.getMailNo())) {
				Map<String, Object> qsnMap = new HashMap<String, Object>();
				qsnMap.put("mailNo", updateInfo.getMailNo());
				qsnMap.put("orderStatus", "1");
				questionnaireDao.updateOrderStatusByMailNo(qsnMap);
				logger.info("单号" + updateInfo.getMailNo() + "写questionnaire用时"
						+ (System.currentTimeMillis() - t3));
			}
		}
		return result;
	}

	@Override
	public boolean orderStatusNodifyByPartition(UpdateWaybillInfo updateInfo)throws Exception {
		Map map = new HashMap();
		map.put("logisticId", updateInfo.getTxLogisticId());
		map.put("clientId", updateInfo.getClientId());
		map.put("mailNo", updateInfo.getMailNo());
		if (StringUtils.isNotBlank(updateInfo.getWeigthStr())) {
			map.put("weight", updateInfo.getWeigthStr());
		}else{
			map.put("weight", updateInfo.getWeight());
		}
		map.put("status", updateInfo.getInfoContent());
		map.put("type", updateInfo.getType());
		map.put("acceptTime", DateUtil.valueof(updateInfo.getAcceptTime(),
				"yyyy-MM-dd HH:mm:ss"));////
		long t1 = System.currentTimeMillis();

		boolean result = dao.updateOrderMailNoByLogisticIdAndPartition(map);
		logger.info("单号" + updateInfo.getMailNo() + "更新状态用时"
				+ (System.currentTimeMillis() - t1));

		if (result) {

			/*// 面单号更新
			if (StringUtils.isNotBlank(updateInfo.getTxLogisticId())) {
				long t2 = System.currentTimeMillis();
				// insert ec_core_build_search_status_weight_index
				Order order = this.getOrderByLogisticId(updateInfo
						.getTxLogisticId());
				MailObjectDTO traderinfo = new MailObjectDTO();
				if (order != null) {
					logger.error(order.getId() + "==="
							+ order.getPartitionDate());
					traderinfo = dao.bulidPartEccoreDataByOrderId(
							order.getId(), DateUtil.format(
									order.getPartitionDate(), "yyyy-MM-dd"));
				}
				logger.info("物流号：" + updateInfo.getTxLogisticId() + "查order用时"
						+ (System.currentTimeMillis() - t2));
				long t4 = System.currentTimeMillis();
				if (order != null) {
					User user = Resource.getUserByCustomerId(order
							.getCustomerId());
					if (user != null) {
						// 线上订单更新面单号
						logger.error("写BuildSearchStatus表   START="+order.getId());
						if(order.getId()%5==0){
							BuildSearchStatusWeightIndexDao.
							addBuildSearchByUpdateOrderForWeightIndex
										(setBuildSearchStatusWeightIndex(order,traderinfo));
							logger.error("addBuildSearchByUpdateOrderForWeightIndex="+order.getId());
						}else if(order.getId()%5==1){
							BuildSearchStatusWeightIndexDao.
							addBuildSearchByUpdateOrderStatus
										(setBuildSearchStatusWeightIndex(order,traderinfo));
							logger.error("addBuildSearchByUpdateOrderStatus="+order.getId());
						}else if(order.getId()%5==2){
							BuildSearchStatusWeightIndexDao.
							addBuildSearchByUpdateOrderStatus2
										(setBuildSearchStatusWeightIndex(order,traderinfo));
							logger.error("addBuildSearchByUpdateOrderStatus2="+order.getId());
						}else if(order.getId()%5==3){
							BuildSearchStatusWeightIndexDao.
							addBuildSearchByUpdateOrderStatus3
										(setBuildSearchStatusWeightIndex(order,traderinfo));
							logger.error("addBuildSearchByUpdateOrderStatus3="+order.getId());
						}else{
							BuildSearchStatusWeightIndexDao
									.addBuildSearchByUpdateOrderStatus4(setBuildSearchStatusWeightIndex(
											order, traderinfo));
							logger.error("addBuildSearchByUpdateOrderStatus4="+order.getId());
						}
						
						logger.info("=======写BuildSearchStatusWeightIndex表  END=======");
					}
				}
				logger.info("物流号：" + updateInfo.getTxLogisticId() + "写BuildSearchStatusWeightIndex用时"
						+ (System.currentTimeMillis() - t4));
			}*/

			long t3 = System.currentTimeMillis();
			if (StringUtils.equals(updateInfo.getInfoContent(), "SIGNED")
					&& !StringUtils.isEmpty(updateInfo.getMailNo())) {
				Map<String, Object> qsnMap = new HashMap<String, Object>();
				qsnMap.put("mailNo", updateInfo.getMailNo());
				qsnMap.put("orderStatus", "1");
				questionnaireDao.updateOrderStatusByMailNo(qsnMap);
				logger.info("单号" + updateInfo.getMailNo() + "写questionnaire用时"
						+ (System.currentTimeMillis() - t3));
			}
		}
		return result;
	}

	// 封装BulidSearch表对象
	private BuildSearch setBuildSearch(Order order, MailObjectDTO traderinfo) {
		BuildSearch buildSearch = new BuildSearch();
		if (StringUtils.isBlank(order.getMailNo())) {
			buildSearch.setBuildTask("1");
		} else {
			buildSearch.setBuildTask("2");
			// 面单号设置的是旧的面单号
			if (order.getMailNo() != null) {
				buildSearch.setMailNo(order.getMailNo());
				logger.info("mylog:mailno=" + order.getMailNo());
			} else
				buildSearch.setMailNo("");
		}

		if (order.getId() != null) {
			buildSearch.setOrderId(order.getId());
			logger.info("mylog:orderid=" + order.getId());
		} else
			buildSearch.setOrderId(0);

		if (order.getStatus() != null) {
			buildSearch.setStatus(order.getStatus());
			logger.info("mylog:status=" + order.getStatus());
		} else
			buildSearch.setStatus("");
		
		if (order.getCustomerId() != null) {
			buildSearch.setCustomerId(order.getCustomerId());
			logger.info("mylog:CustomerId=" + order.getCustomerId());
		} else
			buildSearch.setCustomerId("");

		if (order.getOrderType() != null) {
			buildSearch.setOrderType(order.getOrderType());
			logger.info("mylog:OrderType=" + order.getOrderType());
		} else
			buildSearch.setOrderType(0);
		
		if (order.getTxLogisticId() != null) {
			buildSearch.setTxLogisticId(order.getTxLogisticId());
			logger.info("mylog:getTxLogisticId=" + order.getTxLogisticId());
		} else
			buildSearch.setTxLogisticId("");
		if (order.getTrimFreight() != null) {
			buildSearch.setTrimFreight(order.getTrimFreight());
			logger.info("mylog:getTrimFreight=" + order.getTrimFreight());
		} else
			buildSearch.setTrimFreight(0);

		if (order.getFreightType() != null) {
			buildSearch.setFreightType(order.getFreightType());
			logger.info("mylog:getFreightType=" + order.getFreightType());
		} else
			buildSearch.setFreightType("");

		if (order.getLineType() != null) {
			buildSearch.setLineType(order.getLineType());
			logger.info("mylog:getLineType=" + order.getLineType());
		} else
			buildSearch.setLineType("");

		if (order.getType() != null) {
			buildSearch.setType(order.getType());
			logger.info("mylog:getType=" + order.getType());
		} else
			buildSearch.setType("");

		if (order.getWeight() != null) {
			buildSearch.setWeight(order.getWeight());
			logger.info("mylog:getWeight=" + order.getWeight());
		} else
			buildSearch.setWeight(0);

		if (order.getMailNo() != null) {
			buildSearch.setOldMailNo(order.getMailNo());
			logger.info("mylog:getMailNo=" + order.getMailNo());
		} else
			buildSearch.setOldMailNo("");

		if (order.getType() != null) {
			buildSearch.setHoldTime(order.getType());
			logger.info("mylog:getType=" + order.getType());
		} else
			buildSearch.setHoldTime("");

		if (order.getPartitionDate() != null) {
			buildSearch.setPartitiondate(order.getPartitionDate());
			logger.info("mylog:setPartitiondate=" + order.getPartitionDate());
		} else
			buildSearch.setPartitiondate(new Date());
		if (order.getAcceptTime() != null) {
			buildSearch.setAcceptTime(order.getAcceptTime());
			logger.info("mylog:getAcceptTime=" + order.getAcceptTime());
		} else
			buildSearch.setAcceptTime(new Date());
		
		if(traderinfo==null){
			logger.info("traderinfo===null=="+order.getId()+"=="+DateUtil.format(order.getPartitionDate(), "yyyy-mm-dd"));
			return buildSearch;
		}
		if (traderinfo.getProv() != null) {
			buildSearch.setProv(traderinfo.getProv());
			logger.info("mylog:prov=" + traderinfo.getProv());
		} else
			buildSearch.setProv("");

		if (traderinfo.getProvF() != null) {
			buildSearch.setProvF(traderinfo.getProvF());
			logger.info("mylog:provF=" + traderinfo.getProvF());
		} else
			buildSearch.setProvF("");
		
		if (traderinfo.getName() != null) {
			buildSearch.setName(traderinfo.getName());
			logger.info("mylog:Name=" + traderinfo.getName());
		} else
			buildSearch.setName("");

		if (traderinfo.getPhone() != null&&!"".equals(traderinfo.getPhone())) {
			buildSearch.setPhone(traderinfo.getPhone());
			logger.info("mylog:Phone=" + traderinfo.getPhone());
		} else
			buildSearch.setPhone("0");

		if (traderinfo.getMobile() != null) {
			buildSearch.setMobile(traderinfo.getMobile());
			logger.info("mylog:getMobile=" + traderinfo.getMobile());
		} else
			buildSearch.setMobile("");

		if (traderinfo.getNumProvF() !=null) {
			buildSearch.setNumProvF(traderinfo.getNumProvF());
			logger.info("mylog:setNumProvF=" + traderinfo.getNumProvF());
		} else
			buildSearch.setNumProvF(0);

		if (traderinfo.getNumProv() > 0) {
			buildSearch.setNumProv(traderinfo.getNumProv());
			logger.info("mylog:setNumProv=" + traderinfo.getNumProv());
		} else
			buildSearch.setNumProv(0);

		if (traderinfo.getCity() != null) {
			buildSearch.setCity(traderinfo.getCity());
			logger.info("mylog:setCity=" + traderinfo.getCity());
		} else
			buildSearch.setCity("");

		if (traderinfo.getCityF() != null) {
			buildSearch.setCityF(traderinfo.getCityF());
			logger.info("mylog:setCityF=" + traderinfo.getCityF());
		} else
			buildSearch.setCityF("");

		if (traderinfo.getAddress() != null) {
			buildSearch.setAddress(traderinfo.getAddress());
			logger.info("mylog:getAddress=" + traderinfo.getAddress());
		} else
			buildSearch.setAddress("");
		

		if (traderinfo.getDistrict() != null) {
			buildSearch.setDistrict(traderinfo.getDistrict());
			logger.info("mylog:getDistrict=" + traderinfo.getDistrict());
		} else
			buildSearch.setDistrict("");
		
		buildSearch.setCreateTime(new Date());
		logger.info("mylog:buildSearch date end");
		return buildSearch;
	}
	
	// 封装BulidSearchStatusWeightIndex表对象
	private BuildSearchStatusWeightIndex setBuildSearchStatusWeightIndex
							(Order order, MailObjectDTO traderinfo) {
		BuildSearchStatusWeightIndex buildSearchStatusWeightIndex = 
							new BuildSearchStatusWeightIndex();
		if (StringUtils.isBlank(order.getMailNo())) {
			buildSearchStatusWeightIndex.setBuildTask("1");
		} else {
			buildSearchStatusWeightIndex.setBuildTask("2");
			// 面单号设置的是旧的面单号
			if (order.getMailNo() != null) {
				buildSearchStatusWeightIndex.setMailNo(order.getMailNo());
				logger.info("mylog:mailno=" + order.getMailNo());
			} else
				buildSearchStatusWeightIndex.setMailNo("");
		}

		if (order.getId() != null) {
			buildSearchStatusWeightIndex.setOrderId(order.getId());
			logger.info("mylog:orderid=" + order.getId());
		} else
			buildSearchStatusWeightIndex.setOrderId(0);

		if (order.getStatus() != null) {
			buildSearchStatusWeightIndex.setStatus(order.getStatus());
			logger.info("mylog:status=" + order.getStatus());
		} else
			buildSearchStatusWeightIndex.setStatus("");
		
		if (order.getCustomerId() != null) {
			buildSearchStatusWeightIndex.setCustomerId(order.getCustomerId());
			logger.info("" + order.getCustomerId());
		} else
			buildSearchStatusWeightIndex.setCustomerId("");

		if (order.getOrderType() != null) {
			buildSearchStatusWeightIndex.setOrderType(order.getOrderType());
			logger.info("mylog:OrderType=" + order.getOrderType());
		} else
			buildSearchStatusWeightIndex.setOrderType(0);
		
		if (order.getTxLogisticId() != null) {
			buildSearchStatusWeightIndex.setTxLogisticId(order.getTxLogisticId());
			logger.info("mylog:getTxLogisticId=" + order.getTxLogisticId());
		} else
			buildSearchStatusWeightIndex.setTxLogisticId("");
		if (order.getTrimFreight() != null) {
			buildSearchStatusWeightIndex.setTrimFreight(order.getTrimFreight());
			logger.info("mylog:getTrimFreight=" + order.getTrimFreight());
		} else
			buildSearchStatusWeightIndex.setTrimFreight(0);

		if (order.getFreightType() != null) {
			buildSearchStatusWeightIndex.setFreightType(order.getFreightType());
			logger.info("mylog:getFreightType=" + order.getFreightType());
		} else
			buildSearchStatusWeightIndex.setFreightType("");

		if (order.getLineType() != null) {
			buildSearchStatusWeightIndex.setLineType(order.getLineType());
			logger.info("mylog:getLineType=" + order.getLineType());
		} else
			buildSearchStatusWeightIndex.setLineType("");

		if (order.getType() != null) {
			buildSearchStatusWeightIndex.setType(order.getType());
			logger.info("mylog:getType=" + order.getType());
		} else
			buildSearchStatusWeightIndex.setType("");

		if (order.getWeight() != null) {
			buildSearchStatusWeightIndex.setWeight(order.getWeight());
			logger.info("mylog:getWeight=" + order.getWeight());
		} else
			buildSearchStatusWeightIndex.setWeight((float)0);

		if (order.getMailNo() != null) {
			buildSearchStatusWeightIndex.setOldMailNo(order.getMailNo());
			logger.info("mylog:getMailNo=" + order.getMailNo());
		} else
			buildSearchStatusWeightIndex.setOldMailNo("");

		if (order.getType() != null) {
			buildSearchStatusWeightIndex.setHoldTime(order.getType());
			logger.info("mylog:getType=" + order.getType());
		} else
			buildSearchStatusWeightIndex.setHoldTime("");

		if (order.getPartitionDate() != null) {
			buildSearchStatusWeightIndex.setPartitiondate(order.getPartitionDate());
			logger.info("mylog:setPartitiondate=" + order.getPartitionDate());
		} else
			buildSearchStatusWeightIndex.setPartitiondate(new Date());
		if (order.getAcceptTime() != null) {
			buildSearchStatusWeightIndex.setAcceptTime(order.getAcceptTime());
			logger.info("mylog:getAcceptTime=" + order.getAcceptTime());
		} else
			buildSearchStatusWeightIndex.setAcceptTime(new Date());
		
		if(traderinfo==null){
			logger.info("traderinfo===null=="+order.getId()+"=="+DateUtil.format(order.getPartitionDate(), "yyyy-mm-dd"));
			return buildSearchStatusWeightIndex;
		}
		if (traderinfo.getProv() != null) {
			buildSearchStatusWeightIndex.setProv(traderinfo.getProv());
			logger.info("mylog:prov=" + traderinfo.getProv());
		} else
			buildSearchStatusWeightIndex.setProv("");

		if (traderinfo.getProvF() != null) {
			buildSearchStatusWeightIndex.setProvF(traderinfo.getProvF());
			logger.info("mylog:provF=" + traderinfo.getProvF());
		} else
			buildSearchStatusWeightIndex.setProvF("");
		
		if (traderinfo.getName() != null) {
			buildSearchStatusWeightIndex.setName(traderinfo.getName());
			logger.info("mylog:Name=" + traderinfo.getName());
		} else
			buildSearchStatusWeightIndex.setName("");

		if (traderinfo.getPhone() != null&&!"".equals(traderinfo.getPhone())) {
			buildSearchStatusWeightIndex.setPhone(traderinfo.getPhone());
			logger.info("mylog:Phone=" + traderinfo.getPhone());
		} else
			buildSearchStatusWeightIndex.setPhone("0");

		if (traderinfo.getMobile() != null) {
			buildSearchStatusWeightIndex.setMobile(traderinfo.getMobile());
			logger.info("mylog:getMobile=" + traderinfo.getMobile());
		} else
			buildSearchStatusWeightIndex.setMobile("");

		if (traderinfo.getNumProvF() !=null) {
			buildSearchStatusWeightIndex.setNumProvF(traderinfo.getNumProvF());
			logger.info("mylog:setNumProvF=" + traderinfo.getNumProvF());
		} else
			buildSearchStatusWeightIndex.setNumProvF(0);

		if (traderinfo.getNumProv() > 0) {
			buildSearchStatusWeightIndex.setNumProv(traderinfo.getNumProv());
			logger.info("mylog:setNumProv=" + traderinfo.getNumProv());
		} else
			buildSearchStatusWeightIndex.setNumProv(0);

		if (traderinfo.getCity() != null) {
			buildSearchStatusWeightIndex.setCity(traderinfo.getCity());
			logger.info("mylog:setCity=" + traderinfo.getCity());
		} else
			buildSearchStatusWeightIndex.setCity("");

		if (traderinfo.getCityF() != null) {
			buildSearchStatusWeightIndex.setCityF(traderinfo.getCityF());
			logger.info("mylog:setCityF=" + traderinfo.getCityF());
		} else
			buildSearchStatusWeightIndex.setCityF("");

		if (traderinfo.getAddress() != null) {
			buildSearchStatusWeightIndex.setAddress(traderinfo.getAddress());
			logger.info("mylog:getAddress=" + traderinfo.getAddress());
		} else
			buildSearchStatusWeightIndex.setAddress("");
		

		if (traderinfo.getDistrict() != null) {
			buildSearchStatusWeightIndex.setDistrict(traderinfo.getDistrict());
			logger.info("mylog:getDistrict=" + traderinfo.getDistrict());
		} else
			buildSearchStatusWeightIndex.setDistrict("");
		
		buildSearchStatusWeightIndex.setCreateTime(new Date());
		logger.error("mylog:buildSearchStatusWeightIndexs date end");
		return buildSearchStatusWeightIndex;
	}

	public boolean orderStatusNodifyAndIntoSolr(UpdateWaybillInfo updateInfo)throws Exception {
		Map map = new HashMap();
		map.put("logisticId", updateInfo.getTxLogisticId());
		map.put("clientId", updateInfo.getClientId());
		map.put("mailNo", updateInfo.getMailNo());
		map.put("status", updateInfo.getInfoContent());
		map.put("type", updateInfo.getType());
		map.put("acceptTime", DateUtil.valueof(updateInfo.getAcceptTime(),
				"yyyy-MM-dd HH:mm:ss"));
		long t1 = System.currentTimeMillis();

		boolean result = dao.updateOrderMailNoByLogisticIdAndPartition(map);
		logger.info("单号" + updateInfo.getMailNo() + "更新状态用时"
				+ (System.currentTimeMillis() - t1));

		if (result) {

			// 面单号更新
			if (StringUtils.isNotBlank(updateInfo.getMailNo())) {
				long t2 = System.currentTimeMillis();
				// insert ec_core_build_search
				Order order = this.getOrderByLogisticId(updateInfo
						.getTxLogisticId());
				logger.info("单号" + updateInfo.getMailNo() + "查order用时"
						+ (System.currentTimeMillis() - t2));
				long t4 = System.currentTimeMillis();
				if (order != null) {
					User user = Resource.getUserByCustomerId(order
							.getCustomerId());
					if (user != null) {
						MailObjectDTO mailObjectDTOs = new MailObjectDTO();// dao.bulidPartEccoreDataByOrderId(order.getId(),DateUtil.format(order.getPartitionDate(),
																			// "yyyy-MM-dd"));
						List<TraderInfo> traderInfoList = traderDao
								.getTraderInfoByPartitiondate(order.getId(),
										DateUtil.format(
												order.getPartitionDate(),
												"yyyy-MM-dd"));
						TraderInfo sender = new TraderInfo();
						TraderInfo recevier = new TraderInfo();
						for (TraderInfo info : traderInfoList) {
							if ("0".equals(info.getTradeType())) {
								sender = info;
							} else {
								recevier = info;
							}
						}
						logger.error("查询构建数据用时"
								+ (System.currentTimeMillis() - t4));
						// 线上订单更新面单号
						/*
						 * if (StringUtils.isBlank(order.getMailNo())) {
						 * mailObjectDTOs.setBuildTask("1");
						 * 
						 * } else { mailObjectDTOs.setBuildTask("2");
						 * mailObjectDTOs.setMailNo(order.getMailNo()); //
						 * 面单号设置的是旧的面单号 }
						 */
						long t5 = System.currentTimeMillis();
						this.constructSolrObject(mailObjectDTOs, order,
								recevier, sender);
						eccoreSearchService.buildPartEccoreData(
								ConfigUtilSingle.getInstance()
										.getSolrEccoreUrl(), mailObjectDTOs);

						logger.error("写入solr用时"
								+ (System.currentTimeMillis() - t5) + "单号"
								+ mailObjectDTOs.getMailNo());
					}
				}
				logger.info("单号" + updateInfo.getMailNo() + "写buildSearch用时"
						+ (System.currentTimeMillis() - t4));
			}

			long t3 = System.currentTimeMillis();
			if (StringUtils.equals(updateInfo.getInfoContent(), "SIGNED")
					&& !StringUtils.isEmpty(updateInfo.getMailNo())) {
				Map<String, Object> qsnMap = new HashMap<String, Object>();
				qsnMap.put("mailNo", updateInfo.getMailNo());
				qsnMap.put("orderStatus", "1");
				questionnaireDao.updateOrderStatusByMailNo(qsnMap);
				logger.info("单号" + updateInfo.getMailNo() + "写questionnaire用时"
						+ (System.currentTimeMillis() - t3));
			}
		}
		return result;
	}

	@Override
	public List<T> getOrderByStatus(String status) {
		List<T> list = null;
		return list;
	}

	@Override
	public List<T> getOrderMonitorList(Integer status, String startTime,
			String endTime, Integer vipId, String mailNO, String buyerName,
			String buyerPhone, Integer orderBy, Pagination pagination,
			boolean flag, List bindedId) {
		List<T> list = null;
		Map map = new HashMap();
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		if (status != null)
			map.put("status", status);
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (mailNO != null && !mailNO.equals(""))
			map.put("mailNo", mailNO);
		if (buyerName != null && !(buyerName.trim()).equals(""))
			map.put("buyerName", buyerName.trim());
		if (buyerPhone != null && !(buyerPhone.trim()).equals(""))
			map.put("buyerPhone", buyerPhone.trim());
		// if(vipId!=null){
		// User user = userService.getUserById(vipId);
		// // map.put("vipId", user.getTaobaoEncodeKey());
		// // 要把子账号一起查询
		// map.put("vipId", getCustomerIds(vipId.toString() ) );
		// }

		// 关联账号
		if (bindedId != null && bindedId.size() > 0) {
			// List tlist =(List)map.get("vipId");
			// tlist.addAll(bindedId);
			map.put("vipId", bindedId);
		} else
			return null;

		if (orderBy != null)
			map.put("orderBy", orderBy);
		list = dao.getOrderMonitorList(map);
		return list;
	}

	@Override
	public int countOrderMonitorList(Integer status, String startTime,
			String endTime, Integer vipId, String mailNO, String buyerName,
			String buyerPhone, List bindedId) {
		int count = 0;
		Map map = new HashMap();
		if (status != null)
			map.put("status", status);
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (mailNO != null && !mailNO.equals(""))
			map.put("mailNo", mailNO);
		if (buyerName != null && !(buyerName.trim()).equals(""))
			map.put("buyerName", buyerName.trim());
		if (buyerPhone != null && !(buyerPhone.trim()).equals(""))
			map.put("buyerPhone", buyerPhone.trim());
		if (bindedId != null && bindedId.size() > 0) {
			map.put("vipId", bindedId);
		} else {
			return 0;
		}
		count = dao.countOrderMonitorList(map);
		return count;
	}

	@Override
	public List<T> getMerchantMonitorOrderList(Integer status,
			String startTime, String endTime, Integer vipId, String mailNO,
			String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, boolean flag) {
		List<T> list = null;
		Map map = new HashMap();
		List<String> vipList = new ArrayList<String>();
		/**
		 * 根据当前平台用户的类型，获取该用户来源下的所有卖家用户
		 */
		User curPlatformUser = userService.getUserById(vipId);// 获取当前平台用户
		List<User> userList = userService
				.searchUsersByUserSource(curPlatformUser.getUserSource());
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null)
					vipList.add(user.getTaobaoEncodeKey());
			}
		}
		if (curPlatformUser.getTaobaoEncodeKey() != null)
			vipList.add(curPlatformUser.getTaobaoEncodeKey());
		map.put("vipList", vipList);
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		if (status != null)
			map.put("status", status);
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (mailNO != null && !mailNO.equals(""))
			map.put("mailNo", mailNO);
		if (buyerName != null && !(buyerName.trim()).equals(""))
			map.put("buyerName", buyerName.trim());
		if (buyerPhone != null && !(buyerPhone.trim()).equals(""))
			map.put("buyerPhone", buyerPhone.trim());
		if (orderBy != null)
			map.put("orderBy", orderBy);
		list = dao.getMerchantMonitorOrderList(map);
		return list;
	}

	@Override
	public int countMerchantMonitorOrderList(Integer status, String startTime,
			String endTime, Integer vipId, String mailNO, String buyerName,
			String buyerPhone) {
		int count = 0;
		Map map = new HashMap();
		List<String> vipList = new ArrayList<String>();
		/**
		 * 根据当前平台用户的类型，获取该用户来源下的所有卖家用户
		 */
		User curPlatformUser = userService.getUserById(vipId);// 获取当前平台用户
		List<User> userList = userService
				.searchUsersByUserSource(curPlatformUser.getUserSource());
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null)
					vipList.add(user.getTaobaoEncodeKey());
			}
		}
		if (curPlatformUser.getTaobaoEncodeKey() != null)
			vipList.add(curPlatformUser.getTaobaoEncodeKey());
		map.put("vipList", vipList);
		if (status != null)
			map.put("status", status);
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (mailNO != null && !mailNO.equals(""))
			map.put("mailNo", mailNO);
		if (buyerName != null && !(buyerName.trim()).equals(""))
			map.put("buyerName", buyerName.trim());
		if (buyerPhone != null && !(buyerPhone.trim()).equals(""))
			map.put("buyerPhone", buyerPhone.trim());
		count = dao.countMerchantMonitorOrderList(map);
		return count;
	}

	@Override
	public List getOrderMailNoListByTime(String startTime, String endTime,
			Integer vipId, List bindedId) {
		List list = null;
		Map map = new HashMap();
		if (vipId != null && !vipId.equals("")) {
			map.put("vipId", getCustomerIds(vipId.toString()));
			// 关联账号
			if (bindedId != null && bindedId.size() > 0) {
				List tlist = (List) map.get("vipId");
				tlist.addAll(bindedId);
				map.put("vipId", tlist);
			}

		} else
			return null;
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		list = dao.getOrderMailNoListByTime(map);
		return list;
	}

	@Override
	public List getOrderMailNoListByBuyerInfo(String startTime, String endTime,
			Integer vipId, String buyerName, String buyerPhone, List bindedId) {
		List list = null;
		Map map = new HashMap();
		if (vipId == null || vipId.equals(""))
			return null;
		else {
			User curUser = userService.getUserById(vipId);

			if (curUser.getTaobaoEncodeKey() == null
					|| ("").equals(curUser.getTaobaoEncodeKey()))
				return null;
			else {
				List tplist = new ArrayList();
				tplist.add(curUser.getTaobaoEncodeKey());
				if (bindedId != null && bindedId.size() > 0)
					tplist.addAll(bindedId);
				map.put("vipId", tplist);

			}
		}
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (buyerName != null && !buyerName.trim().equals(""))
			map.put("buyerName", buyerName.trim());
		if (buyerPhone != null && !buyerPhone.trim().equals(""))
			map.put("buyerPhone", buyerPhone.trim());
		list = dao.getOrderMailNoListByBuyerInfo(map);
		return list;
	}

	@Override
	public List cleanOrderList(String partitiondate, String startTime,
			String endTime, Integer limit) throws DataAccessException {
		List list = null;
		Map map = new HashMap();
		if (partitiondate != null && !partitiondate.equals(""))
			map.put("partitiondate", partitiondate);
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (limit != null && !limit.equals(""))
			map.put("limit", limit);
		list = dao.cleanOrderList(map);
		return list;
	}

	@Override
	public List<T> queryOrderListByMailNo(String mailNo)
			throws DataAccessException {
		List<T> list = null;
		try {
			if (mailNo != null)
				list = dao.queryOrderListByMailNo(mailNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public T queryOrderTranderInfoByMailNo(String mailNo) {
		T entity = null;
		try {
			if (mailNo != null) {
				List<T> list = dao.queryOrderTranderInfoByMailNo(mailNo);
				if (list != null && list.size() > 0)
					entity = list.get(0);
			}
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	/**
	 * 根据用户的ID取当前用户的customerId和子账号的customerId
	 * 
	 * @param vipId
	 * @return
	 */
	private List getCustomerIds(String vipId) {
		List list = new ArrayList();
		User user = userService.getUserById(Integer.valueOf(vipId));
		if (user != null) {
			list.add(user.getTaobaoEncodeKey());

			List<User> subUserList = userService
					.getUserByParentId(user.getId());
			if (subUserList != null) {
				for (User subUser : subUserList) {
					String customerId = subUser.getTaobaoEncodeKey();
					// 过滤空值
					if (!StringUtils.isEmpty(customerId)) {
						list.add(customerId);
					}
				}
			}
		}

		return list;
	}

	@Override
	public List<T> queryOrderListByMailNoAndCustomerid(String mailNo,
			Integer currentUserId) {
		List<T> list = null;
		Map map = new HashMap();
		List<String> customerIdList = new ArrayList<String>();
		if (currentUserId == null || currentUserId.equals(""))
			return null;
		else {
			User curUser = userService.getUserById(currentUserId);
			if (curUser.getUserType() == null
					|| curUser.getUserType().equals(""))
				return null;
			if (curUser.getUserType().equals("1")
					|| curUser.getUserType().equals("11")
					|| curUser.getUserType().equals("12")
					|| curUser.getUserType().equals("13")) {
				customerIdList = Resource.getBindedCustomerIdList(curUser);
			} else if (curUser.getUserType().equals("4")) {
				List<User> userList = userService.pingTaiSelect(curUser, 0);
				if (curUser.getTaobaoEncodeKey() != null
						&& !("").equals(curUser.getTaobaoEncodeKey()))
					customerIdList.add(curUser.getTaobaoEncodeKey());
				if (userList != null && userList.size() > 0) {
					for (User u : userList) {
						if (u.getTaobaoEncodeKey() != null
								&& !("").equals(u.getTaobaoEncodeKey()))
							customerIdList.add(u.getTaobaoEncodeKey());
					}
				}
			} else if (curUser.getUserType().equals("2")
					|| curUser.getUserType().equals("21")
					|| curUser.getUserType().equals("22")
					|| curUser.getUserType().equals("23")) {
				List<User> userList = userService.searchUsersBySiteAndUserType(
						curUser.getSite(), "1");
				if (userList != null && userList.size() > 0) {
					for (User user : userList) {
						customerIdList.addAll(Resource
								.getBindedCustomerIdList(user));
					}
				}
			} else {
				customerIdList = null;
			}
			if (customerIdList.size() > 0)
				map.put("customerIdList", customerIdList);
			else
				return null;
		}
		map.put("mailNo", mailNo);
		return dao.queryOrderListByMailNoAndCustomerid(map);
	}

	@Override
	public List relatedOrderMailNoListByTime(String startTime, String endTime,
			String userCode, Date ucUpdateTime) {
		List list = null;
		Map map = new HashMap();
		if (userCode != null && !userCode.trim().equals("")) {
			if (getCustomerIdByUserCode(userCode) != null)
				map.put("customerId", getCustomerIdByUserCode(userCode));
			else
				return null;
		} else {
			return null;
		}
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);

		if (ucUpdateTime != null) {
			map.put("ucUpdateTime", ucUpdateTime);
		}
		list = dao.relatedOrderMailNoListByTime(map);
		return list;
	}

	@Override
	public List relatedOrderMailNoListByBuyerInfo(String startTime,
			String endTime, String userCode, String buyerName,
			String buyerPhone, Date ucUpdateTime) {
		List list = null;
		Map map = new HashMap();
		if (userCode != null && !userCode.trim().equals("")) {
			List<String> customerIdList = getCustomerIdByUserCode(userCode);
			if (getCustomerIdByUserCode(userCode) != null) {
				map.put("customerId", getCustomerIdByUserCode(userCode));
			} else
				return null;
		} else {
			return null;
		}
		if (startTime != null && !startTime.equals(""))
			map.put("startTime", startTime);
		if (endTime != null && !endTime.equals(""))
			map.put("endTime", endTime);
		if (buyerName != null && !buyerName.trim().equals(""))
			map.put("buyerName", buyerName.trim());
		if (buyerPhone != null && !buyerPhone.trim().equals(""))
			map.put("buyerPhone", buyerPhone.trim());
		if (ucUpdateTime != null) {
			map.put("ucUpdateTime", ucUpdateTime);
		}
		list = dao.relatedOrderMailNoListByBuyerInfo(map);
		return list;
	}

	@Override
	public T getOrderbyMailNoAndCustomerId(String mailNo, List customerId)
			throws DataAccessException {
		Map map = new HashMap();
		if (mailNo == null || customerId == null)
			return null;
		if (mailNo.trim().equals("") || customerId.size() <= 0)
			return null;
		map.put("mailNo", mailNo);
		map.put("customerId", customerId);
		List<T> list = dao.getOrderbyMailNoAndCustomerId(map);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public List getBuyerInfoByMailNo(String mailNo) throws DataAccessException {
		return dao.getBuyerInfoByMailNo(mailNo);
	}

	/**
	 * 查找同一用户编码下已激活的卖家
	 * 
	 * @param userCode
	 * @return
	 */
	private List<String> getCustomerIdByUserCode(String userCode) {
		List<String> list = new ArrayList<String>();
		List<User> userList = userService.searchUsersByCodeTypeState(userCode,
				"1", "1");// 查找同一用户编码下已激活的买家
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null
						&& !(user.getTaobaoEncodeKey().equals("")))
					list.add(user.getTaobaoEncodeKey());
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
	 * 查找同一用户编码下已激活的卖家
	 * 
	 * @param userCode
	 * @return
	 */
	private String getStringCustomerIdByUserCode(String userCode) {
		List<String> list = new ArrayList<String>();
		String str = "";
		List<User> userList = userService.searchUsersByCodeTypeState(userCode,
				"1", "1");// 查找同一用户编码下已激活的买家
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null
						&& !(user.getTaobaoEncodeKey().equals(""))) {
					if (str.equals(""))
						str = user.getTaobaoEncodeKey();
					else
						str += "," + user.getTaobaoEncodeKey();
				}

			}
		}
		return str;
	}

	@Override
	public List getOrderbyMailNoCustomerId(String mailNo, String customerId)
			throws DataAccessException {
		Map map = new HashMap();
		if (mailNo == null || customerId == null)
			return null;
		if (mailNo.trim().equals("") || customerId.trim().equals(""))
			return null;
		map.put("mailNo", mailNo);
		map.put("customerId", customerId);
		return dao.getOrderbyMailNoCustomerId(map);
	}

	@Override
	public List getOrderbyMailNoCustomerId(List mailNo, List customerId)
			throws DataAccessException {
		Map map = new HashMap();

		map.put("mailNo", mailNo);
		map.put("customerId", customerId);
		return dao.getOrderbyMailNoCustomerId(map);
	}

	@Override
	public List getOrderbyMailNoCustomerId(Map map) throws DataAccessException {
		return dao.getOrderbyMailNoCustomerId(map);
	}

	@Override
	public int countCurrentDayOrderByCustormerId(List<String> customerIdList,
			String dateString) throws DataAccessException {
		int count = 0;
		Map map = new HashMap();
		if (customerIdList != null && customerIdList.size() > 0) {
			map.put("customerIdList", customerIdList);
		} else
			return count;
		if (dateString != null && !dateString.equals(""))
			map.put("partitionDate", dateString);
		count = dao.countCurrentDayOrderByCustormerId(map);
		return count;
	}

	@Override
	public boolean sendOrders(List<Order> orders) {
		return false;
	}

	@Override
	// public Map queryOrderPrint(Map map, Pagination<T> pagination) {
	public List<DtoOrderPrint> queryOrderPrint(Map map, Pagination<T> pagination) {
		boolean flag = true; // 分页翻页
		if (pagination == null)
			flag = false;

		// 卖家
		if (map.get("relatedId") != null
				&& ((List) map.get("relatedId")).size() > 0) {
			map.put("customerId", (List) map.get("relatedId"));
		}
		// 网点
		if (map.get("userCode") != null && !map.get("userCode").equals("")) {// 网点根据用户编码查询
			if (this.getCustomerIdByUserCode(map.get("userCode").toString()) != null) {
				map.put("customerId",
						getCustomerIdByUserCode(map.get("userCode").toString()));
			}
		}

		map.put("startIndex", pagination.getStartIndex());
		map.put("pageNum", pagination.getPageNum());

		List<DtoOrderPrint> dtoList = new ArrayList<DtoOrderPrint>(); // dtoList
		List<T> orderList = dao.queryOrderPrint(map);
		for (T order : orderList) {
			DtoOrderPrint dtoOrderPrint = getDtoOrderPrint(order);
			dtoList.add(dtoOrderPrint);
		}
		return dtoList;
		// map.put("list", dtoList);
		// return map;
	}

	@Override
	public int countOrderByStatusAndCustormerId(Integer status,
			String startTime, String endTime, List<String> custormerId)
			throws DataAccessException {
		int count = 0;
		Map map = new HashMap();
		if (status != null)
			map.put("status", status);
		if (startTime != null && !startTime.equals("") && endTime != null
				&& !endTime.equals("")) {
			map.put("startTime", startTime);
			map.put("endTime", endTime);
		}
		if (custormerId != null && custormerId.size() > 0) {
			map.put("custormerId", custormerId);
		} else
			return count;
		count = dao.countOrderByStatusAndCustormerId(map);
		return count;
	}

	@Override
	public Map<String, Number> getPieChart(String someDay,
			List<String> custormerId) {
		if (someDay == null || custormerId == null) {
			logger.error("卖家没有选择日期或者卖家没有关联账号");
			return null;
		}
		/**
		 * status分别取值0、1、2、3代表接单中、已签收、正在派送订单、走件中订单。
		 * 键分别命名为"ACCEPT","SIGNED","SENT_SCAN","ONGOING"
		 */
		Map<String, Number> map = new HashMap<String, Number>();
		int ACCEPT_NUM = this.countOrderByStatusAndCustormerId(0, someDay,
				someDay, custormerId);
		int SIGNED_NUM = this.countOrderByStatusAndCustormerId(1, someDay,
				someDay, custormerId);
		int SENT_SCAN_NUM = this.countOrderByStatusAndCustormerId(2, someDay,
				someDay, custormerId);
		int ONGOING_NUM = this.countOrderByStatusAndCustormerId(3, someDay,
				someDay, custormerId);
		// 首先获取这四种订单的总数
		int total_num = ACCEPT_NUM + SIGNED_NUM + SENT_SCAN_NUM + ONGOING_NUM;
		if (total_num != 0) {
			/**
			 * 计算各个状态下的百分比
			 */
			Double ACCEPT = (double) Math.round((ACCEPT_NUM * 100) / total_num);
			Double SIGNED = (double) Math.round((SIGNED_NUM * 100) / total_num);
			Double SENT_SCAN = (double) Math.round((SENT_SCAN_NUM * 100)
					/ total_num);
			Double ONGOING = (double) Math.round((ONGOING_NUM * 100)
					/ total_num);
			if (ONGOING_NUM != 0) {
				ONGOING = 100 - ACCEPT - SIGNED - SENT_SCAN;
			} else if (SENT_SCAN_NUM != 0) {
				SENT_SCAN = 100 - ACCEPT - SIGNED;
			} else if (SIGNED_NUM != 0) {
				SIGNED = 100 - ACCEPT;
			} else if (ACCEPT != 0) {
				ACCEPT = 100.0;
			}

			map.put("ACCEPT", ACCEPT);
			map.put("SIGNED", SIGNED);
			map.put("SENT_SCAN", SENT_SCAN);
			map.put("ONGOING", ONGOING);
		}
		return map;
	}

	@Override
	public List<Order> queryOrderByPage(QueryOrder queryOrder,
			Pagination pagination) {
		Map map = new HashMap();
		if (queryOrder.getCustomerIds() != null
				&& queryOrder.getCustomerIds().size() == 0) {
			map.put("customerIds", null);
		} else {
			map.put("customerIds", queryOrder.getCustomerIds());
		}
		map.put("startIndex", pagination.getStartIndex());
		map.put("pageNum", pagination.getPageNum());
		map.put("startTime", queryOrder.getStartTime());
		map.put("endTime", queryOrder.getEndTime());
		map.put("orderType", queryOrder.getOrderType());
		if ("".equals(queryOrder.getBuyName())) {
			map.put("buyName", null);
		} else {
			map.put("buyName", queryOrder.getBuyName());
		}
		map.put("txLogisticId",
				StringUtil.isBlank(queryOrder.getMailNo()) ? null : queryOrder
						.getMailNo());
		return (List<Order>) dao.queryOrderList(map);
	}

	@Override
	public int countOrders(QueryOrder queryOrder) {
		Map map = new HashMap();
		if (queryOrder.getCustomerIds() != null
				&& queryOrder.getCustomerIds().size() == 0) {
			map.put("customerIds", null);
		} else {
			map.put("customerIds", queryOrder.getCustomerIds());
		}
		map.put("startTime", queryOrder.getStartTime());
		map.put("endTime", queryOrder.getEndTime());
		map.put("orderType", queryOrder.getOrderType());
		if ("".equals(queryOrder.getBuyName())) {
			map.put("buyName", null);
		} else {
			map.put("buyName", queryOrder.getBuyName());
		}
		map.put("txLogisticId",
				StringUtil.isBlank(queryOrder.getMailNo()) ? null : queryOrder
						.getMailNo());
		return dao.countOrders(map);
	}

	// 根据order得到一个dtoOrderPrint对象
	private DtoOrderPrint getDtoOrderPrint(T order) {
		DtoOrderPrint dtoOrderPrint = new DtoOrderPrint(); // 订单打印的dto对象
		dtoOrderPrint.setOrder(order);

		// 交易人信息
		List<TraderInfo> traderList = traderDao.getTraderInfoByOrderId(order
				.getId());
		for (TraderInfo trader : traderList) {
			// 卖家:0/买家:1
			if ("0".equals(trader.getTradeType())) { // 卖家
				dtoOrderPrint.setSname(trader.getName());
				dtoOrderPrint.setSmobile(trader.getMobile());
				dtoOrderPrint.setSphone(trader.getPhone());
				dtoOrderPrint.setSpostcode(trader.getPostCode());
				dtoOrderPrint.setSprov(trader.getProv());
				dtoOrderPrint.setScity(trader.getCity());
				dtoOrderPrint.setSdistrict(trader.getDistrict());
				dtoOrderPrint.setSaddress(trader.getAddress());

				User user = userService.getUserByCustomerId(order
						.getCustomerId());
				if (user != null) {
					dtoOrderPrint.setSshopname(user.getShopName());
				} else {
					logger.info("customerId对应多个用户.customerId:"
							+ order.getCustomerId());
				}
				continue;
			}
			if ("1".equals(trader.getTradeType())) { // 买家
				dtoOrderPrint.setBname(trader.getName());
				dtoOrderPrint.setBmobile(trader.getMobile());
				dtoOrderPrint.setBphone(trader.getPhone());
				dtoOrderPrint.setBpostcode(trader.getPostCode());
				dtoOrderPrint.setBprov(trader.getProv());
				dtoOrderPrint.setBcity(trader.getCity());
				dtoOrderPrint.setBdistrict(trader.getDistrict());
				dtoOrderPrint.setBaddress(trader.getAddress());
			}
		}

		// 产品信息
		List<Product> proList = productDao.getProductListByOrder(order.getId());
		dtoOrderPrint.setProduct(proList);
		return dtoOrderPrint;
	}

	private static String dataTemp = "<item>"
			+ "<store_name>${store_name}</store_name>" // 网店名称
			+ "<ship_name>${ship_name}</ship_name>" // 收货人-姓名
			+ "<ship_regionFullName>${ship_regionFullName}</ship_regionFullName>" // 收货人-地区
			+ "<ship_address>${ship_address}</ship_address>" // 收货人-地址
			+ "<ship_tel>${ship_tel}</ship_tel>" //
			+ "<ship_zip>${ship_zip}</ship_zip>" // 邮编
			+ "<order_no>${order_no}</order_no>" // 订单号
			+ "<shipment_item_count>${shipment_item_count}</shipment_item_count>" // 发货单-物品数量
			+ "<shipment_no>${shipment_no}</shipment_no>" // 发货单号改为商品名称
			+ "<dly_name>${dly_name}</dly_name>" // 发货人-姓名
			// +"<mail_no>${mail_no}</mail_no>" // 面单号
			+ "<dly_regionFullName>${dly_regionFullName}</dly_regionFullName>" // 国家，省，市，区
			+ "<dly_address>${dly_address}</dly_address>" //
			+ "<dly_tel>${dly_tel}</dly_tel>" + "<dly_zip>${dly_zip}</dly_zip>"
			+ "<dly_mobile>${dly_mobile}</dly_mobile>"
			+ "<date_year>${date_year}</date_year>"
			+ "<date_moth>${date_moth}</date_moth>"
			+ "<date_day>${date_day}</date_day>" + "</item>";

	@Override
	public Map queryOrderPrintData(Map map) {

		if (map.get("orderExpId") == null || "0".equals(map.get("orderExpId"))) {
			logger.info("打印模板不存在");
			return Collections.EMPTY_MAP;
		}
		if (map.get("oids") == null) {
			logger.info("参数错误");
			return Collections.EMPTY_MAP;
		}

		String[] oids = map.get("oids").toString().split(",");
		// 绑定运单号, 状态已打印
		List<String> oidsList = new ArrayList<String>();
		// List<String> mailNoList = new ArrayList<String>();
		for (String str : oids) {
			String[] temp = str.split("#");
			if (temp.length < 2) {
				logger.info("#面单打印#我要发货：数据格式异常![异常数据:" + str + "]");
				// continue;
				return Collections.EMPTY_MAP;
			}
			String id = temp[0];
			String mailNo = temp[1];
			oidsList.add(id);
			// mailNoList.add(mailNo);
			T order = dao.getOrderById(Integer.valueOf(id));
			order.setMailNo(mailNo);
			order.setStatus("PRINTED"); // 订单状态：已打印
			if (dao.updateOrder(order)) { // 修改
				logger.info("#面单打印#打印：绑定运单号![orderId:" + id + ";mailNo:"
						+ mailNo + "]");
			} else {
				logger.info("#面单打印#打印：绑定运单号-修改失败![orderId:" + id + ";mailNo:"
						+ mailNo + "]");
			}
		}

		map.put("orderIdList", oidsList);
		// map.put("orderMailNoList", mailNoList);

		List<DtoOrderPrint> orderPrintList = new ArrayList<DtoOrderPrint>(); // dtoList
		List<T> orderList = dao.queryOrderList(map);
		for (T order : orderList) {
			DtoOrderPrint dtoOrderPrint = getDtoOrderPrint(order);
			orderPrintList.add(dtoOrderPrint);
		}

		StringBuffer temp = new StringBuffer();
		int index = 0;
		for (DtoOrderPrint orderPrint : orderPrintList) {
			temp.append(new StringBuffer(dataTemp));

			index = temp.indexOf("${store_name}");
			temp.delete(index, index + "${store_name}".length()).insert(index,
					orderPrint.getSshopname());

			index = temp.indexOf("${ship_name}");
			temp.delete(index, index + "${ship_name}".length()).insert(index,
					orderPrint.getBname());

			index = temp.indexOf("${ship_regionFullName}");
			temp.delete(index, index + "${ship_regionFullName}".length())
					.insert(index,
							orderPrint.getBprov() + "," + orderPrint.getBcity()
									+ "," + orderPrint.getBdistrict());

			index = temp.indexOf("${ship_address}");
			temp.delete(index, index + "${ship_address}".length()).insert(
					index, orderPrint.getBaddress());

			index = temp.indexOf("${ship_tel}");
			temp.delete(index, index + "${ship_tel}".length()).insert(index,
					orderPrint.getBphone());

			index = temp.indexOf("${ship_zip}");
			temp.delete(index, index + "${ship_zip}".length()).insert(index,
					orderPrint.getBpostcode());

			index = temp.indexOf("${order_no}");
			temp.delete(index, index + "${order_no}".length()).insert(index,
					orderPrint.getOrder().getTradeNo());

			index = temp.indexOf("${shipment_item_count}");
			temp.delete(index, index + "${shipment_item_count}".length())
					.insert(index, orderPrint.getProduct().size() + "");

			index = temp.indexOf("${shipment_no}");
			temp.delete(index, index + "${shipment_no}".length()).insert(index,
					orderPrint.getOrder().getTxLogisticId());

			index = temp.indexOf("${dly_name}");
			temp.delete(index, index + "${dly_name}".length()).insert(index,
					orderPrint.getSname());

			index = temp.indexOf("${dly_regionFullName}");
			temp.delete(index, index + "${dly_regionFullName}".length())
					.insert(index,
							orderPrint.getSprov() + "," + orderPrint.getScity()
									+ "," + orderPrint.getSdistrict());

			index = temp.indexOf("${dly_address}");
			temp.delete(index, index + "${dly_address}".length()).insert(index,
					orderPrint.getSaddress());

			index = temp.indexOf("${dly_tel}");
			temp.delete(index, index + "${dly_tel}".length()).insert(index,
					orderPrint.getSphone());

			index = temp.indexOf("${dly_zip}");
			temp.delete(index, index + "${dly_zip}".length()).insert(index,
					orderPrint.getSpostcode());

			index = temp.indexOf("${dly_mobile}");
			temp.delete(index, index + "${dly_mobile}".length()).insert(index,
					orderPrint.getSmobile());

			index = temp.indexOf("${date_year}");
			temp.delete(index, index + "${date_year}".length())
					.insert(index,
							DateUtil.toShortYear(orderPrint.getOrder()
									.getCreateTime()));

			index = temp.indexOf("${date_moth}");
			temp.delete(index, index + "${date_moth}".length()).insert(index,
					DateUtil.toMonth(orderPrint.getOrder().getCreateTime()));

			index = temp.indexOf("${date_day}");
			temp.delete(index, index + "${date_day}".length()).insert(index,
					DateUtil.toShortdd(orderPrint.getOrder().getCreateTime()));
		}

		OrderExpress orderExpress = orderExpressDao.getOrderExpressById(Integer
				.valueOf(map.get("orderExpId").toString()));
		// xml , orderNum,data,bg
		// String xml =
		// "%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Eship_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E487%3A114%3A73%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Eship_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E607%3A171%3A172%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u7535%u8BDD%3C/name%3E%3Cucode%3Eship_tel%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E665%3A114%3A105%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Eship_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E693%3A229%3A88%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Eship_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E501%3A172%3A80%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Edly_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E127%3A98%3A81%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Edly_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E229%3A159%3A162%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Edly_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E134%3A161%3A68%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Edly_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E307%3A228%3A93%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u624B%u673A%3C/name%3E%3Cucode%3Edly_mobile%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E142%3A223%3A108%3A20%3C/position%3E%3C/item%3E%3C/printer%3E";
		// String bg =
		// "http://static.imall114.com/media//default/order/yto.jpg";
		// images/printtemp/yto_08.jpg
		// map.put("data", "<data>"+dataTemp+"</data>");
		/*
		 * try { String xml = URLEncoder.encode(orderExpress.getTemplatedata(),
		 * CharEncoding.UTF_8); map.put("xml", xml); } catch
		 * (UnsupportedEncodingException e) {
		 * logger.error("对打印模板定位数据进行"+CharEncoding.UTF_8+"编码出错", e); }
		 */
		map.put("xml", orderExpress.getTemplatedata()); // 数据库中保存的就是编码后的
		map.put("data", "<data>" + temp.toString() + "</data>");
		map.put("orderNum", orderPrintList.size());// orderPrintList.size()
		map.put("bg", orderExpress.getBackgroundimageurl());

		return map;
	}

	@Override
	public boolean updateMailNoById(String id, String mailNo) {
		Map map = new HashMap();
		map.put("id", id);
		map.put("mailNo", mailNo);
		return dao.updateMailNoById(map);
	}

	@Override
	public boolean toSendOrder(Map map) {

		String[] oids = map.get("oids").toString().split(",");
		// 修改运单号/订单状态
		List<String> oidsList = new ArrayList<String>();
		for (String str : oids) {
			String[] temp = str.split("#");
			if (temp.length < 2) {
				logger.info("#面单打印#我要发货：数据格式异常![异常数据:" + str + "]");
				return false;
			}
			String id = temp[0];
			String mailNo = temp[1];
			oidsList.add(id);

			T order = dao.getOrderById(Integer.valueOf(id));
			order.setMailNo(mailNo);
			// 发货之后修改订单状态为创建
			order.setStatus("0");
			if (dao.updateOrder(order)) { // 修改
				logger.info("#面单打印#我要发货：修改运单号![orderId:" + id + ";mailNo:"
						+ mailNo + "]");
			} else {
				logger.info("#面单打印#我要发货：修改运单号-修改失败![orderId:" + id + ";mailNo:"
						+ mailNo + "]");
			}
		}

		map.put("orderIdList", oidsList);
		List<T> orderList = dao.queryOrderList(map);

		// 同步到金刚
		boolean flagJG = toJgWaybill(orderList);

		return flagJG;
	}

	/**
	 * 发货之后同步到淘宝
	 * 
	 * @param orderList
	 * @return
	 * @throws ApiException
	 */
	/*
	 * @Override public boolean toTaobao(List<T> orderList) {
	 * 
	 * sessionMap = ActionContext.getContext().getSession(); String appKey =
	 * (String) sessionMap.get("appKey"); String sessionKey = (String)
	 * sessionMap.get("sessionKey"); try { TaobaoClient client = new
	 * DefaultTaobaoClient(TaobaoConstant.URL, appKey,
	 * TaobaoConstant.APP_SERCET); LogisticsOnlineSendRequest req = new
	 * LogisticsOnlineSendRequest(); for (T order : orderList) { DtoOrderPrint
	 * dtoOrderPrint = getDtoOrderPrint(order); if
	 * (dtoOrderPrint.getOrder().getLineType().equals("1")) {
	 * req.setTid(Long.valueOf(dtoOrderPrint.getOrder() .getTradeNo())); //
	 * 淘宝交易id req.setOutSid(dtoOrderPrint.getOrder().getMailNo()); // 运单号
	 * req.setCompanyCode(dtoOrderPrint.getOrder() .getLogisticProviderId()); //
	 * 物流公司代码 req.setSenderId(null); // 默认卖家取货地址 req.setCancelId(null); //
	 * 默认卖家退货地址 req.setFeature(null); } } LogisticsOnlineSendResponse response =
	 * client.execute(req, sessionKey); } catch (ApiException e) {
	 * e.printStackTrace(); return false; } return true; }
	 */

	/**
	 * 发货之后同步到 金刚
	 * 
	 * @param orderList
	 * @return
	 */
	@Override
	public boolean toJgWaybill(List<T> orderList) {
		List<JgWaybill> jgWaybillList = new ArrayList<JgWaybill>(); // dtoList
		for (T order : orderList) {
			DtoOrderPrint dtoOrderPrint = getDtoOrderPrint(order);

			JgWaybill jgWaybill = new JgWaybill();
			jgWaybill.setMailNo(dtoOrderPrint.getOrder().getMailNo());
			jgWaybill.setLogisticId(dtoOrderPrint.getOrder().getTxLogisticId());
			jgWaybill.setLineType(dtoOrderPrint.getOrder().getLineType());
			jgWaybill.setClientID(dtoOrderPrint.getOrder().getClientId());
			jgWaybill.setsName(dtoOrderPrint.getSname());
			jgWaybill.setsPostCode(dtoOrderPrint.getSpostcode());
			jgWaybill.setsPhone(dtoOrderPrint.getSphone());
			jgWaybill.setsMobile(dtoOrderPrint.getSmobile());
			jgWaybill.setsProv(dtoOrderPrint.getSprov());
			jgWaybill.setsCity(dtoOrderPrint.getScity());
			jgWaybill.setsDistrict(dtoOrderPrint.getSdistrict());
			jgWaybill.setsAddress(dtoOrderPrint.getSaddress());

			jgWaybill.setbName(dtoOrderPrint.getBname());
			jgWaybill.setbPostCode(dtoOrderPrint.getBpostcode());
			jgWaybill.setbPhone(dtoOrderPrint.getBphone());
			jgWaybill.setbMobile(dtoOrderPrint.getBmobile());
			jgWaybill.setbProv(dtoOrderPrint.getBprov());
			jgWaybill.setbCity(dtoOrderPrint.getBcity());
			jgWaybill.setbDistrict(dtoOrderPrint.getBdistrict());
			jgWaybill.setbAddress(dtoOrderPrint.getBaddress());

			jgWaybillList.add(jgWaybill);
		}
		return jgWaybillService.sendPrintOrders(jgWaybillList);
	}

	@Override
	public Map ecAccountList(Map map,
			Pagination<EccoreSearchResultDTO> paginationDTO,
			Integer posttempId, Posttemp sysPt) {
		boolean flag = true; // true/false = 分页/不分页
		int totalRecords = 0; // 总记录数
		float totalFreight = 0; // 总运费; 等于订单数据的实收运费累加
		float specialTotalFreight = 0; // 特殊运单总运费
		List<T> specialOrderList = new ArrayList<T>(); // 特殊运单list
		List<T> returnedOrderList = new ArrayList<T>(); // 退货订单list
		float returnedTotalFreight = 0; // 退货总费用
		Map resultMap = new Hashtable(); // 返回的结果Map
		List<EccoreSearchResultDTO> listDTO = null;
		List<T> listOrder = new ArrayList<T>();
		List<T> listOrderAll = new ArrayList<T>();
		List<T> errorOrderList = new ArrayList<T>(); // 异常订单会放在这里返回到页面
		Map posttempMap = null;
		String srcProvCode = ""; // 平台用户的srcProvCode在table列表中显示/ 要在order对象中设置
		// 处理平台用户
		if ("4".equals(ObjectUtils.toString(map.get("usertype")))) {
			// 因为所有分仓对账的时候可能有多个不同的分仓用户，会导致有多个不同的运费模板
			// 这里把运费模板存入一个map<costomerId, postinfoMap>;
			// 计算order数据的时候先根据customerId去postinfo对象
			posttempMap = this.buildPosttempInfo(map);
		} else {
			posttempMap = this.buildPosttempInfo(posttempId, sysPt);
			srcProvCode = ObjectUtils.toString(posttempMap
					.get(Constants.SRCPROVCODE)); // 发件地省份
		}
		// 分页
		if (flag) {
			flag = false;
			map.put("curPage", paginationDTO.getStartIndex()); // 设置分页参数
			map.put("pageSize", paginationDTO.getPageNum());
			// 查询数据
			try {
				eccoreSearchService.searchEccoreData(ConfigUtilSingle
						.getInstance().getSolrEccoreUrl(), this
						.buildSearchPage(paginationDTO, map));
			} catch (Exception e) {
				logger.error("#电子对账#从缓存查数据出问题", e);
				return Collections.EMPTY_MAP;
			}
			listDTO = paginationDTO.getRecords(); // 查询结果
			for (EccoreSearchResultDTO dto : listDTO) {
				T order = this.buildOrder(dto); // 根据缓存结果封装order对象
				order = this.calculateOrder(order, srcProvCode, posttempMap); // 计算运费
				listOrder.add(order);
			}
		} // flag(true) 分页查询

		resultMap.put("srcId", srcProvCode); // 卖家在异常页面显示/平台用户不显示这个字段
		resultMap.put("list", listOrder);

		// 不分页
		if (!flag) {
			flag = true;
			map.remove("curPage"); // 去掉分页参数
			map.remove("pageSize");
			// 查询数据
			try {
				eccoreSearchService.searchEccoreData(ConfigUtilSingle
						.getInstance().getSolrEccoreUrl(), this
						.buildSearchPage(paginationDTO, map));
			} catch (Exception e) {
				logger.error("#电子对账#从缓存查数据出问题", e);
			}
			// 查询结果
			listDTO = paginationDTO.getRecords();
			totalRecords = listDTO.size(); // 总记录数
			for (EccoreSearchResultDTO dto : listDTO) {
				T order = this.buildOrder(dto); // 根据缓存结果封装order对象
				order = this.calculateOrder(order, srcProvCode, posttempMap); // 计算运费
				listOrderAll.add(order);

				// 特殊运单
				if ("0".equals(order.getFreightType())) {
					specialOrderList.add(order);
					order.setNetFreight((float) Math.round(order
							.getTrimFreight() * 100) / 100); // 特殊运费
					specialTotalFreight += order.getNetFreight(); // 累加特殊运单的费用
				}
				// 异常订单
				else if ("errorPostinfo".equalsIgnoreCase(order.getRemark())
						|| "errorProv".equalsIgnoreCase(order.getRemark())) {
					errorOrderList.add(order);
					continue; // 不计算费用
				}
				// 退货订单list
				if (order.getOrderType() != null && order.getOrderType() == 3) {
					order.setRemark("thdd");
					returnedOrderList.add(order);
					returnedTotalFreight += order.getNetFreight();
				}

				totalFreight += order.getNetFreight(); // 累加总运费
			}
		} // !flag
			// 减掉退货的钱
		if (returnedOrderList.size() > 0) {
			if (totalFreight < returnedTotalFreight) {
				totalFreight = Math.abs(totalFreight - returnedTotalFreight);
			} else {
				totalFreight = totalFreight - returnedTotalFreight;
			}
		}
		resultMap.put("errorOrderList", errorOrderList);
		resultMap.put("totalFreight",
				(float) Math.round(totalFreight * 100) / 100);
		resultMap.put("totalRecords", totalRecords);
		resultMap.put("specialOrderList", specialOrderList); // 特殊运单
		resultMap.put("specialTotalNum", specialOrderList.size());// 特殊运单总数
		resultMap.put("specialTotalFreight",
				(float) Math.round(specialTotalFreight * 100) / 100); // 特殊运单总运费
		resultMap.put("returnedOrderList", returnedOrderList); // 退货订单

		// 数据对象太大了，及时清除，免OOM
		listDTO.clear();
		listDTO = null;
		paginationDTO.setRecords(null);

		return resultMap;
	}

	/**
	 * 封装参数
	 * 
	 * @param pagination
	 * @param map
	 * @return
	 */
	private Pagination<EccoreSearchResultDTO> buildSearchPage(
			Pagination pagination, Map map) {
		// 电子对账查询运单号非空的订单
		map.put("isDispaly", "0");
		// 卖家
		// solr里的参数是用,分隔字符串
		if (map.get("relatedId") != null) {
			map.put("customerIDs", StringUtils.join(
					((List) map.get("relatedId")).toArray(), ","));
		}

		// 网点根据用户编码查询
		Object objuserCode = map.get("userCode");
		String userCode = StringUtil.nullValue(objuserCode); // 如果为null, 返回""
		if (StringUtils.isNotEmpty(userCode)) {
			if (getCustomerIdByUserCode(userCode) != null) {
				map.put("customerIDs", getStringCustomerIdByUserCode(userCode));
			}
		}

		String startDate = StringUtil.nullValue(map.get("startDate"));
		String endDate = StringUtil.nullValue(map.get("endDate"));
		// 如果没有传日期，就默认设置查询区间为一个月(30天)
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			Date now = new Date();
			endDate = DateUtil.toDay(now); // 结束时间：当前
			startDate = DateUtil.toDay(DateUtil.getDateBefore(now, 30)); // 开始时间：30天前

			map.put("startDate", startDate);
			map.put("endDate", endDate);
		}

		if (StringUtils.isEmpty(ObjectUtils.toString(map.get("pageSize")))) {
			pagination.setPageNum(Integer.MAX_VALUE);
			pagination.setCurrentPage(1);
		}
		/**
		 * map中放入非“UNACCEPT”状态下的订单
		 */
		String relationStr = " OR numStatus:";
		StringBuffer sb = new StringBuffer();
		// sb.append(StatusEnum.CREATE.getValue());
		// sb.append(relationStr);
		sb.append(StatusEnum.SENT_SCAN.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.SIGNED.getValue());
		sb.append(relationStr);
		// sb.append(StatusEnum.ACCEPT.getValue());
		// sb.append(relationStr);
		sb.append(StatusEnum.GOT.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.NOT_SEND.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.FAILED.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.UNKNOW.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.UPDATE.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.TRACKING.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.PRINTED.getValue());
		sb.append(relationStr);
		sb.append(StatusEnum.NOPRINT.getValue());
		map.put("numStatus", sb.toString());
		pagination.setParams(map);
		return pagination;
	}

	/**
	 * 根据查询结果封装order对象
	 * 
	 * @param dto
	 * @return
	 */
	private T buildOrder(EccoreSearchResultDTO dto) {
		T order = (T) new Order();
		order.setId(dto.getId());
		order.setMailNo(dto.getMailNo());
		order.setTxLogisticId(dto.getTxLogisticId());
		order.setCustomerId(dto.getCustomerId());
		order.setOrderType(Integer.valueOf(dto.getOrderType()));
		// order.setStatus(dto.getStatus());
		order.setLineType(dto.getLineType().toString());
		if (dto.getWeight() == null)
			order.setWeight((float) 0);
		else
			order.setWeight(dto.getWeight());
		order.setFreightType(dto.getFreightType().toString()); // 特殊运单标识(0:特殊账单)
		order.setTrimFreight(Double
				.parseDouble(dto.getTrimFreight().toString()));
		order.setCreateTime(dto.getCreateTime());
		order.setAcceptTime(dto.getAcceptTime());

		order.setBuyName(dto.getName()); // 买家姓名
		order.setBuyPhone(dto.getPhone()); // 买家手机或固定电话
		order.setBprovCode(dto.getNumProv().toString()); // 买家 省份编码
		order.setToAddr(StringUtils.defaultString(dto.getProv()) // 目的地/收件地/买家地址
				+ StringUtils.defaultString(dto.getCity())
				+ StringUtils.defaultString(dto.getDistrict())
				+ StringUtils.defaultString(dto.getAddress()));

		order.setSprovCode(dto.getNumProvF().toString()); // 卖家省份编码
		order.setScity(dto.getCityF()); // 卖家城市显示值 (如果省份为广东，会用到这个值判断珠三角地区)

		String statusNum = dto.getStatus();
		String status = null;
		if ("0".equals(statusNum)) {
			status = "接单中";

		} else {
			// 根据状态码去枚举类取状态编码. 根据状态编码去map里取状态显示值
			status = orderStatusMap.get(statusNum);
		}
		order.setStatus(StringUtils.defaultIfEmpty(status, ORDER_STATUS_DEF)); // 订单状态

		// 设置店铺名称
		if (StringUtils.isNotEmpty(order.getCustomerId())) {
			User u = userService.getUserByCustomerId(order.getCustomerId());
			order.setShopName(u.getShopName());
		}

		return order;
	}

	/**
	 * 计算运费信息
	 * 
	 * @param order
	 * @return
	 */
	private T calcOrder(T order, String srcProvCode, Map map) {
		// 平台用户
		if ("4".equals(ObjectUtils.toString(map.get("usertype")))) {
			// 因为平台用户对账的时候, 参数map是<customerId, postinfoMap>格式
			map = MapUtils.getMap(map, order.getCustomerId()); // 此时map已经不是参数map
			if (map == null) {
				map = Collections.EMPTY_MAP;
				logger.error("order.getCustomerId()对应的用户没有运费模板, 该订单将纳入异常订单."
						+ "orderId:" + order.getId() + ";customerId:"
						+ order.getCustomerId());
			} else {
				srcProvCode = MapUtils.getString(map, "srcProvCode"); // 运费模板发件地
				order.setWprovCode(srcProvCode); // 设置网点省份编码
				order.setWprovText(Resource.getNameById(Integer
						.valueOf(srcProvCode))); // 平台用户显示这列
			}
		}
		float firstWeightPirce = 0; // 首重
		float overWeightPirce = 0; // 超重
		float weight = 0; // 重量
		double ow = Double.parseDouble(MapUtils.getString(map, "ow", "1")); // 计算单位
		Map<String, Postinfo> postinfoMap = (Map<String, Postinfo>) map
				.get("postinfo");
		// 特殊账单
		if ("0".equals(order.getFreightType())) {
			if (order.getMailNo() != null) {
				Map mufmap = new HashMap();
				mufmap.put("onemailNo", order.getMailNo());
				List<UnlikeFreight> muflist = unlikeFreightDao
						.getUnlikeFreightList(mufmap);
				if (muflist.size() > 0) {
					order.setToAddr(muflist.get(0).getAddress());
					order.setRemark(muflist.get(0).getReason());
					order.setBuyName(muflist.get(0).getUserName());
					order.setBuyMobile(muflist.get(0).getMobile());
					order.setUpdateTime(muflist.get(0).getCreateTime());
				}
			}
			// 特殊账单#实收运费
			order.setNetFreight((float) Math.round(order.getTrimFreight() * 100) / 100);
		} else {
			Postinfo postinfo = null; // 运费信息
			String bprovCode = order.getBprovCode(); // 收件地/目的地/买家所在地 省份编码
			/*
			 * 广东发广东的件运费有珠三角之分(440000 是region表中广东的Id)
			 * 当发件地和收件地都是广东的时候就要判断收件地的'市'是不是属于珠三角范围 (珠三角地区见kernel.properties)
			 * 广东珠三角(449999)、广东其他地区(448888)
			 */
			if ("440000".equals(bprovCode) && "440000".equals(srcProvCode)) {
				// 如果目的地的市属于珠三角，就取珠三角的运费
				boolean isExist = Resource.orderProvinces.containsKey(order
						.getScity()); // 市 显示值
				postinfo = isExist ? postinfoMap.get("449999") : postinfoMap
						.get("448888");
			} else {
				postinfo = postinfoMap == null ? null : postinfoMap
						.get(bprovCode); // 根据目的地省份编码取运费模板中的运费信息
			}

			if (postinfo == null) {
				order.setRemark("errorPostinfo"); // 标识符：目的地没有运费信息(页面使用这个字段.对DB没影响)
				logger.error("订单中的目的地不在运费模版中。发件地省份编码：" + order.getSprovCode()
						+ ";目的地省份编码:" + bprovCode);
				return order;
			}

			// 订单寄件地和运费模板的发件地是否一致判断
			if (!srcProvCode.equals(order.getSprovCode())) {
				order.setRemark("errorProv"); // 标识符：订单发件地和运费模板发件地省份不一致
				logger.info("订单寄件地和运费模板的始发地不一致。订单Id:" + order.getId()
						+ ";订单发件地省份编码：" + order.getSprovCode() + ";模板发件地省份编码:"
						+ srcProvCode);
				return order;
			}

			// 运费信息
			firstWeightPirce = postinfo.getFwRealPirce(); // 首重实收价
			overWeightPirce = postinfo.getOwRealPirce(); // 超重实收价
			order.setFirstWeightPirce(firstWeightPirce);
			order.setOverWeightPirce(overWeightPirce);
			weight = order.getWeight();
			weight = weight <= 1 ? 1 : weight; // @2011-12-01/ChenRen 重量为0，按1计算

			// 计算运费
			float nf;
			if (ow > 0.01) {
				nf = (float) (weight > 1 ? (firstWeightPirce + Math
						.ceil((weight - 1) / ow) * ow * overWeightPirce)
						: weight * firstWeightPirce);
			} else {
				nf = (float) (weight > 1 ? (firstWeightPirce + (weight - 1)
						* overWeightPirce) : weight * firstWeightPirce);
			}
			order.setNetFreight((float) Math.round(nf * 100) / 100); // 实际运费,
																		// 四舍五入

		} // if 非特殊订单

		// 退货订单不计算费用
		if (order.getOrderType() != null && order.getOrderType() == 3) {
			order.setRemark("thdd");
			// thOrderList.add(t);
			// thtotalFreight +=x;
		}

		return order;
	}

	@Override
	public boolean orderAlterStatusNodify(UpdateWaybillInfo updateInfo,
			SendTaskToTB sendTaskToTB) {
		Map map = new HashMap();
		map.put("txLogisticId", updateInfo.getTxLogisticId());
		map.put("mailNo", updateInfo.getMailNo());
		map.put("weight", updateInfo.getWeight());

		Boolean flag = dao.updateMailNoAndWeightByLogisticId(map);
		if (flag == true) {
			sendTaskToTBDao.addSendTaskToTB(sendTaskToTB);
		}
		return true;
	}

	/**
	 * 平台用户读取分仓用户的运费模板，并将每个分仓的模板以键值对的形式存放在Map集合里返回。<br>
	 * map中键值对格式为<平台用户的customerId,平台用户的模板>
	 * 
	 * @param map
	 * @return
	 * @date 2012-04-12 电子对账功能改造
	 * @author wangyong
	 */
	private Map buildPosttempInfo(Map map) {
		Map tempMap = new HashMap();
		if ("4".equals(ObjectUtils.toString(map.get("usertype")))) {
			List<User> fcuserList = (List<User>) map.get("fcuserList");
			for (User fcUser : fcuserList) {
				tempMap.put("userId", fcUser.getId());
				List<PosttempUser> puList = posttempUserDao
						.getPosttempUserByVipId(tempMap); // map里必须有 userId
				if (puList.size() < 1) {
					// 取系统模板
					Posttemp pt = posttempService.getSysPosttempByVipId(fcUser
							.getId());
					if (pt == null) {
						logger.error("用户的运费模板为空. 用户没有填写基本信息或网点不存在. userId:"
								+ fcUser.getId());
						continue;
					}
					if (fcUser.getTaobaoEncodeKey() != null
							&& !fcUser.getTaobaoEncodeKey().equals(""))
						map.put(fcUser.getTaobaoEncodeKey(), pt);
					else {
						logger.error("用户的customerId为空. userId:"
								+ fcUser.getId());
						continue;
					}
				} else {
					// 解析 分仓账号 自己的模板
					if (fcUser.getTaobaoEncodeKey() != null
							&& !fcUser.getTaobaoEncodeKey().equals("")) {
						Posttemp posttemp = posttempDao.getPosttempById(puList
								.get(0).getPostId()); // 运费模板
						if (posttemp == null) {
							logger.info("运费模板为空.模板id:"
									+ puList.get(0).getPostId());
							return Collections.EMPTY_MAP;
						} else
							map.put(fcUser.getTaobaoEncodeKey(), posttemp);
					} else {
						logger.error("用户的customerId为空. userId:"
								+ fcUser.getId());
						continue;
					}
				}
			}
		}
		return map;
	}

	/**
	 * 构建模板map键值对。map中包含Constants.POSTTEMP和srcProvCode，分别表示运费模板和运费模板始发地省份编码
	 * 
	 * @param posttempId
	 * @param sysPt
	 * @return
	 * @date 2012-04-12 电子对账功能改造
	 * @author wangyong
	 */
	private Map buildPosttempInfo(Integer posttempId, Posttemp sysPt) {
		Map map = new HashMap();
		List<Postinfo> pList = null;
		if (posttempId == 0 && sysPt != null) { // 系统模板
			map.put(Constants.POSTTEMP, sysPt); // 存放系统模板
		} else {
			Posttemp posttemp = posttempDao.getPosttempById(posttempId); // 运费模板
			if (posttemp == null) {
				logger.info("运费模板为空.模板id:" + posttempId);
				return Collections.EMPTY_MAP;
			}
			map.put(Constants.POSTTEMP, posttemp); // 存放运费模板
		}
		Resource.buildPostinfo((Posttemp) map.get(Constants.POSTTEMP), map);
		return map;
	}

	/**
	 * 计算运费：计费方式有四种，1、固定价格 2、简单重量规则 3、续重价格规则 4、续重统计单位<br>
	 * 调用EcAccountHelper.java中ecaccount方法，传入订单对象Order和运费模板对象Posttemp参数
	 * 
	 * @param order
	 * @param srcProvCode
	 * @param map
	 * @return
	 * @date 2012-04-12 电子对账功能改造
	 * @author wangyong
	 */
	private T calculateOrder(T order, String srcProvCode, Map map) {
		Posttemp posttemp = null;
		// 平台用户
		if ("4".equals(ObjectUtils.toString(map.get("usertype")))) {
			// 因为平台用户对账的时候, 参数map是<customerId, Posttemp>格式
			posttemp = (Posttemp) map.get(order.getCustomerId()); // 此时返回Posttemp对象
			if (posttemp == null) {
				logger.error("order.getCustomerId()对应的用户没有运费模板, 该订单将纳入异常订单."
						+ "orderId:" + order.getId() + ";customerId:"
						+ order.getCustomerId());
			} else {
				Resource.buildPostinfo(posttemp, map);
				srcProvCode = MapUtils.getString(map, Constants.SRCPROVCODE); // 运费模板发件地
				order.setWprovCode(srcProvCode); // 设置网点省份编码
				order.setWprovText(Resource.getNameById(Integer
						.valueOf(srcProvCode))); // 平台用户显示这列
			}
		} else {
			posttemp = (Posttemp) map.get(Constants.POSTTEMP);
		}
		Map<String, Postinfo> postinfoMap = (Map<String, Postinfo>) map
				.get(Constants.POSTINFO);
		// 特殊账单
		if ("0".equals(order.getFreightType())) {
			if (order.getMailNo() != null) {
				Map mufmap = new HashMap();
				mufmap.put("onemailNo", order.getMailNo());
				List<UnlikeFreight> muflist = unlikeFreightDao
						.getUnlikeFreightList(mufmap);
				if (muflist.size() > 0) {
					order.setToAddr(muflist.get(0).getAddress());
					order.setRemark(muflist.get(0).getReason());
					order.setBuyName(muflist.get(0).getUserName());
					order.setBuyMobile(muflist.get(0).getMobile());
					order.setUpdateTime(muflist.get(0).getCreateTime());
				}
			}
			// 特殊账单#实收运费
			order.setNetFreight((float) Math.round(order.getTrimFreight() * 100) / 100);
		} else {
			Postinfo postinfo = null; // 运费信息
			String bprovCode = order.getBprovCode(); // 收件地/目的地/买家所在地 省份编码
			/*
			 * 广东发广东的件运费有珠三角之分(440000 是region表中广东的Id)
			 * 当发件地和收件地都是广东的时候就要判断收件地的'市'是不是属于珠三角范围 (珠三角地区见kernel.properties)
			 * 广东珠三角(449999)、广东其他地区(448888)
			 */
			if ("440000".equals(bprovCode) && "440000".equals(srcProvCode)) {
				// 如果目的地的市属于珠三角，就取珠三角的运费
				boolean isExist = Resource.orderProvinces.containsKey(order
						.getScity()); // 市 显示值
				postinfo = isExist ? postinfoMap.get("449999") : postinfoMap
						.get("448888");
			} else {
				postinfo = postinfoMap == null ? null : postinfoMap
						.get(bprovCode); // 根据目的地省份编码取运费模板中的运费信息
			}
			if (postinfo == null) {
				order.setRemark("errorPostinfo"); // 标识符：目的地没有运费信息(页面使用这个字段.对DB没影响)
				logger.error("订单中的目的地不在运费模版中。发件地省份编码：" + order.getSprovCode()
						+ ";目的地省份编码:" + bprovCode);
				return order;
			}
			// 订单寄件地和运费模板的发件地是否一致判断
			if (!srcProvCode.equals(order.getSprovCode())) {
				order.setRemark("errorProv"); // 标识符：订单发件地和运费模板发件地省份不一致
				logger.info("订单寄件地和运费模板的始发地不一致。订单Id:" + order.getId()
						+ ";订单发件地省份编码：" + order.getSprovCode() + ";模板发件地省份编码:"
						+ srcProvCode);
				return order;
			}
			// 计算运费
			try {
				order.setFirstWeightPirce(postinfo.getFwRealPirce());
				order.setOverWeightPirce(postinfo.getOwRealPirce());
				order.setNetFreight((float) Math.round(EcAccountHelper
						.ecaccount(posttemp, order) * 100) / 100); // 实际运费, 四舍五入
				/**
				 * 以下插入首重重量、固定价格、重量单价、最低收费价格
				 */
				order.setFirstWeight(postinfo.getFirstWeight());
				order.setFixedPirce(postinfo.getFixedPirce());
				order.setWeightPirce(postinfo.getWeightPirce());
				order.setFloorPirce(postinfo.getFloorPirce());

			} catch (Exception e) {
				logger.error("计算运费出错：出错模板id:" + posttemp.getId());
				order.setRemark("errorCalcute");// 标识符：计算运费出现异常
			}

		}
		// 退货订单不计算费用
		if (order.getOrderType() != null && order.getOrderType() == 3) {
			order.setRemark("thdd");
		}
		return order;
	}

	@Override
	public List<OrderWeightUpdateDTO> getOrderByPartitiondate(
			String partitiondate, Integer limit) {
		return dao.getOrderByPartitiondate(partitiondate, limit);
	}

	@Override
	public List<OrderWeightUpdateDTO> getOrderByPartitiondateById(
			String partitiondate, int id, Integer limit) {
		logger.error("start query order ,id:" + id);
		return dao.getOrderByPartitiondateById(partitiondate, id, limit);
	}

	@Override
	public boolean bindedOrUpdateMailNo(Integer orderId, String mailNo,
			String txLogiscticId, String clientId) {
		Map updMap = new HashMap();
		updMap.put("id", orderId);
		updMap.put("mailNo", mailNo);
		updMap.put("txLogiscticId", txLogiscticId);
		updMap.put("clientId", clientId);

		boolean result = false;
		try {
			result = dao.updateMailNoById(updMap);

			// 面单更新的sendTask
			SendTask sendTask = OrderOperateHelper.createTask(txLogiscticId,
					clientId, mailNo, "UPDATE");
			result = this.sendTaskService.addSendTask(sendTask);
		} catch (Exception e) {
			logger.error("面单更新错误", e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean cancelOrder(Integer orderId, String txLogiscticId,
			String clientId) {
		Map cancelMap = new HashMap();
		cancelMap.put("id", orderId);
		cancelMap.put("txLogiscticId", txLogiscticId);
		cancelMap.put("clientId", clientId);
		boolean result = false;
		try {
			result = dao.cancelOrder(cancelMap);

			// 取消订单的sendTask
			SendTask sendTask = OrderOperateHelper.createTask(txLogiscticId,
					clientId, null, "WITHDRAW");
			this.sendTaskService.addSendTask(sendTask);
			result = true;
		} catch (Exception e) {
			logger.info("撤销订单发生错误", e);
		}

		return result;
	}

	@Override
	public void updateOrderWeight(
			List<OrderWeightUpdateDTO> orderWeightUpdateDTOs) {
		if (orderWeightUpdateDTOs == null || orderWeightUpdateDTOs.isEmpty()) {
			return;
		}
		Map<String, Float> mailnoWeightMap = new HashMap<String, Float>();
		OrderWeightUpdateDTO orderWeightUpdateDTO = new OrderWeightUpdateDTO();
		StringBuffer mailBuffer = new StringBuffer();
		for (int i = 0; i < orderWeightUpdateDTOs.size(); i++) {
			orderWeightUpdateDTO = orderWeightUpdateDTOs.get(i);
			mailnoWeightMap.put(orderWeightUpdateDTO.getMailNo(),
					orderWeightUpdateDTO.getWeight());
			if (i == (orderWeightUpdateDTOs.size() - 1)) {
				mailBuffer.append("'" + orderWeightUpdateDTO.getMailNo() + "'");
			} else {
				mailBuffer
						.append("'" + orderWeightUpdateDTO.getMailNo() + "',");
			}
		}

		OrderWeightUpdateHelper.getWeightFromJingang(mailBuffer.toString(),
				mailnoWeightMap);
		String mailNo = "";
		for (int i = 0; i < orderWeightUpdateDTOs.size(); i++) {
			OrderWeightUpdateDTO orderWeightDTO = new OrderWeightUpdateDTO();
			orderWeightDTO = orderWeightUpdateDTOs.get(i);
			mailNo = orderWeightDTO.getMailNo();
			if (mailnoWeightMap.containsKey(mailNo)) {
				orderWeightDTO.setWeight(mailnoWeightMap.get(mailNo));
			}
		}
		dao.batchUpdateWeight(orderWeightUpdateDTOs);
		Resource.orderParam.put("pageId",
				orderWeightUpdateDTOs.get(orderWeightUpdateDTOs.size() - 1)
						.getId());

		logger.error("batch update:" + orderWeightUpdateDTOs.size()
				+ "last order id:" + Resource.orderParam.get("pageId"));

	}

	@Override
	public List<Order> getOrderEccoreSearchByOrderIds(List<Integer> orderId,
			Pagination pagination, String sortType) {
		if (orderId != null && orderId.size() > 0) {
			StringBuffer orderIds = new StringBuffer();
			for (int i = 0; i < orderId.size(); i++) {
				if (i != 0)
					orderIds.append(",");
				orderIds.append(orderId.get(i));
			}
			Map map = new HashMap();
			map.put("orderIds", orderIds.toString());
			if (sortType == null)
				map.put("sortType", "desc");
			else
				map.put("sortType", sortType);
			pagination.setParams(map);
			try {
				eccoreSearchService.getOrderEccoreSearchByOrderIds(
						ConfigUtilSingle.getInstance().getSolrEccoreUrl(),
						pagination);
				List<EccoreSearchResultDTO> listDTO = pagination.getRecords();
				List<Order> result = new ArrayList<Order>();
				for (EccoreSearchResultDTO dto : listDTO) {
					T order = this.buildOrder(dto);
					result.add(order);
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("查询solr中异常运单出错");
				return null;
			}
		} else
			return null;
	}

	@Override
	public boolean updateMailNoValue(T entiry) {
		try {
			dao.updateMailNoValue(entiry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改订单表出错");
			return false;
		}
	}

	@Override
	public List<SearchReportBean> getSignRatioByPamams(int userType,
			String theId, String startDate, String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userType", userType);
		params.put("theId", theId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return dao.getSignRatioByPamams(params);
	}

	@Override
	public List<SearchReportBean> getSellSignRatioForDayByPamams(String theId,
			String startDate, int afterDay) {
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("theId", theId);
		params.put("startDate", startDate);
		// 获取同一发货时间的总发货数量
		int countAll = dao.getSellSignRatioForDayByPamams(params);
		// 获取同一发货时间下,每天的成功签收数量
		params.put("status", 1); // 成功标识
		String endDate;
		SearchReportBean report;
		int indexCount = 0;
		int count;
		List<SearchReportBean> list = new ArrayList<SearchReportBean>();
		for (int i = 0; i < afterDay; i++) {
			endDate = getAfterDay(startDate, i);
			params.put("endDate", endDate);
			count = dao.getSellSignRatioForDayByPamams(params);
			indexCount += count;
			report = new SearchReportBean();
			report.setSendStartTime(getFormatMonthDay(endDate));
			report.setNumber(countAll);
			report.setAccept(indexCount);
			list.add(report);
		}
		return list;
	}

	@Override
	public List<SearchReportBean> getPlatformSignRatioForDayByPamams(
			String theId, String startDate, int afterDay) {
		// 查询条件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("theId", theId);
		params.put("startDate", startDate);
		// 获取同一发货时间的总发货数量
		int countAll = dao.getSellSignRatioForDayByPamams(params);
		// 获取同一发货时间下,每天的成功签收数量
		params.put("status", 1); // 成功标识
		String endDate;
		SearchReportBean report;
		int indexCount = 0;
		int count;
		List<SearchReportBean> list = new ArrayList<SearchReportBean>();
		for (int i = 0; i < afterDay; i++) {
			endDate = getAfterDay(startDate, i);
			params.put("endDate", endDate);
			count = dao.getSellSignRatioForDayByPamams(params);
			indexCount += count;
			report = new SearchReportBean();
			report.setSendStartTime(getFormatMonthDay(endDate));
			report.setNumber(countAll);
			report.setAccept(indexCount);
			list.add(report);
		}
		return list;
	}

	@Override
	public List<SearchReportBean> getSellHourRatioByPamams(String theId,
			String startDate, String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("theId", theId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		List<SearchReportBean> reports = dao.getSellHourRatioByPamams(params);
		if (reports == null) {
			reports = new ArrayList<SearchReportBean>();
		}
		return reports;
	}

	@Override
	public List<SearchReportBean> getPlatformHourRatioByPamams(String theId,
			String startDate, String endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("theId", theId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		List<SearchReportBean> reports = dao
				.getPlatformHourRatioByPamams(params);
		if (reports == null) {
			reports = new ArrayList<SearchReportBean>();
		}
		return reports;
	}

	private String getAfterDay(String date, int after) {
		String theDate = date;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate;
		Date eDate;
		try {
			sDate = format.parse(date);
			eDate = DateUtil.getDateAfter(sDate, after);
			theDate = format.format(eDate);
		} catch (ParseException e) {
			logger.error("IN SERVICE:OrderServiceImpl METHOD:getAfterDay() parse date:"
					+ date + "ERR");
		}
		return theDate;
	}

	private String getFormatMonthDay(String date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String theDate = date;
		try {
			theDate = DateUtil.toMonth(format.parse(date)) + "月";
			theDate += DateUtil.toShortdd(format.parse(date)) + "日";
		} catch (ParseException e) {
			logger.error("IN SERVICE:OrderServiceImpl METHOD:getFormatDate() parse date:"
					+ date + "ERR");
		}
		return theDate;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionMap = session;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	@Override
	public List<String> queryTenOrdersByCustomerId(Map<String, Object> params) {
		// TODO Auto-generated method stub
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		if (!params.containsKey("customerId")) {
			params.put("customerId", null);
		}
		if (!params.containsKey("limit") || params.get("limit") == null
				|| Integer.parseInt(params.get("limit").toString()) < 0) {
			params.put("limit", 10);
		}
		return dao.queryTenOrdersByCustomerId(params);
	}

	public boolean sendMailNoToEC(String txLogisticId, String mailNo,
			String clientId, String type) {

		// clientId为空，则不知道此订单推送给哪个厂商
		if (StringUtils.isEmpty(clientId)) {
			return false;
		}
		String url = Resource.getChannel(clientId);// 推送给客户的URL地址
		String secretID = Resource.getSecretId(clientId); // 获取客户的parternId

		logger.debug("clientId:" + clientId);
		logger.debug("type:" + type);
		logger.debug("url:" + url);
		logger.debug("secretID:" + secretID);

		String xmlString = null;
		xmlString = XmlBuildProcessor.getEcUpdateXML(clientId, mailNo,
				txLogisticId);
		logger.debug(xmlString.toString());
		String dslogisticsInterface; // 明文信息
		String dsdataDigest; // 密文
		try {
			dslogisticsInterface = java.net.URLEncoder.encode(
					xmlString.toString(), XmlSender.UTF8_CHARSET);
			dsdataDigest = java.net.URLEncoder.encode(
					Md5Encryption.MD5Encode(xmlString.toString() + secretID),
					XmlSender.UTF8_CHARSET);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.error("xml加密并转码失败！");
			return false;
		}

		// 将SendTask存到数据库表中.
		SendTaskToTB sendTask = new SendTaskToTB();
		// 保存为淘宝等其它电商平台的访问地址,需要根据clientID查询到对应的Url访问地址.
		sendTask.setClientId(clientId);
		sendTask.setRequestURL(url);// url预读是为了防止服务启动时缓存未加载导致url取不到为空
		sendTask.setOrderId(0);

		sendTask.setRemark("推送面单号");
		sendTask.setTxLogisticId(txLogisticId);

		String s = txLogisticId;
		s = s.substring(s.length() - 1);
		sendTask.setTaskFlagId(Integer.parseInt(s));
		sendTask.setTaskFlag(s);

		sendTask.setRequestParams("logistics_interface=" + dslogisticsInterface
				+ "&" + "data_digest=" + dsdataDigest + "&type=" + type);
		boolean flag = false;
		try {
			sendTaskToTBDao.addSendTaskToTB(sendTask);
			flag = true;
		} catch (Exception e) {
			logger.error("插入任务表失败，物流号：" + txLogisticId);
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;

	}

	@Inject
	private DbExeMapper mapper;

	/**
	 * 数据导入
	 * 
	 * @return
	 */
	public void insertImportDb() {
		int allCount = mapper.getOrderTempAllCount();
		System.out.println("ORDER表示总数据：" + allCount);
		System.out.println(" 数据导入执行开始...");

		// 一次查询的数量
		int pageNum = 1000;
		int n = allCount / pageNum + 1; // 总数/一页数 +1
		Map params = new HashMap();

		// 每次按 pageNum 数量查询处理
		int startIndex = 0;
		List<Order> orderTempAll;
		for (int i = 0; i <= n; i++) {
			System.out.println("数据执行开始数...:" + startIndex);
			params.put("startIndex", startIndex);
			params.put("pageNum", pageNum);
			orderTempAll = mapper.getOrderTempAll(params);
			Integer oldOrderId = null;
			Integer newOrderId = null;
			if (orderTempAll != null) {
				List<Product> productTempByOrderId = null;
				List<TraderInfo> traderInfoTempByOrderId = null;
				int x = 0;
				for (Order order : orderTempAll) {
					// System.out.println("ORDER数据...:"+x);
					x++;
					oldOrderId = order.getId();
					order.setId(null);
					// paserDateToString(order.getPartitionDate(),"yyyy-MM-dd")
					mapper.addOrderFromTemp(order);
					newOrderId = order.getId();
					productTempByOrderId = mapper
							.getProductTempByOrderId(oldOrderId);
					for (Product product : productTempByOrderId) {
						product.setId(null);
						product.setOrderId(newOrderId);
						// product.setPartitionDate(paserStringToDate("2012-04-18","yyyy-MM-dd"));
						mapper.addProductFromTemp(product);
					}
					traderInfoTempByOrderId = mapper
							.getTraderInfoTempByOrderId(oldOrderId);
					for (TraderInfo traderInfo : traderInfoTempByOrderId) {
						traderInfo.setOrderId(newOrderId);
						// traderInfo.setPartitionDate(paserStringToDate("2012-04-18","yyyy-MM-dd"));
						mapper.addTraderInfoFromTemp(traderInfo);
					}
				}
				System.out.println("ORDER数据...:" + (startIndex + x));
			}
			startIndex = startIndex + pageNum;
		}
		System.out.println(" 数据导入执行结束...");
	}

	/**
	 * 解析日期型字符串到字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	private String paserDateToString(Date date, String format) {
		SimpleDateFormat mat = new SimpleDateFormat(format);
		String formatDate = "";
		formatDate = mat.format(date);
		return formatDate;
	}

	/**
	 * 解析日期型字符串到字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	private Date paserStringToDate(String date, String format) {
		SimpleDateFormat mat = new SimpleDateFormat(format);
		Date formatDate = null;
		try {
			formatDate = mat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatDate;
	}

	@Override
	public void updateOrderMailNoByLogisticIdAndClientIdBatch(
			List<UpdateInfo> list) {
		// TODO Auto-generated method stub
		dao.updaetBatchOrderByTxlogisc(list);
	}

	@Override
	public boolean updOrderMailNoByLogisticIdAndClientId(
			List<UpdateWaybillInfo> updateInfoList, String jinGangOrder,
			String clientID)throws Exception {
		StringBuffer toXmlFragment = new StringBuffer();

		// 传给金刚的报文数据
		List<UpdateWaybillInfo> params = new ArrayList<UpdateWaybillInfo>();

		// 返回报文的头片段设置
		toXmlFragment
				.append(new Response().DATA_HEAD_RESPONSE.toXmlFragment5());
		
		for (UpdateWaybillInfo bean : updateInfoList) {
			
			// 表无的更新字段
			if (bean.getFieldName().equals("suspect")) {

				// 疑似异常订单
				params.add(bean);
				bean.getResponse().setTxLogisticId(bean.getTxLogisticId());
				bean.getResponse().setFieldName(bean.getFieldName());
				bean.getResponse().setLogisticProviderId(
						bean.getLogisticProviderId());
				bean.getResponse().setSuccess("true");
				toXmlFragment.append(bean.getResponse().toXmlFragment3());
			} else {
				
				// 更新成功场合
				if (!this.orderStatusNodifyByPartition(bean)) {
					
					logger.error("没有更新到记录");
					// 更新失败直接返回
					return false;
				}
				
				bean.getResponse().setTxLogisticId(bean.getTxLogisticId());
				bean.getResponse().setFieldName(bean.getFieldName());
				bean.getResponse().setLogisticProviderId(
						bean.getLogisticProviderId());
				bean.getResponse().setSuccess("true");
				toXmlFragment.append(bean.getResponse().toXmlFragment3());
				params.add(bean);
			}
		}
		toXmlFragment.append("</responseItems>");
		toXmlFragment.append("</responses>");

		// --> 加入SendTask.
		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId(clientID);

		// 备注信息设置为 返回给淘宝的报文
		sendTask.setRemark(toXmlFragment.toString());
		sendTask.setTaskFlagId(getflagid(updateInfoList.get(0)
				.getTxLogisticId()));
		sendTask.setTaskFlag(getflag(updateInfoList.get(0).getTxLogisticId()));

		// 取更新的第一个物流号
		sendTask.setTxLogisticId(updateInfoList.get(0).getTxLogisticId());
		sendTask.setRequestURL(Resource.getChannel(jinGangOrder));

		// 金刚（logisticsInterface）参数设置
		StringBuffer jglogisticsInterfaceTemp = new StringBuffer();
		Response response1 = new Response();
		response1.setLogisticProviderId(updateInfoList.get(0)
				.getLogisticProviderId());
		response1.setEcCompanyId(updateInfoList.get(0).getClientId());
		jglogisticsInterfaceTemp.append(response1.toXmlFragment4());
		if (params != null && params.size() > 0) {
			for (UpdateWaybillInfo entry : params) {
				response1.setTxLogisticId(entry.getTxLogisticId());
				response1.setFieldName(entry.getFieldName());
				response1.setFieldValue(entry.getFieldValue());
				response1.setRemark(entry.getRemark());
				jglogisticsInterfaceTemp.append(response1.toXmlFragment6());
			}
		}
		jglogisticsInterfaceTemp.append("</fieldList>");
		jglogisticsInterfaceTemp.append("</UpdateInfo>");

		// 金刚参数编码
		String jglogisticsInterface = encode(
				jglogisticsInterfaceTemp.toString(), XmlSender.UTF8_CHARSET);
		String jgdataDigest = encode(
				Md5Encryption.MD5Encode(jglogisticsInterfaceTemp.toString()
						+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO()),
				XmlSender.UTF8_CHARSET);
		
		// 金刚报文设置
		sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
				+ jglogisticsInterface + "&" + DATA_DIGEST_PARAM + "="
				+ jgdataDigest + "&" + MSG_TYPE + "=" + ORDER_UPDATE);
		
		// 添加到sendtask表
		if (!sendTaskService.addSendTask(sendTask)) {

			// sendtask表更新失败直接返回
			return false;
		}
		
		return true;
	}

	/**
	 * 面单打印
	 * 
	 * @param order
	 * @param orderFormInfo
	 * @param receiver
	 * @param sender
	 * @author liucy
	 */
	public boolean addOrderPrintMethod(Order order,
			OrderFormInfo orderFormInfo, TraderInfo receiver,
			TraderInfo sender, List<Product> products) {

		// 线上下单,放入 待打印面单表
		OrderPrint orderPrint = new OrderPrint();
		orderPrint.setId(order.getId());
		orderPrint.setLogisticProviderId(orderFormInfo.getLogisticProviderId());
		orderPrint.setTxLogisticId(orderFormInfo.getTxLogisticId());
		orderPrint.setTradeNo(orderFormInfo.getTradeNo());
		orderPrint.setCustomerId(orderFormInfo.getCustomerId());
		orderPrint.setMailNo(StringUtils.EMPTY);// 默认成空字符串
		orderPrint.setFlag(orderFormInfo.getFlag());
		orderPrint.setType("");
		// orderPrint.setCreateTime(new Date());

		if (orderFormInfo.getSendStartTime() != null
				&& !orderFormInfo.getSendStartTime().equals("null")
				&& orderFormInfo.getSendStartTime() != null
				&& !orderFormInfo.getSendStartTime().equals("")) {
			orderPrint.setSendStartTime(DateUtil.valueOfStandard(orderFormInfo
					.getSendStartTime()));
		}
		if (orderFormInfo.getSendEndTime() != null
				&& !orderFormInfo.getSendEndTime().equals("null")
				&& orderFormInfo.getSendEndTime() != null
				&& !orderFormInfo.getSendEndTime().equals("")) {
			orderPrint.setSendEndTime(DateUtil.valueOfStandard(orderFormInfo
					.getSendEndTime()));
		}
		// orderPrint.setInsuranceValue(null);
		// orderPrint.setPackageOrNot(null);
		if (orderFormInfo.getSpecial() != null
				&& !orderFormInfo.getSpecial().equals("")) {
			orderPrint.setSpecial(orderFormInfo.getSpecial().getCode());
		}
		orderPrint.setClientId(orderFormInfo.getClientId());
		orderPrint.setStatus(Order.CREATE_ORDER_TYPE);// 订单状态，创建/取消
		if (orderFormInfo.getItemsWeight() != null
				&& !orderFormInfo.getItemsWeight().equals("")) {
			orderPrint
					.setWeight(Double.valueOf(orderFormInfo.getItemsWeight()));
		}

		// orderPrint.setSignPrice(null);
		orderPrint.setVipId(orderFormInfo.getVipId());
		orderPrint.setLineType(orderFormInfo.getLineType());
		// orderPrint.setWeightUpdateFlag(null);

		if (orderFormInfo.getOrderType() != null
				&& !orderFormInfo.getOrderType().equals("")) {
			orderPrint.setOrderType(Integer.valueOf(orderFormInfo
					.getOrderType()));
		}
		if (orderFormInfo.getServiceType() != null
				&& !orderFormInfo.getServiceType().equals("")) {
			orderPrint.setServiceType(Integer.valueOf(orderFormInfo
					.getServiceType()));
		}
		if (orderFormInfo.getGoodsValue() != null
				&& !orderFormInfo.getGoodsValue().equals("")) {
			orderPrint.setGoodsValue(Double.parseDouble(orderFormInfo
					.getGoodsValue()));
		}
		if (orderFormInfo.getItemsValue() != null
				&& !orderFormInfo.getItemsValue().equals("")) {
			orderPrint.setItemsValue(Double.parseDouble(orderFormInfo
					.getItemsValue()));
		}
		if (orderFormInfo.getTotalServiceFee() != null
				&& !orderFormInfo.getTotalServiceFee().equals("")) {
			orderPrint.setTotalServiceFee(Double.valueOf(orderFormInfo
					.getTotalServiceFee()));
		}
		if (orderFormInfo.getCodSplitFee() != null
				&& !orderFormInfo.getCodSplitFee().equals("")) {
			orderPrint.setCodSplitFee(Double.valueOf(orderFormInfo
					.getCodSplitFee()));
		}
		if (orderFormInfo.getBuyServiceFee() != null
				&& !orderFormInfo.getBuyServiceFee().equals("")) {
			orderPrint.setBuyServiceFee(Double.valueOf(orderFormInfo
					.getBuyServiceFee()));
		}
		// 联系人 收件人
		orderPrint.setBuyName(receiver.getName());
		orderPrint.setBuyMobile(receiver.getMobile());
		orderPrint.setBuyTelphone(receiver.getPhone());
		orderPrint.setBuyPostcode(receiver.getPostCode());
		// 由于红孩子商城的address里面包含省市信息所以此处只将address放入buyfulladdress字段中
		if (StringUtils.equals("REDBABY", orderFormInfo.getClientId())) {
			orderPrint.setBuyFulladdress(receiver.getAddress());
		} else {
			String Prov = receiver.getProv() == null ? "" : receiver.getProv();
			String City = receiver.getCity() == null ? "" : receiver.getCity();
			String District = receiver.getDistrict() == null ? "" : receiver
					.getDistrict();
			String Address = receiver.getAddress() == null ? "" : receiver
					.getAddress();
			String BuyFulladdress = Prov + City + District + Address;
			orderPrint.setBuyFulladdress(BuyFulladdress);
			logger.info("Prov==>" + Prov);
			logger.info("City==>" + City);
			logger.info("District==>" + District);
			logger.info("Address==>" + Address);
			logger.info("BuyFulladdress==>" + BuyFulladdress);
		}
		orderPrint.setBuyProv(receiver.getProv());
		orderPrint.setBuyCity(receiver.getCity());
		orderPrint.setBuyDistrict(receiver.getDistrict());
		orderPrint.setBuyAddress(receiver.getAddress());

		// 发件人
		orderPrint.setSaleName(sender.getName());
		orderPrint.setSaleMobile(sender.getMobile());
		orderPrint.setSaleTelphone(sender.getPhone());
		orderPrint.setBuyPostcode(sender.getPostCode());

		String SaleProv = sender.getProv() == null ? "" : sender.getProv();
		String SaleCity = sender.getCity() == null ? "" : sender.getCity();
		String SaleDistrict = sender.getDistrict() == null ? "" : sender
				.getDistrict();
		String SaleAddress = sender.getAddress() == null ? "" : sender
				.getAddress();
		String SaleFulladdress = SaleProv + SaleCity + SaleDistrict
				+ SaleAddress;
		logger.info("SaleProv" + SaleProv);
		logger.info("SaleCity" + SaleCity);
		logger.info("SaleDistrict" + SaleDistrict);
		logger.info("SaleAddress" + SaleAddress);
		logger.info("SaleFulladdress" + SaleFulladdress);
		orderPrint.setSaleFulladdress(SaleFulladdress);
		orderPrint.setSaleProv(sender.getProv());
		orderPrint.setSaleCity(sender.getCity());
		orderPrint.setSaleDistrict(sender.getDistrict());
		orderPrint.setSaleAddress(sender.getAddress());

		orderPrint
				.setOldPrice(Double.parseDouble(orderFormInfo.getGoodsValue()));
		orderPrint.setParentId(0);

		orderPrint.setFirstProductName(products.get(0).getItemName());
		StringBuffer stringBuffer = new StringBuffer();
		for (Product product : products) {
			stringBuffer.append("<dd>" + product.getItemName() + ",,,"
					+ product.getItemNumber() + ",,," + product.getItemValue()
					+ "</dd>");
		}
		orderPrint.setProductName(stringBuffer.toString());

		orderPrint.setRemark(stringBuffer.toString());// 将产品信息记录在remark中

		orderPrint.setJoinNum(0);
		orderPrint.setIsCombine("N");
		orderPrint.setIsPrint("N");
		orderPrint.setIsPrintSend("N");
		orderPrint.setIsSend("N");
		boolean flag = orderPrintService.addOrderPrint(orderPrint);
		return flag;
	}

	/**
	 * 线下订单则向jgwaybill表中插入记录
	 * 
	 * @param order
	 * @param orderFormInfo
	 * @author liucy
	 */
	public boolean addJgWaybillMethod(Order order, OrderFormInfo orderFormInfo) {
		JgWaybill jgWaybill = new JgWaybill();
		logger.info("addJgWaybillMethod addJgWaybillMethod start...");
		// 快递保障
		if (StringUtils.equals(orderFormInfo.getServiceType(), "16")) {
			jgWaybill.setCustomerId(orderFormInfo.getCustomerId());
			logger.error("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getCustomerId()..."
					+ orderFormInfo.getCustomerId());
		}
		jgWaybill.setServiceType(orderFormInfo.getServiceType());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getServiceType()..."
				+ orderFormInfo.getServiceType());
		jgWaybill.setbAddress(orderFormInfo.getReceiver().getAddress());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getAddress()..."
				+ orderFormInfo.getReceiver().getAddress());
		jgWaybill.setbCity(orderFormInfo.getReceiver().getCity());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getCity()..."
				+ orderFormInfo.getReceiver().getCity());
		jgWaybill.setbDistrict(orderFormInfo.getReceiver().getDistrict());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getDistrict()..."
				+ orderFormInfo.getReceiver().getDistrict());
		jgWaybill.setbMobile(orderFormInfo.getReceiver().getMobile());
		logger.info("addJgWaybillMethod addJgWaybillMethod start orderFormInfo.getReceiver().getMobile()..."
				+ orderFormInfo.getReceiver().getMobile());
		jgWaybill.setbName(orderFormInfo.getReceiver().getName());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getName()..."
				+ orderFormInfo.getReceiver().getName());
		jgWaybill.setbPhone(orderFormInfo.getReceiver().getPhone());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getPhone()..."
				+ orderFormInfo.getReceiver().getPhone());
		jgWaybill.setbPostCode(orderFormInfo.getReceiver().getPostCode());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getPostCode()..."
				+ orderFormInfo.getReceiver().getPostCode());
		jgWaybill.setbProv(orderFormInfo.getReceiver().getProv());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getReceiver().getProv()..."
				+ orderFormInfo.getReceiver().getProv());
		jgWaybill.setMailNo(orderFormInfo.getMailNo());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getMailNo()..."
				+ orderFormInfo.getMailNo());
		jgWaybill.setOrderId(order.getId());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  order.getId()..."
				+ order.getId());
		jgWaybill.setsAddress(orderFormInfo.getSender().getAddress());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getAddress()..."
				+ orderFormInfo.getSender().getAddress());
		jgWaybill.setsCity(orderFormInfo.getSender().getCity());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getCity()..."
				+ orderFormInfo.getSender().getCity());
		jgWaybill.setsProv(orderFormInfo.getSender().getProv());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getProv()..."
				+ orderFormInfo.getSender().getProv());
		jgWaybill.setsPostCode(orderFormInfo.getSender().getPostCode());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getPostCode()..."
				+ orderFormInfo.getSender().getPostCode());
		jgWaybill.setsPhone(orderFormInfo.getSender().getPhone());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getPhone()..."
				+ orderFormInfo.getSender().getPhone());
		jgWaybill.setsName(orderFormInfo.getSender().getName());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getName()..."
				+ orderFormInfo.getSender().getName());
		jgWaybill.setsMobile(orderFormInfo.getSender().getMobile());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getMobile..."
				+ orderFormInfo.getSender().getMobile());
		jgWaybill.setsDistrict(orderFormInfo.getSender().getDistrict());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getDistrict()..."
				+ orderFormInfo.getSender().getDistrict());
		jgWaybill.setLogisticId(orderFormInfo.getTxLogisticId());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getTxLogisticId()..."
				+ orderFormInfo.getTxLogisticId());
		jgWaybill.setLineType(orderFormInfo.getLineType());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getLineType()..."
				+ orderFormInfo.getLineType());
		jgWaybill.setClientID(orderFormInfo.getClientId());
		logger.info("addJgWaybillMethod addJgWaybillMethod start  orderFormInfo.getSender().getClientId()..."
				+ orderFormInfo.getClientId());
		logger.error("addJgWaybillMethod"+jgWaybill.getLogisticId() + "mailNo:"+jgWaybill.getMailNo());
		boolean flag = this.jgWaybillDao.addJgWaybill(jgWaybill);
		logger.info("addJgWaybillMethod addJgWaybillMethod start  2...");
		return flag;
	}

	public void addSendTask() {

	}
	
	/**
	 * 增加订单
	 * 
	 * @param orderFormInfo
	 * @author liucy
	 */
	public boolean addOrderMethod(OrderFormInfo orderFormInfo, Order order,
			TraderInfo sender, TraderInfo receiver, List<Product> products) {
		logger.info("add order setp1");

		boolean flag = true;

		// copy order value.
		order.setClientId(orderFormInfo.getClientId());
		order.setCustomerId(orderFormInfo.getCustomerId());
		order.setFlag(orderFormInfo.getFlag());
		order.setLogisticProviderId(orderFormInfo.getLogisticProviderId());
		order.setMailNo(orderFormInfo.getMailNo());
		order.setRemark(orderFormInfo.getRemark());
		if ((orderFormInfo.getSendStartTime() != null)
				&& !orderFormInfo.getSendStartTime().equals("null")
				&& !orderFormInfo.getSendStartTime().equals("")) {
			order.setSendStartTime(DateUtil.valueof(
					orderFormInfo.getSendStartTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		if ((orderFormInfo.getSendEndTime() != null)
				&& !orderFormInfo.getSendEndTime().equals("null")
				&& !orderFormInfo.getSendEndTime().equals("")) {
			order.setSendEndTime(DateUtil.valueof(
					orderFormInfo.getSendEndTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		if (orderFormInfo.getSpecial() != null)
			order.setSpecial(orderFormInfo.getSpecial().getCode());

		// 设置订单的初始状态
		order.setStatus(Order.CREATE_ORDER_TYPE);
		order.setTradeNo(orderFormInfo.getTradeNo());
		order.setTxLogisticId(orderFormInfo.getTxLogisticId());
		order.setVersion(orderFormInfo.getVersion());
		order.setVipId(orderFormInfo.getVipId());
		order.setLineType(orderFormInfo.getLineType());
		if (orderFormInfo.getItemsWeight() != null
				&& !orderFormInfo.getItemsWeight().equals(""))
			order.setWeight(Float.valueOf(orderFormInfo.getItemsWeight()));
		// ADD--MGL 2011-09-22
		if (orderFormInfo.getOrderType() != null
				&& !orderFormInfo.getOrderType().equals(""))
			order.setOrderType(Integer.valueOf(orderFormInfo.getOrderType()));
		if (orderFormInfo.getServiceType() != null
				&& !orderFormInfo.getServiceType().equals(""))
			order.setServiceType(Long.valueOf(orderFormInfo.getServiceType()));
		if (orderFormInfo.getGoodsValue() != null
				&& !orderFormInfo.getGoodsValue().equals(""))
			order.setGoodsValue(Double.valueOf(orderFormInfo.getGoodsValue()));
		if (orderFormInfo.getItemsValue() != null
				&& !orderFormInfo.getItemsValue().equals(""))
			order.setItemsValue(Double.valueOf(orderFormInfo.getItemsValue()));
		if (orderFormInfo.getTotalServiceFee() != null
				&& !orderFormInfo.getTotalServiceFee().equals(""))
			order.setTotalServiceFee(Double.valueOf(orderFormInfo
					.getTotalServiceFee()));
		if (orderFormInfo.getBuyServiceFee() != null
				&& !orderFormInfo.getBuyServiceFee().equals(""))
			order.setBuyServiceFee(Double.valueOf(orderFormInfo
					.getBuyServiceFee()));
		if (orderFormInfo.getCodSplitFee() != null
				&& !orderFormInfo.getCodSplitFee().equals(""))
			order.setCodSplitFee(Double.valueOf(orderFormInfo.getCodSplitFee()));
		order.setPartitionDate(orderFormInfo.getPartitiondate());

		// 发件人类型设置
		sender.setTradeType(TraderInfo.SENDE_TYPE);
		sender.setPartitionDate(orderFormInfo.getPartitiondate());
		receiver.setPartitionDate(orderFormInfo.getPartitiondate());
		// 收件人类型设置
		receiver.setTradeType(TraderInfo.RECEIVE_TYPE);

		logger.info("add order setp2");
		if (!this.dao.addOrder((T) order)) {
			flag = false;
		}
		logger.info("add order setp3");

		sender.setOrderId(order.getId());

		// 插入发件人信息traderInfo表
		logger.info("add order setp4");
		if (!this.traderDao.addTraderInfo(sender)) {
			flag = false;
		}
		logger.info("add order setp5");

		// 所对应的订单编号
		receiver.setOrderId(order.getId());

		// 插入收件人信息traderInfo表
		logger.info("add order setp6");
		if (!this.traderDao.addTraderInfo(receiver)) {
			flag = false;
		}
		logger.info("add order setp7");

		for (Product product : products) {
			product.setLogisticId(order.getTxLogisticId());

			// 所对应的订单编号
			product.setOrderId(order.getId());

			// 插入产品表信息
			if (!this.productDao.addProduct(product)) {
				flag = false;
			}
		}
		logger.info("add order setp8");

		if (StringUtils.equals("HDYS", orderFormInfo.getClientId())) {
			int i = sendTaskMailNoDao.addMailNo(orderFormInfo.getMailNo(),
					orderFormInfo.getClientId());
			logger.error("记录运单号,MailNo:" + orderFormInfo.getMailNo() + "size:"
					+ i);
		}
		logger.info("add order setp9");
		return flag;
	}

	private static int getflagid(String arg) {
		String s = arg;
		// String s="klllll123456";
		s = s.substring(s.length() - 1);
		logger.debug(s);
		return Integer.parseInt(s);

	}

	private static String getflag(String arg) {
		String sid = arg;
		// String s="klllll123456";
		sid = sid.substring(sid.length() - 1);
		String s = sid;
		// 按照对应关系取配置文件
		/*
		 * if(sid){ s= }
		 */
		logger.debug(s);
		return s;
	}

	/**
	 * 淘宝接口返回失败文本设置
	 * 
	 * @param orderFormInfo
	 * @return
	 * @author liuchunyan
	 */
	public String getToFailedXmlFragment(OrderFormInfo orderFormInfo) {
		orderFormInfo.getResponse().setSuccess("false");
		orderFormInfo.getResponse().setReason("S07");
		String toXmlFragment = orderFormInfo.getResponse().toXmlFragment2();
		return toXmlFragment;
	}

	/**
	 * 使用指定的字符集charset对字符串arg进行编码,将编码后的字符串返回.
	 * 
	 * @param arg
	 *            待编码字符串.
	 * @param charset
	 * @return
	 */
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, Object> orderListStatusNodify(
			List<UpdateWaybillInfo> updateInfoList) {
		StringBuffer toXmlFragment = new StringBuffer();

		// 返回报文的头片段设置
		toXmlFragment
				.append(new Response().DATA_HEAD_RESPONSE.toXmlFragment5());
		// 记录更新成功的数据
		List<UpdateWaybillInfo> updateInfoListTemp = new ArrayList<UpdateWaybillInfo>();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 循环处理金刚接口报文对象
		for (UpdateWaybillInfo bean : updateInfoList) {

			if (bean.getFieldName().equals("exception")
					|| (bean.getFieldName().equals("status") &&
					// 接单成功
					!("ACCEPT".equals(bean.getFieldValue().toUpperCase())
							// 接单失败
							|| "UNACCEPT".equals(bean.getFieldValue()
									.toUpperCase())
							// 签收成功
							|| "SIGNED".equals(bean.getFieldValue()
									.toUpperCase())
							// 签收失败
							|| "FAILED".equals(bean.getFieldValue()
									.toUpperCase())
							// 揽收成功
							|| "GOT".equals(bean.getFieldValue().toUpperCase())
							// 揽收失败
							|| "NOT_SEND".equals(bean.getFieldValue()
									.toUpperCase())
					// 派件扫描成功
					|| "SENT_SCAN".equals(bean.getFieldValue().toUpperCase())))) {

				// 异常订单
				bean.getResponse().setTxLogisticId(bean.getTxLogisticId());
				bean.getResponse().setFieldName(bean.getFieldName());
				bean.getResponse().setLogisticProviderId(
						bean.getLogisticProviderId());
				bean.getResponse().setSuccess("true");

				// 返回 成功响应文本
				toXmlFragment.append(bean.getResponse().toXmlFragment3());

				// 添加到临时更新成功对象中
				updateInfoListTemp.add(bean);
			} else {
				
				  if (StringUtils.equals("GOT", bean.getInfoContent())) {
					  bean.setType(bean.getAcceptTime()); 
				  }
				 
				boolean flag = this.orderStatusNodify(bean);

				// 更新成功场合
				if (flag) {
					bean.getResponse().setLogisticProviderId(
							bean.getLogisticProviderId());
					bean.getResponse().setSuccess("true");
					bean.getResponse().setTxLogisticId(bean.getTxLogisticId());
					bean.getResponse().setTxLogisticId(bean.getTxLogisticId());
					bean.getResponse().setFieldName(bean.getFieldName());
					bean.getResponse().setLogisticProviderId(
							bean.getLogisticProviderId());
					bean.getResponse().setSuccess("true");

					// 返回 成功响应文本
					toXmlFragment.append(bean.getResponse().toXmlFragment3());

					// 添加到临时更新成功对象中
					updateInfoListTemp.add(bean);
				}

				// 更新失败场合
				else {
					bean.getResponse().setTxLogisticId(bean.getTxLogisticId());
					bean.getResponse().setFieldName(bean.getFieldName());
					bean.getResponse().setLogisticProviderId(
							bean.getLogisticProviderId());
					bean.getResponse().setSuccess("false");
					bean.getResponse().setReason("S07");
					toXmlFragment.append(bean.getResponse().toXmlFragment3());
				}
			}
		}
		toXmlFragment.append("</responseItems>");
		toXmlFragment.append("</responses>");
		resultMap.put("toXmlFragment", toXmlFragment.toString());
		return resultMap;
	}

	@Override
	public boolean orderListStatusNodifyFromMQ(
			List<UpdateWaybillInfo> updateInfoList)throws Exception {
		boolean flag = false;
		// 记录更新成功的数据
		List<UpdateWaybillInfo> updateInfoListTemp = new ArrayList<UpdateWaybillInfo>();

		// 循环处理金刚接口报文对象
		for (UpdateWaybillInfo bean : updateInfoList) {

			if (bean.getFieldName().equalsIgnoreCase(MAILNO)
					|| bean.getFieldName().equalsIgnoreCase(WEIGHT)
					|| (bean.getFieldName().equalsIgnoreCase(STATUS) && (StatusEnum.ACCEPT.getName()
							.equalsIgnoreCase(bean.getFieldValue())
							
							// 接单失败
							|| StatusEnum.UNACCEPT.getName().equalsIgnoreCase(bean.getFieldValue())
							
							// 签收成功
							|| StatusEnum.SIGNED.getName().equalsIgnoreCase(bean.getFieldValue())
							
							// 签收失败
							|| StatusEnum.FAILED.getName().equalsIgnoreCase(bean.getFieldValue())
							
							// 揽收成功
							|| StatusEnum.GOT.getName().equalsIgnoreCase(bean.getFieldValue())
							
							// 揽收失败
							|| StatusEnum.NOT_SEND.getName().equalsIgnoreCase(bean.getFieldValue())
							
					// 派件扫描成功
					|| StatusEnum.SENT_SCAN.getName().equalsIgnoreCase(bean.getFieldValue())))) {
				
				if (StringUtils.equals("GOT", bean.getInfoContent())) {
					  bean.setType(bean.getAcceptTime()); 
				  }
				
				flag = this.orderStatusNodifyByPartition(bean);
				
				if(!flag){
					break;
				}
			}
		}
		return flag;
	}

	
	/**
	 * 根据订单构造solr对象
	 * 
	 * @param mailObjectDTO
	 * @param order
	 * @param receiver
	 * @param sender
	 */
	private void constructSolrObject(MailObjectDTO mailObjectDTO, Order order,
			TraderInfo receiver, TraderInfo sender) {

		mailObjectDTO.setBuildTask("0");

		mailObjectDTO.setAcceptTime(order.getAcceptTime());
		mailObjectDTO.setAddress(receiver.getAddress());

		mailObjectDTO.setCity(receiver.getCity());
		mailObjectDTO.setCityF(sender.getCity());
		mailObjectDTO.setCreateTime(order.getCreateTime());
		mailObjectDTO.setCustomerId(order.getCustomerId());
		mailObjectDTO.setDistrict(receiver.getDistrict());
		mailObjectDTO.setHoldTime(order.getType());
		mailObjectDTO.setId(order.getId());
		if (NumberUtils.isDigits(order.getLineType())) {
			mailObjectDTO.setLineType(Integer.valueOf(order.getLineType()));
		} else {
			mailObjectDTO.setLineType(1);
		}

		mailObjectDTO.setMailNo(order.getMailNo());
		mailObjectDTO.setMobile(receiver.getMobile());
		mailObjectDTO.setName(receiver.getName());
		mailObjectDTO.setPhone(receiver.getPhone());
		mailObjectDTO.setNumProv(receiver.getNumProv());
		if (order.getWeight() != null)
			mailObjectDTO.setWeight(order.getWeight());
		else
			mailObjectDTO.setWeight(0.0f);
		mailObjectDTO.setStatus(order.getStatus());
		mailObjectDTO.setOrderType(order.getOrderType());

		mailObjectDTO.setNumProvF(sender.getNumProv());
		mailObjectDTO.setCityF(sender.getCity());
		mailObjectDTO.setTxLogisticId(order.getTxLogisticId());

		if (order.getTrimFreight() != null) {
			mailObjectDTO.setTrimFreight(order.getTrimFreight().floatValue());
		}

		if (NumberUtils.isDigits(order.getFreightType())) {
			mailObjectDTO
					.setFreightType(Integer.valueOf(order.getFreightType()));
		}

		mailObjectDTO.setPartitiondate(order.getPartitionDate());

	}

	@Override
	public BuildSearch addOrderFormInfoByMsg(String msg, String partitiondate) throws Exception{

		OrderFormInfo orderFormInfo = new OrderFormInfo().toObject(msg);
		orderFormInfo.setPartitiondate(DateUtil.valueof(partitiondate,
				net.ytoec.kernel.action.common.Constants.DATE_FORMAT_yyyyMMdd));
		orderFormInfo.setLineType(StringUtils.equals(orderFormInfo.getServiceType(), Order.SERVICETYPE_1) ? Order.ONLINE_TYPE : Order.OFFLINE_TYPE);
		
		// 创建订单信息
		Order order = this.addOrderFormInfoForTB(orderFormInfo);
		
		if(order != null){
			
			// 线上订单场合
			if (StringUtils.equals(orderFormInfo.getLineType(),Order.ONLINE_TYPE)) {
				
				// 线上订单推送给金刚
				SendTask sendTask = OrderOperateHelper.makeOrderCreateTask(msg,
						orderFormInfo);
				sendTaskService.addSendTask(sendTask);
			}
			
			// 构建solr对象
			return getSolarDtoForAdd(order);
		}

		return null;
	}
	
	private static String getType(String serviceType) {

		// 将服务类型转换成二进制标识
		serviceType = Integer.toBinaryString(Integer.parseInt(serviceType));
		serviceType = serviceType.substring(serviceType.length() - 1,
				serviceType.length());
		if (StringUtils.equals("1", serviceType)) {
			return "online";
		} else if (StringUtils.equals("0", serviceType)) {
			return "offline";
		}
		return "offline";
	}

	/**
	 * 创建易迅订单
	 * @param tabOrder
	 * @return
	 */
	public int addOrderForYixun(TabUniteOrder tabOrder,String cusCode){
		boolean flag_saveorder = true;
		int result = 0;	//0表示正常创建订单，以下非0值为各种异常状态
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		/*******向 ec_core_order_log标准插入物流号，来判断是否重复*****/
		logger.info("==== before insert ec_core_order_log ====");
		flag_saveorder = orderLogDao.addOrderLog(cusCode+tabOrder.getTxLogisitcId());
		if(!flag_saveorder){
			result = 1;	//订单重复
			return result;
		}
		logger.info("==== after insert ec_core_order_log ====");
		/*****订单实体类*****/
		logger.info("==== before insert ec_core_order ====");
		Order order = new Order();
		order.setLogisticProviderId(tabOrder.getLogisticProviderId());
		order.setTxLogisticId(cusCode+tabOrder.getTxLogisitcId());
		order.setTradeNo("0");
		order.setCustomerId(cusCode);
		
		//面单号
		List<String> subPags = tabOrder.getSubPackages();
		order.setMailNo(subPags.get(0));
		
		order.setFlag("0");
		order.setInsuranceValue(Float.parseFloat(tabOrder.getInsuranceValue()));
		order.setClientId(cusCode);
		order.setStatus("0");
		order.setWeight(Float.parseFloat(tabOrder.getWeight()));
		order.setSignPrice(0);
		order.setLineType("1");
		order.setWeightUpdateFlag("0");
		order.setOrderType(1);
		order.setServiceType(1L);
		order.setGoodsValue(0.0);
		order.setItemsValue(0.0);
		order.setTotalServiceFee(0.0);
		order.setCodSplitFee(0.0);
		
		//日期分区字段	
		try{
			order.setPartitionDate(sdf.parse(sdf.format(new Date())));
		}catch(Exception e){
			
		}
		order.setFreightType("1");
		order.setFreight(0.0);
		order.setTrimFreight(0.0);
		flag_saveorder = dao.addOrder((T)order);
		if(!flag_saveorder){
			result = 2;		//数据库插入订单表错误
			return result;
		}
		logger.info("==== after insert ec_core_order ====");
		/********** 保存收件人和发件人信息 ***********/
		logger.info("==== before insert ec_core_traderinfo for send person ====");
		// 发件人实体类
		TraderInfo sender = new TraderInfo();
		sender.setName(tabOrder.getSendName());
		sender.setPostCode(tabOrder.getSendPostCode());
		sender.setPhone(tabOrder.getSendPhone());
		sender.setMobile(tabOrder.getSendMobile());
		sender.setProv(tabOrder.getSendProv());
		sender.setCity(tabOrder.getSendCity());
		sender.setAddress(tabOrder.getSendAddress());
		sender.setTradeType(TraderInfo.SENDE_TYPE);
		sender.setOrderId(order.getId());
		sender.setDistrict(tabOrder.getSendCounty());
		try{
			sender.setPartitionDate(sdf.parse(sdf.format(new Date())));
		}catch(Exception e){
			
		}
		sender.setRemark(tabOrder.getRemark());
		int numSendProv = 0;
		if(sender.getProv().indexOf("省") > -1){
			numSendProv = Resource.getCodeByName(sender.getProv().replace("省", ""));
		}else if(sender.getProv().indexOf("特别行政区") > -1){
			numSendProv = Resource.getCodeByName(sender.getProv().replace("特别行政区", ""));
		}else{
			numSendProv = Resource.getCodeByName(sender.getProv());
		}
		sender.setNumProv(numSendProv);
		flag_saveorder = traderDao.addTraderInfo(sender);
		if(!flag_saveorder){
			result = 3;	//数据库插入发件人信息错误
			return result;
		}
		logger.info("==== after insert ec_core_traderinfo for send person ====");
		// 收件人实体类
		logger.info("==== before insert ec_core_traderinfo for receive person ====");
		TraderInfo receiver = new TraderInfo();
		receiver.setName(tabOrder.getReceiveName());
		receiver.setPostCode(tabOrder.getReceivePostCode());
		receiver.setPhone(tabOrder.getReceivePhone());
		receiver.setMobile("");
		receiver.setProv(tabOrder.getReceiveProv());
		receiver.setCity(tabOrder.getReceiveCity());
		receiver.setAddress(tabOrder.getReceiveAddress());
		receiver.setTradeType(TraderInfo.RECEIVE_TYPE);
		receiver.setOrderId(order.getId());
		receiver.setDistrict(tabOrder.getReceiveCounty());
		try{
			receiver.setPartitionDate(sdf.parse(sdf.format(new Date())));
		}catch(Exception e){
			
		}
		receiver.setRemark(tabOrder.getRemark());
		int numRevProv = 0;
		if(receiver.getProv().indexOf("省") > -1){
			numRevProv = Resource.getCodeByName(receiver.getProv().replace("省", ""));
		}else if(sender.getProv().indexOf("特别行政区") > -1){
			numRevProv = Resource.getCodeByName(receiver.getProv().replace("特别行政区", ""));
		}else{
			numRevProv = Resource.getCodeByName(receiver.getProv());
		}
		receiver.setNumProv(numRevProv);
		flag_saveorder = traderDao.addTraderInfo(receiver);
		if(!flag_saveorder){
			result = 4;	//数据库插入收件人信息错误
			return result;
		}
		logger.info("==== after insert ec_core_traderinfo for receive person ====");
		/******** 保存商品信息 **********/
		logger.info("==== before insert ec_core_product ====");
		List<GoodInfo> goodList = tabOrder.getGoods();
		for(GoodInfo good : goodList){
			Product product = new Product();
			product.setItemName(good.getGoodName());
			product.setItemNumber(good.getPiece());
			product.setRemark(good.getRemark());
			product.setOrderId(order.getId());
			product.setItemValue((Double.parseDouble(good.getPrice())/100)*(good.getPiece()));
			product.setLogisticId(order.getTxLogisticId());
			flag_saveorder = productDao.addProduct(product);
			if(!flag_saveorder){
				result = 5;	//数据库插入商品信息错误
				return result;
			}
		}
		logger.info("==== after insert ec_core_product ====");
		/********* 保存订单相关信息到搜索引擎 ***********/
		logger.info("==== before insert ec_core_build_search ====");
		User user = Resource.getUserByCustomerId(cusCode);
		if (user != null) {
			MailObjectDTO traderinfo = new MailObjectDTO();
			traderinfo.setProv(receiver.getProv());
			traderinfo.setProvF(sender.getProv());
			traderinfo.setName(receiver.getName());
			traderinfo.setPhone(receiver.getPhone());
			traderinfo.setMobile(receiver.getMobile());
			traderinfo.setNumProv(receiver.getNumProv());
			traderinfo.setNumProvF(sender.getNumProv());
			traderinfo.setCity(receiver.getCity());
			traderinfo.setCityF(sender.getCity());
			traderinfo.setAddress(receiver.getAddress());
			traderinfo.setDistrict(receiver.getDistrict());
			BuildSearch buildSearch = setBuildSearch(order, traderinfo);
			buildSearch.setBuildTask("0");
			flag_saveorder = buildSearchDao.addBuildSearchByUpdateOrder(buildSearch);
			if(!flag_saveorder){
				result = 6;	//保存搜索引擎错误
				return result;
			}
		}
		logger.info("==== after insert ec_core_build_search ====");
		/********* 存入ec_core_jgwaybill **********/
		logger.info("==== before insert ec_core_jgwaybill ====");
		JgWaybill jgWaybill = new JgWaybill();
		jgWaybill.setServiceType(String.valueOf(order.getServiceType()));
		jgWaybill.setbAddress(receiver.getAddress());
		jgWaybill.setbCity(receiver.getCity());
		jgWaybill.setbDistrict(receiver.getDistrict());
		jgWaybill.setbMobile(receiver.getMobile());
		jgWaybill.setbName(receiver.getName());
		jgWaybill.setbPhone(receiver.getPhone());
		jgWaybill.setbPostCode(receiver.getPostCode());
		jgWaybill.setbProv(receiver.getProv());
		jgWaybill.setMailNo(order.getMailNo());
		jgWaybill.setOrderId(order.getId());
		jgWaybill.setsAddress(sender.getAddress());
		jgWaybill.setsCity(sender.getCity());
		jgWaybill.setsProv(sender.getProv());
		jgWaybill.setsPostCode(sender.getPostCode());
		jgWaybill.setsPhone(sender.getPhone());
		jgWaybill.setsName(sender.getName());
		jgWaybill.setsMobile(sender.getMobile());
		jgWaybill.setsDistrict(sender.getDistrict());
		jgWaybill.setLogisticId(order.getTxLogisticId());
		jgWaybill.setLineType(order.getLineType());
		jgWaybill.setClientID(order.getClientId());
		flag_saveorder = this.jgWaybillDao.addJgWaybill(jgWaybill);
		if(!flag_saveorder){
			result = 7;	//存入JGWaybill表错误
			return result;
		}
		logger.info("==== after insert ec_core_jgwaybill ====");
		return result;
	}
	
	public BuildSearchStatusWeightIndex getSolarDto(String txLogisticID){
		Order order = this.getOrderByLogisticId(txLogisticID);
		MailObjectDTO traderinfo = new MailObjectDTO();
		if (order != null) {
			logger.error(order.getId() + "==="+ order.getPartitionDate());
			traderinfo = dao.bulidPartEccoreDataByOrderId(
					order.getId(), DateUtil.format(
							order.getPartitionDate(), "yyyy-MM-dd"));
		}
		if (order != null) {
			User user = Resource.getUserByCustomerId(order
					.getCustomerId());
			if (user != null) {
				
				BuildSearchStatusWeightIndex dataObject = setBuildSearchStatusWeightIndex(order,traderinfo);
				
				return dataObject;
			}
		}
		return null;
	}

	@Override
	public List<T> getListOrderByMailNo(String mailNo) {

		return  dao.getOrderByMailNo(mailNo);
	}

	/**
	 * 获取重量更新信息实体类
	 * @param mailNo
	 * @return
	 */
	public List<BuildSearchStatusWeightIndex> getSolarUpdateWeight(String mailNo) {

		List<BuildSearchStatusWeightIndex> dataObjectList = new ArrayList<BuildSearchStatusWeightIndex>();

		// 根据面单号获取订单结果集
		List<Order> orderList = (List<Order>) dao.getOrderByMailNo(mailNo);

		// 获取结果集
		if (orderList != null && orderList.size() > 0) {
			for (Order bean : orderList) {
				
				// 获取数据到solr的dto
				MailObjectDTO traderinfo = new MailObjectDTO();
				traderinfo = dao.bulidPartEccoreDataByOrderId(bean.getId(),
						DateUtil.format(bean.getPartitionDate(), "yyyy-MM-dd"));

				// 获取用户信息
				User user = Resource.getUserByCustomerId(bean.getCustomerId());

				// 用户信息非空的场合
				if (user != null) {

					// 获取订单更新信息实体类
					BuildSearchStatusWeightIndex dataObject = setBuildSearchStatusWeightIndex(
							bean, traderinfo);

					dataObjectList.add(dataObject);
				}
			}
			return dataObjectList;
		}

		return null;
	}
	
	public BuildSearch getSolarDtoForAdd(Order order){
		
		TraderInfo receiver = order.getReceiver();
		TraderInfo sender = order.getSender();
		User user = Resource.getUserByCustomerId(order.getCustomerId());
		if (user != null) {

			MailObjectDTO traderinfo = new MailObjectDTO();
			traderinfo.setProv(receiver.getProv());
			traderinfo.setProvF(sender.getProv());
			traderinfo.setName(receiver.getName());
			traderinfo.setPhone(receiver.getPhone());
			traderinfo.setMobile(receiver.getMobile());
			traderinfo.setNumProv(receiver.getNumProv());
			traderinfo.setNumProvF(sender.getNumProv());
			traderinfo.setCity(receiver.getCity());
			traderinfo.setCityF(sender.getCity());
			traderinfo.setAddress(receiver.getAddress());
			traderinfo.setDistrict(receiver.getDistrict());
			BuildSearch buildSearch = setBuildSearch(order, traderinfo);
			buildSearch.setBuildTask("0");
			return buildSearch;
			}
		return null;
	}
	
}
