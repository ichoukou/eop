package net.ytoec.kernel.dao;

import junit.framework.Assert;
import net.ytoec.kernel.dataobject.ConfigCode;

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
public class ConfigCodeDaoImplTest {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */

	public static ConfigCodeDao<ConfigCode> dao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		dao = (ConfigCodeDao) ctx.getBean("configCodeDaoImpl");
	}

	// @Test
	public void testAdd() {
		ConfigCode configCode = new ConfigCode();
		configCode.setConfKey("test7776");
		configCode.setConfValue("测试添加");
		boolean result = dao.addConfig(configCode);
		Assert.assertEquals(result, true);
	}

	@Test
	public void testGetConfigCodeById() {
		ConfigCode configCode = dao.getConfigById(33);
		System.out.println(configCode.getConfValue());
	}

	// @Test
	public void testRemove() {
		ConfigCode configCode = dao.getConfByKey("test7776");
		boolean rs = dao.removeConfig(configCode);
		Assert.assertEquals(rs, true);
	}

}
