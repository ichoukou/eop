package net.ytoec.kernel.timer;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.FailinfoMD5Util;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.NotifyType;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.OrderStatusUpdService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SendTaskToTBService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.OrderStatusJDBCUtilSingle;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 用来处理更新订单状态到ec_core_order create by wangmindong create timer 2013-03-25
 */
public class OrderStatusUpdate2dbTimer extends TimerTask {

	private static Logger logger = Logger
			.getLogger(OrderStatusUpdate2dbTimer.class);
	private OrderService<Order> orderService;
	@Inject
	private OrderStatusUpdService orderStatusUpdService;

	private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private SMSServiceService smsServiceService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private TraderInfoService<TraderInfo> traderInfoService;

	/** 线程启动标识 */
	private static boolean isRunning = false;
	private MailService<Mail> mailService;
	private String receiver = "851713352@qq.com";
	/** 物流平台标识 **/
	public static final String SUCCESS_TRUE = "true";
	public static final String SUCCESS_FALSE = "false";
	private static final String LOGISTIC_PROVIDER_ID = "YTO"; // 物流公司编号
	private static final String INFO_TYPE = "STATUS";
	private static final String INFO_CONTENT_ACCEPT = "ACCEPT"; // 接单
	private static final String INFO_CONTENT_UNACCEPT = "UNACCEPT"; // 不接单
	private static final String INFO_CONTENT_GOT = "GOT"; // 揽收成功
	private static final String INFO_CONTENT_NOT_SEND = "NOT_SEND"; // 揽收不成功
	private static final String INFO_CONTENT_SIGNED = "SIGNED"; // 送达成功
	private static final String INFO_CONTENT_SENT_SCAN = "SENT_SCAN"; // 派件扫描
	private static final String INFO_CONTENT_FAILED = "FAILED"; // 送达失败
	private final String tableName = "YTORD.T_ORD_ORDER_MESSAGE";
	private final int BATCH_SIZE = 1000; // 一次读1000条
	private ConfigCodeService<ConfigCode> configCodeService;
	private ConfigCode conf = null;
	private UpdateInfo updateInfo = null;
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private String tomcatId;
	private String BATCH_NO;
	Date now = Calendar.getInstance().getTime();
	private int step = 100000; // 默认步长100000
	private String nextMessageTime;
	private String strStarttime;

	// List<UpdateInfo> updateOrderList = new ArrayList<UpdateInfo>();//
	// 订单状态更新list
	// List<UpdateInfo> questionnaireList = new ArrayList<UpdateInfo>();//
	// 问题件list
	// List<SendTaskToTB> sendTaskList = new ArrayList<SendTaskToTB>();
	// 当前线程数
	private Integer notify = 0;

	// 默认线程数
	private static final int DEFAULT_THREAD_NUM = 5;

	// 线程数
	private Integer threadNum;
	List<List<UpdateInfo>> subUpdateInfos = new ArrayList<List<UpdateInfo>>();
	List<List<UpdateInfo>> subQuestionnaires = new ArrayList<List<UpdateInfo>>();
	List<StringBuffer> subs = new ArrayList<StringBuffer>();

	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	private String strEndtime = null;

	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

