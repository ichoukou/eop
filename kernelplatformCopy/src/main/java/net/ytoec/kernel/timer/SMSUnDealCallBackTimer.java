package net.ytoec.kernel.timer;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SMSUnDealService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 将无线天利超过72小时
 * 未正确处理的短信
 * 转移到ec_core_paysms_info短信历史记录表中 
 * @author guolongchao
 */
public class SMSUnDealCallBackTimer extends QuartzJobBean{

	private SMSUnDealService  smsUnDealService;
	
	private static int  limit  = 1000;
	private static boolean isRunning = false;
	private Logger logger = LoggerFactory.getLogger(SMSUnDealCallBackTimer.class);	
	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    
    /**
     * 超过的小时数
     */
    private static int  hours  = 12;
    

	/**
     * 邮件模版
     */
    private static String    mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                               + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                               + "<DIV> ${mailContent}</DIV>"
                                               + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                               + "<BR>--------------------------------------------------------------------------------------"
                                               + "</BODY></HTML>";
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		 try {
	            logger.error("SMSUnDealCallBackTimer start ...");
	            if (isRunning) {
	                return;
	            }
	            isRunning = true;
	            
	            /**
	        	 * 将未正确处理的短信统统转移到短信历史记录表ec_core_paysms_info
	        	 * @param limit
	        	 */
	            smsUnDealService.sendToHistoryInfo(limit,hours);
	            
	            isRunning = false;

	        } catch (Exception e) {
	            logger.error("build error", e);
	            isRunning = false;
	            StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("SMSUnDealCallBackTimer出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
				mailService.sendMail(mail);
	        }
	}
	
	public  int getHours() {
		return hours;
	}

	public  void setHours(int hours) {
		SMSUnDealCallBackTimer.hours = hours;
	}
	
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
    	SMSUnDealCallBackTimer.limit = limit;
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

	public SMSUnDealService getSmsUnDealService() {
		return smsUnDealService;
	}

	public void setSmsUnDealService(SMSUnDealService smsUnDealService) {
		this.smsUnDealService = smsUnDealService;
	}
}
