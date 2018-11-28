package net.ytoec.kernel.timer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.MQService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.SendTaskToTBService;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author qixiaobing
 * 
 */
public class JobMonitor extends QuartzJobBean {

    public static Logger logger = LoggerFactory.getLogger(JobMonitor.class);

    private MailService<Mail> mailService;
    private SendTaskService<SendTask> sendTaskService;
    private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;

    private Mail mail;
    private String receiver = "liuchunyan@ytoxl.com;luodian@ytoxl.com;youjianbo@ytoxl.com;linhuajie@ytoxl.com;";
    private int frequency = 5;
    private int taskFromTB = 0;
    private int taskFromJinGang = 0;
    private int taobaoAlarmValue = 2000;
    private int jinGangAlarmValue = 10000;

    // 用来监控build_search表
    // build_search_status_weight_index 表
    // 的数量
    private int addMQDepth = 0;
    private int updateMQDepth = 0;
    private int backoutMQDepth = 0;
    private int numBuildSearch = 100000;

    private static Map<String, String> resultMap = new LinkedHashMap<String, String>();
    private static Integer todayTotalNumber = 0;

    private String nextStarttime;
    private String weightUpdateTime;

    // 创建订单上次更新的实际时间
    private Map<String, String> createOrderNextStarttimes;

    // 更新订单状态上次更新的实际时间
    private Map<String, String> updateOrderStateUpdateTimes;

    private String timeInfosString;
    // 阀值级别 1为一级阀值 2为二级阀值
    private int norm = 1;
    private String normReceiver = "";
    private String ccReceiver = "";// 邮件抄送人
    private int level = 10; // 预警级别
    @Autowired
	public MQService mqService;

