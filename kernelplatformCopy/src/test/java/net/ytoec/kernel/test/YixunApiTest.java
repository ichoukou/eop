package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.StatusEnum;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import edu.emory.mathcs.backport.java.util.Arrays;

@ContextConfiguration("classpath*:applicationContext-common.xml,classpath*:/applicationContext-eopcore.xml,classpath*:/applicationContext-mq.xml")
public class YixunApiTest extends AbstractJUnit38SpringContextTests{

	@Test
	public void testSendStatus(){
		String url = "http://116.228.187.243:8061/ApiService.aspx";
		XmlSender xmlSender = new XmlSender();	
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
		
		String apiKey="886525da1113280e";	//需填入
		String secret = "cd9a38129424361e";	//需填入
		String realLogis = "20127801";//需填入
		
//		String params_got = createGot(apiKey,secret,realLogis);
//		String params_sentscan = createSentScan(apiKey,secret,realLogis);
		String params_signed = createSigned(apiKey,secret,realLogis);
//		String params_failed = createFailed(apiKey,secret,realLogis);
		
//		System.out.println(params_got);
//		System.out.println(params_sentscan);
		System.out.println(params_signed);
//		System.out.println(params_failed);
		
//		xmlSender.setRequestParams(params_got);
//		xmlSender.setRequestParams(params_sentscan);
		xmlSender.setRequestParams(params_signed);
//		xmlSender.setRequestParams(params_failed);
		
		String result = xmlSender.send();
		System.out.println(result);
	}
	
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	//GOT STATUS
	private String createGot(String apiKey, String secret, String realLogis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = sdf.format(new Date());
		StringBuffer sb1 = new StringBuffer();
		sb1.append("apikey="+apiKey);
		sb1.append("&format=xml");
		sb1.append("&method=reportabnormal");
		sb1.append("&packageno="+realLogis);
		String part1 = sb1.toString();
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append("&type=已取货&desc=已取货");
		sb2.append("&createtime="+datestr);
		String part2 = sb2.toString();
		
		StringBuffer sb3 = new StringBuffer();
		sb3.append("&type=");
		sb3.append(encode("已取货",XmlSender.UTF8_CHARSET));
		sb3.append("&desc=");
		sb3.append(encode("已取货",XmlSender.UTF8_CHARSET));
		sb3.append("&createtime="+datestr);
		String part3 = sb3.toString();
		
		String part4 = "&logicdata=";
		
		StringBuffer sb5 = new StringBuffer();
		sb5.append("<UpdateInfo>");
		sb5.append("<LogisticProviderID>");
		sb5.append(apiKey);
		sb5.append("</LogisticProviderID>");
		sb5.append("<OrderStates>");
		sb5.append("<OrderState>");
		sb5.append("<TxLogisitcId>");
		sb5.append(realLogis);
		sb5.append("</TxLogisitcId>");
		sb5.append("<State>");
		sb5.append("Buy01");
		sb5.append("</State>");
		sb5.append("<StateTime>");
		sb5.append(datestr);
		sb5.append("</StateTime>");
		sb5.append("<OperName>圆通快递</OperName>");
		sb5.append("<OperateTime>"+datestr+"</OperateTime>");
		sb5.append("<OperatorUnit>圆通快递</OperatorUnit>");
		sb5.append("<DeliveryMan></DeliveryMan>");
		sb5.append("<DeliveryMobile></DeliveryMobile>");
		sb5.append("<Reason></Reason>");
		sb5.append("<Remark></Remark>");
		sb5.append("</OrderState>");
		sb5.append("</OrderStates>");
		sb5.append("</UpdateInfo>");
		String part5 = sb5.toString();
		String part6 = encode(sb5.toString(),XmlSender.UTF8_CHARSET);

		//参数按首字母排序
		String sortParams = createSortParams(part1+part2+part4+part5);
		
		//获得参数签名字符串
		String signStr = createSign(sortParams,secret);
		
		//第二次参数排序
		String sortParams2 = createSortParams(part1+part3+part4+part6);
		
		String retstr = sortParams2+"&sign="+signStr;
		return retstr;
	}

