/**
 * 
 */
package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.dataobject.BuildSearch;
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

import com.ytoec.kernel.thread.MyThread;

/**
 * @author wangyong 2012-02-14
 */
@SuppressWarnings("all")
public class EccorePartBuildSearchStatusTimer extends QuartzJobBean {

    private EccoreSearchService             eccoreSearchService;

    private static int                      limit  = 1000;

    private static boolean                  isRunning = false;

    private static Logger                   logger = LoggerFactory.getLogger(EccorePartBuildSearchStatusTimer.class);

	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    
    private static int threadNum;
    private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;
    List<BuildSearchStatusWeightIndex> sublist=null;
    
    private List<List<BuildSearchStatusWeightIndex>> subBuildSearchStatusWeights = new ArrayList<List<BuildSearchStatusWeightIndex>>();
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
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
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        try {
            if (isRunning) {
                return;
            }
            isRunning = true;
            logger.error("EccorePartBuildSearchStatusTimer start...");
            long t1 = System.currentTimeMillis();
            final CountDownLatch cdl = new CountDownLatch(threadNum);
            List<BuildSearchStatusWeightIndex> list=eccoreSearchService.getUpdateBuildData(limit,"1");
            logger.error("查询weight用时"+(System.currentTimeMillis()-t1));
            if(list==null){
            	isRunning = false;
            	return;
            }
            if(list.size()<1000&&list.size()>0){
            	logger.error("EccorePartBuildSearchStatusTimer size <100");
            	eccoreSearchService.buildUpdatePartEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),list,"1");
            	isRunning = false;
            	return;
            }else if(list.size()<=0){
            	logger.error("EccorePartBuildSearchStatusTimer size <=0");
            	isRunning = false;
            	return;
            }
            for (int d = 0; d < threadNum; d++) {
				List<BuildSearchStatusWeightIndex> tempBuildSearchStatusWeightIndexList = new ArrayList<BuildSearchStatusWeightIndex>();
				subBuildSearchStatusWeights.add(tempBuildSearchStatusWeightIndexList);
			}
            int subNumber=0,allNumber=list.size();
            if(allNumber%threadNum==0){
            	subNumber=allNumber/threadNum;
            }else{
            	subNumber=allNumber/threadNum+1;
            }
            long t2 = System.currentTimeMillis();
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
            logger.error("list加载数据用时"+(System.currentTimeMillis()-t2));
            CountDownLatch threadSignal = new CountDownLatch(threadNum);

			// 线程类处理结果集
            BuildSearchStatusThread[] threads = new BuildSearchStatusThread[threadNum];
			for (int d = 0; d < threads.length; d++) {
				BuildSearchStatusThread synWeightThread = new BuildSearchStatusThread(
						subBuildSearchStatusWeights.get(d), threadSignal);

				// 设置线程名称
				synWeightThread.setName("statussub1_" + d);
				threads[d] = synWeightThread;
				logger.error("thread领取subBuildSearchStatus1_" + d + ",size:"
						+ subBuildSearchStatusWeights.get(d).size());

				// 执行线程
				threads[d].start();
			}

			// 等待所有子线程执行完毕
			threadSignal.await();
            /*int subNumber=0,allNumber=list.size();
            
            if(allNumber%threadNum==0){
            	subNumber=allNumber/threadNum;
            }else{
            	subNumber=allNumber/threadNum+1;
            }
            logger.error("EccorePartBuildSearchStatusTimer:subNumber="+subNumber);
            for(int i=0;i<threadNum;i++){
            	if((i+1)*subNumber<=allNumber){
            		sublist=list.subList(i*subNumber,(i+1)*subNumber );
            	}
            	else{
            		 if(i*subNumber>=allNumber){
     	            	 break;
            		 }
     	            sublist=list.subList(i*subNumber,allNumber);
            	}
            	logger.error("EccorePartBuildSearchStatusTimer:sublist.size="+sublist.size());
	            MyThread myThread = new MyThread(){
					@Override
					public void run() {
						try{
							eccoreSearchService.buildUpdatePartEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),sublist,"1");
							 
						}catch (Exception e) {
							logger.error("thread error:", e);
						}finally{
							cdl.countDown();
						}
					}
	            };
	           
	            addThreadPoolTaskExecutor.execute(myThread);
            }
            cdl.await();
            list.clear();*/
            logger.error("EccorePartBuildSearchStatusTimer end...");
			isRunning = false;

        } catch (Exception e) {
            logger.error("build error", e);
            isRunning = false;
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("EccorePartBuildSearchStatusTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" 
						+ e.getClass() + "\n方法名为：" + ex.getMethodName()+"\n异常行数：" + ex.getLineNumber()) + "\n"+ e.getMessage());
			mailService.sendMail(mail);
        }

    }
    
    private class BuildSearchStatusThread extends Thread {

		private List<BuildSearchStatusWeightIndex> buildSearchStatusWeightIndexs;

		private CountDownLatch threadsSignal;
		
		BuildSearchStatusThread(List<BuildSearchStatusWeightIndex> buildSearchStatusWeightIndexs,
				CountDownLatch threadsSignal) {
			this.buildSearchStatusWeightIndexs = buildSearchStatusWeightIndexs;
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			try {
				int k=0,j=0;
				if (buildSearchStatusWeightIndexs.size() > 0) {
					eccoreSearchService.buildUpdatePartEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),buildSearchStatusWeightIndexs,"1");
					 
				}
			} catch (Exception e) {
				logger.error("BuildSearchStatusThread error", e);
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
        EccorePartBuildSearchStatusTimer.limit = limit;
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
		EccorePartBuildSearchStatusTimer.threadNum = threadNum;
	}
}
