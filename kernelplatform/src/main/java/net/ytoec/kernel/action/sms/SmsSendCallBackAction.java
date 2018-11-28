package net.ytoec.kernel.action.sms;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSTemplateService;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 
 *短信模块处发送短信之后回调的action
 *
 *@author wu.sha
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SmsSendCallBackAction extends AbstractActionSupport {

	private static final long serialVersionUID = -6396325181757062681L;
	
	private Logger logger = LoggerFactory.getLogger(SmsSendCallBackAction.class);

	@Inject
	private SMSInfoService smsInfoService;
	
	@Inject
	private SMSTemplateService smsTemplateService;
	
	/**
	 * 短信主键
	 */
	private Integer sequenceID;
	
	/**
	 * 短信成功失败的状态
	 */
	private String status;
	
	/**
	 * 错误码
	 */
	private String errorCode;
	
	/**
	 * 短信发送成功数
	 */
	private Integer succTotal;
	
	/**
	 * 短信发送时间
	 */
	private String sendTime;
	
	/**
	 * 更新短信模块状态是否成功
	 */
	private String isSuccess;
	
	/**
	 * 短信回调方法
	 * @return
	 */
	public String SmsSendCallBack() {
		try {
			SMSInfo smsInfo = null;
			
			if (sequenceID != null) {
				smsInfo = smsInfoService.getSMSInfoById(sequenceID);
				if (smsInfo == null) {
					// 如果数据库中不存在sequenceID对应的记录，则直接删除掉短信队列中对应的！
					return isSuccess = "success";
				}
			} else {
				return isSuccess = "error";
			}
			
			// 发送成功
			if (StringUtils.equals("0", status)) {
				
				if(smsInfo.getCount() != null) {
					// 说明一条短信回调多次了。
					isSuccess = "success";
					return "success";
				} else {
					smsInfo.setCount(succTotal);
					smsInfo.setStatus("0");
					smsInfo.setSendTime(DateUtil.parseDate(sendTime));
					smsInfoService.updateSMSInfoById(smsInfo);
					
					//短信发送成功,更新模版 SEND_COUNT
					Integer smsTemplateId = smsInfo.getSmsTemplateId();
					if(smsTemplateId!=null) {
						Map params = new HashMap(); 
						params.put("id", smsTemplateId);
						params.put("sendCount", succTotal);
						smsTemplateService.updateSendCountById(params); //根据ID更新SEND_COUNT(数据库send_count + 参数 succTotal)
					}
					
					logger.error("短信ID：" + smsInfo.getId() +  ", 使用模版为：" + smsTemplateId);
				}
			} else {
				//  发送失败
				smsInfo.setStatus("2");
				smsInfo.setSendTime(DateUtil.parseDate(sendTime));
				smsInfo.setCount(0);
				
			/*	if (StringUtils.equals("-2", errorCode)) {
					smsInfo.setErrorSend("手机号码错误！");
				} else {
					smsInfo.setErrorSend("系统繁忙！");
				}*/
				smsInfo.setErrorSend(errorCode);
				smsInfoService.updateErrorSendById(smsInfo);
			}
			isSuccess = "success";
		} catch (Exception e) {
			isSuccess = "error";
			logger.error("更新短信明细表出错！", e);
		}
		return "success";
	}

	public SMSInfoService getSmsInfoService() {
		return smsInfoService;
	}

	public void setSmsInfoService(SMSInfoService smsInfoService) {
		this.smsInfoService = smsInfoService;
	}

	public Integer getSequenceID() {
		return sequenceID;
	}

	public void setSequenceID(Integer sequenceID) {
		this.sequenceID = sequenceID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getSuccTotal() {
		return succTotal;
	}

	public void setSuccTotal(Integer succTotal) {
		this.succTotal = succTotal;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	
}
