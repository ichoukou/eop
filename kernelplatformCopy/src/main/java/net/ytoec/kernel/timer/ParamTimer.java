package net.ytoec.kernel.timer;

import java.util.TimerTask;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 定时更新 卖家信息到缓存里
 * @author longyongli 
 *
 */
public class ParamTimer extends TimerTask {

	private static Logger logger=LoggerFactory.getLogger(ParamTimer.class);
	private UserService<User> userService;
	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                                  + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                                  + "<DIV> ${mailContent}</DIV>"
                                                                  + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                                  + "<BR>--------------------------------------------------------------------------------------"
                                                                  + "</BODY></HTML>";

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


    public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}
	@Override
	public void run() {
		// try {
		// logger.info("开始加载卖家用户账户信息");
		//
		//
		// List<User> sellers = userService.getUserListByUserType("1");
		//
		// for (int i = 0; i < sellers.size(); i++) {
		// User user = sellers.get(i);
		// Resource.sellerUserMap.put(user.getTaobaoEncodeKey(),user);
		// }
		//
		// List<User> siters = userService.getUserListByUserType("2");
		// for (int i = 0; i < siters.size(); i++) {
		// User user = siters.get(i);
		// // Resource.siteUserMap.put(user.getUserName(), user);
		// }
		// logger.info("用户信息更新完毕,共有卖家:"+sellers.size()+"网点:"+siters.size()+"账户信息已加载入内存-----------");
		// } catch (Exception e) {
		// // TODO: handle exception
		// StackTraceElement ex = e.getStackTrace()[0];
		// Mail mail = new Mail();
		// mail.setSubject("ParamTimer出现异常！");
		// mail.setSendToMail(this.receiver);
		// mail.setContent(mailContentTemp.replace("${mailContent}",
		// "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" +
		// ex.getLineNumber()));
		// mailService.sendMail(mail);
		// }

	}


	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

}
