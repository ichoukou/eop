package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Resource;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class TestDecode extends AbstractJUnit38SpringContextTests{

	/**
	 * @param args
	 */
	
	
	@Test
	public void testData(){
		String logisticsInterface = 
			decode("%3C%3Fxml+version%3D%271.0%27+encoding%3D%27UTF-8%27%3F%3E%3CMailNoRequest%3E%3C" +
					"customerCode%3EK10101010+%3C%2FcustomerCode%3E%3C%2FMailNoRequest%3E","UTF-8");
		String dataDigest="xnJYz4Adiw5tn4VFDT8ArA%3D";
		
//		String parternCode = Resource.getSecretId("K10101010+");
		TestDecode test = new TestDecode();
		test.validateData(logisticsInterface, "3HKTp9m6", dataDigest);
	}

	
	public void validateData(String logisticsInterface, String parternCode,String dataDigest) {
		
		String newDataDigest = encode(Md5Encryption.MD5Encode(logisticsInterface+ parternCode),"UTF-8");
		
		System.out.println("logisticsInterface:"+logisticsInterface);
		
		System.out.println("newDataDigest:" + newDataDigest);
		
		System.out.println("   dataDigest:" + dataDigest);
		
		if (dataDigest.equals(newDataDigest)) {
			System.out.println("-");
		}
		

	}
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
//			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}
	}

	private static String decode(String arg, String charset) {
		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
//			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
