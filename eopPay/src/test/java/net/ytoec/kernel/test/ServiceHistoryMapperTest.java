package net.ytoec.kernel.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.mapper.ServiceHistoryMapper;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ServiceHistoryMapperTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private ServiceHistoryMapper<ServiceHistory>  mapper;
	
//	@Test
//	public void testAdd() {
//		ServiceHistory serviceHistory=new ServiceHistory();
//		serviceHistory.setDserviceId(2);
//		serviceHistory.setPayId(1);
//		serviceHistory.setUserId(10);
//		serviceHistory.setCircle("月");
//		serviceHistory.setBeginDate(new Date());
//		serviceHistory.setEndDate(new Date());
//		serviceHistory.setAutoRenew("1");
//		serviceHistory.setDealType("1");
//		serviceHistory.setDealName("DealName");
//		serviceHistory.setSmsCount(100);
//		serviceHistory.setCost(10.8);
//		serviceHistory.setPromCost(12.5);
//		serviceHistory.setRemark("备注");
//		
//		mapper.add(serviceHistory);
//		System.out.println("-----------");
//	}
	
	
//	@Test
//	public void testGetServiceHistoryList() {
//		Map map=new HashMap();
//		map.put("startIndex", 0);
//		map.put("pageNum", 100);
//		map.put("userId", 11);
//		System.out.println("-----------"+mapper.getServiceHistoryList(map).size());
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
		mapper.edit(serviceHistory);
		System.out.println("-----------");
	}
}

