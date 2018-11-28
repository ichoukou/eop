package net.ytoec.kernel.test;

import java.util.LinkedList;
import java.util.List;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSDeliverService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class SMSDeliverServiceTest {

	public static SMSDeliverService<SMSObject> service;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (SMSDeliverService) ctx.getBean("SMSDeliverServiceImpl");
	}
	

	@Test
	public void testremoveByIDs(){
		SMSObject o1=new SMSObject();
		o1.setId(1);
		SMSObject o2=new SMSObject();
		o2.setId(2);
		SMSObject o3=new SMSObject();
		o3.setId(3);
		List<SMSObject> ids=new LinkedList<SMSObject>();
		ids.add(o1);
		ids.add(o2);
		ids.add(o3);
		System.out.print(service.removeByIDs(ids));
	}
	
}

