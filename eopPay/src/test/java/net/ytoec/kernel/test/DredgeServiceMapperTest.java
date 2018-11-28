package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.mapper.DredgeServiceMapper;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class DredgeServiceMapperTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private DredgeServiceMapper<DredgeService>  mapper;
	
//	@Test
//	public void testAdd() {
//		
//		DredgeService dredgeService=new DredgeService();
//		dredgeService.setUserId(10);
//		dredgeService.setServiceId(4);
//		dredgeService.setFlag("1");		
//		dredgeService.setCircle("0");
//		dredgeService.setBeginDate(new Date());
//		dredgeService.setEndDate(new Date());
//		dredgeService.setAutoRenew("1");
//		/*dredgeService.setBalance(10.0);
//		dredgeService.setSmsUsecount(20);
//		dredgeService.setSmsAllcount(200);
//		dredgeService.setSmsSendcount(250);
//		dredgeService.setSmsLess(30);
//		dredgeService.setCost(65.9);
//		dredgeService.setRemindFlag("1");*/
//		dredgeService.setRemark("短信不足提醒");
//		
//		mapper.add(dredgeService);
//		System.out.println("-----------");
//	}

//	@Test
//	public void testEdit() {
//		
//		DredgeService dredgeService=new DredgeService();
//		dredgeService.setUserId(11);
//		dredgeService.setServiceId(5);
//		dredgeService.setFlag("1");
//		dredgeService.setBalance(10.0);
//		dredgeService.setCircle("3");
//		dredgeService.setBeginDate(new Date());
//		dredgeService.setEndDate(new Date());
//		dredgeService.setAutoRenew("0");
//		dredgeService.setSmsUsecount(20);
//		dredgeService.setSmsAllcount(200);
//		dredgeService.setSmsSendcount(250);
//		dredgeService.setSmsLess(88);
//		dredgeService.setCost(65.9);
//		dredgeService.setRemindFlag("1");
//		dredgeService.setId(3);
//		dredgeService.setRemark("标记信息已被更新4");
//		
//		mapper.updateDredgeserviceBalanceById(dredgeService);
//		System.out.println("-----------");
//	}
	
	//@Test
	/*public void testRemove() {
		
		DredgeService dredgeService=new DredgeService();
	    dredgeService.setId(1);
		mapper.remove(dredgeService);
		System.out.println("-----------");
	}*/
	
//	@Test
//	public void testInsertBatch() {
//		
//		DredgeService dredgeService=new DredgeService();
//		dredgeService.setUserId(22);
//		dredgeService.setServiceId(2);
//		dredgeService.setFlag("1");
//		dredgeService.setBalance(10.0);
//		dredgeService.setCircle("3");
//		dredgeService.setBeginDate(new Date());
//		dredgeService.setEndDate(new Date());
//		dredgeService.setAutoRenew("0");
//		dredgeService.setSmsUsecount(20);
//		dredgeService.setSmsAllcount(200);
//		dredgeService.setSmsSendcount(250);
//		dredgeService.setSmsLess(88);
//		dredgeService.setCost(65.9);
//		dredgeService.setRemindFlag("1");
//		dredgeService.setId(3);
//		dredgeService.setRemark("批量1");
//		
//		DredgeService dredgeService1=new DredgeService();
//		dredgeService1.setUserId(33);
//		dredgeService1.setServiceId(5);
//		dredgeService1.setFlag("1");
//		dredgeService1.setBalance(10.0);
//		dredgeService1.setCircle("3");
//		dredgeService1.setBeginDate(new Date());
//		dredgeService1.setEndDate(new Date());
//		dredgeService1.setAutoRenew("0");
//		dredgeService1.setSmsUsecount(20);
//		dredgeService1.setSmsAllcount(200);
//		dredgeService1.setSmsSendcount(250);
//		dredgeService1.setSmsLess(88);
//		dredgeService1.setCost(65.9);
//		dredgeService1.setRemindFlag("1");
//		dredgeService1.setId(3);
//		dredgeService1.setRemark("批量2");
//		
//		
//		DredgeService dredgeService2=new DredgeService();
//		dredgeService2.setUserId(44);
//		dredgeService2.setServiceId(5);
//		dredgeService2.setFlag("1");
//		dredgeService2.setBalance(10.0);
//		dredgeService2.setCircle("3");
//		dredgeService2.setBeginDate(new Date());
//		dredgeService2.setEndDate(new Date());
//		dredgeService2.setAutoRenew("0");
//		dredgeService2.setSmsUsecount(20);
//		dredgeService2.setSmsAllcount(200);
//		dredgeService2.setSmsSendcount(250);
//		dredgeService2.setSmsLess(88);
//		dredgeService2.setCost(65.9);
//		dredgeService2.setRemindFlag("1");
//		dredgeService2.setId(3);
//		dredgeService2.setRemark("批量3");
//		
//		List<DredgeService> list=new ArrayList<DredgeService>();
//		list.add(dredgeService);
//		list.add(dredgeService1);
//		list.add(dredgeService2);
//		
//		mapper.insertBatch(list);
//		
//		System.out.println("批量插入完成");
//	}
//	
	@Test
	public void testGet() {
		DredgeService dredgeService=new DredgeService();
		dredgeService.setId(2);
		System.out.print(mapper.get(dredgeService).getRemark());
	}
}


