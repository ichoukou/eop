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

import com.techcenter.client.api.IReportReceiver;
import com.techcenter.protocol.standard12.Standard_Head;
import com.techcenter.protocol.standard12.Standard_Report;

public class ReportReceiver implements IReportReceiver {	
	private static final Logger logger = LoggerFactory.getLogger(ReportReceiver.class);
	//待发送短信表
	private SMSObjectService<SMSObject> SMSObjectService=SMSSender.getSMSObjectService();
	//已处理的短信表
    private SMSDeliverService<SMSObject> SMSDeliverService=SMSSender.getSMSDeliverService();
    //开通服务表
    private DredgeServiceService<DredgeService> dredgeServiceService=SMSSender.getDredgeServiceService();
    
    // 发送短信历史记录表    
    private SMSHistoryInfoService<SMSObject> smsHistoryInfoService=SMSSender.getSMSHistoryInfoService();
    private SMSOtherWaitService<SMSObject> smsOtherWaitService=SMSSender.getSMSOtherWaitService();
    
	@Override
	public void receive(Standard_Head obj) {
		    Standard_Report report=(Standard_Report)obj;     // 转换为Standard_Report对象
		    logger.error("=======================回调方法开始ReportReceiverreport:" + report);         // 输出report日志 
		 
			//若report.getSequenceId()>0,则认为是易通平台的短信；否则认为是代运营的短信
		    
			if((int)report.getSequenceId()>0)
			{
				logger.error("易通发送短信回调-------------start");
			    SMSObject sMSObject=new SMSObject();
			    sMSObject.setId((int)report.getSequenceId());
			    sMSObject=SMSObjectService.get(sMSObject);	
			    
			    if(sMSObject==null){
			    	logger.error("待发送短信ec_core_paysms表 id=:" + report.getSequenceId()+"记录已被删除...");   
			    	return;
			    }
				if(report.getState() == 0){
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
				sMSObject.setErrorCode(report.getErrorCode());
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
				 smsInfo.setId((int)report.getSequenceId());
	     		 smsInfo=SMSObjectService.get(smsInfo);	
				 boolean bool=false;
				 if(smsInfo.getSuccTotal()+smsInfo.getFailTotal()==smsInfo.getPkTotal())
				 {
					 //将回调过的短信插入到历史记录表中
					 smsInfo.setId(null);
					 bool = smsHistoryInfoService.add(smsInfo);	
					 if(bool==false)
						 logger.error("从ec_core_paysms插入到发送短信历史记录表:ec_core_paysms_info  id="+(int)report.getSequenceId()+"失败...");
					 
					 //将待发送短信表ec_core_paysms的对应的记录删除掉
					 smsInfo.setId((int)report.getSequenceId());
					 bool = SMSObjectService.remove(smsInfo);
					 if(bool==false)
						 logger.error("删除待发送短信ec_core_paysms表  id="+(int)report.getSequenceId()+"失败...");
				 }
				
				 smsInfo = null;
				 sMSObject=null;
				 logger.error("易通发送短信回调-------------end");
			}
			else
			{
				logger.error("代运营发送短信回调-------------start");
				SMSObject sMSObject=new SMSObject();
				sMSObject.setId((int)report.getSequenceId()*(-1));
				sMSObject=smsOtherWaitService.get(sMSObject);	
				if(sMSObject==null){
				    logger.error("待发送短信ec_core_sms_wait表 id=:" + report.getSequenceId()*(-1)+"记录已被删除...");   
				    return;
			    }
				
				if(report.getState() == 0){
					sMSObject.setSuccTotal(sMSObject.getSuccTotal()+1);
					sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SUCCESS.getValue());
				}else{
					sMSObject.setFailTotal(sMSObject.getFailTotal()+1);
					sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
				}
				
				sMSObject.setErrorCode(report.getErrorCode());
				sMSObject.setDeliverTime(new Date());
				smsOtherWaitService.edit(sMSObject);
				
				/**
				 * 将ec_core_sms_wait的数据转移到ec_core_paysms_info中
				 * 20120919添加
				 */
				SMSObject smsInfo=new SMSObject();
				smsInfo.setId((int)report.getSequenceId()*(-1));
		 		smsInfo=smsOtherWaitService.get(smsInfo);	
			    boolean bool=false;
				if(smsInfo.getSuccTotal()+smsInfo.getFailTotal()==smsInfo.getPkTotal())
				{
					 //将回调过的短信插入到短信历史记录表中
					 smsInfo.setId(null);
					 bool = smsHistoryInfoService.add(smsInfo);	
					 if(bool==false)			
						 logger.error("从ec_core_sms_wait插入到发送短信历史记录表:ec_core_paysms_info  id="+(int)report.getSequenceId()*(-1)+"失败...");
					 
					 //将待发送短信表ec_core_sms_wait的对应的记录删除掉
					 smsInfo.setId((int)report.getSequenceId()*(-1));
					 bool = smsOtherWaitService.remove(smsInfo);
					 if(bool==false)
						 logger.error("删除待发送短信ec_core_sms_wait表  id="+(int)report.getSequenceId()*(-1)+"失败...");
				}
				logger.error("代运营发送短信回调-------------end");
			}
		}
	public DredgeService  compreDredgeService(Integer userId){
		  DredgeService dredgeService=null;
		  DredgeService source_dredgeService=null;
		  Map map=new HashMap();		  
		  map.put("userId", userId);
		  map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
		  map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		  List<DredgeService> dredgeServiceList=
		   dredgeServiceService.getServiceRecordByUserId(map);
			   if(CollectionUtils.isNotEmpty(dredgeServiceList)){
			     dredgeService=dredgeServiceList.get(0);			   
			    }
			  return null;
	}
	
	public boolean  updateSmscount(Integer userId, DredgeService dredgeService,Integer smsCount){	
			    //2.1)当前账号是否开通服务
			   	//2.1.1)短信是否充足(在插入短信的时候已经判断)
			    	 if(dredgeService.getSmsUsecount()>=smsCount){
			    		 dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()-smsCount);
						 dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()+smsCount);
						 dredgeServiceService.edit(dredgeService); 
						return true;
			           }
			   return false;
	}
	
	public boolean  updatePTSmscount(Integer userId,DredgeService dredgeService,Integer smsCount){
				    //2.1)当前账号是否开通服务
				   	//2.1.1)短信是否充足(在插入短信的时候已经判断)
				    	 if(dredgeService.getSmsUsecount()>=smsCount){
				    		 dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()-smsCount);
							 dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()+smsCount);
							 dredgeServiceService.edit(dredgeService); 
							 return true;
				      }
			  return false;
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
