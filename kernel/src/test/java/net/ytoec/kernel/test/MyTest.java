package net.ytoec.kernel.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;
import net.ytoec.kernel.util.PropertiesUtil;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.xml.sax.SAXException; 

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class MyTest extends AbstractJUnit38SpringContextTests {
	@Autowired
	private OrderService<Order> orderService;
	@Autowired
	private ConfigCodeService<ConfigCode> configCodeService;
	@Test
	public void testM(){
		
		Collection<?> c = new ArrayList<String>();

//		c.add(new Object()); // 编译时错误
//		String path="d:\\timer.properties";
//		Connection con=null;
//		PreparedStatement ps=null;
//		ResultSet rs=null;
//		try {
//			//在文件内记录读取时间点
//			con = JDBCUtilSingle.getInstance().getConnection();
//			String started = PropertiesUtil.readValue(path, "started");
//			String ended = PropertiesUtil.readValue(path, "ended");
//			if(started!=null&&!"".equals(started)){
//            System.out.println("-------------开始更新前一日未更新的重量信息----------------");
//            System.out.println("开始时间:["+started+"]");
//            System.out.println("结束时间:["+ended+"]");
////			String startTime="2011-08-23 20:00:00";
////			String endTime="2011-08-24 20:00:00";
//			// 更新前一日重量信息
//			ps = con.prepareStatement("select * from weight_test where tx_logistic_id=?");
//			List<Order> orderlist=orderService.getOrdersByWeight(started,ended);
//			System.out.println("******发现"+orderlist.size()+"数据需要做更新处理*******");
//            for(int i=0;i<orderlist.size();i++){
//            	Order order=(Order)orderlist.get(i);
//            	System.out.println("开始处理运单号为["+order.getTxLogisticId()+"]数据");
//            	ps.setString(1, order.getTxLogisticId());
//            	rs = ps.executeQuery();
//    			if (rs.next()) {
//    				// 更新数据
//    				System.out.println("获取到运单["+order.getTxLogisticId()+"]的重量信息["+rs.getFloat("weight")+"]");
//    				orderService.updateOrderWeightByLogisticId(order.getTxLogisticId(),rs.getFloat("weight"));
//    				//在文件内记录时间点
//    				PropertiesUtil.writeProperties(path, "started", DateUtil.format(rs.getTimestamp("create_time"),"yyyy-MM-dd HH:mm:ss"));
//    				System.out.println("已完成运单["+order.getTxLogisticId()+"]的重量信息更新");
//    			}
//            }
//            System.out.println("-------------新前一日未更新的重量信息处理完毕----------------");
//			}
//			// 更新当日重量信息
//            String start=PropertiesUtil.readValue(path, "start");
//            String end=DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
//            
//            System.out.println("-------------开始更新当日重量信息----------------");
//            System.out.println("开始时间:["+start+"]");
//            System.out.println("结束时间:["+end+"]");
//            
//			ps = con.prepareStatement("select tx_logistic_id,weight,create_time FROM weight_test o WHERE  o.create_time>=date_format('"
//									  +start+"','%Y-%m-%d %H:%i:%s') and  o.create_time<=date_format('"
//									  +end+"','%Y-%m-%d %H:%i:%s')  order by create_time");
//			rs = ps.executeQuery();
//			rs.last();
//			System.out.println("******发现"+rs.getRow()+"数据需要做更新处理*******");
//			rs.first();
//			while (rs.next()) {
//				
//				System.out.println("开始处理数据--运单号["+rs.getString("tx_logistic_id")+"] 创建时间为["+rs.getTimestamp("create_time")+"]");
//				// 更新数据
//				Order order=(Order)orderService.getOrderByLogisticId(rs.getString("tx_logistic_id"));
//				if (order != null) {// 根据运单号查找,存在则更新重量信息
//					System.out.println("获取到运单["+rs.getString("tx_logistic_id")+"]的重量信息 ["+rs.getFloat("weight")+"]");
//					orderService.updateOrderWeightByLogisticId(rs.getString("tx_logistic_id"), rs.getFloat("weight"));
//					//在文件内记录时间点
//					PropertiesUtil.writeProperties(path, "start", DateUtil.format(rs.getTimestamp("create_time"),"yyyy-MM-dd HH:mm:ss"));
//					System.out.println("已完成运单["+rs.getString("tx_logistic_id")+"]的重量信息更新");
//				}else{
//					System.out.println("没有获取到该记录的重量信息");
//				}
//			}
//			PropertiesUtil.writeProperties(path, "started", start);
//			PropertiesUtil.writeProperties(path, "ended", end);
//			System.out.println("-------------更新当日重量信息处理完毕----------------");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			JDBCUtilSingle.free(rs, ps, con);
//		}
		
		Date aa=DateUtil.valueof("2005-08-24 08:00:00.0 CST", "yyyy-MM-dd HH:mm:ss.S Z");
		
		System.out.println("-------------重量信息更新开始----------------");
    	String path=((net.ytoec.kernel.dataobject.ConfigCode)configCodeService.getConfByKey("weightTimerPath")).getConfValue();
    	System.out.println("配置文件路径:"+path);
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try {
			//在文件内记录读取时间点
			con = JDBCUtilSingle.getInstance().getConnection();
			String started= DateUtil.format(DateUtil.getDateBefore(new Date(), 2),"yyyy-MM-dd HH:mm:ss");
			String ended= DateUtil.format(DateUtil.getDateBefore(new Date(), 1),"yyyy-MM-dd HH:mm:ss");
//			String started = PropertiesUtil.readValue(path, "started");
//			String ended = PropertiesUtil.readValue(path, "ended");
			
			if(started!=null&&!"".equals(started)){
            System.out.println("-------------开始更新前一日未更新的重量信息----------------");
            System.out.println("开始时间:["+started+"]");
            System.out.println("结束时间:["+ended+"]");
			// 更新前一日重量信息
			ps = con.prepareStatement("select * from v_itf_op_taking where waybill_no=?");
			List<Order> orderlist=orderService.getOrdersByWeight(started,ended,0,0);
			System.out.println("******发现"+orderlist.size()+"数据需要做更新处理*******");
            for(int i=0;i<orderlist.size();i++){
            	Order order=(Order)orderlist.get(i);
            	System.out.println("开始处理运单号为["+order.getMailNo()+"]数据");
            	ps.setString(1, order.getMailNo());
            	rs = ps.executeQuery();
    			if (rs.next()) {
    				// 更新数据
    				System.out.println("获取到运单["+order.getMailNo()+"]的重量信息["+rs.getFloat("weigh_weight")+"]");
    				orderService.updateOrderWeightByMailNo(order.getMailNo(),rs.getFloat("weigh_weight"));
    				//在文件内记录时间点
    				PropertiesUtil.writeProperties(path, "started", DateUtil.format(rs.getTimestamp("modify_time"),"yyyy-MM-dd HH:mm:ss"));
    				System.out.println("已完成运单["+order.getMailNo()+"]的重量信息更新");
    			}
            }
            System.out.println("-------------新前一日未更新的重量信息处理完毕----------------");
			}
			// 更新当日重量信息
            String start=PropertiesUtil.readValue(path, "start");
            String end=DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
            
            System.out.println("-------------开始更新当日重量信息----------------");
            System.out.println("开始时间:["+start+"]");
            System.out.println("结束时间:["+end+"]");
            
			ps = con.prepareStatement("select * FROM v_itf_op_taking o WHERE  o.modify_time>=to_date('"
									  +start+"','yyyy-mm-dd hh24:mi:ss') and  o.modify_time<=to_date('"
									  +end+"','yyyy-mm-dd hh24:mi:ss')  order by modify_time",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			System.out.println("select * FROM v_itf_op_taking o WHERE  o.modify_time>=to_date('"
									  +start+"','yyyy-mm-dd hh24:mi:ss') and  o.modify_time<=to_date('"
									  +end+"','yyyy-mm-dd hh24:mi:ss')  order by modify_time");
			rs = ps.executeQuery();
//			rs.last();
//			System.out.println("******发现"+rs.getRow()+"数据需要做更新处理*******");
//			rs.first();
			while (rs.next()) {
				
				System.out.println("开始处理数据--运单号["+rs.getString("waybill_no")+"] 创建时间为["+rs.getTimestamp("modify_time")+"]");
				// 更新数据
				Order order=(Order)orderService.getOrderByMailNo(rs.getString("waybill_no"));
				if (order != null) {// 根据运单号查找,存在则更新重量信息
					System.out.println(rs.getString("waybill_no"));
					System.out.println("获取到运单["+rs.getString("waybill_no")+"]的重量信息 ["+rs.getFloat("weigh_weight")+"]");
					orderService.updateOrderWeightByMailNo(rs.getString("waybill_no"), rs.getFloat("weigh_weight"));
					//在文件内记录时间点
					PropertiesUtil.writeProperties(path, "start", DateUtil.format(rs.getTimestamp("modify_time"),"yyyy-MM-dd HH:mm:ss"));
					System.out.println("已完成运单["+rs.getString("waybill_no")+"]的重量信息更新");
				}else{
					System.out.println("没有获取到运单号为["+rs.getString("waybill_no")+"]的记录，不进行更新操作");
				}
			}
			PropertiesUtil.writeProperties(path, "started", start);
			PropertiesUtil.writeProperties(path, "ended", end);
			System.out.println("-------------更新当日重量信息处理完毕----------------");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}

	}
	
	 /**
     * 判断字符串的编码
     *
     * @param str
     * @return
     */  
   public static String getEncoding(String str) {   
        String encode = "GB2312";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s = encode;   
               return s;   
            }   
        } catch (Exception exception) {   
        }   
        encode = "ISO-8859-1";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s1 = encode;   
               return s1;   
            }   
        } catch (Exception exception1) {   
        }   
        encode = "UTF-8";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s2 = encode;   
               return s2;   
            }   
        } catch (Exception exception2) {   
        }   
        encode = "GBK";   
       try {   
           if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s3 = encode;   
               return s3;   
            }   
        } catch (Exception exception3) {   
        }   
       return "";   
    }   
	/**
	 * @param args
	 * @throws  
	 * @throws  
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		UserCustom uc=new UserCustom();
		Class.forName("net.ytoec.kernel.dataobject.UserCustom").getMethod("setField003", String.class).invoke(uc, "2");
		
//		Method[] methods=Class.forName("net.ytoec.kernel.dataobject.UserCustom").getMethods();
//		for(Method m:methods){
//			
//			System.out.println(m.getName());
//			if("setField003".equals(m.getName())){
//				m.invoke(uc, "1");
//			}
//		}
		System.out.println(uc.getField003());
//		System.out.println(StringUtil.isBlank("111"));
//		     
//              System.out.print(Md5Encryption.MD5EncodeGBK("<order>测试</order>123456"));
//              System.out.println(Math.ceil(5.9/0.5));
//		  // 建立 schema 工厂
//
//	       SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
//
//	       // 建立验证文档文件对象，利用此文件对象所封装的文件进行 schema 验证
//
//	       File schemaFile = new File("D:/yto/src/main/webapp/WEB-INF/classes/VipOrderQuerySchema.xsd");
//           System.out.println(schemaFile.exists());
//	       // 利用 schema 工厂，接收验证文档文件对象生成 Schema 对象
//
//	       Schema schema = schemaFactory.newSchema(schemaFile);
//
//	       // 通过 Schema 产生针对于此 Schema 的验证器，利用 students.xsd 进行验证
//
//	       Validator validator = schema.newValidator();
//
//	       // 得到验证的数据源，就是 students.xml
////	       String a="<RequestOrder><clientID>360buy</clientID> <logisticProviderID>YTO</logisticProviderID><txLogisticID>LP111098989212575666</txLogisticID><customerId>中文</customerId><tradeNo>2005082300225709</tradeNo><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender><name>itemname</name><postCode>456300</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></sender><receiver><name>name</name><postCode>100000</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></receiver><sendStartTime>2011-07-25 16:00:00.0 CST</sendStartTime><sendEndTime>2011-08-15 17:30:00.0 CST</sendEndTime><itemsValue>2000</itemsValue><itemsWeight>0</itemsWeight><items><item><itemName>Nokia N73</itemName><number>2</number><remark>ASDFAS</remark></item><item><itemName>Nokia N72</itemName><number>1</number><remark>ASDFAS</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><special>5</special><remark>d</remark></RequestOrder>";
//	       String a="<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>360buy</clientID><orders><order><mailNo>0571115987</mailNo></order></orders></BatchQueryRequest>";
//	       
//	       ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(a.getBytes());
//	       Source source = new StreamSource(tInputStringStream);
//
//	       // 开始验证，成功输出 success!!! ，失败输出 fail
//
//	       try {
//
//	           validator.validate(source);
//
//	           System.out.println("success!!!");
//
//	       } catch (Exception ex) {
//
//	           System.out.println("fail");
//
//	       } 
		
//		try {
//			   Class.forName("oracle.jdbc.driver.OracleDriver");
//			   Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.1.195.151:1521:exprd1", "YTITF", "YTITF");
//			  } catch (Exception e) {
//			   e.printStackTrace();
//	    }
//		String psw="<RequestOrder><logisticProviderID>YTO</logisticProviderID><txLogisticID>LP08013100000635</txLogisticID><customerId>tbtest430</customerId><tradeNo>2232356449</tradeNo><type>4</type><flag>0</flag><mailNo>358792146022</mailNo><sender><name>wanghaiping</name><postCode>310013</postCode><phone>13858176086</phone><prov>浙江省</prov><city>杭州市</city><address>西湖区99号</address></sender><receiver><name>youyou</name><postCode>474174</postCode><phone>13757136086</phone><prov>河南省</prov><city>南阳市</city><address>邓州市白落乡</address></receiver><sendStartTime>2010-12-22 17:14:00.0 CST</sendStartTime><sendEndTime>2010-12-22 17:14:00.0 CST</sendEndTime><goodsValue>13.0</goodsValue><itemsValue>13.0</itemsValue><items><item><itemName>flower</itemName><number>1</number><itemValue>12.0</itemValue></item></items><insuranceValue>0.0</insuranceValue><special>0</special><ecCompanyId>TAOBAO</ecCompanyId><orderType>1</orderType><serviceType>1</serviceType></RequestOrder>123456";
//		String psw="<test>test</test>123456";
//		String string1 = Md5Encryption.MD5Encode(psw);
//		System.out.println(string1);
	     URL url=new URL("http://110.75.120.136/consign/Logistics_test_receive_message.do");
	     String updateString="<UpdateInfo><txLogisticID>F110722722369</txLogisticID><logisticProviderID>YTO</logisticProviderID><infoType>STATUS</infoType><infoContent>ACCEPT</infoContent></UpdateInfo>";
 		String digestString="foRqP%2BLobcW3lgbJLwbWcw%3D%3D";
		
		StringBuffer sbBuffer=new StringBuffer();
		sbBuffer.append("logistics_interface=");
		sbBuffer.append("%3CUpdateInfo%3E%3CtxLogisticID%3EF110722722369%3C%2FtxLogisticID%3E%3ClogisticProviderID%3EYTO%3C%2FlogisticProviderID%3E%3CinfoType%3ESTATUS%3C%2FinfoType%3E%3CinfoContent%3EACCEPT%3C%2FinfoContent%3E%3C%2FUpdateInfo%3E");
		sbBuffer.append("&data_digest=");
		sbBuffer.append("foRqP%2BLobcW3lgbJLwbWcw%3D%3D");
	     
		String content=	sbBuffer.toString();
		
	     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	       
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        
	        DataOutputStream out = new DataOutputStream(conn
	                .getOutputStream());
	        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
	       
	        out.writeBytes(content); 
	        out.flush();
	        out.close(); // flush and close 
	        
	        
	       // logger.debug("try to get input stream");
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conn
	                .getInputStream()));
	        String line;
	        StringBuffer buffer = new StringBuffer(1024);
	        
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
	        
	        String value = buffer.toString();
	        //logger.debug("get resposne: '" + value + "'");
	        
	        value = URLDecoder.decode(value, "UTF-8");
	        System.out.println(value);
	}
	public static void test() throws SAXException{
		
	}
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
