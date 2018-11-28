package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.SendTaskToTBService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SendTaskToTBServiceTest {

	private static SendTaskToTBService<SendTaskToTB> sendTaskToTBService;
	private static int id;

	private static SendTaskToTBDao<SendTaskToTB> sendTaskToTBDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		sendTaskToTBService = (SendTaskToTBService) ctx.getBean("sendTaskToTBServiceImpl");

		sendTaskToTBDao = (SendTaskToTBDao) ctx.getBean("sendTaskToTBDaoImpl");
	}

	@Test
	public void testAddSendTaskToTB() {

		SendTaskToTB st = new SendTaskToTB();
		st.setClientId("clientID");
		st.setOrderId(45);
		st
				.setRequestURL("http://jingangtest.yto56.com.cn/ordws/VipOrderServlet");
		String logisticsInterface = "<RequestOrder><clientID>360buy</clientID><logisticProviderID>YTO</logisticProviderID><txLogisticID>LP2005082300225709000</txLogisticID><customerId>zhangsan123</customerId><tradeNo>2005082300225709</tradeNo><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender> <name>itemname</name><postCode>456300</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></sender><receiver><name>name</name><postCode>100000</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></receiver><sendStartTime>2011-05-12 16:00:00.0 CST</sendStartTime><sendEndTime>2011-05-12 17:30:00.0 CST</sendEndTime><itemsValue>2000</itemsValue><items><item><itemName>Nokia N73</itemName><number>2</number><remark>ASDFAS</remark></item><item><itemName>Nokia N72</itemName><number>1</number><remark>ASDFAS</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><special>5</special><remark>remarkstring</remark></RequestOrder>";
		String utf8LogisticsInterface = encode(logisticsInterface, "UTF-8");
		String dataDigest = logisticsInterface + "123456";
		String md5DataDigest = Md5Encryption.MD5Encode(dataDigest);
		String utf8DataDigest = encode(md5DataDigest, "UTF-8");
		st.setRequestParams("logistics_interface=" + utf8LogisticsInterface
				+ "&data_digest=" + utf8DataDigest);
		st.setRemark("kkkkkkkkkkkk");
		sendTaskToTBService.addSendTaskToTB(st);

	}

	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetListSendTaskToTB() {
		List<SendTaskToTB> sendTaskToTBList = sendTaskToTBService.getSendTaskToTBList(5);
		System.out.println(sendTaskToTBList.size());
	}

	@Test
	public void testGetSendTaskToTB() {

	}

	@Test
	public void testUpdateSendTaskToTB() {

	}

	@Test
	public void testRemoveSendTaskToTB() {
		List<SendTaskToTB> list = sendTaskToTBDao.getAll();
		for (SendTaskToTB st : list) {
			System.out.println(st);
			// sendTaskToTBDao.removeSendTaskToTB(st);
			break;
		}
	}
	
	@Test
	public void testPagination(){
		Pagination pagination = new Pagination(1,10);
		List<SendTaskToTB> list = sendTaskToTBService.getPaginationSendTaskToTBList(pagination, true);
		for (SendTaskToTB st : list) {
			System.out.println(st);
		}
		pagination.setTotalRecords(sendTaskToTBService.getPaginationSendTaskToTBList(pagination, false).size());
		System.out.println("总记录数："+pagination.getTotalRecords());
	}
	


}
