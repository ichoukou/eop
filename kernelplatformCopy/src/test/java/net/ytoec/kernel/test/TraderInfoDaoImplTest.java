package net.ytoec.kernel.test;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dataobject.TraderInfo;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-22
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class TraderInfoDaoImplTest extends AbstractJUnit38SpringContextTests {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;
	private static int tempOrderId = -1;

	@Inject
	private TraderInfoDao<TraderInfo> dao;

	@Test
	public void testAddTraderInfo() {
		// 插入2条数据, 一条测试removeById, 一条测试delByOrderId
		for (int i = 1; i < 3; i++) {
			TraderInfo traderInfo = new TraderInfo();
			traderInfo.setName("ZhangSan");
			traderInfo.setAddress("HuaXuRoad");
			traderInfo.setOrderId(100 + i);
			dao.addTraderInfo(traderInfo);
		}
	}

	//@Test
	public void testGetTraderInfoById() {
		List<TraderInfo> list = dao.getAllTraderInfo();
		for (TraderInfo order : list) {

			// 取当前表中最大的Id值
			int id = order.getId();
			if (tempId < id) {
				tempId = id;
				continue;
			}
		} // for

		System.out.println(dao.getTraderInfoById(tempId));
	}

	//@Test
	public void testEditTraderInfo() {
		TraderInfo traderInfo = dao.getTraderInfoById(tempId);
		System.out.println(traderInfo);

		if (traderInfo == null)
			return;

		traderInfo.setName("ZhangSan_edtied");
		traderInfo.setAddress("QingPuQuHuaXuRoad_edtied");
		dao.editTraderInfo(traderInfo);
		System.out.println(dao.getTraderInfoById(tempId));
	}

	//@Test
	public void testRemoveTraderInfo() {
		TraderInfo traderInfo = dao.getTraderInfoById(tempId);
		if (traderInfo == null)
			return;

		System.out.println("删除结果: " + dao.delTraderInfo(traderInfo));
		System.out.println(dao.getTraderInfoById(tempId));
	}

	//@Test
	public void testGetTraderInfoByOrderId() {

		List<TraderInfo> list = dao.getAllTraderInfo();
		for (TraderInfo traderInfo : list) {

			// 取当前表中最大的OrderId值
			int orderId = traderInfo.getOrderId();
			if (tempOrderId < orderId) {
				tempOrderId = orderId;
				continue;
			}
		} // for

		System.out.println(dao.getTraderInfoByOrderId(tempOrderId));
	}

	//@Test
	public void testDelTraderInfoByOrderId() {
		System.out.println("删除结果: " + dao.delTraderInfoByOrderId(tempOrderId));
		System.out.println("再次查询该OrderId数据: "
				+ dao.getTraderInfoByOrderId(tempOrderId));
	}

}
