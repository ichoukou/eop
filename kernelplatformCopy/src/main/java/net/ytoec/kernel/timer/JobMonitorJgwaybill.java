package net.ytoec.kernel.timer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.JgWaybillService;
import net.ytoec.kernel.service.MailService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;

/**
 * @author qixiaobing
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class JobMonitorJgwaybill extends QuartzJobBean {

	public static Logger logger = LoggerFactory.getLogger(JobMonitorJgwaybill.class);

	
	private Mail mail;
	private String receiver = "yitonggroup@ytoxl.com;";
	private int jinGangAlarmValue = 10000;
	private static Map<String, String> resultMap = new LinkedHashMap<String, String>();
	// 阀值级别 1为一级阀值 2为二级阀值
	private String normReceiver = "";
	private String ccReceiver = "";// 邮件抄送人
	
	@Inject
	private MailService<Mail> mailService;
	
	@Inject
	private JgWaybillService<JgWaybill> jgWaybillService;
	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto.net.cn\" TARGET=\"_BLANK\">http://ec.yto.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

	@Override
	public void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("start run job Jgwaybillmonitor... number:");
		try {
			resultMap.put("监控信息汇报：", "");
			Integer jgWaybillNum=jgWaybillService.countJgwaybill();
			StringBuilder subject = new StringBuilder();
			if (jgWaybillNum>= jinGangAlarmValue) {
				subject.append("JgWaybill表订单积压    ");
				resultMap.put("监控信息汇报：", "<br>&nbsp;&nbsp;&nbsp;&nbsp;Jgwaybill表存在积压");
				resultMap.put("<br>&nbsp;&nbsp;&nbsp;&nbsp;JgWaybill表积压量：", jgWaybillNum.toString());
				resultMap.put("<br><br><br><br>负责人：林华杰，罗典，刘春燕，黄建华，梁栋<br>&nbsp;&nbsp;&nbsp;&nbsp;","");
			 
				mail = constructMail(1, resultMap, subject.toString());
				mailService.sendMail(mail);
				resultMap.clear();
			}
			else{
				subject.append("JgWaybill表没有积压    ");
				resultMap.put("监控信息汇报：", "<br>&nbsp;&nbsp;&nbsp;&nbsp;Jgwaybill表没有积压");
				resultMap.put("<br>&nbsp;&nbsp;&nbsp;&nbsp;JgWaybill表订单量：", jgWaybillNum.toString());
				resultMap.put("<br><br><br><br>负责人：林华杰，罗典，刘春燕，黄建华，梁栋<br>&nbsp;&nbsp;&nbsp;&nbsp;","");
			 
				mail = constructMail(0, resultMap, subject.toString());
				mailService.sendMail(mail);
				resultMap.clear();
			}
			logger.info("end run job monitor....");
		} catch (Exception e) {
			logger.error("jobMonitor", e);
			resultMap.put("监控出现异常：", e.toString());
			mail = constructMail(2, resultMap, "查询失败，请检查" );
			mailService.sendMail(mail);
		}

	}

	private Mail constructMail(int level, Map<String, String> resultMap,
			String subject ) {

		Mail mail = new Mail();
		if (level == 1) {
			mail.setSubject("【警告】" + subject);
		}  
	    if (level == 0){
			mail.setSubject("【正常】没有积压！");
		}
	    if (level ==2){
	    	mail.setSubject("【Jgwaybill监控异常】");
	    }
		 
	    mail.setSendToMail(this.receiver + this.normReceiver);
		mail.setSendCCMail(this.ccReceiver);
		 

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
 

	public int getJinGangAlarmValue() {
		return jinGangAlarmValue;
	}

	public void setJinGangAlarmValue(int jinGangAlarmValue) {
		this.jinGangAlarmValue = jinGangAlarmValue;
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

	public JgWaybillService<JgWaybill> getJgWaybillService() {
		return jgWaybillService;
	}

	public void setJgWaybillService(JgWaybillService<JgWaybill> jgWaybillService) {
		this.jgWaybillService = jgWaybillService;
	}

	
	

	 
}
