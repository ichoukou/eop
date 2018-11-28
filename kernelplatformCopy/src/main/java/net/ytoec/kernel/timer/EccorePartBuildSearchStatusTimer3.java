package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EccorePartBuildSearchStatusTimer3 extends QuartzJobBean {

	private EccoreSearchService eccoreSearchService;

	private static int limit = 1000;

	private static boolean isRunning = false;

	private static Logger logger = LoggerFactory
			.getLogger(EccorePart2BuildSearchStatusTimer.class);

	private MailService<Mail> mailService;
	private String receiver = "yto_yitong1@163.com";

	private static int threadNum;
	private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;
	List<BuildSearchStatusWeightIndex> sublist = null;
	private List<List<BuildSearchStatusWeightIndex>> subBuildSearchStatusWeights = new ArrayList<List<BuildSearchStatusWeightIndex>>();
	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

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

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			if (isRunning) {
				return;
			}
			isRunning = true;
			logger.error("EccorePartBuildSearchStatusTimer3 start...");

			final CountDownLatch cdl = new CountDownLatch(threadNum);
			
			//TODO  获取订单更新结果集
			List<BuildSearchStatusWeightIndex> list = eccoreSearchService
					.getUpdateBuildData2(limit,4);
			
			// 结果集为空的场合
			if (list == null) {
				isRunning = false;
				return;
			}
			
			// 获取结果集size
			int allNumber = list.size();
			
			// 结果集  < 1000的场合
			if (allNumber < 1000 && allNumber > 0) {
				logger.error("EccorePartBuildSearchStatusTimer3 size <1000");
				
				// TODO MAPPER映射的实体类要与BuildSearchStatusWeightIndex一致
				eccoreSearchService.buildUpdatePartEccoreData(ConfigUtilSingle
						.getInstance().getSolrEccoreUrl(), list,"4");
				
				isRunning = false;
				return;
			} else if (allNumber <= 0) {
				logger.error("EccorePartBuildSearchStatusTimer3 size <=0");
				isRunning = false;
				return;
			}
			int subNumber=0;
            if(allNumber%threadNum==0){
            	subNumber=allNumber/threadNum;
            }else{
            	subNumber=allNumber/threadNum+1;
            }
            for (int d = 0; d < threadNum; d++) {
				List<BuildSearchStatusWeightIndex> tempBuildSearchStatusWeightIndexList = new ArrayList<BuildSearchStatusWeightIndex>();
				subBuildSearchStatusWeights.add(tempBuildSearchStatusWeightIndexList);
			}
            List<BuildSearchStatusWeightIndex> tmpList=null;
            for(int i=0;i<threadNum;i++){
            	if((i+1)*subNumber<=allNumber){
            		tmpList=list.subList(i*subNumber,(i+1)*subNumber );
            	}
            	else{
            		 if(i*subNumber>=allNumber){
     	            	 break;
            		 }
            		 tmpList=list.subList(i*subNumber,allNumber);
            	}
            	subBuildSearchStatusWeights.get(i).addAll(tmpList);
            }
            CountDownLatch threadSignal = new CountDownLatch(threadNum);

			// 线程类处理结果集
            BuildSearchStatusThread2[] threads = new BuildSearchStatusThread2[threadNum];
			for (int d = 0; d < threads.length; d++) {
				BuildSearchStatusThread2 synWeightThread = new BuildSearchStatusThread2(
						subBuildSearchStatusWeights.get(d), threadSignal);

				// 设置线程名称
				synWeightThread.setName("statussub3_" + d);
				threads[d] = synWeightThread;
				logger.error("thread领取subBuildSearchStatus3_" + d + ",size:"
						+ subBuildSearchStatusWeights.get(d).size());

				// 执行线程
				threads[d].start();
			}

			// 等待所有子线程执行完毕
			threadSignal.await();
			/*int subNumber = 0; 

			if (allNumber % threadNum == 0) {
				subNumber = allNumber / threadNum;
			} else {
				subNumber = allNumber / threadNum + 1;
			}
			
			logger.error("EccorePart2BuildSearchStatusTimer:subNumber="
					+ subNumber);
			
			for (int i = 0; i < threadNum; i++) {
				if ((i + 1) * subNumber <= allNumber) {
					sublist = list.subList(i * subNumber, (i + 1) * subNumber);
				} else {
					if (i * subNumber >= allNumber) {
						break;
					}
					sublist = list.subList(i * subNumber, allNumber);
				}
				
				logger.error("EccorePart2BuildSearchStatusTimer:sublist.size="
						+ sublist.size());
				
				MyThread myThread = new MyThread() {
					
					@Override
					public void run() {
						try {
							
							// 订单更新数据信息添加到solar中
							eccoreSearchService.buildUpdatePartEccoreData(
									ConfigUtilSingle.getInstance()
											.getSolrEccoreUrl(), sublist,"2");

						} catch (Exception e) {
							logger.error("thread error:", e);
						} finally {
							cdl.countDown();
						}
					}
				};

				addThreadPoolTaskExecutor.execute(myThread);
			}
			
			cdl.await();*/
			list.clear();
			logger.error("EccorePartBuildSearchStatusTimer3 end...");
			isRunning = false;

		} catch (Exception e) {
			logger.error("build error", e);
			isRunning = false;
			StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("EccorePartBuildSearchStatusTimer3出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace(
					"${mailContent}",
					"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n方法名为："
							+ ex.getMethodName() + "\n异常行数："
							+ ex.getLineNumber())
					+ "\n" + e.getMessage());
			mailService.sendMail(mail);
		}

	}
	
	private class BuildSearchStatusThread2 extends Thread {

		private List<BuildSearchStatusWeightIndex> buildSearchStatusWeightIndexs;

		private CountDownLatch threadsSignal;
		
		BuildSearchStatusThread2(List<BuildSearchStatusWeightIndex> buildSearchStatusWeightIndexs,
				CountDownLatch threadsSignal) {
			this.buildSearchStatusWeightIndexs = buildSearchStatusWeightIndexs;
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			try {
				int k=0,j=0;
				if (buildSearchStatusWeightIndexs.size() > 0) {
					eccoreSearchService.buildUpdatePartEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),buildSearchStatusWeightIndexs,"4");
					 
				}
			} catch (Exception e) {
				logger.error("BuildSearchStatusThread3 error", e);
			} finally {
				// 线程结束时计数器减1
				threadsSignal.countDown();
			}
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		EccorePartBuildSearchStatusTimer3.limit = limit;
	}

	public EccoreSearchService getEccoreSearchService() {
		return eccoreSearchService;
	}

	public void setEccoreSearchService(EccoreSearchService eccoreSearchService) {
		this.eccoreSearchService = eccoreSearchService;
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
		EccorePartBuildSearchStatusTimer3.threadNum = threadNum;
	}
}
