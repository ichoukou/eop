package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dao.OrderStatusDao;
import net.ytoec.kernel.timer.WeightUpdaeTimer;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class WeightUpdateTest extends AbstractJUnit38SpringContextTests {

	@Inject
	public WeightUpdaeTimer weightUpdate;
	@Inject
	private OrderStatusDao dao;

	// 测试action
	@Test
	public void testAdd() throws Exception {
		 UpdateInfo info1 = new UpdateInfo();
	        UpdateInfo info2 = new UpdateInfo();
	        List<UpdateInfo> list  = new ArrayList<UpdateInfo>();
	        info1.setMailNo("7104962929");
	        info1.setOrderStatus("0");
	        info2.setMailNo("7104962864");
	        info2.setOrderStatus("0");
	        list.add(info1);
	        list.add(info2);
	        dao.batchUpdateOrderStatusByMailNo(list);
	}
}