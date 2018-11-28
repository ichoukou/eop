package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.SendTaskDao;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.service.SendTaskService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SendTaskServiceTest {

	private static SendTaskService<SendTask> sendTaskService;
	private static int id;

	private static SendTaskDao<SendTask> sendTaskDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		sendTaskService = (SendTaskService) ctx.getBean("sendTaskServiceImpl");

		sendTaskDao = (SendTaskDao) ctx.getBean("sendTaskDaoImpl");
	}

	@Test
	public void testSendTask() {
		List<SendTask> sendTaskList = sendTaskService.getSendTaskList(10000);
		for(int i=0;i<sendTaskList.size();i++){
			try{
			SendTask ost =sendTaskList.get(i);
			SendTask st = new SendTask();
			st.setClientId("Taobao");
			st.setTaskFlag("1");
			st.setTaskFlagId(1);
			st.setRemark("联调测试");
			String[] a =ost.getRequestParams().split("&data_digest=");
			a[0]=a[0].replace("logistics_interface=", "");
			a[0]=encode(a[0],"UTF-8");
			a[1]=encode(a[1],"UTF-8");
			System.out.println("logistics_interface="+a[0]+"&data_digest="+a[1]);
			st.setRequestParams("logistics_interface="+a[0]+"&data_digest="+a[1]);
			sendTaskService.addSendTask(st);
			}catch(Exception e){
				
			}
		}
	}
	
	@Test
	public void testAddSendTask() {

		SendTask st = new SendTask();
		st.setClientId("clientID");
		st.setOrderId(45);
		st.setRequestURL("http://jingangtest.yto56.com.cn/ordws/VipOrderServlet");
		String logisticsInterface = "<RequestOrder><clientID>360buy</clientID><logisticProviderID>YTO</logisticProviderID><txLogisticID>LP2005082300225709000</txLogisticID><customerId>zhangsan123</customerId><tradeNo>2005082300225709</tradeNo><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender> <name>itemname</name><postCode>456300</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></sender><receiver><name>name</name><postCode>100000</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></receiver><sendStartTime>2011-05-12 16:00:00.0 CST</sendStartTime><sendEndTime>2011-05-12 17:30:00.0 CST</sendEndTime><itemsValue>2000</itemsValue><items><item><itemName>Nokia N73</itemName><number>2</number><remark>ASDFAS</remark></item><item><itemName>Nokia N72</itemName><number>1</number><remark>ASDFAS</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><special>5</special><remark>remarkstring</remark></RequestOrder>";
		String utf8LogisticsInterface = encode(logisticsInterface, "UTF-8");
		String dataDigest = logisticsInterface + "123456";
		String md5DataDigest = Md5Encryption.MD5Encode(dataDigest);
		String utf8DataDigest = encode(md5DataDigest, "UTF-8");
		st.setRequestParams("logistics_interface=" + utf8LogisticsInterface
				+ "&data_digest=" + utf8DataDigest);
		sendTaskService.addSendTask(st);

	}

	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testGetListSendTask() {
		List<SendTask> sendTaskList = sendTaskService.getSendTaskList(5);
		System.out.println(sendTaskList.size());
	}

	@Test
	public void testGetSendTask() {

	}

	@Test
	public void testUpdateSendTask() {

	}

	@Test
	public void testRemoveSendTask() {
		List<SendTask> list = sendTaskDao.getAll();
		for (SendTask st : list) {
			System.out.println(st);
			// sendTaskDao.removeSendTask(st);
			break;
		}
	}

	@Test
	public void testPagination() {
		Pagination pagination = new Pagination(1, 10);
		List<SendTask> list = sendTaskService.getPaginationSendTaskList(
				pagination, true);
		for (SendTask st : list) {
			System.out.println(st);
		}
		pagination.setTotalRecords(sendTaskService.getPaginationSendTaskList(
				pagination, false).size());
		System.out.println("总记录数：" + pagination.getTotalRecords());
	}

	@Test
	public void testGetSendTaskListByFlagsAndLimit() {
		List<Integer> flags = new ArrayList<Integer>();
		flags.add(1);
		flags.add(2);

		List<SendTask> tasks = sendTaskService.getSendTaskListByFlagsAndLimit(
				flags, 10);
		Assert.assertNotNull(tasks);
		if (tasks.isEmpty()) {
			System.out.println("haha");
		}

	}

}
