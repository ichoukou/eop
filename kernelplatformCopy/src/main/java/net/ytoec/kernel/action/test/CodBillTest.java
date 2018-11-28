package net.ytoec.kernel.action.test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.bill.BillDetailDto;
import net.ytoec.kernel.service.CodBillService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.opensymphony.xwork2.inject.Inject;


public class CodBillTest {

	@Inject
	private static CodBillService<BillDetailDto> codBillService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:applicationContext-*.xml");
		codBillService = (CodBillService) ctx.getBean("codBillServiceImpl");

	}
	
	@Test
	public void listCODDetailTest(){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("userCode", "K21003787");
//		params.put("waybillNoStr", "");
//		params.put("customerConfirmFlg", "");
//		params.put("pageFlag", "");
//		params.put("detailStartTime", new GregorianCalendar(2013, 03, 01).getTime());
//		params.put("detailEndTime", new Date());
//		params.put("signoffFlg", "");
//		params.put("startIndex", "1");
//		params.put("pageNum", "50");
		params.put("timeType", "4");
//		Integer count  = codBillService.getTotalCodBill(params);
		List<BillDetailDto> cods = codBillService.getCodBillDtails(params);
		System.out.println(cods.size());
//		System.out.println("find the cod bill count is : " + count);
	}
	
}
