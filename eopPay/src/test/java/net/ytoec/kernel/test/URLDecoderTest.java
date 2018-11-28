package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
/**
 * 将UTF-8编码的字符串反编码
 * @author yto
 * 发送给金刚加密方式:jNpKcyXrHfNJ 2FnBhFRscUA2TXSZSf8PUg%3D%3D GElwSBndPh9NVO%2Flg2N%2FVw%3D%3D
 */
public class URLDecoderTest {

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String paramsFiled = "%3CUpdateInfo%3E%0A++%3CtxLogisticID%3EIMP997219873%3C%2FtxLogisticID%3E%0A++%3CmailNo%3E6311366516%3C%2FmailNo%3E%0A++%3ClogisticProviderID%3EYTO%3C%2FlogisticProviderID%3E%0A++%3CinfoType%3ESTATUS%3C%2FinfoType%3E%0A++%3CinfoContent%3ESIGNED%3C%2FinfoContent%3E%0A++%3Cname%3E%E5%91%A8%3C%2Fname%3E%0A++%3CacceptTime%3E2013-04-16+14%3A24%3A18.541+CST%3C%2FacceptTime%3E%0A%3C%2FUpdateInfo%3E";
		String xml = URLDecoder.decode(paramsFiled);
		String dslogisticsInterface = encode(xml, "UTF-8");
		System.err.println(xml);
		
		
		String parentId = "jNpKcyXrHfNJ"; 
		String dsdataDigest = encode(md5Encode(xml + parentId), "UTF-8");
		System.err.println("原来的密码     C2SEvpDP4Z0fQfqH0Ob4BQ%3D%3D");
		System.err.println("加密后的密码"+dsdataDigest);
		// md5加密参数
	}
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String md5Encode(String xmlParams) {
		String str = xmlParams;
		byte[] byteMd5 = md5(str, "UTF-8");
		byte[] byteBase64 = encodeToByteArray(byteMd5);
		return new String(byteBase64);

	}
	
	public static byte[] md5(String str) {
		byte[] bytesMd5 = DigestUtils.md5(str);
		return bytesMd5;
	}
	
	public static byte[] md5(String str, String charset) {
		byte[] bytes;
		try {
			bytes = str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		byte[] bytesMd5 = DigestUtils.md5(bytes);
		return bytesMd5;
	}
	
	public static byte[] encodeToByteArray(byte[] bytes) {
		Base64 base64 = new Base64();
		byte[] byteBase64 = base64.encode(bytes);
		return byteBase64;
	}
	
	public static String encodeToString(byte[] bytes) {
		Base64 base64 = new Base64();
		byte[] byteBase64 = base64.encode(bytes);
		String s = new String(byteBase64);
		return s;
	}
	
	public static byte[] decodeToByteArray(byte[] bytes) {
		Base64 base64 = new Base64();
		byte[] byteOrig = base64.decode(bytes);
		return byteOrig;
	}
	
	public static String decodeToString(byte[] bytes) {
		Base64 base64 = new Base64();
		byte[] byteOrig = base64.decode(bytes);
		String s = new String(byteOrig);
		return s;
	}
}
