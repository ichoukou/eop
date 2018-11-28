/**
 * 
 */
package net.ytoec.kernel.timer;

import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author wangyong 2012-02-14
 */
@SuppressWarnings("all")
public class OnceEccoreAllBuildSearchTimer extends QuartzJobBean {

    private EccoreSearchService           eccoreSearchService;

    private ConfigCodeService<ConfigCode> configCodeService;

    private static int                    limit     = 100000;
    private int                           days      = 10;
    private int                           threadNum = 1;
    private int                           initId    = 191583268;
    private int                           totalId   = 329080620;//329079620 这次的时间从2012-05-01

    private static Logger                        logger    = LoggerFactory.getLogger(OnceEccoreAllBuildSearchTimer.class);

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
            logger.error("start once...");
            for (int i = initId; i <= totalId;) {
                long startTime = System.currentTimeMillis();
                eccoreSearchService.buildEccoreDataOnce(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), i, limit);
                logger.error(Thread.currentThread().getName() + ",once开始id:" + i + ",bulid数据 " + limit + ",耗时："
                             + (System.currentTimeMillis() - startTime));
                i = i + limit;

            }

        } catch (Exception e) {
            logger.error("build error", e);
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("OnceEccoreAllBuildSearchTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);

        }

    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public EccoreSearchService getEccoreSearchService() {
        return eccoreSearchService;
    }

    public void setEccoreSearchService(EccoreSearchService eccoreSearchService) {
        this.eccoreSearchService = eccoreSearchService;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        OnceEccoreAllBuildSearchTimer.limit = limit;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getInitId() {
        return initId;
    }

    public void setInitId(int initId) {
        this.initId = initId;
    }

    public int getTotalId() {
        return totalId;
    }

    public void setTotalId(int totalId) {
        this.totalId = totalId;
    }

    private class BulidDataThread extends Thread {

        private int start;
        private int end;

        BulidDataThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {

			// try {
			// if
			// (StringUtils.equalsIgnoreCase(Resource.buildAllMap.get(Thread.currentThread().getName()
			// + "Status"),
			// "online")) {
			// return;
			// }
			// Resource.buildAllMap.put(Thread.currentThread().getName() +
			// "Status", "online");
			//
			//
			// } catch (Exception e) {
			// logger.error("", e);
			// Resource.buildAllMap.put(Thread.currentThread().getName() +
			// "Status", "offline");
			// } finally {
			//
			// }
        }

    }
}
