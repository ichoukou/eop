package net.ytoec.kernel.test;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.service.ZebraParternService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ZebraParternServiceTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraParternService<ZebraPartern> service;

	// 测试上传密钥到金刚
	@Test
	public void testParternTimer() throws Exception {
		service.parternTimer();
	}
}