package net.ytoec.kernel.user;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.mapper.UserCustomMapper;
import net.ytoec.kernel.mapper.UserMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class UserCustomMapperTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private UserCustomMapper<UserCustom> mapper;

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

//	@Test
//	public void testAdd() {
//
//		User user = new User();
//		user.setUserSource("2");
//		user.setShopAccount("xx");
////		user.setUserCode("2011-09-27x");
////		user.setUserName("YTO_ZhangSan");
////		user.setUserPassword(Md5Encryption.MD5Encode("123456")); // 使用MD5加密
////		user.setTelePhone("1567890262332");
////		user.setAddressProvince("上海市");
////		user.setAddressCity("上海市");
////		user.setAddressDistrict("清浦区");
////		user.setAddressStreet("华新镇华徐公路3029弄28号");
////		user.setRemark(this.getClass().getName()+"#"+this.getName());
//		mapper.add(user);
//		System.out.println(mapper.get(user));
//	}

	@Test
	public void testGet() {
		UserCustom userCustom=new UserCustom();
		userCustom.setBindedUserName("1");
		List<UserCustom> list = mapper.searchUsers(userCustom);
		for (UserCustom user : list) {
             System.out.println(user.getCustomerId());
		} // for
	}


}
