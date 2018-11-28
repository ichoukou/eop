package net.ytoec.kernel.test;

import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraForewarnDao;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.service.SellerInfoService;
import net.ytoec.kernel.service.ZebraForewarnService;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import com.ibm.icu.text.SimpleDateFormat;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ZebraForewarnDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraForewarnDao<ZebraForewarn> dao;
	@Inject
	private ZebraSurfacebillService zebraSurfacebillService;
	@Inject
	private ZebraForewarnService zebraForewarnService;
	@Inject
	private SellerInfoService sellerInfoService;
	@Inject
	private SellerInfoService impl;
	@Inject
	private ZebraForewarnDao<ZebraForewarn> zebraForewarnDao;

	// 测试预警
	@Test
	public void testUpdateForwarnByCustomerCode() throws ParseException {
		// ZebraForewarn z = new ZebraForewarn();
		// z.setBranckWarnValue(342432);
		//
		// dao.updateForwarnByCustomerCode(z);
		//
		// z.setBranckWarnValue(432432);
		//
		// // dao.updateForwarnByCustomerCode(z);
		// System.out.println();
		// List<User> userList = sellerInfoService.findUserAll();
		// List<User> userList=impl.findUserAll();
		// System.out.println(userList.get(0));

		// int a=zebraSurfacebillService.selectNoUseCounts("1000");
		// int toteFaceNum = zebraSurfacebillService.selectAllCount("1");
		// ZebraForewarn forewarn=new ZebraForewarn();
		// zebraForewarnService.updateForwarnByCustomerCode(forewarn);
		// System.out.print(forewarn);

		// ZebraForewarn z=zebraForewarnService.selectByCustomerCode("1");
		// System.out.print(z);
		ZebraForewarn z = new ZebraForewarn();
		z.setCustomerCode("333");
		z.setBranckWarnValue(321312);
		z.setCustomerWarnValue(321312);
		z.setPhone("342432");
		z.setRemarkPhone("342432");
		z.setEmail("32312312");
		z.setPhoneWarn(true);
		z.setEmailWarn(true);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date d = df.parse(df.format(new Date()));
		z.setUpdateTime(d);
		zebraForewarnService.insertForwarnByCustomerCode(z);
		// zebraForewarnService.updateForwarnByCustomerCode(z);
	}

    @Test
	public void testUpdateSendPhoneStateByCustomerCode() {
		String customerCode = "K22000014";
		int sendPhoneState = 1;
		int rows=zebraForewarnDao.updateSendPhoneStateByCustomerCode(customerCode,
				sendPhoneState);
		System.out.print(rows);
	}
    
    @Test
	public void testUpdateSendMailStateByCustomerCode() {
		String customerCode = "K22000014";
		int sendMailState = 1;
		int rows=zebraForewarnDao.updateSendMailStateByCustomerCode(customerCode,
				sendMailState);
		System.out.print(rows);
	}
    
 }
