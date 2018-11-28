package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.search.service.JgOrderCommandService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import net.ytoec.kernel.action.remote.process.OrderProcessor;

@SuppressWarnings("all")
public class JqCreateOrderTimerQuartz extends QuartzJobBean {
	private static Logger logger = LoggerFactory
			.getLogger(JqCreateOrderTimerQuartz.class);

	private MailService<Mail> mailService;
	private String receiver = "";
	private static boolean isRunning = false;

	private static int limit = 50;
	private JgOrderCommandService jgOrderCommandService;
	private OrderService<Order> orderService;
	private static String partitiondateFormat = "yyyy-MM-dd";
	OrderProcessor orderProcessor = new OrderProcessor();
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
		try {
			if (!isRunning) {
				logger.error("定时器开始执行JqCreateOrderTimerQuartz");
				isRunning = true;
				boolean status = false;

				// 获取订单结果集
				List<JGOrderDTO> jgOrderList = new ArrayList<JGOrderDTO>();
				jgOrderList = jgOrderCommandService.getJGOrderByLimit(limit);

				// 获取删除订单ID范围
				int minId = 0;
				int maxId = 0;
				if (jgOrderList == null || jgOrderList.size() <= 0) {
					
					logger.error("金刚遗漏订单空值");
					isRunning = false;
					return;
				} else {
					minId = Integer.valueOf(jgOrderList.get(0).getId());
					maxId = Integer.valueOf(jgOrderList.get(
							jgOrderList.size() - 1).getId());
				}
				
				logger.error("获取金刚遗漏订单 minId:" + minId + "maxId:" + maxId);
				
				for (JGOrderDTO jgDTO : jgOrderList) {
					
					// 创建订单
					try{
						status = this.orderCreate(jgDTO);
						if (!status) {
							logger.error("金刚遗漏订单创建失败"+jgDTO.getOrderLogisticsCode());
							isRunning = false;
							return;
						}
						
					} catch (Exception e) {
						logger.error("金刚遗漏订单创建异常", e);
					} 
					
				}
				
				try{
					jgOrderCommandService.removeJGOrderById(minId, maxId);
					logger.error("ec_core_jg_order_command删除成功minId:"
							+ minId + "maxId:" + maxId);
					
				} catch (Exception e) {
					logger.error("金刚遗漏订单删除异常", e);
				} 
					
					
				logger.error("定时器JqCreateOrderTimerQuartz结束");
			}

		} catch (Exception e) {
			e.printStackTrace();
			StackTraceElement ex = e.getStackTrace()[0];
			/**
			 * 产生异常时，发送邮件。
			 */
			Mail mail = new Mail();
			mail.setSubject("BuyersTaobaoTaskTimer出现异常！");
			mail.setSendToMail("wusha@ytoxl.com");
			mail.setContent(mailContentTemp.replace("${mailContent}",
					"BuyersTaobaoTaskTimer执行时出现异常！\n异常类型：" + e.getClass()
							+ "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
		} finally {
			isRunning = false;
		}

	}

	/**
	 * description 订单创建 created by hufei@2013-04-22
	 */
	private boolean orderCreate(JGOrderDTO jgOrderDTO) throws Exception {
		boolean flag = false;
		OrderFormInfo requestOrder = orderProcessor.createToObject(jgOrderDTO);
		logger.error("JqCreateOrderTimerQuartz订单创建物流号requestOrder:"
				+ requestOrder.getTxLogisticId());
		logger.error("JqCreateOrderTimerQuartz物流号jgOrderDTO:"
				+ jgOrderDTO.getOrderLogisticsCode());
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
			
			logger.error("JqOrderCreate创建失败,错误原因为S05,存在空值！");
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
			requestOrder.setFromJGCreate(true);
			this.orderService.addOrderFormInfo(requestOrder);
			flag = true;
			logger.info("JqOrderCreate订单创建成功 物流号:"
					+ requestOrder.getLogisticProviderId());
		} catch (DuplicateKeyException de) {
			logger.error("物流号不能重复！物流号：" + requestOrder.getTxLogisticId(), de);
			flag = false;
		} catch (Exception e) {
			logger.error(
					"JqOrderCreate订单创建失败:" + jgOrderDTO.getOrderLogisticsCode()
							+ "信息为:", e);
			// TODO 添加到某个表里面重新拉取
			flag = false;
		}
		return flag;
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

	public JgOrderCommandService getJgOrderCommandService() {
		return jgOrderCommandService;
	}

	public void setJgOrderCommandService(
			JgOrderCommandService jgOrderCommandService) {
		this.jgOrderCommandService = jgOrderCommandService;
	}

	public OrderService<Order> getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService<Order> orderService) {
		this.orderService = orderService;
	}

	public static String getPartitiondateFormat() {
		return partitiondateFormat;
	}

	public static void setPartitiondateFormat(String partitiondateFormat) {
		JqCreateOrderTimerQuartz.partitiondateFormat = partitiondateFormat;
	}

	public OrderProcessor getOrderProcessor() {
		return orderProcessor;
	}

	public void setOrderProcessor(OrderProcessor orderProcessor) {
		this.orderProcessor = orderProcessor;
	}

	public static String getMailContentTemp() {
		return mailContentTemp;
	}

	public static void setMailContentTemp(String mailContentTemp) {
		JqCreateOrderTimerQuartz.mailContentTemp = mailContentTemp;
	}

}
