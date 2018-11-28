package net.ytoec.kernel.test.mail;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mail.xml",
		"classpath*:spring-*.xml" })
public class YTOXLTest {
	protected static Logger log = LoggerFactory.getLogger(YTOXLTest.class);
}
