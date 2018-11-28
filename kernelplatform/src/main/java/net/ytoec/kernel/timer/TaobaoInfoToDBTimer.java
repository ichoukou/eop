package net.ytoec.kernel.timer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.TaobaoTask;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SMSBuyersService;
import net.ytoec.kernel.service.TaobaoTaskService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ytoec.kernel.thread.MyThread;

/**
 * 执行淘宝获取订单之后产生任务的定时器！
 * 
 * @author wusha 2012-07-12
 */
@SuppressWarnings("all")
public class TaobaoInfoToDBTimer extends QuartzJobBean {

    private SMSBuyersService  smsBuyersService;

    private MailService<Mail> mailService;

    private TaobaoTaskService taobaoTaskService;

    private ThreadPoolTaskExecutor addThreadPoolTaskExecutor;
    
    public ThreadPoolTaskExecutor getAddThreadPoolTaskExecutor() {
		return addThreadPoolTaskExecutor;
	}

	public void setAddThreadPoolTaskExecutor(
			ThreadPoolTaskExecutor addThreadPoolTaskExecutor) {
		this.addThreadPoolTaskExecutor = addThreadPoolTaskExecutor;
	}

	private static boolean           isRunning       = false;

    /**
     * 邮件模版
     */
    private static String     mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                + "<DIV> ${mailContent}</DIV>"
                                                + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                + "<BR>--------------------------------------------------------------------------------------"
                                                + "</BODY></HTML>";

    private static Logger     logger          = LoggerFactory.getLogger(TaobaoInfoToDBTimer.class);

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	if (!isRunning) {
    		try {
                isRunning = true;
                List<TaobaoTask> taskLists = taobaoTaskService.getTaobaoTaskByUserId("1",null);
                logger.info("taskLists.size() = "+taskLists.size());                
                if (taskLists != null && taskLists.size() > 0) {
                	final CountDownLatch cdl = new CountDownLatch(taskLists.size());
                    for (final TaobaoTask taobaoTask:taskLists) {
                    	MyThread myThread = new MyThread(){
							@Override
							public void run() {
								BufferedReader reader =null;
	                            TaobaoTask taobaoTaskTemp = null;
	                            long start = System.currentTimeMillis();
	                        	try {
		                            logger.info("开始解析淘宝订单数据，taobao_executeInternal(),文件id：" + taobaoTask.getTaskId() + "," + taobaoTask.getEndDate());
		                            File resultFile = new File(ConfigUtilSingle.getInstance().getTAOBAO_MEMBER_UNZIP_URL());
		                            File taskFiles = new File(resultFile, "/" + taobaoTask.getTaskId());
		                            logger.info("order_unzip_path = "+taskFiles.getAbsolutePath());
		                            logger.info("更新淘宝会员信息进入数据库始。。。。,update taobao info into DB");

		                            reader =  new BufferedReader(new FileReader(taskFiles));
	
									String tempString = null;
									int line = 1;
									logger.info("TaobaoInfoToDBTimer =====>begin to read line,"+ taobaoTask.getTaskId()+ ", startTime = "+ System.currentTimeMillis());
									// 一次读入一行，直到读入null为文件结束
									while ((tempString = reader.readLine()) != null) {
										smsBuyersService.taobaoDataToDB(taskFiles,taobaoTask, tempString);
									}
									logger.info("TaobaoInfoToDBTimer =====>end to read line,"+ taobaoTask.getTaskId()+ ", endTime = "+ System.currentTimeMillis());
									reader.close();
	
									// 更新状态为已经插入数据库 即flag = 2；
									logger.info("开始更新状态并删除文件 , 文件id ="+ taobaoTask.getTaskId()+ ",startTime = "+ System.currentTimeMillis());
									taobaoTaskService.updateTaskAndDeleteFile(taobaoTask.getTaskId(), taskFiles);
									logger.info("结束更新状态并删除文件 , 文件id ="+ taobaoTask.getTaskId()+ ",endTime = "+ System.currentTimeMillis());
	
									logger.info("结束解析淘宝订单数据，taobao_executeInternal(),文件id：" + taobaoTask.getTaskId() + ",耗时："
	                                        + (System.currentTimeMillis() - start));
	                                } catch (Exception e) {
	                                	logger.info("出现异常错误,taskId = "+taobaoTask.getTaskId()+",Exception = "+e.getMessage());
	                                    e.printStackTrace();
	                                    // 更新状态   flag = 4,(2013-08-07定义，表示其他错误)
	                                    long start4 = System.currentTimeMillis();
	                                    taobaoTaskTemp = (TaobaoTask) taobaoTaskService.getTaobaoTaskByTaskId(taobaoTask.getTaskId());
	                                    logger.info("taskId:" + taobaoTask.getTaskId() + taobaoTaskTemp);
	                                    taobaoTaskTemp.setFlag("4");
	                                    taobaoTaskService.updateTaobaoTask(taobaoTaskTemp);
	                                    logger.info("更新状态4成功 ,"+taobaoTask.getTaskId()+", 耗时："+(System.currentTimeMillis() - start4));
	                                    
	                                } finally {
	                                    if (reader != null) {
	                                        try {
	                                            reader.close();
	                                        } catch (IOException e1) {
	                                        }
	                                    } 
	                                    cdl.countDown();
	                                }
							}	
                    	};
                    	addThreadPoolTaskExecutor.execute(myThread);
                    }
                    cdl.await();
                }
                isRunning = false;
    		} catch (Exception e) {
	            logger.error("数据处理发生异常:"+e.getMessage());
	            e.printStackTrace();
	            StackTraceElement ex = e.getStackTrace()[0];
	            /**
	             * 产生异常时，发送邮件。
	             */
	            
	            Mail mail = new Mail();
	            mail.setSubject("TaobaoInfoToDBTimer出现异常！");
	            mail.setSendToMail("wusha@ytoxl.com");
	            mail.setContent(mailContentTemp.replace("${mailContent}",
	                                                    "TaobaoInfoToDBTimer执行时出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
	                                                            + ex.getLineNumber()));
	            mailService.sendMail(mail);
	        }finally{
	        	isRunning = false;
	        }
    	}
    }

    public MailService<Mail> getMailService() {
        return mailService;
    }

    public void setMailService(MailService<Mail> mailService) {
        this.mailService = mailService;
    }

    public SMSBuyersService getSmsBuyersService() {
        return smsBuyersService;
    }

    public void setSmsBuyersService(SMSBuyersService smsBuyersService) {
        this.smsBuyersService = smsBuyersService;
    }

    public TaobaoTaskService getTaobaoTaskService() {
        return taobaoTaskService;
    }

    public void setTaobaoTaskService(TaobaoTaskService taobaoTaskService) {
        this.taobaoTaskService = taobaoTaskService;
    }

}
