package net.ytoec.kernel.action.remote;

import java.io.UnsupportedEncodingException;

import org.springframework.context.annotation.Scope;

import net.ytoec.kernel.action.common.Md5Encryption;
@Deprecated
@Scope("prototype")
public class VipOrderServlet {
	private String logisticsInterface;
	private String dataDigest;

	public VipOrderServlet(String logisticsInterface, String dataDigest) {
		this.logisticsInterface = decode(logisticsInterface, "UTF-8");
		this.dataDigest = decode(dataDigest, "UTF-8");
	}

	public boolean validateData() {
		String string0 = logisticsInterface + "123456";
		String string1 = Md5Encryption.MD5Encode(string0);
		if (string1.equals(dataDigest)) {
			return true;
		}
		return false;
	}

	private static boolean validateAccount(String clientId, String parternId) {

		return false;
	}

	public <T> T process() throws Exception {

		return null;

	}

	/**
	 * 使用指定的字符集charset对字符串arg进行编码,将编码后的字符串返回.
	 * 
	 * @param arg
	 *            待编码字符串.
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

	/**
	 * 使用指定的字符集charset对字符串arg进行解码,将解码后的字符串返回.
	 * 
	 * @param arg
	 *            待解码字符串.
	 * @param charset
	 * @return
	 */
	private static String decode(String arg, String charset) {

		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

	}

	public String getLogisticsInterface() {
		return logisticsInterface;
	}

	public void setLogisticsInterface(String logisticsInterface) {
		this.logisticsInterface = logisticsInterface;
	}

	public String getDataDigest() {
		return dataDigest;
	}

	public void setDataDigest(String dataDigest) {
		this.dataDigest = dataDigest;
	}

}
