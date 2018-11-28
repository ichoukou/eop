package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.service.JgWaybillService;
import net.ytoec.kernel.service.OrderService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class JgWaybillServiceTest extends AbstractJUnit38SpringContextTests {
	
	/*
	 * 首先报错：OrderDao找不到bean... 注入失败.
	 * 因为实现类中没有注解：@Component
	 */

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;
	
	
	@Inject
	private JgWaybillService service;

	@Test
	public void testAdd() {
		 List jgWaybill=new ArrayList();
	      JgWaybill jb=new JgWaybill();
	        jb.setbAddress("1111");
	        jb.setbCity("1111");
	        jb.setbDistrict("1111");
	        jb.setbMobile("1111");
	        jb.setbName("1111");
	        jb.setbPhone("1111");
	        jb.setLogisticId("1111");
	        jb.setsName("1111");
	        jb.setClientID("SIMDOO");
	        jgWaybill.add(jb);
	
		service.sendPrintOrders(jgWaybill);
	}
}
