package net.ytoec.kernel.timer;

import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.service.MQService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ytoec.kernel.thread.MyThread;

/**
 * 金刚订单更新定时器
 * 
 * @author LCY 2013-09-25
 */
@SuppressWarnings("all")
public class JinGangCommandReadFromMQTimer extends QuartzJobBean {

	private MQService mqService;

	private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;

	private int threadNum;

	private static boolean isRunning = false;

	private static Logger logger = LoggerFactory
			.getLogger(JinGangCommandReadFromMQTimer.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		if (!isRunning) {

			isRunning = true;
			try {
				final CountDownLatch cdl = new CountDownLatch(threadNum);
				for (int i = 0; i < threadNum; i++) {
					MyThread myThread = new MyThread() {
						@Override
						public void run() {

							try {
								mqService.receive(Constants.JGCOMMAND);
							} catch (Exception e) {
								logger.error("mq receive exception", e);
							} finally {
								cdl.countDown();
							}
						}
					};
					addThreadPoolTaskExecutor.execute(myThread);
				}
				cdl.await();
				isRunning = false;
			}

			catch (Exception e) {
				logger.error("OrderReadFromMQTimer exception", e);
			} finally {
				isRunning = false;
			}
		}
	}

	public ThreadPoolTaskExecutor getAddThreadPoolTaskExecutor() {
		return addThreadPoolTaskExecutor;
	}

	public void setAddThreadPoolTaskExecutor(
			ThreadPoolTaskExecutor addThreadPoolTaskExecutor) {
		this.addThreadPoolTaskExecutor = addThreadPoolTaskExecutor;
	}

	public MQService getMqService() {
		return mqService;
	}

	public void setMqService(MQService mqService) {
		this.mqService = mqService;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

}
