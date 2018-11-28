package net.ytoec.kernel.test;


import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/application*.xml",
		"classpath*:/test.xml" })
public class BaseTest {
	protected static Logger log = LoggerFactory.getLogger(BaseTest.class);
}
