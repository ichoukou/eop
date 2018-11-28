package net.ytoec.kernel.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSObjectService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SMSObjectServiceTest {


	public static SMSObjectService<SMSObject> service;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (SMSObjectService) ctx.getBean("SMSObjectServiceImpl");
	}
	

	
	@Test
	public void testadd(){
		SMSObject sMSObject=new SMSObject();
		sMSObject.setCreateTime(new Date());
		String smsContent="【购贵妃水送百元优惠券】登陆手机淘宝： herborn.m.tmall.com";
		 
		sMSObject.setMessageContent(smsContent);
		sMSObject.setDestMobile("13764395168");
		sMSObject.setSequenceID(123456);
		sMSObject.setStatus("8");

		sMSObject.setSmsType("1");
		sMSObject.setUserId(7);
		service.add(sMSObject);
		
		/* Map<String, Object> map = new HashMap<String, Object>();
 		  map.put("limit", 800);
        List<SMSObject> sMSObjectLis=service.getList(map);
        System.out.println("==============================="+sMSObjectLis.size());*/
		/*SMSObject sMSObject=new SMSObject();
		sMSObject.setId(5);
		sMSObject=service.get(sMSObject);
		sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
		sMSObject.setSendTime(new Date());
		service.edit(sMSObject);
		sMSObject=service.get(sMSObject);
		System.out.println("====="+sMSObject.getStatus()+"==SendTime="+sMSObject.getSendTime());*/
	 	}
}

