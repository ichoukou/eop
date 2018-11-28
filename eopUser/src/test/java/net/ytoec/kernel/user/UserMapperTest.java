package net.ytoec.kernel.user;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.mapper.UserMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 
 * @author ChenRen
 * @date 2011-7-25
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class UserMapperTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private UserMapper<User> mapper;

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
		List<User> list = mapper.getAllUser();
		int i=0;
		for (User user : list) {
			if("99".equals(user.getUserLevel())){
	            user.setUserPassword(Md5Encryption.MD5Encode("YTO"+user.getUserCode()));
				mapper.edit(user);
				i++;
			}

			
//			// 取当前表中最大的Id值
//			if (tempId < user.getId()) {
//				tempId = user.getId();
//				continue;
//			}
		} // for
System.out.println(i);
//		User user = new User();
//		user.setId(tempId);
//
//		System.out.println(mapper.get(user));
	}

//	@Test
//	public void testEdit() {
//		User user = new User();
//		user.setId(tempId);
//
//		user = mapper.get(user);
//		System.out.println(user);
//
//		user.setAddressProvince("ShangHai Province");
//		user.setAddressCity("ShangHai City");
//		user.setAddressDistrict("QingPu District");
//		user.setAddressStreet("HuaXin Town HuaXu Road No.28 Of No.3029."); // 华新镇华徐公路3029弄28号
//		user.setRemark(this.getClass().getName()+"#"+this.getName());
//
//		mapper.edit(user);
//		System.out.println(mapper.get(user));
//	}
//
//	@Test
//	public void testRemove() {
//		User user = new User();
//		user.setId(tempId);
//
//		mapper.remove(user);
//		System.out.println(mapper.get(user));
//	}
/*
 *  searchUsers 以改成了分页查询的方法; 见service的实现
	@Test
	public void testSearchUsers() {
		User user = new User();
		user.setUserNameText("si");

		List<User> list = mapper.searchUsers(user);
		System.out.println("参数是：si__________" + "总数：" + list.size());
		for (User u : list) {
			System.out.println(u.getUserName() + ":" + u.getUserNameText());
		} // for
	}
*/
}
