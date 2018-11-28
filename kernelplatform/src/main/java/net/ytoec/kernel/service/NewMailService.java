package net.ytoec.kernel.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;

import freemarker.template.TemplateException;

public interface NewMailService {
	public void sendMail(String to, String template, Map<String, String> data,
			String subject) throws MessagingException,
			UnsupportedEncodingException, IOException, TemplateException;

	/**
	 * 发送邮件方法
	 * 
	 * @param to
	 *            发送至email
	 * @param template
	 *            模板
	 * @param data
	 *            数据源
	 * @param subject
	 *            邮件抬头
	 * @param isHtml
	 *            是否是ＨＴＭＬ格式
	 * @param cc
	 *            抄送
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void sendMail(String to, String template, Map<String, String> data,
			String subject, Boolean isHtml)
			throws MessagingException, UnsupportedEncodingException,
			IOException, TemplateException;

}