	// SENT_SCAN STATUS
	private String createSentScan(String apiKey, String secret, String realLogis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = sdf.format(new Date());
		StringBuffer sb1 = new StringBuffer();
		sb1.append("apikey="+apiKey);
		sb1.append("&format=xml");
		sb1.append("&method=reportabnormal");
		sb1.append("&packageno="+realLogis);
		String part1 = sb1.toString();
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append("&type=配送中&desc=配送中");
		sb2.append("&createtime="+datestr);
		String part2 = sb2.toString();
		
		StringBuffer sb3 = new StringBuffer();
		sb3.append("&type=");
		sb3.append(encode("配送中",XmlSender.UTF8_CHARSET));
		sb3.append("&desc=");
		sb3.append(encode("配送中",XmlSender.UTF8_CHARSET));
		sb3.append("&createtime="+datestr);
		String part3 = sb3.toString();
		
		String part4 = "&logicdata=";
		
		StringBuffer sb5 = new StringBuffer();
		sb5.append("<UpdateInfo>");
		sb5.append("<LogisticProviderID>");
		sb5.append(apiKey);
		sb5.append("</LogisticProviderID>");
		sb5.append("<OrderStates>");
		sb5.append("<OrderState>");
		sb5.append("<TxLogisitcId>");
		sb5.append(realLogis);
		sb5.append("</TxLogisitcId>");
		sb5.append("<State>");
		sb5.append("Buy01");
		sb5.append("</State>");
		sb5.append("<StateTime>");
		sb5.append(datestr);
		sb5.append("</StateTime>");
		sb5.append("<OperName>圆通快递</OperName>");
		sb5.append("<OperateTime>"+datestr+"</OperateTime>");
		sb5.append("<OperatorUnit>圆通快递</OperatorUnit>");
		sb5.append("<DeliveryMan></DeliveryMan>");
		sb5.append("<DeliveryMobile></DeliveryMobile>");
		sb5.append("<Reason></Reason>");
		sb5.append("<Remark></Remark>");
		sb5.append("</OrderState>");
		sb5.append("</OrderStates>");
		sb5.append("</UpdateInfo>");
		String part5 = sb5.toString();
		String part6 = encode(sb5.toString(),XmlSender.UTF8_CHARSET);

		//参数按首字母排序
		String sortParams = createSortParams(part1+part2+part4+part5);
		
		//获得参数签名字符串
		String signStr = createSign(sortParams,secret);
		
		//第二次参数排序
		String sortParams2 = createSortParams(part1+part3+part4+part6);
		
		String retstr = sortParams2+"&sign="+signStr;
		return retstr;
	}
	
	// SIGNED STATUS
	private String createSigned(String apiKey, String secret, String realLogis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = sdf.format(new Date());
		StringBuffer sb1 = new StringBuffer();
		sb1.append("apikey="+apiKey);
		sb1.append("&format=xml");
		sb1.append("&method=reportsignstatus");
		sb1.append("&packageno="+realLogis);
		sb1.append("&status=1");
		sb1.append("&paysysno=1&amt=0&note=");
		sb1.append("&signtime="+datestr);
		String part1 = sb1.toString();
		
		String part2 = "&logicdata=";

		StringBuffer sb3 = new StringBuffer();
		sb3.append("<UpdateInfo>");
		sb3.append("<LogisticProviderID>");
		sb3.append(apiKey);
		sb3.append("</LogisticProviderID>");
		sb3.append("<OrderStates>");
		sb3.append("<OrderState>");
		sb3.append("<TxLogisitcId>");
		sb3.append(realLogis);
		sb3.append("</TxLogisitcId>");
		sb3.append("<State>");
		sb3.append("Buy03");
		sb3.append("</State>");
		sb3.append("<StateTime>");
		sb3.append(datestr);
		sb3.append("</StateTime>");
		sb3.append("<OperName>圆通快递</OperName>");
		sb3.append("<OperateTime>"+datestr+"</OperateTime>");
		sb3.append("<OperatorUnit>圆通快递</OperatorUnit>");
		sb3.append("<DeliveryMan></DeliveryMan>");
		sb3.append("<DeliveryMobile></DeliveryMobile>");
		sb3.append("<Reason></Reason>");
		sb3.append("<Remark></Remark>");
		sb3.append("</OrderState>");
		sb3.append("</OrderStates>");
		sb3.append("</UpdateInfo>");
		String part3 = sb3.toString();
		String part4 = encode(sb3.toString(),XmlSender.UTF8_CHARSET);
		
		//参数按首字母排序
		String sortParams = createSortParams(part1+part2+part3);
		
		//获得参数签名字符串
		String signStr = createSign(sortParams,secret);
		
		String sortParams2 = createSortParams(part1+part2+part4);
		
		String retstr = sortParams2+"&sign="+signStr;
		return retstr;
	}
	
