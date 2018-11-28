package net.ytoec.kernel.common;

import net.ytoec.kernel.techcenter.api.SMSSearchCore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 短信状态查询类
 * @author hufei
 *
 */
public class SMSStatus {
 
	private static SMSStatus smsStatus = null;
	private static final Logger logger = LoggerFactory.getLogger(SMSStatus.class);
	private static SMSSearchCore smsSearch = new SMSSearchCore();
	public synchronized static SMSStatus getInstance() {
		if(smsStatus == null){
			logger.error("==========进入短信查询功能 开始===========");
			smsSearch.smsStatusSearch();
			logger.error("==========进入短信查询功能结束===========");
		}
		return smsStatus;
	}
	
}
