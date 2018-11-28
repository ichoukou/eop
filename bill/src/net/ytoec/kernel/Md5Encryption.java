package net.ytoec.kernel;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * MD5加密工具类
 * 
 * @author huangtianfu
 * 
 */
public class Md5Encryption {

	/**
	 * 将指定的字符串采用MD5算法加密
	 * 
	 * @param origin
	 * @return 加密后的字符串
	 */
	public static String MD5Encode(String psw) {
		return MD5Encode(psw, "UTF8");
		/*
		 * MessageDigest messagedigest = null; try { messagedigest =
		 * MessageDigest.getInstance("MD5");
		 * messagedigest.update(psw.getBytes("UTF8")); byte[] abyte0 =
		 * messagedigest.digest();
		 * 
		 * String password = Base64.encodeBase64String(abyte0); // return
		 * password; return password; } catch (Exception e) { throw new
		 * RuntimeException("数据加密出现异常!", e); }
		 */
	}

	/**
	 * 将指定的字符串采用MD5算法加密
	 * 
	 * @param origin
	 * @return 加密后的字符串
	 */
	public static String MD5EncodeGBK(String psw) {
		return MD5Encode(psw, "GBK");
		/*
		 * MessageDigest messagedigest = null; try { messagedigest =
		 * MessageDigest.getInstance("MD5");
		 * messagedigest.update(psw.getBytes("UTF8")); byte[] abyte0 =
		 * messagedigest.digest();
		 * 
		 * String password = Base64.encodeBase64String(abyte0); // return
		 * password; return password; } catch (Exception e) { throw new
		 * RuntimeException("数据加密出现异常!", e); }
		 */
	}

	/**
	 * 将指定的字符串采用MD5算法加密,指定的字符串使用指定的字符集编码charset.
	 * 
	 * @param psw
	 *            指定的字符串.
	 * @param charset
	 *            字符集.
	 * @return 返回加密后的字符串.
	 */
	public static String MD5Encode(String psw, String charset) {
		MessageDigest messagedigest = null;
		try {
			messagedigest = MessageDigest.getInstance("MD5"); // 创建消息摘要
			messagedigest.update(psw.getBytes(charset)); // 用明文字符串计算消息摘要。
			byte[] abyte0 = messagedigest.digest(); // 读取消息摘要。

			String password = new String(Base64.encodeBase64(abyte0));
			return password;
		} catch (Exception e) {
			throw new RuntimeException("数据加密出现异常!", e);
		}
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	// 测试类
	public static void main(String[] args) {
		System.out.println(Md5Encryption.MD5Encode("YTO315017"));
	}

}