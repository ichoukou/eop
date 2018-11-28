package net.ytoec.kernel.timer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来处理运单重量的更新：从金刚系统生成的文件内读取数据 解析后 更新相应记录的重量信息
 * <tr>
 * RomoteTask 由spring 配置调度的频率
 * <tr>
 * 
 * @author mgl
 */
public class WeightUpdaeTimer extends TimerTask {

    private static Logger                 logger     = LoggerFactory.getLogger(WeightUpdaeTimer.class);
    private OrderService<Order>           orderService;
    /** 线程启动标识 */
	private static boolean isRunning = false;
    private ConfigCodeService<ConfigCode> configCodeService;

    private static long                   Interval   = 120000;                                  // 间隔时间(单位毫秒)

    private static String                 dateFormat = "yyyy-MM-dd HH:mm:ss";

    private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    
    private Integer threadNum;
    private List<List<WeightInfo>> subWeightInfos = new ArrayList<List<WeightInfo>>();
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
    	if (!isRunning) {
			isRunning = true;
	        ConfigCode conf = configCodeService.getConfByKey("weightUpdateTime");
	        logger.error("-------------重量信息更新开始----------------" + conf.getConfValue());
	        String end = process(conf.getConfValue(), new Date());
	        conf.setConfValue(end);
	        configCodeService.editConfig(conf);
	        logger.error("-------------重量信息处理完毕----------------" + end);
	        isRunning=false;
        }
    }

    public String process(String startTime, Date now) {
        Date startDate = DateUtil.valueof(startTime, dateFormat);
        if (DateUtil.millisecondsInterval(new Date(), startDate) < 300000*24) {
			isRunning = false;
			return startTime;
		}
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
            int i = 0;// j = 0, k = 0;
            String waybillNo="";
            Float weight=0.0F;
            for (int d = 0; d < threadNum; d++) {
				List<WeightInfo> tempWeightInfoList = new ArrayList<WeightInfo>();
				subWeightInfos.add(tempWeightInfoList);
			}
            int num = 0;
			int mod = 0;
            while (rs.next()) {
                // logger.debug("更新-->运单:" + rs.getString("waybill_no") +"|"+ rs.getTimestamp("modify_time")+
                // "的 重量为:"+rs.getFloat("weigh_weight"));
                weight = rs.getFloat("weigh_weight");
                if (weight ==0 ||weight == null) {
					weight = rs.getFloat("INPUT_WEIGHT");
				}
                Date createtime=rs.getDate("CREATE_TIME");
                Calendar cal = Calendar.getInstance();
				cal.setTime(createtime);
				cal.add(Calendar.DATE, -3); // 毫秒/1000
				// 结束时间
				Date starttime = cal.getTime();
				cal.setTime(createtime);
				cal.add(Calendar.DATE, +3);
				Date endtime = cal.getTime();
				
                waybillNo = rs.getString("waybill_no");
                if (weight ==0 ||weight == null||StringUtils.isEmpty(waybillNo) ) {
                    continue;
                }
                WeightInfo weightInfo=new WeightInfo();
                weightInfo.setWaybillNo(waybillNo);
                weightInfo.setWeight(weight);
                weightInfo.setEndtime(endtime);
                weightInfo.setStarttime(starttime);
                mod = num % threadNum;
                subWeightInfos.get(mod).add(weightInfo);
				num++;
                /*boolean result = orderService.updateOrderWeightByMailNo(waybillNo, weight);
                logger.info("更新-->运单:" + waybillNo + "|" + rs.getTimestamp("modify_time") + "的 重量为:"
                            + weight + "处理结果:" + result);
                if (result) j++;
                else k++;*/
                i++;
                
            }
            CountDownLatch threadSignal = new CountDownLatch(threadNum);

			// 线程类处理结果集
            SynWeightThread[] threads = new SynWeightThread[threadNum];
			for (int d = 0; d < threads.length; d++) {
				SynWeightThread synWeightThread = new SynWeightThread(
						subWeightInfos.get(d), threadSignal);

				// 设置线程名称
				synWeightThread.setName("weightsub" + d);
				threads[d] = synWeightThread;
				logger.error("thread领取task：subWeightList" + d + ",size:"
						+ subWeightInfos.get(d).size());

				// 执行线程
				threads[d].start();
			}

			// 等待所有子线程执行完毕
			threadSignal.await();
            //logger.error("共查询到[" + i + "]条数据  更新成功[" + j + "]更新失败[" + k + "]");
            return endTime;
        } catch (Exception e) {
            logger.error("重量更新出现异常", e);
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("WeightUpdaeTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
			isRunning=false;
            return startTime;
        } finally {
            JDBCUtilSingle.free(rs, ps, con);
            
            for(List<WeightInfo> tempWeightInfoList:subWeightInfos){
            	tempWeightInfoList.clear();
            }
            subWeightInfos.clear();
            subWeightInfos= new ArrayList<List<WeightInfo>>();
            logger.error("重量更新结束");
            isRunning=false;
        }

    }
    
    private class WeightInfo{
    	private String waybillNo;
    	private float weight;
    	private Date starttime;
    	private Date endtime;
		public String getWaybillNo() {
			return waybillNo;
		}
		public void setWaybillNo(String waybillNo) {
			this.waybillNo = waybillNo;
		}
		public float getWeight() {
			return weight;
		}
		public void setWeight(float weight) {
			this.weight = weight;
		}
		public Date getStarttime() {
			return starttime;
		}
		public void setStarttime(Date starttime) {
			this.starttime = starttime;
		}
		public Date getEndtime() {
			return endtime;
		}
		public void setEndtime(Date endtime) {
			this.endtime = endtime;
		}
		 
    	
    }
    
    private class SynWeightThread extends Thread {

		private List<WeightInfo> WeightInfos;

		private CountDownLatch threadsSignal;
		
		SynWeightThread(List<WeightInfo> WeightInfos,
				CountDownLatch threadsSignal) {
			this.WeightInfos = WeightInfos;
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			try {
				int k=0,j=0;
				if (WeightInfos.size() > 0) {
					
					for (int i = 0; i < WeightInfos.size(); i++) {
						WeightInfo weightInfo = WeightInfos.get(i);
						//orderService.updateOrderWeightByMailNo(weightInfo.getWaybillNo(), weightInfo.getWeight());
						boolean result = orderService
								.updateOrderWeightByMailNo(
										weightInfo.getWaybillNo(),
										weightInfo.getWeight(),
										weightInfo.getStarttime(),
										weightInfo.getEndtime());
		                logger.info("更新-->运单:" + weightInfo.getWaybillNo()  + "的 重量为:"
		                            + weightInfo.getWeight() + "处理结果:" + result);
		                if (result) j++;
		                else k++;
					}
				}
				logger.error(this.getName()+"共处理" + WeightInfos.size() + "条数据  更新成功" + j + "更新失败" + k + "");
			} catch (Exception e) {
				logger.error("SynWeightThread error", e);
			} finally {
				// 线程结束时计数器减1
				threadsSignal.countDown();
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

	public Integer getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(Integer threadNum) {
		this.threadNum = threadNum;
	}
}
