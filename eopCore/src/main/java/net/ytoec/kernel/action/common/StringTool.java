/** 
 * 文件名：StringTool.java
 * 创 建 人： 王勇
 * 版 本 号：v1.0
 */
package net.ytoec.kernel.action.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 验证码字符串符号集合工具类
 * @author Wangyong
 * @2011-8-4
 * net.ytoec.kernel.action.common
 */
public class StringTool {

	/**
	 * 生成随即字符集和
	 * @param randomLength 设置的字符集和长度
	 * @return
	 */
	public static String randomChars(int randomLength){
		List<Character> charList = createCharCollection();
		Random random = new Random();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < randomLength; i++) {
			ret.append(charList.get(random.nextInt(charList.size())));
		}
		random = null;
		return ret.toString();
	}
	
	/**
	 * 创建字符总集和
	 * @return
	 */
	private static List<Character> createCharCollection(){
		List<Character> charList = new ArrayList<Character>();
		for(char c='2'; c<='9'; c++){
			charList.add(c);
		}
		for(char c='A'; c<='Z'; c++){
			if(c != 'O' && c != 'I' && c != 'D')
				charList.add(c);
		}
		for(char c='a'; c<='z'; c++){
			if(c != 'o' && c != 'l')
				charList.add(c);
		}
		return charList;
	}
	
}
