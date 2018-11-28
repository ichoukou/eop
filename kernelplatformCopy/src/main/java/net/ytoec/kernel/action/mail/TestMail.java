package net.ytoec.kernel.action.mail;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.JsonUtil;
import net.ytoec.kernel.mail.MailSenderInfo;
import net.ytoec.kernel.mail.SimpleMailSender;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class TestMail  extends AbstractActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private SimpleMailSender service;
	
	public void sendMail() throws UnsupportedEncodingException{
		//这个类主要是设置邮件  
	    MailSenderInfo mailInfo = new MailSenderInfo();   
	    mailInfo.setMailServerHost("smtp.exmail.qq.com");   
	    mailInfo.setMailServerPort("25");   
	    mailInfo.setValidate(true);   
	    mailInfo.setUserName("yitong@ytoxl.com");   
	    mailInfo.setPassword("yto123");
	    mailInfo.setFromAddress("yitong@ytoxl.com");   
	    mailInfo.setToAddress("1987853317@qq.com");   
	    mailInfo.setSubject("123456");   
	    mailInfo.setContent("中文乱码");   
	       //这个类主要来发送邮件  
	//    sms.sendTextMail(mailInfo);//发送文体格式   
	    Boolean flag=service.sendHtmlMail(mailInfo);//发送html格式 
	    JsonBean jsonBean = new JsonBean();
	    jsonBean.setInfo(flag);
	    String json = JsonUtil.toJson(jsonBean);
		JsonUtil.response(json);
	}
	
	class JsonBean {
		private boolean info;

		public boolean getInfo() {
			return info;
		}

		public void setInfo(boolean info) {
			this.info = info;
		}
	}
}
