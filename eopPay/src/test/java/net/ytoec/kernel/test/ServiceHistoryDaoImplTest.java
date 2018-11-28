package net.ytoec.kernel.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.impl.ServiceHistoryDaoImpl;
import net.ytoec.kernel.dataobject.ServiceHistory;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ServiceHistoryDaoImplTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private ServiceHistoryDaoImpl<ServiceHistory>  dao;
	
//	@Test
//	public void testGetServiceHistoryList() {
//		Map map=new HashMap();
//		map.put("startIndex", 0);
//		map.put("pageNum", 60);
//		map.put("userId", 10);
//		
//		System.out.println("-----------"+dao.getServiceHistoryList(map).size());
//	}
	
	@Test
	public void testEdit()
	{
		ServiceHistory serviceHistory=new ServiceHistory();
		serviceHistory.setId(6);
		serviceHistory.setDserviceId(345);
		serviceHistory.setPayId(1);
		serviceHistory.setUserId(10);
		serviceHistory.setCircle("季");
		serviceHistory.setBeginDate(new Date());
		serviceHistory.setEndDate(new Date());
		serviceHistory.setAutoRenew("1");
		serviceHistory.setDealType("1");
		serviceHistory.setDealName("添加测试");
		serviceHistory.setSmsCount(100);
		serviceHistory.setCost(10.8);
		serviceHistory.setPromCost(12.5);
		serviceHistory.setRemark("修改信息了");
		
		System.out.println("-----------"+dao.edit(serviceHistory));
	}
}
