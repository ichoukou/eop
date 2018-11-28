package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.SMSConfigURL;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.LogsService;
import net.ytoec.kernel.service.SMSConfigURLService;
import net.ytoec.kernel.service.SMSDeliverService;
import net.ytoec.kernel.service.SMSSendtoOtherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 将已处理的短信发送到其他模块 service
 * @author guolongchao
 * 20120808
 */
@Service
@Transactional
public class SMSSendtoOtherServiceImpl implements SMSSendtoOtherService{

	private static final Logger logger = LoggerFactory.getLogger(SMSSendtoOtherServiceImpl.class);	
	
	@Inject
    private SMSDeliverService<SMSObject>  deliverService;
	
	@Inject
	private SMSConfigURLService<SMSConfigURL>  configURLService;
	@Inject
	private LogsService<Logs> logsService;
	@Override
	public void sendToOther(int limit) {
		if(limit <= 0) {
			return;
		}
		//1)读取已处理的短信表-ec_core_deliverSMS表信息、短信模块url配置表  -ec_core_configURLSMS
        Map<Object,Object> map = new HashMap<Object,Object>();
        map.put("limit", limit);
        List<SMSObject> list=  deliverService.getList(map);
        List<SMSObject> ids=new LinkedList<SMSObject>();
        //2)把信息post给相应模块[根据url传送]
        if(list==null || list.size() <= 0) {
        	logger.error("需要发送的短信数为空！");
        	return;
        }
        
        for(int i = 0;i < list.size(); i++) {
        	SMSObject obj = list.get(i);
        	SMSConfigURL configUrl=new SMSConfigURL();
        	configUrl.setType(obj.getSmsType());
        	configUrl = configURLService.get(configUrl);
        	if(configUrl == null) {
        		throw new RuntimeException("请正确配置ec_core_configcode表中要发送的模块的URL");
        	}
            XmlSender sender =new XmlSender();
	        sender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
			
	        sender.setRequestParams("id="+obj.getId()+"&smsType="+obj.getSmsType()+
	        		"&sequenceID="+obj.getSequenceID()+"&userId="+obj.getUserId()+"&createTime="+obj.getCreateTime()+
	        		"&sendTime="+obj.getSendTime()+"&deliverTime="+obj.getDeliverTime()+"&sendMobile="+obj.getSendMobile()+
	        		"&destMobile="+obj.getDestMobile()+"&pkTotal="+obj.getPkTotal()+"&succTotal="+obj.getSuccTotal()+
	        		"&messageContent="+obj.getMessageContent()+"&status="+obj.getStatus()+
	        		"&errorCode="+obj.getErrorCode()
	        		);
	        
	        sender.setUrlString(configUrl.getConfUrl());
	        String res = sender.send();
	        if(res!=null&&(res.trim().toLowerCase().indexOf("success")>=0||res.trim().toLowerCase().indexOf("true")>=0)) {
	        	//只返回成功"success" 业务才做处理,其他都不做处理
	        	ids.add(obj);
	        }else {
	        	logger.error("error调用"+configUrl.getConfUrl()+"&sequenceID="+obj.getSequenceID()+"返回结果为"+res);
	        }
        }
        if(ids.size()<=0) return;  
		//3)处理后的数据移除掉
        boolean flag = deliverService.removeByIDs(ids);
        if(flag == false) {
        	Logs log = new Logs();
			log.setOperName("短信回调方法删除失败");
			log.setOperType(PayEnumConstants.OPERTYPE.ERROR.getValue());
			log.setUserId(00);
			  StringBuffer sb=new StringBuffer();
			  for(SMSObject sMSObject:ids){
				  sb.append(sMSObject.getSequenceID()+":");
			  }
			log.setRemark("[ids]"+sb.toString());
			logsService.add(log);
        	logger.error("ec_core_deliverSMS删除数据失败 ..." + sb.toString());
        }
	}
	@Override
	public boolean add(Object entity) {
		return false;
	}

	@Override
	public boolean edit(Object entity) {
		return false;
	}

	@Override
	public boolean remove(Object entity) {
		return false;
	}

	@Override
	public Object get(Object entity) {
		return null;
	}

}
