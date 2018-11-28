package net.ytoec.kernel.techcenter.api;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.SMSDeliverService;
import net.ytoec.kernel.service.SMSHistoryInfoService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSOtherWaitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.techcenter.protocol.standard12.Standard_Submit;

public class SMSSender {
	private static final Logger logger = LoggerFactory
	.getLogger(SMSSender.class);
	private static SMSSender smsSender = null;
	
	private static SMSObjectService<SMSObject> SMSObjectService=null;
	
	private static SMSDeliverService<SMSObject> SMSDeliverService=null;
	
	private static DredgeServiceService<DredgeService> dredgeServiceService=null;
	
	private static SMSHistoryInfoService<SMSObject> SMSHistoryInfoService=null;
	
	private static  Properties properties=null;
	private ConcurrentLinkedQueue<Standard_Submit> abq;
	
	/**
	 * 待发送短信表（供其他模块使用，不受定时器发送时间的限制）
	 */
	private static  SMSOtherWaitService<SMSObject> SMSOtherWaitService=null;
	
	
	private SMSSender() {
		abq=new ConcurrentLinkedQueue<Standard_Submit>(); 
	}

	public static SMSSender getInstance() {
		if (smsSender == null) {
			logger.error("smsSender为null 不是同一个进程");
			smsSender = new SMSSender();
		}
		return smsSender;
	}

	public static Properties  getPropertiesInstance() {
		if (properties == null) {			
			try {
				properties = PropertiesLoaderUtils.loadAllProperties("config.properties");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	//第二种方案

	public Standard_Submit remove() {
		 synchronized(abq) { 
			 Standard_Submit ssm=null;
				if(abq!=null){
					ssm=abq.poll();
					if(ssm!=null){
						logger.error("==================================把值remove;"+ssm.getSequenceId());
					}
				}
				return ssm; 
		 }
		
	}

	public boolean add(Standard_Submit ssm) {
		synchronized(abq) { 
		boolean flag=false;
		if(abq!=null){
			flag=abq.add(ssm);
			logger.error("==================================把值放到发送list里面"+ssm.getSequenceId());
		}
		return flag;
		}
		
	}
	public ConcurrentLinkedQueue<Standard_Submit> getList() {
		return abq;
	}

	public static SMSObjectService<SMSObject> getSMSObjectService() {
		return SMSObjectService;
	}

	public void setSMSObjectService(
			SMSObjectService<SMSObject> sMSObjectService) {
		SMSObjectService = sMSObjectService;
	}

	public static SMSDeliverService<SMSObject> getSMSDeliverService() {
		return SMSDeliverService;
	}

	public  void setSMSDeliverService(
			SMSDeliverService<SMSObject> sMSDeliverService) {
		SMSDeliverService = sMSDeliverService;
	}

	public static DredgeServiceService<DredgeService> getDredgeServiceService() {
		return dredgeServiceService;
	}

	public  void setDredgeServiceService(
			DredgeServiceService<DredgeService> dredgeServiceService) {
		SMSSender.dredgeServiceService = dredgeServiceService;
	}

	public static SMSHistoryInfoService<SMSObject> getSMSHistoryInfoService() {
		return SMSHistoryInfoService;
	}

	public  void setSMSHistoryInfoService(
			SMSHistoryInfoService<SMSObject> sMSHistoryInfoService) {
		SMSHistoryInfoService = sMSHistoryInfoService;
	}

	public static SMSOtherWaitService<SMSObject> getSMSOtherWaitService() {
		return SMSOtherWaitService;
	}

	public  void setSMSOtherWaitService(
			SMSOtherWaitService<SMSObject> sMSOtherWaitService) {
		SMSOtherWaitService = sMSOtherWaitService;
	}
}
