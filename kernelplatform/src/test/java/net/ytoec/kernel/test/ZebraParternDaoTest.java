package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraParternDao;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.service.SellerInfoService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ZebraParternDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraParternDao<ZebraPartern> dao;
	@Inject
	private SellerInfoService sellerInfoService;

	// 测试根据商家代码查询密钥
	@Test
	public void testSelectZebraSurfacebillsByCustomerCode() throws Exception {
//		String customerCode = "1";
//		String parternCode = dao.selectParternCodeByCustomerCode(customerCode);
//		System.out.println(parternCode);
		List list=new ArrayList();
		list=sellerInfoService.searchUserByUserCode("123456");
		System.out.println(list.get(0));
	}

}
