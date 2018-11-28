package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.service.DredgeServiceService;

import org.apache.commons.collections.CollectionUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class DredgeServiceServiceTest {

	public static DredgeServiceService<DredgeService> service;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (DredgeServiceService) ctx.getBean("dredgeServiceServiceImpl");
	}
	

//	
//	@Test
//	public void testGetUsersByCode(){
//		List<DredgeService> dredgeServiceList=new ArrayList<DredgeService>();
//		DredgeService dredgeService1=new DredgeService();
//		dredgeService1.setUserId(22);
//		dredgeService1.setServiceId(2);
//		dredgeService1.setFlag("4");
//		dredgeService1.setBalance(100.00);
//		dredgeService1.setSmsLess(100);
//		dredgeServiceList.add(dredgeService1);
//		
//		DredgeService dredgeService2=new DredgeService();
//		dredgeService2.setUserId(22);
//		dredgeService2.setServiceId(2);
//		dredgeService2.setFlag("4");
//		dredgeService2.setBalance(100.00);
//		dredgeService2.setSmsLess(100);
//		dredgeServiceList.add(dredgeService2);
//		service.updateBatch(dredgeServiceList);
//	}
	
//	@Test
//	public void testGetDredgeserviceList(){
//		Map map=new HashMap();
//		map.put("smsAllcount", 100);
//		map.put("beginDate", new Date());
//		
//		Pagination pagination =new Pagination(1,100);
//		service.getDredgeserviceList(map, pagination, false);
//	}
	
	/*@Test
	public void testInsertBatch() {
		
		DredgeService dredgeService=new DredgeService();
		dredgeService.setUserId(22);
		dredgeService.setServiceId(2);
		dredgeService.setFlag("1");
		dredgeService.setBalance(10.0);
		dredgeService.setCircle("3");
		dredgeService.setBeginDate(new Date());
		dredgeService.setEndDate(new Date());
		dredgeService.setAutoRenew("0");
		dredgeService.setSmsUsecount(20);
		dredgeService.setSmsAllcount(200);
		dredgeService.setSmsSendcount(250);
		dredgeService.setSmsLess(88);
		dredgeService.setCost(65.9);
		dredgeService.setRemindFlag("1");
		dredgeService.setId(3);
		dredgeService.setRemark("批量66");
		
		DredgeService dredgeService1=new DredgeService();
		dredgeService1.setUserId(33);
		dredgeService1.setServiceId(5);
		dredgeService1.setFlag("1");
		dredgeService1.setBalance(10.0);
		dredgeService1.setCircle("3");
		dredgeService1.setBeginDate(new Date());
		dredgeService1.setEndDate(new Date());
		dredgeService1.setAutoRenew("0");
		dredgeService1.setSmsUsecount(20);
		dredgeService1.setSmsAllcount(200);
		dredgeService1.setSmsSendcount(250);
		dredgeService1.setSmsLess(88);
		dredgeService1.setCost(65.9);
		dredgeService1.setRemindFlag("1");
		dredgeService1.setId(3);
		dredgeService1.setRemark("批量88");
		
	
		
		List<DredgeService> list=new ArrayList<DredgeService>();
		list.add(dredgeService);
		list.add(dredgeService1);
		
		service.insertBatch(list);
		
		System.out.println("批量插入Service完成");*/
	
	
	
	@Test
	public void testGetUsersByCode(){
		Map map=new HashMap();
		map.put("userId", 7);

		map.put("serviceName", "时效提醒");
		
		System.out.println("========"+service.getServiceByUserIdAndServiceName(map));
	}
	
}
