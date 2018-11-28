package net.ytoec.kernel.action.remote;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.xml.PassMessageQueryOrder;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.ApplyKdbz;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.ApplyKdbzService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.OrderCODService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * 提供给TAOBAO的接口地址Action. 此类目前已不再使用，此类里面的方法调用有问题
 * 
 */
@Controller
@Scope("prototype")
public class TaoBaoOrderAction extends AbstractInterfaceAction {

	private static final long serialVersionUID = 1031890960559598071L;

	// 消息内容
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";

	// 消息签名
	private static final String DATA_DIGEST_PARAM = "data_digest";
	
	// TP编号
	private static final String DATA_LOGISTIC_PROVIDER_PARAM = "logistic_provider_id"; 
	private static final String DATA_YTO = "YTO"; 
	private static final String DATA_TAOBAO = "TAOBAO"; 

	// 电商标识
	private static final String CLIENT_ID_PARAM = "ecCompanyId";

	// 消息类型
	private static final String MSG_TYPE = "msg_type";

	private static final String GET_METHOD_RESPONSE = "Success";

	public static String ORDER_BIND = "TaoBaoOrderBindSchema.xsd";
	public static String ORDER_CANCEL = "TaoBaoOrderCancelSchema.xsd";
	public static String ORDER_CREATE = "TaoBaoOrderCreateSchema.xsd";
	public static String ORDER_QUERY = "TaoBaoOrderQuerySchema.xsd";
	public static String ORDER_PAY = "TaoBaoCODSchema.xsd";
	public static String SERVICE_APPLY = "TaoBaoServiceApplySchema.xsd";
	public static String SERVICE_CANCEL = "TaoBaoServiceCancelSchema.xsd";

	/**
	 * 金刚接口访问地址.
	 */
	private static final String JIN_GANG_ORDER = "JINGANG.ORDER";
	private static final String JIN_GANG_QOERY = "JINGGANG.QUERY";

	@Autowired
	private ApplyKdbzService<ApplyKdbz> applyKdbzService;

	@Autowired
	private OrderService<Order> orderService;

	@Autowired
	private TraderInfoService<TraderInfo> traderInfoService;

	@Autowired
	private SMSPortService smsPortService;

	@Autowired
	private OrderCODService orderCODService;

	private static Logger logger = LoggerFactory.getLogger(TaoBaoOrderAction.class);
	// 物流平台标识
	private String JGtxLogisticID = "0";

	// 物流平台标识
	private String VIPtxLogisticID = "0";

	@Autowired
	private SMSInfoService smsInfoService;
	@Autowired
	private UserService<User> userService;
	@Autowired
	private MessageService<Message> messageService;

	/**
	 * TAOBAO的接口调用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String vipOrderServlet() throws Exception {
		logger.info(" TAOBAO的接口调用 into start");
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			super.print(GET_METHOD_RESPONSE);
			return null;
		}
		String classpath = Thread.currentThread().getContextClassLoader()
		.getResource("/").toString();
		
		//String classpath =  this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		classpath = classpath.replace("file:", "");
		logger.error("classpath:" + classpath);

		// 1.获取参数值.
		String logisticsInterface = request
				.getParameter(LOGISTICS_INTERFACE_PARAM);
		String dataDigest = request.getParameter(DATA_DIGEST_PARAM);

		// 电商标识
		String clientID = request.getParameter(CLIENT_ID_PARAM);

		// 消息类型
		String msgType = request.getParameter(MSG_TYPE);
		String type = request.getParameter("type");

		String ip = null;
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}

		// 判断淘宝发送给物流公司的参数是否为空
		if (logisticsInterface == null || dataDigest == null
				|| clientID == null || (msgType == null && type == null)) {
			print(Response.DATA_INSECURITY_RESPONSE_5.toXmlFragment());
			return null;
		}

		// 对非法IP的权限限制
		/*
		 * boolean ipFlag = IpWhiteListProcessor.checkIp(clientID, ip); if
		 * (!ipFlag) { print("您好，此IP为非法IP，如有疑问请与管理员联系 QQ：2294882345！");
		 * logger.error("非法用户访问！ip：" + ip + "  clientId:" + clientID); return
		 * null; }
		 */

		// 判断接口是否是（test.jsp）测试使用
		if (request.getParameter("test") != null) {
			logisticsInterface = decode(logisticsInterface,
					XmlSender.UTF8_CHARSET);
			dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
		}

		// liug编码---发送给金刚
		String jglogisticsInterface = null;
		String jgdataDigest = null;
		jglogisticsInterface = encode(logisticsInterface,
				XmlSender.UTF8_CHARSET);
		jgdataDigest = encode(dataDigest, XmlSender.UTF8_CHARSET);

		// 验证
		logger.error("TaoBaoOrderAction:logisticsInterface=== "
				+ logisticsInterface);
		logger.error("TaoBaoOrderAction:dataDigest=== " + dataDigest);

		// 淘宝安全验证
		if (!validateData(logisticsInterface, dataDigest)) {
			print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
			logger.error(LogInfoEnum.PARAM_INVALID.getValue() + ",S02"
					+ "logisticsInterface:" + logisticsInterface
					+ ",dataDigest:" + dataDigest);
			return null;
		}

		// 处理
		// 接受参数已经处理
		String xmlFragment = logisticsInterface;

