package net.ytoec.kernel.test;


import java.util.HashMap;
import java.util.Map;

import net.ytoec.kernel.dto.SMSMessage;
import net.ytoec.kernel.service.SMSPortService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SMSPortServiceTest {


	public static SMSPortService service;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (SMSPortService) ctx.getBean("SMSPortServiceImpl");
	}
	

	
	@Test
	public void testGetUsersByCode(){
		
		
		//* supplySendSMS(Integer userId,String softwareSerialNo,String key,String[] mobiles,String smsContent,String sendTime)
		//* @return map(sendId,sendSMSCount)
		//1）测试发送短信
		/*  Map map=new HashMap();
		 map.put("userId", 30994);
		map.put("userType", "1");
		map.put("softwareSerialNo", "0SDK-EAA-6688-JEUTM");
		map.put("key", "799634");
		map.put("mobiles", new String[]{"15026599217"});
		map.put("smsContent", "测试");
		Map  maps=service.supplySendSMS(map);
		System.out.println("======================="+maps.get("sendId"));*/
		//2)测试短信接口是否开启
		/*Boolean bool=service.supplyStatusOpenUp(31615);
		System.out.println("======================="+bool);*/
		//3)可用短信数量、开始时间、结束时间 返回一个小的对象（查询开通的服务表记录）
		SMSMessage supplySMSMessage=service.supplySMSMessage(30994);
		System.out.println("======================="+supplySMSMessage.getSmsUsecount());
		//4)更改金额
		// * @param Map 的值：（Integer）userId、(Integer) SMSnum,(double) prince,(String) name,String softwareSerialNo,String  key
		
	/*	Map map=new HashMap();
		map.put("userId", 3575);
		map.put("SMSnum",10);
		map.put("prince",10);
		map.put("name", "100元超值套餐 ");
		Integer returnnum=service.supplyRecharge(map);
		System.out.println("======================="+returnnum);*/
	}
}
