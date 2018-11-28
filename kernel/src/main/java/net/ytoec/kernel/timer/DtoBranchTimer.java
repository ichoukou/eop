package net.ytoec.kernel.timer;


import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 用来更新网点缓存信息
 * 
 * @author mabo
 */
public class DtoBranchTimer extends QuartzJobBean {

    private MailService<Mail>                   mailService;
    private String                              receiver               = "yto_yitong1@163.com";
    /**
     * 邮件模版
     */
    private static String                       mailContentTemp        = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                         + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                         + "<DIV> ${mailContent}</DIV>"
                                                                         + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                         + "<BR>--------------------------------------------------------------------------------------"
                                                                         + "</BODY></HTML>";
    
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
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // TODO Auto-generated method stub
        try {
            Resource.UpdateDotCache();
        } catch (Exception e) {
            // TODO: handle exception
            //logger.info("网点缓存更新失败!", e);
            StackTraceElement ex = e.getStackTrace()[0];
            Mail mail = new Mail();
            mail.setSubject("QuestionnaireTimer出现异常！");
            mail.setSendToMail(this.receiver);
            mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass()
                                                                      + "\n异常行数：" + ex.getLineNumber()));
            mailService.sendMail(mail);
        }


    }

}