    /**
     * 邮件模版
     */
    private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
            + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
            + "<DIV> ${mailContent}</DIV>"
            + "<DIV>阀值标准：<A HREF=\"http://w3.ytoxl.com/confluence/pages/viewpage.action?pageId=14451341\" TARGET=\"_BLANK\">http://w3.ytoxl.com/confluence/pages/viewpage.action?pageId=14451341</A></DIV></DIV>"
            + "<DIV>系统地址：<A HREF=\"http://ec.yto.net.cn\" TARGET=\"_BLANK\">http://ec.yto.net.cn</A></DIV>"
            + "<BR>--------------------------------------------------------------------------------------"
            + "</BODY></HTML>";

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        // TODO Auto-generated method stub
        logger.error("start run job monitor... number:" + todayTotalNumber);
        try {

            // 获取问题件和重量上次更新的实际
            nextStarttime = Resource.getJobMonitorTime("nextStarttime");
            weightUpdateTime = Resource.getJobMonitorTime("weightUpdateTime");

            // 获取订单创建上次更新的实际时间
            createOrderNextStarttimes = new HashMap<String, String>();
            List<ConfigCode> createOrderNextStarttimeList = Resource.getJobMonitorTime1("%__orderCreate_key%");
            for (ConfigCode bean : createOrderNextStarttimeList) {
                createOrderNextStarttimes.put(bean.getConfText() + ":", bean.getConfValue());
            }

            // 获取订单状态更新上次更新的实际时间
            updateOrderStateUpdateTimes = new HashMap<String, String>();
            List<ConfigCode> updateOrderStateUpdateTimeList = Resource.getJobMonitorTime1("%__orderUpdate_key%");
            for (ConfigCode bean : updateOrderStateUpdateTimeList) {
                updateOrderStateUpdateTimes.put(bean.getConfText() + ":", bean.getConfValue());
            }

            timeInfosString = "<br>&nbsp;&nbsp;&nbsp;&nbsp;问题件上次同步时间：" + nextStarttime
                    + "<br>&nbsp;&nbsp;&nbsp;&nbsp;重量更新上次同步时间：" + weightUpdateTime;

            resultMap.put("监控信息汇报：", timeInfosString);
            resultMap.putAll(createOrderNextStarttimes);
            resultMap.putAll(updateOrderStateUpdateTimes);
            
            // 监控solr订单创建MQ 满指定数发邮件
            try{
                logger.error("solr订单创建MQ SOLR.TO.YT.ADD.ORDER !");
                addMQDepth = mqService.getMQDepth(net.ytoec.kernel.common.Constants.SOLR_ADD_ORDER);
            }catch(Exception e){
                logger.error("solr SOLR.TO.YT.ADD.ORDER error! message:"+e);
            }
            
            // 监控solr订单更新MQ 满指定数发邮件
            try{
                logger.error("solr订单更新MQ JG.TO.YT.ORDER!");
                updateMQDepth = mqService.getMQDepth(net.ytoec.kernel.common.Constants.JGCOMMAND);
            }catch(Exception e){
                logger.error("solr JG.TO.YT.ORDER error! message:"+e.getMessage());
            }
            
            // 监控solr回收MQ 满指定数发邮件
            try{
                logger.error("solr回收MQ SOLR.TO.YT.ORDER.RETURN!");
                backoutMQDepth = mqService.getMQDepth(net.ytoec.kernel.common.Constants.SOLR_ADD_ORDER_BACK);
            }catch(Exception e){
                logger.error("solr SOLR.TO.YT.ORDER.RETURN error! message:"+e.getMessage());
            }

            // 监控ec_core_send_task表
            taskFromTB = sendTaskService.countTask();
            logger.error("Search  ec_core_send_tasktoTB !");
            // 监控 ec_core_send_tasktoTB表
            taskFromJinGang = sendTaskToTBService.countTask();

            resultMap.put("<br>&nbsp;&nbsp;&nbsp;&nbsp;solr订单创建增量【SOLR.TO.YT.ADD.ORDER】：", String.valueOf(addMQDepth));
//            resultMap.put("&nbsp;&nbsp;&nbsp;&nbsp;solr状态增量：", String.valueOf(taskBuildSearchStatusWeightIndex));
            resultMap.put("&nbsp;&nbsp;&nbsp;&nbsp;solr订单状态增量【JG.TO.YT.ORDER】：",String.valueOf(updateMQDepth));
            resultMap.put("&nbsp;&nbsp;&nbsp;&nbsp;solr回收队列【SOLR.TO.YT.ORDER.RETURN】：",String.valueOf(backoutMQDepth));
            resultMap.put("问题件和重量更新负责人：游建波<br>订单同步负责人：林华杰<br>增量同步solr负责人：刘春燕<br>&nbsp;&nbsp;&nbsp;&nbsp;推送金刚task表【ec_core_send_task】：", String.valueOf(taskFromTB));
            resultMap.put("&nbsp;&nbsp;&nbsp;&nbsp;推送电商平台task表【ec_core_send_tasktoTB】：", String.valueOf(taskFromJinGang));

            todayTotalNumber++;

            String flag = "";
            /**
             * falg的值标识积压的种类： "1":标识solr订单增量积压, "2":表示订单状态增量积压, "3":表示订单积压,
             * "4"：表示订单反馈状态积压
             * 如果solr订单增量和订单状态增量都积压，则为"12",四个数据全部积压为"1234",其余情况falg值的规则也是如此
             */

            if (addMQDepth >= numBuildSearch) {
                flag = "1";
            }
            if (updateMQDepth >= numBuildSearch) {
                flag = flag + "2";
            }
            if (taskFromTB > taobaoAlarmValue) {
                flag = flag + "3";
            }
            if (taskFromJinGang > jinGangAlarmValue) {
                flag = flag + "4";
            } 
            if (backoutMQDepth >= numBuildSearch) {
                flag = flag + "5";
            }
            StringBuilder subject = new StringBuilder();
            if (StringUtils.contains(flag, "1")) {
                subject.append("solr订单创建增量【SOLR.TO.YT.ADD.ORDER】积压 ；");
            }
            if (StringUtils.contains(flag, "2")) {
                subject.append("solr订单状态增量【JG.TO.YT.ORDER】积压 ；  ");
            }
            if (StringUtils.contains(flag, "3")) {
                subject.append("推送金刚task表【ec_core_send_task表】积压 ； ");
            }
            if (StringUtils.contains(flag, "4")) {
                subject.append("推送电商平台task表【ec_core_send_tasktoTB】积压  ； ");
            }
            if (StringUtils.contains(flag, "5")) {
                subject.append("solr回收队列【SOLR.TO.YT.ORDER.RETURN】积压 ； ");
            }

            // 二级阀值邮件接受添加
            if (addMQDepth > (numBuildSearch * 10)
                    || updateMQDepth > (numBuildSearch * 10) ||  backoutMQDepth > (numBuildSearch * 10) 
                    || taskFromTB > (taobaoAlarmValue * 10) || taskFromJinGang > (jinGangAlarmValue * 10)) {
                norm = 2;
                level = 11;
                subject.append("注意：");
                if (addMQDepth > (numBuildSearch * 10)) {
                    subject.append("solr订单创建增量【SOLR.TO.YT.ADD.ORDER】积压 ； ");
                }
                if (updateMQDepth > (numBuildSearch * 10)) {
                    subject.append("solr订单状态增量【JG.TO.YT.ORDER】积压  ； ");
                }
                if (backoutMQDepth > (numBuildSearch * 10)) {
                    subject.append("solr回收队列【SOLR.TO.YT.ORDER.RETURN】积压  ；");
                }
                if (taskFromTB > (taobaoAlarmValue * 10)) {
                    subject.append("推送金刚task表【ec_core_send_task表】积压 ； ");
                }
                if (taskFromJinGang > (jinGangAlarmValue * 10)) {
                    subject.append("推送电商平台task表【ec_core_send_tasktoTB】积压 ；  ");
                }
                subject.append("超过二级阀值");
            }
            logger.error("todaytotalNumber " + todayTotalNumber + "  frequency " + frequency + "  flag:" + flag + "---");
            logger.error(StringUtils.isEmpty(flag) + "---------" + (todayTotalNumber == frequency));
            if (StringUtils.isEmpty(flag) && todayTotalNumber == frequency) {

                logger.error("yeah! it's the " + frequency + " times today");
                logger.error("order:" + taskFromTB + " orderStatus:" + taskFromJinGang + " solrOrder:" + addMQDepth
                        + " solrOrderStatus:" + updateMQDepth + "solrBackout" + backoutMQDepth);
                todayTotalNumber = 0;
                mail = constructMail(0, resultMap, "", norm);
                mailService.sendMail(mail);
                resultMap.clear();
            }
            if (!StringUtils.isEmpty(flag)) {
                logger.error("order:" + taskFromTB + " orderStatus:" + taskFromJinGang + " solrOrder:" + " solrOrder:" + addMQDepth
                        + " solrOrderStatus:" + updateMQDepth + "solrBackout" + backoutMQDepth);
                mail = constructMail(level, resultMap, subject.toString(), norm);
                mailService.sendMail(mail);
                resultMap.clear();
            }
           
        } catch (Exception e) {
            logger.error("jobMonitor", e.getMessage());
            mail = constructMail(9, resultMap, "查询失败，请检查", norm);
            mailService.sendMail(mail);
        }
        
