package net.ytoec.kernel.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.SMSDeliverService;
import net.ytoec.kernel.service.SMSHistoryInfoService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSOtherWaitService;
import net.ytoec.kernel.service.SMSUnDealService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 将未正确处理的短信统统转移到短信历史记录表ec_core_paysms_info
 * @author guolongchao
 * 20120919
 */
@Service
@Transactional
public class SMSUnDealServiceImpl implements SMSUnDealService{

	private static final Logger logger = LoggerFactory.getLogger(SMSUnDealServiceImpl.class);	
	
	@Inject
    private SMSHistoryInfoService<SMSObject>  smsHistoryInfoService;
	
	@Inject
	private SMSObjectService<SMSObject>  smsObjectService;
	
	@Inject
	private SMSOtherWaitService<SMSObject> smsOtherWaitService;
	
	@Inject
	private SMSDeliverService<SMSObject>  smsDeliverService;
	
	@Inject
	private DredgeServiceService<DredgeService> dredgeServiceService;
	
	@Override
	public boolean add(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean edit(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean remove(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object get(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void sendToHistoryInfo(int limit,int hours) {
		// TODO Auto-generated method stub
		  Date date=new Date();
		  Map<Object,Object> map = new HashMap<Object,Object>();
	      map.put("limit", limit);
	      map.put("createTime", new Date(date.getTime()-hours*60*60*1000) );	      
	      
	      boolean bool=false;
	      List<SMSObject> list=  smsObjectService.getList(map);
	      for(int i=0;i<list.size();i++)
	      {
	    	  SMSObject sms=list.get(i);
	    	  //将status改为发送失败状态2
	    	  sms.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
	    	  bool = smsObjectService.remove(sms);
	    	  if(bool==false)
				  logger.error("删除ec_core_paysms表  id="+sms.getId()+"失败...");
	    	  
	    	  sms.setId(null);
	    	  bool = smsHistoryInfoService.add(sms);
	    	  if(bool==false)
				  logger.error("从ec_core_paysms插入到发送短信历史记录表:ec_core_paysms_info失败...");
	    	  
	    	  //加入到已处理短信表ec_core_deliverSMS
	    	  if(StringUtils.equals(sms.getSmsType(), String.valueOf(PayEnumConstants.SERVICE.SMS.getValue())))
	    	  {
	    		  bool = smsDeliverService.add(sms);
		    	  if(bool==false)
					  logger.error("从ec_core_paysms插入到ec_core_deliverSMS失败...");
		    	  
		    	  //短信数量退回(插入数据短信数量已经减少)
		    	  updateAddSmscount(sms.getUserId(),sms.getPkTotal());
	    	  }
	      }
	      
	      
	      //代理商(如本草堂)专用待发短信表ec_core_sms_wait    --------------start
	      List<SMSObject> list2=  smsOtherWaitService.getList(map);
	      for(int j=0;j<list2.size();j++)
	      {
	    	  SMSObject sms2=list2.get(j);
	    	  //将status改为发送失败状态2
	    	  sms2.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL.getValue());
	    	  bool = smsOtherWaitService.remove(sms2);
	    	  if(bool==false)
				  logger.error("删除ec_core_sms_wait表  id="+sms2.getId()+"失败...");
	    	  
	    	  sms2.setId(null);
	    	  bool = smsHistoryInfoService.add(sms2);
	    	  if(bool==false)
				  logger.error("从ec_core_sms_wait插入到发送短信历史记录表:ec_core_paysms_info失败...");
	      }
		  //代理商(如本草堂)专用待发短信表ec_core_sms_wait    --------------end
	}
	
	public void  updateAddSmscount(Integer userId,Integer smsCount){	
	      DredgeService dredgeService=null;
		  Map map=new HashMap();		  
		  map.put("userId", userId);
		  map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
		  map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		  List<DredgeService> dredgeServiceList = dredgeServiceService.getServiceRecordByUserId(map);
		  if(CollectionUtils.isNotEmpty(dredgeServiceList)) {
			  dredgeService=dredgeServiceList.get(0);	
			  dredgeService.setSmsUsecount(dredgeService.getSmsUsecount()+smsCount);
			  dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()-smsCount);
			  dredgeServiceService.edit(dredgeService); 
		  }
    }
}