	@Override
	public void run() {
		if (!isRunning) {
			try {
				isRunning = true;
				initFlagParams(); // 初始化参数

				String conKey = tomcatId + "__orderUpdate_key";
				conf = configCodeService.getConfByKey(conKey);
				if (conf == null) {
					logger.error("conf is null");
					// TODO 发邮件
					isRunning = false;
					return;
				}
				nextMessageTime = conf.getConfValue();

				// 开始时间
				Date startDate = DateUtil.valueof(nextMessageTime, dateFormat);
				Date lastEndDate = DateUtil.add(startDate, Calendar.SECOND,
						-10000 / 1000);
				strStarttime = DateUtil.format(lastEndDate, dateFormat);
				// 如果发现上次同步时间与当前时间之差大于2倍的period，则提升本次同步跨度的2倍，保证同步的延迟在一个合理的范围
				if (DateUtil.millisecondsInterval(new Date(), startDate) > step * 3) {
					step = step * 3;
				} else if (DateUtil.millisecondsInterval(new Date(), startDate) < step) {
					return;
				}
				String partition = "PAR_"
						+ DateUtil.format(startDate, "yyyy_MM_dd");

				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.SECOND, step / 1000); // 毫秒/1000
				// 结束时间
				Date endDate = cal.getTime();

				endDate = DateUtil.verifyDayInterval(startDate, endDate);

				strEndtime = DateUtil.format(endDate, dateFormat);
				logger.error("-------------------开始执行更新订单状态同步----------------------");
				long t1 = System.currentTimeMillis();
				con = OrderStatusJDBCUtilSingle.getInstance().getConnection();
				if (con == null) {
					logger.error("获取数据库连接错误");
					isRunning = false;
					return;
				}
				String sql = "select message.ID id,message.ORDER_LOGISTICS_CODE orderLogisticsCode,"
						+ "message.ORDER_CHANNEL_CODE orderChannelCode,message.WAYBILL_NO waybillNo, message.MESSAGE_TYPE messageType, "
						+ "to_char(message.MESSAGE_TIME,'yyyy-MM-dd hh24:mi:ss') messageTime, message.INFORM_CHANNEL_TYPE informChannelType, "
						+ "to_char(message.OPER_TIME,'yyyy-MM-dd hh24:mi:ss') operTime,  "
						+ " message.SIGNOFF_NAME signoffName ,"
						+ " message.WEIGHT weight "
						+ "from "
						+ tableName
						+ " message WHERE MESSAGE_TIME between to_date("
						+ "'"
						+ strStarttime
						+ "'"
						+ ",'yyyy-MM-dd hh24:mi:ss') "
						+ "  and   to_date('"
						+ strEndtime
						+ "', 'yyyy-MM-dd HH24:mi:ss')"
						+ "and substr(ORDER_LOGISTICS_CODE, -1, 1) = "
						+ BATCH_NO
						+ " and MESSAGE_TYPE = 5 and INFORM_CHANNEL_TYPE in (1,2,3,4,7,8,11)  "
						+ " ORDER BY MESSAGE_TIME ASC ";

				logger.error("更新订单状态, SQL:" + sql);
				logger.error("================查询物流号尾数为" + BATCH_NO
						+ "的订单===========");
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				logger.info("===========查询金刚数据，数据循环数据开始=================");
				boolean flagNum = false;
				// 根据线程数定义处理结果集
				for (int i = 0; i < threadNum; i++) {
					List<UpdateInfo> tempJGOrderDTOList = new ArrayList<UpdateInfo>();
					StringBuffer temStr = new StringBuffer();
					subUpdateInfos.add(tempJGOrderDTOList);
					subQuestionnaires.add(tempJGOrderDTOList);
					subs.add(temStr);
				}

				int num = 0;
				int mod = 0;
				while (rs.next()) {
					String txLogisticId = rs.getString("orderLogisticsCode"); // 物流平台的物流号（不能为空）
					String id = rs.getString("id");
					String logisticProviderId = LOGISTIC_PROVIDER_ID; // 物流公司编号
					String operTime = rs.getString("operTime"); // 揽收时间
					String mailNo = rs.getString("waybillNo"); // 运单号
					// 有值表示错误信息
					String orderChannelCode = rs.getString("orderChannelCode");
					String signoffName = rs.getString("signoffName");
					Double weight = rs.getDouble("weight");
					String messageTime = rs.getString("messageTime");

					updateInfo = new UpdateInfo();
					/** 创建通知类型 **/
					createNotifyTypeProcessor(updateInfo,
							rs.getShort("informChannelType"));

					updateInfo.setTxLogisticID(txLogisticId);
					updateInfo.setLogisticProviderID(logisticProviderId);// 物流平台的物流号
					updateInfo.setAcceptTime(operTime);
					updateInfo.setMailNo(mailNo);
					updateInfo.setClientId("Taobao");
					updateInfo.setOrderChannelCode(orderChannelCode);
					updateInfo.setSignoffName(signoffName);
					updateInfo.setWeight(weight);
					updateInfo.setSuccess("true");

					/** 参数校验 **/
					if (validateParams(updateInfo)) {
						logger.info(".......为空校验失败.........!");
						// TODO 应该发邮件
						continue;
					}

					/** 当揽收成功，或者失败的时候，将揽收时间记录到order表中的type字段中。 **/
					if (StringUtils.equals("GOT", updateInfo.getInfoContent())
							|| StringUtils.equals("NOT_SEND",
									updateInfo.getInfoContent())) {

						updateInfo.setType(operTime);
					}

					// 设定结果集数据
					mod = num % threadNum;

					/** 当SIGNED状态时要修改问题件状态 **/
					if (StringUtils.equals(updateInfo.getInfoContent(),
							"SIGNED")
							&& !StringUtils.isEmpty(updateInfo.getMailNo())) {
						UpdateInfo questionnairInfo = new UpdateInfo();
						questionnairInfo.setMailNo(updateInfo.getMailNo());
						questionnairInfo.setOrderStatus("1");
						subQuestionnaires.get(mod).add(questionnairInfo);
					}

					subUpdateInfos.get(mod).add(updateInfo);
					num++;

					/** 组装批量删除sql **/
					subs.get(mod).append("'").append(id).append("'")
							.append(",");

				}
				if (num == 0) {
					conf.setConfValue(strEndtime);
					configCodeService.editConfig(conf);
					isRunning = false;
					return;
				}

				// if (!flagNum) {
				// if (messageTime != null && !messageTime.equals("")) {
				// // 将下一次时间作为第二次查询的起始时间
				// // startTime = new
				// //
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageTime);
				// logger.error("下次查询起始时间为:" + messageTime);
				// nextMessageTime = messageTime;
				// flagNum = true;
				// }
				// }
				logger.error("共查询到:" + num + "条数据,用时:"
						+ (System.currentTimeMillis() - t1) + "");
				logger.error("===========查询金刚数据，数据循环数据结束=================");

				try {
					// if (Resource.verifyDispatch("Taobao")) { // 判断电商客户对应的推送标记

					// 初始化countDown
					CountDownLatch threadSignal = new CountDownLatch(threadNum);
					// 线程类处理结果集
					SendOrderStatusUpdate2dbThread[] threads = new SendOrderStatusUpdate2dbThread[threadNum];
					for (int i = 0; i < threads.length; i++) {
						SendOrderStatusUpdate2dbThread orderCreateThread = new SendOrderStatusUpdate2dbThread(
								subUpdateInfos.get(i),
								subQuestionnaires.get(i), subs.get(i),
								threadSignal);

						// 设置线程名称
						orderCreateThread.setName("sub" + i);
						threads[i] = orderCreateThread;
						logger.error("thread领取task：subUpdateInfos" + i
								+ ",size:" + subUpdateInfos.get(i).size()
								+ "/n subQuestionnaires:" + i + ",size:"
								+ subQuestionnaires.get(i).size());

						// 执行线程
						threads[i].start();
					}

					// 等待所有子线程执行完毕
					threadSignal.await();
					// }
					logger.error("处理此次数据总耗时为"
							+ (System.currentTimeMillis() - t1));
				} catch (Exception e) {
					logger.error("==============批量批量修改订单状态 失败!=============", e);
				}
			} catch (Exception e) {
				isRunning = false;
				logger.error("订单状态同步出错!", e);
				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("OrderStatusUpdate2dbTimer出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace(
						"${mailContent}",
						"Timer监控出现异常！\n异常类型：" + e.getMessage() + "\n异常行数："
								+ ex.getLineNumber()));
				mailService.sendMail(mail);
			} finally {
				subUpdateInfos.clear();
				subQuestionnaires.clear();
				subs.clear();
				isRunning = false;
				OrderStatusJDBCUtilSingle.getInstance().free(rs, ps, con);
			}
		}
	}

	/**
	 * 根据tomcatId分配job查询数据
	 */
	private void initFlagParams() {
		tomcatId = SystemHelper.TOMCAT_ID;
		// tomcatId = "35";
		if (StringUtils.isEmpty(tomcatId)) {
			logger.error("tomcat id is Empty");
			isRunning = false;
			return;
		}
		if (!StringUtils.isNumeric(tomcatId)) {
			logger.error("tomcat id is not is Numeric");
			isRunning = false;
			return;
		}
		logger.error("tomcat id:" + tomcatId);
		if (tomcatId.equals("33"))
			BATCH_NO = "0";
		else if (tomcatId.equals("34"))
			BATCH_NO = "1";
		else if (tomcatId.equals("35"))
			BATCH_NO = "2";
		else if (tomcatId.equals("36"))
			BATCH_NO = "3";
		else if (tomcatId.equals("37"))
			BATCH_NO = "4";
		else if (tomcatId.equals("43"))
			BATCH_NO = "5";
		else if (tomcatId.equals("44"))
			BATCH_NO = "6";
		else if (tomcatId.equals("45"))
			BATCH_NO = "7";
		else if (tomcatId.equals("46"))
			BATCH_NO = "8";
		else if (tomcatId.equals("47"))
			BATCH_NO = "9";
		else if (tomcatId.equals("530"))
			BATCH_NO = "10";
		else if (tomcatId.equals("540"))
			BATCH_NO = "11";
		else if (tomcatId.equals("550"))
			BATCH_NO = "12";
		else if (tomcatId.equals("560"))
			BATCH_NO = "13";
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

	/**
	 * 创建订单状态
	 * 
	 * @param short1
	 */
	private void createNotifyTypeProcessor(UpdateInfo updateInfo,
			short informChannelType) {
		if (NotifyType.ACCEPT.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_ACCEPT);
			updateInfo.setStatus(INFO_CONTENT_ACCEPT);
			updateInfo.setInfoType(INFO_TYPE);
		}

		if (NotifyType.UNACCEPT.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_UNACCEPT);
			updateInfo.setStatus(INFO_CONTENT_UNACCEPT);
			updateInfo.setInfoType(INFO_TYPE);
		}

		if (NotifyType.GOT.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_GOT);
			updateInfo.setStatus(INFO_CONTENT_GOT);
			updateInfo.setInfoType(INFO_TYPE);
		}

		if (NotifyType.NOT_SEND.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_NOT_SEND);
			updateInfo.setStatus(INFO_CONTENT_NOT_SEND);
			updateInfo.setInfoType(INFO_TYPE);
		}

		if (NotifyType.SIGNED.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_SIGNED);
			updateInfo.setStatus(INFO_CONTENT_SIGNED);
			updateInfo.setInfoType(INFO_TYPE);
		}

		if (NotifyType.FAILED.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_FAILED);
			updateInfo.setStatus(INFO_CONTENT_FAILED);
			updateInfo.setInfoType(INFO_TYPE);
		}

		if (NotifyType.DELIVERY.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_SENT_SCAN);
			updateInfo.setStatus(INFO_CONTENT_SENT_SCAN);
			updateInfo.setInfoType(INFO_TYPE);
		}
	}

	// /**
	// * 批量删除修改成功的数据
	// *
	// * @return
	// */
	/*
	 * private void batchDeleteMassge(Connection con, PreparedStatement
	 * ps,ResultSet rs, StringBuffer sb1, StringBuffer sb2, Integer count) {
	 * long t1 = System.currentTimeMillis(); try {
	 * 
	 * String paramsIdsOne = sb1.toString().substring(0, sb1.length() - 1);
	 * String sql1 = "delete from YTORD.T_ORD_ORDER_MESSAGE where id in (" +
	 * paramsIdsOne + ")"; System.err.println("删除SQL One:" + sql1); ps =
	 * con.prepareStatement(sql1); rs = ps.executeQuery(); if (count > 1000) {
	 * String paramsIdsTwo = sb2.toString().substring(0, sb2.length() - 1);
	 * String sql2 = "delete from YTORD.T_ORD_ORDER_MESSAGE where id in (" +
	 * paramsIdsTwo + ")"; System.err.println("删除SQL Two:" + sql2); ps =
	 * con.prepareStatement(sql2); rs = ps.executeQuery(); } long t2 =
	 * System.currentTimeMillis(); long t3 = t2 - t1; logger.error("成功删除" +
	 * count + "条据,用时" + t3); } catch (Exception e) {
	 * logger.error("删除数据库数据数据失败==========" + e); } }
	 */

	/**
	 * 批量删除修改成功的数据
	 * 
	 * @return
	 */
	private void batchDeleteMassge(Connection con, PreparedStatement ps,
			ResultSet rs, StringBuffer sb1) {
		long t1 = System.currentTimeMillis();
		try {

			String paramsIdsOne = sb1.toString().substring(0, sb1.length() - 1);
			String sql = "delete from YTORD.T_ORD_ORDER_MESSAGE where id in ("
					+ paramsIdsOne + ")";
			logger.error("删除SQL :" + sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			long t2 = System.currentTimeMillis();
			long t3 = t2 - t1;
			logger.error("成功删除" + sb1.length() + "条据,用时" + t3);
		} catch (Exception e) {
			logger.error("删除数据库数据数据失败==========" + e);
		}
	}

	/**
	 * 如果揽收，派送扫描，签收 可能需要给买家发短信 短信方法处理
	 * 
	 * @author wmd
	 * @throws Exception
	 */
	private void isSendSMStoSeller(List<UpdateInfo> updateInfoList)
			throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < updateInfoList.size(); i++) {
			/** 揽收，派送扫描，签收 可能需要给买家发短信 **/
			if (StringUtils.equalsIgnoreCase(StatusEnum.GOT.getName(),
					updateInfoList.get(i).getInfoContent())
					|| StringUtils.equalsIgnoreCase(
							StatusEnum.SENT_SCAN.getName(),
							updateInfoList.get(i).getInfoContent())
					|| StringUtils.equalsIgnoreCase(
							StatusEnum.SIGNED.getName(), updateInfoList.get(i)
									.getInfoContent())) {
				String status = updateInfoList.get(i).getInfoContent(); // 状态
				String mailNo = updateInfoList.get(i).getMailNo(); // 运单号
				String tx_LogisticId = updateInfoList.get(i).getTxLogisticID();

				if (StringUtils.isEmpty(status) || StringUtils.isEmpty(mailNo)
						|| StringUtils.isEmpty(tx_LogisticId)) {
					logger.error("订单状态、物流号、运单号不能为空！");
					continue;
				}
				// 通过物流号，获取订单
				Order order = orderService.getOrderByLogisticId(tx_LogisticId);
				if (order == null || order.getId() == null) {
					continue;
				}
				// 通过订单的customer_id 获取订单对应的用户
				User user = Resource.getUserByCustomerId(order.getCustomerId());
				if (user == null || user.getId() == null) {
					continue;
				}
				String childType = user.getChildType();
				Map<String, Object> map = new HashMap<String, Object>();

				if (StringUtils.equalsIgnoreCase("B", childType)) {
					if (StringUtils.isNotBlank(user.getUserSource())) { // 若：B:分仓
						map.put("userId",
								Integer.parseInt(user.getUserSource()));
					} else {
						map.put("userId", user.getId());
					}
				} else {
					Integer parentId = user.getParentId();
					if (parentId != null) { // 主ID,或子ID
						map.put("userId", parentId);
					} else {
						map.put("userId", user.getId());
					}
				}

				map.put("status", StringUtils.upperCase(status));
				map.put("mailNo", mailNo);
				map.put("txLogisticId", tx_LogisticId);
				map.put("createUserId", user.getId());
				// 判断订单用户，是否有短信可发。 且判断服务是否开通
				if (smsPortService.supplyStatusOpenUp(user.getId())
						&& smsServiceService.isOpeanService(user.getId(),
								status)) {
					TraderInfo traderInfo = traderInfoService
							.getTraderInfoByIdAndByTraderType(order.getId(), 1);// 1为收件人
					String buyMobile = traderInfo.getMobile();// 收件人手机号
					String buyName = traderInfo.getName();// 收件人姓名

					logger.error(" smsInfoService.saveInfoToQueue(map);"
							+ map.toString());
					if (!StringUtils.isEmpty(buyMobile)) {
						map.put("buyMobile", buyMobile);
						map.put("buyName", buyName);
						logger.error(" START  --->   smsInfoService.saveInfoToQueue(map);"
								+ map.toString());
						smsInfoService.saveInfoToQueue(map);
						logger.error(" END --> smsInfoService.saveInfoToQueue(map);");
					}
				}
			}
		}
	}

	/**
	 * remark 参数封装
	 * 
	 * @return
	 */

	private String toXmlFragment(UpdateInfo updateInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Response>");
		if (!isBlank(updateInfo.getTxLogisticID())) {
			sb.append("<txLogisticID>");
			sb.append(updateInfo.getTxLogisticID());
			sb.append("</txLogisticID>");
		}

		if (!isBlank(updateInfo.getLogisticProviderID())) {
			sb.append("<logisticProviderID>");
			sb.append(updateInfo.getLogisticProviderID());
			sb.append("</logisticProviderID>");
		}

		sb.append("<success>");
		sb.append(updateInfo.getSuccess());
		sb.append("</success>");
		if (SUCCESS_FALSE.equals(updateInfo.getSuccess())) {
			sb.append("<reason>");
			sb.append(new UpdateWaybillInfo().getResponse()); // ??
			sb.append("</reason>");
		}

		sb.append("</Response>");
		return sb.toString();
	}

	private boolean isBlank(String arg) {
		if (arg == null || "".equals(arg)) {
			return true;
		}
		return false;
	}

	/**
	 * xml参数加密
	 * 
	 * @param xmlParams
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String md5Encode(String xmlParams) throws NoSuchAlgorithmException {
		String str = xmlParams;
		String byteMd5 = FailinfoMD5Util.encrypt(str);

		return new String(byteMd5);

	}

	/**
	 * 将物流平台的物流号装换成flag_id
	 * 
	 * @param arg
	 * @return
	 */

	private static int getflagid(String arg) {
		String s = arg;
		// String s="klllll123456";
		s = s.substring(s.length() - 1);
		logger.debug(s);
		return Integer.parseInt(s);
	}

	/**
	 * 将物流平台的物流号装换成flag_id
	 * 
	 * @param arg
	 * @return
	 */
	private static String getflag(String arg) {
		String sid = arg;
		sid = sid.substring(sid.length() - 1);
		String s = sid;
		logger.debug(s);
		return s;
	}

	/**
	 * 查询订单状态数据校验
	 */
	private boolean validateParams(UpdateInfo info) {
		boolean b = false;
		if (StringUtils.isEmpty(info.getTxLogisticID())
				|| StringUtils.isEmpty(info.getInfoType())
				|| StringUtils.isEmpty(info.getInfoContent())
				|| ("SIGNED".equals(info.getInfoContent().toUpperCase())
						&& StringUtils.isEmpty(info.getAcceptTime()) && StringUtils
						.isEmpty(info.getMailNo()))) {
			b = true;
		}
		return b;
	}

	/**
	 * 更新订单状态 线程类
	 * 
	 * @author liuchunyan@2013-06-13
	 * 
	 */
	private class SendOrderStatusUpdate2dbThread extends Thread {
		private List<UpdateInfo> sendUpdateOrderList;
		private List<UpdateInfo> sendQuestionnaireList;
		private StringBuffer sendSb1 = null;
		private CountDownLatch threadsSignal;

		SendOrderStatusUpdate2dbThread(List<UpdateInfo> updateOrderList,
				List<UpdateInfo> questionnaireList, StringBuffer sb1,
				CountDownLatch threadsSignal) {
			this.sendUpdateOrderList = updateOrderList;
			this.sendQuestionnaireList = questionnaireList;
			this.threadsSignal = threadsSignal;
			this.sendSb1 = sb1;
		}

		@Override
		public void run() {
			try {
				if (sendUpdateOrderList.size() > 0) {
					orderStatusUpdService.batchUpdateOrdStatus(
							sendUpdateOrderList, sendQuestionnaireList);// 批量修改订单状态
					// batchDeleteMassge(con, ps, rs, sendSb1); // 批量删除查询的数据
					// 将待发送短信的数据插入到ec_core_wait_sms,(效率低，暂时改掉)
					/*
					 * if(orderStatusSMSService.batchAddOrdForSMS( smsInfoList
					 * )){ logger.error("成功批量插入ec_core_orderstatus_sms表,此次插入"
					 * +smsInfoList.size()+"条记录"); }
					 */
					// isSendSMStoSeller(updateOrderList); // 短信发送
				}
			} catch (Exception e) {
				logger.error("SendOrderStatusUpdate2dbThread error", e);
			} finally {
				setNotify();

				// 线程结束时计数器减1
				threadsSignal.countDown();
			}
		}
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

	public OrderService<Order> getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService<Order> orderService) {
		this.orderService = orderService;
	}

	public SendTaskToTBService<SendTaskToTB> getSendTaskToTBService() {
		return sendTaskToTBService;
	}

	public void setSendTaskToTBService(
			SendTaskToTBService<SendTaskToTB> sendTaskToTBService) {
		this.sendTaskToTBService = sendTaskToTBService;
	}

	public Integer getThreadNum() {
		return threadNum;
	}

	@SuppressWarnings("static-access")
	public void setThreadNum(Integer threadNum) {
		if (threadNum == null || threadNum.intValue() == 0) {
			this.threadNum = this.DEFAULT_THREAD_NUM;
		} else {
			this.threadNum = threadNum;
		}
	}

	public Integer getNotify() {
		return notify;
	}

	synchronized public void setNotify() {
		logger.error("notify main thread:" + Thread.currentThread().getName());
		this.notify = notify + 1;
		if (notify >= threadNum) {
			conf.setConfValue(strEndtime);
			configCodeService.editConfig(conf);
			isRunning = false;
		}
	}

	public ConfigCodeService<ConfigCode> getConfigCodeService() {
		return configCodeService;
	}

	public void setConfigCodeService(
			ConfigCodeService<ConfigCode> configCodeService) {
		this.configCodeService = configCodeService;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

}
