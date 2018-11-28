package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.ServerConfig;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.ServerConfigService;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SendTask2JinggangTimerQuartz extends QuartzJobBean {

    private static Logger                     logger          = LoggerFactory.getLogger(SendTask2JinggangTimerQuartz.class);
    private static final int                  DEFAULT_LIMIT   = 5000;
    private static boolean                    running         = false;

    private Integer                           limit;
    private List<SendTask>                    taskList        = new ArrayList<SendTask>();
    private List<Integer>                     taskFlags       = new ArrayList<Integer>();
    private SendTaskService<SendTask>         sendTaskService;
    private ServerConfigService<ServerConfig> serverConfigService;

    private String                            tomcatId;
    private MailService<Mail>                 mailService;
    private String                            receiver        = "yto_yitong1@163.com";
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                + "<DIV> ${mailContent}</DIV>"
                                                                + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                + "<BR>--------------------------------------------------------------------------------------"
                                                                + "</BODY></HTML>";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	if (!running) {
			logger.error("tojg" + Thread.currentThread().getId());
			try {
				running = true;

				tomcatId = "36";
//				tomcatId = SystemHelper.TOMCAT_ID;
				logger.error("SendTask2JinggangTimerQuartz --->tomcat id:" + tomcatId);
				if (StringUtils.isEmpty(tomcatId)) {
					logger.error("tomcat id is Empty");
					running = false;
					return;
				}
				if (!StringUtils.isNumeric(tomcatId)) {
					logger.error("tomcat id is not is Numeric");
					running = false;
					return;
				}
				
				ServerConfig serverConfig = serverConfigService
						.getConfigByServerId(Integer.valueOf(tomcatId));
				if (serverConfig == null || serverConfig.getServerId() == null) {
					logger.error("serverConfig is null");
					running = false;
					return;
				}

				// 解析tomcat 对应的flag
				stringToList(serverConfig.getTaskFlag());

				// FIXME 时间排序 asc
				long startTime = System.currentTimeMillis();

				// FIXME error-info
				logger.error("start time:" + startTime + "," + new Date() + ","
						+ Thread.currentThread().getId());

				taskList = sendTaskService.getSendTaskListByFlagsAndLimit(
						taskFlags, limit);
				logger.error("taskFlags :"+ taskFlags);
				taskFlags.clear();
				logger.error("get task list time:"
						+ (System.currentTimeMillis() - startTime) + "ms,"
						+ new Date());
				logger.error("taskList:" + taskList.size());
				SendTask sendTask = new SendTask();
				XmlSender xmlSender = new XmlSender();

				for (int i = 0; i < taskList.size(); i++) {
					sendTask = taskList.get(i);
					xmlSender.setUrlString(sendTask.getRequestURL());
					// 设置请求方法为POST方法.
					xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
					// 设置请求参数.
					xmlSender.setRequestParams(sendTask.getRequestParams());
					
					if (sendTask.getRequestParams().contains("UpdateInfo")
							&& sendTask.getRequestParams().contains("infoType")
							&& sendTask.getRequestParams().contains("infoContent")) {
						sendTaskService.sendTask(sendTask, xmlSender);
					} else {
						if (StringUtils.isNotBlank(sendTask.getRequestURL())
								&& sendTask.getRequestURL().toUpperCase().indexOf("TaoBao14NewServlet") > -1) {
							sendTaskService.sendTaskJG(sendTask, xmlSender);
						}else{
							sendTaskService.sendTask(sendTask, xmlSender);
						}
					}
				}
				taskList.clear();
				// FIXME error-info
				logger.error("end time:" + System.currentTimeMillis()
						+ ",total time"
						+ (System.currentTimeMillis() - startTime) + "ms,"
						+ new Date());
				running = false;
				return;
			} catch (Exception e) {
				running = false;
				logger.error("ToJinGangTimer error", e);
				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("SendTask2JinggangTimer出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
				mailService.sendMail(mail);
			}

        }

    }

    public SendTaskService<SendTask> getSendTaskService() {
        return sendTaskService;
    }

    public void setSendTaskService(SendTaskService<SendTask> sendTaskService) {
        this.sendTaskService = sendTaskService;
    }

    public ServerConfigService<ServerConfig> getServerConfigService() {
        return serverConfigService;
    }

    public void setServerConfigService(ServerConfigService<ServerConfig> serverConfigService) {
        this.serverConfigService = serverConfigService;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit == null || limit.intValue() == 0) {
            limit = DEFAULT_LIMIT;
        }
        this.limit = limit;
    }

    private void stringToList(String string) {
        if (StringUtils.isEmpty(string)) {
            return;
        }
        String[] strings = StringUtils.split(string, ",");
        for (String str : strings) {
            if (StringUtils.isNumeric(str)) {
                taskFlags.add(Integer.valueOf(str));
            }
        }
    }

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

}
