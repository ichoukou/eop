package net.ytoec.kernel.timer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.search.service.BuildSearchService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.SendTaskToTBService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author qixiaobing
 *
 */
public class JobMonitor extends QuartzJobBean {

	public static Logger logger = Logger.getLogger(JobMonitor.class);

	private MailService<Mail> mailService;
	private SendTaskService<SendTask> sendTaskService;
	private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;

	private Mail mail;
	private String receiver = "yto_yitong1@163.com;lilongyong@ytoxl.com;liuchunyan@ytoxl.com;";
	private int frequency = 5;
	private int taskFromTB = 0;
	private int taskFromJinGang = 0;
	private int taobaoAlarmValue = 2000;
	private int jinGangAlarmValue = 10000;

	private BuildSearchService<BuildSearch> buildSearchService;

	private BuildSearchService<BuildSearchStatusWeightIndex> weightBuildSearchService;

	private BuildSearchService service;
	// 用来监控build_search表
	// build_search_status_weight_index 表
	// 的数量
	private int taskBuildSearch = 0;
	private int taskBuildSearchStatusWeightIndex = 0;
	private int numBuildSearch = 10000;
	private int numBuildSearchStatusWeightIndex = 10000;

	private static Map<String, String> resultMap = new LinkedHashMap<String, String>();
	private static Integer todayTotalNumber = 0;

	private String nextStarttime;
	private String weightUpdateTime;
	private String qsnStartDay;
	
	// 创建订单上次更新的实际时间
	private Map<String,String> createOrderNextStarttimes;
	
	// 更新订单状态上次更新的实际时间
	private Map<String,String>  updateOrderStateUpdateTimes;
	
