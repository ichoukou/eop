package net.ytoec.kernel.user;

import java.util.List;

import net.ytoec.kernel.dao.UserThreadDao;
import net.ytoec.kernel.dataobject.UserThread;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
public class UserThreadDaoImplTest {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */

	public static UserThreadDao<UserThread> dao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		dao = (UserThreadDao) ctx.getBean("userThreadDaoImpl");
	}
	

	//@Test
	public void testGetUserById() {
		UserThread user = dao.getUserById(13366);
		System.out.println(user.getUserName());
	}

	//@Test
	public void testgetUserThreadByPostId() {
		List<UserThread>list =dao.getUserThreadByPostId(90);
		System.out.print(list.size());
	}
	
	@Test
	public void testGetUsersByCode(){
	    UserThread entity = new UserThread();
	    entity.setUserCode("K53400049");
	    List<UserThread> list =dao.searchUsersByCode(entity);
        System.out.println("结果数量："+list.size());
        for(UserThread ut : list){
            System.out.println(ut.getUserName()+"---"+ut.getUserCode());
        }
	}

}
