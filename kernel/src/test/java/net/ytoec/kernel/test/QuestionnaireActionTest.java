package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;

 



public class QuestionnaireActionTest {
	public static void main(String[] args) {
		QuestionnaireActionTest t = new QuestionnaireActionTest();
		t.testParseString();
	}

	public void testParseString() {

		XmlSender xmlSender = new XmlSender();
//		XmlSender xmlSender = new XmlSender(new DomDriver("utf-8"));

		 String logisticsInterface="<RequestIssueOrder>"
		        +"<mailNo>6052727027</mailNo>"
		        +"<type>地址不详电话联系不上</type>"
		        +"<code>210077</code>"
		        +"<time>2011-08-03 14:21:39</time>"
		        +"<sName>陈丰</sName>"
		        +"<sPhone>13888888888</sPhone>"
		        +"<bName>陈丰</bName>"
		        +"<bPhone>13888888888</bPhone>"
		        +"<prov>浙江</prov>"                   
		        +"<city>杭州</city>"                     
		        +"<district>西湖区</district>"           
		        +"<address>华星科技大厦9层</address>"
		        +"<status>派送扫描</status>"
		        +"</RequestIssueOrder>";
		
		String utf8LogisticsInterface = encode(logisticsInterface,"utf-8");
		String dataDigest = logisticsInterface + "123456";
		String md5DataDigest = Md5Encryption.MD5Encode(dataDigest);
		System.out.println("utf8LogisticsInterface:===" + utf8LogisticsInterface+"===liug==");
		System.out.println("md5DataDigest:===" + md5DataDigest+"===liug==");
		
		String utf8DataDigest = encode(md5DataDigest,xmlSender.UTF8_CHARSET);
		System.out.println("md5utf8DataDigest:===" + utf8DataDigest+"===liug==");

		String url = "http://localhost:8080/questionnaireProcess.action";
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
//		xmlSender.setRequestMethod(XmlSender.GET_REQUEST_METHOD);   
		xmlSender.setRequestParams("logistics_interface=" + utf8LogisticsInterface + "&data_digest=" + utf8DataDigest);
		System.out.println("utf8LogisticsInterface:"+utf8LogisticsInterface);
		System.out.println("data_digest:"+utf8DataDigest);
		String result = xmlSender.send();
		System.out.println(result);

	}

	/**
	 * 
	 * URL编码.
	 * @param arg
	 * @param charset
	 * @return
	 */
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * try { //System.out.println(new String("你好123456".getBytes("GBK")));
	 * md5DataDigest = Md5Encryption.MD5Encode(new
	 * String(dataDigest.getBytes("UTF-8"))); System.out.println(md5DataDigest);
	 * } catch (UnsupportedEncodingException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); }
	 */

}
