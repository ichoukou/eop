package net.ytoec.eop.app.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;
import net.ytoec.eop.app.bean.App;
import net.ytoec.eop.app.service.AppService;
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
public class AppDaoImplTest {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	@Inject
	public static AppDao<App> dao;
	@Inject
	public static AppService appService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		dao = (AppDao) ctx.getBean("appDaoImpl");
		appService = (AppService) ctx.getBean("appServiceImpl");
	}
	@Test
	public void testAdd() {
		App app = new App();

		app.setAppKey("100012");
		app.setAppName("liug测试应用2");
		app.setAccount("管理员");
		app.setApptype("1");
		app.setAppstatus("2");
		app.setSecret("adbs325gcd2");
		app.setUserId(1);
        boolean result = dao.add(app);
        Assert.assertEquals(true, result);
 //       dao.remove(app);
	}

	@Test
	public void testGetAppById() {
		App app = dao.get(21);
		System.out.println("修改前："+app.getAppName());
		app.setAppName(app.getAppName()+new Date().toString());
		appService.edit(app);
		System.out.println("修改后："+app.getAppName());
		app.setAppName("修改完成");
		appService.edit(app);
	}
	@Test
	public void testGetAppall() {
		Map map=new HashMap();
		List list=dao.getPageList(map);
		for (int i = 0; i < list.size(); i++) {
			App app = (App) list.get(i);
			System.out.println(app.getId());
		}
		
		System.out.println("list.size()="+list.size());
	}

}
