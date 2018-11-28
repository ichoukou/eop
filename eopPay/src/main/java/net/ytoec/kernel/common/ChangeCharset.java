package net.ytoec.kernel.common;

import java.io.UnsupportedEncodingException;

/**
 * 转换字符串的编码
 */
public class ChangeCharset {

	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	 public static final String US_ASCII = "US-ASCII";

	 /** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
	 public static final String ISO_8859_1 = "ISO-8859-1";

	 /** 8 位 UCS 转换格式 */
	 public static final String UTF_8 = "UTF-8";

	 /** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
	 public static final String UTF_16BE = "UTF-16BE";

	 /** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
	 public static final String UTF_16LE = "UTF-16LE";

	 /** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	 public static final String UTF_16 = "UTF-16";

	 /** 中文超大字符集 */
	 public static final String GBK = "GBK";

	 /**
	  * 将字符编码转换成US-ASCII码
	  */
	 public String toASCII(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, US_ASCII);
	 }
	 /**
	  * 将字符编码转换成ISO-8859-1码
	  */
	 public String toISO_8859_1(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, ISO_8859_1);
	 }
	 /**
	  * 将字符编码转换成UTF-8码
	  */
	 public String toUTF_8(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, UTF_8);
	 }
	 /**
	  * 将字符编码转换成UTF-16BE码
	  */
	 public String toUTF_16BE(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, UTF_16BE);
	 }
	 /**
	  * 将字符编码转换成UTF-16LE码
	  */
	 public String toUTF_16LE(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, UTF_16LE);
	 }
	 /**
	  * 将字符编码转换成UTF-16码
	  */
	 public String toUTF_16(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, UTF_16);
	 }
	 /**
	  * 将字符编码转换成GBK码
	  */
	 public String toGBK(String str) throws UnsupportedEncodingException{
	  return this.changeCharset(str, GBK);
	 }
	 
	 /**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 public String changeCharset(String str, String newCharset)
	   throws UnsupportedEncodingException {
	  if (str != null) {
	   //用默认字符编码解码字符串。
	   byte[] bs = str.getBytes();
	   //用新的字符编码生成字符串
	   return new String(bs, newCharset);
	  }
	  return null;
	 }
	 /**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param oldCharset 原编码
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 public String changeCharset(String str, String oldCharset, String newCharset)
	   throws UnsupportedEncodingException {
	  if (str != null) {
	   //用旧的字符编码解码字符串。解码可能会出现异常。
	   byte[] bs = str.getBytes(oldCharset);
	   //用新的字符编码生成字符串
	   return new String(bs, newCharset);
	  }
	  return null;
	 }

	 public static void main(String[] args) throws UnsupportedEncodingException {
	      String xmlString = "<clientID>11111</clientID><BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><orders><order><mailNo>1111111111</mailNo></order></orders></BatchQueryRequest>";


	     // xmlString.replace("<clientID>"+*+"</clientID>", "<clientID>CMBCHINA</clientID>");
	     // <clientID>CMBCHINA</clientID>
	      int m=xmlString.indexOf("<clientID>");
	      int n=xmlString.indexOf("</clientID>");
	    String aa=  xmlString.substring(0,m+10)+"CMBCHINA"+xmlString.substring(n+11);
	    System.out.println("=="+aa);
	 }
	 
	 
	 public byte[] gbk2utf8(String chenese) {
	        
	        // Step 1: 得到GBK编码下的字符数组，一个中文字符对应这里的一个c[i]
	        char c[] = chenese.toCharArray();
	        
	        // Step 2: UTF-8使用3个字节存放一个中文字符，所以长度必须为字符的3倍
	        byte[] fullByte = new byte[3 * c.length];
	        
	        // Step 3: 循环将字符的GBK编码转换成UTF-8编码
	        for (int i = 0; i < c.length; i++) {
	            
	            // Step 3-1：将字符的ASCII编码转换成2进制值
	            int m = (int) c[i];
	            String word = Integer.toBinaryString(m);
	            System.out.println(word);

	            // Step 3-2：将2进制值补足16位(2个字节的长度) 
	            StringBuffer sb = new StringBuffer();
	            int len = 16 - word.length();
	            for (int j = 0; j < len; j++) {
	                sb.append("0");
	            }
	            // Step 3-3：得到该字符最终的2进制GBK编码
	            // 形似：1000 0010 0111 1010
	            sb.append(word);
	            
	            // Step 3-4：最关键的步骤，根据UTF-8的汉字编码规则，首字节
	            // 以1110开头，次字节以10开头，第3字节以10开头。在原始的2进制
	            // 字符串中插入标志位。最终的长度从16--->16+3+2+2=24。
	            sb.insert(0, "1110");
	            sb.insert(8, "10");
	            sb.insert(16, "10");
	            System.out.println(sb.toString());

	            // Step 3-5：将新的字符串进行分段截取，截为3个字节
	            String s1 = sb.substring(0, 8);
	            String s2 = sb.substring(8, 16);
	            String s3 = sb.substring(16);

	            // Step 3-6：最后的步骤，把代表3个字节的字符串按2进制的方式
	            // 进行转换，变成2进制的整数，再转换成16进制值
	            byte b0 = Integer.valueOf(s1, 2).byteValue();
	            byte b1 = Integer.valueOf(s2, 2).byteValue();
	            byte b2 = Integer.valueOf(s3, 2).byteValue();
	            
	            // Step 3-7：把转换后的3个字节按顺序存放到字节数组的对应位置
	            byte[] bf = new byte[3];
	            bf[0] = b0;
	            bf[1] = b1;
	            bf[2] = b2;
	            
	            fullByte[i * 3] = bf[0];            
	            fullByte[i * 3 + 1] = bf[1];            
	            fullByte[i * 3 + 2] = bf[2];
	            
	            // Step 3-8：返回继续解析下一个中文字符
	        }
	        return fullByte;
	    }
}
