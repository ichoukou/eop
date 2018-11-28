package net.ytoec.kernel.test;

import java.util.Random;

import javax.inject.Inject;

import net.ytoec.kernel.dao.BaseMapperDao;
import net.ytoec.kernel.dataobject.CoreContact;
import net.ytoec.kernel.mapper.CoreContactMapper;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class CoreContactDaoImplTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private BaseMapperDao<CoreContact> dao;

	@Inject
	// private int TEST_ID = (new Random()).nextInt();
	@Before
	public void init() {
		System.out.println(dao);
		dao.setMapperClass(CoreContactMapper.class);

	}

	// 测试dao
	public void testAdd() throws Exception {
		init();
		CoreContact CoreContact = new CoreContact();
		CoreContact.setId(27);
		CoreContact.setAddress("address");
		CoreContact.setName("name-----");
		CoreContact.setOrderId(8888888);
		dao.add(CoreContact);
		// System.out.println(dao.add(CoreContact));
	}

	public void testGet() throws Exception {
		init();
		CoreContact acc = new CoreContact();
		acc.setId(27);
		System.out.println(dao.get(acc));
	}
}
