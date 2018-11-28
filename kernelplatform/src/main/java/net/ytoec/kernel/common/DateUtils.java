package net.ytoec.kernel.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转化公共类
 * create by wangmindong
 * create time 2013-03-28
 * @author wmd
 *
 */
public class DateUtils {
	
	private static final String[] FORMATS = {"yyyy-MM-dd HH:mm:ss z" ,"yyyy-MM-dd HH:mm:ss.S z"};
	
	public static Date toDate(String s) {
		for (String format : FORMATS) {
			SimpleDateFormat sdf= new SimpleDateFormat(format);
			Date date = null;
			try {
				date = sdf.parse(s);
				return date;
			} catch (ParseException e) { }
		}

		return null;
	}
	
	public static String toString(Date date) {
		if (date == null)
			return "";
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
		String str = sdf.format(date);
		
		return str;
	}

	public static String toString2(Date date) {
		if (date == null)
			return "";
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0 z");
		String str = sdf.format(date);
		
		return str;
	}
	
	/*
	public static void main(String[] args) {
		String s = "2005-08-24 08:00:00 CST";
		
		Date date = DateUtils.toDate(s);
		
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		System.out.println(date);
		System.out.println(d1);
	}
	
	
	public static void main(String[] args) {
		Date date = new Date();
		
		String str = DateUtils.toString(date);
		
		System.out.println(str);
	}*/
	
}

