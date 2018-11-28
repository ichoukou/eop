package net.ytoec.kernel.test.mail;

import javax.inject.Inject;

import net.ytoec.kernel.service.NewMailService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-mail.xml")
public class MailTest extends AbstractJUnit38SpringContextTests {
	@Inject
	private NewMailService mailService;
	
	@Test
	public void testNewMail() throws Exception {
//		NewMailService mailService=new MailServiceImpl();
		mailService.toString();
		
		
	}
	
}