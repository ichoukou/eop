package net.ytoec.kernel.timer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import net.ytoec.kernel.action.remote.process.OrderProcessor;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.TimerLog;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.search.service.JgOrderCommandService;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.JgWaybillUpdateService;
import net.ytoec.kernel.service.MQService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.TimerLogService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.OrderJDBCUtilSingle;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson.JSON;

/**
 * description 从金刚中取出淘宝订单，更新易通电商的订单表，traderInfo表和product表 created by
 * hufei@2013-04-22
 * 
 */
public class CreateOrderTimerQuartz extends QuartzJobBean {
	private static Logger logger = LoggerFactory
			.getLogger(CreateOrderTimerQuartz.class);
	private OrderService<Order> orderService;
	private JgOrderCommandService<JGOrderDTO> jgOrderCommandService;
	private JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService;
	private ConfigCodeService<ConfigCode> configCodeService;
	private static boolean isRunning = false;
	OrderProcessor orderProcessor = new OrderProcessor();
	private String BATCH_NO;
	private String tomcatId;
	private int step = 100000; // 默认步长100000
	private String nextMessageTime;
	private String strStarttime;

	private MailService<Mail> mailService;
	private String receiver = "851713352@qq.com";
	// 默认线程数
	private static final int DEFAULT_THREAD_NUM = 3;

	private ConfigCode conf = null;

	// 线程数
	private Integer threadNum;

	// 从金刚获得订单数据
	private List<List<JGOrderDTO>> subJGOrderDTOs = new ArrayList<List<JGOrderDTO>>();
	// 当前线程数
	private Integer notify = 0;

	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	private static String partitiondateFormat = "yyyy-MM-dd";

	private String strEndtime = null;
	private int interval = 100000; // 时间间隔100000

	// 订单创建失败条数
	private AtomicInteger errTotal = new AtomicInteger();

	// 日志表
	private TimerLogService<TimerLog> timerLogService;
	private TimerLog timerLog = null;
	public static final String STRING_CREATE = "CREATE";