		String responseString = null;

		
		try {

			// 服务协议接口-->申请/取消
			if (xmlFragment.contains("<ServiceApply")
					&& xmlFragment.contains("</ServiceApply")) {
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ SERVICE_APPLY)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface + ",classpath:"
							+ classpath);
					return null;
				}
				return "toTaobaoServiceApplyServlet";
			} else if (xmlFragment.contains("<ServiceCancel")
					&& xmlFragment.contains("</ServiceCancel")) {
				
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ SERVICE_CANCEL)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface + ",classpath:"
							+ classpath);
					return null;
				}
				return "toTaobaoServiceApplyServlet";
			}

			// 支付接口 --> 支付成功
			else if (xmlFragment.contains("<UpdateInfo>")
					&& xmlFragment.contains("</UpdateInfo>")
					&& xmlFragment.contains("STATUS")
					&& xmlFragment.contains("PAY_SUCCESS")) {
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_PAY)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}
				responseString = cod(xmlFragment, jglogisticsInterface,
						jgdataDigest, msgType);
			}

			// 支付接口 --> 支付失败
			else if (xmlFragment.contains("<UpdateInfo>")
					&& xmlFragment.contains("</UpdateInfo>")
					&& xmlFragment.contains("STATUS")
					&& xmlFragment.contains("PAY_FAILED")) {
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_PAY)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}
				responseString = cod(xmlFragment, jglogisticsInterface,
						jgdataDigest, msgType);
			}

			// 订单检索使用
			else if (xmlFragment.contains("<BatchQueryRequest")
					&& xmlFragment.contains("</BatchQueryRequest")) {
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_QUERY)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}
				responseString = orderSearch(jglogisticsInterface, jgdataDigest);
			}

			// 订单创建
			else if (StringUtils.equalsIgnoreCase("ORDERCREATE", msgType)) {

				// schema 验证xsd没有变更
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_CREATE)) {

					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}
				
				// 根据xml片段信息生成对象数据.
				OrderFormInfo requestOrder = new OrderFormInfo().toObject(xmlFragment);
				
				// 创建订单
				responseString = orderCreate(requestOrder,
						jglogisticsInterface, jgdataDigest);
			}

			// 订单更新/消除订单
			else if (StringUtils.equalsIgnoreCase("UPDATE", msgType)) {
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_CANCEL)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}

				// 订单更新/消除订单
				responseString = orderUpdateAndCancel(xmlFragment, clientID,
						jgdataDigest);
			}

		} catch (Exception re) {
			responseString = null;
			logger.error("error", re);
		}

		// 系统错误
		if (responseString == null) {
			responseString = Response.DATA_INSECURITY_RESPONSE_9
					.toXmlFragment();
			super.print(responseString);
			logger.error("order create failed:" + logisticsInterface);
			return null;
		}

		super.print(responseString);
		return null;
	}

	/**
	 * 订单创建.
	 */
	private String orderCreate(OrderFormInfo requestOrder,
			String jglogisticsInterface, String jgdataDigest) throws Exception {

		// 根据服务类型来判断 下单类型(online/offline)
		String type = getType(requestOrder.getServiceType());
		logger.debug("orderCreate" + requestOrder.getTxLogisticId());
		if (StringUtils.isEmpty(requestOrder.getOrderType()) || 
				StringUtils.isEmpty(requestOrder.getServiceType()) || 
				(StringUtils.isEmpty(requestOrder.getSender().getMobile()) && StringUtils
						.isEmpty(requestOrder.getSender().getPhone()))
				|| (StringUtils.isEmpty(requestOrder.getReceiver().getMobile()) && StringUtils
						.isEmpty(requestOrder.getReceiver().getPhone()))
				|| (StringUtils.equals("offline", type) && StringUtils
						.isEmpty(requestOrder.getMailNo()))) {
			
			requestOrder.getResponse().setTxLogisticId(
					requestOrder.getTxLogisticId());
			requestOrder.getResponse().setLogisticProviderId(
					requestOrder.getLogisticProviderId());
			requestOrder.getResponse().setSuccess("false");
			requestOrder.getResponse().setReason("S05");
			String toXmlFragment = requestOrder.getResponse().toXmlFragment1();

			return toXmlFragment;
		}

		// 设置分区时间
		requestOrder.setPartitiondate(Calendar.getInstance().getTime());

		// 订单类型设置
		if (StringUtils.equals("online", type)) {

			// 线上订单
			requestOrder.setLineType(Order.ONLINE_TYPE);
		} else if (StringUtils.equals("offline", type)) {

			// 线下订单
			requestOrder.setLineType(Order.OFFLINE_TYPE);
		} else {

			// 线下订单
			requestOrder.setLineType(Order.OFFLINE_TYPE);
		}
		requestOrder.setClientId(request.getParameter(CLIENT_ID_PARAM));
		String toXmlFragment = null;
		// 插入数据库
		/*toXmlFragment = this.orderService.addOrderFormInfo(requestOrder,
				jgdataDigest, jglogisticsInterface, LOGISTICS_INTERFACE_PARAM,
				DATA_DIGEST_PARAM, JIN_GANG_ORDER, MSG_TYPE);*/

		return toXmlFragment;
	}

	/**
	 * 订单查询. 对于订单查询操作,系统不做实际的查询处理,直接将此请求转发给金刚系统,并由从金刚得到的响应结果返回给电商.
	 * 此操作对于电商、核心平台、金刚三者是一个同步操作.
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return 返回响应信息.
	 * @throws Exception
	 */
	private String orderSearch(String logisticsInterface, String dataDigest)
			throws Exception {
		String url = Resource.getChannel(JIN_GANG_QOERY);
//		String url = "http://jingang.yto56.com.cn/ordws/Vip15Servlet";
		logger.info("订单查询请求转发给金刚系统==>"+url);
		XmlSender xmlSender = new XmlSender();
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
		xmlSender.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
				+ logisticsInterface + "&" + DATA_DIGEST_PARAM + "="
				+ dataDigest);
		return xmlSender.send();
	}

	/**
	 * 订单查询
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return 返回响应信息.
	 * @throws Exception
	 */
	public List<QueryOrder> ordersSearch(String[] logisticsId) throws Exception {
		if (logisticsId == null || logisticsId.length < 1)
			return null;
		List<QueryOrder> orderList = null;
		try {
			// 构建XML
			String logisticsInterface = "";
			String dataDigest = "";

			StringBuilder sb = new StringBuilder();
			sb.append("<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><orders>");
			for (int i = 0; i < logisticsId.length; i++) {
				sb.append("<order><mailNo>");
				sb.append(logisticsId[i]);
				sb.append("</mailNo></order>");
			}
			sb.append("</orders></BatchQueryRequest>");
			logisticsInterface = sb.toString();
			logger.debug(logisticsInterface);
			
			dataDigest = Md5Encryption.MD5Encode(logisticsInterface
					+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO());
			
			String utf8LogisticsInterface = encode(logisticsInterface, "UTF-8");
			String utf8DataDigest = encode(dataDigest, "UTF-8");

			// 发送查询请求
			String xmlFragment = orderSearch(utf8LogisticsInterface,utf8DataDigest);
			// 去除相应报文里的空格
			if (xmlFragment != null)
				xmlFragment = xmlFragment.replace(" ", "");
			logger.debug(xmlFragment);
			// 解析XML
			InputStream inputStream = ProcessorUtils
					.getInputStream(xmlFragment.toString());
			DocumentReader documentReader = new DocumentReader();
			Document document = documentReader.getDocument(inputStream);
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			orderList = new ArrayList<QueryOrder>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if ("orders".equals(node.getNodeName())) {
					NodeList order = node.getChildNodes();
					logger.debug("****" + order.getLength());
					for (int j = 0; j < order.getLength(); j++) {
						// QueryOrder(order.item(j).getTextContent());
						QueryOrder queryOrder = new QueryOrder(order.item(j));
						if (queryOrder.getSteps() == null
								|| queryOrder.getSteps().size() < 1)
							queryOrder.setOrderStatus("NULL");
						orderList.add(queryOrder);
					}
				}
			}
		} catch (Exception e) {
			logger.error("订单查询失败", e);
			return null;
		}
		return orderList;
	}
	
	/**
	 * 时效提醒的订单查询
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return 返回响应信息.
	 * @throws Exception
	 */
	public List<PassMessageQueryOrder> ManageOrdersSearch(String[] logisticsId) throws Exception {
		if (logisticsId == null || logisticsId.length < 1)
			return null;
		List<PassMessageQueryOrder> orderList = null;
		try {
			// 构建XML
			String logisticsInterface = "";
			String dataDigest = "";

			StringBuilder sb = new StringBuilder();
			sb.append("<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><orders>");
			for (int i = 0; i < logisticsId.length; i++) {
				sb.append("<order><mailNo>");
				sb.append(logisticsId[i]);
				sb.append("</mailNo></order>");
			}
			sb.append("</orders></BatchQueryRequest>");
			logisticsInterface = sb.toString();
			logger.debug(logisticsInterface);
			
			dataDigest = Md5Encryption.MD5Encode(logisticsInterface
					+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO());
			
			String utf8LogisticsInterface = encode(logisticsInterface, "UTF-8");
			String utf8DataDigest = encode(dataDigest, "UTF-8");

			// 发送查询请求
			String xmlFragment = orderSearch(utf8LogisticsInterface,utf8DataDigest);
//			StringBuffer xmlFragment=new StringBuffer();
//			xmlFragment.append("<BatchQueryResponse><logisticProviderID>YTO</logisticProviderID><orders><order><mailNo>3306427179</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 10:12:56.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 12:15:08.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 18:05:35.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 22:12:23.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:09:38.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:18:33.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-15 03:37:36.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 03:41:25.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-16 09:03:29.0 CST</acceptTime><acceptAddress>广州转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>200901</orgCode></step><step><acceptTime>2013-09-16 09:05:54.0 CST</acceptTime><acceptAddress>广州转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>200901</orgCode></step><step><acceptTime>2013-09-16 11:21:01.0 CST</acceptTime><acceptAddress>虎门转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>769901</orgCode></step><step><acceptTime>2013-09-16 11:24:10.0 CST</acceptTime><acceptAddress>虎门转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>769901</orgCode></step><step><acceptTime>2013-09-16 11:25:05.0 CST</acceptTime><acceptAddress>虎门转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>769901</orgCode></step><step><acceptTime>2013-09-16 12:33:32.0 CST</acceptTime><acceptAddress>广东省东莞市虎门公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>769020</orgCode></step><step><acceptTime>2013-09-16 12:50:59.0 CST</acceptTime><acceptAddress>广东省东莞市虎门公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>769020</orgCode></step><step><acceptTime>2013-09-17 09:56:48.0 CST</acceptTime><acceptAddress>广东省东莞市麻涌公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>769026</orgCode></step><step><acceptTime>2013-09-17 12:52:52.0 CST</acceptTime><acceptAddress>广东省东莞市麻涌公司</acceptAddress><name>郭锦全</name><status>true</status><remark>派件中</remark><orgCode>769026</orgCode></step><step><acceptTime>2013-09-17 15:39:52.0 CST</acceptTime><acceptAddress>广东省东莞市麻涌公司</acceptAddress><name>李志昌</name><status>true</status><remark>已签收</remark><orgCode>769026</orgCode></step></steps></order><order><mailNo>3306427413</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 10:30:28.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 14:06:51.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 18:07:20.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 22:07:11.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 22:19:50.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:14:12.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-15 03:37:45.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 03:41:21.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-16 01:11:29.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-16 01:12:17.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-17 00:07:12.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-17 00:09:17.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-17 00:22:35.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-17 02:39:31.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-17 07:23:05.0 CST</acceptTime><acceptAddress>黑龙江省哈尔滨市阿城市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>451049</orgCode></step><step><acceptTime>2013-09-17 08:04:46.0 CST</acceptTime><acceptAddress>黑龙江省哈尔滨市阿城市公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>451049</orgCode></step><step><acceptTime>2013-09-17 08:24:46.0 CST</acceptTime><acceptAddress>黑龙江省哈尔滨市阿城市公司</acceptAddress><name>周井龙</name><status>true</status><remark>派件中</remark><orgCode>451049</orgCode></step><step><acceptTime>2013-09-17 16:20:42.0 CST</acceptTime><acceptAddress>黑龙江省哈尔滨市阿城市公司</acceptAddress><name>周爽</name><status>true</status><remark>已签收</remark><orgCode>451049</orgCode></step></steps></order><order><mailNo>3306446620</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 09:20:16.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 14:13:59.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 18:05:37.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 22:05:33.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-13 23:58:14.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 00:10:27.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 04:09:39.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-14 04:13:20.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 08:20:16.0 CST</acceptTime><acceptAddress>成都转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>280001</orgCode></step><step><acceptTime>2013-09-15 18:50:10.0 CST</acceptTime><acceptAddress>成都转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>280001</orgCode></step><step><acceptTime>2013-09-16 04:59:58.0 CST</acceptTime><acceptAddress>重庆市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>230001</orgCode></step><step><acceptTime>2013-09-16 05:00:20.0 CST</acceptTime><acceptAddress>重庆市公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>230001</orgCode></step><step><acceptTime>2013-09-16 21:25:20.0 CST</acceptTime><acceptAddress>重庆市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>230001</orgCode></step><step><acceptTime>2013-09-17 08:16:21.0 CST</acceptTime><acceptAddress>重庆市江津区公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>230019</orgCode></step><step><acceptTime>2013-09-17 17:57:37.0 CST</acceptTime><acceptAddress>重庆市江津区公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>230019</orgCode></step><step><acceptTime>2013-09-17 18:17:42.0 CST</acceptTime><acceptAddress>重庆市江津区公司</acceptAddress><name>林松</name><status>true</status><remark>派件中</remark><orgCode>230019</orgCode></step><step><acceptTime>2013-09-17 18:27:21.0 CST</acceptTime><acceptAddress>重庆市江津区公司</acceptAddress><name>门卫</name><status>true</status><remark>已签收</remark><orgCode>230019</orgCode></step></steps></order><order><mailNo>3306427207</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 10:15:15.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 12:18:40.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 18:14:13.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 21:51:04.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-13 23:53:55.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 00:08:25.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 04:11:37.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-14 04:12:18.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 01:17:56.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-15 01:17:24.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-15 08:55:52.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-15 23:43:28.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-16 06:56:13.0 CST</acceptTime><acceptAddress>吉林省吉林市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>432001</orgCode></step><step><acceptTime>2013-09-16 07:06:02.0 CST</acceptTime><acceptAddress>吉林省吉林市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>432001</orgCode></step><step><acceptTime>2013-09-16 08:57:50.0 CST</acceptTime><acceptAddress>吉林省吉林市昌邑区公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>432015</orgCode></step><step><acceptTime>2013-09-17 17:16:37.0 CST</acceptTime><acceptAddress>吉林省吉林市昌邑区公司</acceptAddress><name>钟超</name><status>true</status><remark>派件中</remark><orgCode>432015</orgCode></step><step><acceptTime>2013-09-17 17:30:31.0 CST</acceptTime><acceptAddress>吉林省吉林市昌邑区公司</acceptAddress><name>段悦</name><status>true</status><remark>已签收</remark><orgCode>432015</orgCode></step></steps></order><order><mailNo>3306427194</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 10:14:15.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 12:15:56.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 18:11:58.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 21:59:41.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:50:04.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:57:19.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-15 04:05:50.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 04:09:22.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-16 01:08:30.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-16 01:09:40.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-16 23:43:36.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-17 02:18:39.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-17 06:30:31.0 CST</acceptTime><acceptAddress>黑龙江省大庆市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>459001</orgCode></step><step><acceptTime>2013-09-17 06:37:28.0 CST</acceptTime><acceptAddress>黑龙江省大庆市公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>459001</orgCode></step><step><acceptTime>2013-09-17 11:06:39.0 CST</acceptTime><acceptAddress>黑龙江省大庆市萨尔图区新村二分部公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>459025</orgCode></step><step><acceptTime>2013-09-17 12:13:26.0 CST</acceptTime><acceptAddress>黑龙江省大庆市萨尔图区新村二分部公司</acceptAddress><name>秦艳敏</name><status>true</status><remark>派件中</remark><orgCode>459025</orgCode></step><step><acceptTime>2013-09-17 16:43:30.0 CST</acceptTime><acceptAddress>黑龙江省大庆市萨尔图区新村二分部公司</acceptAddress><name>曹晓霞</name><status>true</status><remark>已签收</remark><orgCode>459025</orgCode></step></steps></order><order><mailNo>3306427073</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 10:04:51.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 12:14:05.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 18:17:18.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 22:03:03.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:44:10.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:49:47.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-15 04:11:05.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 04:14:51.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-16 09:01:43.0 CST</acceptTime><acceptAddress>广州转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>200901</orgCode></step><step><acceptTime>2013-09-16 09:02:49.0 CST</acceptTime><acceptAddress>广州转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>200901</orgCode></step><step><acceptTime>2013-09-16 11:20:51.0 CST</acceptTime><acceptAddress>虎门转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>769901</orgCode></step><step><acceptTime>2013-09-16 11:38:57.0 CST</acceptTime><acceptAddress>虎门转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>769901</orgCode></step><step><acceptTime>2013-09-16 11:43:13.0 CST</acceptTime><acceptAddress>虎门转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>769901</orgCode></step><step><acceptTime>2013-09-16 21:17:57.0 CST</acceptTime><acceptAddress>广东省汕头市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>754001</orgCode></step><step><acceptTime>2013-09-17 01:13:44.0 CST</acceptTime><acceptAddress>广东省汕头市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>754001</orgCode></step><step><acceptTime>2013-09-17 08:15:13.0 CST</acceptTime><acceptAddress>广东省揭阳市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>754007</orgCode></step><step><acceptTime>2013-09-17 08:45:34.0 CST</acceptTime><acceptAddress>广东省揭阳市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>754007</orgCode></step><step><acceptTime>2013-09-17 09:30:18.0 CST</acceptTime><acceptAddress>广东省揭阳市榕城区分部公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>754018</orgCode></step><step><acceptTime>2013-09-17 09:56:06.0 CST</acceptTime><acceptAddress>广东省揭阳市榕城区分部公司</acceptAddress><name>张仰</name><status>true</status><remark>派件中</remark><orgCode>754018</orgCode></step><step><acceptTime>2013-09-17 11:23:38.0 CST</acceptTime><acceptAddress>广东省揭阳市榕城区分部公司</acceptAddress><name>黄志兴</name><status>true</status><remark>已签收</remark><orgCode>754018</orgCode></step></steps></order><order><mailNo>3306446647</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-12 09:59:05.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 15:43:39.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>柳延杰</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 17:45:54.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 22:13:37.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-12 22:51:48.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-12 23:25:09.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-13 03:44:57.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-13 03:46:10.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-14 13:49:56.0 CST</acceptTime><acceptAddress>成都转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>280001</orgCode></step><step><acceptTime>2013-09-14 13:55:53.0 CST</acceptTime><acceptAddress>成都转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>280001</orgCode></step><step><acceptTime>2013-09-14 23:09:08.0 CST</acceptTime><acceptAddress>成都转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>280001</orgCode></step><step><acceptTime>2013-09-15 05:42:50.0 CST</acceptTime><acceptAddress>四川省成都市双流县航空港公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>280080</orgCode></step><step><acceptTime>2013-09-15 08:43:47.0 CST</acceptTime><acceptAddress>四川省成都市双流县航空港公司</acceptAddress><name>胡坤</name><status>true</status><remark>派件中</remark><orgCode>280080</orgCode></step><step><acceptTime>2013-09-16 15:11:07.0 CST</acceptTime><acceptAddress>四川省成都市双流县航空港公司</acceptAddress><name>杨将</name><status>true</status><remark>已签收</remark><orgCode>280080</orgCode></step></steps></order><order><mailNo>3306446823</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-12 15:54:44.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>肖娟之</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 17:25:17.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 18:13:13.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 21:57:31.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-12 23:58:24.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-13 00:05:19.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-13 04:05:35.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-13 04:09:18.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-14 00:46:16.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-14 00:46:34.0 CST</acceptTime><acceptAddress>长春转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>431001</orgCode></step><step><acceptTime>2013-09-14 13:45:42.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-14 19:44:38.0 CST</acceptTime><acceptAddress>哈尔滨转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>451001</orgCode></step><step><acceptTime>2013-09-15 05:50:27.0 CST</acceptTime><acceptAddress>黑龙江省大庆市公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>459001</orgCode></step><step><acceptTime>2013-09-15 06:05:51.0 CST</acceptTime><acceptAddress>黑龙江省大庆市公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>459001</orgCode></step><step><acceptTime>2013-09-15 06:07:00.0 CST</acceptTime><acceptAddress>黑龙江省大庆市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>459001</orgCode></step><step><acceptTime>2013-09-15 11:17:37.0 CST</acceptTime><acceptAddress>黑龙江省大庆市让胡路区龙新分部公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>459021</orgCode></step><step><acceptTime>2013-09-15 13:07:48.0 CST</acceptTime><acceptAddress>黑龙江省大庆市让胡路区龙新分部公司</acceptAddress><name>孙俪</name><status>true</status><remark>派件中</remark><orgCode>459021</orgCode></step><step><acceptTime>2013-09-16 13:09:11.0 CST</acceptTime><acceptAddress>黑龙江省大庆市让胡路区龙新分部公司</acceptAddress><name>孙俪.</name><status>true</status><remark>派件中</remark><orgCode>459021</orgCode></step><step><acceptTime>2013-09-16 13:34:31.0 CST</acceptTime><acceptAddress>黑龙江省大庆市让胡路区龙新分部公司</acceptAddress><name>韩旭</name><status>true</status><remark>已签收</remark><orgCode>459021</orgCode></step></steps></order><order><mailNo>3306427134</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-13 10:09:56.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>刘小琳</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-13 12:14:37.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 18:13:14.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-14 22:11:06.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 22:21:37.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-14 23:32:19.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-15 03:33:41.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-15 03:37:14.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-16 09:57:04.0 CST</acceptTime><acceptAddress>武汉转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>270902</orgCode></step><step><acceptTime>2013-09-16 10:19:28.0 CST</acceptTime><acceptAddress>武汉转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>270902</orgCode></step><step><acceptTime>2013-09-16 21:38:01.0 CST</acceptTime><acceptAddress>湖北省武昌区青山公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>270030</orgCode></step><step><acceptTime>2013-09-16 22:06:33.0 CST</acceptTime><acceptAddress>湖北省武昌区青山公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>270030</orgCode></step><step><acceptTime>2013-09-16 22:50:07.0 CST</acceptTime><acceptAddress>湖北省武汉市武昌区青山杨园公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>270101</orgCode></step><step><acceptTime>2013-09-17 07:39:07.0 CST</acceptTime><acceptAddress>湖北省武汉市武昌区青山杨园公司</acceptAddress><name>李群</name><status>true</status><remark>派件中</remark><orgCode>270101</orgCode></step><step><acceptTime>2013-09-17 11:25:31.0 CST</acceptTime><acceptAddress>湖北省武汉市武昌区青山杨园公司</acceptAddress><name>陈</name><status>true</status><remark>已签收</remark><orgCode>270101</orgCode></step></steps></order><order><mailNo>3306446142</mailNo><mailType>EXPRESS</mailType><orderStatus>SIGNED</orderStatus><steps><step><acceptTime>2013-09-12 16:22:59.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>肖娟之</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 17:33:02.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><name>吴雪萍</name><status>true</status><remark>已收件</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 18:21:14.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已打包</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 18:23:22.0 CST</acceptTime><acceptAddress>山东省青岛市胶南市公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>532009</orgCode></step><step><acceptTime>2013-09-12 21:50:14.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-12 21:51:39.0 CST</acceptTime><acceptAddress>潍坊转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>536901</orgCode></step><step><acceptTime>2013-09-13 02:07:08.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-13 02:10:25.0 CST</acceptTime><acceptAddress>济南转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>531901</orgCode></step><step><acceptTime>2013-09-13 12:48:41.0 CST</acceptTime><acceptAddress>郑州转运中心公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>371901</orgCode></step><step><acceptTime>2013-09-13 13:05:02.0 CST</acceptTime><acceptAddress>郑州转运中心公司</acceptAddress><status>true</status><remark>已拆包</remark><orgCode>371901</orgCode></step><step><acceptTime>2013-09-14 02:05:36.0 CST</acceptTime><acceptAddress>郑州转运中心公司</acceptAddress><status>true</status><remark>已发出</remark><orgCode>371901</orgCode></step><step><acceptTime>2013-09-14 08:01:08.0 CST</acceptTime><acceptAddress>河南省新乡市封丘县公司</acceptAddress><status>true</status><remark>已收入</remark><orgCode>373003</orgCode></step><step><acceptTime>2013-09-14 14:22:56.0 CST</acceptTime><acceptAddress>河南省新乡市封丘县公司</acceptAddress><name>刘东旭</name><status>true</status><remark>派件中</remark><orgCode>373003</orgCode></step></steps></order></orders></BatchQueryResponse>");
			// 去除相应报文里的空格
			if (xmlFragment != null)
				xmlFragment = xmlFragment.replace(" ", "");
			logger.debug(xmlFragment);
			// 解析XML
			InputStream inputStream = ProcessorUtils
					.getInputStream(xmlFragment.toString());
			DocumentReader documentReader = new DocumentReader();
			Document document = documentReader.getDocument(inputStream);
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			orderList = new ArrayList<PassMessageQueryOrder>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if ("orders".equals(node.getNodeName())) {
					NodeList order = node.getChildNodes();
					logger.debug("****" + order.getLength());
					for (int j = 0; j < order.getLength(); j++) {
						// QueryOrder(order.item(j).getTextContent());
						PassMessageQueryOrder queryOrder = new PassMessageQueryOrder(order.item(j));
						if (queryOrder.getSteps() == null
								|| queryOrder.getSteps().size() < 1)
							queryOrder.setOrderStatus("NULL");
						orderList.add(queryOrder);
					}
				}
			}
		} catch (Exception e) {
			logger.error("订单查询失败", e);
			return null;
		}
		
		return orderList;
	}
	
	/**
	 * 订单信息更新(订单更新/消除订单)
	 * 
	 * @param xmlFragment
	 * @return,String jgdataDigest, String jglogisticsInterface, String
	 *                dataDigest, String jinGangOrder)
	 * @throws Exception
	 * @author liuchunyan(2013/06/27)
	 */
	private String orderUpdateAndCancel(String xmlFragment, String clientID,
			String jgdataDigest) throws Exception {
		logger.error("===============================订单更新/订单取消");

		// 获取订单信息数据
		List<UpdateWaybillInfo> updateInfoList = new UpdateWaybillInfo()
				.toObjectTaoBao(xmlFragment);

		if (updateInfoList != null && updateInfoList.size() > 0) {
//			return orderService.updOrderMailNoByLogisticIdAndClientId(
//					updateInfoList, JIN_GANG_ORDER, clientID);
			return "";
		} else {

			// 非法的通知内容
			return Response.DATA_INSECURITY_RESPONSE_5.toXmlFragment();
		}
	}

	private String cod(String xmlFragment, String jglogisticsInterface,
			String jgdataDigest, String msgType) throws Exception {
		UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
				.toObject(xmlFragment);

		// 是否要判断次单在我们的order表中是否存在！
		logger.debug("txLogisticid: " + updateInfo.getTxLogisticId());
		logger.debug("payTime: " + updateInfo.getPayTime());
		logger.debug("payAmount: " + updateInfo.getPayAmount());
		logger.debug("unitId: " + updateInfo.getUnitId());
		logger.debug("employeeId: " + updateInfo.getEmployeeId());
		logger.debug("remark: " + updateInfo.getRemark());

		// --> 加入SendTask.
		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId(updateInfo.getClientId());
		sendTask.setTaskFlagId(getflagid(updateInfo.getTxLogisticId()));
		sendTask.setTaskFlag(getflag(updateInfo.getTxLogisticId()));
		sendTask.setTxLogisticId(updateInfo.getTxLogisticId());
		sendTask.setRequestURL(Resource.getChannel(JIN_GANG_ORDER));
		sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
				+ jglogisticsInterface + "&" + DATA_DIGEST_PARAM + "="
				+ jgdataDigest + "&" + MSG_TYPE + "=" + msgType);

		return orderCODService.addCOD(updateInfo, sendTask);
	}

	/**
	 * 验证参数值.
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return
	 */
	private static boolean validateData(String logisticsInterface,
			String dataDigest) {
		String string0 = logisticsInterface
				+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO();
		String string1 = Md5Encryption.MD5Encode(string0);
		logger.debug(string1 + "    "
				+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO());
		logger.debug(dataDigest);
		if (dataDigest.equals(string1)) {
			return true;
		}
		return false;

	}

	/**
	 * 验证参数值.
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return
	 */
	private static boolean validateDataGBK(String logisticsInterface,
			String dataDigest) {
		String string0 = logisticsInterface
				+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO();
		String string1 = Md5Encryption.MD5EncodeGBK(string0);
		if (dataDigest.equals(string1)) {
			return true;
		}
		return false;

	}

	/**
	 * 使用指定的字符集charset对字符串arg进行解码,将解码后的字符串返回.
	 * 
	 * @param arg
	 *            待解码字符串.
	 * @param charset
	 * @return
	 */
	private static String decode(String arg, String charset) {

		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}

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
	 * 调用金刚接口.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String kingGang2TaoBao() throws Exception {
		logger.info("易通接收数据 into start");
		long t1 = System.currentTimeMillis();
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			super.print(GET_METHOD_RESPONSE);
			return null;
		}
		String classpath = Thread.currentThread().getContextClassLoader()
		.getResource("/").toString();
		
		//String classpath =  this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		classpath = classpath.replace("file:", "");
		logger.error("classpath:" + classpath);

		logger.debug("kinggang2taobao" + super.request.getRemoteAddr() + ","
				+ super.request.getRemoteHost() + "---------------"
				+ System.getProperty("file.encoding") + "---------------"
				+ super.request.getCharacterEncoding() + "---------------");

		// 1.获取参数值.
		String logisticsInterface = super.request
				.getParameter(LOGISTICS_INTERFACE_PARAM);
		String dataDigest = super.request.getParameter(DATA_DIGEST_PARAM);
		String clientId = DATA_YTO;
		String provider = super.request
				.getParameter(DATA_LOGISTIC_PROVIDER_PARAM);
		String msgType = super.request.getParameter(MSG_TYPE);
		String url = Resource.getChannel(DATA_TAOBAO);
		
		logger.error("易通接收数据logisticsInterface===" +"【"+ logisticsInterface+"】"+"dataDigest===" + "【"+dataDigest+"】"+"clientId===" + "【"+clientId
				+"】"+"provider===" + "【"+provider+"】"+"msgType===" + "【"+msgType+"】"+"url===" + "【"+url+"】");
		
		if (logisticsInterface == null || dataDigest == null
				|| provider == null || clientId == null || msgType == null
				|| url == null) {
			
			print(Response.DATA_INSECURITY_RESPONSE_5.toXmlFragment());
			return null;
		}

		// 非法的物流公司
		if (!StringUtils.equals(provider, DATA_YTO)) {
			print(Response.DATA_INSECURITY_RESPONSE_3.toXmlFragment());
			return null;
		}

		// 判断接口是否是（test.jsp）测试使用
		if (request.getParameter("test") != null) {
			logisticsInterface = decode(logisticsInterface,
					XmlSender.UTF8_CHARSET);
			dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
		}

		// liug编码---发送给淘宝
		String dslogisticsInterface = null;
		String dsdataDigest = null;
		dslogisticsInterface = encode(logisticsInterface,
				XmlSender.UTF8_CHARSET);
		dsdataDigest = encode(dataDigest, XmlSender.UTF8_CHARSET);

		logger.error("logisticsInterface===" + logisticsInterface);
		logger.debug("dataDigest===" + dataDigest);

		// 金刚安全验证
		if (!validateData(logisticsInterface, dataDigest)) {
			print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
			return null;

		}

		// 处理参数
		String xmlFragment = logisticsInterface;

		/*
		 * 1.4.1接口升级 这块淘宝那边不会发消息 // 服务申请 if
		 * (xmlFragment.contains("<ServiceApply>") &&
		 * xmlFragment.contains("</ServiceApply>")) {
		 * 
		 * if (!ProcessorUtils.validateXML(logisticsInterface, classpath +
		 * SERVICE_APPLY)) {
		 * print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
		 * logger.error("xml验证失败:" + logisticsInterface + ",classpath:" +
		 * classpath); return null; } // 金刚消息对象化 ApplyKdbz applyKdbz = new
		 * ApplyKdbz().toObject(xmlFragment); Customer customer =
		 * applyKdbz.getCustomer();
		 * 
		 * // 对象数据验证 if (StringUtils.isEmpty(customer.getMobile()) &&
		 * StringUtils.isEmpty(customer.getPhone())) {
		 * print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment1()); return
		 * null; } return applyKdbzMeMethod(applyKdbz,clientId,
		 * url,dslogisticsInterface,dsdataDigest,msgType); } // 取消服务 if
		 * (xmlFragment.contains("<ServiceCancel>") &&
		 * xmlFragment.contains("</ServiceCancel>")) {
		 * 
		 * if (!ProcessorUtils.validateXML(logisticsInterface, classpath +
		 * SERVICE_CANCEL)) {
		 * print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
		 * logger.error("xml验证失败:" + logisticsInterface + ",classpath:" +
		 * classpath); return null; } // 金刚消息对象化 ApplyKdbz applyKdbz = new
		 * ApplyKdbz().toObject(xmlFragment);
		 * 
		 * return applyKdbzMeMethod(applyKdbz,clientId,
		 * url,dslogisticsInterface,dsdataDigest,msgType); }
		 */
		if (xmlFragment.contains("<AlterInfo>")
				&& xmlFragment.contains("</AlterInfo>")) {
			UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
					.toAlterObject(xmlFragment);

			if (StringUtils.isEmpty(updateInfo.getTxLogisticId())
					|| StringUtils.isEmpty(updateInfo.getInfoType())
					|| StringUtils.isEmpty(updateInfo.getInfoContent())
					|| StringUtils.isEmpty(updateInfo.getLogisticProviderId())
					|| (StringUtils.isEmpty(updateInfo.getMailNo()) && StringUtils
							.isEmpty((updateInfo.getWeight().toString())))) {
				print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
				return null;
			}

			updateInfo.getResponse().setLogisticProviderId(
					updateInfo.getLogisticProviderId());
			updateInfo.getResponse().setSuccess("true");
			updateInfo.getResponse().setTxLogisticId(
					updateInfo.getTxLogisticId());
			updateInfo.setClientId(request.getParameter(CLIENT_ID_PARAM));

			VIPtxLogisticID = updateInfo.getTxLogisticId();

			SendTaskToTB sendTaskToTB = new SendTaskToTB();
			sendTaskToTB.setClientId(clientId);
			sendTaskToTB.setRequestURL(url);
			sendTaskToTB.setOrderId(0);
			sendTaskToTB.setRemark(updateInfo.getResponse().toXmlFragment());
			sendTaskToTB.setTaskFlagId(getflagid(VIPtxLogisticID));
			sendTaskToTB.setTaskFlag(getflag(VIPtxLogisticID));
			sendTaskToTB.setTxLogisticId(VIPtxLogisticID);
			sendTaskToTB.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
					+ dslogisticsInterface + "&" + DATA_LOGISTIC_PROVIDER_PARAM
					+ "=" + DATA_YTO + "&msg_type=" + msgType + "&"
					+ DATA_DIGEST_PARAM + "=" + dsdataDigest);

			try {
				boolean flag = this.orderService.orderAlterStatusNodify(
						updateInfo, sendTaskToTB);
					if (flag){
						super.print(updateInfo.getResponse().toXmlFragment());
					}
					else{
						super.print(Response.DATA_INSECURITY_RESPONSE_7.toXmlFragment());
					}
				} catch (Exception e) {
				super.print(Response.DATA_INSECURITY_RESPONSE_7.toXmlFragment());
			}
			logger.debug("响应发送完毕!");
			return null;
		}

		// 订单状态更新
		if (StringUtils.equalsIgnoreCase("UPDATE", msgType)) {
			if (!ProcessorUtils.validateXML(logisticsInterface, classpath
					+ ORDER_CANCEL)) {
				print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
				logger.error("xml验证失败:" + logisticsInterface + ",classpath:"
						+ classpath);
				return null;
			}

			// 报文对象化
			List<UpdateWaybillInfo> updateInfoList = new UpdateWaybillInfo()
					.toObjectTaoBao(xmlFragment);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap = orderService.orderListStatusNodify(updateInfoList);
			String toXmlFragment = (String) resultMap.get("toXmlFragment");

			// 返回跟金刚的文本
			super.print(toXmlFragment);
			logger.error("反馈给金刚的xml:" + toXmlFragment);
			List<UpdateWaybillInfo> updateInfoListTemp = (List<UpdateWaybillInfo>) resultMap
					.get("updateInfoList");

			if (updateInfoListTemp != null && updateInfoListTemp.size() > 0) {

				// 揽收，派送扫描，签收 给买家发短信
				for (UpdateWaybillInfo bean : updateInfoListTemp) {
					if (bean.getFieldName().equals("exception")
							|| bean.getFieldName().equals("suspect")
							|| (bean.getFieldName().equals("status") && !("SIGNED"
									.equals(bean.getFieldValue())
									|| "SENT_SCAN".equals(bean.getFieldValue()) || "GOT"
									.equals(bean.getFieldValue())))) {

					} else {
						orderUpdateSendMsgMethod(bean);
					}
				}
			}
			logger.debug("核心数据更新完毕!");
			logger.error("整个方法执行的时间：" + (System.currentTimeMillis() - t1));
		}

		return null;
	}

	/**
	 * 订单创建.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String orderCreateMethod() throws Exception {
		String xmlFragment = request.getParameter(super
				.getInitParameter(REQUEST_KEY_PARAM));
		if (xmlFragment == null) {
			super.print("orderCreateMethod xmlFragment is null value!");
			return null;
		}
		logger.debug("create...." + xmlFragment);

		OrderFormInfo requestOrder = null;
		try {
			requestOrder = new OrderFormInfo().toObject(xmlFragment);
		} catch (Exception spe) {
			print("解析xml出错." + request.getMethod() + "," + spe.getMessage());
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), spe);
			return null;
		}

		requestOrder.getResponse().setTxLogisticId("1234");
		requestOrder.getResponse().setLogisticProviderId("abcccccc");
		requestOrder.getResponse().setSuccess("false");
		requestOrder.getResponse().setReason("ddddd");

		String toXmlFragment = requestOrder.getResponse().toXmlFragment();
		super.print(toXmlFragment);

		return null;
	}

	/**
	 * 订单取消.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String orderCancelMethod() throws Exception {

		String xmlFragment = request.getParameter(super
				.getInitParameter(REQUEST_KEY_PARAM));
		if (xmlFragment == null) {
			super.print("orderCancelMethod xmlFragment value is null!");
			return SUCCESS;
		}

		UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
				.toObject(xmlFragment);

		logger.debug("cancel:" + updateInfo.getInfoContent());

		updateInfo.getResponse().setTxLogisticId("1234");
		updateInfo.getResponse().setLogisticProviderId("abcccccc");
		updateInfo.getResponse().setSuccess("false");
		updateInfo.getResponse().setReason("ddddd");

		String toXmlFragment = updateInfo.getResponse().toXmlFragment();

		super.print(toXmlFragment);
		return null;

	}

	/**
	 * 运单更新.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String dribbleUpdateMethod() throws Exception {
		String xmlFragment = request.getParameter(super
				.getInitParameter(REQUEST_KEY_PARAM));
		if (xmlFragment == null) {
			super.print("dribbleUpdateMethod xmlFragment is null value!");
			return null;
		}

		UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
				.toObject(xmlFragment);
		logger.debug(updateInfo.getInfoContent());

		updateInfo.getResponse().setTxLogisticId("1234");
		updateInfo.getResponse().setLogisticProviderId("abcccccc");
		updateInfo.getResponse().setSuccess("false");
		updateInfo.getResponse().setReason("ddddd");

		String toXmlFragment = updateInfo.getResponse().toXmlFragment();

		super.print(toXmlFragment);

		return null;
	}

	/**
	 * 流转查询.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String turningSearchMethod() throws Exception {

		String xmlFragment = request.getParameter(super
				.getInitParameter(REQUEST_KEY_PARAM));
		if (xmlFragment == null) {
			super.print("turningSearchMethod xmlFragment is null value!");
			return null;
		}

		BatchQueryRequest batchQueryRequest = new BatchQueryRequest()
				.toObject(xmlFragment);
		super.print(batchQueryRequest.getBatchQueryResponse().toXmlFragment());

		return null;
	}

	/**
	 * 状态通知接口.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String InfoFeedbackMethod() throws Exception {

		return SUCCESS;
	}

	/**
	 * 发送消息
	 * 
	 * @param userId
	 *            主账号ID
	 */
	public boolean sendMessag(Integer userId) {
		String messageTheme = "短信不足提醒";
		Integer smsResult = smsPortService.smsLessForRemaind(userId);
		boolean result = false;
		if (smsResult != null) {
			String messageContent = "亲，截止"
					+ DateUtil.getDateStr(new Date(), "yyyy年MM月dd日HH时 ")
					+ "，你的易通可发短信数不足"
					+ smsResult
					+ "条，为不影响你的使用，请及时购买短信。去这<a href='smsServiceMarket_inBuyPorts.action?menuFlag=sms_package'>[购买短信]</a>";
			User curUser = userService.getUserById(userId);
			if ("1".equals(curUser.getUserType())) {
				User adminUser = userService.getUserByUserName("admin");
				List<String> receList = new ArrayList<String>();
				if (curUser.getUserCode() != null
						&& curUser.getUserCode() != "") {
					receList.add(curUser.getUserCode());
					result = messageService.sendMessage(adminUser,
							messageTheme, messageContent, receList, 1);
				}
			}
		}
		return result;
	}

	/**
	 * 金刚接口 --> 更新订单状态给买家发短信
	 * 
	 * @param updateInfo
	 * @throws Exception
	 * @author liuchunyan
	 */
	public String orderUpdateSendMsgMethod(UpdateWaybillInfo updateInfo)
			throws Exception {

		// 揽收，派送扫描，签收 可能需要给买家发短信哦
		if (StringUtils.equalsIgnoreCase(StatusEnum.GOT.getName(),
				updateInfo.getInfoContent())
				|| StringUtils.equalsIgnoreCase(StatusEnum.SENT_SCAN.getName(),
						updateInfo.getInfoContent())
				|| StringUtils.equalsIgnoreCase(StatusEnum.SIGNED.getName(),
						updateInfo.getInfoContent())) {

			String status = updateInfo.getInfoContent(); // 状态
			String mailNo = updateInfo.getMailNo(); // 运单号
			String txLogisticId = updateInfo.getTxLogisticId();
			if (StringUtils.isEmpty(status) || StringUtils.isEmpty(mailNo)
					|| StringUtils.isEmpty(txLogisticId)) {
				logger.error("订单状态、物流号、运单号不能为空！");
				return null;
			}

			// 通过物流号，获取订单
			Order order = orderService.getOrderByLogisticId(txLogisticId);
			if (order == null || order.getId() == null) {
				return null;
			}

			// 通过订单的customer_id 获取订单对应的用户
			User user = Resource.getUserByCustomerId(order.getCustomerId());
			if (user == null || user.getId() == null) {
				return null;
			}
			// TODO 用户必须购买时效提醒
			String childType = user.getChildType();
			Map<String, Object> map = new HashMap<String, Object>();

			if (StringUtils.equalsIgnoreCase("B", childType)) {

				// 若：B:分仓C:企业的平台ID为NULL,则使用自身ID,注意：此情况实际属于,错误数据
				if (StringUtils.isNotBlank(user.getUserSource())) {
					map.put("userId", Integer.parseInt(user.getUserSource()));
				} else {
					map.put("userId", user.getId());
				}
			} else {
				Integer parentId = user.getParentId();

				// 主ID,或子ID
				if (parentId != null) {
					map.put("userId", parentId);
				} else {
					map.put("userId", user.getId());
				}
			}

			map.put("status", StringUtils.upperCase(status));
			map.put("mailNo", mailNo);
			map.put("txLogisticId", txLogisticId);
			map.put("createUserId", user.getId());

			// 根据orderId获取收件人信息
			TraderInfo traderInfo = traderInfoService
					.getTraderInfoByIdAndByTraderType(order.getId(), 1);

			// 收件人手机号
			String buyMobile = traderInfo.getMobile();

			// 收件人姓名
			String buyName = traderInfo.getName();
			logger.info(" smsInfoService.saveInfoToQueue(map);"
					+ map.toString());
			if (!StringUtils.isEmpty(buyMobile)) {
				map.put("buyMobile", buyMobile);
				map.put("buyName", buyName);
				logger.info(" START  --->   smsInfoService.saveInfoToQueue(map);"
						+ map.toString());

				// 往短信表中和短信队列表中插入记录
				smsInfoService.saveInfoToQueue(map);
			}
		}
		
		return null;
	}
	
	/**
	 * 1.4.1接口不使用此功能
	 * @param applyKdbz
	 * @param clientId
	 * @param url
	 * @param dslogisticsInterface
	 * @param dsdataDigest
	 * @param msgType
	 * @return
	 * @throws Exception
	 */
	public String applyKdbzMeMethod(ApplyKdbz applyKdbz, String clientId, String url,String dslogisticsInterface,String dsdataDigest,String msgType)throws Exception{

		applyKdbz.getResponse().setLogisticProviderId(
				applyKdbz.getLogisticProvider());
		applyKdbz.getResponse().setRequestNo(applyKdbz.getRequestNo());
		applyKdbz.getResponse().setSuccess("true");

		// sendTaskToTB实体类
		SendTaskToTB sendTaskToTB = new SendTaskToTB();
		sendTaskToTB.setClientId(clientId);
		sendTaskToTB.setRequestURL(url);
		sendTaskToTB.setOrderId(0);
		sendTaskToTB.setRemark(applyKdbz.getResponse().toXmlFragment1());
		sendTaskToTB.setTaskFlagId(getflagid(VIPtxLogisticID));
		sendTaskToTB.setTaskFlag(getflag(VIPtxLogisticID));
		sendTaskToTB.setTxLogisticId(VIPtxLogisticID);
		sendTaskToTB.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
				+ dslogisticsInterface + "&" + DATA_LOGISTIC_PROVIDER_PARAM + "="
				+ DATA_YTO + "&" + DATA_DIGEST_PARAM + "="
				+ dsdataDigest );
		
		applyKdbz.getResponse().setLogisticProviderId(applyKdbz.getLogisticProvider());
		applyKdbz.getResponse().setRequestNo(applyKdbz.getRequestNo());
		try {

			// add sendTaskToTB
			boolean flag = this.applyKdbzService.applyStatusNodify(
					applyKdbz, sendTaskToTB);
			
			if (flag) {
				
				// 成功报文
				applyKdbz.getResponse().setSuccess("true");
				super.print(applyKdbz.getResponse().toXmlFragment1());
			} else {
				
				// 失败的报文
				applyKdbz.getResponse().setSuccess("false");
				applyKdbz.getResponse().setReason("S07");
				super.print(applyKdbz.getResponse().toXmlFragment1());
			}
		} catch (Exception e) {
			
			// 失败的报文
			applyKdbz.getResponse().setSuccess("false");
			applyKdbz.getResponse().setReason("S07");
			super.print(applyKdbz.getResponse().toXmlFragment1());
		}
		logger.debug("响应发送完毕!");
		return null;
	}
}
