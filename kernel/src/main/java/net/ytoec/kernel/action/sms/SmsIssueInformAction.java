package net.ytoec.kernel.action.sms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.dataobject.SMSInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.FilterRuleService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 问题件通知
 * @author yangqinghua
 *
 */
@Controller
@Scope("prototype")
public class SmsIssueInformAction extends AbstractActionSupport {

	private static final long serialVersionUID = -6396325181757062681L;
	
	private Logger logger=Logger.getLogger(SmsIssueInformAction.class);

	@Inject
	private FilterRuleService<FilterRule> filterService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private SMSServiceService  smsServiceService;
	
	private final static String SERVICE_NAME = "问题件通知";
	
	private FilterResult result;	//过滤短信返回对象
	private String content;			//短信内容
	private String mailNo;			//运单号
	private String buyName;			//买家名称
	private String buyMobile;		//买家手机号码
	private String resultString;	//响应页面
	
	public String index(){
		logger.info("==进入index");
		
		return "index";
	}
	
	/**
	 * 发送问题件通知
	 * @return
	 */
	public String sendMsg(){
		
		/*logger.info("短信内容："+content);
		
		User curUser = super.readCookieUser();
		Integer userId = curUser.getParentId() == null ? curUser.getId() : curUser.getParentId();
		//判断短信服务是否开启
		boolean f = smsPortService.supplyStatusOpenUp(userId);
		if(!f){
			resultString = "抱歉！您的短信服务尚未开启！";
			return "sendMsg";
		}
		
		//判断问题件通知是否开启
		boolean flag = smsPortService.supplyStatusOpenUp(userId);		
		if(!flag){
			resultString = "抱歉！您的问题件通知服务尚未开启！";
			return "sendMsg";
		}
		
		//过滤短信
		if("".equals(content) || content == null)resultString = "抱歉，您输入的信息为空！";
		else result = filterService.bfilter(content, FilterRule.TYPE_ISSUEREPORT);
		
		if(!result.isvalid()){
			resultString = "抱歉！您输入的信息不合法，请重新输入。";
			return "sendMsg";
		}
		
		// 保存短信记录
		SMSInfo info = new SMSInfo();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", this.SERVICE_NAME);
		params.put("userId", userId);
		Integer sid = smsServiceService.getSMSServiceByServiceName(params).getId();
		info.setSmsTypeId(sid);
		info.setMailNo(mailNo);
		info.setBuyName(buyName);
		info.setBuyLoginName(curUser.getUserName());
		info.setBuyMobile(buyMobile);
		info.setContent(content);
		info.setStatus("1");
		if(curUser.getParentId() != null)info.setUserId(curUser.getParentId());
		info.setCreateTime(new Date());
		info.setCreateUserId(curUser.getId());
		
		int id = 0;
		boolean bb = false;
		try{ 
			boolean bool = smsInfoService.saveMsg(info);
			id = info.getId();
			logger.info("保存短信信息是否成功："+bool+"，生成主键ID="+id);  
		}catch(Exception e){
			logger.error("##保存短信信息异常.异常信息："+e);
			
			resultString = "抱歉！短信发送失败，请稍后再试！";
			return "sendMsg";
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("phone", buyMobile);
		map.put("message", content);
		//调用发送短信接口
		Map resultMap =null; //smsPortService.supplySendSMS(map);//不在以此方法发送[合作方改变]
		if((Integer)resultMap.get("sendId") == 0){
			
			//修改发送数量
			Integer sendCount = (Integer) resultMap.get("sendSMSCount");
			SMSInfo info1 = new SMSInfo();
			info1.setId(id);
			info1.setStatus("0");
			info1.setCount(sendCount);
			info1.setSendTime(new Date());
			boolean b = smsInfoService.updateSMSInfoById(info1);
			
			resultString = "信息发送成功！";
			return "sendMsg";
		}
		else if((Integer)resultMap.get("sendId") == 1){
			resultString = "抱歉！您的短信余额数不足，请充值！";
			bb = true;
		}else{
			resultString = "抱歉！短信发送失败，请稍后再试！";
			bb = true;
		}
		if(bb){
			SMSInfo info1 = new SMSInfo();
			info1.setId(id);
			info1.setErrorSend(resultString);
			info1.setStatus("2");
			info1.setSendTime(new Date());
			boolean b = smsInfoService.updateErrorSendById(info1);
			return "sendMsg";
		}*/
		return null;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getBuyName() {
		return buyName;
	}
	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}
	public String getBuyMobile() {
		return buyMobile;
	}
	public void setBuyMobile(String buyMobile) {
		this.buyMobile = buyMobile;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	
}
