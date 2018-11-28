package net.ytoec.kernel.action.remote.process;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dao.impl.OrderDaoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Processor接口帮助类.
 * 
 */
public class ProcessorUtils {
	
	private static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
	private ProcessorUtils() {

	}
	
    public static boolean PROCESS_SWITCH=true;
    
	/**
	 * 根据一个xml片段,得到此xml文档的一个InputStream对象.
	 * 
	 * @param xmlFragment
	 * @return
	 */
	public static InputStream getInputStream(String xmlFragment) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
		sb.append(xmlFragment);
		// liug 20110803
		byte[] bytes = null;
		try {
			bytes = sb.toString().getBytes("UTF-8");
			// bytes = sb.toString().getBytes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}

	/**
	 * MD5加密 验证参数值是否相同.
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return
	 */
	public static boolean validateData(String logisticsInterface,
			String dataDigest, String clientId) {
		// String s = arg0 + parternID
		String string0 = logisticsInterface + clientId;
		String string1 = Md5Encryption.MD5Encode(string0);
		if (dataDigest.equals(string1)) {
			return true;
		}
		return false;

	}

	/**
	 * 使用指定的字符集charset对字符串arg进行解码,将解码后的字符串返回.
	 * 
	 * @param arg
	 *            待解码字符串.
	 * @param charset
	 * @return
	 */
	public static String decode(String arg, String charset) {

		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 使用指定的字符集charset对字符串arg进行编码,将编码后的字符串返回.
	 * 
	 * @param arg
	 *            待编码字符串.
	 * @param charset
	 * @return
	 */
	public static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean validateXML(String content, String path) {
		try {
			logger.debug("========start xsd validate=======");
			logger.debug("========path======="+path);
			logger.debug("======content======"+content);
			// 建立 schema 工厂
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			// 建立验证文档文件对象，利用此文件对象所封装的文件进行 schema 验证
			File schemaFile = new File(path);// "D:/VipOrderCreateSchema.xsd"
			// 利用 schema 工厂，接收验证文档文件对象生成 Schema 对象
			Schema schema = schemaFactory.newSchema(schemaFile);
			// 通过 Schema 产生针对于此 Schema 的验证器，利用 students.xsd 进行验证
			Validator validator = schema.newValidator();
			// 得到验证的数据源，就是 students.xml
//			content = "<RequestOrder><clientID>360buy</clientID> <logisticProviderID>YTO</logisticProviderID><txLogisticID>LP111098989212575666</txLogisticID><customerId>中文</customerId><tradeNo>2005082300225709</tradeNo><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender><name>itemname</name><postCode>456300</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></sender><receiver><name>name</name><postCode>100000</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></receiver><sendStartTime>2011-07-25 16:00:00.0 CST</sendStartTime><sendEndTime>2011-08-15 17:30:00.0 CST</sendEndTime><itemsValue>2000</itemsValue><itemsWeight>0</itemsWeight><items><item><itemName>Nokia N73</itemName><number>2</number><remark>ASDFAS</remark></item><item><itemName>Nokia N72</itemName><number>1</number><remark>ASDFAS</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><special>5</special><remark>d</remark></RequestOrder>";
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
					content.getBytes("UTF-8"));
			Source source = new StreamSource(tInputStringStream);
			// 开始验证，成功输出 success,失败输出 fail
			try {
				validator.validate(source);
				logger.info("xsd success");
				return true;
			} catch (Exception ex) {
				logger.error("xsd fail", ex);
				return false;
			}

		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}
	
	public static int getflagid(String arg) {
		String s = arg;
		s = s.substring(s.length() - 1);
		logger.debug(s);
		return Integer.parseInt(s);

	}

	public static String getflag(String arg) {
		String sid = arg;
		sid = sid.substring(sid.length() - 1);
		String s = sid;
		logger.debug(s);
		return s;

	}
	
	
	
}
