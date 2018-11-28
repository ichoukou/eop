package net.ytoec.kernel.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.RechargeSMSFlagEnum;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.exception.BusinessException;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;

import org.apache.commons.collections.CollectionUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class PaymentServiceTest {

	public static PaymentService<Payment> service;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (PaymentService) ctx.getBean("paymentServiceImpl");
	}
	

	
//	@Test
//	public void testGetUsersByCode(){
//		//自动续费，插入超过时间的为付完款的信息
//	/*	SimpleDateFormat fm=new SimpleDateFormat("yyyy-MM-dd");
//		 Map pay_map=new HashMap();
//		 pay_map.put("userId", 10);
//		 pay_map.put("delFlag", PayEnumConstants.DEAL_FLAG.NDEL.value);
//		 pay_map.put("createTime", DateUtil.getDateAfter(new Date(),7));//7天前的日期
//		 System.out.println("==============="+ fm.format(DateUtil.getDateAfter(new Date(),7)));
//		 pay_map.put("dealStatus", PayEnumConstants.DEALSTATUS.WAIT.getValue());//0 等待付款 
//		List<Payment> paymentList= service.getPaymentListByUserId(pay_map);
//		StringBuffer sb=new StringBuffer();
//		 if(CollectionUtils.isNotEmpty(paymentList)){
//			 System.out.println("================= "+paymentList.size());
//		 }*/
//		 Map pay_map=new HashMap();
//		 pay_map.put("userId", 10);
//		 pay_map.put("userId", 10);
//		 pay_map.put("delFlag", PayEnumConstants.DEAL_FLAG.NDEL.value);
//		 Payment payment=new Payment();
//		 payment.setUserId(10);
//		 payment.setAuserId(2);
//		 payment.setDealMoney(1000.00);
//		 payment.setDealName("这天记录是测试test");
//		 payment.setDealType("0");
//		// service.dealPayment(payment);
//	 	}
	
	@Test
	public void testGetUsersByCode(){
		//自动续费，插入超过时间的为付完款的信息
	/*	SimpleDateFormat fm=new SimpleDateFormat("yyyy-MM-dd");
		 Map pay_map=new HashMap();
		 pay_map.put("userId", 10);
		 pay_map.put("delFlag", PayEnumConstants.DEAL_FLAG.NDEL.value);
		 pay_map.put("createTime", DateUtil.getDateAfter(new Date(),7));//7天前的日期
		 System.out.println("==============="+ fm.format(DateUtil.getDateAfter(new Date(),7)));
		 pay_map.put("dealStatus", PayEnumConstants.DEALSTATUS.WAIT.getValue());//0 等待付款 
		List<Payment> paymentList= service.getPaymentListByUserId(pay_map);
		StringBuffer sb=new StringBuffer();
		 if(CollectionUtils.isNotEmpty(paymentList)){
			 System.out.println("================= "+paymentList.size());
		 }*/
		/* Map pay_map=new HashMap();
		 pay_map.put("userId", 10);
		 pay_map.put("userId", 10);
		 pay_map.put("delFlag", PayEnumConstants.DEAL_FLAG.NDEL.value);
		 Payment payment=new Payment();
		 payment.setUserId(10);
		 payment.setAuserId(2);
		 payment.setDealMoney(1000.00);
		 payment.setDealName("这天记录是测试test");
		 payment.setDealType("0");*/
		// service.dealPayment(payment);
		//测试支付宝
		/*Map<String,Object> map=new HashMap<String,Object>();
		Payment payment=new Payment();
		
		//1)封装 payment对象的值	
		payment.setUserId(10);
		payment.setUserName("测试人");
		//payment.setAuserId(auserId);
		payment.setDealMoney(1.00);
		payment.setDealName(RechargeSMSFlagEnum.SPAYMENT.getName());
		payment.setDealType(PayEnumConstants.DEALTYPE.ONLINE.getValue()); //在线充值
		payment.setFlag(PayEnumConstants.DEALFLAG_FLAG.IN.value);//进账
		payment.setDelFlag(PayEnumConstants.DEAL_FLAG.NDEL.value);   
		payment.setDealStatus(PayEnumConstants.DEALSTATUS.WAIT.getValue());
		
		map.put("payment", payment);//在线充值传值
		String alipayURL=service.dealPayment(map);
		System.out.println("==========支付宝路径========= "+alipayURL);*/
		
		//2)测试支付宝返回
		//2.1)购买服务
		/*Map<String,String> map=new HashMap<String,String>();
		map.put("trade_no", "123456");
		map.put("out_trade_no", "193");
		map.put("total_fee", "15");
		map.put("subject", "问题件");
		map.put("body", "6");
		map.put("buyer_email", "lee_buddy@163.com");
		map.put("trade_status", "true");
		service.alipayReturn(map);*/
		
		//2.2)在线充值
		/*Map<String,String> map=new HashMap<String,String>();
		map.put("trade_no", "123456");
		map.put("out_trade_no", "21");
		map.put("total_fee", "1000");
		map.put("subject", "问题件");
		map.put("body", "5");
		map.put("buyer_email", "lee_buddy@163.com");
		map.put("trade_status", "true");
		service.alipayReturn(map);*/
		//第一个
		/*Map<String,String> map=new HashMap<String,String>();
		map.put("trade_no", "123456");
		map.put("out_trade_no", "20120903092536158");
		map.put("total_fee", "200");
		map.put("subject", "200元超值套餐");
		map.put("buyer_email", "fancy-eyes@163.com");
		map.put("trade_status", "true");
		service.alipayReturn(map);*/
		
		//第二个 [调用支付宝]订单号20120903070923157
		/*Map<String,String> map=new HashMap<String,String>();
		map.put("trade_no", "123456");
		map.put("out_trade_no", "20120903070923157");
		map.put("total_fee", "50");
		map.put("subject", "50元超值套餐");
		map.put("buyer_email", "yxt1981@126.com");
		map.put("trade_status", "true");
		service.alipayReturn(map);*/
	
	 	}

	public void testGetList()
	{
	/*	Map map=new HashMap();
		map.put("userId", 10);
		map.put("dealStatus","4");//未付款
		map.put("delFlag", "2");//未关闭
		map.put("dealName","交易名称");//交易名称
		;
		System.out.println(service.getList(map).size());*/
		
	}
	
//	@Test
//	public void testGet() {
//		
//			Map<String,String> map=new HashMap<String,String>();
//		map.put("trade_no", "123456");
//		map.put("out_trade_no", "526");
//		map.put("total_fee", "0.03");
//		map.put("subject", "时效提醒");
//		//map.put("body", "161");
//		map.put("buyer_email", "lee_buddy@163.com");
//		map.put("trade_status", "true");
//		service.alipayReturn(map,"0SDK-EAA-6688-JEUTM","799634");
//	}


}
