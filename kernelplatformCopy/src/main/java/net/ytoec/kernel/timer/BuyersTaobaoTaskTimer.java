package net.ytoec.kernel.timer;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SMSBuyersService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 执行淘宝获取订单之后产生任务的定时器！
 * @author wusha 2012-07-12
 */
@SuppressWarnings("all")
public class BuyersTaobaoTaskTimer extends QuartzJobBean {
	private static Logger  logger     = LoggerFactory.getLogger(BuyersTaobaoTaskTimer.class);

    private SMSBuyersService smsBuyersService;
    
	private MailService<Mail> mailService;
	
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                  + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                  + "<DIV> ${mailContent}</DIV>"
                                                                  + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                  + "<BR>--------------------------------------------------------------------------------------"
                                                                  + "</BODY></HTML>";

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	
        try {
        	logger.error("-------------获取淘宝会员信息开始----------------");
        	smsBuyersService.downZipFromTaobao();
        	logger.error("-------------获取淘宝会员信息结束----------------");
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement ex = e.getStackTrace()[0];
            /**
             * 产生异常时，发送邮件。
             */
			Mail mail = new Mail();
			mail.setSubject("BuyersTaobaoTaskTimer出现异常！");
			mail.setSendToMail("wusha@ytoxl.com");
			mail.setContent(mailContentTemp.replace("${mailContent}", "BuyersTaobaoTaskTimer执行时出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
        }

    }

    public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

	public SMSBuyersService getSmsBuyersService() {
		return smsBuyersService;
	}

	public void setSmsBuyersService(SMSBuyersService smsBuyersService) {
		this.smsBuyersService = smsBuyersService;
	}

}
