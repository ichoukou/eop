package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.ytoec.kernel.dao.ConfigCodeDao;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮件接口实现类
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
@Service
@Transactional
@SuppressWarnings("all")
public class MailServiceImpl<T extends Mail> implements MailService<T> {

	private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	private static Map confMap;
	private static Map paramMap;
	@Inject
	private ConfigCodeDao<ConfigCode> confDao;

	@Override
	public boolean sendMail(T mail) {

		if (mail == null) {
			logger.error("mail is empity");
			return false;
		}
		if (paramMap == null) {
			paramMap = new HashMap();
			paramMap.put("confKey", "smtp");
			paramMap.put("confType", "2");
		}
		if (confMap == null) {
			confMap = new HashMap();
			
			// 根据key/value完全匹配查找父配置项
			List<ConfigCode> pConfList = confDao.getConfByKeyAndType(paramMap);
			if(pConfList.size() < 1) {
				logger.error("没有匹配的配置信息!/r/n参数信息：[smtp]");
			}
			
			// 所有smtp的字配置项
			List<ConfigCode> list = confDao.getConfByPid(pConfList.get(0).getId());
			for (ConfigCode conf : list) {
				confMap.put(conf.getConfKey(), conf.getConfValue());
			}
		}	

		return sendMail(mail, confMap);
	} // sendMail

	@Override
	public boolean sendMail(T mail, Map smtpMap) {

		if (mail == null) {
			logger.error("mail is empity");
			return false;
		}
		if (smtpMap == null) {
			logger.error("smtpMap is empity");
			return false;
		}
		boolean flag = false;
		Transport transport = null;
		try {
			Properties props = new Properties();
			String _username = null;
			String _pwd = null;
			Set smtpSet = smtpMap.entrySet();
			for (Iterator smtpConf = smtpSet.iterator(); smtpConf.hasNext();) {
				Map.Entry<String, String> entMap = (Map.Entry) smtpConf.next();
				
				// 用户名和密码也配置在DB中, 创建连接对象要用, 这里单独拿出来用变量存储
				if("username".equalsIgnoreCase(entMap.getKey())) {
					_username = entMap.getValue();
					if(_username.indexOf("gmail") > 0) {
						// Gmail邮箱登陆帐号是@符号前那一段字符
						_username = _username.substring(0, _username.indexOf("@"));
					}
					continue;
				}
				if("password".equalsIgnoreCase(entMap.getKey())) {
					_pwd = entMap.getValue();
					continue;
				}
				// senderMail和senderText要单独设置mail的属性
				if("senderMail".equalsIgnoreCase(entMap.getKey())) {
					mail.setFromMail(entMap.getValue());
				}
				if("senderText".equalsIgnoreCase(entMap.getKey())) {
					mail.setFromMailText(entMap.getValue());
				}
				
				props.put(entMap.getKey(), entMap.getValue());
			}

			Session session = Session.getDefaultInstance(props);
			Address from_address = new InternetAddress(mail.getFromMail(), mail.getFromMailText());
			//session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(from_address);
            String[] toAddress = StringUtils.split(mail.getSendToMail(), ";");
            for (String string : toAddress) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(string.trim()));
            }
            if (!StringUtils.isEmpty(mail.getSendCCMail())) {
                String[] ccAddress = StringUtils.split(mail.getSendCCMail(), ";");
                for (String string : ccAddress) {
                	message.addRecipient(Message.RecipientType.CC, new InternetAddress(string.trim()));
    			}
			}
//            BASE64Encoder base64 = new BASE64Encoder();
//			message.setSubject("=?GB2312?B?"+base64.encode((mail.getSubject()).getBytes())+"?=");
//          message.setSubject(base64.encode((mail.getSubject()).getBytes()));//
//          message.setSubject( MimeUtility.encodeText(mail.getSubject()));//
//          message.setSubject(MimeUtility.encodeText(mail.getSubject(),"UTF-8","B"));
            message.setSubject(mail.getSubject()); 
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mail.getContent(), "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			message.saveChanges();
			transport = session.getTransport("smtp");
			transport.connect(_username, _pwd);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			flag = true;
		}
		catch (Exception e) {
			flag = false;
			logger.error(e.getMessage());
		}finally {
			try {
				if (transport != null) {
					transport.close();
					transport = null;
				}
			} catch (MessagingException e) {
				
				logger.error(e.getMessage());
			}
		}

		return flag;
	}

}
/**
插入Mail配置信息
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (0, NULL, 'username', 'yto.ec.core@gmail.com', '发送mail的帐号', 'smtp', NULL, '这个key不能改动');
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (1, NULL, 'password', 'whY70.1c.xioure', 'mail密码', 'smtp', NULL, '这个key不能改动');
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (2, NULL, 'mail.smtp.host', 'smtp.gmail.com', NULL, 'smtp', NULL, NULL);
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (3, NULL, 'mail.smtp.auth', 'true', NULL, 'smtp', NULL, NULL);
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (4, NULL, 'mail.smtp.port', '465', NULL, 'smtp', NULL, NULL);
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (5, NULL, 'mail.smtp.socketFactory.class', 'javax.net.ssl.SSLSocketFactory', NULL, 'smtp', NULL, NULL);
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (6, NULL, 'mail.smtp.socketFactory.fallback', 'false', '', 'smtp', NULL, NULL);
INSERT INTO `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`) VALUES (7, NULL, 'mail.smtp.socketFactory.port', '465', NULL, 'smtp', NULL, NULL);

@ 2011-11-07/ChenRen
@ 邮件服务器参数配置修改SQL
@ 注意：执行SQL后要修改pid
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (NULL, 'smtp', 'smtp', '邮件服务器参数配置', '2', 1, '邮件服务器参数配置-父节点', 1);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'senderMail', 'mike@test.yto56.com.cn', '发件人mail', '1', 1, NULL, 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'senderText', '[圆通]系统邮件', '发件人显示名称', '1', 1, NULL, 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'username', 'mike', '发送邮件的登录帐号', '1', 1, NULL, 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'password', '123456', '邮箱帐号密码', '1', 1, NULL, 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'mail.smtp.host', '10.1.198.71', '邮件服务器', '1', 1, NULL, 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'mail.smtp.auth', 'true', NULL, '1', 1, NULL, 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) VALUES (1, 'mail.smtp.port', '25', NULL, '1', 1, NULL, 0);

 */
