package net.ytoec.kernel.test;

import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.SMSTemp;
import net.ytoec.kernel.service.SMSHistoryInfoService;
import net.ytoec.kernel.service.SMSTempService;
import net.ytoec.kernel.service.SMSObjectService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;



public class SMSTempServiceTest {


	public static SMSTempService<SMSTemp> service;
    private static SMSObjectService<SMSObject>  SMSObjectService=null;
	
	private static SMSHistoryInfoService<SMSObject>  smsHistoryInfoService=null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:applicationContext-*.xml");
		service = (SMSTempService) ctx.getBean("SMSTempServiceImpl");
		SMSObjectService=(net.ytoec.kernel.service.SMSObjectService<SMSObject>) ctx.getBean("SMSObjectServiceImpl");
		smsHistoryInfoService=(net.ytoec.kernel.service.SMSHistoryInfoService<SMSObject>) ctx.getBean("SMSHistoryInfoServiceImpl");
	}
	
	
	
	
	@Test
	public void testadd(){
		
	SMSObject smsInfo=new SMSObject();
	 smsInfo.setId(116698);
	 smsInfo=SMSObjectService.get(smsInfo);	
	 boolean bool=false;
	 if(smsInfo.getSuccTotal()+smsInfo.getFailTotal()==smsInfo.getPkTotal())
	 {
		 //将回调过的短信插入到历史记录表中
		 bool = smsHistoryInfoService.add(smsInfo);	
		 if(bool)
			 System.out.println("插入到发送短信历史记录表:ec_core_paysms_info  id=成功...");
		 else
			 System.out.println("插入到发送短信历史记录表:ec_core_paysms_info  id=失败...");
		 
		 //将待发送短信表ec_core_paysms的对应的记录删除掉
		 bool = SMSObjectService.remove(smsInfo);
		 if(bool)
			 System.out.println("删除待发送短信ec_core_paysms表  id=成功...");
		 else
			 System.out.println("删除待发送短信ec_core_paysms表  id=失败...");
	 }
	 
	}
}

