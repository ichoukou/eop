package net.ytoec.kernel.techcenter.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.SMSDeliverService;
import net.ytoec.kernel.service.SMSHistoryInfoService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSOtherWaitService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 发送失败类处理
 * @author wmd
 * 2013-03-11
 */
public class SMSSendFailed {

	protected static final Logger logger = LoggerFactory.getLogger(SMSSendFailed.class);
	private SMSObjectService<SMSObject> SMSObjectService=SMSSender.getSMSObjectService();
    private SMSDeliverService<SMSObject> SMSDeliverService=SMSSender.getSMSDeliverService();
    private DredgeServiceService<DredgeService> dredgeServiceService=SMSSender.getDredgeServiceService();
    //发送短信历史记录表    
    private SMSHistoryInfoService<SMSObject> smsHistoryInfoService=SMSSender.getSMSHistoryInfoService();
    //待发送短信表（供其他代理商（如：本草堂）使用
	private SMSOtherWaitService<SMSObject> smsOtherWaitService=SMSSender.getSMSOtherWaitService();
	/**
	 * 短信发送失败处理
	 */
	public void sendMSMFiled(Integer id,String destMobile,String smsType){
		 SMSObject smsObj=new SMSObject();
		 smsObj.setId(id);
		 SMSObject obj = SMSObjectService.get(smsObj);
		 Integer sequenceId = obj.getSequenceID();
		 logger.error("短信发送失败状态 sequenceId为：" + sequenceId);// 发送日志
		//若submit.getSequenceId()>0,则认为是易通平台的短信；否则认为是代运营的短信
		if(sequenceId>0){
			logger.error("易通发送短信平台接收异常-------------start");
			SMSObject sMSObject=new SMSObject();
			sMSObject.setId(id);			
			sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
			sMSObject.setDeliverTime(new Date());
			sMSObject.setErrorCode("平台接收异常");
			sMSObject.setDestMobile(destMobile);
			sMSObject.setSequenceID(sequenceId);
			sMSObject.setUserId(obj.getUserId());
			sMSObject.setMessageContent(obj.getMessageContent());
			sMSObject.setSendMobile(obj.getSendMobile());
			sMSObject.setSendTime(obj.getSendTime());
			sMSObject.setSmsType(obj.getSmsType());
			SMSObjectService.edit(sMSObject);
			sMSObject.setId(null);  
			SMSDeliverService.add(sMSObject);
			//短信数量退回(插入数据短信数量已经减少)
			if(StringUtils.equals(smsType, String.valueOf(PayEnumConstants.SERVICE.SMS.getValue()))){
				 updateAddSmscount(sMSObject.getUserId(),sMSObject.getPkTotal());//改变作法
				}
			
			/*SMSObject smsInfo=new SMSObject();
			smsInfo.setId(id);
     		smsInfo=SMSObjectService.get(smsInfo);*/	
			boolean bool=false;
			
			//将回调过的短信插入到历史记录表中
			obj.setId(null);
			bool = smsHistoryInfoService.add(obj);	
			if(bool==false)
				logger.error("从ec_core_paysms插入到发送短信历史记录表:ec_core_paysms_info  id="+sequenceId+"失败...");
				 
			//将待发送短信表ec_core_paysms的对应的记录删除掉
			obj.setId(id);
			bool = SMSObjectService.remove(obj);
			if(bool==false)
				logger.error("删除待发送短信ec_core_paysms表  id="+sequenceId+"失败...");
			
			logger.error("易通发送短信平台接收异常-------------end");
		}else{
			logger.error("代运营发送短信平台接收异常-------------start");
			SMSObject smsInfo=new SMSObject();
			smsInfo.setId(sequenceId*(-1));			
			smsInfo.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
			smsInfo.setDeliverTime(new Date());
			smsInfo.setErrorCode("平台接收异常");
			smsOtherWaitService.edit(smsInfo);	
			
			//将回调过的短信插入到短信历史记录表中
			 boolean bool=false;
			 smsInfo.setId(sequenceId*(-1));
     		 smsInfo=smsOtherWaitService.get(smsInfo);	
			 smsInfo.setId(null);
			 bool = smsHistoryInfoService.add(smsInfo);	
			 if(bool==false)			
				 logger.error("从ec_core_sms_wait插入到发送短信历史记录表:ec_core_paysms_info  id="+sequenceId*(-1)+"失败...");
			 
			 //将待发送短信表ec_core_sms_wait的对应的记录删除掉
			 smsInfo.setId(sequenceId*(-1));
			 bool = smsOtherWaitService.remove(smsInfo);
			 if(bool==false)
				 logger.error("删除待发送短信ec_core_sms_wait表  id="+sequenceId*(-1)+"失败...");
			 
			 logger.error("代运营发送短信平台接收异常-------------end");
		}
	}
	
	public void  updateAddSmscount(Integer userId,Integer smsCount){	
	      DredgeService dredgeService=null;
		  Map map=new HashMap();		  
		  map.put("userId", userId);
		  map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
		  map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		  List<DredgeService> dredgeServiceList=
			  dredgeServiceService.getServiceRecordByUserId(map);
			   if(CollectionUtils.isNotEmpty(dredgeServiceList)){
			     dredgeService=dredgeServiceList.get(0);	
			   }
		  dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()+smsCount);
		  dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()-smsCount);
		  dredgeServiceService.edit(dredgeService); 

   }
}
