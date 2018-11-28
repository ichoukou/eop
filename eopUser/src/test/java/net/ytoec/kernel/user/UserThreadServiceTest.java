package net.ytoec.kernel.user;

import java.util.List;

import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.UserThreadService;

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
public class UserThreadServiceTest {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */

	public static UserThreadService<UserThread> service;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		service = (UserThreadService) ctx.getBean("userThreadServiceImpl");
	}
	

	
//	@Test
	public void testGetUsersByCode(){
	    UserThread entity = new UserThread();
	    entity.setUserCode("YTO1289747");
	    List<UserThread> list =service.searchUsersByCode(entity);
        System.out.println("结果数量："+list.size());
        for(UserThread ut : list){
            System.out.println(ut.getUserName()+"---"+ut.getUserCode());
        }
	}
	
	@Test
	public void testGenerateCode(){
	    String userCode = service.generateUserCode("210053");
	    System.out.println(userCode);
	}

}
