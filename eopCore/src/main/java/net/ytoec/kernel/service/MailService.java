package net.ytoec.kernel.service;

import java.util.Map;

/**
 * @author ChenRen
 * @date 2011-08-10
 * 
 * @param <T> {@link net.ytoec.kernel.dataobject.Mail}类对象或子类对象
 * 
 */
public interface MailService<T> {

	/**
	 * 发邮件<br>
	 * 该方法使用数据库默认服务器参数发送邮件
	 * 
	 * @param mail
	 * @return @
	 */
	public abstract boolean sendMail(T mail);

	/**
	 * 发邮件<br>
	 * 重载方法. 可自定义服务器参数
	 * 
	 * @param mail
	 * @param smtpMap
	 *            自定义的服务器参数
	 * @return @
	 */
	public abstract boolean sendMail(T mail, Map smtpMap);

}