	private String timeInfosString;
	// 阀值级别 1为一级阀值 2为二级阀值
	private int norm = 1;
	private String normReceiver = "";
	private String ccReceiver = "";// 邮件抄送人
	private int level = 10; // 预警级别

	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>阀值标准：<A HREF=\"http://w3.ytoxl.com/confluence/pages/viewpage.action?pageId=14451341\" TARGET=\"_BLANK\">http://w3.ytoxl.com/confluence/pages/viewpage.action?pageId=14451341</A></DIV></DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto.net.cn\" TARGET=\"_BLANK\">http://ec.yto.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.error("start run job monitor... number:" + todayTotalNumber);
		try {

			// 获取问题件和重量上次更新的实际
			nextStarttime = Resource.getJobMonitorTime("nextStarttime");
			weightUpdateTime = Resource.getJobMonitorTime("weightUpdateTime");
			
			// 获取订单创建上次更新的实际时间
			createOrderNextStarttimes = new HashMap<String ,String>();
			List<ConfigCode> createOrderNextStarttimeList= Resource.getJobMonitorTime1("%__orderCreate_key%");
			for(ConfigCode bean : createOrderNextStarttimeList){
				createOrderNextStarttimes.put(bean.getConfText()+":", bean.getConfValue());
			}
			
			// 获取订单状态更新上次更新的实际时间
			updateOrderStateUpdateTimes = new HashMap<String ,String>();
			List<ConfigCode> updateOrderStateUpdateTimeList= Resource.getJobMonitorTime1("%__orderUpdate_key%");
			for(ConfigCode bean : updateOrderStateUpdateTimeList){
				updateOrderStateUpdateTimes.put(bean.getConfText()+":", bean.getConfValue());
			}
			
			timeInfosString = "<br>&nbsp;&nbsp;&nbsp;&nbsp;问题件上次同步时间："
					+ nextStarttime + "<br>&nbsp;&nbsp;&nbsp;&nbsp;重量更新上次同步时间："
					+ weightUpdateTime;

			resultMap.put("监控信息汇报：", timeInfosString);
			resultMap.putAll(createOrderNextStarttimes);
			resultMap.putAll(updateOrderStateUpdateTimes);
			
			// 监控ec_core_build_search表 满指定数发邮件
			taskBuildSearch = buildSearchService.countBuildSearch();
			// 监控ec_core_build_search_status_weight_index表 满指定数发邮件
			int i = weightBuildSearchService
 					.countBuildSearchStatusWeightIndex();
			int p = service.countBuildSearchStatusWeightIndexPressure();
			int v = service.countBuildSearchStatusWeightVersion();
			taskBuildSearchStatusWeightIndex = i + p + v;
			// 监控ec_core_send_task表
			taskFromTB = sendTaskService.countTask();
			// 监控 ec_core_send_tasktoTB表
			taskFromJinGang = sendTaskToTBService.countTask();

			resultMap.put("负责人：武莎<br>&nbsp;&nbsp;&nbsp;&nbsp;solr订单增量：",
					String.valueOf(taskBuildSearch));
			resultMap.put("&nbsp;&nbsp;&nbsp;&nbsp;solr状态增量：",
					String.valueOf(taskBuildSearchStatusWeightIndex));
			resultMap.put("负责人：王敏东，陈峰，顿杰<br>&nbsp;&nbsp;&nbsp;&nbsp;taskFromTB：",
					String.valueOf(taskFromTB));
			resultMap.put("&nbsp;&nbsp;&nbsp;&nbsp;taskFromJinGang：",
					String.valueOf(taskFromJinGang));

			todayTotalNumber++;

			String flag = "";
			/**
			 * falg的值标识积压的种类： "1":标识solr订单增量积压, "2":表示订单状态增量积压, "3":表示订单积压,
			 * "4"：表示订单反馈状态积压
			 * 如果solr订单增量和订单状态增量都积压，则为"12",四个数据全部积压为"1234",其余情况falg值的规则也是如此
			 */

			if (taskBuildSearch > numBuildSearch) {
				flag = "1";
			}
			if (taskBuildSearchStatusWeightIndex > numBuildSearchStatusWeightIndex) {
				flag = flag + "2";
			}
			if (taskFromTB > taobaoAlarmValue) {
				flag = flag + "3";
			}
			if (taskFromJinGang > jinGangAlarmValue) {
				flag = flag + "4";
			}
			StringBuilder subject = new StringBuilder();
			if (StringUtils.contains(flag, "1")) {
				subject.append("solr订单增量积压  ");
			}
			if (StringUtils.contains(flag, "2")) {
				subject.append("solr订单状态增量积压   ");
			}
			if (StringUtils.contains(flag, "3")) {
				subject.append("订单积压    ");
			}
			if (StringUtils.contains(flag, "4")) {
				subject.append("订单状态积压   ");
			}

			// 二级阀值邮件接受添加
			if (taskBuildSearch > (numBuildSearch * 10)
					|| taskBuildSearchStatusWeightIndex > (numBuildSearchStatusWeightIndex * 10)
					|| taskFromTB > (taobaoAlarmValue * 10)
					|| taskFromJinGang > (jinGangAlarmValue * 10)) {
				norm = 2;
				level = 11;
				subject.append("注意：");
				if (taskBuildSearch > (numBuildSearch * 10)) {
					subject.append("solr订单增量积压   ");
				}
				if (taskBuildSearchStatusWeightIndex > (numBuildSearchStatusWeightIndex * 5)) {
					subject.append("solr订单状态增量积压   ");
				}
				if (taskFromTB > (taobaoAlarmValue * 10)) {
					subject.append("订单积压    ");
				}
				if (taskFromJinGang > (jinGangAlarmValue * 10)) {
					subject.append("订单状态积压   ");
				}
				subject.append("超过二级阀值");
			}
			logger.error("todaytotalNumber " + todayTotalNumber
					+ "  frequency " + frequency + "  flag:" + flag + "---");
			logger.error(StringUtils.isEmpty(flag) + "---------"
					+ (todayTotalNumber == frequency));
			if (StringUtils.isEmpty(flag)&& todayTotalNumber == frequency) {
				 
				logger.error("yeah! it's the " + frequency + " times today");
				logger.error("order:" + taskFromTB + " orderStatus:"
						+ taskFromJinGang + " solrOrder:" + taskBuildSearch
						+ " solrOrderStatus:"
						+ taskBuildSearchStatusWeightIndex);
				todayTotalNumber = 0;
				mail = constructMail(0, resultMap, "", norm);
				mailService.sendMail(mail);
				resultMap.clear();
			}
			if (!StringUtils.isEmpty(flag)) {
				logger.error("order:" + taskFromTB + " orderStatus:"
						+ taskFromJinGang + " solrOrder:" + taskBuildSearch
						+ " solrOrderStatus:"
						+ taskBuildSearchStatusWeightIndex);
				mail = constructMail(level, resultMap, subject.toString(), norm);
				mailService.sendMail(mail);
				resultMap.clear();
			}

			logger.error("end run job monitor....");
		} catch (Exception e) {
			logger.error("jobMonitor", e);
			mail = constructMail(9, resultMap, "查询失败，请检查", norm);
			
			mailService.sendMail(mail);
		}

	}

	private Mail constructMail(int level, Map<String, String> resultMap,
			String subject, int norm) {

		Mail mail = new Mail();
		if (level == 10) {
			mail.setSubject("【一级警告】" + subject);
		} else if (level == 11) {
			mail.setSubject("【二级警告】" + subject);
		} else if (level == 9) {
			mail.setSubject("【严重警告】" + subject);
		} else {
			mail.setSubject("【正常】没有积压！");
		}
		if (norm == 2) {
			mail.setSendToMail(this.receiver + this.normReceiver);
			mail.setSendCCMail(this.ccReceiver);
		} else {
			mail.setSendToMail(this.receiver);
		}

		StringBuffer sBuffer = new StringBuffer();
		for (Entry<String, String> entry : resultMap.entrySet()) {
			sBuffer.append(entry.getKey() + "&nbsp;" + entry.getValue()
					+ "<br/>");
		}

		String content = mailContentTemp.replace("${mailContent}",
				sBuffer.toString());
		mail.setContent(content);

		return mail;
	}

	public SendTaskService<SendTask> getSendTaskService() {
		return sendTaskService;
	}

	public void setSendTaskService(SendTaskService<SendTask> sendTaskService) {
		this.sendTaskService = sendTaskService;
	}

	public SendTaskToTBService<SendTaskToTB> getSendTaskToTBService() {
		return sendTaskToTBService;
	}

	public void setSendTaskToTBService(
			SendTaskToTBService<SendTaskToTB> sendTaskToTBService) {
		this.sendTaskToTBService = sendTaskToTBService;
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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getTaskFromJinGang() {
		return taskFromJinGang;
	}

	public void setTaskFromJinGang(int taskFromJinGang) {
		this.taskFromJinGang = taskFromJinGang;
	}

	public int getTaobaoAlarmValue() {
		return taobaoAlarmValue;
	}

	public void setTaobaoAlarmValue(int taobaoAlarmValue) {
		this.taobaoAlarmValue = taobaoAlarmValue;
	}

	public int getJinGangAlarmValue() {
		return jinGangAlarmValue;
	}

	public void setJinGangAlarmValue(int jinGangAlarmValue) {
		this.jinGangAlarmValue = jinGangAlarmValue;
	}

	public BuildSearchService<BuildSearchStatusWeightIndex> getWeightBuildSearchService() {
		return weightBuildSearchService;
	}

	public void setWeightBuildSearchService(
			BuildSearchService<BuildSearchStatusWeightIndex> weightBuildSearchService) {
		this.weightBuildSearchService = weightBuildSearchService;
	}

	public BuildSearchService getService() {
		return service;
	}

	public void setService(BuildSearchService service) {
		this.service = service;
	}

	public BuildSearchService<BuildSearch> getBuildSearchService() {
		return buildSearchService;
	}

	public void setBuildSearchService(
			BuildSearchService<BuildSearch> buildSearchService) {
		this.buildSearchService = buildSearchService;
	}

	public int getNumBuildSearch() {
		return numBuildSearch;
	}

	public void setNumBuildSearch(int numBuildSearch) {
		this.numBuildSearch = numBuildSearch;
	}

	public int getNumBuildSearchStatusWeightIndex() {
		return numBuildSearchStatusWeightIndex;
	}

	public void setNumBuildSearchStatusWeightIndex(
			int numBuildSearchStatusWeightIndex) {
		this.numBuildSearchStatusWeightIndex = numBuildSearchStatusWeightIndex;
	}

	public String getNormReceiver() {
		return normReceiver;
	}

	public void setNormReceiver(String normReceiver) {
		this.normReceiver = normReceiver;
	}

	public String getCcReceiver() {
		return ccReceiver;
	}

	public void setCcReceiver(String ccReceiver) {
		this.ccReceiver = ccReceiver;
	}
}
