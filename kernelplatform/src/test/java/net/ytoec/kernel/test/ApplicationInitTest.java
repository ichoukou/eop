package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.service.OrderStatusUpdService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ApplicationInitTest extends AbstractJUnit38SpringContextTests {
	
	@Inject
	OrderStatusUpdService orderStatusUpdService;
	
	@Test
	public void test(){
		List<Object[]> lists = new ArrayList<Object[]>();
		List<UpdateInfo> list = new ArrayList<UpdateInfo>();
		UpdateInfo info1 = new UpdateInfo();
		info1.setStatus("1");
		info1.setClientId("Taobao");
		info1.setMailNo("525698742356");
		info1.setTxLogisticID("LP00011010631102");
		info1.setType("ACCEPT");
		info1.setRemark("测试数据0");
		list.add(info1);
		
		UpdateInfo info2 = new UpdateInfo();
		info2.setStatus("1");
		info2.setClientId("Taobao");
		info2.setMailNo("525698742356");
		info2.setTxLogisticID("LP00010997976347");
		info2.setType("ACCEPT");
		info2.setRemark("测试数据1");
		list.add(info2);
		
		UpdateInfo info3 = new UpdateInfo();
		info3.setStatus("1");
		info3.setClientId("Taobao");
		info3.setMailNo("525698742356");
		info3.setTxLogisticID("LP00011014266240");
		info3.setType("ACCEPT");
		info3.setRemark("测试数据2");
		list.add(info3);
		
		orderStatusUpdService.batchUpdateOrdStatus(list, list);
	}
	

}
