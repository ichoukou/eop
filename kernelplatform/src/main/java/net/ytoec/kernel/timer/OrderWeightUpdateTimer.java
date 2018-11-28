package net.ytoec.kernel.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class OrderWeightUpdateTimer extends QuartzJobBean {

	private static Logger logger = LoggerFactory
			.getLogger(OrderWeightUpdateTimer.class);

	private Integer limit = 5000;

	private Integer jingangLimit = 100;

	private OrderService<Order> orderService;
	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";


	/**
	 * 从哪一天开始同步
	 */
	private String startDay;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	private List<OrderWeightUpdateDTO> orderWeightUpdateDTOs = new ArrayList<OrderWeightUpdateDTO>();

	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

	private static boolean isrunning = false;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		try {
			if (!isrunning) {
				isrunning = true;
				logger.error("start...");
				Date date = new Date();
				
				Date updateDate = (Date) Resource.orderParam.get("updateDay");
				if (updateDate==null) {
					updateDate=dateFormat.parse(startDay);
					Resource.orderParam.put("updateDay",updateDate);
				}
				if (!DateUtil.compareDay(updateDate, date)) {
					isrunning=false;
					return;
				}

				
				String partitiondate = DateUtil.format(updateDate, "yyyy-MM-dd");
				logger.error("partitiondate:"+partitiondate);
				Integer pageId = (Integer) Resource.orderParam.get("pageId");
				if (pageId == null) {
					orderWeightUpdateDTOs = orderService
							.getOrderByPartitiondate(partitiondate, limit);
				} else {
					orderWeightUpdateDTOs = orderService
							.getOrderByPartitiondateById(partitiondate, pageId,
									limit);
				}
                if (orderWeightUpdateDTOs.size()<limit) {
                	Resource.orderParam.put("updateDay",DateUtil.getDateBefore(updateDate, 1));
				}
				
				if (orderWeightUpdateDTOs.size() <= jingangLimit&& !orderWeightUpdateDTOs.isEmpty()) {
					
					orderService.updateOrderWeight(orderWeightUpdateDTOs);
					orderWeightUpdateDTOs.clear();
					isrunning=false;
					return;
				}
				List<OrderWeightUpdateDTO> tmpOrderWeightUpdateDTOs = new ArrayList<OrderWeightUpdateDTO>();
				for (int i = 0; i < orderWeightUpdateDTOs.size(); i++) {
					tmpOrderWeightUpdateDTOs.add(orderWeightUpdateDTOs.get(i));
					if ((i + 1) % jingangLimit == 0) {
						orderService
								.updateOrderWeight(tmpOrderWeightUpdateDTOs);
						tmpOrderWeightUpdateDTOs.clear();
					}
				}
				if (tmpOrderWeightUpdateDTOs.size() > 0) {
					orderService.updateOrderWeight(tmpOrderWeightUpdateDTOs);
					tmpOrderWeightUpdateDTOs.clear();
				}
				isrunning=false;
			}
		} catch (Exception e) {
			isrunning=false;
			logger.error("更新重量异常", e);
			StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("OrderWeightUpdateTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace(
					"${mailContent}",
					"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
							+ ex.getLineNumber()));
			mailService.sendMail(mail);

		}

	}


	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public OrderService<Order> getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService<Order> orderService) {
		this.orderService = orderService;
	}

	public String getUpdateDay() {
		return startDay;
	}

	public void setUpdateDay(String updateDay) {
		this.startDay = updateDay;
	}

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public Integer getJingangLimit() {
		return jingangLimit;
	}

	public void setJingangLimit(Integer jingangLimit) {
		this.jingangLimit = jingangLimit;
	}

	
}
