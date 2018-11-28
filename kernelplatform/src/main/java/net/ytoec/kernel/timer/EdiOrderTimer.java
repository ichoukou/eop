/**
 * EdiOrderTimer.java
 * Created at Oct 7, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.timer;

import java.util.Date;

import net.ytoec.kernel.dao.ApiLogDao;
import net.ytoec.kernel.dataobject.ApiLog;
import net.ytoec.kernel.service.EdiOrderService;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>
 * ClassName: EdiOrderTimer
 * </p>
 * <p>
 * Description: 凡客订单同步Timer
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Oct 7, 2013
 * </p>
 */
public class EdiOrderTimer extends QuartzJobBean {
    /**
     * <p>
     * Field LOG: LOG
     * </p>
     */
    private final static Logger LOG = LoggerFactory.getLogger(EdiOrderTimer.class);

    /**
     * <p>
     * Field isRunning: 对接LCID
     * </p>
     */
    private final static String LCID = "f16e88fe-6344-47e1-8725-3b84a6542f67";

    /**
     * <p>
     * Field isRunning: 线程是否启动
     * </p>
     */
    private static boolean isRunning = false;

    /**
     * <p>
     * Field ediOrderService: 凡客数据同步业务Service
     * </p>
     */
    private EdiOrderService ediOrderService;
    
    /**
     * <p>Field apiLogDao: 日志记录</p>
     */
    private ApiLogDao<ApiLog> apiLogDao;


    /**
     * <p>
     * Title: executeInternal
     * </p>
     * <p>
     * Description: 执行方法
     * </p>
     * 
     * @param jobContext
     * @throws JobExecutionException
     * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
        ApiLog  log  = new ApiLog();   // 日志
        long  startingDate = System.currentTimeMillis();
        log.setCreateTime(new Date()); // 创建时间
        log.setLogType("VANCL_TIMER");
        String content = null;
        try {
            if (!isRunning) {
                isRunning = true;
                LOG.error("VANCL starting..........................");
                log.setRequestMsg(LCID);
                content = ediOrderService.getOrders(LCID);
                log.setResponseMsg(content);
                log.setException(false);
                LOG.error("VANCL orders string:" + content);
                LOG.info("订单信息:" + content);
                LOG.error("VANCL success..........................");
            }
        } catch (Exception e) {
            LOG.error("VANCL synchronous exceptions message:", e);
            LOG.error("error", e);
            log.setException(false);
            log.setExceptionMsg(e.getMessage());
        } finally {
            log.setUsedtime(System.currentTimeMillis()-startingDate); // 接口方法执行时间
            if(StringUtils.isNotBlank(log.getResponseMsg())||log.isException()){
                this.apiLogDao.insertApiLog(log); // 记录日志信息
            }
            isRunning = false;
        }
    }

    /**
     * <p>
     * Description: 凡客数据同步业务Service
     * </p>
     * 
     * @return the ediOrderService
     */
    public EdiOrderService getEdiOrderService() {
        return this.ediOrderService;
    }

    /**
     * <p>
     * Description: 凡客数据同步业务Service
     * </p>
     * 
     * @param ediOrderService the ediOrderService to set
     */
    public void setEdiOrderService(EdiOrderService ediOrderService) {
        this.ediOrderService = ediOrderService;
    }
    
    /**
     * <p>Description: 日志记录</p>
     * @return the apiLogDao
     */
    public ApiLogDao<ApiLog> getApiLogDao() {
        return this.apiLogDao;
    }

    /**
     * <p>Description: 日志记录</p>
     * @param apiLogDao the apiLogDao to set
     */
    public void setApiLogDao(ApiLogDao<ApiLog> apiLogDao) {
        this.apiLogDao = apiLogDao;
    }

}
