package net.ytoec.kernel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 字符串处理集合类 主要功能：判断是否为null或者空,字符串是否在数组中,截取，字符在字符串中出现的次数等方法
 * 
 * @author huangtianfu
 * 
 */
public class StringUtil {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(StringUtil.class);
	public static int PASSWORD_LEVEL_LOW = 1;

	public static int PASSWORD_LEVEL_MIDDLE = 2;

	public static int PASSWORD_LEVEL_HIGH = 3;

	static String htmlMatch = "";
	/**
	 * 定义全局空字符串
	 * 
	 */
	public final static String EC_CORE_EMPTY = "";
	/**
	 * 定义全局NULL
	 * 
	 */
	public final static String EC_CORE_NULL = null;
	/**
	 * 订单打印状态已打印
	 * 
	 */
	public final static String EC_CORE_ORDERINFO_Y = "Y";
	/**
	 * 订单打印状态未打印
	 * 
	 */
	public final static String EC_CORE_ORDERINFO_N = "N";
	/**
	 * 运单号码使用
	 * 
	 */
	public final static String EC_CORE_WAYBILLNOCOL_YES = "YES";
	/**
	 * 运单号码停用
	 */
	public final static String EC_CORE_WAYBILLNOCOL_NO = "NO";

	/**
	 * 返回目标字符串的字节长度
	 * 
	 * @param str
	 * @return
	 */
	public static int getLength(String str) {
		try {
			return str.getBytes("Unicode").length;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 按字节长度截取字符串，可以是中英文混排
	 * 
	 * @param s
	 *            要截取的目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String multiSubStr(String s, int len) {
		if (s == null) {
			return "";
		}
		try {
			if (s.getBytes("Unicode").length <= len) {
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return multiSubstring2(s, len) + "...";
	}

	/**
	 * 按字节长度截取字符串，可以是中英文混排，同时过滤font标签
	 * 
	 * @param s
	 *            要截取的目标字符串
	 * @param len
	 *            截取长度
	 * @return
	 */
	public static String filterSubStr(String s, int len) {
		if (s == null) {
			return "";
		}
		if (s.length() <= len) {
			return s;
		} else {
			if (s.contains("<font color='red'>") && s.contains("</font>")) {
				int i = ("<font color='red'></font>").length();
				String result = "";
				if (s.length() > (i + len)) {
					result = s.substring(0, (i + len));
					return result + "...";
				} else {
					result = s;
					return result;
				}
			}
		}
		return s.substring(0, len) + "...";
	}

	/**
	 * 在纠错显示文本的时候，去除高亮显示时所添加的font标签
	 * 
	 * @param s
	 * @return
	 */
	public static String removeFont(String s) {
		if (s == null) {
			return "";
		}
		String result = "";
		if (s.contains("<font color='red'>") && s.contains("</font>")) {
			String str = s.replace("<font color='red'>", "");
			result = str.replace("</font>", "");
		} else {
			return s;
		}
		return result;
	}

	/**
	 * 按字节长度截取字符串,内容包含html标签
	 */
	public static String multiSubStrOfHtml(String s, int len) {
		if (s == null) {
			return "";
		}
		try {
			if (s.getBytes("Unicode").length <= len) {
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return subStringHTML(s, len, "...");
	}

	/**
	 * 截断含有html标签的source
	 */
	public static String subStringHTML(String param, int length, String endWith) {

		if (length < 1) {
			System.out.println("length must >0");
			return "";
		}

		if (param.length() < length) {
			return param;
		}

		StringBuffer result = new StringBuffer();
		StringBuffer str = new StringBuffer();
		int n = 0;

		char temp;

		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == "<".toCharArray()[0]) {
				isCode = true;
			} else if (temp == "&".toCharArray()[0]) {
				isHTML = true;
			} else if (temp == ">".toCharArray()[0]) {
				n = n - 1;
				isCode = false;
			} else if (temp == ";".toCharArray()[0] && isHTML) {
				isHTML = false;
			}
			if (!isCode && !isHTML) {
				n = n + 1;
				// UNICODE码字符占两个字节
				if ((temp + "").getBytes().length > 1) {
					n = n + 1;
				}
				str.append(temp);
			}
			result.append(temp);
			if (n >= length) {
				break;
			}
		}

		result.append(endWith);
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
				"$1$2");

		// 去掉不需要结束标记的HTML标记

		temp_result = temp_result
				.replaceAll(
						"<(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/>",
						"");

		// 去掉成对的HTML标记
		// temp_result=temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>","$2");
		htmlMatch = temp_result;
		temp_result = removeMatchHtmlTag();

		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = new ArrayList<String>();

		while (m.find()) {
			endHTML.add(m.group(1));
		}

		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * 去除source当中的html标签
	 * 
	 * @return
	 */
	public static String removeMatchHtmlTag() {
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
		Matcher m = p.matcher(htmlMatch);
		if (m.find()) {
			htmlMatch = htmlMatch.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
					"$2");
			removeMatchHtmlTag();
		}
		return htmlMatch;
	}

	/**
	 * 按字节长度截取字符串
	 * 
	 * @param s
	 *            要截取的目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String multiSubstring2(String s, int length) {
		String s1 = s;
		try {
			byte[] bytes = s.getBytes("Unicode");
			int n = 0; // 表示当前的字节数
			int i = 2; // 要截取的字节数，从第3个字节开始

			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}
			}

			// 如果i为奇数时，处理成偶数
			if (i % 2 == 1) {
				// 该UCS2字符是汉字时，去掉这个截一半的汉字
				if (bytes[i - 1] != 0) {
					i = i - 1;
				}
				// 该UCS2字符是字母或数字，则保留该字符
				else {
					i = i + 1;
				}
			}

			s1 = new String(bytes, 0, i, "Unicode");
		} catch (UnsupportedEncodingException u) {
			u.printStackTrace();
		}
		return s1;
	}

	/**
	 * 截取指定字节长度的字符串
	 * 
	 * @param s
	 *            原来的字符
	 * @param m
	 *            字节数
	 * @return
	 */
	public static String substring(String s, int m) {
		if (isBlank(s)) {
			return "";
		}
		long l = 0;
		String r = s;
		for (int i = 0; i < s.length() && i < m; i++) {
			int c = s.charAt(i);
			if (c > 127) {
				l = l + 2;
			} else {
				l = l + 1;
			}
			if (l >= m) {
				r = s.substring(0, i + 1);
				break;
			}
		}

		return r;
	}

	/**
	 * 截取英文字符串方法，超出部分用...表示，单词不截断
	 * 
	 * @param s
	 *            字符串
	 * @param len
	 *            长度
	 * @return
	 */
	public static String substring4e(String s, int len) {
		if (s == null) {
			return "";
		}
		if (s.getBytes().length <= len) {
			return s;
		}
		while (!" ".equals(s.substring(len, len + 1)) && len > 0) {
			len--;
		}
		return substring(s, len) + "...";
	}

	public static String substringSimple(String s, int len) {
		if (s.length() > len) {
			s = s.substring(0, len);
		}
		return s;
	}

	@SuppressWarnings("unused")
	private static String CutString(String text, int textMaxChar)
			throws UnsupportedEncodingException {
		int size, index;
		String returnString = null;
		if (textMaxChar <= 0) {
			returnString = text;
		} else {
			for (size = 0, index = 0; index < text.length()
					&& size < textMaxChar; index++) {
				if (text.substring(index, index + 1).getBytes("GBK").length == 3) {
					size += 2;
				} else {
					size += text.substring(index, index + 1).getBytes("GBK").length;
				}
			}
			if (size >= textMaxChar) {
				return returnString = text.substring(0, index) + "";
			}
			returnString = text.substring(0, index);
		}
		return returnString;
	}

	/**
	 * 去除字符串空格
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	public static String trim(String s) {
		if (s == null) {
			return "";
		}

		s = s.replaceAll("　", " ");
		s = s.replaceAll("\\s+", " ");

		return s.trim();
	}

	/**
	 * 以\s+格式截取字符串返回list
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List split(String s) {
		List list = new ArrayList();

		s = trim(s);
		if (s == null) {
			return list;
		}
		String[] rs = s.split("\\s+");

		for (int i = 0; i < rs.length; i++) {
			if (rs[i].trim().length() > 0) {
				list.add(rs[i]);
			}
		}

		return list;
	}

	/**
	 * 截取字符串返回string数组
	 * 
	 * @param s
	 *            字符串
	 * @param delim
	 *            截取方式
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String[] split2Array(String s, String delim) {
		List list = split(s, delim);
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * 截取字符串
	 * 
	 * @param s
	 *            字符串
	 * @param delim
	 *            截取方式
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List split(String s, String delim) {
		List list = new ArrayList();

		s = trim(s);
		if (s == null) {
			return list;
		}
		String[] rs = s.split(delim);

		for (int i = 0; i < rs.length; i++) {
			if (rs[i].trim().length() > 0) {
				list.add(rs[i]);
			}
		}

		return list;
	}

	/**
	 * 字符在字符串中出现的次数
	 * 
	 * @param string
	 * @param a
	 * @return
	 */
	public static int occurTimes(String string, String a) {
		int pos = -2;
		int n = 0;

		while (pos != -1) {
			if (pos == -2) {
				pos = -1;
			}
			pos = string.indexOf(a, pos + 1);
			if (pos != -1) {
				n++;
			}
		}
		return n;
	}

	/**
	 * 返回字符串如果为null 则返回""
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	public static String nullValue(String s) {
		return s == null ? "" : s.trim();
	}

	/**
	 * 返回字符串 如果为null 返回defaultValue
	 * 
	 * @param s
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static String nullValue(String s, String defaultValue) {
		return s == null ? defaultValue : s;
	}

	/**
	 * 重载方法
	 * 
	 * @param s
	 *            对象
	 * @return
	 */
	public static String nullValue(Object s) {
		return s == null ? "" : s.toString();
	}

	public static String LongValue(Long s) {
		return s == null || s.intValue() <= 0 ? "" : s.toString();
	}

	public static String LongValueZero(Long s) {
		return s == null || s.intValue() <= 0 ? "0" : s.toString();
	}

	public static String LongValueOne(Long s) {
		return s == null || s.intValue() <= 0 ? "1" : s.toString();
	}

	/**
	 * 重载方法
	 * 
	 * @param s
	 * @return
	 */
	public static String nullValue(long s) {
		return s < 0 ? "" : String.valueOf(s);
	}

	/**
	 * 重载方法
	 * 
	 * @param s
	 * @return
	 */
	public static String nullValue(int s) {
		return s < 0 ? "" : "" + s;
	}

	/**
	 * 
	 * @param s
	 * @param multiples
	 * @return
	 */
	/*
	 * public static String getMultiplesValue(int s, double multiples) { return
	 * s < 0 ? "" : String.valueOf(NumberUtil.round(s * multiples, 1)); }
	 */

	/**
	 * 是否selected
	 * 
	 * @param arg
	 *            值
	 * @param selectedValue
	 *            值
	 * @return
	 */
	public static String isSelected(String arg, String selectedValue) {
		return (arg != null && arg.equals(selectedValue)) ? "selected" : "";
	}

	/**
	 * 是否checked
	 * 
	 * @param arg
	 *            值
	 * @param selectedValue
	 *            值
	 * @return
	 */
	public static String isChecked(String arg, String checkedValue) {
		return (arg != null && arg.equals(checkedValue)) ? "checked" : "";
	}

	/**
	 * 返回字符串如为null返回空
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	public static String noNull(String s) {
		return s == null ? "" : s;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 首字母大写
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	public static String upperCaseFirst(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 字符串是否在字符串数组中 忽略大小写
	 * 
	 * @param url
	 *            字符串
	 * @param allUrl
	 *            字符串数组
	 * @return
	 */
	public static boolean in(String url, String[] allUrl) {
		if (StringUtils.isEmpty(url)) {
			logger.error("要比较的元素为空");
			return false;
		}
		if (allUrl == null || allUrl.length == 0) {
			logger.error("要比较的数组集合为空");
			return false;
		}
		for (int i = 0; i < allUrl.length; i++) {
			if (allUrl[i].equalsIgnoreCase(url)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串是否在字符串数组中 不忽略大小写
	 * 
	 * @param url
	 *            字符串
	 * @param allUrl
	 *            字符串数组
	 * @return
	 */
	public static boolean inWithCase(String url, String[] allUrl) {
		for (int i = 0; i < allUrl.length; i++) {
			if (allUrl[i].equals(url)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回字符
	 * 
	 * @param n
	 *            第几个
	 * @return
	 */
	public static String getChar(int n) {
		return String.valueOf((char) n);
	}

	/**
	 * 返回字符
	 * 
	 * @param n
	 *            第几个
	 * @return
	 */
	public static String getCol(int n) {
		return String.valueOf((char) (n + 65));
	}

	/**
	 * 字符串中是否含有中文
	 * 
	 * @param s
	 *            字符串
	 * @return
	 */
	public static boolean includeChinese(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 100) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 格式化sql
	 * 
	 * @param sql
	 *            字符串
	 * @return
	 */
	public static String escapeSql(String sql) {
		if (sql == null) {
			return null;
		}
		// 非常重要，需要进一步丰富
		sql = sql.replaceAll("'", "''");
		sql = sql.replaceAll("_", "\\_");
		sql = sql.replaceAll("%", "\\%");
		sql = sql.replaceAll("\\(", "\\\\(");
		sql = sql.replaceAll("\\)", "\\\\)");
		return sql;
	}

	/**
	 * 格式化字符串并在前台加上空格
	 * 
	 * @param text
	 *            字符串
	 * @return
	 */
	public static String firstIndent(String text) {
		text = text.trim();
		text = text.replaceAll("&nbsp;", "");
		text = text.replaceAll("　", "");
		text = text.replaceAll("　", "");
		text = text.replaceAll("\\s+", "");
		return "&nbsp;&nbsp;&nbsp;&nbsp;" + text;
	}

	// 格式化留言出现的乱码
	public static String formatContent(String text) {
		text = text.replaceAll("&lt;", "<");
		text = text.replaceAll("&gt;", ">");
		text = text.replaceAll("&quot;", "'");
		// &ldquo;&rdquo
		text = text.replaceAll("&ldquo;", "“");
		text = text.replaceAll("&rdquo;", "”");
		text = text.replaceAll("&nbsp;", " ");
		text = text.replaceAll("MainServlet", "http://localhost/MainServlet");

		int i = text.indexOf("SCRRPT");
		int j = text.indexOf("script");

		if (i != -1 || j != -1) {
			text = "留言包含脚本,已被处理!";
		}

		return text;
	}

	/**
	 * 
	 * @param password
	 * @param intensity
	 * @param pwdLength
	 * @return 密码强度 1 为低等强度 2为中等强度 3为高等强度
	 */
	public static int validPassword(String password, String intensity,
			int pwdLength) {
		String charGroup[][] = {
				{ "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
						"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
						"x", "y", "z" },
				{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
						"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
						"X", "Y", "Z" },
				{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" } };
		int level = 0;
		boolean flagGroup[] = { false, false, false };
		for (int i = 0; i < charGroup.length; i++) {
			String charSmallGroup[] = charGroup[i];

			for (int j = 0; j < password.length(); j++) {
				if (flagGroup[i] == true) {
					continue;
				}

				String str = password.substring(j, j + 1);
				if (inWithCase(str, charSmallGroup)) {
					level++;
					flagGroup[i] = true;
				}
			}
		}

		return level;
	}

	/**
	 * 将用逗号隔开的sql参数变成in (...)的形式
	 * 
	 * @param arg
	 *            format: 1,2,3,4,5
	 * @return format: in ('1','2','3','4','5')
	 */
	public static String getInSqlId(String arg) {
		if (arg == null || arg.trim().length() == 0)
			return null;

		return getInSqlId(StringUtils.split(arg, ','));
	}

	/**
	 * 将用逗号隔开的sql参数变成in (...)的形式
	 * 
	 * @param arr
	 * @return format: in ('1','2','3','4','5')
	 */
	public static String getInSqlId(String[] arr) {
		if (arr == null || arr.length == 0)
			return null;
		else if (arr.length == 1) {
			return "='" + StringEscapeUtils.escapeSql(arr[0]) + "'";
		}

		String returnStr = " in ('" + StringEscapeUtils.escapeSql(arr[0]) + "'";
		for (int i = 1; i < arr.length; i++) {
			returnStr += ",'" + StringEscapeUtils.escapeSql(arr[i]) + "'";
		}
		returnStr += ")";

		return returnStr;
	}

	/**
	 * 左边补零以满足长度要求
	 * 
	 * resultLength 最终长度
	 */
	public static String addZeroLeft(String arg, int resultLength) {
		if (arg == null)
			return "";
		String result = arg;
		if (result.length() < resultLength) {
			for (int i = result.length(); i < resultLength; i++) {
				result = "0" + result;
				if (result.length() == resultLength)
					break;
			}
		}
		return result;
	}

	/**
	 * 获取相同字符的个数；
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int getSameCharCount(String str1, String str2) {
		// 评论的内容有问题，统计不正确
		// int count = 0;
		// int start = str2.length();
		// int i = 0;
		// if (str1 != null && str1 != "") {
		// while (i < str1.length() && count < 5) {
		// if (str1.indexOf(str2, i) != -1) {
		// count++;
		// i = i + start;
		// } else {
		// i++;
		// if (count == 0 && i >= 50) {
		// break;
		// }
		// }
		// }
		// }
		// return count;
		return StringUtils.countMatches(str1, str2);
	}

	/**
	 * 判断是否手机号码
	 * 
	 * @param str
	 * @param num
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[3458]\\d{9}");
		Matcher matcher = pattern.matcher(str.trim());
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否固定电话
	 * 
	 * @param str
	 * @param num
	 * @return
	 */
	public static boolean isPhone(String str) {
		Pattern pattern = Pattern
				.compile("0\\d{2}-\\d{8}|0\\d{3}-\\d{7}|\\d{8}|\\d{7}");
		Matcher matcher = pattern.matcher(str.trim());
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isSingleMail(String str) {
		Pattern pattern = Pattern
				.compile("(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}");
		Matcher matcher = pattern.matcher(str.trim());
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isMailNo(String str) {
		if (StringUtils.isNotEmpty(str)) {
			if (str.contains("/") && str.length() > 1) {
				String[] tempArray = str.split("/");
				for (String tempStr : tempArray) {
					if (!isSingleMail(tempStr)) {
						return false;
					}
				}
				return true;
			} else if (str.indexOf(" ") > 1) {
				String[] tempArray = str.split(" ");
				for (String tempStr : tempArray) {
					if (!isSingleMail(tempStr)) {
						return false;
					}
				}
				return true;
			} else if (str.indexOf("\n") > 1) {
				String[] tempArray = str.split("\n");
				for (String tempStr : tempArray) {
					if (!isSingleMail(tempStr)) {
						return false;
					}
				}
				return true;
			} else {
				return isSingleMail(str);
			}
		}

		return false;
	}

	/**
	 * 在min和max之间生成随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int NextInt(final int min, final int max) {

		Random rand;
		rand = new Random();
		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;
	}

	/**
	 * 解析json字符串
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static Object toObject(String json, Class clazz) {
		Object obj = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (StringUtils.isNotEmpty(json)) {
				obj = mapper.readValue(json, clazz);
			}
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}

}
