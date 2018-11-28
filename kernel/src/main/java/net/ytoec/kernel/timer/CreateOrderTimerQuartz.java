package net.ytoec.kernel.timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.action.remote.process.OrderProcessor;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.JgWaybillUpdateService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.OrderJDBCUtilSingle;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * description 从金刚中取出淘宝订单，更新易通电商的订单表，traderInfo表和product表 created by
 * hufei@2013-04-22
 * 
 */
public class CreateOrderTimerQuartz extends QuartzJobBean {
	private static Logger logger = Logger
			.getLogger(CreateOrderTimerQuartz.class);
	private OrderService<Order> orderService;
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
	private static final int DEFAULT_THREAD_NUM = 5;

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
			Thread.currentThread().setName("thread.createOrderTimer");
			long t1 = System.currentTimeMillis();
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				logger.error("-------------订单创建开始----------------");
				isRunning = true;
				initFlagParams(); // 初始化参数

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

				conn = OrderJDBCUtilSingle.getInstance().getConnection();
				String sql = "SELECT ID AS ID, COMMAND_TYPE AS COMMANDTYPE,to_char(CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') AS CREATETIME,"
						+ "ORDER_LOGISTICS_CODE AS ORDERLOGISTICSCODE,ORDER_CHANNEL_CODE AS ORDERCHANNELCODE,STATUS AS STATUS,"
						+ "IS_OFFLINE AS ISOFFLINE,COMMAND_CONTENT AS COMMANDCONTENT "
						+ "FROM ytord.t_ord_order_command  partition("
						+ partition
						+ " )WHERE CREATE_TIME between to_date("
						+ "'"
						+ strStarttime
						+ "'"
						+ ",'yyyy-mm-dd hh24:mi:ss')"
						+ "  and   to_date('"
						+ strEndtime
						+ "', 'yyyy-MM-dd HH24:mi:ss')"
						+ " and ORDER_CHANNEL_CODE = 'TAOBAO_STD' and substr(ORDER_LOGISTICS_CODE, -1, 1) = "
						+ BATCH_NO + "  ORDER BY CREATE_TIME ASC";
				logger.error("sql语句：" + sql);
				logger.error("================查询物流号尾数为" + BATCH_NO
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

				while (rs.next()) {
					JGOrderDTO jGOrder = new JGOrderDTO();
					jGOrder.setId(rs.getString("ID"));
					jGOrder.setCommandType(rs.getInt("COMMANDTYPE"));
					jGOrder.setCreateTime(rs.getString("CREATETIME"));
					jGOrder.setOrderLogisticsCode(rs
							.getString("ORDERLOGISTICSCODE"));
					jGOrder.setOrderChannelCode(rs
							.getString("ORDERCHANNELCODE"));
					jGOrder.setStatus(rs.getInt("STATUS"));
					jGOrder.setIsOffline(rs.getInt("ISOFFLINE"));
					jGOrder.setCommendContent(rs.getString("COMMANDCONTENT"));

					// 设定结果集数据
					mod = num % threadNum;
					subJGOrderDTOs.get(mod).add(jGOrder);
					num++;
				}
				if (num == 0) {
					conf.setConfValue(strEndtime);
					logger.error("查询条数为0时订单创建定时器下次查询时间为："+ strEndtime);
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
				
				// 定时器下次查询时间更新
				setNotify();
				
				logger.error("下次查询起始时间===================" + nextMessageTime);
				logger.error("此次订单创建操作共" + num + "条数据,耗时"
						+ (System.currentTimeMillis() - t1));
			} catch (Exception e) {
				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("CreateOrderTimerQuartz出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace(
						"${mailContent}", "异常tomcatId ："+ tomcatId +"\n"+
						"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
								+ ex.getLineNumber()));
				mailService.sendMail(mail);
				logger.error("查询出错！", e);
			} finally {
				isRunning = false;
				subJGOrderDTOs.clear();
				OrderJDBCUtilSingle.getInstance().free(rs, stmt, conn);
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
				|| StringUtils.isEmpty(traderInfo.getPostCode())
				|| (StringUtils.isEmpty(traderInfo.getMobile()) && StringUtils
						.isEmpty(traderInfo.getPhone()))
				|| StringUtils.isEmpty(traderInfo.getCity())
				|| StringUtils.isEmpty(traderInfo.getProv())
				|| (StringUtils.isEmpty(receiver.getMobile()) && StringUtils
						.isEmpty(receiver.getPhone()))
				|| (jgOrderDTO.getIsOffline() == 1
						&& StringUtils.isEmpty(requestOrder.getMailNo()) && true != isPrint)) {
			logger.error("创建失败,错误原因为S05");
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
			requestOrder.setStatus("2");
			requestOrder.setPartitiondate(DateUtil.valueof(
					jgOrderDTO.getCreateTime(), partitiondateFormat));
			this.orderService.addOrderFormInfo(requestOrder);
			flag = true;
			logger.info("orderCreate" + "订单创建成功");
		} catch (DuplicateKeyException de) {
			logger.error("物流号不能重复！物流号：" + requestOrder.getTxLogisticId(), de);
			flag = false;
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
			logger.error(jgOrderDTO.getOrderLogisticsCode() + "面单绑定修改成功");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		if (jgOrderDTO.getIsOffline() == 1) {
			JgWaybillUpdate jgWaybillUpdate = new JgWaybillUpdate();
			jgWaybillUpdate.setLogisticId(updateInfo.getTxLogisticId());
			jgWaybillUpdate.setMailNo(updateInfo.getMailNo());
			jgWaybillUpdate.setClientID(updateInfo.getClientId());
			jgWaybillUpdateService.addJgWaybillUpdate(jgWaybillUpdate);
		}
		return flag;
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
						JGOrderDTO jGOrderDTO = new JGOrderDTO();
						jGOrderDTO = sendjgOrderDTOs.get(i);
						Integer orderStatus = jGOrderDTO.getCommandType();
						if (orderStatus == 10) {
							// 订单创建
							if (!orderCreate(jGOrderDTO)) {
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
			logger.error("订单创建定时器下次查询时间为："+ strEndtime);
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

}
