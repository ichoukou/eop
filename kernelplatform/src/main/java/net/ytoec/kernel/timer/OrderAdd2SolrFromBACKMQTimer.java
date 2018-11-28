package net.ytoec.kernel.timer;
/**
 * 将收集暂时写SOLR不成功的消息队列中的数据再次写入SOLR
 * */
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

public class OrderAdd2SolrFromBACKMQTimer extends QuartzJobBean {
	private MQService mqService;

	private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;

	private int threadNum;

	private static boolean isRunning = false;

	private static Logger logger = LoggerFactory
			.getLogger(OrderAdd2SolrFromBACKMQTimer.class);
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if (!isRunning) {
			isRunning = true;
			//System.out.println("OrderAdd2SolrFromBACKMQTimer------------");
			try {
				final CountDownLatch cdl = new CountDownLatch(threadNum);
				for (int i = 0; i < threadNum; i++) {
					MyThread myThread = new MyThread() {
						@Override
						public void run() {
							try {
							    mqService.receive4SolrAddBack(Constants.ONLINE_TYPE);
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
				logger.error("OrderAdd2SolrFromBACKMQTimer exception", e);
			} finally {
				isRunning = false;
			}
		}
	}
	public MQService getMqService() {
		return mqService;
	}
	public void setMqService(MQService mqService) {
		this.mqService = mqService;
	}
	public ThreadPoolTaskExecutor getAddThreadPoolTaskExecutor() {
		return addThreadPoolTaskExecutor;
	}
	public void setAddThreadPoolTaskExecutor(
			ThreadPoolTaskExecutor addThreadPoolTaskExecutor) {
		this.addThreadPoolTaskExecutor = addThreadPoolTaskExecutor;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
}
