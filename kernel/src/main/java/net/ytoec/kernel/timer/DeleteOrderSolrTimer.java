/**
 * net.ytoec.kernel.timer
 * DeleteOrderSolr.java
 * 2012-10-16下午04:19:06
 * @author wangyong
 */
package net.ytoec.kernel.timer;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.MailService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 每天3：30分删除不存在用户在solr中的订单数据
 * @author wangyong
 * 2012-10-16
 */
public class DeleteOrderSolrTimer extends QuartzJobBean {
	
	
	private EccoreSearchService eccoreSearchService;
	
	private String	searchServer;
	
	private MailService<Mail> mailService;
	
    private String receiver;
    
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
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			eccoreSearchService.deleteBefore7Days(searchServer);
		} catch(Exception e) {
			e.printStackTrace();
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("BranchTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
		}
		
	}

	public EccoreSearchService getEccoreSearchService() {
		return eccoreSearchService;
	}

	public void setEccoreSearchService(EccoreSearchService eccoreSearchService) {
		this.eccoreSearchService = eccoreSearchService;
	}

	public String getSearchServer() {
		return searchServer;
	}

	public void setSearchServer(String searchServer) {
		this.searchServer = searchServer;
	}

}
