package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuestionnaireCompensateTimer extends QuartzJobBean {

	private static Logger logger = LoggerFactory
			.getLogger(QuestionnaireCompensateTimer.class);
	/** 线程启动标识 */
	private static boolean isRunning = false;

	/**
	 * 对象表名<br>
	 * 在配置文件中配置(kernel.properties)，由spring注入
	 */
	private String tablename;

	private static String dateFormat = "yyyy-MM-dd";

	/**
	 * 从哪一天开始同步
	 */
	private String startDay;
	private Integer limit = 10000;

	/**
	 * 每次查询的金刚记录数，
	 */
	private Integer jingangLimit = 100;

	private QuestionnaireService<Questionnaire> questionnaireService;

	private List<Questionnaire> questionnairesList = new ArrayList<Questionnaire>();

	private MailService<Mail> mailService;

	private String receiver = "yto_yitong1@163.com";
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
		// TODO Auto-generated method stub

		// Calendar cal = dateFormat.getCalendar();

		try {
			if (!isRunning) {
				isRunning = true;
				tablename = ConfigUtilSingle.getInstance()
						.getQuestionnaireIssue();
				if (StringUtils.isEmpty(tablename)) {
					logger.error("问题件表名为空! 请检查配置文件是否配置或spring是否注入成功!");
					isRunning = false;
					return;
				}
				// 从数据库中拿到时间
				String updateDatestr = Resource.getQsnConfig("qsnStartDay");
				Date dealTime = DateUtil.valueof(updateDatestr, dateFormat);
				logger.error("last update day:" + updateDatestr);

				Integer pageId = (Integer) Resource.questionnaireMap
						.get("pageId");
				if (pageId == null) {
					questionnairesList = questionnaireService
							.getQuestionnaireByDealTime(dealTime, limit);
				} else {
					questionnairesList = questionnaireService
							.getQestionnaireById(pageId, limit);
				}
				 logger.error("get易通问题件 "+questionnairesList.size());
				if (questionnairesList.size() < limit) {
					Resource.updateQsnConfig("qsnStartDay", DateUtil.format(
							DateUtil.getDateAfter(dealTime, 1), dateFormat));
				}

				if (questionnairesList.size() <= jingangLimit
						&& !questionnairesList.isEmpty()) {

					questionnaireService
							.updateQuestionnaire(questionnairesList);
					questionnairesList.clear();
					isRunning = false;
					return;
				}
				List<Questionnaire> tmpQuestionnairesList = new ArrayList<Questionnaire>();
				for (int i = 0; i < questionnairesList.size(); i++) {
					tmpQuestionnairesList.add(questionnairesList.get(i));
					if ((i + 1) % jingangLimit == 0) {
						questionnaireService
								.updateQuestionnaire(tmpQuestionnairesList);
						tmpQuestionnairesList.clear();
					}
				}
				if (tmpQuestionnairesList.size() > 0) {
					questionnaireService
							.updateQuestionnaire(tmpQuestionnairesList);
					tmpQuestionnairesList.clear();
				}
				isRunning = false;
			}
		} catch (Exception e) {
			isRunning = false;
			logger.info("问题件同步出错!", e);
			StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("QuestionnaireCompensateTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace(
					"${mailContent}",
					"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
							+ ex.getLineNumber()));
			mailService.sendMail(mail);
		}

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

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public QuestionnaireService<Questionnaire> getQuestionnaireService() {
		return questionnaireService;
	}

	public void setQuestionnaireService(
			QuestionnaireService<Questionnaire> questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
