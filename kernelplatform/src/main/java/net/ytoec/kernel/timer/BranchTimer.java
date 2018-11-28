/**
 * 
 */
package net.ytoec.kernel.timer;

import net.ytoec.kernel.dataobject.Branch;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.search.service.BranchSearchService;
import net.ytoec.kernel.service.MailService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author wangyong 2012-02-14
 */
@SuppressWarnings("all")
public class BranchTimer extends QuartzJobBean {

    private BranchSearchService<Branch> branchSearchService;

    private String                      searchServer;

    private static final int            PAGENUM = 5000;     // 每次查询记录数

	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                  + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                  + "<DIV> ${mailContent}</DIV>"
                                                                  + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                  + "<BR>--------------------------------------------------------------------------------------"
                                                                  + "</BODY></HTML>";

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


    public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}
    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        try {
            branchSearchService.commitBranchData(searchServer);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("BranchTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
        }

    }

    public BranchSearchService<Branch> getBranchSearchService() {
        return branchSearchService;
    }

    public void setBranchSearchService(BranchSearchService<Branch> branchSearchService) {
        this.branchSearchService = branchSearchService;
    }

    public String getSearchServer() {
        return searchServer;
    }

    public void setSearchServer(String searchServer) {
        this.searchServer = searchServer;
    }

}