        logger.error("end run job monitor....");

    }

    private Mail constructMail(int level, Map<String, String> resultMap, String subject, int norm) {

        Mail mail = new Mail();
        if (level == 10) {
            mail.setSubject("【一级警告】" + subject);
        } else if (level == 11) {
            mail.setSubject("【二级警告】" + subject);
        } else if (level == 9) {
            mail.setSubject("【严重警告】" + subject);
        } else {
            mail.setSubject("【正常】没有积压！");
        }
        if (norm == 2) {
            mail.setSendToMail(this.receiver);
            mail.setSendToMail(this.receiver + this.normReceiver);
            mail.setSendCCMail(this.ccReceiver);
        } else {
            mail.setSendToMail(this.receiver);
        }

        StringBuffer sBuffer = new StringBuffer();
        for (Entry<String, String> entry : resultMap.entrySet()) {
            sBuffer.append(entry.getKey() + "&nbsp;" + entry.getValue() + "<br/>");
        }

        String content = mailContentTemp.replace("${mailContent}", sBuffer.toString());
        mail.setContent(content);

        return mail;
    }

    public SendTaskService<SendTask> getSendTaskService() {
        return sendTaskService;
    }

    public void setSendTaskService(SendTaskService<SendTask> sendTaskService) {
        this.sendTaskService = sendTaskService;
    }

    public SendTaskToTBService<SendTaskToTB> getSendTaskToTBService() {
        return sendTaskToTBService;
    }

    public void setSendTaskToTBService(SendTaskToTBService<SendTaskToTB> sendTaskToTBService) {
        this.sendTaskToTBService = sendTaskToTBService;
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getTaskFromJinGang() {
        return taskFromJinGang;
    }

    public void setTaskFromJinGang(int taskFromJinGang) {
        this.taskFromJinGang = taskFromJinGang;
    }

    public int getTaobaoAlarmValue() {
        return taobaoAlarmValue;
    }

    public void setTaobaoAlarmValue(int taobaoAlarmValue) {
        this.taobaoAlarmValue = taobaoAlarmValue;
    }

    public int getJinGangAlarmValue() {
        return jinGangAlarmValue;
    }

    public void setJinGangAlarmValue(int jinGangAlarmValue) {
        this.jinGangAlarmValue = jinGangAlarmValue;
    }

    public int getNumBuildSearch() {
        return numBuildSearch;
    }

    public void setNumBuildSearch(int numBuildSearch) {
        this.numBuildSearch = numBuildSearch;
    }

    public String getNormReceiver() {
        return normReceiver;
    }

    public void setNormReceiver(String normReceiver) {
        this.normReceiver = normReceiver;
    }

    public String getCcReceiver() {
        return ccReceiver;
    }

    public void setCcReceiver(String ccReceiver) {
        this.ccReceiver = ccReceiver;
    }

	public int getAddMQDepth() {
		return addMQDepth;
	}

	public void setAddMQDepth(int addMQDepth) {
		this.addMQDepth = addMQDepth;
	}

	public int getUpdateMQDepth() {
		return updateMQDepth;
	}

	public void setUpdateMQDepth(int updateMQDepth) {
		this.updateMQDepth = updateMQDepth;
	}

	public int getBackoutMQDepth() {
		return backoutMQDepth;
	}

	public void setBackoutMQDepth(int backoutMQDepth) {
		this.backoutMQDepth = backoutMQDepth;
	}

	public MQService getMqService() {
		return mqService;
	}

	public void setMqService(MQService mqService) {
		this.mqService = mqService;
	}
}
