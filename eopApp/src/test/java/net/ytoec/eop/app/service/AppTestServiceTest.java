package net.ytoec.eop.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.eop.app.bean.AppTest;
import net.ytoec.kernel.common.Pagination;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AppTestServiceTest {
	
	private static AppTestService<AppTest> appTestService;
	
	@BeforeClass
	@SuppressWarnings("unchecked")
	public static void setBeforeClass(){
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
		"classpath*:applicationContext-common.xml");
		appTestService = (AppTestService<AppTest>) ctx.getBean("appTestServiceImpl");
	}
	
	//@Test
	public void testAdd() {
		AppTest a = new AppTest();
		a.setId(3);
		a.setAppId(38);
		a.setClientId("ali.taobao.com");
		a.setCreateTime(new Date());
		a.setMsgType("1");
		a.setStatus("1");
		a.setOrderType("1");
		a.setTxLogisticId("LP00005385650361");
		a.setRemark("测试添");
		boolean rs = appTestService.add(a);
		Assert.assertTrue(rs);
	}

	//@Test
	public void testGet() {
		AppTest a = appTestService.get(1);
		System.out.println("物流号："+a.getTxLogisticsId()+" 客户端ID："+a.getClientId());
	}
	
	@Test
	public void testGetAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appId", 38);
		params.put("orderType", "offline");
		List<AppTest> list = appTestService.getAll(params);
		for(AppTest a : list){
			System.out.println("应用ID："+a.getAppId()+"	物流号："+a.getTxLogisticsId()+"	客户端ID："+a.getClientId()
					+"	订单类型："+a.getOrderType()+"	订单状态："+(a.getStatus().equals("1")?"成功":"失败")+" 错误消息："+a.getErrorMsg());
		}
	}
	
	//@Test
	public void testRemove() {
		boolean rs = appTestService.remove(4);
		Assert.assertTrue(rs);
	}

	//@Test
	public void testUpdate() {
		AppTest a = appTestService.get(1);
		a.setEncodeMsg("utf8编码过的字符");
		a.setDecodeMsg("按本地编码解码过的字符");
		boolean rs = appTestService.edit(a);
		Assert.assertTrue(rs);
	}

	
	//@Test
	@SuppressWarnings("unchecked")
	public void testGetPageList(){
		Pagination<AppTest> page = new Pagination<AppTest>(1, 2);
		System.out.println(page.getStartIndex());
		//page.setCurrentPage(2);
		Map<String, Object> params = new HashMap<String, Object>();
		
		page = appTestService.getPageList(page,params);
		System.out.println("共"+page.getTotalRecords()+"条记录，共"+page.getTotalPages()+"页");
		for(AppTest apptest : page.getRecords()){
			System.out.println("标题："+apptest.getTxLogisticsId());
		}
	}
	

}
