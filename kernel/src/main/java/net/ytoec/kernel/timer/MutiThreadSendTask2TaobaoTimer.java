package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.ServerConfig;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SendTaskToTBService;
import net.ytoec.kernel.service.ServerConfigService;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MutiThreadSendTask2TaobaoTimer extends TimerTask {

    private static Logger                     logger             = Logger.getLogger(MutiThreadSendTask2TaobaoTimer.class);
    private static final int                  DEFAULT_LIMIT      = 5000;
    private static final int                  DEFAULT_THREAD_NUM = 5;
    private static boolean                    running            = false;

    private Integer                           limit;
    private Integer                           threadNum;
    private List<SendTaskToTB>                taskList           = new ArrayList<SendTaskToTB>();
    private List<Integer>                     taskFlags          = new ArrayList<Integer>();
    private SendTaskToTBService<SendTask>     sendTaskToTBService;
    private ServerConfigService<ServerConfig> serverConfigService;

    private Integer                           notify             = 0;

    private String                            tomcatId;
    private MailService<Mail>                 mailService;
    private String                            receiver           = "yto_yitong1@163.com";
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp    = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                   + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                   + "<DIV> ${mailContent}</DIV>"
                                                                   + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                   + "<BR>--------------------------------------------------------------------------------------"
                                                                   + "</BODY></HTML>";

    @Override
    public void run() {
        logger.error("mutithread2tb" + Thread.currentThread().getId()+","+running);
        if (!running) {
            logger.error("mutithread2tb" + Thread.currentThread().getId());
            running = true;
            try {

                tomcatId = SystemHelper.TOMCAT_ID;
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
                logger.error("tomcat id:" + tomcatId);
                ServerConfig serverConfig = serverConfigService.getConfigByServerId(Integer.valueOf(tomcatId));
                if (serverConfig == null || serverConfig.getServerId() == null) {
                    logger.error("serverConfig null");
                    running = false;
                    return;
                }

                // 解析tomcat 对应的flag
                stringToList(serverConfig.getTaskFlag());
                logger.info("taskFlag:" + serverConfig.getTaskFlag());

                long startTime = System.currentTimeMillis();
                taskList = sendTaskToTBService.getSendTaskListByFlagsAndLimit(taskFlags, limit);
                taskFlags.clear();

                logger.error("taskList:" + taskList.size() + "," + (System.currentTimeMillis() - startTime) + ","
                             + new Date());
                if (taskList.size() == 0) {
                    running = false;
                    return;
                }
                List<List<SendTaskToTB>> subTaskLists = new ArrayList<List<SendTaskToTB>>();
                for (int i = 0; i < threadNum; i++) {
                    List<SendTaskToTB> tempTaskList = new ArrayList<SendTaskToTB>();
                    subTaskLists.add(tempTaskList);
                }

                int mod = 0;

                for (int i = 0; i < taskList.size(); i++) {
                    SendTaskToTB sendTaskToTB = taskList.get(i);
                    mod = getMod(sendTaskToTB.getTxLogisticId());
                    subTaskLists.get(mod).add(sendTaskToTB);
                }
                taskList = null;

                SendTaskToTbThread[] threads = new SendTaskToTbThread[threadNum];
                for (int i = 0; i < threads.length; i++) {
                    SendTaskToTbThread taskToTbThread = new SendTaskToTbThread(subTaskLists.get(i));
                    taskToTbThread.setName("sub"+i);
                    threads[i] = taskToTbThread;
                    logger.error("thread领取task：subtaskList" + i + ",size:" + subTaskLists.get(i).size());
                    threads[i].start();
                }

            } catch (Exception e) {
                logger.error("MultiThreandToTaoBaoTimer error", e);
                running = false;
                StackTraceElement ex = e.getStackTrace()[0];
                Mail mail = new Mail();
                mail.setSubject("MutiThreadSendTask2TaobaoTimer出现异常！");
                mail.setSendToMail(this.receiver);
                mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass()
                                                                          + "\n异常行数：" + ex.getLineNumber()));
                mailService.sendMail(mail);
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

    public SendTaskToTBService<SendTask> getSendTaskToTBService() {
        return sendTaskToTBService;
    }

    public void setSendTaskToTBService(SendTaskToTBService<SendTask> sendTaskToTBService) {
        this.sendTaskToTBService = sendTaskToTBService;
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

    public Integer getThreadNum() {
        return threadNum;
    }

    @SuppressWarnings("static-access")
    public void setThreadNum(Integer threadNum) {
        if (threadNum == null || threadNum.intValue() == 0) {
            this.threadNum = this.DEFAULT_THREAD_NUM;
        } else {
            this.threadNum = threadNum;
        }
    }

    private int getMod(String logisticId) {
        if (StringUtils.isEmpty(logisticId)) {
            logger.error("Mod failed :" + logisticId + " empty");
            return 0;
        }
        int hashcode = Math.abs(logisticId.hashCode());
        return hashcode % threadNum;

    }

    public Integer getNotify() {
        return notify;
    }

    synchronized public void setNotify() {
        logger.error("notify main thread:" + Thread.currentThread().getName());
        this.notify = notify + 1;
        if (notify >= threadNum) {
            running = false;
        }
    }

    private class SendTaskToTbThread extends Thread {

        private List<SendTaskToTB> sendTaskToTBs;

        SendTaskToTbThread(List<SendTaskToTB> sendTaskToTBs) {
            this.sendTaskToTBs = sendTaskToTBs;
        }

        @Override
        public void run() {
            try {
                XmlSender xmlSender = new XmlSender();
                xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);

                SendTaskToTB sendTaskToTB = null;
                for (int i = 0; i < sendTaskToTBs.size(); i++) {

                    sendTaskToTB = sendTaskToTBs.get(i);
                    xmlSender.setUrlString(sendTaskToTB.getRequestURL());
                    // 设置请求参数.
                    xmlSender.setRequestParams(sendTaskToTB.getRequestParams());
                    sendTaskToTBService.sendTaskToTB(sendTaskToTB, xmlSender);
                }
                sendTaskToTBs = null;

            } catch (Exception e) {
                logger.error("SendTaskToTbThread error", e);
            } finally {
                setNotify();
            }
        }

    }

}
