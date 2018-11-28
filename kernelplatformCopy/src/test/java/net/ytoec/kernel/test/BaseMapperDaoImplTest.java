package net.ytoec.kernel.test;

import java.util.Random;

import javax.inject.Inject;

import net.ytoec.kernel.dao.BaseMapperDao;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.mapper.UserMapper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class BaseMapperDaoImplTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private BaseMapperDao<User> dao;

	private int TEST_ID = (new Random()).nextInt();

	@Before
	public void init() {
		System.out.println(dao);
		dao.setMapperClass(UserMapper.class);

	}

	
	@Test
	public void testGet() throws Exception {
		init();
		User acc = new User();
		acc.setId(TEST_ID);
		System.out.println(dao.get(acc));
	}
}
