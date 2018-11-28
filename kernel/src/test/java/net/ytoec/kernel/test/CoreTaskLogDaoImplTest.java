package net.ytoec.kernel.test;

import java.util.Random;

import javax.inject.Inject;

import net.ytoec.kernel.dao.BaseMapperDao;
import net.ytoec.kernel.dataobject.CoreTaskLog;
import net.ytoec.kernel.mapper.CoreTaskLogMapper;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class CoreTaskLogDaoImplTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private BaseMapperDao<CoreTaskLog> dao;

	@Inject
	// private int TEST_ID = (new Random()).nextInt();
	@Before
	public void init() {
		System.out.println(dao);
		dao.setMapperClass(CoreTaskLogMapper.class);

	}

	// 测试dao
	public void testAdd() throws Exception {
		init();
		CoreTaskLog coreTaskLog = new CoreTaskLog();
		coreTaskLog.setId(27);
		coreTaskLog.setRequestURL("www.cccc.cn");
		coreTaskLog.setFailMessage("setFailMessage");
		dao.add(coreTaskLog);
		// System.out.println(dao.add(CoreTaskLog));
	}

	public void testGet() throws Exception {
		init();
		CoreTaskLog acc = new CoreTaskLog();
		acc.setId(27);
		System.out.println(dao.get(acc));
	}
}
