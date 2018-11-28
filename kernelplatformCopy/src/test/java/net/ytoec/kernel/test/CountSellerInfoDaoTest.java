package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.process.RequestOrderProcessor;
import net.ytoec.kernel.dao.AttentionMailDao;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dao.CountSellerInfoDao;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.CountSellerInfo;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class CountSellerInfoDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private CountSellerInfoDao<CountSellerInfo> dao;
	@Inject
	private BuildSearchDao buildSearchDao;
	
	@Inject
	private OrderDao orderDao;
	
	@Inject
	private BuildSearchStatusWeightIndexDao BuildSearchStatusWeightIndexDao;
	
	@Inject
	private RequestOrderProcessor requestOrder;
	
	@Inject
    private AttentionMailDao<AttentionMail> Attentiondao;
	@Test
	public void testSelectByPhone() {
		String phone = "1";
		CountSellerInfo countSellerInfo = dao.selectByPhone(phone);
		System.out.println(countSellerInfo);
	}

//	@Test
//	public void testAddCountSellerInfo() {
//		Date date = new Date();
//		CountSellerInfo countSellerInfo = new CountSellerInfo();
//		countSellerInfo.setBuyNum(1);
//		countSellerInfo.setCreateTime(date);
//		countSellerInfo.setUpdateTime(date);
//		countSellerInfo.setPhone("123333333");
//		dao.insertCountSellerInfo(countSellerInfo);
////	}
//
//	@Test
//	public void testUpdateCountSellerInfo() {
//		int sid = 1;
//		CountSellerInfo countSellerInfo = new CountSellerInfo();
//		countSellerInfo.setSid(sid);
//		countSellerInfo.setBuyNum(2);
//		countSellerInfo.setUpdateTime(new Date());
//		System.out.println(countSellerInfo);
//	}
//	@Test
//	public void test() {
//		BuildSearchStatusWeightIndex buildSearch = new BuildSearchStatusWeightIndex();
//		buildSearch.setBuildTask("1");
//		buildSearch.setAcceptTime(new Date());
//		buildSearch.setOrderId(1);
//		buildSearch.setStatus("1");
//		buildSearch.setProv("安徽");
//		buildSearch.setProvF("安徽");
//		buildSearch.setCustomerId("11111111");
//		buildSearch.setOrderType(1);
//		buildSearch.setName("zhangsan");
//		buildSearch.setPhone("3231");
//		buildSearch.setMobile("111");
//		buildSearch.setNumProvF(111);
//		buildSearch.setNumProv(111);
//		buildSearch.setCity("111");
//		buildSearch.setCityF("1111");
//		buildSearch.setTxLogisticId("1111");
//		buildSearch.setAddress("232");
//		buildSearch.setTrimFreight(2);
//		buildSearch.setFreightType("1");
//		buildSearch.setLineType("1");
//		buildSearch.setType("1");
//		buildSearch.setWeight((float)0);
//		buildSearch.setOldMailNo("231");
//		buildSearch.setHoldTime("11");
//		buildSearch.setPartitiondate(new Date());
//		buildSearch.setDistrict("12");
//		buildSearch.setMailNo("23312");//26
//		
//		
//		BuildSearchStatusWeightIndexDao.addBuildSearchByUpdateOrderForWeightIndex(buildSearch);
//		MailObjectDTO
//		traderinfo=orderDao.bulidPartEccoreDataByOrderId(5240,"2013-07-29");
//		try{
//			traderinfo.getAddress();
//		}catch(Exception e){
//			e.getMessage();
//		}
	
	
	
	@Test
	public void testCount(){
		Map map = new HashMap();
		map.put("sendStartTime", "450");
        map.put("sendEndTime", "700");
        map.put("mailNo", "1234567890");
        List customerId=new ArrayList();
        customerId.add("12121");
        map.put("customerId", customerId);
		Attentiondao.searchByMailNoAndCustomerIdsAndTime(map);
	}
	
}
