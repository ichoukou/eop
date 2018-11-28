/**
 * 
 */
package net.ytoec.kernel.timer;

import java.util.Map;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author wangyong 2012-02-14
 */
@SuppressWarnings("all")
public class EccoreAllBuildSearchTimer extends QuartzJobBean {

    private EccoreSearchService           eccoreSearchService;

    private ConfigCodeService<ConfigCode> configCodeService;

    private static int                    limit     = 100000;
    private int                           days      = 10;
    private int                           threadNum = 1;
    private int                           initId    = 1;
    private int                           totalId   = 200000000;

    private Logger                        logger    = Logger.getLogger(EccoreAllBuildSearchTimer.class);
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

            BulidDataThread[] bulidDataThreads = new BulidDataThread[threadNum];

            String smallThreadName = "";
            String bigThreadName = "";
            Integer interval = (totalId - 0) / threadNum;
            Map<String, String> map = Resource.getLastAllBuildStatus();
            for (int i = 0; i < bulidDataThreads.length; i++) {
                smallThreadName = "buildSolr" + (i + 1);
                bulidDataThreads[i] = new BulidDataThread(Integer.valueOf(map.get(smallThreadName)),
                                                          interval * (i + 1));
                bulidDataThreads[i].setName(smallThreadName);

                bulidDataThreads[i].start();
            }

        } catch (Exception e) {
            logger.error("build error", e);
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("EccoreAllBuildSearchTimer出现异常！");
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
        EccoreAllBuildSearchTimer.limit = limit;
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

            try {
                if (StringUtils.equalsIgnoreCase(Resource.buildAllMap.get(Thread.currentThread().getName() + "Status"),
                                                 "online")) {
                    return;
                }
                Resource.buildAllMap.put(Thread.currentThread().getName() + "Status", "online");
                for (int i = start; i <= end; i++) {
                    long startTime = System.currentTimeMillis();
                    eccoreSearchService.buildEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), i, limit);
                    logger.error(Thread.currentThread().getName() + ",开始id:" + i + ",bulid数据 " + limit + ",耗时："
                                 + (System.currentTimeMillis() - startTime));
                    i = i + limit;
                    Resource.updateAllBuildStatus(Thread.currentThread().getName(), String.valueOf(i));

                }

            } catch (Exception e) {
                logger.error("", e);
                Resource.buildAllMap.put(Thread.currentThread().getName() + "Status", "offline");
            } finally {

            }
        }

    }
}
