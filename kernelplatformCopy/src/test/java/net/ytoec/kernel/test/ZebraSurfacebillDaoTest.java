package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-common.xml")
public class ZebraSurfacebillDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraSurfacebillDao<ZebraSurfacebill> dao;

	//修改面单状态
	@Test
	public void testUpdateStateBySequence(){
		dao.updateStateBySequence("12226", 0);
	}
	
	// 删除商家一定数量的回单
	@Test
	public void testDeleteUploadBillByLimit(){
		int count = dao.deleteUploadBillByLimit("K22000014", 1);
		System.out.println(count+"==================");
	}
	
	// 查询商家一定数量的面单
	@Test
	public void testQuerySurfaceBillListByLimit(){
//		List<String> list =  dao.selectByCustomerCodeAndCounts(1,"K22000014");
		
		List<String> list = dao.querySurfaceBillListByLimit("K22000014", 20);
		System.out.println(list.size());
	}
	
	// 测试根据商家代码查询电子面单
	@Test
	public void testSelectZebraSurfacebillsByCustomerCode() {
		String customerCode = "1";
		int counts = 1;
		List<ZebraSurfacebill> zebraSurfacebills = dao
				.selectZebraSurfacebillsByCustomerCode(customerCode, counts,0);
		System.out.println(zebraSurfacebills);
	}

	// 测试批量更新面单下发(即:使用)状态
	@Test
	public void testBatchUpdateUseState() {
		List<String> waybillNos = new ArrayList<String>();
		waybillNos.add("1");
		waybillNos.add("2");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		int rows = dao.batchUpdateUseState(params);
		System.out.println(rows);
	}

	// 测试批量更新面单打印状态
	@Test
	public void testBatchUpdatePrintState() {
		List<String> waybillNos = new ArrayList<String>();
		waybillNos.add("1");
		waybillNos.add("2");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		int rows = dao.batchUpdatePrintState(params);
		System.out.println(rows);
	}

	// 测试插入电子面单
	@Test
	public void testInertBill() {
		for (int i = 1; i <= 20; i++) {
			ZebraSurfacebill bill = new ZebraSurfacebill();
			bill.setWaybillNo((1020 + i) + "");
			bill.setCustomerCode("k10101010");
			Date date=new Date();
			bill.setFbCreatedate(date);
			dao.insertBill(bill);
		}
	}

	// 测试根据商家编码查询商家未下发(即:未使用)状态的电子面单数量
	@Test
	public void testSelectNoUseCounts() {
		String customerCode = "1";
		int counts = dao.selectCountsByState(customerCode, 0);
		System.out.println(counts);
	}

}
