package net.ytoec.kernel.timer;
import net.ytoec.kernel.common.SMSStatus;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SearchSMSStatusTimber extends QuartzJobBean{
	
		private Logger logger = LoggerFactory.getLogger(SearchSMSStatusTimber.class);
		private static boolean teachRunning = true;
		@Override
		protected void executeInternal(JobExecutionContext context)
				throws JobExecutionException {
			
			 
			if (teachRunning) {
				 logger.error("==================短信发送状态查询定时器开始=========");
				 SMSStatus.getInstance();
				 logger.error("==================短信发送状态查询定时器结束=========");
	        }
		}
	}