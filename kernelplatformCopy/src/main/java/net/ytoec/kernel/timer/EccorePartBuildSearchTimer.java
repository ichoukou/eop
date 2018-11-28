/**
 * 
 */
package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.search.dto.MailObjectDTO;
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
 * 往build新增订单
 * @author wangyong 2012-02-14
 */
@SuppressWarnings("all")
public class EccorePartBuildSearchTimer extends QuartzJobBean {

    private EccoreSearchService             eccoreSearchService;

    private static int                      limit  = 1000;

    private static boolean                  isRunning = false;

    private static Logger                   logger = LoggerFactory.getLogger(EccorePartBuildSearchTimer.class);

	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    
    private static int threadNum;
    private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;
    List<BuildSearch> sublist=null;
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                  + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                  + "<DIV> ${mailContent}</DIV>"
                                                                  + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                  + "<BR>--------------------------------------------------------------------------------------"
                                                                  + "</BODY></HTML>";

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        try {
            if (isRunning) {
                return;
            }
            logger.error("EccorePartBuildSearchTimer start...");
            isRunning = true;
            final CountDownLatch cdl = new CountDownLatch(threadNum);
            List<BuildSearch> list=eccoreSearchService.getBuildData(limit);
            if(list==null){
            	isRunning = false;
            	return;
            }
            if(list.size()<1000&&list.size()>0){
            	logger.error("EccorePartBuildSearchTimer size <100");
            	eccoreSearchService.newBuildPartEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),list,null);
            	isRunning = false;
            	return;
            }else if(list.size()<=0){
            	logger.error("EccorePartBuildSearchTimer size <=0");
            	isRunning = false;
            	return;
            }
            int subNumber=0,allNumber=list.size();
            
            if(allNumber%threadNum==0){
            	subNumber=allNumber/threadNum;
            }else{
            	subNumber=allNumber/threadNum+1;
            }
            logger.error("subNumber="+subNumber);
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
            	logger.error("sublist.size="+sublist.size());
	            MyThread myThread = new MyThread(){
					@Override
					public void run() {
						try{
							eccoreSearchService.newBuildPartEccoreData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),sublist,null);
							 
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
            list.clear();
            logger.error("EccorePartBuildSearchTimer end...");
			isRunning = false;
			
        } catch (Exception e) {
            logger.error("build error", e);
            StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("EccorePartBuildSearchTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" 
					+ e.getClass() + "\n方法名为：" + ex.getMethodName()+"\n异常行数：" + ex.getLineNumber())+"\n"+e.getMessage());
			mailService.sendMail(mail);
			isRunning = false;
        }

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        EccorePartBuildSearchTimer.limit = limit;
    }


    public EccoreSearchService getEccoreSearchService() {
        return eccoreSearchService;
    }

    public void setEccoreSearchService(EccoreSearchService eccoreSearchService) {
        this.eccoreSearchService = eccoreSearchService;
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
		EccorePartBuildSearchTimer.threadNum = threadNum;
	}
}
