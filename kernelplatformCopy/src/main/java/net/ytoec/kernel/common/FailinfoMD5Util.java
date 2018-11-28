package net.ytoec.kernel.common;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class FailinfoMD5Util {
	/*
	 * 依据md5算法，生成一个摘要
	 * 特点
	 *   a  唯一型、性：有唯一的摘要与之对应
	 *   b  不可逆性，不能反推原始明文
	 */
	public static String encrypt(String origStr)throws NoSuchAlgorithmException{
		MessageDigest md=MessageDigest.getInstance("md5");
		byte[] buff=md.digest(origStr.getBytes());
		//将字节数组转换成一个字符串
		BASE64Encoder encoder=new BASE64Encoder();
		String str=encoder.encode(buff);
		return str;
	}
	public static void main(String[] args) {
		try {
			System.out.println(FailinfoMD5Util.encrypt("12345"));
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
	}
}