	private MQService mqService;

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
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if (!isRunning) {
			isRunning = true;
			Thread.currentThread().setName("thread.createOrderTimer");
			long t1 = System.currentTimeMillis();
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			StringBuffer sqlStr = new StringBuffer();
			try {
				logger.error("----订单创建开始----");

				// 初始化参数
				if (!initFlagParams()) {
					logger.error("tomcat id is Empty");
					isRunning = false;
					return;
				}

				String conKey = tomcatId + "__orderCreate_key";
				conf = configCodeService.getConfByKey(conKey);
				if (conf == null) {
					logger.error("conf is null");
					isRunning = false;
					return;
				}
				nextMessageTime = conf.getConfValue();

				// 开始时间
				Date startDate = DateUtil.valueof(nextMessageTime, dateFormat);
				Date lastEndDate = DateUtil.add(startDate, Calendar.SECOND,
						-(interval) / 1000);
				strStarttime = DateUtil.format(lastEndDate, dateFormat);

				if (DateUtil.millisecondsInterval(new Date(), startDate) < 600000) {
					isRunning = false;
					return;
				}
				Date nowDate = DateUtil.add(new Date(), Calendar.SECOND,
						-(interval) / 1000);
				// 如果发现上次同步时间与当前时间之差大于2倍的period，则提升本次同步跨度的2倍，保证同步的延迟在一个合理的范围
				if (DateUtil.millisecondsInterval(nowDate, startDate) > step * 3) {
					step = step * 3;
				} else if (DateUtil.millisecondsInterval(nowDate, startDate) < step) {
					return;
				}
				String partition = "PAR_"
						+ DateUtil.format(lastEndDate, "yyyy_MM_dd");

				Calendar cal = Calendar.getInstance();
				cal.setTime(lastEndDate);
				cal.add(Calendar.SECOND, step / 1000); // 毫秒/1000
				// 结束时间
				Date endDate = cal.getTime();

				// endDate = DateUtil.verifyDayInterval(lastEndDate, endDate);

				strEndtime = DateUtil.format(endDate, dateFormat);

				conn = OrderJDBCUtilSingle.getInstance().getConnection();
				sqlStr.append("SELECT ID AS ID, COMMAND_TYPE AS COMMANDTYPE,to_char(CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') AS CREATETIME,"
						+ "ORDER_LOGISTICS_CODE AS ORDERLOGISTICSCODE,ORDER_CHANNEL_CODE AS ORDERCHANNELCODE,STATUS AS STATUS,"
						+ "IS_OFFLINE AS ISOFFLINE,COMMAND_CONTENT AS COMMANDCONTENT "
						+ "FROM ytord.t_ord_order_command_xl ");

				// 如果检索开始时间和结束时间是跨天时 金刚数据不根据分区字段进行检索
				if (!DateUtil.isVerifyDayInterval(lastEndDate, endDate)) {
					sqlStr.append(" partition(" + partition + " )");
				} else {
					logger.error("跨天->检索开始时间：" + strStarttime + "检索结束时间："
							+ strEndtime);
				}
				sqlStr.append(" WHERE CREATE_TIME between to_date("
						+ "'"
						+ strStarttime
						+ "'"
						+ ",'yyyy-mm-dd hh24:mi:ss')"
						+ "  and   to_date('"
						+ strEndtime
						+ "', 'yyyy-MM-dd HH24:mi:ss')"
						+ " and ORDER_CHANNEL_CODE = 'TAOBAO_STD' and substr(ORDER_LOGISTICS_CODE, -1, 1) = "
						+ "'" + BATCH_NO + "'" + "  ORDER BY CREATE_TIME ASC");

				String sql = sqlStr.toString();
				logger.error("sql语句：" + sql);
				logger.info("================查询物流号尾数为" + BATCH_NO
						+ "的订单===========");
				stmt = conn.prepareStatement(sql); // 执行查询
				rs = stmt.executeQuery();

				// 根据线程数定义处理结果集
				for (int i = 0; i < threadNum; i++) {
					List<JGOrderDTO> tempJGOrderDTOList = new ArrayList<JGOrderDTO>();
					subJGOrderDTOs.add(tempJGOrderDTOList);
				}

				int num = 0;
				int mod = 0;
				JGOrderDTO jGOrder = null;
				while (rs.next()) {
					jGOrder = new JGOrderDTO();
					if (StringUtils.isNotBlank(rs.getString("ID"))) {
						jGOrder.setId(rs.getString("ID"));
					} else {
						jGOrder.setId(null);
					}
					jGOrder.setCommandType(rs.getInt("COMMANDTYPE"));
					if (StringUtils.isNotBlank(rs.getString("CREATETIME"))) {
						jGOrder.setCreateTime(rs.getString("CREATETIME"));
					} else {
						jGOrder.setCreateTime(null);
					}
					if (StringUtils.isNotBlank(rs
							.getString("ORDERLOGISTICSCODE"))) {
						jGOrder.setOrderLogisticsCode(rs
								.getString("ORDERLOGISTICSCODE"));
					} else {
						jGOrder.setOrderLogisticsCode(null);
					}

					if (StringUtils
							.isNotBlank(rs.getString("ORDERCHANNELCODE"))) {
						jGOrder.setOrderChannelCode(rs
								.getString("ORDERCHANNELCODE"));
					} else {
						jGOrder.setOrderChannelCode(null);
					}
					jGOrder.setStatus(rs.getInt("STATUS"));
					jGOrder.setIsOffline(rs.getInt("ISOFFLINE"));

					if (StringUtils.isNotBlank(rs.getString("COMMANDCONTENT"))) {
						jGOrder.setCommendContent(rs
								.getString("COMMANDCONTENT"));
					} else {
						jGOrder.setCommendContent(null);
					}

					// 设定结果集数据
					mod = num % threadNum;
					subJGOrderDTOs.get(mod).add(jGOrder);
					num++;
				}
				if (num == 0) {
					conf.setConfValue(strEndtime);
					logger.error("查询条数为0时订单创建定时器下次查询时间为：" + strEndtime);
					configCodeService.editConfig(conf);
					isRunning = false;
					return;
				}

				// 初始化countDown
				CountDownLatch threadSignal = new CountDownLatch(threadNum);

				// 线程类处理结果集
				SendOrderCreateThread[] threads = new SendOrderCreateThread[threadNum];
				for (int i = 0; i < threads.length; i++) {
					SendOrderCreateThread orderCreateThread = new SendOrderCreateThread(
							subJGOrderDTOs.get(i), threadSignal);

					// 设置线程名称
					orderCreateThread.setName("sub" + i);
					threads[i] = orderCreateThread;
					logger.error("thread领取task：subJGOrderList" + i + ",size:"
							+ subJGOrderDTOs.get(i).size());

					// 执行线程
					threads[i].start();
				}

				// 等待所有子线程执行完毕
				threadSignal.await();

				// timer 执行信息插入日志表
				timerLog = new TimerLog();
				timerLog.setStartTimeStr(strStarttime);
				timerLog.setEndTimeStr(strEndtime);
				timerLog.setOperate(TimerLog.OPERATE_0);
				timerLog.setTimerNO(tomcatId + "_" + STRING_CREATE);
				timerLog.setUseTime(System.currentTimeMillis() - t1);
				timerLog.setDataFrom(TimerLog.DATAFROM_JINGANG);
				timerLog.setTableName(TimerLog.TABLENAME_ORDER);
				timerLog.setNum(num);
				timerLog.setErrorNum(errTotal.get());
				timerLog.setIsError(TimerLog.ISERROR_TRUE);
				timerLog.setMessage("金刚订单同步");
				// 定时器下次查询时间更新
				setNotify();

				logger.info("下次查询起始时间===================" + strEndtime);
				logger.error("此次订单创建操作共" + num + "条数据,耗时"
						+ (System.currentTimeMillis() - t1));
			} catch (Exception e) {
				Exception exception = e;
				StringWriter writer = new StringWriter();
				exception.printStackTrace(new PrintWriter(writer));

				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("CreateOrderTimerQuartz出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp
						.replace("${mailContent}", "异常tomcatId ：" + tomcatId
								+ "\n" + "Timer监控出现异常！\n异常类型：" + e.getClass()
								+ "\n异常行数：" + ex.getLineNumber()
								+ "\n异常堆栈信息：\n" + writer.getBuffer().toString()));
				mailService.sendMail(mail);
				logger.error("查询出错！", e);
			} finally {
				logger.error("订单创建同步主线程finally");

				if (timerLog != null) {
					logger.error("订单创建同步-----------> 添加timer日志");

					// 添加日志
					timerLogService.addTimerLog(timerLog);
				}
				errTotal.set(0);
				subJGOrderDTOs.clear();
				OrderJDBCUtilSingle.getInstance().free(rs, stmt, conn);
				if (timerLog != null) {
					timerLog = null;
				}
				isRunning = false;
			}
		}
	}

	/**
	 * description 订单创建 created by hufei@2013-04-22
	 */
	private boolean orderCreate(JGOrderDTO jgOrderDTO) throws Exception {
		boolean flag = false;
		OrderFormInfo requestOrder = orderProcessor.createToObject(jgOrderDTO);
		TraderInfo traderInfo = requestOrder.getSender();
		TraderInfo receiver = requestOrder.getReceiver();
		boolean isPrint = Resource.getIsPrint(requestOrder.getClientId());// 判断是否面单打印
		if (StringUtils.isEmpty(requestOrder.getLogisticProviderId())
				|| StringUtils.isEmpty(requestOrder.getTxLogisticId())
				|| StringUtils.isEmpty(traderInfo.getName())
				|| StringUtils.isEmpty(traderInfo.getAddress())
				|| (StringUtils.isEmpty(traderInfo.getMobile()) && StringUtils
						.isEmpty(traderInfo.getPhone()))
				|| StringUtils.isEmpty(traderInfo.getCity())
				|| StringUtils.isEmpty(traderInfo.getProv())
				|| (StringUtils.isEmpty(receiver.getMobile()) && StringUtils
						.isEmpty(receiver.getPhone()))
				|| (jgOrderDTO.getIsOffline() == 1
						&& StringUtils.isEmpty(requestOrder.getMailNo()) && true != isPrint)) {
			if (StringUtils.isEmpty(requestOrder.getLogisticProviderId())) {
				jgOrderDTO.setOrderType(JGOrderDTO.logisticProviderIdEmpty);
			} else if (StringUtils.isEmpty(requestOrder.getTxLogisticId())) {
				jgOrderDTO.setOrderType(JGOrderDTO.txLogisticIdEmpty);
			} else if (StringUtils.isEmpty(traderInfo.getName())) {
				jgOrderDTO.setOrderType(JGOrderDTO.traderInfoNameEmpty);
			} else if (StringUtils.isEmpty(traderInfo.getAddress())) {
				jgOrderDTO.setOrderType(JGOrderDTO.traderInfoAddressEmpty);
			} else if (StringUtils.isEmpty(traderInfo.getMobile())
					&& StringUtils.isEmpty(traderInfo.getPhone())) {
				jgOrderDTO
						.setOrderType(JGOrderDTO.traderInfoMobileandPhoneEmpty);
			} else if (StringUtils.isEmpty(traderInfo.getCity())) {
				jgOrderDTO.setOrderType(JGOrderDTO.traderInfoCityEmpty);
			} else if (StringUtils.isEmpty(traderInfo.getProv())) {
				jgOrderDTO.setOrderType(JGOrderDTO.traderInfoProvEmpty);
			} else if (StringUtils.isEmpty(receiver.getMobile())
					&& StringUtils.isEmpty(receiver.getPhone())) {
				jgOrderDTO.setOrderType(JGOrderDTO.receiverMobileandPhoneEmpty);
			} else if (jgOrderDTO.getIsOffline() == 1
					&& StringUtils.isEmpty(requestOrder.getMailNo())
					&& true != isPrint) {
				jgOrderDTO
						.setOrderType(JGOrderDTO.getIsOfflineandmailnoisnullandisPrint);
			} else {
				jgOrderDTO.setOrderType(JGOrderDTO.unknowerror);
			}
			// 订单类型设置为异常订单
			// jgOrderDTO.setOrderType(JGOrderDTO.ORDERTYPE_1);

			// 添加到金刚遗漏订单表中

			jgOrderCommandService.addJGOrder(jgOrderDTO);
			logger.error("金刚同步异常订单---->添加到金刚遗漏订单表中;创建失败,错误原因为S05");
			return flag;
		}
		// 线上下单
		// 通过serviceType来判
		if (jgOrderDTO.getIsOffline() == 0)
			requestOrder.setLineType(Order.ONLINE_TYPE);
		else if (jgOrderDTO.getIsOffline() == 1)
			requestOrder.setLineType(Order.OFFLINE_TYPE);
		else
			requestOrder.setLineType(String.valueOf(jgOrderDTO.getIsOffline()));
		if (jgOrderDTO.getOrderChannelCode().equals("TAOBAO_STD")) {
			requestOrder.setClientId("Taobao");
		}
		try {
			requestOrder.setPartitiondate(DateUtil.valueof(
					jgOrderDTO.getCreateTime(), partitiondateFormat));
			requestOrder.setFromJGCreate(true);

			Order result = this.orderService.addOrderFormInfoForTB(requestOrder);
			flag = true;
			// 订单创建添加到solrorder,sender,receiver
			if(result != null){
				String message = getObjectFromJson(result);

				if (StringUtils.isNotBlank(message)) {
					try {
						if (mqService
								.send(message,
										net.ytoec.kernel.common.Constants.SOLR_ADD_ORDER)) {
						} else {
							logger.error("金刚创建同步到MQ失败");
						}

					} catch (Exception e) {
						logger.error("金刚订单创建同步到MQ异常", e);
					}
				}
					
			}
			
			logger.info("orderCreate" + "订单创建成功");
		} catch (DuplicateKeyException de) {
			logger.error("物流号不能重复！物流号：" + requestOrder.getTxLogisticId(), de);
			flag = true;
		} catch (Exception e) {
			logger.error(jgOrderDTO.getOrderLogisticsCode() + "订单创建失败", e);
			flag = false;
		}
		return flag;
	}

	/**
	 * description 订单取消 created by hufei@2013-04-22
	 */
	private boolean orderCancel(JGOrderDTO jgOrderDTO) throws Exception {
		boolean flag = false;
		UpdateWaybillInfo updateInfo = orderProcessor
				.cancelOrWayMergerToObject(jgOrderDTO, 1); // 订单取消和面单绑定json解析
		logger.debug("订单取消");
		if (jgOrderDTO.getOrderChannelCode().equals("TAOBAO_STD")) {
			updateInfo.setClientId("Taobao");
		}
		try {
			orderService.updateOrderMailNoByLogisticIdAndClientId(
					updateInfo.getTxLogisticId(), updateInfo.getClientId(),
					updateInfo.getMailNo(), updateInfo.getInfoContent());
			flag = true;
			logger.error(jgOrderDTO.getOrderLogisticsCode() + "订单取消修改成功");
		} catch (Exception e) {
			logger.error("订单取消失败", e);
			flag = false;
		}
		return flag;
	}

	/**
	 * description 面单绑定 created by hufei@2013-04-22
	 */
	private boolean orderUpdate(JGOrderDTO jgOrderDTO) {
		boolean flag = false;
		UpdateWaybillInfo updateInfo = orderProcessor
				.cancelOrWayMergerToObject(jgOrderDTO, 2);
		logger.debug("面单绑定");
		if (jgOrderDTO.getOrderChannelCode().equals("TAOBAO_STD")) {
			updateInfo.setClientId("Taobao");
		}
		try {
			orderService.updateOrderMailNoByLogisticIdAndClientId(
					updateInfo.getTxLogisticId(), updateInfo.getClientId(),
					updateInfo.getMailNo(), updateInfo.getInfoContent());
			flag = true;
		} catch (Exception e) {
			flag = false;
			logger.error("金刚订单创建同步->面单绑定修改失败"
					+ jgOrderDTO.getOrderLogisticsCode());
		}
		if (jgOrderDTO.getIsOffline() == 1) {
			JgWaybillUpdate jgWaybillUpdate = new JgWaybillUpdate();
			jgWaybillUpdate.setLogisticId(updateInfo.getTxLogisticId());
			jgWaybillUpdate.setMailNo(updateInfo.getMailNo());
			jgWaybillUpdate.setClientID(updateInfo.getClientId());
			if (!jgWaybillUpdateService.addJgWaybillUpdate(jgWaybillUpdate)) {
				logger.error("金刚订单创建同步->面单绑定进入JgWaybill表失败"
						+ jgOrderDTO.getOrderLogisticsCode());
			}
			;
		}
		return flag;
	}

	/**
	 * 根据tomcatId分配job查询数据
	 */
	private boolean initFlagParams() {
		tomcatId = SystemHelper.TOMCAT_ID;
		// tomcatId = "33";
		if (StringUtils.isEmpty(tomcatId)) {
			return false;
		}
		if (!StringUtils.isNumeric(tomcatId)) {
			return false;
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

		return true;
	}

	/**
	 * 订单创建 线程类
	 * 
	 * @author liuchunyan@2013-06-6
	 * 
	 */
	private class SendOrderCreateThread extends Thread {

		private List<JGOrderDTO> sendjgOrderDTOs;

		private CountDownLatch threadsSignal;

		SendOrderCreateThread(List<JGOrderDTO> jgOrderDTOs,
				CountDownLatch threadsSignal) {
			this.sendjgOrderDTOs = jgOrderDTOs;
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			try {
				if (sendjgOrderDTOs.size() > 0) {
					for (int i = 0; i < sendjgOrderDTOs.size(); i++) {
						JGOrderDTO jGOrderDTO = sendjgOrderDTOs.get(i);
						Integer orderStatus = jGOrderDTO.getCommandType();
						if (orderStatus == 10) {

							// 订单创建
							if (!orderCreate(jGOrderDTO)) {
								errTotal.getAndIncrement();

								// 订单类型设置为正常订单
								jGOrderDTO.setOrderType(JGOrderDTO.ORDERTYPE_0);

								// 添加到金刚遗漏订单表中
								jgOrderCommandService.addJGOrder(jGOrderDTO);
								logger.error("金刚创建订单同步->订单创建失败添加到金刚遗漏订单表中");
								continue;
							}
						} else if (orderStatus == 30) {
							// 面单绑定
							if (!orderUpdate(jGOrderDTO)) {
								continue;
							}
						} else if (orderStatus == 40) {
							// 订单取消
							if (!orderCancel(jGOrderDTO)) {
								continue;
							}
						} else {
							continue;
						}
					}
				}

			} catch (Exception e) {
				logger.error("SendOrderCreateThread error", e);
			} finally {
				// 线程结束时计数器减1
				threadsSignal.countDown();
			}
		}
	}

	/************************ GET AND SET ***********************/

	public JgWaybillUpdateService<JgWaybillUpdate> getJgWaybillUpdateService() {
		return jgWaybillUpdateService;
	}

	public void setJgWaybillUpdateService(
			JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService) {
		this.jgWaybillUpdateService = jgWaybillUpdateService;
	}

	public ConfigCodeService<ConfigCode> getConfigCodeService() {
		return configCodeService;
	}

	public void setConfigCodeService(
			ConfigCodeService<ConfigCode> configCodeService) {
		this.configCodeService = configCodeService;
	}

	public OrderService<Order> getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService<Order> orderService) {
		this.orderService = orderService;
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

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setNotify() {
		conf.setConfValue(strEndtime);

		// 定时器下次查询时间更新
		configCodeService.editConfig(conf);
		logger.error("订单创建定时器下次查询时间为：" + strEndtime);
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public TimerLogService<TimerLog> getTimerLogService() {
		return timerLogService;
	}

	public void setTimerLogService(TimerLogService<TimerLog> timerLogService) {
		this.timerLogService = timerLogService;
	}

	public JgOrderCommandService<JGOrderDTO> getJgOrderCommandService() {
		return jgOrderCommandService;
	}

	public void setJgOrderCommandService(
			JgOrderCommandService<JGOrderDTO> jgOrderCommandService) {
		this.jgOrderCommandService = jgOrderCommandService;
	}

	public MQService getMqService() {
		return mqService;
	}

	public void setMqService(MQService mqService) {
		this.mqService = mqService;
	}

	/**
	 * 易通solar订单更新MQ消息设置
	 * 
	 * @param orderUpdateInfo
	 * @return
	 */
	public String getObjectFromJson(Order order) {
		try {
			BuildSearch bean = orderService
					.getSolarDtoForAdd(order);
			if (bean != null) {
				String jsonString = JSON.toJSONString(bean);
				return jsonString;
			}
		} catch (Exception e) {
			Log.error("CreateOrderTimerQuartz getObjectFromJson error ", e);
		}
		return null;
	}

}
