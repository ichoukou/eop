package net.ytoec.kernel.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.mapper.OrderMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class OrderMapperTest extends AbstractJUnit38SpringContextTests {

	/*
	 * 1. sql配置文件中的表明、字段名应该大写; 刚因为小写了表名, 报错：表不存在... 2. create_time刚开始在DB中不显示时间,
	 * 勾选NOT NULL 才显示 3. 使用注解测试, 要继承 AbstractJUnit38SpringContextTests 才能注入值
	 */

	@Inject
	private OrderMapper<Order> mapper;

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

	@Test
	public void testAdd() {

		Order order = new Order();
		order.setVersion("V 0.0.1");
		order.setLogisticProviderId("lp001");
		order.setTxLogisticId("No lp001");
		order.setTradeNo("20110720001");
		order.setCustomerId("c1");
		order.setMailNo("0571115987");
		order.setType("I");
		order.setFlag("Y");
		order.setInsuranceValue(2.23f);
		order.setPackageOrNot("Y");
		order.setSpecial("test");
		order.setRemark("add...");
		order.setClientId("ip:4.110");
		order.setStatus("1");

		mapper.add(order);

		System.out.println(mapper.get(order));
	}

	@Test
	public void testGet() {
		List<Order> list = mapper.getAllOrder();
		for (Order order : list) {

			// 取当前表中最大的Id值
			if (tempId < order.getId()) {
				tempId = order.getId();
				continue;
			}
		} // for

		Order order = new Order();
		order.setId(tempId);

		System.out.println(mapper.get(order));
	}

	@Test
	public void testEdit() {
		Order order = new Order();
		order.setId(tempId);

		order = mapper.get(order);
		System.out.println(order);

		order.setVersion("V 0.0.2");
		order.setTradeNo("20110720002");
		order.setMailNo("0571115987");
		order.setRemark("edit...");

		mapper.edit(order);
		System.out.println(mapper.get(order));
	}

	@Test
	public void testRemove() {
		Order order = new Order();
		order.setId(tempId);

		mapper.remove(order);
		System.out.println(mapper.get(order));
	}
	
	@Test
	public void testECAccount() {
//		System.out.println(mapper.getAllOrder().size());
		System.out.println(mapper.ecAccount(new HashMap()).size());
	}
	
	/**
	 * @author ChenRen/2011-09-26
	 * 电子对账采用新的数据查询方法; 不用三表联查，改用两表联查，在程序中处理地区数据
	 */
	@Test
	public void testECAccountHalf() {
		Map map = new HashMap();
		map.put("addr", "fromAddr");
		map.put("tradeType", "0");
		map.put("starttime", "2011-09-01");
		map.put("endtime", "2011-09-26");
		map.put("vipId", "2");
		
		List list = mapper.ecAccountHalf(map);
		for (Object obj : list) {
			System.out.println(obj);
		}
	}
	
}
