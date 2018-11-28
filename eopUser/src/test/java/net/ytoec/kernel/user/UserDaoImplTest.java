package net.ytoec.kernel.user;

import java.util.HashMap;
import java.util.Map;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.lowagie.text.List;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
public class UserDaoImplTest {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

	public static UserDao<User> dao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		dao = (UserDao) ctx.getBean("userDaoImpl");
	}
	//@Test
	public void testAdd() {
		User user = new User();

		user.setUserName("test_liangzai");
		user.setUserPassword(Md5Encryption.MD5Encode("123456")); // 使用MD5加密
		user.setTelePhone("1567890262332");
		user.setAddressProvince("上海市");
		user.setAddressCity("上海市");
		user.setAddressDistrict("清浦区");
		user.setAddressStreet("华新镇华徐公路3029弄28号");
		user.setRemark(this.getClass().getName()+"#");

		dao.add(user);
	}

	//@Test
	public void testGetUserById() {
		User user = dao.getUserById(1);
		System.out.println(user.getUserName());
	}

	@Test
	public void testEdit() {
		User user = dao.getUserById(16739);
		//System.out.println(user);

		if (user == null)
			return;

		user.setChildType("B");
		user.setShopName("大富豪土豆公司上海分公司");
		
		boolean res = dao.edit(user);
		Assert.assertTrue(res);
	}

	//@Test
	public void testRemove() {
		User user = dao.getUserById(30624);
		if (user == null)
			return;

		dao.delUser(user);
	}

	@Test
	public void testEditPwd() {
	    Map<String, String> map=new HashMap<String, String>();
	    map.put("remark", "418_add");
		java.util.List<User> users =dao.getUsersByRemark(map);
		User user=new User();
		System.out.println("总数:"+users.size());
		String pwd="";
		for (int i = 0; i < users.size(); i++) {
            user=users.get(i);
            pwd=Md5Encryption.MD5Encode("YTO"+user.getSite());
            System.out.println(user.getSite()+"--:"+pwd);
            user.setUserPassword(pwd);
            dao.edit(user);
        }
		
	}

}
