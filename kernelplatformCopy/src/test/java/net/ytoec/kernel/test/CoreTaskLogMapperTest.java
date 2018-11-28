package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.CoreTaskLog;
import net.ytoec.kernel.mapper.CoreTaskLogMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CoreTaskLogMapperTest {
	private static CoreTaskLogMapper<CoreTaskLog> mapper;

	private static int id = 28;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		// Thread.sleep(100000);
		mapper = (CoreTaskLogMapper) ctx.getBean("coreTaskLogMapper");
		System.out.println(mapper);
	}

	@Test
	public void testGetCoreTaskLog() {
		CoreTaskLog acc = new CoreTaskLog();
		acc.setId(28);
		System.out.println(mapper.get(acc));
	}

	@Test
	public void testAdd() {
		CoreTaskLog coreTaskLog = new CoreTaskLog();
		coreTaskLog.setId(26);
		coreTaskLog.setRequestURL("www.ccc.com");
		/* coreTaskLog.setRequestTime(requestTime); */
		coreTaskLog.setOrderId(001);
		coreTaskLog.setClientId("12922");
		mapper.add(coreTaskLog);
	}

	@Test
	public void testEditCoreTaskLog() {
		 
		CoreTaskLog acc = new CoreTaskLog();
		acc.setId(id);
		acc = mapper.get(acc);
		System.out.println(acc);
		acc.setFailMessage("fulan");
		acc.setRequestURL("www.fulan.com");
		mapper.edit(acc);
		System.out.println(mapper.get(acc));
		 
	}

	@Test
	public void testCoreTaskLogList() {
		List<CoreTaskLog> acc = mapper.getAllCoreTaskLog();
		System.out.println(acc.size());
		System.out.println(acc);
	}
	
}
