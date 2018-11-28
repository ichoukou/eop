package net.ytoec.kernel.test;

import junit.framework.Assert;
import net.ytoec.kernel.dataobject.ServerConfig;
import net.ytoec.kernel.service.ServerConfigService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ServerConfigSericeTest {

	private static ServerConfigService<ServerConfig> serverConfigSerice;
	private static int id;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		serverConfigSerice = (ServerConfigService) ctx
				.getBean("serverConfigServiceImpl");

	}

	@Test
	public void testGetConfigByServerId() {
		ServerConfig serverConfig = serverConfigSerice.getConfigByServerId(2);
		Assert.assertNull(serverConfig);
		// Assert.assertNotNull(serverConfig);
	}

}