	//FAILED STATUS
	private String createFailed(String apiKey, String secret, String realLogis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = sdf.format(new Date());
		StringBuffer sb = new StringBuffer();
		sb.append("apikey="+apiKey);
		sb.append("&method=reportsignstatus");
		sb.append("&packageno="+realLogis);
		sb.append("&status=2");
		sb.append("&paysysno=1&amt=0&note=");
		sb.append("&signtime="+datestr);
		sb.append("&logicdata=");
		sb.append("<UpdateInfo>");
		sb.append("<LogisticProviderID>");
		sb.append(apiKey);
		sb.append("</LogisticProviderID>");
		sb.append("<OrderStates>");
		sb.append("<OrderState>");
		sb.append("<TxLogisitcId>");
		sb.append(realLogis);
		sb.append("</TxLogisitcId>");
		sb.append("<State>");
		sb.append("Buy05");
		sb.append("</State>");
		sb.append("<StateTime>");
		sb.append(datestr);
		sb.append("</StateTime>");
		sb.append("<OperName>圆通快递</OperName>");
		sb.append("<OperateTime>"+datestr+"</OperateTime>");
		sb.append("<OperatorUnit>圆通快递</OperatorUnit>");
		sb.append("<DeliveryMan></DeliveryMan>");
		sb.append("<DeliveryMobile></DeliveryMobile>");
		sb.append("<Reason></Reason>");
		sb.append("<Remark></Remark>");
		sb.append("</OrderState>");
		sb.append("</OrderStates>");
		sb.append("</UpdateInfo>");
		sb.append("&format=xml");
		
		//参数按首字母排序
		String sortParams = createSortParams(sb.toString());
		
		//获得参数签名字符串
		String signStr = createSign(sortParams,secret);
		
		String retstr = sortParams+"&sign="+signStr;
		return retstr;
	}

	 private String createSign(String params, String secret) {
	        StringBuffer sb = new StringBuffer();
	        String [] paramArr = params.split("&");
	        for(int i=0;i<paramArr.length;i++){
	            String [] arr = paramArr[i].split("=");
	            sb.append(arr[0]);
	            sb.append(arr[1]);
	        }
	        sb.append(secret);
	        String retStr = Md5Encryption.MD5(sb.toString()).toLowerCase();
	        return retStr;
	}
	
	private String createSortParams(String params) {
		String [] paramArr = params.split("&");
		Arrays.sort(paramArr);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<paramArr.length;i++){
			String [] arr = paramArr[i].split("=");
			if(arr.length == 2){
				if(i == (paramArr.length-1)){
					sb.append(paramArr[i]);
				}else{
					sb.append(paramArr[i]+"&");
				}	
			}	
		}
		return sb.toString();
	}

	public static void main(String [] args){
		if("FAILED".equals(StatusEnum.FAILED.getName()))
			System.out.println("hello");
	}
	
}
