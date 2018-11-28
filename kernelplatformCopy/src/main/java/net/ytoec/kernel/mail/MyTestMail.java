package net.ytoec.kernel.mail;

import java.io.UnsupportedEncodingException;

public class MyTestMail {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		   //这个类主要是设置邮件  
	      MailSenderInfo mailInfo = new MailSenderInfo();   
	      mailInfo.setMailServerHost("smtp.exmail.qq.com");   
	      mailInfo.setMailServerPort("25");   
	      mailInfo.setValidate(true);   
	      mailInfo.setUserName("yitong@ytoxl.com");   
	      mailInfo.setPassword("yto123");
	      mailInfo.setFromAddress("yitong@ytoxl.com");   
	      mailInfo.setToAddress("1987853317@qq.com");   
	      mailInfo.setSubject("主题");   
	      mailInfo.setContent("内容");   
	         //这个类主要来发送邮件  
	      SimpleMailSender sms = new SimpleMailSender();  
//	      sms.sendTextMail(mailInfo);//发送文体格式   
	      sms.sendHtmlMail(mailInfo);//发送html格式 

	}

}
