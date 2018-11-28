package net.ytoec.kernel.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.MailNoInfo;
import net.ytoec.kernel.dataobject.UserWaybillInfo;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;

import com.sun.mail.util.XmlJaxbMapper;

public class ZebraSurfacebillDaoTest extends ParentTest {

	@Test
	public void testBatchUpdateStatebyQuantity(){
		zebraSurfacebillDao.batchUpdateStatebyQuantity("K10101011", 10,"10" );
	}
	
	// 批量插入面单数据 
	@Test
	public void testBatchInsertSurfaceBill() {
		List<MailNoInfo> mailNoList = new ArrayList<MailNoInfo>();
		try {
			for(int i = 100000 ;i<102000;i++){
				String incrementNo = new SimpleDateFormat("sSSS")
				.format(new Date());
//				Thread.sleep(1l);
				MailNoInfo info = new MailNoInfo();
				info.setCreateTime(new Date());
				info.setCustomerCode("K10000033");
				info.setWaybillNo(incrementNo+i);
				mailNoList.add(info);
			}
			zebraSurfacebillDao.batchInsert(mailNoList);
		}catch(DuplicateKeyException e){
			System.out.println("]]]]]]]]]]]]]]]]]]]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 统计面单数据
	@Test
	public void testCountWaybillInfo(){
		try {
			List<String> customerCodeList = new ArrayList<String>();
//			customerCodeList.add("K10000172");
			customerCodeList.add("K10101011");
			List<UserWaybillInfo> list = zebraSurfacebillDao.countWaybillInfo(customerCodeList);
			UserWaybillInfo info = list.get(0);
			System.out.println(XmlJaxbMapper.writeValue(info));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 根据序列号和商家编码查询面单数据
	@Test
	public void queryWaybillBySequence(){
		try {
			List<String> list = zebraSurfacebillDao.queryWaybillBySequence("K10101011", 385);
			System.out.println(list.toArray());
		} catch (Exception e) {
			e.printStackTrace()	;
		}
	}
	
	// 修改面单状态
	@Test
	public void testUpdateStateBySequence() {
		zebraSurfacebillDao.updateStateBySequence("12226", 0);
	}

	// 补齐上传单
	@Test
	public void testBatchInsertUploadOrder() throws Exception {
		String customerCode = "K10000172";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 10000; i++) {
			// 自动生成一个编号作为面单号，防止唯一建
			String incrementNo = new SimpleDateFormat("yyyyMMddHHmmssSSS")
					.format(new Date());
			Thread.sleep(5l);
			Map<String, String> map = new HashMap<String, String>();
			map.put("waybillNo", incrementNo);
			map.put("orderNo", incrementNo);
			map.put("customerCode", customerCode);
			list.add(map);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", list);
		params.put("customerCode", customerCode);
		zebraSurfacebillDao.batchInsert(params);
	}

	// 删除商家一定数量的回单
	@Test
	public void testDeleteUploadBillByLimit() {
		int count = zebraSurfacebillDao.deleteUploadBillByLimit("K22000014", 1);
		System.out.println(count + "==================");
	}

	// 查询商家一定数量的面单
	@Test
	public void testQuerySurfaceBillListByLimit() {
		// List<String> list =
		// zebraSurfacebillDao.selectByCustomerCodeAndCounts(1,"K22000014");

		List<String> list = zebraSurfacebillDao.querySurfaceBillListByLimit(
				"K22000014", 20);
		System.out.println(list.size());
	}

	// 测试根据商家代码查询电子面单
	@Test
	public void testSelectZebraSurfacebillsByCustomerCode() {
		String customerCode = "1";
		int counts = 1;
		List<ZebraSurfacebill> zebraSurfacebills = zebraSurfacebillDao
				.selectZebraSurfacebillsByCustomerCode(customerCode, counts, 0);
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
		int rows = zebraSurfacebillDao.batchUpdateUseState(params);
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
		int rows = zebraSurfacebillDao.batchUpdatePrintState(params);
		System.out.println(rows);
	}

	// 测试插入电子面单
	@Test
	public void testInertBill() {
		for (int i = 1; i <= 20; i++) {
			ZebraSurfacebill bill = new ZebraSurfacebill();
			bill.setWaybillNo((8 + i) + "");
			bill.setCustomerCode("k10101010");
			Date date = new Date();
			bill.setFbCreatedate(date);
			zebraSurfacebillDao.insertBill(bill);
		}
	}

	// 测试根据商家编码查询商家未下发(即:未使用)状态的电子面单数量
	@Test
	public void testSelectNoUseCounts() {
		String customerCode = "1";
		int counts = zebraSurfacebillDao.selectCountsByState(customerCode, 0);
		System.out.println(counts);
	}
	
	@Test
	public void testTr(){
		try
		{
			System.out.println(
					"========================");
			zebraSurfacebillService.exportWaybills(null,null);
//			zebraSurfacebillService.testTrans();
			System.out.println("]]]]]]]]]]]]]]]]");
		}catch(Exception e){
			e.printStackTrace();	}
	}

}
