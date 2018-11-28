package net.ytoec.kernel.timer;


import java.io.PrintWriter;
import java.io.StringWriter;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class BuildSolrTimer extends QuartzJobBean{
	private static Logger logger = LoggerFactory.getLogger(BuildSolrTimer.class);
	private MailService<Mail> mailService;
	private String receiver = "";
	private static boolean isRunning = false;
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
		if(!isRunning){
			try{
				logger.error("线程开始");
				isRunning = true;
				System.out.println("此处开始执行线程");
				
				logger.error("线程结束");
			} catch (Exception e) {
				Exception exception = e; 
				StringWriter writer = new StringWriter();  
	            exception.printStackTrace(new PrintWriter(writer));
				
				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("CreateOrderTimerQuartz出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace(
						"${mailContent}", "异常tomcatId ："+ "" +"\n"+
						"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
								+ ex.getLineNumber() + "\n异常堆栈信息：\n" + writer.getBuffer().toString()));
				mailService.sendMail(mail);
				logger.error("查询出错！", e);
			} finally {
				isRunning = false;
			}
		}
	}
	



}
