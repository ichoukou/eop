package net.ytoec.kernel.user;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dao.UserCustomDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.mapper.UserCustomMapper;
import net.ytoec.kernel.mapper.UserMapper;
import net.ytoec.kernel.service.UserCustomService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class UserCustomDaoImplTest extends AbstractJUnit38SpringContextTests {

//	@Inject
//	private UserDao<User> dao;
	@Inject
	private UserCustomDao<UserCustom> cusdao;
	@Inject
	private UserCustomService<UserCustom> service;
	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

	@Test
	public void testGet() {
//		UserCustom userCustom=new UserCustom();
//		userCustom.setBindedUserName("test_vip2");
//		List<UserCustom> list = service.searchUserCustom(userCustom);
//		for (UserCustom user : list) {
//             System.out.println(user.getCustomerId());
//		}
		UserCustom userCustom1=new UserCustom();
		userCustom1.setBindedUserName("test_vip2");
		userCustom1.setUserName("test_vip2");
		userCustom1.setType("1");
		
		List<UserCustom> list = service.searchUserCustom(userCustom1);
		for (UserCustom user : list) {
             System.out.println(user.getCustomerId());
		}
		
		System.out.println(service.remove(userCustom1));
		
	}

}
