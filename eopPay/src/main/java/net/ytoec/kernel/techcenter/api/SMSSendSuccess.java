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

public class SMSSendSuccess {
	
	
	protected static final Logger logger = LoggerFactory.getLogger(SMSSenderCore.class);
	private SMSObjectService<SMSObject> SMSObjectService = SMSSender.getSMSObjectService();//发送短信历史记录表    
	private SMSOtherWaitService<SMSObject> smsOtherWaitService=SMSSender.getSMSOtherWaitService();//待发送短信表（供其他代理商（如：本草堂）使用
    private SMSHistoryInfoService<SMSObject> smsHistoryInfoService=SMSSender.getSMSHistoryInfoService();// 发送短信历史记录表 
    private DredgeServiceService<DredgeService> dredgeServiceService=SMSSender.getDredgeServiceService();//开通服务表
    private SMSDeliverService<SMSObject> SMSDeliverService=SMSSender.getSMSDeliverService(); //已处理的短信表
	
	/**
	 * 短信发送后状态修改
	 * 1：修改短信表 ec_core_paysms表
	 * 2: 短信信息插入EC_CORE_DELIVERSMS(已处理的短信表)
	 * 3：插入ec_core_paysms_info历史记录表
	 * 4：将待发送短信表ec_core_paysms的对应的记录删除掉 
	 * @param
	 * sequenceId : 发送平台(>0 易通平台,<0代运营平台)
	 * status : 短信发送之后返回的状态
	 */
	public void smsStatusUpd(Integer sequenceId,Integer status,String errorCode){
		logger.error("=======================短信已发送，修改短信信息开始====================");
		
		if(sequenceId>0){

			logger.error("易通发送短信回调-------------start");
		    SMSObject sMSObject=new SMSObject();
		    sMSObject.setId(sequenceId);
		    sMSObject=SMSObjectService.get(sMSObject);	
		    
		    if(sMSObject==null){
		    	logger.error("待发送短信ec_core_paysms表 id=:" + sequenceId+"记录已被删除...");   
		    	return;
		    }
			if(status == 0){
				sMSObject.setSuccTotal(sMSObject.getSuccTotal()+1);
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SUCCESS.getValue());
			}else{
				sMSObject.setFailTotal(sMSObject.getFailTotal()+1);
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
				//失败后把短信数据加回来(插入时减相应的数据)
				if(StringUtils.equals(sMSObject.getSmsType(), String.valueOf(PayEnumConstants.SERVICE.SMS.getValue()))){
				      updateAddSmscount(sMSObject.getUserId());//改变作法
				}				
			}			
			sMSObject.setErrorCode(errorCode);
			sMSObject.setDeliverTime(new Date());
			SMSObjectService.edit(sMSObject);
			
			
			if(StringUtils.equals(sMSObject.getSmsType(), String.valueOf(PayEnumConstants.SERVICE.SMS.getValue()))
					&&(sMSObject.getSuccTotal()+sMSObject.getFailTotal()==sMSObject.getPkTotal()))
			{
				  logger.error("往返回状态表中插入数据");
				  sMSObject.setId(null);  				  
				  SMSDeliverService.add(sMSObject);	//1)插入表action回调			 
			}
			   
			 SMSObject smsInfo=new SMSObject();
			 smsInfo.setId(sequenceId);
     		 smsInfo=SMSObjectService.get(smsInfo);	
			 boolean bool=false;
			 if(smsInfo.getSuccTotal()+smsInfo.getFailTotal()==smsInfo.getPkTotal())
			 {
				 //将回调过的短信插入到历史记录表中
				 smsInfo.setId(null);
				 bool = smsHistoryInfoService.add(smsInfo);	
				 if(bool==false)
					 logger.error("从ec_core_paysms插入到发送短信历史记录表:ec_core_paysms_info  id="+sequenceId+"失败...");
				 
				 //将待发送短信表ec_core_paysms的对应的记录删除掉
				 smsInfo.setId(sequenceId);
				 bool = SMSObjectService.remove(smsInfo);
				 if(bool==false)
					 logger.error("删除待发送短信ec_core_paysms表  id="+sequenceId+"失败...");
			 }
			
			 smsInfo = null;
			 sMSObject=null;
			 logger.error("易通发送短信回调-------------end");
		
		}else{

			logger.error("代运营发送短信回调-------------start");
			SMSObject sMSObject=new SMSObject();
			sMSObject.setId(sequenceId*(-1));
			sMSObject=smsOtherWaitService.get(sMSObject);	
			if(sMSObject==null){
			    logger.error("待发送短信ec_core_sms_wait表 id=:" + sequenceId*(-1)+"记录已被删除...");   
			    return;
		    }
			
			if(status == 0){
				sMSObject.setSuccTotal(sMSObject.getSuccTotal()+1);
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SUCCESS.getValue());
			}else{
				sMSObject.setFailTotal(sMSObject.getFailTotal()+1);
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
			}
			
			sMSObject.setErrorCode(errorCode);
			sMSObject.setDeliverTime(new Date());
			smsOtherWaitService.edit(sMSObject);
			
			/**
			 * 将ec_core_sms_wait的数据转移到ec_core_paysms_info中
			 * 20120919添加
			 */
			SMSObject smsInfo=new SMSObject();
			smsInfo.setId(sequenceId *(-1));
	 		smsInfo=smsOtherWaitService.get(smsInfo);	
		    boolean bool=false;
			if(smsInfo.getSuccTotal()+smsInfo.getFailTotal()==smsInfo.getPkTotal())
			{
				 //将回调过的短信插入到短信历史记录表中
				 smsInfo.setId(null);
				 bool = smsHistoryInfoService.add(smsInfo);	
				 if(bool==false)			
					 logger.error("从ec_core_sms_wait插入到发送短信历史记录表:ec_core_paysms_info  id="+sequenceId*(-1)+"失败...");
				 
				 //将待发送短信表ec_core_sms_wait的对应的记录删除掉
				 smsInfo.setId(sequenceId*(-1));
				 bool = smsOtherWaitService.remove(smsInfo);
				 if(bool==false)
					 logger.error("删除待发送短信ec_core_sms_wait表  id="+sequenceId*(-1)+"失败...");
			}
			logger.error("代运营发送短信回调-------------end");
		
		}
	}
	public boolean  updateAddSmscount(Integer userId){	
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
  		 dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()+1);
			 dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()-1);
			 dredgeServiceService.edit(dredgeService); 
			return true;

}
}
