
package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.TraderInfo;
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
public class OrderServiceTest extends AbstractJUnit38SpringContextTests {
	
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
	private OrderService<Order> service;

	@Test
	public void testAdd() {
//		for(int i=0;i<10000000;i++){
//		Order order = new Order();
//		order.setVersion("V 0.1.0");
//		order.setLogisticProviderId("lp00211111");
//		order.setTxLogisticId("test_1111111");
//		order.setTradeNo("20110720100");
//		order.setCustomerId("c2");
//		order.setMailNo("0571115987");
//		order.setType("I");
//		order.setFlag("Y");
//		order.setInsuranceValue(2.23f);
//		order.setPackageOrNot("Y");
//		order.setSpecial("dao test...");
//		order.setRemark("dao test. add.");
//		order.setClientId("ip:4.110");
//		order.setStatus("1");
//		order.setOrderType(Integer.valueOf("1"));
//		order.setServiceType(Long.valueOf("1"));
//		order.setGoodsValue(Double.valueOf("1.1"));
//		order.setItemsValue(Double.valueOf("1.1"));
//		order.setTotalServiceFee(Double.valueOf("1.1"));
//		order.setBuyServiceFee(Double.valueOf("1.1"));
//		order.setCodSplitFee(Double.valueOf("1.1"));
//			try {
//			service.addOrder(order);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		long mailNo = 6031446234l;

//			Order order=service.getOrderById(1502);
//			System.out.println(111);
			// long j = 6031446234l;
			int i = 0;
		// for (long j = 6031446234l; j <= 6031446266l; j++) {
			
			//wusha19860530,梦雁别乡,roses367
			//2b3904a57250e6da3bff95fcf8692ad2   swpuxiaomei
			String[] customerIds = new String[]{"e1418fa960674361acdb9ecab9914073","3a8023a5e1ef965f511acf63816223b1","97aae6ebce93ad312dcb3b3aeae9d4dd","e217451c55dc86b3fb1c6b7f3e76d845","2b3904a57250e6da3bff95fcf8692ad2"};
			String[] prov = new String[]{"上海","江苏省","浙江省","陕西省","安徽省"};
			String[] productString = new String[]{"面包--昇鹏说名字要长---------还要换行------------","喔喔奶糖--昇鹏说名字要长---------还要换行------------","围巾--昇鹏说名字要长---------还要换行------------","眼镜--昇鹏说名字要长---------还要换行------------","电脑--昇鹏说名字要长---------还要换行------------","鞋子--昇鹏说名字要长---------还要换行------------","手机--昇鹏说名字要长---------还要换行------------这个能换行吧-----------------","小米科技小米手机"};
			Random random=new Random();
			Random randomNew=new Random();
		for (i = 990530; i < 990531; i++) {
			
			Integer index=randomNew.nextInt(5);
			TraderInfo sender=new TraderInfo();
			sender.setAddress("天山路1号------------------------------------------------------------------地址");
			sender.setCity("乌鲁木齐");
			sender.setDistrict("天山区");
			sender.setMobile("0917-6421768");
			sender.setName("1name"+i);
			sender.setPhone("15129002762");
			sender.setPostCode("123456");
			sender.setTradeType("1");
			sender.setProv(prov[index]);
			TraderInfo receiver=new TraderInfo();
			receiver.setAddress("东成路383弄34号----------------------------------------------------地址");
			receiver.setCity("北京市");
			receiver.setDistrict("东城区");
			receiver.setMobile("0917-6421765");
			receiver.setName("2n"+i);
			receiver.setPhone("18392162193");
			receiver.setPostCode("654321");
			receiver.setTradeType("2");
			receiver.setProv(" 北京");
			List<Product> products =new ArrayList<Product>();
			OrderFormInfo orderFormInfo=new OrderFormInfo();
			orderFormInfo.setTxLogisticId("LP1111111"+i);
			orderFormInfo.setBuyServiceFee("0");
			orderFormInfo.setClientId("TAOBAO");
			orderFormInfo.setCodSplitFee("0");
			orderFormInfo.setCustomerId(customerIds[index]);
			orderFormInfo.setFlag("0");
			orderFormInfo.setGoodsValue("10");
			orderFormInfo.setInsuranceValue(0);
			orderFormInfo.setItemsValue("10");
			orderFormInfo.setLineType("1");
			orderFormInfo.setLogisticProviderId("YTO");
			orderFormInfo.setMailNo("");
			//orderFormInfo.setMailNo("0571" + i);
			// orderFormInfo.setMailNo(String.valueOf(j));
			orderFormInfo.setOrderType("0");
			orderFormInfo.setPackageOrNot(true);
			orderFormInfo.setReceiver(receiver);
			orderFormInfo.setRemark("0");
			orderFormInfo.setSendEndTime("2005-08-24 12:00:00.0 CST");
			orderFormInfo.setSender(sender);
			orderFormInfo.setSendStartTime("2005-08-24 08:00:00.0 CST");
			orderFormInfo.setServiceType("0");
			orderFormInfo.setTotalServiceFee("0");
			orderFormInfo.setTradeNo("1110000000"+i);
			orderFormInfo.setVersion("1.0");
			orderFormInfo.setVipId("2");
			Date now=new Date();
			Product product1=new Product();
			product1.setItemName(productString[index]);
			product1.setItemNumber(1);
			product1.setItemValue(1001);
			product1.setLogisticId(orderFormInfo.getTxLogisticId());
			product1.setMailNo(orderFormInfo.getMailNo());
			product1.setCreateTime(now);
			Product product2=new Product();
			product2.setItemName(productString[index]);
			product2.setItemNumber(1);
			product2.setItemValue(1002);
			product2.setLogisticId(orderFormInfo.getTxLogisticId());
			product2.setMailNo(orderFormInfo.getMailNo());
			product2.setCreateTime(now);
			Product product3=new Product();
			product3.setItemName(productString[index]);
			product3.setItemNumber(1);
			product3.setItemValue(1003);
			product3.setLogisticId(orderFormInfo.getTxLogisticId());
			product3.setMailNo(orderFormInfo.getMailNo());
			product3.setCreateTime(now);
			
//			Product product4=new Product();
//			product4.setItemName(productString[indexNew]);
//			
//			product4.setItemNumber(1);
//			product4.setItemValue(1003);
//			product4.setLogisticId(orderFormInfo.getTxLogisticId());
//			product4.setMailNo(orderFormInfo.getMailNo());
//			product4.setCreateTime(now);
//			Product product5=new Product();
//			product5.setItemName(productString[indexNew]);
//			product5.setItemNumber(1);
//			product5.setItemValue(1003);
//			product5.setLogisticId(orderFormInfo.getTxLogisticId());
//			product5.setMailNo(orderFormInfo.getMailNo());
//			product5.setCreateTime(now);
//			Product product6=new Product();
//			product6.setItemName(productString[indexNew]);
//			product6.setItemNumber(1);
//			product6.setItemValue(1003);
//			product6.setLogisticId(orderFormInfo.getTxLogisticId());
//			product6.setMailNo(orderFormInfo.getMailNo());
//			product6.setCreateTime(now);
//			Product product7=new Product();
//			product7.setItemName(productString[indexNew]);
//			product7.setItemNumber(1);
//			product7.setItemValue(1003);
//			product7.setLogisticId(orderFormInfo.getTxLogisticId());
//			product7.setMailNo(orderFormInfo.getMailNo());
//			product7.setCreateTime(now);
//			Product product8=new Product();
//			product8.setItemName(productString[indexNew]);
//			product8.setItemNumber(1);
//			product8.setItemValue(1003);
//			product8.setLogisticId(orderFormInfo.getTxLogisticId());
//			product8.setMailNo(orderFormInfo.getMailNo());
//			product8.setCreateTime(now);
//			Product product9=new Product();
//			product9.setItemName(productString[indexNew]);
//			product9.setItemNumber(1);
//			product9.setItemValue(1003);
//			product9.setLogisticId(orderFormInfo.getTxLogisticId());
//			product9.setMailNo(orderFormInfo.getMailNo());
//			product9.setCreateTime(now);
//			Product product10=new Product();
//			product10.setItemName(productString[indexNew]);
//			product10.setItemNumber(1);
//			product10.setItemValue(1003);
//			product10.setLogisticId(orderFormInfo.getTxLogisticId());
//			product10.setMailNo(orderFormInfo.getMailNo());
//			product10.setCreateTime(now);
//			Product product11=new Product();
//			product11.setItemName(productString[indexNew]);
//			product11.setItemNumber(1);
//			product11.setItemValue(1003);
//			product11.setLogisticId(orderFormInfo.getTxLogisticId());
//			product11.setMailNo(orderFormInfo.getMailNo());
//			product11.setCreateTime(now);
//			Product product12=new Product();
//			product12.setItemName(productString[indexNew]);
//			product12.setItemNumber(1);
//			product12.setItemValue(1003);
//			product12.setLogisticId(orderFormInfo.getTxLogisticId());
//			product12.setMailNo(orderFormInfo.getMailNo());
//			product12.setCreateTime(now);
//			Product product13=new Product();
//			product13.setItemName(productString[indexNew]);
//			product13.setItemNumber(1);
//			product13.setItemValue(1003);
//			product13.setLogisticId(orderFormInfo.getTxLogisticId());
//			product13.setMailNo(orderFormInfo.getMailNo());
//			product13.setCreateTime(now);
//			Product product14=new Product();
//			product14.setItemName(productString[indexNew]);
//			product14.setItemNumber(1);
//			product14.setItemValue(1003);
//			product14.setLogisticId(orderFormInfo.getTxLogisticId());
//			product14.setMailNo(orderFormInfo.getMailNo());
//			product14.setCreateTime(now);
//			Product product15=new Product();
//			product15.setItemName(productString[indexNew]);
//			product15.setItemNumber(1);
//			product15.setItemValue(1003);
//			product15.setLogisticId(orderFormInfo.getTxLogisticId());
//			product15.setMailNo(orderFormInfo.getMailNo());
//			product15.setCreateTime(now);
//			
			products.add(product1);
			products.add(product2);
			products.add(product3);
//			products.add(product4);
//			products.add(product5);
//			products.add(product6);
//			products.add(product7);
//			products.add(product8);
//			products.add(product9);
//			products.add(product10);
//			products.add(product12);
//			products.add(product13);
//			products.add(product14);
//			products.add(product15);
//			products.add(product11);
			orderFormInfo.setItems(products);
			orderFormInfo.setLineType("0");
			orderFormInfo.setRemark("ytotest88888888");
			service.addOrderFormInfo(orderFormInfo);
			}

	}
}
