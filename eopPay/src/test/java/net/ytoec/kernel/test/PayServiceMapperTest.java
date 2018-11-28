package net.ytoec.kernel.test;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.mapper.PayServiceMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class PayServiceMapperTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private PayServiceMapper<PayService>  mapper;
	
//	@Test
//	public void testAdd() {
//
//		PayService  payService=new PayService();
//		payService.setName("短信服务");
//		payService.setUnitPrice(0.5);
//		payService.setFlag("1");
//		payService.setServiceType("1");
//		payService.setRemark("备注6");		
//		
//		mapper.add(payService);
//		System.out.println("-----------");
//	}
//	
	@Test
	public void testGet() {
		PayService payService=new PayService();
		payService.setId(1);
		System.out.print(mapper.get(payService).getName());
	}
}
