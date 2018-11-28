package net.ytoec.kernel.util;

import java.io.UnsupportedEncodingException;


public class WebUtil {
	/**
	 * 
	 * URL编码.
	 * @param arg
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

	public static void main(String[] args) {
		
	}
}
