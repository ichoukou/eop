package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.service.ZebraForewarnService;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ZebraSurfacebillServiceTest extends
		AbstractJUnit38SpringContextTests {

	@Autowired
	private ZebraSurfacebillService service;
	
	@Autowired
	private ZebraSurfacebillDao<ZebraSurfacebill> dao;

	@Autowired
	private ZebraForewarnService<ZebraForewarn> zebraForewarnService;


	// 测试从金刚同步电子面单到易通
	@Test
	public void testWaybillTimer() {
		service.waybillTimer();
	}

	// 测试添加电子面单
	@Test
	public void testInsertBill() {
		for (int i = 0; i < 20; i++) {
			Date date = new Date();
			ZebraSurfacebill bill = new ZebraSurfacebill();
			bill.setWaybillNo((800111000 + i) + "");
			bill.setVersionNo("1");
			bill.setPrint(false);
			bill.setUse(false);
			bill.setFbCreatedate(date);
			bill.setCustomerCode("Ke0000140");
			service.insertBill(bill);
		}
	}

	// 测试批量插入仓配通回传电子面单
	public void testBatchInsert() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, String>> waybillNos = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("waybillNo", "1");
		map.put("orderNo", "1");
		waybillNos.add(map);
		map = new HashMap<String, String>();
		map.put("waybillNo", "2");
		map.put("orderNo", "2");
		waybillNos.add(map);
		map = new HashMap<String, String>();
		map.put("waybillNo", "3");
		map.put("orderNo", "3");
		waybillNos.add(map);
		String customerCode = "1";
		params.put("waybillNos", waybillNos);
		params.put("customerCode", customerCode);
		int rows = service.addBatch(waybillNos, customerCode);
		System.out.println(rows);
	}

	// 测试批量删除仓配通回传电子面单
	public void testBatchDelete() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> waybillNos = new ArrayList<String>();
		waybillNos.add("1");
		waybillNos.add("2");
		waybillNos.add("3");
		String customerCode = "1";
		params.put("waybillNos", waybillNos);
		params.put("customerCode", customerCode);
		int rows = service.batchDelete(waybillNos, customerCode);
		System.out.println(rows);
	}

	/**
	 * 测试: <br>
	 * 易通给仓配通的某商家下发多少条电子面单，<br>
	 * 就相应的删除回传电子面单中某商家的多少条数据
	 */
	public void testDeleteByCustomerCodeAndCounts() {
		String customerCode = "1";

		ZebraForewarn zebraForewarn = zebraForewarnService
				.selectByCustomerCode(customerCode);
		int counts = 0;
		if (zebraForewarn != null && zebraForewarn.isInitState()) {
			counts = zebraForewarn.getCustomerWarnValue();
		} else {
			counts = service.selectCountsByCustomerCode(customerCode);
		}

		List<ZebraSurfacebill> waybills = service
				.selectZebraSurfacebillsByCustomerCode(customerCode, counts,0);
		List<String> waybillNos = new ArrayList<String>();
		for (ZebraSurfacebill zebraSurfacebill : waybills) {
			waybillNos.add(zebraSurfacebill.getWaybillNo());
		}
		if (!waybillNos.isEmpty()) {
			int rows1 = service.batchUpdateUseState(waybillNos,1);
			System.out.println(rows1);

		}

		List<String> deleteWaybillNos = service.selectByCustomerCodeAndCounts(
				waybillNos.size(), customerCode);
		if (!deleteWaybillNos.isEmpty()) {
			int rows = service.batchDelete(deleteWaybillNos, customerCode);
			System.out.println(rows);
		}

		// 第一次给仓配通某商家下单面单后，设置为不是初始状态
		if (zebraForewarn != null && zebraForewarn.isInitState()) {
			zebraForewarnService.updateInitStateByCustomerCode(customerCode);
		}
	}

	// 测试生成获取电子面单的请求序列
	@Test
	public void testGenerateSequence() {
		String customerCode = "1";
		String parternCode = "1111";
		System.out.print(service.insertSequence(customerCode, parternCode));
	}

	// 测试验证请求序列
	@Test
	public void testValidateSequence() {
		String sequence = "97";
		String customerCode="K10101010";
		System.out.print(service.validateSequence(customerCode,sequence));
	}
	
	// 测试批量电子面单
	public void testBatchUpdateUseState(){
		List<String> waybillNos = new ArrayList<String>();
		waybillNos.add("V000000000");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		params.put("state", 1);
		params.put("sequence", 1);
		int batchUpdateUseState = dao.batchUpdateUseState(params);
		System.out.print(batchUpdateUseState);
	}

}
