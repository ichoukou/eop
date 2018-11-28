package net.ytoec.kernel.dao;

import junit.framework.Assert;
import net.ytoec.kernel.service.MemcacheService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MemcacheServiceTest extends BaseTest {

	@Autowired
	MemcacheService<String> memcacheService;

	@Test
	public void testAddConfig() {

//		 memcacheService.printStatus();
		Assert.assertEquals(true, memcacheService.add("test00", "test"));
		System.out.println("##########################"
				+ memcacheService.get("test00"));
	}
}
