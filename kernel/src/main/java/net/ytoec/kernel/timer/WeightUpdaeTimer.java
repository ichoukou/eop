package net.ytoec.kernel.timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 用来处理运单重量的更新：从金刚系统生成的文件内读取数据 解析后 更新相应记录的重量信息
 * <tr>
 * RomoteTask 由spring 配置调度的频率
 * <tr>
 * 
 * @author mgl
 */
public class WeightUpdaeTimer extends TimerTask {

    private static Logger                 logger     = Logger.getLogger(WeightUpdaeTimer.class);
    private OrderService<Order>           orderService;

    private ConfigCodeService<ConfigCode> configCodeService;

    private static long                   Interval   = 600000;                                  // 间隔时间(单位毫秒)

    private static String                 dateFormat = "yyyy-MM-dd HH:mm:ss";

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


    @Test
    @Override
    public void run() {
        logger.error("-------------重量信息更新开始----------------");
        ConfigCode conf = configCodeService.getConfByKey("weightUpdateTime");
        String end = process(conf.getConfValue(), new Date());
        conf.setConfValue(end);
        configCodeService.editConfig(conf);

        logger.error("-------------重量信息处理完毕----------------");

    }

    public String process(String startTime, Date now) {
        Date startDate = DateUtil.valueof(startTime, dateFormat);
        // 极端结束时间 如果当前时间
        Date endDate = null;
        if (now.getTime() - startDate.getTime() > 5 * Interval) {
            endDate = new Date(startDate.getTime() + 5 * Interval);
        } else if (now.getTime() - startDate.getTime() > Interval) {
            endDate = new Date(startDate.getTime() + Interval);// 计算结束时间
        } else {
            return startTime;
        }

        // 判断时间是否跨天
        endDate = DateUtil.verifyDayInterval(startDate, endDate);
        // 如果计算所得结束时间超过当前时间 则取当前时间
        String endTime = DateUtil.format(DateUtil.min(endDate, now), dateFormat);
        // 数据库分区名称
        String partition = "PAR_" + DateUtil.format(startDate, "yyyy_MM_dd");

        startTime = DateUtil.format(DateUtil.add(DateUtil.valueof(startTime, dateFormat), Calendar.SECOND, -3000 / 1000),
                                    dateFormat);
        logger.info("开始时间:[" + startTime + "]");
        logger.info("结束时间:[" + endTime + "]");
        logger.info("分区表:[" + partition + "]");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JDBCUtilSingle.getInstance().getConnection();
            logger.debug("-------------开始更新重量信息----------------");
            String sql = "select WAYBILL_NO,WEIGH_WEIGHT, INPUT_WEIGHT,VOLUME_WEIGHT,MODIFY_TIME,CREATE_TIME"
                         + " from YTEXP.T_EXP_OP_RECORD_TAKING  partition(" + partition + ") "
                         + "where modify_time>to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ssxff') "
                         + "and modify_time<=to_timestamp('" + endTime
                         + "','yyyy-mm-dd hh24:mi:ssxff') and  STATUS > 0";
            
//            String sql = "select WAYBILL_NO,WEIGH_WEIGHT"
//                + " from YTEXP.T_EXP_OP_RECORD_TAKING  partition(" + partition + ") "
//                + "where modify_time>to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ssxff') "
//                + "and modify_time<=to_timestamp('" + endTime
//                + "','yyyy-mm-dd hh24:mi:ssxff') and  STATUS > 0";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 0, j = 0, k = 0;
            String waybillNo="";
            Float weight=0.0F;
            while (rs.next()) {
                // logger.debug("更新-->运单:" + rs.getString("waybill_no") +"|"+ rs.getTimestamp("modify_time")+
                // "的 重量为:"+rs.getFloat("weigh_weight"));
                weight = rs.getFloat("weigh_weight");
                if (weight ==0 ||weight == null) {
					weight = rs.getFloat("INPUT_WEIGHT");
				}
                waybillNo = rs.getString("waybill_no");
                if (weight ==0 ||weight == null||StringUtils.isEmpty(waybillNo) ) {
                    continue;
                }
                boolean result = orderService.updateOrderWeightByMailNo(waybillNo, weight);
                logger.info("更新-->运单:" + waybillNo + "|" + rs.getTimestamp("modify_time") + "的 重量为:"
                            + weight + "处理结果:" + result);
                if (result) j++;
                else k++;
                i++;
            }
            logger.info("共查询到[" + i + "]条数据  更新成功[" + j + "]更新失败[" + k + "]");
            return endTime;
        } catch (Exception e) {
            logger.error("重量更新出现异常", e);
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("WeightUpdaeTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
            
            return startTime;
        } finally {
            JDBCUtilSingle.free(rs, ps, con);
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
	
    public OrderService<Order> getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService<Order> orderService) {
        this.orderService = orderService;
    }

    public ConfigCodeService<ConfigCode> getConfigCodeService() {
        return configCodeService;
    }

    public void setConfigCodeService(ConfigCodeService<ConfigCode> configCodeService) {
        this.configCodeService = configCodeService;
    }
}
