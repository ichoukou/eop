package net.ytoec.kernel.action.order;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.OrderTemp;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.service.MQService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.OrderTempService;
import net.ytoec.kernel.service.ProductService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.FileUtil;

//import org.apache.commons.id.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class OrderImportAction extends AbstractActionSupport {

	private Logger logger = LoggerFactory.getLogger(OrderImportAction.class);

	private static final long serialVersionUID = -8785035380345490527L;

	/**
	 * 模板类型的定义
	 * */
	private static final String MAIJIA_TAOBAO = "淘宝模板2.0";
	private static final String MAIJIA_PAIPAI = "拍拍模板2.0";
	private static final String MAIJIA_ALIBABA = "阿里巴巴模板2.0";
	private static final String MAIJIA_DEFAULT = "默认模板2.0";
	private static final String MAIJIA_DEFAULT2 = "默认模板二1.0";
	private static final String MAIJIA_JINGDONG = "京东模板1.0";

	private static final String WANGDIAN_TAOBAO = "淘宝模板2.0";
	private static final String WANGDIAN_PAIPAI = "拍拍模板2.0";
	private static final String WANGDIAN_ALIBABA = "阿里巴巴模板2.0";
	private static final String WANGDIAN_DEFAULT = "默认模板1.0";
	private static final String WANGDIAN_DEFAULT2 = "默认模板二1.0";
	private static final String WANGDIAN_JINGDONG = "京东模板1.0";

	private static final String WANGDIAN_TAOBAO_NEW = "淘宝模板3.0";
	private static final String WANGDIAN_PAIPAI_NEW = "拍拍模板3.0";
	private static final String WANGDIAN_ALIBABA_NEW = "阿里巴巴模板3.0";
	private static final String WANGDIAN_DEFAULT_NEW = "默认模板2.0";
	private static final String WANGDIAN_DEFAULT2_NEW = "默认模板二2.0";
	private static final String WANGDIAN_JINGDONG_NEW = "京东模板2.0";

	// 匹配首位为数字或字母与9位数字自由组合
	private static final String REGEX_STRING_A = "^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$";
	//匹配民航单号前三位为CAE与9位数字自由组合
	private static final String AVIATION_STRING_A="[C][A][E][0-9]{9}$";
	// 匹配地址字符串,省份和具体地址中间用空格分开
	private static final String REGEX_STRING_B = "[\u4e00-\u9fa5]";

	// 文件打包下载部分
	private String[] downLoadPaths;
	private OutputStream res;
	private ZipOutputStream zos;

	// 文件上传部分
	private File upload;// 上传的文件
	private String uploadContentType; // 文件的内容类型
	private String uploadFileName; // 上传文件名
	private BufferedReader bf;

	// 页面传入的参数
	private String orderIds = "" ;// 勾选的临时订单
	private String orderId;// 删除的临时订单编号

	// 错误消息提醒
	private String uploadErrType;
	private String insertErr;
	private List<String> uploadErrlist;
	private int uploadListCount;

	private String queryMessage;
	
	private Integer orderTempSize;  //当前页面上展示的订单条数
	
	@Inject
	private OrderTempService<OrderTemp> orderTempService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private TraderInfoService<TraderInfo> traderInfoService;
	@Inject
	private ProductService<Product> productService;
	@Inject
	private UserCustomService<UserCustom> userCustomService;
	@Inject
	public MQService mqService;

	private OrderTemp orderTemp;

	private List<OrderTemp> orderTempList;// 存放当前用户临时的订单数据

	private List<OrderTemp> uploadOrderTemps;// 当前上传的临时的订单表

	private String tempkey; // 数据上传时的标示符，取用户的ID

	private String mailNoOrOrderNo; // 根据运单号或者订单号查询
	// 分页的参数
	private Integer currentPage = 1;
	private Integer numOfPage = 50;
	private Integer pageNm = 50;
	private Pagination<OrderTemp> pagination; // 分页对象
	private User user = null;

	private Integer currentUserId;

	// 导入时候的参数
	private String isLocal;// 是否本地发货
	private String pro;// 发货省份
	private String city;// 发货市
	private String area;// 发货区域

	// 导入的成功和失败条目
	private int successCount = 0;
	private int failedCount = 0;

	// 网点登录判断弹层是否弹出
	private String notice;// 0为弹层显示，1为弹层不显示
	private String flag;// 判断页面是否从菜单进入
	

	/**
	 * 根据运单号查找订单入口
	 * */
	public String searchOrderTemp() {
		logger.error(mailNoOrOrderNo);
		if (StringUtils.equals("运单号/订单号", mailNoOrOrderNo)) {
			mailNoOrOrderNo = null;
		}
		user = super.readCookieUser();
		currentUserId = user.getId();
		pagination = new Pagination(currentPage, pageNm);
		this.numOfPage = pageNm;
		orderTempList = orderTempService.getOrderTempListByRemarkPage(
				String.valueOf(currentUserId), pagination, mailNoOrOrderNo);
		orderTempSize = orderTempList.size();
		int total = orderTempService.countOrderTempListByMap(currentUserId
				.toString());
		pagination.setTotalRecords(total);
		if (orderTempList == null || orderTempList.isEmpty()) {
			queryMessage = "have";
		}
		return "toOrderImoprt";
	}

	/**
	 * 订单导入指南文件下载
	 * */
	public String downLoad() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			File file = new File(FileUtil.getClassRoot()
					+ "/template/订单导入指南.doc");
			if (!file.exists()) {
				logger.error("文件不存在!" + file);
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = 0;
			String fname = file.getName();

			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
			} else {
				fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
			}
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/msword");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ fname);
			OutputStream out = response.getOutputStream();
			while ((len = bis.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bis.close();
			out.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "toOrderImoprt";
	}

	public String index() {
		init();
		user = super.readCookieUser();
		currentUserId = user.getId();
		pagination = new Pagination(currentPage, numOfPage);
		orderTempList = orderTempService.getOrderTempListByRemarkPage(
				String.valueOf(currentUserId), pagination, null);
		orderTempSize = orderTempList.size();
		int total = orderTempService.countOrderTempListByMap(currentUserId
				.toString());
		pagination.setTotalRecords(total);
		return "toOrderImoprt";
	}

	/**
	 * 跳转到订单导入页面入口
	 * */
	public String toOrderImoprt() {
		init();
		user = super.readCookieUser();
		currentUserId = user.getId();
		pagination = new Pagination(currentPage, numOfPage);
		orderTempList = orderTempService.getOrderTempListByRemarkPage(
				String.valueOf(currentUserId), pagination, null);
		logger.error("orderTempList.size()="+orderTempList.size());
		
//		//当前页面所展示的所有订单的数量
//		orderTempSize = orderTempList.size();
//		for(int i = 0; i < orderTempList.size(); i++){
//			orderTemp = orderTempList.get(i);
//			orderIds += orderTemp.getId() + ",";
//		}
		orderTempSize = orderTempList.size();
		
		int total = orderTempService.countOrderTempListByMap(currentUserId
				.toString());
		pagination.setTotalRecords(total);
		return "toOrderImoprt";
	}

	/**
	 * 清理数据库，清除掉超过一个月订单
	 * */
	public void init() {
		user = super.readCookieUser();
		currentUserId = user.getId();

		flag = (String) request.getParameter("flag");

		UserCustom userCustom = new UserCustom();
		userCustom.setUserName(user.getUserName());
		userCustom.setBindedUserName(user.getUserName());
		userCustom.setType("9");

		List<UserCustom> customlist = userCustomService
				.searchUserCustom(userCustom);
		if (customlist != null && customlist.size() > 0) {
			notice = "1";
		} else {
			notice = "0";
		}
		try {
			List<OrderTemp> list = new ArrayList<OrderTemp>();
			list = orderTempService.getOrderTempListByRemark(String
					.valueOf(currentUserId));
			if (null != list && list.size() > 0) {
				for (OrderTemp orderTemp : list) {
					if (orderTemp.getEndDate().before(new Date())) {
						orderTempService.delOrderTemp(orderTemp);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 文件下载入口
	 * */
	public String downLoadZip() throws Exception {
		if (downLoadPaths.length != 0) {
			logger.debug("downLoadPaths.length="+downLoadPaths.length);
			preProcess();// 进行预处理
		} else {
			return "nofile";// 没有文件可以下载，返回
		}
		writeZip(downLoadPaths);// 处理
		afterProcess();// 后处理关闭流
		// return "downLoadZip";
		// return toOrderImoprt();
		return "toOrderImoprt";
	}

	/**
	 * 根据文件的地址来压缩文件
	 * 
	 * @param downLoadPaths文件地址数组
	 * */
	public void writeZip(String[] downLoadPaths) throws IOException {
		byte[] buf = new byte[4096];
		int len;
		zos.setEncoding("gb2312");
		for (String filename : downLoadPaths) {
			String classpath = FileUtil.getClassRoot();
			logger.error("classpath:" + classpath + filename);
			File file = new File(classpath + filename);
			ZipEntry ze = new ZipEntry(file.getName());// apache的ant.jar的ZipEntry
			zos.putNextEntry(ze);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			while ((len = bis.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.flush();
			zos.closeEntry();
			bis.close();
		}
	}

	/**
	 * 文件下载前的准备，设置文件头等等
	 * */
	public void preProcess() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		res = response.getOutputStream();
		String fname = "易通订单导入模板.zip";
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
		} else {
			fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
		}
		response.reset();// 清空输出流
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fname);// 设定输出文件头
		response.setContentType("application/zip");
		zos = new ZipOutputStream(res);
	}

	/**
	 * 关闭输入输出流，清理垃圾
	 * */
	public void afterProcess() throws IOException {
		zos.close();
		res.close();
	}

	/**
	 * 删除一个临时订单
	 * */
	public String deleteOrderTemp() throws Exception {
		String order = orderId;
		OrderTemp orderTemp = orderTempService.getOrderTempById(Integer
				.parseInt(order));
		try {
			orderTempService.delOrderTemp(orderTemp);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		user = super.readCookieUser();
		currentUserId = user.getId();
		pagination = new Pagination(currentPage, pageNm);
		this.numOfPage = pageNm;
		orderTempList = orderTempService.getOrderTempListByRemarkPage(
				String.valueOf(currentUserId), pagination, null);
		orderTempSize = orderTempList.size();
		int total = orderTempService.countOrderTempListByMap(currentUserId
				.toString());
		pagination.setTotalRecords(total);
		return "toOrderImoprt";
		// return toOrderImoprt();
	}

	/**
	 * 删除一组临时订单
	 * */
	public String deleteOrderTempList() throws Exception {
		if (StringUtils.isNotBlank(orderIds)) {
			String[] orders = orderIds.split(",");
			try {
				for (String id : orders) {
					OrderTemp orderTemp = orderTempService
							.getOrderTempById(Integer.parseInt(id));
					orderTempService.delOrderTemp(orderTemp);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		user = super.readCookieUser();
		currentUserId = user.getId();
		pagination = new Pagination(currentPage, pageNm);
		this.numOfPage = pageNm;
		orderTempList = orderTempService.getOrderTempListByRemarkPage(
				String.valueOf(currentUserId), pagination, null);
		orderTempSize = orderTempList.size();
		int total = orderTempService.countOrderTempListByMap(currentUserId
				.toString());
		pagination.setTotalRecords(total);
		return "toOrderImoprt";
		// return toOrderImoprt();
	}

	/**
	 * 网点登录弹出层显示
	 * */
	public String showNotice() {
		User currentUser = super.readCookieUser();
		String usertype = currentUser.getUserType();

		UserCustom userCustom = new UserCustom();
		userCustom.setUserName(currentUser.getUserName());
		userCustom.setBindedUserName(currentUser.getUserName());
		userCustom.setType("9");
		userCustomService.add(userCustom);

		return "showNotice";
	}

	/**
	 * 临时表数据导入正式表入口
	 * */
	public String insertOrder() {
		User currentUser = super.readCookieUser();
		currentUserId = currentUser.getId();
		String sendProv;
		String sendCity;
		String sendDistrict;
		String sendAddress;
		if ("true".equals(isLocal)) {
			sendProv = currentUser.getAddressProvince();
			sendCity = currentUser.getAddressCity();
			sendDistrict = currentUser.getAddressDistrict();
			sendAddress = currentUser.getAddressStreet();
		} else {
			sendProv = pro;
			sendCity = city;
			sendDistrict = area;
			sendAddress = null;
		}
		// if (StringUtils.isBlank(sendProv) || StringUtils.isBlank(sendCity)
		// || StringUtils.isBlank(sendDistrict)) {
		// return toOrderImoprt();
		// }
		logger.error(sendProv + " " + sendCity + " " + sendDistrict + ""
				+ sendAddress);
		try {
			if (null != orderIds && !"".equals(orderIds.trim())) {
				String[] orders = orderIds.split(",");
				List<OrderTemp> ls = new ArrayList<OrderTemp>();
				for (String id : orders) {
					logger.error("id:" + id);
					OrderTemp orderTemp = orderTempService
							.getOrderTempById(Integer.parseInt(id));
					logger.error("orderTemp:" + orderTemp);
					ls.add(orderTemp);
				}
				if (null != ls && ls.size() > 0) {
					insertIntoDb(ls, sendProv, sendCity, sendDistrict,
							sendAddress);
				}
			} else {
				List<OrderTemp> ls = orderTempService
						.getOrderTempListByRemark(String.valueOf(currentUserId));
				if (null != ls && ls.size() > 0) {
					insertIntoDb(ls, sendProv, sendCity, sendDistrict,
							sendAddress);
				}
			}
			putMsgForOrderImport(JsonResponse.INFO_TYPE_MSG,
					StringUtils.isBlank(insertErr), insertErr, "",
					String.valueOf(successCount), String.valueOf(failedCount));
		} catch (Exception e) {
			logger.error("订单导入异常", e);
		}
		return "jsonRes";
	}

	/**
	 * 数据导入的入口 将临时表征的数据导入到order，product，和tradeinfo三张表中
	 * */
	public void insertIntoDb(List<OrderTemp> list, String sendProv,
			String sendCity, String sendDistrict, String sendAddress) {
		if (list == null || list.isEmpty()) {
			return;
		}
		User currentUser = super.readCookieUser();
		String usertype = currentUser.getUserType();
		String started = DateUtil.format(DateUtil.getDateBefore(15),
				"yyyy-MM-dd HH:mm:ss");
		logger.error("startTime:" + started);
		String ended = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		logger.error("endTime:" + ended);
		String customerId = currentUser.getTaobaoEncodeKey();
		// 订单表中插入数据
		if ("2".equals(usertype)) {
			customerId = currentUser.getId().toString();
		} else if ("21".equals(usertype) || "22".equals(usertype)
				|| "23".equals(usertype)) {
			customerId = currentUser.getParentId().toString();
		}
		List<String> mailNoList = orderService.getMailNoByPartitionDate(
				started, ended, customerId);
		logger.error("orderList:" + mailNoList.size());
		int index = 0;
		for (OrderTemp orderTemp : list) {
			logger.error("插入数据：" + orderTemp.toString());
			// 去除掉订单表中已经存在的运单号的项
			if (isHave(orderTemp.getMailNo(), mailNoList)) {
				// uploadErrType = "抱歉，无法导入运单号重复的订单，这些订单将被保留在列表中！";
				index = index + 1;
				continue;
			}
			// 订单
			OrderFormInfo requestOrder = new OrderFormInfo();

			// 订单表中插入数据
			if ("1".equals(usertype) || "11".equals(usertype)
					|| "12".equals(usertype) || "13".equals(usertype)) {
				requestOrder.setCustomerId(customerId);
				requestOrder.setClientId("METONG");
			} else if ("21".equals(usertype) || "22".equals(usertype)
					|| "23".equals(usertype) || "2".equals(usertype)) {
				requestOrder.setCustomerId(customerId);
				requestOrder.setClientId("WETONG");
			}
			// 默认为普通订单
			requestOrder.setOrderType("1");
			requestOrder.setMailNo(orderTemp.getMailNo());
			requestOrder.setTradeNo(orderTemp.getTxLogisticId());
			requestOrder.setLogisticProviderId("YTO");
			requestOrder.setCreateTime(new Date());
			requestOrder.setType("1");// 兼容 默认1
			requestOrder.setVipId(currentUser.getId().toString());
			requestOrder.setTxLogisticId("IMP" + orderTemp.getTxLogisticId());
			requestOrder.setFlag("0");// "0"
			requestOrder.setInsuranceValue(0);
			requestOrder.setLineType(Order.OFFLINE_TYPE);
			requestOrder.setRemark("");
			requestOrder.setServiceType("0");

			TraderInfo sender = new TraderInfo();// 如果是网点则判断发货地址是否为空，为空取网点本身地址，不为空取发货地址
			if ("21".equals(usertype) || "22".equals(usertype)
					|| "23".equals(usertype) || "2".equals(usertype)) {
				if (StringUtils.isNotBlank(orderTemp.getSendAddress())) {
					sender.setProv(sendProv);
					sender.setCity(sendCity);
					sender.setAddress(orderTemp.getSendAddress());
				} else {
					sender.setAddress(sendAddress);
					sender.setCity(sendCity);
					sender.setDistrict(sendDistrict);
					sender.setProv(sendProv);
				}
			} else {
				sender.setAddress(sendAddress);
				sender.setCity(sendCity);
				sender.setDistrict(sendDistrict);
				sender.setProv(sendProv);
			}
			sender.setCreateTime(new Date());
			sender.setTradeType(TraderInfo.SENDE_TYPE);
			sender.setNumProv(Resource.getCodeByName(sendProv));
			sender.setName(orderTemp.getSendName());
			sender.setPhone(orderTemp.getSendPhone());
			sender.setMobile(orderTemp.getSendMobile());
			requestOrder.setSender(sender);

			TraderInfo receiver = new TraderInfo();
			receiver.setAddress(orderTemp.getAddress());
			receiver.setCity(orderTemp.getCity());
			receiver.setDistrict(orderTemp.getDistrict());
			receiver.setProv(orderTemp.getProv());
			receiver.setCreateTime(new Date());
			receiver.setTradeType(TraderInfo.RECEIVE_TYPE);
			receiver.setName(orderTemp.getName());
			receiver.setNumProv(Resource.getCodeByName(orderTemp.getProv()));
			receiver.setMobile(orderTemp.getMobile());
			receiver.setPhone(orderTemp.getPhone());
			receiver.setPostCode(orderTemp.getPostCode());
			requestOrder.setReceiver(receiver);

			Product product = new Product();
			product.setItemName(orderTemp.getGoodsName());
			product.setCreateTime(new Date());
			if (StringUtils.isBlank(orderTemp.getQuantity())) {
				product.setItemNumber(0);
			} else {
				product.setItemNumber(Integer.parseInt(orderTemp.getQuantity()));
			}
			if (StringUtils.isBlank(orderTemp.getSinglePrice())) {
				product.setItemValue(0);
			} else {
				product.setItemValue(Double.valueOf(orderTemp.getSinglePrice()));
			}
			product.setLogisticId(orderTemp.getTxLogisticId());
			product.setMailNo(orderTemp.getMailNo());
			List<Product> items = new ArrayList<Product>();
			items.add(product);
			requestOrder.setItems(items);

			boolean flag = true;
			boolean flag2 = true;
			try {
				flag = orderTempService.addOrderFormInfo(requestOrder);
				if(flag){
					String message = getObjectFromJson(requestOrder);
					if (StringUtils.isNotBlank(message)) {
						try {
							if (mqService
									.send(message,
											net.ytoec.kernel.common.Constants.SOLR_ADD_ORDER)) {
								logger.error("订单导入创建同步到MQ成功");
							} else {
								logger.error("订单导入创建同步到MQ失败");
							}

						} catch (Exception e) {
							logger.error("订单导入创建同步到MQ异常", e);
						}
					}
				}
				flag2 = orderTempService.delOrderTemp(orderTemp);
			} catch (Exception e) {
				logger.error(e.getMessage());
				insertErr = "导入失败，请检查！";
			}
		}
		if (index > 0) {
			uploadErrType = "抱歉，无法导入运单号重复的订单，这些订单将被保留在列表中！";
		}
		failedCount = index;
		successCount = list.size() - index;
	}
	//构建json对象
	public  String getObjectFromJson(OrderFormInfo orderUpdateInfo) {
		
		  BuildSearch bean =buildSearch(orderUpdateInfo);
		  if(bean != null){
			  JSONObject jsonObject = JSONObject.fromObject(bean);
				if (jsonObject == null){
					return null;
				}
				return jsonObject.toString();
		  }
		  return null;
	}
	
	/**
	 * 构建BuildSearch
	 */
	@SuppressWarnings("unused")
	private BuildSearch buildSearch(OrderFormInfo orderFormInfo){
		Order order=new Order();
		MailObjectDTO traderinfo = new MailObjectDTO();
		TraderInfo receiver = orderFormInfo.getReceiver();
		// 发件人实体类
		TraderInfo sender = orderFormInfo.getSender();
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
		
		return setBuildSearch(order,traderinfo);
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
				logger.error("orderImport:mailno=" + order.getMailNo());
			} else
				buildSearch.setMailNo("");
		}

		if (order.getId() != null) {
			buildSearch.setOrderId(order.getId());
			logger.error("orderImport:orderid=" + order.getId());
		} else
			buildSearch.setOrderId(0);

		if (order.getStatus() != null) {
			buildSearch.setStatus(order.getStatus());
			logger.error("orderImport:status=" + order.getStatus());
		} else
			buildSearch.setStatus("");
		
		if (order.getCustomerId() != null) {
			buildSearch.setCustomerId(order.getCustomerId());
			logger.error("orderImport:CustomerId=" + order.getCustomerId());
		} else
			buildSearch.setCustomerId("");

		if (order.getOrderType() != null) {
			buildSearch.setOrderType(order.getOrderType());
			logger.error("orderImport:OrderType=" + order.getOrderType());
		} else
			buildSearch.setOrderType(0);
		
		if (order.getTxLogisticId() != null) {
			buildSearch.setTxLogisticId(order.getTxLogisticId());
			logger.error("orderImport:getTxLogisticId=" + order.getTxLogisticId());
		} else
			buildSearch.setTxLogisticId("");
		if (order.getTrimFreight() != null) {
			buildSearch.setTrimFreight(order.getTrimFreight());
			logger.error("orderImport:getTrimFreight=" + order.getTrimFreight());
		} else
			buildSearch.setTrimFreight(0);

		if (order.getFreightType() != null) {
			buildSearch.setFreightType(order.getFreightType());
			logger.error("orderImport:getFreightType=" + order.getFreightType());
		} else
			buildSearch.setFreightType("");

		if (order.getLineType() != null) {
			buildSearch.setLineType(order.getLineType());
			logger.error("orderImport:getLineType=" + order.getLineType());
		} else
			buildSearch.setLineType("");

		if (order.getType() != null) {
			buildSearch.setType(order.getType());
			logger.error("orderImport:getType=" + order.getType());
		} else
			buildSearch.setType("");

		if (order.getWeight() != null) {
			buildSearch.setWeight(order.getWeight());
			logger.error("orderImport:getWeight=" + order.getWeight());
		} else
			buildSearch.setWeight(0);

		if (order.getMailNo() != null) {
			buildSearch.setOldMailNo(order.getMailNo());
			logger.error("orderImport:getMailNo=" + order.getMailNo());
		} else
			buildSearch.setOldMailNo("");

		if (order.getType() != null) {
			buildSearch.setHoldTime(order.getType());
			logger.error("orderImport:getType=" + order.getType());
		} else
			buildSearch.setHoldTime("");

		if (order.getPartitionDate() != null) {
			buildSearch.setPartitiondate(order.getPartitionDate());
			logger.error("orderImport:setPartitiondate=" + order.getPartitionDate());
		} else
			buildSearch.setPartitiondate(new Date());
		if (order.getAcceptTime() != null) {
			buildSearch.setAcceptTime(order.getAcceptTime());
			logger.error("orderImport:getAcceptTime=" + order.getAcceptTime());
		} else
			buildSearch.setAcceptTime(new Date());
		
		if(traderinfo==null){
			logger.error("traderinfo===null=="+order.getId()+"=="+DateUtil.format(order.getPartitionDate(), "yyyy-mm-dd"));
			return buildSearch;
		}
		if (traderinfo.getProv() != null) {
			buildSearch.setProv(traderinfo.getProv());
			logger.error("orderImport:prov=" + traderinfo.getProv());
		} else
			buildSearch.setProv("");

		if (traderinfo.getProvF() != null) {
			buildSearch.setProvF(traderinfo.getProvF());
			logger.error("orderImport:provF=" + traderinfo.getProvF());
		} else
			buildSearch.setProvF("");
		
		if (traderinfo.getName() != null) {
			buildSearch.setName(traderinfo.getName());
			logger.error("orderImport:Name=" + traderinfo.getName());
		} else
			buildSearch.setName("");

		if (traderinfo.getPhone() != null&&!"".equals(traderinfo.getPhone())) {
			buildSearch.setPhone(traderinfo.getPhone());
			logger.error("orderImport:Phone=" + traderinfo.getPhone());
		} else
			buildSearch.setPhone("0");

		if (traderinfo.getMobile() != null) {
			buildSearch.setMobile(traderinfo.getMobile());
			logger.error("orderImport:getMobile=" + traderinfo.getMobile());
		} else
			buildSearch.setMobile("");

		if (traderinfo.getNumProvF() !=null) {
			buildSearch.setNumProvF(traderinfo.getNumProvF());
			logger.error("orderImport:setNumProvF=" + traderinfo.getNumProvF());
		} else
			buildSearch.setNumProvF(0);

		if (traderinfo.getNumProv() > 0) {
			buildSearch.setNumProv(traderinfo.getNumProv());
			logger.error("orderImport:setNumProv=" + traderinfo.getNumProv());
		} else
			buildSearch.setNumProv(0);

		if (traderinfo.getCity() != null) {
			buildSearch.setCity(traderinfo.getCity());
			logger.error("orderImport:setCity=" + traderinfo.getCity());
		} else
			buildSearch.setCity("");

		if (traderinfo.getCityF() != null) {
			buildSearch.setCityF(traderinfo.getCityF());
			logger.error("orderImport:setCityF=" + traderinfo.getCityF());
		} else
			buildSearch.setCityF("");

		if (traderinfo.getAddress() != null) {
			buildSearch.setAddress(traderinfo.getAddress());
			logger.error("orderImport:getAddress=" + traderinfo.getAddress());
		} else
			buildSearch.setAddress("");
		

		if (traderinfo.getDistrict() != null) {
			buildSearch.setDistrict(traderinfo.getDistrict());
			logger.error("orderImport:getDistrict=" + traderinfo.getDistrict());
		} else
			buildSearch.setDistrict("");
		
		
		logger.error("orderImport:buildSearch date end");
		return buildSearch;
	}
	/**
	 * 判断字符串在列表中是否已经存在
	 * */
	public boolean isHave(String mailNo, List<String> list) {
		if (list == null || list.isEmpty()) {
			return false;
		}
		if (list.contains(mailNo)) {
			return true;
		}
		return false;
	}

	/**
	 * 订单上传的入口
	 * */
	public String upload() throws Exception {
		User currentUser = super.readCookieUser();
		String userType = currentUser.getUserType();
		List<String[]> list = importCsv(upload.getPath());
		logger.error(upload.getPath());
		logger.error("List size" + (list.size() - 2));
		uploadOrderTemps = new ArrayList<OrderTemp>();
		if (list.size() <= 2) {
			uploadErrType = "上传的表格不能为空!";
			return toOrderImoprt();
		}
		if (list.size() > 1002) {
			uploadErrType = "上传超过1000行了，请少一点哦";
			return toOrderImoprt();
		}
		if ("1".equals(userType) || "11".equals(userType)
				|| "12".equals(userType) || "13".equals(userType)) {
			if (MAIJIA_TAOBAO.equals(list.get(0)[0])) {
				uploadOrderTemps = m_taobaoToOrderTemp(list);
			} else if (MAIJIA_PAIPAI.equals(list.get(0)[0])) {
				uploadOrderTemps = m_paipaiToOrderTemp(list);
			} else if (MAIJIA_ALIBABA.equals(list.get(0)[0])) {
				uploadOrderTemps = m_alibabaToOrderTemp(list);
			} else if (MAIJIA_JINGDONG.equals(list.get(0)[0])) {
				uploadOrderTemps = m_jingdongToOrderTemp(list);
			} else if (MAIJIA_DEFAULT.equals(list.get(0)[0])) {
				uploadOrderTemps = m_defaultToOrderTemp(list);
			} else if (MAIJIA_DEFAULT2.equals(list.get(0)[0])) {
				uploadOrderTemps = m_default2ToOrderTemp(list);
			} else {
				uploadErrType = "请确认您的表格第一行模板名称和版本号无误后再上传！";
				return toOrderImoprt();
			}
		}
		if ("21".equals(userType) || "22".equals(userType)
				|| "23".equals(userType) || "2".equals(userType)) {
			if (WANGDIAN_TAOBAO.equals(list.get(0)[0])) {
				uploadOrderTemps = w_taobaoToOrderTemp(list);
			} else if (WANGDIAN_PAIPAI.equals(list.get(0)[0])) {
				uploadOrderTemps = w_paipaiToOrderTemp(list);
			} else if (WANGDIAN_ALIBABA.equals(list.get(0)[0])) {
				uploadOrderTemps = w_alibabaToOrderTemp(list);
			} else if (WANGDIAN_DEFAULT.equals(list.get(0)[0])) {
				uploadOrderTemps = w_defaultToOrderTemp(list);
			} else if (WANGDIAN_JINGDONG.equals(list.get(0)[0])) {
				uploadOrderTemps = w_jingdongToOrderTemp(list);
			} else if (WANGDIAN_DEFAULT2.equals(list.get(0)[0])) {
				uploadOrderTemps = w_default2ToOrderTemp(list);
			} else if (WANGDIAN_TAOBAO_NEW.equals(list.get(0)[0])) {
				uploadOrderTemps = w_taobaoNewToOrderTemp(list);
			} else if (WANGDIAN_PAIPAI_NEW.equals(list.get(0)[0])) {
				uploadOrderTemps = w_paipaiNewToOrderTemp(list);
			} else if (WANGDIAN_ALIBABA_NEW.equals(list.get(0)[0])) {
				uploadOrderTemps = w_alibabaNewToOrderTemp(list);
			} else if (WANGDIAN_DEFAULT_NEW.equals(list.get(0)[0])) {
				uploadOrderTemps = w_defaultNewToOrderTemp(list);
			} else if (WANGDIAN_JINGDONG_NEW.equals(list.get(0)[0])) {
				uploadOrderTemps = w_jingdongNewToOrderTemp(list);
			} else if (WANGDIAN_DEFAULT2_NEW.equals(list.get(0)[0])) {
				uploadOrderTemps = w_default2NewToOrderTemp(list);
			} else {
				uploadErrType = "请确认您的表格第一行模板名称和版本号无误后再上传！";
				return toOrderImoprt();
			}
		}
		if (null != uploadOrderTemps && uploadOrderTemps.size() > 0) {
			boolean flag = true;
			try {
				// 对省份城市的长度进行限制，金刚的长度为varchar2(100)，则限制汉字长度为33位
				for(OrderTemp temp:uploadOrderTemps){
					if((temp.getProv() != null && temp.getProv().length() > 33 )||
							temp.getCity() != null && temp.getCity().length() > 33){
						uploadErrType = "订单的省份城市长度最多不能超过33个字符(包括空格)，请检查后在提交，谢谢!";
						return toOrderImoprt();
					}
					//直接生成随机数，写入ordertmp表
					Random r=new Random();
					temp.setTxLogisticId(UUID.randomUUID().toString().replaceAll("-", "")+r.nextInt(1000));
//					temp.setTxLogisticId(new String(Hex.encodeHex(UUID.randomUUID().getRawBytes())));
				}
				flag = orderTempService.addOrderTempList(uploadOrderTemps);
				uploadListCount = uploadOrderTemps.size();
				uploadErrType = "上传成功，共上传" + uploadOrderTemps.size() + "条记录!";
				return toOrderImoprt();
			} catch (Exception e) {
				logger.error(e.getMessage());
				uploadErrType = "上传失败，请按照模板填写表格，检查后重新上传！";
				return toOrderImoprt();
			}
		} else {
			uploadErrType = "上传失败，请按照模板填写表格，检查后重新上传！";
			return toOrderImoprt();
		}
	}
	public static void main(String agrs[]){
		Random r=new Random();
		for(int i=0;i<100;i++){
			System.out.println(r.nextInt(1000));
		}
	}
	/**
	 * csv文件解析成List
	 * 
	 * @param String
	 * 
	 * @return csv文件数据（List<String[]>）
	 * 
	 * @throws FileNotFoundException
	 * 
	 * @throws IOException
	 */
	public List<String[]> importCsv(String file) {
		List<String[]> list = new ArrayList<String[]>();
		CsvReader reader = null;
		try {
			// 初始化CsvReader并指定列分隔符和字符编码
			reader = new CsvReader(file, ',', Charset.forName("GBK"));
			while (reader.readRecord()) {
				// 读取每行数据以数组形式返回
				String[] str = reader.getValues();
				if (str != null && str.length > 0) {
					if (!str[0].startsWith("#") && str[0] != null
							&& !"".equals(str[0].trim())) {
						str.toString().replaceAll("\n", "");
						if (isNotNull(str)) {
							list.add(str);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				// 关闭CsvReader
				reader.close();
		}
		return list;
	}

	/**
	 * 判断一个数组的元素是否全为空格 不是 true 是 false
	 * */
	public static boolean isNotNull(String[] ls) {
		for (String string : ls) {
			if (string.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据卖家淘宝模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> m_taobaoToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String totalPrice = list.get(i)[1].trim();
				String name = list.get(i)[2].trim();
				String addstr = list.get(i)[3].trim();
				String phone = list.get(i)[4].trim();
				String mobile = list.get(i)[5].trim();
				String createTime = list.get(i)[6].trim();
				String goodsName = list.get(i)[7].trim();
				String mailNo = list.get(i)[8].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(totalPrice)) {
					sb.append("B;");
				} else {
					orderTemp.setTotalPrice(totalPrice);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("C;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("D;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address.substring(0,
								address.length() - 8));
						orderTemp.setPostCode(address.substring(
								address.length() - 7, address.length() - 1));

					} else {
						sb.append("D;");
					}
				}

				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("F;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						if (phone.startsWith("'")) {
							orderTemp.setPhone(phone.substring(1));
						} else {
							orderTemp.setPhone(phone);
						}
					} else {
						sb.append("E;");
					}
				}

				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						if (mobile.trim().startsWith("'")) {
							orderTemp.setMobile(mobile.substring(1));
						} else {
							orderTemp.setMobile(mobile);
						}
					} else {
						sb.append("F;");
					}
				}

				if (StringUtils.isBlank(createTime)) {
					sb.append("G;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(goodsName)) {
					sb.append("H;");
				} else {
					orderTemp.setGoodsName(goodsName);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("I;");
				} else {
					if (!mailNo.split(":")[1].matches(REGEX_STRING_A)) {
						sb.append("I;");
					} else {
						orderTemp.setMailNo(mailNo.split(":")[1]);
					}
				}

				orderTemp.setSendName(user.getUserNameText());
				orderTemp.setSendMobile(user.getMobilePhone());
				orderTemp.setSendPhone(user.getTelePhone());
				orderTemp.setCreateTime(createTime);
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			System.out.println(e);
		}
		return orderTemps;
	}

	/**
	 * 根据卖家阿里巴巴模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> m_alibabaToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String totalPrice = list.get(i)[1].trim();
				String createTime = list.get(i)[2].trim();
				String name = list.get(i)[3].trim();
				String addstr = list.get(i)[4].trim();
				String phone = list.get(i)[5].trim();
				String mobile = list.get(i)[6].trim();
				String goodsName = list.get(i)[7].trim();
				String mailNo = list.get(i)[8].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(totalPrice)) {
					sb.append("B;");
				} else {
					orderTemp.setTotalPrice(totalPrice);
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("C;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("D;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("E;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address);
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("G;");
				}

				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("F;");
					}
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isBlank(goodsName)) {
					sb.append("H;");
				} else {
					orderTemp.setGoodsName(goodsName);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("I;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("I;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				orderTemp.setSendName(user.getUserNameText());
				orderTemp.setSendMobile(user.getMobilePhone());
				orderTemp.setSendPhone(user.getTelePhone());
				orderTemp.setCreateTime(createTime);
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据卖家拍拍模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> m_paipaiToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String goodsName = list.get(i)[1].trim();
				String totalPrice = list.get(i)[2].trim();
				String name = list.get(i)[3].trim();
				String prov = list.get(i)[4].trim();
				String adddetial = list.get(i)[5].trim();
				String mobile = list.get(i)[6].trim();
				String phone = list.get(i)[7].trim();
				String createTime = list.get(i)[8].trim();
				String mailNo = list.get(i)[9].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(goodsName)) {
					sb.append("B;");
				} else {
					orderTemp.setGoodsName(goodsName);
				}
				if (StringUtils.isBlank(totalPrice)) {
					sb.append("C;");
				} else {
					orderTemp.setTotalPrice(totalPrice);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("D;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(prov)) {
					sb.append("E;");
				} else {
					orderTemp.setProv(prov);
				}
				if (StringUtils.isBlank(adddetial)) {
					sb.append("F;");
				} else {
					String address = adddetial.replaceAll(" ", "");
					orderTemp.setAddress(address);
				}

				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("H;");
				}

				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("H;");
					}
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("I;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("J;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("J;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				orderTemp.setSendName(user.getUserNameText());
				orderTemp.setSendMobile(user.getMobilePhone());
				orderTemp.setSendPhone(user.getTelePhone());
				orderTemp.setCreateTime(createTime);
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据卖家京东模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> m_jingdongToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String adddetial = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String mobile = list.get(i)[2].trim();
				String mailNo = list.get(i)[3].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(adddetial)) {
					sb.append("A;");
				} else {
					int position = getIndex(adddetial);
					String newAddetial = adddetial.substring(position,
							adddetial.length());
					String prov = newAddetial.substring(0, 2);
					String address = newAddetial.substring(2,
							newAddetial.length()).trim();
					orderTemp.setProv(prov);
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}

				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("C;");
					}
				} else {
					sb.append("C;");
				}

				if (StringUtils.isBlank(mailNo)) {
					sb.append("D;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("D;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}

				orderTemp.setSendName(user.getUserNameText());
				orderTemp.setSendMobile(user.getMobilePhone());
				orderTemp.setSendPhone(user.getTelePhone());
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据默认模板(卖家)将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> m_defaultToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String addstr = list.get(i)[2].trim();
				String phone = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A) && !mailNo.matches(AVIATION_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("C;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address);
					} else {
						sb.append("C;");
					}
				}

				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("E;");
				}

				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("D;");
					}
				}

				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("E;");
					}
				}
				orderTemp.setSendName(user.getUserNameText());
				orderTemp.setSendMobile(user.getMobilePhone());
				orderTemp.setSendPhone(user.getTelePhone());
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据默认模板2(卖家)将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> m_default2ToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String prov = list.get(i)[2].trim();
				String city = list.get(i)[3].trim();
				String district = list.get(i)[4].trim();
				String address = list.get(i)[5].trim();
				String phone = list.get(i)[6].trim();
				String mobile = list.get(i)[7].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)&& !mailNo.matches(AVIATION_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(prov)) {
					sb.append("C;");
				} else {
					orderTemp.setProv(prov);
				}
				orderTemp.setCity(city);
				orderTemp.setDistrict(district);
				if (StringUtils.isBlank(address)) {
					sb.append("F;");
				} else {
					orderTemp.setAddress(address);
				}

				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("H;");
				}

				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("H;");
					}
				}
				orderTemp.setSendName(user.getUserNameText());
				orderTemp.setSendMobile(user.getMobilePhone());
				orderTemp.setSendPhone(user.getTelePhone());
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据网点淘宝模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> w_taobaoToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String addstr = list.get(i)[2].trim();
				String phone = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();
				String createTime = list.get(i)[5].trim();
				String mailNo = list.get(i)[6].trim();
				String sendName = list.get(i)[7].trim();
				String sendPhone = list.get(i)[8].trim();
				String sendMobile = list.get(i)[9].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("C;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address.substring(0,
								address.length() - 8));
						orderTemp.setPostCode(address.substring(
								address.length() - 7, address.length() - 1));
					} else {
						sb.append("C;");
					}
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("E;");
				}

				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						if (phone.startsWith("'")) {
							orderTemp.setPhone(phone.substring(1));
						} else {
							orderTemp.setPhone(phone);
						}
					} else {
						sb.append("D;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						if (mobile.trim().startsWith("'")) {
							orderTemp.setMobile(mobile.substring(1));
						} else {
							orderTemp.setMobile(mobile);
						}
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("F;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("G;");
				} else if (!mailNo.split(":")[1].matches(REGEX_STRING_A)) {
					sb.append("G;");
				} else {
					orderTemp.setMailNo(mailNo.split(":")[1]);
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("H;");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("J;");
				}

				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("I;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("J;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			System.out.println(e);
		}
		return orderTemps;
	}

	public List<OrderTemp> w_taobaoNewToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String addstr = list.get(i)[2].trim();
				String phone = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();
				String createTime = list.get(i)[5].trim();
				String mailNo = list.get(i)[6].trim();
				String sendName = list.get(i)[7].trim();
				String sendAddress = list.get(i)[8].trim();
				String sendPhone = list.get(i)[9].trim();
				String sendMobile = list.get(i)[10].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("C;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address.substring(0,
								address.length() - 8));
						orderTemp.setPostCode(address.substring(
								address.length() - 7, address.length() - 1));
					} else {
						sb.append("C;");
					}
				}

				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("E;");
				}

				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						if (phone.startsWith("'")) {
							orderTemp.setPhone(phone.substring(1));
						} else {
							orderTemp.setPhone(phone);
						}
					} else {
						sb.append("D;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						if (mobile.trim().startsWith("'")) {
							orderTemp.setMobile(mobile.substring(1));
						} else {
							orderTemp.setMobile(mobile);
						}
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("F;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("G;");
				} else if (!mailNo.split(":")[1].matches(REGEX_STRING_A)) {
					sb.append("G;");
				} else {
					orderTemp.setMailNo(mailNo.split(":")[1]);
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("H;");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isNotBlank(sendAddress)) {
					orderTemp.setSendAddress(sendAddress);
				} else {
					sb.append("I;");
				}

				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("K;");
				}

				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("J;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("K;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			System.out.println(e);
		}
		return orderTemps;
	}

	/**
	 * 根据网点拍拍模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> w_paipaiToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String prov = list.get(i)[2].trim();
				String adddetial = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();
				String phone = list.get(i)[5].trim();
				String createTime = list.get(i)[6].trim();
				String mailNo = list.get(i)[7].trim();
				String sendName = list.get(i)[8].trim();
				String sendPhone = list.get(i)[9].trim();
				String sendMobile = list.get(i)[10].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(prov)) {
					sb.append("C;");
				} else {
					orderTemp.setProv(prov);
				}
				if (StringUtils.isBlank(adddetial)) {
					sb.append("D;");
				} else {
					String address = adddetial.replaceAll(" ", "");
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("F;");
				}

				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("F;");
					}
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("G;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("H;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("H;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("I;");
				} else {
					orderTemp.setSendName(sendName);
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("K;");
				}

				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("J;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("K;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	public List<OrderTemp> w_paipaiNewToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String prov = list.get(i)[2].trim();
				String adddetial = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();
				String phone = list.get(i)[5].trim();
				String createTime = list.get(i)[6].trim();
				String mailNo = list.get(i)[7].trim();
				String sendName = list.get(i)[8].trim();
				String sendAddress = list.get(i)[9].trim();
				String sendPhone = list.get(i)[10].trim();
				String sendMobile = list.get(i)[11].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(prov)) {
					sb.append("C;");
				} else {
					orderTemp.setProv(prov);
				}
				if (StringUtils.isBlank(adddetial)) {
					sb.append("D;");
				} else {
					String address = adddetial.replaceAll(" ", "");
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(mobile) && StringUtils.isBlank(phone)) {
					sb.append("E;");
				}

				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("F;");
					}
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("G;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(mailNo)) {
					sb.append("H;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("H;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("I;");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isNotBlank(sendAddress)) {
					orderTemp.setSendAddress(sendAddress);
				} else {
					sb.append("J;");
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("L;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("K;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("L;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据网点阿里巴巴模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> w_alibabaToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String createTime = list.get(i)[1].trim();
				String name = list.get(i)[2].trim();
				String addstr = list.get(i)[3].trim();
				String phone = list.get(i)[4].trim();
				String mobile = list.get(i)[5].trim();
				String mailNo = list.get(i)[6].trim();
				String sendName = list.get(i)[7].trim();
				String sendPhone = list.get(i)[8].trim();
				String sendMobile = list.get(i)[9].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("B;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("C;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("D;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address);
					} else {
						sb.append("D;");
					}
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("F;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("F;");
					}
				}

				if (StringUtils.isBlank(mailNo)) {
					sb.append("G;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("G;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("H;");
				} else {
					orderTemp.setSendName(sendName);
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("J;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("I;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("J;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	public List<OrderTemp> w_alibabaNewToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String buyerName = list.get(i)[0].trim();
				String createTime = list.get(i)[1].trim();
				String name = list.get(i)[2].trim();
				String addstr = list.get(i)[3].trim();
				String phone = list.get(i)[4].trim();
				String mobile = list.get(i)[5].trim();
				String mailNo = list.get(i)[6].trim();
				String sendName = list.get(i)[7].trim();
				String sendAddress = list.get(i)[8].trim();
				String sendPhone = list.get(i)[9].trim();
				String sendMobile = list.get(i)[10].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(buyerName)) {
					sb.append("A;");
				} else {
					orderTemp.setBuyerName(buyerName);
				}
				if (StringUtils.isBlank(createTime)) {
					sb.append("B;");
				} else {
					orderTemp.setCreateTime(createTime);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("C;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("D;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address);
					} else {
						sb.append("D;");
					}
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("F;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("E;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("F;");
					}
				}

				if (StringUtils.isBlank(mailNo)) {
					sb.append("G;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("G;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("H;");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isNotBlank(sendAddress)) {
					orderTemp.setSendAddress(sendAddress);
				} else {
					sb.append("I;");
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("K;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("J;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("K;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据网点京东模板将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> w_jingdongToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String adddetial = list.get(i)[1].trim();
				String name = list.get(i)[2].trim();
				String mobile = list.get(i)[3].trim();
				String sendName = list.get(i)[4].trim();
				String sendMobile = list.get(i)[5].trim();
				String sendPhone = list.get(i)[6].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(adddetial)) {
					sb.append("B;");
				} else {
					int position = getIndex(adddetial);
					String newAddetial = adddetial.substring(position,
							adddetial.length()).trim();
					String prov = newAddetial.substring(0, 2);
					String address = newAddetial.substring(2,
							newAddetial.length()).trim();
					orderTemp.setProv(prov);
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("C;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("D;");
					}
				} else {
					sb.append("D;");
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("E;");
				} else {
					orderTemp.setSendName(sendName);
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("F;");
				}

				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("F;");
					}
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("G;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	public List<OrderTemp> w_jingdongNewToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String adddetial = list.get(i)[1].trim();
				String name = list.get(i)[2].trim();
				String mobile = list.get(i)[3].trim();
				String sendName = list.get(i)[4].trim();
				String sendAddress = list.get(i)[5].trim();
				String sendMobile = list.get(i)[6].trim();
				String sendPhone = list.get(i)[7].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(adddetial)) {
					sb.append("B;");
				} else {
					int position = getIndex(adddetial);
					String newAddetial = adddetial.substring(position);
					String prov = newAddetial.substring(0, 2);
					String address = newAddetial.substring(2);
					orderTemp.setProv(prov);
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(name)) {
					sb.append("C;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("D;");
					}
				} else {
					sb.append("D;");
				}
				if (StringUtils.isBlank(sendName)) {
					sb.append("E;");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isNotBlank(sendAddress)) {
					orderTemp.setSendAddress(sendAddress);
				} else {
					sb.append("F;");
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("G;");
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("H;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据默认模板(网点)将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> w_defaultToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String addstr = list.get(i)[2].trim();
				String phone = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();
				String sendName = list.get(i)[5].trim();
				String sendPhone = list.get(i)[6].trim();
				String sendMobile = list.get(i)[7].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A) && !mailNo.matches(AVIATION_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("C;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address);
					} else {
						sb.append("C;");
					}
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("E;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("D;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("E;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				if (StringUtils.isBlank(sendName)) {
					sb.append("F;");
				} else {
					orderTemp.setSendName(sendName);
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("H;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("H;");
					}
				}

				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	public List<OrderTemp> w_defaultNewToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String addstr = list.get(i)[2].trim();
				String phone = list.get(i)[3].trim();
				String mobile = list.get(i)[4].trim();
				String sendName = list.get(i)[5].trim();
				String sendAddress = list.get(i)[6].trim();
				String sendPhone = list.get(i)[7].trim();
				String sendMobile = list.get(i)[8].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A) && !mailNo.matches(AVIATION_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(addstr)) {
					sb.append("C;");
				} else {
					addstr = addstr.replaceAll("　", " ");
					if (addstr.indexOf(" ") != -1) {
						int charNum = addstr.indexOf(" ");
						String prov = addstr.substring(0, charNum);
						orderTemp.setProv(prov);
						String add = addstr.substring(charNum + 1,
								addstr.length()).trim();
						String address = add.replaceAll(" ", "");
						orderTemp.setAddress(address);
					} else {
						sb.append("C;");
					}
				}

				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("E;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("D;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("E;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				if (StringUtils.isBlank(sendName)) {
					sb.append("F;");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isNotBlank(sendAddress)) {
					orderTemp.setSendAddress(sendAddress);
				} else {
					sb.append("G;");
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("I;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("H;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("I;");
					}
				}

				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 根据默认模板2(网点)将数据转换成对象
	 * 
	 * @param List
	 *            <String[]>
	 * 
	 * @return List<OrderTemp>
	 */
	public List<OrderTemp> w_default2ToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String prov = list.get(i)[2].trim();
				String city = list.get(i)[3].trim();
				String district = list.get(i)[4].trim();
				String address = list.get(i)[5].trim();
				String phone = list.get(i)[6].trim();
				String mobile = list.get(i)[7].trim();
				String sendName = list.get(i)[8].trim();
				String sendPhone = list.get(i)[9].trim();
				String sendMobile = list.get(i)[10].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A) && !mailNo.matches(AVIATION_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(prov)) {
					sb.append("C;");
				} else {
					orderTemp.setProv(prov);
				}
				orderTemp.setCity(city);
				orderTemp.setDistrict(district);
				if (StringUtils.isBlank(address)) {
					sb.append("F;");
				} else {
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("H;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("H;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				if (StringUtils.isBlank(sendName)) {
					sb.append("I");
				} else {
					orderTemp.setSendName(sendName);
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("K;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("J;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("K;");
					}
				}
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	public List<OrderTemp> w_default2NewToOrderTemp(List<String[]> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0;// 出错行计数器
		List<OrderTemp> orderTemps = new ArrayList<OrderTemp>();
		try {
			for (int i = 2; i < list.size(); i++) {
				sb.setLength(0);
				String mailNo = list.get(i)[0].trim();
				String name = list.get(i)[1].trim();
				String prov = list.get(i)[2].trim();
				String city = list.get(i)[3].trim();
				String district = list.get(i)[4].trim();
				String address = list.get(i)[5].trim();
				String phone = list.get(i)[6].trim();
				String mobile = list.get(i)[7].trim();
				String sendName = list.get(i)[8].trim();
				String sendAddress = list.get(i)[9].trim();
				String sendPhone = list.get(i)[10].trim();
				String sendMobile = list.get(i)[11].trim();

				OrderTemp orderTemp = new OrderTemp();

				if (StringUtils.isBlank(mailNo)) {
					sb.append("A;");
				} else {
					if (!mailNo.matches(REGEX_STRING_A) && !mailNo.matches(AVIATION_STRING_A)) {
						sb.append("A;");
					} else {
						orderTemp.setMailNo(mailNo);
					}
				}
				if (StringUtils.isBlank(name)) {
					sb.append("B;");
				} else {
					orderTemp.setName(name);
				}
				if (StringUtils.isBlank(prov)) {
					sb.append("C;");
				} else {
					orderTemp.setProv(prov);
				}
				orderTemp.setCity(city);
				orderTemp.setDistrict(district);
				if (StringUtils.isBlank(address)) {
					sb.append("F;");
				} else {
					orderTemp.setAddress(address);
				}
				if (StringUtils.isBlank(phone) && StringUtils.isBlank(mobile)) {
					sb.append("H;");
				}
				if (StringUtils.isNotBlank(phone)) {
					if (!isHaveChina(phone)) {
						orderTemp.setPhone(phone);
					} else {
						sb.append("G;");
					}
				}
				if (StringUtils.isNotBlank(mobile)) {
					if (!isHaveChina(mobile)) {
						orderTemp.setMobile(mobile);
					} else {
						sb.append("H;");
					}
				}
				orderTemp.setCreateTime(DateUtil.getDateStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				if (StringUtils.isBlank(sendName)) {
					sb.append("I");
				} else {
					orderTemp.setSendName(sendName);
				}

				if (StringUtils.isNotBlank(sendAddress)) {
					orderTemp.setSendAddress(sendAddress);
				} else {
					sb.append("J;");
				}
				if (StringUtils.isBlank(sendPhone)
						&& StringUtils.isBlank(sendMobile)) {
					sb.append("L;");
				}
				if (StringUtils.isNotBlank(sendPhone)) {
					if (!isHaveChina(sendPhone)) {
						orderTemp.setSendPhone(sendPhone);
					} else {
						sb.append("K;");
					}
				}
				if (StringUtils.isNotBlank(sendMobile)) {
					if (!isHaveChina(sendMobile)) {
						orderTemp.setSendMobile(sendMobile);
					} else {
						sb.append("L;");
					}
				}
				orderTemp.setRemark(tempkey);
				orderTemp.setEndDate(DateUtil.getDateAfter(30));
				logger.error(orderTemp.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index < 10) {
						continue;
					} else {
						break;
					}
				} else {
					orderTemps.add(orderTemp);
				}
			}
			if (uploadErrlist.size() > 0) {
				orderTemps.clear();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return orderTemps;
	}

	/**
	 * 判断字符串中是否含有中文字符 ture-含有中文字符，false-不含有中文字符
	 * */
	public static boolean isHaveChina(String str) {
		for (int i = 0; i < str.length(); i++) {
			char ss = str.charAt(i);
			boolean s = String.valueOf(ss).matches(REGEX_STRING_B);
			if (s) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去的字符串中第一个汉字出现的位置
	 * */
	public static int getIndex(String str) {
		for (int i = 0; i < str.length(); i++) {
			String s = str.substring(i, i + 1);
			if (s.matches(REGEX_STRING_B)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 字符串转日期类型
	 * */
	public static Date StringToDate(String dateStr) {
		return DateUtil.valueof(dateStr, "yyyy-MM-dd ");
	}

	/**
	 * 科学计数法转换成普通计数
	 * */
	public static String cancelSicen(String str) {
		Double d = Double.valueOf(str);
		DecimalFormat df = new java.text.DecimalFormat("###");
		return df.format(d);
	}

	public static boolean doCancelSicen(String str) {
		try {
			Double d = Double.valueOf(str);
			DecimalFormat df = new java.text.DecimalFormat("###");
			String dd = df.format(d);
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	public Integer getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getDownLoadPaths() {
		return downLoadPaths;
	}

	public void setDownLoadPaths(String[] downLoadPaths) {
		this.downLoadPaths = downLoadPaths;
	}

	public OutputStream getRes() {
		return res;
	}

	public void setRes(OutputStream res) {
		this.res = res;
	}

	public ZipOutputStream getZos() {
		return zos;
	}

	public void setZos(ZipOutputStream zos) {
		this.zos = zos;
	}

	public OrderTemp getOrderTemp() {
		return orderTemp;
	}

	public void setOrderTemp(OrderTemp orderTemp) {
		this.orderTemp = orderTemp;
	}

	public String getUploadErrType() {
		return uploadErrType;
	}

	public void setUploadErrType(String uploadErrType) {
		this.uploadErrType = uploadErrType;
	}

	public List<String> getUploadErrlist() {
		return uploadErrlist;
	}

	public void setUploadErrlist(List<String> uploadErrlist) {
		this.uploadErrlist = uploadErrlist;
	}

	public int getUploadListCount() {
		return uploadListCount;
	}

	public void setUploadListCount(int uploadListCount) {
		this.uploadListCount = uploadListCount;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<OrderTemp> getOrderTempList() {
		return orderTempList;
	}

	public void setOrderTempList(List<OrderTemp> orderTempList) {
		this.orderTempList = orderTempList;
	}

	public List<OrderTemp> getUploadOrderTemps() {
		return uploadOrderTemps;
	}

	public void setUploadOrderTemps(List<OrderTemp> uploadOrderTemps) {
		this.uploadOrderTemps = uploadOrderTemps;
	}

	public String getQueryMessage() {
		return queryMessage;
	}

	public void setQueryMessage(String queryMessage) {
		this.queryMessage = queryMessage;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public Integer getNumOfPage() {
		return numOfPage;
	}

	public void setNumOfPage(Integer numOfPage) {
		this.numOfPage = numOfPage;
	}

	public String getTempkey() {
		return tempkey;
	}

	public void setTempkey(String tempkey) {
		this.tempkey = tempkey;
	}

	public String getMailNoOrOrderNo() {
		return mailNoOrOrderNo;
	}

	public void setMailNoOrOrderNo(String mailNoOrOrderNo) {
		this.mailNoOrOrderNo = mailNoOrOrderNo;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(String isLocal) {
		this.isLocal = isLocal;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getPageNm() {
		return pageNm;
	}

	public void setPageNm(Integer pageNm) {
		this.pageNm = pageNm;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public String getInsertErr() {
		return insertErr;
	}

	public void setInsertErr(String insertErr) {
		this.insertErr = insertErr;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getOrderTempSize() {
		return orderTempSize;
	}

	public void setOrderTempSize(Integer orderTempSize) {
		this.orderTempSize = orderTempSize;
	}

	public MQService getMqService() {
		return mqService;
	}

	public void setMqService(MQService mqService) {
		this.mqService = mqService;
	}
	
	
	
}