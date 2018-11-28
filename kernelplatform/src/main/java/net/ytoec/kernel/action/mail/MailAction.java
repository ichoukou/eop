package net.ytoec.kernel.action.mail;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 邮件发送action
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
@Controller
@Scope("prototype")
public class MailAction extends ActionSupport {

	// === fields ===
	@Inject
	private MailService<Mail> service;
	private Mail mail;

	private static final long serialVersionUID = 4236432083531136430L;
	private String result;

	// === getter && setter ===
	public String getResult() {
		return result;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	// === actions ===
	/**
	 * 邮件发送的Action接口<br>
	 * 访问该方法的URL: <code>mail!sendMail.action</code><br>
	 * 该方法为ajax请求. 返回json格式信息.<br>
	 * 使用方式：见方法内注释
	 */
	public String sendMail() {
		/*
		 // JS Code
		 $("#x").click(function() {
				$.ajax({
					type:	"POST",
					url:	"mail!sendMail.action",
					dataType:"json",
					success:function(data) {
						alert(data.result);
					},	// textStatus
					error:  function(XMLHttpRequest, statusText, errorThrown) {
						alert("发送失败!\r\n" + "错误信息: " + XMLHttpRequest.statusText || errorThrown);
					}
				});
			});
			
		  // HTML Code
		  <a href="javascript:;" id="x" >[邮件测试]</a>
		 */
		mail = new Mail();
		mail.setFromMail("yto.eccore@gmail.com");
		mail.setFromMailText("　x");
		mail.setSendToMail("xx@163.com");
		mail.setSubject("你好. 邮件测试.");
		mail.setContent("<b>内容...<b>显示名称:　x.");

		boolean flag = service.sendMail(mail);
		if (flag) {
			result = "发送成功!";
		} else {
			result = "发送失败!";
		}
		return "jsonResult";
	}
}
