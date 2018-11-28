package net.ytoec.kernel.timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 用来处理问题单同步：从金刚数据库内读取数据 同步到核心平台数据库中
 * <tr>
 * RomoteTask 由spring 配置调度的频率
 * <tr>
 * 
 * @author mgl
 */
public class QuestionnaireTimer extends TimerTask {

    private static Logger                       logger                 = Logger.getLogger(QuestionnaireTimer.class);
    private QuestionnaireService<Questionnaire> questionnaireService;
    private OrderService<Order>                 orderService;

    /*
     * 2011-12-08/ChenRen private static final String MAIL_TYPE="TYPE"; private static final String MAIL_NO="MAILNO";
     * private static final String DEAL_TIME="TIME"; private static final String PROVINCE="BPROV"; private static final
     * String CITY="BCITY"; private static final String COUNTRY="BDISTRICT"; private static final String
     * ADDRESS="BADDRESS"; private static final String BRANCK_ID="CODE"; private static final String VIP_NAME="SNAME";
     * private static final String VIP_PHONE="SPHONE"; private static final String BUY_USERNAME="BNAME"; private static
     * final String BUY_USERPHONE="BPHONE"; private static final String FEEDBACK_INFO="STATUS";
     */

    /**
     * 对象表名<br>
     * 在配置文件中配置(kernel.properties)，由spring注入
     */
    private String                              tablename;
    /**
     * timer的启动时间间隔<br>
     * 在配置文件中配置(kernel.properties)，由spring注入
     */
    private int                                 period;

    /** 线程启动标识 */
    private static boolean                      isRunning              = false;
    private static String                       dateFormat             = "yyyy-MM-dd HH:mm:ss";

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

    @Override
    public void run() {
        Connection con = null;
        PreparedStatement ps = null;
        // PreparedStatement delps = null;
        ResultSet rs = null;

        if (!isRunning) {
            try {
                isRunning = true;
                tablename = ConfigUtilSingle.getInstance().getQuestionnaireIssue();
                period=ConfigUtilSingle.getInstance().getQuestionnairePeriod();
                if (StringUtils.isEmpty(tablename)) {
                    logger.error("问题件表名为空! 请检查配置文件是否配置或spring是否注入成功!");
                    isRunning = false;
                    return;
                }

                // 获取缓存中上次存的启动开始时间
                String strStarttime = Resource.getQsnConfig(Resource.QSN_NEXTSTARTTIME);

                // 开始时间
                Date startDate = DateUtil.valueof(strStarttime, dateFormat);
                Date lastEndDate = startDate;
                lastEndDate = DateUtil.add(lastEndDate, Calendar.SECOND, -10000 / 1000);
                strStarttime = DateUtil.format(lastEndDate, dateFormat);
                // 如果发现上次同步时间与当前时间之差大于2倍的period，则提升本次同步跨度的2倍，保证同步的延迟在一个合理的范围
                if (DateUtil.millisecondsInterval(new Date(), startDate) > period * 3) {
                    period = period * 3;
                } else if (DateUtil.millisecondsInterval(new Date(), startDate) < period) {
                    return;
                }
                String partition = "PAR_" + DateUtil.format(startDate, "yyyy_MM_dd");

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.SECOND, period / 1000); // 毫秒/1000
                // 结束时间
                Date endDate = cal.getTime();

                endDate = DateUtil.verifyDayInterval(startDate, endDate);

                String strEndtime = DateUtil.format(endDate, dateFormat);

                logger.info("-------------------开始执行问题件同步----------------------");
                con = JDBCUtilSingle.getInstance().getConnection();
                String sql = "select * from " + tablename + " partition(" + partition
                             + ") where modify_time between to_date('" + strStarttime + "', 'yyyy-MM-dd HH24:mi:ss')"
                             + " and to_date('" + strEndtime + "', 'yyyy-MM-dd HH24:mi:ss')";
                logger.error("问题件同步, SQL:" + sql);
                ps = con.prepareStatement(sql);
                // delps = con.prepareStatement("delete from "+tablename+" where waybill_no=?");
                long t1 = System.currentTimeMillis();

                rs = ps.executeQuery();
                logger.info("query time:" + (System.currentTimeMillis() - t1));
                if (!rs.next()) {
                    // 如果结果集为空，直接更新缓存
                    Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME, strEndtime);
                    logger.error("问题件同步, 没有结果集。不更新问题件, 直接更新缓存时间。");
                    isRunning = false;
                    return;
                }
                /**
                 * 每30分钟会重新build一次Resource.autoNotifyUserMap;
                 * lastNotifyUserMapTime记录的是上次更新Resource.autoNotifyUserMap的时间
                 */
                Date curTime = new Date();
                if(Resource.lastNotifyUserMapTime==0)
                	Resource.initBuildAutoNotifyUser();
                if((curTime.getTime() - Resource.lastNotifyUserMapTime) > 900000)
                	Resource.initBuildAutoNotifyUser();
                questionnaireService.getQuestionnaireFromJIinGang(rs,strEndtime);
            } catch (Exception e) {
                isRunning = false;
                logger.error("问题件同步出错!", e);
                StackTraceElement ex = e.getStackTrace()[0];
                Mail mail = new Mail();
                mail.setSubject("QuestionnaireTimer出现异常！");
                mail.setSendToMail(this.receiver);
                mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass()
                                                                          + "\n异常行数：" + ex.getLineNumber()));
                mailService.sendMail(mail);
            } finally {
                isRunning = false;
                JDBCUtilSingle.free(rs, ps, con);
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

    public QuestionnaireService<Questionnaire> getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService(QuestionnaireService<Questionnaire> questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public OrderService<Order> getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService<Order> orderService) {
        this.orderService = orderService;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

}
