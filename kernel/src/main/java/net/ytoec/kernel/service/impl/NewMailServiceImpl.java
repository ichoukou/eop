//package net.ytoec.kernel.service.impl;
//
//import java.io.IOException;
//import java.io.StringWriter;
//import java.io.UnsupportedEncodingException;
//import java.io.Writer;
//import java.util.Map;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//import net.ytoec.kernel.common.CommonConstants;
//import net.ytoec.kernel.service.NewMailService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
//
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//
//@Service
//@Transactional
//public class NewMailServiceImpl implements NewMailService {
//
//	private static final String UTF8 = "UTF-8";
//
//	@Autowired
//	private JavaMailSender javaMailSender;
//	
//	@Value("${mail.sender.email}")
//	private String senderEmail;
//	@Value("${mail.sender.name}")
//	private String senderName;
//
//	@Autowired
//	private FreeMarkerConfigurer freeMarkerConfigurer;
//
//	@Override
//	public void sendMail(String to, String template, Map<String, String> data,
//			String subject) throws MessagingException,
//			IOException, TemplateException {
//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
//				UTF8);
//		messageHelper.setFrom(senderEmail, senderName);
//		messageHelper.setTo(to);
////		messageHelper.setCc(cc);
//		messageHelper.setSubject(subject);
//		String text = getMailContent(to, template, data);
//		messageHelper.setText(text, true);
//		javaMailSender.send(mimeMessage);
//	}
//
//	private String getMailContent(String to, String template,
//			Map<String, String> data) throws IOException, TemplateException {
//		Template fmTemplate = freeMarkerConfigurer.getConfiguration()
//				.getTemplate(template + CommonConstants.FREEMARK_POSTFIX);
//		Writer out = new StringWriter();
//		fmTemplate.process(data, out);
//		String mailContent = out.toString();
//		return mailContent;
//	}
//
//	@Override
//	public void sendMail(String to, String template, Map<String, String> data,
//			String subject, Boolean isHtml)
//			throws MessagingException, UnsupportedEncodingException,
//			IOException, TemplateException {
//		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
//				UTF8);
//		messageHelper.setFrom(senderEmail, senderName);
//		messageHelper.setTo(to);
//		messageHelper.setSubject(subject);
//		String text = getMailContent(to, template, data);
//		messageHelper.setText(text, isHtml);
//		javaMailSender.send(mimeMessage);
//	}
//
//}
