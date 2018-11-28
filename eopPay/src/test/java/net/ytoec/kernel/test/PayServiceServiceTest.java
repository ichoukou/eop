package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.service.PayServiceService;

import org.apache.commons.collections.CollectionUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class PayServiceServiceTest {


	public static PayServiceService<PayService> service;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (PayServiceService) ctx.getBean("payServiceServiceImpl");
	}
	

	
	@Test
	public void testGetUsersByCode(){
		PayService payService=new PayService();
		//payService.setId(1);
		payService.setName("余额不足提醒");
		 payService=  service.get(payService);
		System.out.println("----------------- "+payService.getId());
	/*	if(CollectionUtils.isNotEmpty(payServiceList)){
			for(PayService payService:payServiceList){
				 System.out.println("---"+payService.getName());
			}
		}*/
	 	}
}
