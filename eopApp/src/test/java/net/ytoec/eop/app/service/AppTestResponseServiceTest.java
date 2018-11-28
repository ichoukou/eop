package net.ytoec.eop.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.eop.app.bean.AppTestResponse;
import net.ytoec.kernel.common.Pagination;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AppTestResponseServiceTest {
	
	private static AppTestResponseService<AppTestResponse> appTestResService;
	
	@BeforeClass
	@SuppressWarnings("unchecked")
	public static void setBeforeClass(){
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
		"classpath*:applicationContext-common.xml");
		appTestResService = (AppTestResponseService<AppTestResponse>) ctx.getBean("appTestResponseServiceImpl");
	}
	
	//@Test
	public void testAdd() {
		AppTestResponse a = new AppTestResponse();
		a.setId(2);
		a.setAppTestId(1);
		a.setCreateTime(new Date());
		a.setStatus("1");
		a.setTxLogisticsId("LP00005385650361");
		a.setRemark("测试添");
		boolean rs = appTestResService.add(a);
		Assert.assertTrue(rs);
	}

	//@Test
	public void testGet() {
		AppTestResponse a = appTestResService.get(1);
		System.out.println("物流号："+a.getTxLogisticsId());
	}
	
	//@Test
	public void testGetAll() {
		List<AppTestResponse> list = appTestResService.getAll(null);
		for(AppTestResponse a : list){
			System.out.println("物流号："+a.getTxLogisticsId());
		}
	}
	
	//@Test
	public void testRemove() {
		boolean rs = appTestResService.remove(2);
		Assert.assertTrue(rs);
	}

	@Test
	public void testUpdate() {
		AppTestResponse a = appTestResService.get(1);
		a.setOperType("1");
		a.setErrorMsg("数字签名错误");
		boolean rs = appTestResService.edit(a);
		Assert.assertTrue(rs);
	}

	
	//@Test
	@SuppressWarnings("unchecked")
	public void testGetPageList(){
		Pagination<AppTestResponse> page = new Pagination<AppTestResponse>(1, 1);
		System.out.println(page.getStartIndex());
		//page.setCurrentPage(2);
		Map<String, Object> params = new HashMap<String, Object>();
		
		page = appTestResService.getPageList(page,params);
		System.out.println("共"+page.getTotalRecords()+"条记录，共"+page.getTotalPages()+"页");
		for(AppTestResponse a : page.getRecords()){
			System.out.println("标题："+a.getTxLogisticsId());
		}
	}
	

}
