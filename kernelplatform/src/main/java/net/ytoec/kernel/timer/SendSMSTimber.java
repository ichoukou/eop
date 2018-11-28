package net.ytoec.kernel.timer;

import net.ytoec.kernel.common.RegistexTeach;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSObjectService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SendSMSTimber  extends QuartzJobBean{

	private static boolean issRunning = false;
	private static int  limit  = 80;
	private static Logger logger = LoggerFactory.getLogger(SendSMSTimber.class);
	private SMSObjectService<SMSObject> SMSObjectService;
	private static boolean teachRunning = true;
	private static int productId=2401;
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		
		if (issRunning) {
			return;
		}
		issRunning = true;
		 
		if (teachRunning) {
			teachRunning = false;
			logger.error("==================发送短信定时器激活开始");
			RegistexTeach.getInstance();
			logger.error("==================发送短信定时器激活结束");
        }
	}
	
	
    /*public  void sendSMS(){    
    	  Map<String, Object> map = new HashMap<String, Object>();
          map.put("status", TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
  		  map.put("limit", limit);
  		 List<SMSObject> sMSObjectLis=SMSObjectService.getList(map);
         if(CollectionUtils.isNotEmpty(sMSObjectLis)){
        	 for(int i=0;i<sMSObjectLis.size();i++){
        		 SMSObject sMSObject=sMSObjectLis.get(i);
        		 Standard_Submit ssm = new Standard_Submit();         		 
	        		if(sMSObject.getMessageContent().length()>70){
	      					ssm.setMessageFormat((short) 32);
	      				}else{
	      					ssm.setMessageFormat((short) 15);
	      				}
	      				
	      				ssm.setContentString(sMSObject.getMessageContent()); 
	      				int count=sMSObject.getMessageContent().length();
	        				if(count<=70){
	        					sMSObject.setPkTotal(1);
	        				}else{
	        					sMSObject.setPkTotal((count/67+1));
	        				} 
        		
        		
       				ssm.setSrcNumber(""); 
       				ssm.setMessagePriority((short) 3); 
       				ssm.setReportType((short) 1);  	
       				ssm.setSequenceId(sMSObject.getId()); 
       				ssm.setDestMobile(sMSObject.getDestMobile()); 
       				ssm.setProductID(productId);
       				SMSObjectService.addSendSMS(ssm, sMSObject);
        	 }
  				
        	 }
        }   */
	public static int getLimit() {
		return limit;
	}

	public static void setLimit(int limit) {
		SendSMSTimber.limit = limit;
	}

	public SMSObjectService<SMSObject> getSMSObjectService() {
		return SMSObjectService;
	}

	public void setSMSObjectService(SMSObjectService<SMSObject> sMSObjectService) {
		SMSObjectService = sMSObjectService;
	}

	public static int getProductId() {
		return productId;
	}

	public static void setProductId(int productId) {
		SendSMSTimber.productId = productId;
	}
	
}
