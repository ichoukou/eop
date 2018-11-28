package net.ytoec.kernel.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.service.ServiceHistoryService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * @author GLC
 * @date 2012-07-04
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ServiceHistoryServiceImplTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private ServiceHistoryService<ServiceHistory>  service;
	
//	@Test
//	public void testGetServiceHistoryList()
//	{
//		Map map=new HashMap();	
//		map.put("userId", 10);	
//		map.put("dserviceId", 2);
//		
//		Pagination<ServiceHistory> pagination=new Pagination<ServiceHistory>(2,100);		
//		
//		System.out.println("-----------"+service.getServiceHistoryList(map,pagination, true).size());
//	}
	
//	@Test
//	public void testAdd()
//	{
//		ServiceHistory serviceHistory=new ServiceHistory();
//		serviceHistory.setDserviceId(345);
//		serviceHistory.setPayId(1);
//		serviceHistory.setUserId(10);
//		serviceHistory.setCircle("月");
//		serviceHistory.setBeginDate(new Date());
//		serviceHistory.setEndDate(new Date());
//		serviceHistory.setAutoRenew("1");
//		serviceHistory.setDealType("1");
//		serviceHistory.setDealName("添加测试");
//		serviceHistory.setSmsCount(100);
//		serviceHistory.setCost(10.8);
//		serviceHistory.setPromCost(12.5);
//		serviceHistory.setRemark("备注添加测试");
//		
//		service.add(serviceHistory);
//		System.out.println("-----------");
//	}
	
//	@Test
//	public void testEdit()
//	{
//		ServiceHistory serviceHistory=new ServiceHistory();
//		serviceHistory.setId(6);
//		serviceHistory.setDserviceId(345);
//		serviceHistory.setPayId(1);
//		serviceHistory.setUserId(10);
//		serviceHistory.setCircle("季");
//		serviceHistory.setBeginDate(new Date());
//		serviceHistory.setEndDate(new Date());
//		serviceHistory.setAutoRenew("1");
//		serviceHistory.setDealType("1");
//		serviceHistory.setDealName("添加测试");
//		serviceHistory.setSmsCount(100);
//		serviceHistory.setCost(10.8);
//		serviceHistory.setPromCost(12.5);
//		serviceHistory.setRemark("修改信息了");
//		
//		System.out.println("-----------"+service.edit(serviceHistory));
//	}
	
	@Test
	public void testRemove()
	{
		ServiceHistory serviceHistory=new ServiceHistory();
		serviceHistory.setId(6);
		System.out.println("-----------"+service.remove(serviceHistory));
	}
}
