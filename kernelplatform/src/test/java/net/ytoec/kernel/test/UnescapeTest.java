package net.ytoec.kernel.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import net.ytoec.kernel.dataobject.OrderPrint;
import net.ytoec.kernel.service.OrderPrintService;

public class UnescapeTest{
	
	private static OrderPrintService<OrderPrint> orderPrintServiceImpl;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:applicationContext-*.xml");
		orderPrintServiceImpl = (OrderPrintService) ctx.getBean("orderPrintServiceImpl");
	}
	
	@Test
	public  void testUnescape() {
		String str = "%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Eship_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E487%3A114%3A73%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Eship_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E607%3A171%3A172%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u7535%u8BDD%3C/name%3E%3Cucode%3Eship_tel%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E665%3A114%3A105%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Eship_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E693%3A229%3A88%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Eship_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E501%3A172%3A80%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Edly_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E127%3A98%3A81%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Edly_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E229%3A159%3A162%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Edly_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E134%3A161%3A68%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Edly_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E307%3A228%3A93%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u624B%u673A%3C/name%3E%3Cucode%3Edly_mobile%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E142%3A223%3A108%3A20%3C/position%3E%3C/item%3E%3C/printer%3E";
		String s = orderPrintServiceImpl.unescape(str);
		
		s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + s;
		System.out.println(s);
		JSONObject jsonObject =  (JSONObject) new XMLSerializer().read(s);
        System.out.println(jsonObject.toString());
		
	}
	
    //@Test
    public void testConvertXMLText(){
        String xmlText = "<?xml version='1.0' encoding='gb2312'?><printer picposition='0:0'><item><name>收货人-姓名</name><ucode>ship_name</ucode><font>null</font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>487:114:73:20</position></item><item><name>收货人-地址</name><ucode>ship_address</ucode><font>null</font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>607:171:172:20</position></item><item><name>收货人-电话</name><ucode>ship_tel</ucode><font>null</font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>665:114:105:20</position></item></printer>";
        JSONObject jsonObject =  (JSONObject) new XMLSerializer().read(xmlText);
        System.out.println(jsonObject.toString());
    }
}
