package net.ytoec.kernel.timer;

import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.service.JgWaybillService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DelJGWaybillFromJGTimer extends QuartzJobBean {
	private static Logger logger = LoggerFactory
			.getLogger(DelJGWaybillFromJGTimer.class);
	 
	private static boolean isRunning = false;
	 
	// 默认线程数
	private static final int DEFAULT_THREAD_NUM = 5;
	
	private int limit=10000;

	private JgWaybillService<JgWaybill> jgWaybillService;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if (!isRunning) {
			isRunning = true;
			Thread.currentThread().setName("thread.createOrderTimer");
			long t1 = System.currentTimeMillis();
			 
			try {
				logger.error("----删除jgwaybill垃圾数据开始----");
				jgWaybillService.delJgWaybillFromJG(limit);
				logger.error("----删除jgwaybill垃圾数据结束----");
				 
			} catch (Exception e) {
				isRunning = false;
				logger.error("删除jgwaybill垃圾数据出错！", e);
			} finally {
				logger.error("删除jgwaybill垃圾数据finally");
				isRunning = false;
			}
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public JgWaybillService<JgWaybill> getJgWaybillService() {
		return jgWaybillService;
	}

	public void setJgWaybillService(JgWaybillService<JgWaybill> jgWaybillService) {
		this.jgWaybillService = jgWaybillService;
	}

	 
}
