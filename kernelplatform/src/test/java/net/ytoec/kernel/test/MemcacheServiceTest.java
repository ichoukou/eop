package net.ytoec.kernel.test;

import junit.framework.Assert;
import net.ytoec.kernel.service.MemcacheService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MemcacheServiceTest extends BaseTest {

	@Autowired
	MemcacheService<String> memcacheService;

	@Test
	public void testAddConfig() {

		Assert.assertEquals(true, memcacheService.add("test9", "hello"));
		System.out.println("##########################"
				+ memcacheService.get("test9"));
	}
}
