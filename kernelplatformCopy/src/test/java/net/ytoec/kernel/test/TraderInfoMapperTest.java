package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.mapper.TraderInfoMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * @author ChenRen
 * @date 2011-7-22
 */
public class TraderInfoMapperTest {

	private static TraderInfoMapper<TraderInfo> mapper;
	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;
	private static int tempOrderId = -1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		mapper = (TraderInfoMapper) ctx.getBean("traderInfoMapper");
	}

	@Test
	public void testAdd() {
		// 插入2条数据, 一条测试removeById, 一条测试delByOrderId
		for (int i = 1; i < 3; i++) {
			TraderInfo traderInfo = new TraderInfo();
			traderInfo.setName("ZhangSan");
			traderInfo.setAddress("HuaXuRoad");
			traderInfo.setOrderId(100 + i);
			mapper.add(traderInfo);
		}

	}

	@Test
	public void testGetTraderInfo() {

		List<TraderInfo> list = mapper.getAllTraderInfo();
		for (TraderInfo traderInfo : list) {

			// 取当前表中最大的Id值
			if (tempId < traderInfo.getId()) {
				tempId = traderInfo.getId();
				continue;
			}
		} // for

		TraderInfo traderInfo = new TraderInfo();
		traderInfo.setId(tempId);
		System.out.println(mapper.get(traderInfo));
	}

	@Test
	public void testTraderInfoList() {
		List<TraderInfo> traderInfo = mapper.getAllTraderInfo();
		System.out.println(traderInfo.size());
		System.out.println(traderInfo);
	}

	@Test
	public void testEdit() {
		TraderInfo traderInfo = new TraderInfo();
		traderInfo.setId(tempId);
		traderInfo = mapper.get(traderInfo);
		System.out.println(traderInfo);

		if (traderInfo == null)
			return;

		traderInfo.setName("ZhangSan_edtied");
		traderInfo.setAddress("QingPuQuHuaXuRoad_edtied");
		mapper.edit(traderInfo);
		System.out.println(mapper.get(traderInfo));
	}

	@Test
	public void testRemove() {
		TraderInfo traderInfo = new TraderInfo();
		traderInfo.setId(tempId);
		mapper.remove(traderInfo);
		System.out.println(mapper.get(traderInfo));
	}

	@Test
	public void testGetTraderInfoByOrderId() {

		List<TraderInfo> list = mapper.getAllTraderInfo();
		for (TraderInfo traderInfo : list) {

			// 取当前表中最大的OrderId值
			int orderId = traderInfo.getOrderId();
			if (tempOrderId < orderId) {
				tempOrderId = orderId;
				continue;
			}
		} // for

		System.out.println(mapper.getTraderInfoByOrderId(tempOrderId));
	}

	@Test
	public void testDelTraderInfoByOrderId() {
		System.out.println("受影响的行数: "
				+ mapper.delTraderInfoByOrderId(tempOrderId));
		System.out.println("再次查询该OrderId数据: "
				+ mapper.getTraderInfoByOrderId(tempOrderId));
	}

}
