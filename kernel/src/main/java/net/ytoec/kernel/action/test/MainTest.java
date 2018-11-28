package net.ytoec.kernel.action.test;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

public class MainTest {

	private static Logger logger=Logger.getLogger(MainTest.class);
	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String string="234123dfdsfsdafasdfasdfasd123dfdfdsfsd";
		String string2=java.net.URLEncoder.encode(string, "GBK");
		logger.debug(string2);
		logger.debug(string);
		
	
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
}
