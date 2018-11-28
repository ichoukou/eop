package net.ytoec.kernel.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.dao.AttentionMailDao;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.search.dataobject.EccoreItem;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.MQService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext*.xml")
public class InsertTest  extends AbstractJUnit38SpringContextTests{
	 @Inject
	 private EccoreSearchService eccoreSearchService;
	 
	 @Inject
	 private AttentionMailDao<AttentionMail> attentiondao;
	 
	@Autowired
	private MQService mqService;
	 
	 static SimpleDateFormat holdTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
	 
	 
	 @Test
	 public void testMq(){
		 try
		 {
			 mqService.receive4Solr("SOLR");
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 @Test
	 public void testMq12(){
		 try
		 {
			 mqService.receive("SOLR");
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 
	 @Test 
	 public void testSolr(){
		 try
		 {
			 String  solrUrl = "http://10.1.205.30:7850/isolr/eccore";
			 SolrServer solrServer = new CommonsHttpSolrServer(solrUrl);
			 //--查询条件
			 SolrQuery query = new SolrQuery();
			 String startDate = "2013-09-23";
			 String endDate = "2013-10-23";
			 StringBuffer queryFilter = new StringBuffer();
			 long numStartDay = getNumDayStr(startDate);
			 long numEndDay = getNumDayStr(endDate);
			 queryFilter.append(" (");
			 for (long i = numStartDay; i <= numEndDay; i++) {
				if (i != numStartDay) {
					queryFilter.append(" OR ");
				}
				queryFilter.append("numCreateTime:").append(i);
			 }
			 queryFilter.append(") ");
			 //System.out.println(queryFilter);
			 queryFilter.append(" AND ");
			 queryFilter.append(" customerId:b1055c6758a8a8dd80bec6ca831fbbfb ");
			 query.setQuery(queryFilter.toString());
			 /*List<String> fqList = new ArrayList<String>();
			 fqList.add("customerId:b1055c6758a8a8dd80bec6ca831fbbfb");
			 query.setFilterQueries(fqList.toArray(new String[fqList.size()]));*/
			 //--结果
			 QueryResponse rsp = solrServer.query(query);
			 System.out.println("结果条数："+rsp.getResults().size()+" 耗时："+rsp.getQTime()+" ms");
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 
	 @Test 
	 public void testSolrUpdate(){
		 try
		 {
			 SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(
				"yyyy-MM-dd");
			 SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
			 String  solrUrl = "http://10.1.205.30:7850/isolr/eccore";
			 List<BuildSearchStatusWeightIndex> buildSearchs = new ArrayList<BuildSearchStatusWeightIndex>();
			 BuildSearchStatusWeightIndex beanSearch = new BuildSearchStatusWeightIndex();
			 beanSearch.setOrderId(1406505887);
			 beanSearch.setStatus("SIGNED");
			 beanSearch.setMailNo("73473205321111");
			 beanSearch.setPartitiondate(simpleDateFormat1.parse("2013-10-07"));
			 beanSearch.setCreateTime(simpleDateFormat2.parse("2013-10-08 11:16:31"));
			 beanSearch.setAcceptTime(simpleDateFormat2.parse("2013-10-12 15:58:19"));
			 //beanSearch.setCustomerId("9a65921b32a3e0205dddb48cfd273e5f");
			 beanSearch.setCustomerId("9a65921b32a3e0205dddb4811111111");
			 beanSearch.setName("徐琦");
			 beanSearch.setMobile("13771507335");
			 beanSearch.setPhone("13771507335");
			 beanSearch.setNumProv(320000);
			 beanSearch.setCity("无锡市");
			 beanSearch.setProv("江苏省");
			 beanSearch.setDistrict("锡山区");
			 beanSearch.setAddress("张泾镇泾声路41号");
			 beanSearch.setWeight(0.1f);
			 beanSearch.setOrderType(0);
			 beanSearch.setLineType("1");
			 beanSearch.setTrimFreight(0.0);
			 beanSearch.setFreightType("1");
			 beanSearch.setCityF("衢州市");
			 beanSearch.setProvF("浙江省");
			 beanSearch.setHoldTime("2013-10-08 11:34:10");
			 beanSearch.setBuildTask("4");
			 beanSearch.setTxLogisticId("LP00017766395923");
			 beanSearch.setOldMailNo("");
			 String buildFlag = "1";
			 //--
			 buildSearchs.add(beanSearch);
			 //--
			 /*EccoreItem bean = new EccoreItem();
			 bean2EccoreItem(beanSearch,bean);
			 SolrServer solrServer = new CommonsHttpSolrServer(solrUrl);
			 solrServer.addBean(bean);
			 solrServer.commit(false,false);*/
			 eccoreSearchService.buildUpdatePartEccoreData(solrUrl,buildSearchs,buildFlag);
			 System.out.println("---------------------更新Solr-------------------");
			 
			 /*System.out.println("----------------------------------------");
			 System.out.println(eccoreSearchService.getClass().toString());
			 System.out.println("----------------------------------------");*/
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	
	
	@Test
	public void testAddOrder(){
		String url="jdbc:mysql://116.228.70.232:3306/eccoredb_test?user=yitong&password=ZTI3MTA5ZD";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			
			con = DriverManager.getConnection(url);
			
			stmt = con.createStatement();
			String query = " select max(id) id1 from ec_core_order ";
			rs=stmt.executeQuery(query);
			System.out.println("新增前MAX(ID)：");
			while(rs.next())
			{
				System.out.println("Max(ID)："+rs.getInt(1));
			}
			
			/*String insert = "insert into ec_core_order(logistic_provider_id,tx_logistic_id,tradeNo,customer_id,mailNo,type,flag,create_time,update_time,send_start_time,send_end_time,"+
	    "insurance_value,package_or_not,special,remark,client_id,status,weight,sign_price,vip_id,line_type,weight_update_flag,order_type,service_type,goods_value,items_value,total_service_fee,"+
        "buy_service_fee,cod_split_fee,accept_time,partitiondate,freight_type,freight,trim_freight)"+
        "values('YTO','FP00005386160001','519086823144470','abcd8975be1aa8d7339c2d0007a5860','9824790761','2012-02-05','00:00:03',CURDATE(),CURDATE(),CURDATE(),CURDATE(),"+
	    "'123.1','0','0','11111','Taobao','UPDATE','0.32','0','','0','1','1','1','34.8','34.8',null,null,null,CURDATE(),'0000-00-00','1','0',null)";
			//--
			stmt.executeUpdate(insert);
			//----------------------------
			query = " select max(id) id1 from ec_core_order ";
			stmt = con.createStatement();
			rs=stmt.executeQuery(query);
			System.out.println("新增后MAX(ID)：");
			while(rs.next())
			{
				System.out.println("Max(ID)："+rs.getInt(1));
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(Exception e){
				
			}
		}
		
	}
	
	
	@Test
	public void testAddInt(){
		String url="jdbc:mysql://127.0.0.1:3306/test?user=root&password=test";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try
		{
			
			con = DriverManager.getConnection(url);
			
			stmt = con.createStatement();
			String query = " select id id1 from learn ";
			rs=stmt.executeQuery(query);
			System.out.println("新增前MAX(ID)：");
			while(rs.next())
			{
				//Mysql bigint 对应 Java Long类型
				//Mysql int 类型，用 Java Long类型也能读，所以如果保证数值是整数，可定义为long型
				System.out.println("Max(ID)："+rs.getLong(1));
				//System.out.println("Max(ID)："+rs.getInt(1));
			}
			
			String insert = "insert into learn(cname,cnumber) values('FF',15) ";
			//--
			stmt.executeUpdate(insert);
			//----------------------------
			query = " select id id1 from learn ";
			stmt = con.createStatement();
			rs=stmt.executeQuery(query);
			System.out.println("新增后MAX(ID)：");
			while(rs.next())
			{
				System.out.println("Max(ID)："+rs.getInt(1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				stmt.close();
				con.close();
			}catch(Exception e){
				
			}
		}
		
	}
	private static boolean bean2EccoreItem(BuildSearchStatusWeightIndex buildSearch,EccoreItem bean) throws Exception{
		String status = "0";
		bean.setId(buildSearch.getOrderId());
		if (buildSearch.getStatus() != null)
			status = buildSearch.getStatus();
		if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
				.toString())) {
			status = StatusEnum.CREATE.getName();
		}
		if (StatusEnum.isExitEnum(status) == false)
			return false;
		bean.setNumStatus(StatusEnum.valueOf(status).getValue());
		if (StringUtils.isEmpty(buildSearch.getMailNo())) {
			// 运单号为空 solr不允许搜索此数据
			bean.setMailNo("YTO" + buildSearch.getId());
			bean.setIsDispaly(new Integer(1).shortValue());
		} else {
			// 支持
			bean.setMailNo(buildSearch.getMailNo());
			bean.setIsDispaly(new Integer(0).shortValue());
		}
		bean.setNumCreateTime((int) getNumDay(buildSearch
				.getPartitiondate()));
		bean.setCreateTime(buildSearch.getCreateTime());
		bean.setAcceptTime(buildSearch.getAcceptTime());
		bean.setCustomerId(buildSearch.getCustomerId());
		bean.setName(buildSearch.getName());

		bean.setPhone(new String[] { buildSearch.getMobile(),
				buildSearch.getPhone() });

		bean.setDisplayPhone(buildSearch.getMobile());
		if (StringUtils.isEmpty(bean.getDisplayPhone())) {
			bean.setDisplayPhone(buildSearch.getPhone());
		}
		bean.setNumProv(Integer.valueOf(buildSearch.getNumProv()));

		bean.setNumCity(Resource.getCodeByName(buildSearch.getCity()));

		bean.setNumDistrict(Resource.getCodeByName(buildSearch
				.getDistrict()));
		bean.setAddress(buildSearch.getAddress());
		bean.setWeight(buildSearch.getWeight());
		if (buildSearch.getOrderType() == 0) {
			bean.setOrderType(new Integer(0).shortValue());
		} else {
			bean.setOrderType(new Integer(buildSearch.getOrderType())
					.shortValue());
		}
		if (buildSearch.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
			bean.setLineType(new Integer(1).shortValue());
		} else {
			bean.setLineType(new Integer(0).shortValue());
		}
		bean.setTrimFreight((float) buildSearch.getTrimFreight());
		if (buildSearch.getFreightType() != null) {
			bean.setFreightType(new Integer(1).shortValue());
		} else {
			bean.setFreightType(new Integer(0).shortValue());
		}
		bean.setCityF(buildSearch.getCityF());
		bean.setNumProvF(Resource.getCodeByName(buildSearch.getProvF()));
		try
		{
			if (!StringUtils.isEmpty(buildSearch.getHoldTime())) {
				String holdTime = buildSearch.getHoldTime().trim();
				if (holdTime.length() == 19
						|| holdTime.length() == 10) {
					holdTime = holdTime.substring(0, 10);
					Date dateTemp = holdTimeFormat.parse(holdTime);
					long dayTemp = getNumDay(dateTemp);
					bean.setHoldTime((int)dayTemp);
				}else{
					bean.setHoldTime((int) getNumDay(holdTimeFormat
							.parse(holdTimeFormat.format(new Date()))));
				}
			}
		}catch(Exception e){
			bean.setHoldTime(new Integer(9999));
		}
		bean.setMailNoAndCustomerId(bean.getMailNo()
				+ buildSearch.getCustomerId());

		bean.setTxLogisticId(buildSearch.getTxLogisticId());
		return true;
	}
	
	private static long getNumDayStr(String ymd) throws ParseException {
		if (StringUtils.isEmpty(ymd)) {

			return 0;
		}
		Date date = DateUtils.parseDate(ymd, new String[] { "yyyy-MM-dd" });
		return getNumDay(date);
	}

	private static long getNumDay(Date ymd) throws ParseException {
		if (ymd == null) {
			return 0;
		}
		Date originalDate = DateUtils.parseDate("2011-11-19",
				new String[] { "yyyy-MM-dd" });
		long result = (ymd.getTime() - originalDate.getTime())
				/ (1000 * 60 * 60 * 24);
		if (result < 0)
			result = 0;
		return result;
	}
}
