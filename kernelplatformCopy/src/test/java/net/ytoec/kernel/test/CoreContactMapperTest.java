package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.CoreContact;
import net.ytoec.kernel.mapper.CoreContactMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class CoreContactMapperTest {
	private static CoreContactMapper<CoreContact> mapper;

	private static int id = 28;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		// Thread.sleep(100000);
		mapper = (CoreContactMapper) ctx.getBean("coreContactMapper");
		System.out.println(mapper);
	}

	@Test
	public void testGetCoreContact() {
		CoreContact acc = new CoreContact();
		acc.setId(id);
		System.out.println(mapper.get(acc));
	}

//	@Test
	public void testAdd() {
		CoreContact coreContact = new CoreContact();
		coreContact.setId(27);
		coreContact.setName("name-");
		coreContact.setPhone("139000000");
		coreContact.setAddress("ceshi");
		mapper.add(coreContact);
	}

	@Test
	public void testEditCoreContact() {
		CoreContact acc = new CoreContact();
		acc.setId(id);
		acc = mapper.get(acc);
		System.out.println(acc);
		acc.setCity("city");
		acc.setPostCode("postcode");
		acc.setName("name--");
		acc.setOrderId(9999999);
		mapper.edit(acc);
		System.out.println(mapper.get(acc));
	}

	@Test
	public void testCoreContactList() {
		List<CoreContact> acc = mapper.getAllCoreContact();
		System.out.println(acc.size());
		System.out.println(acc);
	}

	@Test
	public void testRemoveCoreContact() {
		CoreContact acc = new CoreContact();
		acc.setId(27);
		mapper.remove(acc);
		System.out.println(mapper.get(acc));
	}

}
