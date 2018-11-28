package net.ytoec.kernel.test;

import java.util.List;

import junit.framework.Assert;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.StandardPosttempDao;
import net.ytoec.kernel.dataobject.StandardPosttemp;
import net.ytoec.kernel.service.StandardPosttempService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.annotation.Rollback;

public class StandardPosttempServiceTest {

	private static StandardPosttempService<StandardPosttemp> standardPosttempService;

	private static StandardPosttempDao<StandardPosttemp> standardPosttempDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		standardPosttempService = (StandardPosttempService) ctx.getBean("standardPosttempServiceImpl");

		standardPosttempDao = (StandardPosttempDao) ctx.getBean("standardPosttempDaoImpl");
	}

	@Test
	@Rollback(true)
	public void testAddStandardPosttemp() {

		StandardPosttemp st = new StandardPosttemp();
		st.setSourceId(1);
		st.setDestId(2);
		st.setStandardPrice("10");
		st.setContinuationPrice("9");
		
		st.setName("name");
		st.setRemark("remark");
		Assert.assertEquals(standardPosttempService.addStandardPosttemp(st),
				true);

	}


	@Test
	public void testGetStandardPosttemp() {

	}

	@Test
	public void testUpdateStandardPosttemp() {

	}

	@Test
	public void testRemoveStandardPosttemp() {
		List<StandardPosttemp> list = standardPosttempDao.getAll();
		for (StandardPosttemp st : list) {
			System.out.println(st);
			// standardPosttempDao.removeStandardPosttemp(st);
			break;
		}
	}

	@Test
	public void testPagination() {
		Pagination pagination = new Pagination(1, 10);
		List<StandardPosttemp> list = standardPosttempService.getStandardPosttempListBySourceId(1, pagination, true);
		for (StandardPosttemp st : list) {
			System.out.println(st);
		}
		System.out.println("总记录数：" + list.size());
	}


}
