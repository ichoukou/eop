package net.ytoec.kernel.test;

import net.ytoec.kernel.dataobject.CountSellerInfo;
import net.ytoec.kernel.service.CountSellerInfoService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class CountSellerInfoServiceTest extends
		AbstractJUnit38SpringContextTests {

	@Autowired
	private CountSellerInfoService service;

	// 测试根据电话号码查询统计商家号码信息
	@Test
	public void testSelectByPhone() {
		String phone = "1";
		CountSellerInfo countSellerInfo = service.selectByPhone(phone);
		System.out.println(countSellerInfo);
	}
	
	
	// 测试统计商家号码信息
	@Test
	public void testCountSellerInfoTimer() {
		service.countSellerInfoTimer();
	}
}
