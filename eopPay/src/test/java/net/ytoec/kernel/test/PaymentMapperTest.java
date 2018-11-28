package net.ytoec.kernel.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.mapper.PaymentMapper;
import net.ytoec.kernel.common.DateUtil;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class PaymentMapperTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private PaymentMapper<Payment>  mapper;
	
//	@Test
//	public void testAdd() {
//		
//		Payment  payment=new Payment();
//		
//		payment.setUserId(10);
//		payment.setAuserId(11);
//		payment.setDealMoney(200.9);
//		payment.setDealTime(new Date());
//		payment.setCreateTime(DateUtil.getDateAfter(new Date(),8));
//		payment.setDealType("1");
//		payment.setDealName("订购服务");
//		payment.setDealStatus("0");
//		payment.setFlag("0");
//		payment.setReason("Reason");
//		payment.setRemark("等待付款");
//		payment.setDelFlag("0");		
//		mapper.add(payment);
//		System.out.println("-----------");
//	}
//	
	@Test
	public void testGet() {
		
		Payment  payment=new Payment();		
		payment.setId(256);	
		System.out.println("-----------"+mapper.get(payment).getDealName());
	}
//	
//	//
	/*@Test
	public void testgetPaymentList() {
		
		Map map=new HashMap();		
		map.put("id", 1);	
		map.put("startIndex", 0);	
		map.put("pageNum", 10);	
		System.out.println("-----------"+mapper.getPaymentList(map).size());
	}*/
	
}
