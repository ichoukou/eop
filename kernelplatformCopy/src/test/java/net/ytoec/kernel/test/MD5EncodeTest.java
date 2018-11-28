//package net.ytoec.kernel.test;
//
//import java.io.UnsupportedEncodingException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import org.apache.commons.lang.time.DateUtils;
//
//import net.ytoec.kernel.action.common.XmlSender;
//import net.ytoec.kernel.action.remote.xml.UpdateInfo;
//import net.ytoec.kernel.common.Base64Utils;
//import net.ytoec.kernel.common.Md5Utils;
//
//import com.thoughtworks.xstream.XStream;
//
///**
// * 加密测试
// * @author yto
// *
// */
//public class MD5EncodeTest {
//
//	public static void main(String[] args) throws ParseException {
//		
//		UpdateInfo updateInfo = new UpdateInfo();
//		updateInfo.setTxLogisticID("LP00011574858868");
//		updateInfo.setMailNo("1962862695");
//		updateInfo.setLogisticProviderID("YTO");
//		updateInfo.setInfoType("STATUS");
//		updateInfo.setInfoContent("GOT");
//		updateInfo.setAcceptTime("2012-11-01 20:25:45.0 CST");
//		String xml = convertInfoToXML(updateInfo);
//		
//		//System.err.println(xml);
//			String jingangMd5 = md5Encode(xml+"jNpKcyXrHfNJ");
//			String md5Base64 = encode(jingangMd5,XmlSender.UTF8_CHARSET);
//			//System.out.println("加密后的值为:"+md5Base64);
//			//System.out.println("原 来 的 值 为 :H%2FXaxXnQFbXd%2Fvfal2J1Ig%3D%3D");
//			//System.err.println(encode(xml,XmlSender.UTF8_CHARSET));
//			
//			Date requestTime = new Date(Calendar.getInstance().getTimeInMillis());
//			//System.err.println(net.ytoec.kernel.common.DateUtils.toString(requestTime));
//			//System.err.println(net.ytoec.kernel.common.DateUtils.toDate("2013-03-28 12:00:23"));
//			
//			Date now = Calendar.getInstance().getTime();
//			Date startTime = DateUtils.addHours(now, -120);// 只查询5天前发生的数据
//			System.err.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
//	}
//
//	private static String encode(String arg, String string) {
//		try {
//			return java.net.URLEncoder.encode(arg, string);
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	private static String md5Encode(String params) {
//		String str = params;
//		byte[] byteMd5 = Md5Utils.md5(str, "GBK");
//		byte[] byteBase64 = Base64Utils.encodeToByteArray(byteMd5);
//		return new String(byteBase64);
//	}
//
//	private static String convertInfoToXML(UpdateInfo info) {
//		XStream x = new XStream();
//		x.alias("UpdateInfo", UpdateInfo.class);
//		String xml = x.toXML(info);
//		return xml;
//	}
//}
