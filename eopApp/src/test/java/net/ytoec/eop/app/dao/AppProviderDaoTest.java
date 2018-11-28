package net.ytoec.eop.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;

import net.ytoec.kernel.dao.AppProviderDao;
import net.ytoec.kernel.dataobject.AppProvider;

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
public class AppProviderDaoTest {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	@Inject
	public static AppProviderDao<AppProvider> dao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		dao = (AppProviderDao) ctx.getBean("appProviderDaoImpl");
	}
	
	//@Test
	public void testAdd() {
		AppProvider app = new AppProvider();
		app.setServicesType(AppProvider.TYPE_PLATFORM);
		app.setLinkman("测试人员");
		boolean result = dao.add(app);
        Assert.assertEquals(true, result);
	}

	//@Test
	public void testGetAppProviderByUserId() {
		AppProvider app = dao.getByUserId(3);
		System.out.println("联系人："+app.getLinkman());
	}
	
	//@Test
	public void testRemove(){
		AppProvider app = new AppProvider();
		app.setId(3);
		boolean result = dao.remove(app);
        Assert.assertEquals(true, result);
	}
	
	@Test
	public void testGetAppall() {
		Map<String ,Object> map=new HashMap<String ,Object>();
		List<AppProvider> list=dao.getAll(map);
		for (AppProvider app:list) {
			System.out.println("联系人："+app.getLinkman());
		}
		
		System.out.println("list.size()="+list.size());
	}

}
