package net.ytoec.kernel.common;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
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

