package com.ytoec.uninet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 日期处理集合类 主要功能：日期格式转换,日期比较。
 * 
 */

public class DateUtil {

	 private static Logger logger=LoggerFactory.getLogger(DateUtil.class);
     public static String getDateStr(Date date,String format){
    	 if(date==null)date=new Date();
         SimpleDateFormat sdf=new SimpleDateFormat(format);
         return sdf.format(date); 
     }

	/**
	 * 格式化日期以字符串形式返回
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */
	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date == null) {
			return "";
		}
		return sdf.format(date);
	}

	/**
	 * 格式化日期 返回 如(2000-01-10 12:00:20)
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toSeconds(Date date) {
		// return format(date, "yyyy-MM-dd hh:mm:ss");
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String toSecond(Date date) {
		// return format(date, "yyyy-MM-dd hh:mm:ss");
		return format(date, "yyyyMMddHHmmss");
	}

	/**
	 * 格式化日期 返回 如(2000-01-10)
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toDay(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化日期 返回 如(20000110)
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toShortDay(Date date) {
		return format(date, "yyyyMMdd");
	}

	/**
	 * 格式化日期 返回月份
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toMonth(Date date) {
		return format(date, "MM");
	}

	/**
	 * 格式化日期 返回日期
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toShortdd(Date date) {
		return format(date, "dd");
	}

	/**
	 * 格式化日期 返回时分秒
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toShortSeconds(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 格式化日期 返回年份
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String toShortYear(Date date) {
		return format(date, "yyyy");
	}

	/**
	 * 以指定的格式返回日期
	 * 
	 * @param s
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */
	public static Date valueof(String s, String format) {
		return parse(s, format);
	}

	/**
	 * 以默认的格式返回日期
	 * 
	 * @param s
	 * @return
	 */
	public static Date valueOfStandard(String s) {
		return parse(s, "yyyy-MM-dd");
	}

	/**
	 * 返回日期 返回 如(20000110)
	 * 
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static Date valueOfShort(String s) throws ParseException {
		return parse(s, "yyyyMMdd");
	}

	/**
	 * 判断日期是否相同
	 * 
	 * @param d1
	 *            日期
	 * @param d2
	 *            日期
	 * @return
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		return DateUtil.roundToDay(d1).getTime() == DateUtil.roundToDay(d2)
				.getTime();
	}

	/**
	 * 比较日期大小返回布尔型
	 * 
	 * @param d1
	 *            日期
	 * @param d2
	 *            日期
	 * @return
	 */
	public static boolean compareDay(Date d1, Date d2) {
		return DateUtil.roundToDay(d1).before(DateUtil.roundToDay(d2));
	}

	/**
	 * 以指定格式格式化日期
	 * 
	 * @param s
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */
	private static Date parse(String s, String format) {
		if (s == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 日期相减 返回天数
	 * 
	 * @param big
	 *            大的日期
	 * @param small
	 *            小的日期
	 * @return
	 */
	public static double dayInterval(Date big, Date small) {
		big = roundToDay(big);
		small = roundToDay(small);
		return (big.getTime() - small.getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 日期相减 返回分钟
	 * 
	 * @param big
	 *            大的日期
	 * @param small
	 *            小的日期
	 * @return
	 */
	public static double minuteInterval(Date big, Date small) {
		return (big.getTime() - small.getTime()) / (1000 * 60);
	}

	/**
	 * 日期相减 返回秒数
	 * 
	 * @param big
	 *            大的日期
	 * @param small
	 *            小的日期
	 * @return
	 */
	public static double secondInterval(Date big, Date small) {
		return (big.getTime() - small.getTime()) / 1000;
	}

	/**
	 * 日期相减 以几个星期的形式返回
	 * 
	 * @param small
	 *            小的日期
	 * @param big
	 *            大的日期
	 * @return
	 */
	public static int workDayInterval(Date small, Date big) {
		big = roundToDay(big);
		small = roundToDay(small);

		GregorianCalendar smallGc = new GregorianCalendar();
		smallGc.setTime(small);

		GregorianCalendar bigGc = new GregorianCalendar();
		bigGc.setTime(big);

		int workDays = 0;
		long bigTime = bigGc.getTime().getTime();
		while (smallGc.getTime().getTime() < bigTime) {
			int week = smallGc.get(Calendar.DAY_OF_WEEK);
			// int month = smallGc.get(Calendar.MONTH) + 1;
			smallGc.add(Calendar.DATE, 1);
			if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
				continue;
			} else {
				workDays++;
			}
		}
		return workDays;
	}

	/**
	 * 判断日期是否为周六和周日
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static boolean isWorkDay(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int week = gc.get(Calendar.DAY_OF_WEEK);
		if (week == Calendar.SATURDAY || week == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date roundToDay(Date date) {
		date = roundToHour(date);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		return gc.getTime();
	}

	public static Date roundToHour(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	/**
	 * 返回日期的下一天
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date nextDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, 1);
		return roundToDay(gc.getTime());
	}

	/**
	 * 返回日期的下一个小时
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date nextHour(Date date) {
		date = add(date, Calendar.HOUR, 1);
		return roundToHour(date);
	}

	public static Date add(Date date, int field, int amount) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(field, amount);
		return gc.getTime();
	}

	/**
	 * 返回日期的上一天
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date lastDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, -1);
		return roundToDay(gc.getTime());
	}

	/**
	 * 返回日期的上三天
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static Date lastThreeDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, -3);
		return gc.getTime();
	}

	public static Date roundToMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.DATE, 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	public static Date getFirstDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.DATE, 1);
		return roundToDay(gc.getTime());
	}

	public static Date getFirstDayOfMonthByNum(Date date, int num) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, num);
		gc.set(Calendar.DATE, 1);
		return roundToDay(gc.getTime());
	}

	public static Date getFirstDayOfWeek(Date date) {
		while (DateUtil.getTimeField(date, Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			date = DateUtil.lastDate(date);
		}
		return date;
	}

	public static Date getLastDayOfMonth(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		gc.add(Calendar.MONTH, 1);
		gc.set(Calendar.DATE, 0);
		return roundToDay(gc.getTime());
	}

	public static Date getLastDayOfWeek(Date date) {
		while (DateUtil.getTimeField(date, Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			date = DateUtil.nextDate(date);
		}
		return date;
	}

	/**
	 * 返回oracle的日期函数格式
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String oracleToDate(Date date) {
		return "to_date('" + DateUtil.format(date, "yyyy-MM-dd")
				+ "', 'yyyy-mm-dd')";
	}

	public static int getTimeField(Date date, int field) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return gc.get(field);
	}

	public static Date setTimeField(Date date, int field, int timeNum) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(field, timeNum);
		return gc.getTime();
	}

	/**
	 * 判断日期是 pm还是 am
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String ampm(Date date) {
		int hours = getTimeField(date, Calendar.HOUR_OF_DAY);

		if (hours <= 12) {
			return "A";
		}
		return "P";
	}

	public static String ampm(Date startTime, Date endTime) {
		String start = ampm(startTime);
		String end = ampm(endTime);

		if (start == "A" && end == "A") {
			return "A";
		} else if (start == "P" && end == "P") {
			return "P";
		}
		return "N";

	}

	public static Date[] getTimeInterval(Date date, String ampm) {

		Date startDate = (Date) date.clone();
		Date endDate = (Date) date.clone();

		if (ampm == "A") {
			// startDate.setHours(9);
			startDate = setTimeField(startDate, Calendar.HOUR_OF_DAY, 9);
			endDate = setTimeField(endDate, Calendar.HOUR_OF_DAY, 12);
		} else if (ampm == "P") {
			startDate = setTimeField(startDate, Calendar.HOUR_OF_DAY, 12);
			endDate = setTimeField(endDate, Calendar.HOUR_OF_DAY, 18);
		} else if (ampm == "N") {
			startDate = setTimeField(startDate, Calendar.HOUR_OF_DAY, 9);
			endDate = setTimeField(endDate, Calendar.HOUR_OF_DAY, 18);
		}

		startDate = setTimeField(startDate, Calendar.MINUTE, 0);
		endDate = setTimeField(endDate, Calendar.MINUTE, 0);
		startDate = setTimeField(startDate, Calendar.SECOND, 0);
		endDate = setTimeField(endDate, Calendar.SECOND, 0);

		Date[] dates = new Date[2];
		dates[0] = startDate;
		dates[1] = endDate;

		return dates;
	}

	public static String getChineseWeekName(Date date) {
		int w = DateUtil.getTimeField(date, Calendar.DAY_OF_WEEK);
		String cw = "";
		switch (w) {
		case Calendar.SUNDAY:
			cw = "星期日";
			break;
		case Calendar.MONDAY:
			cw = "星期一";
			break;
		case Calendar.TUESDAY:
			cw = "星期二";
			break;
		case Calendar.WEDNESDAY:
			cw = "星期三";
			break;
		case Calendar.THURSDAY:
			cw = "星期四";
			break;
		case Calendar.FRIDAY:
			cw = "星期五";
			break;
		case Calendar.SATURDAY:
			cw = "星期六";
			break;
		default:
			break;
		}
		return cw;
	}

	public static Date nextSevenDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, 7);
		return roundToDay(gc.getTime());
	}

	public static Date previousSevenDate(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, -7);
		return roundToDay(gc.getTime());
	}

	public static Date previousYear(Date date, int num) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, -num);
		return roundToDay(gc.getTime());
	}

	public static Date setTimeOfDay(Date date, int hour, int minute, int second) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.HOUR_OF_DAY, hour);
		gc.set(Calendar.MINUTE, minute);
		gc.set(Calendar.SECOND, second);
		gc.set(Calendar.MILLISECOND, 0);
		return gc.getTime();
	}

	/**
	 * 
	 * @param oldStartTime
	 * @param oldEndTime
	 * @param newStartTime
	 * @param newEndTime
	 * @return
	 */
	public static boolean isTimeContain(Date oldStartTime, Date oldEndTime,
			Date newStartTime, Date newEndTime) {
		if (!(oldEndTime.getTime() <= newStartTime.getTime() || oldStartTime
				.getTime() >= newEndTime.getTime())) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
		// List list = listMonthOption(24);
		// for (int i = 0; i < list.size(); i++) {
		// Date d = (Date) list.get(i);
		// logger.debug(format(d, "yyyyMM"));
		// }
		logger.debug(format(new Date(), "yyyy-M-d h:m:s"));
	}

	/**
	 * 取日期的最小值
	 * 
	 * @param d1
	 *            日期
	 * @param d2
	 *            日期
	 * @return
	 */
	public static Date min(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			return d2;
		} else {
			return d1;
		}
	}

	/**
	 * 求日期的最大值
	 * 
	 * @param d1
	 *            日期
	 * @param d2
	 *            日期
	 * @return
	 */
	public static Date max(Date d1, Date d2) {
		if (d1.getTime() < d2.getTime()) {
			return d2;
		} else {
			return d1;
		}
	}

	/**
	 * 比较时间 如date1小于date2返回1 date1大于date2返回-1否则返回0
	 * 
	 * @param date1
	 *            日期
	 * @param date2
	 *            日期
	 * @return
	 */
	public static int compareTime(Date date1, Date date2) {
		GregorianCalendar g1 = new GregorianCalendar();
		g1.setTime(date1);
		GregorianCalendar g2 = new GregorianCalendar();
		g1.setTime(date2);

		clearYMD(g1);
		clearYMD(g2);

		if (g1.before(g2)) {
			return 1;
		} else if (g2.before(g1)) {
			return -1;
		}

		return 0;
	}

	/**
	 * 清空年月日
	 * 
	 * @param g
	 *            日期对象
	 */
	private static void clearYMD(GregorianCalendar g) {
		g.set(Calendar.YEAR, 1900);
		g.set(Calendar.MONTH, 0);
		g.set(Calendar.DATE, 1);
	}

	/**
	 * 显示开始月份和结束月份之间的所有月份
	 * 
	 * @param startMonth
	 *            开始月份
	 * @param endMonth
	 *            结束月份
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List listMonthOption(Date startMonth, Date endMonth) {
		List list = new ArrayList();

		Date date = endMonth;
		while (date.getTime() - startMonth.getTime() > 0) {
			list.add(date);
			date = DateUtil.add(date, Calendar.MONTH, -1);
		}
		return list;
	}

	/**
	 * 重载方法
	 * 
	 * @param monthNum
	 *            月份差
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List listMonthOption(int monthNum) {
		Date endDate = new Date();
		Date startDate = DateUtil.add(endDate, Calendar.MONTH, -monthNum);

		return listMonthOption(startDate, endDate);
	}

	/**
	 * 取 指定天数 前的日期
	 * 
	 * @param date
	 *            参考日期
	 * @param days
	 *            指定天数
	 * @return date
	 */
	@SuppressWarnings("static-access")
	public static Date getDateBefore(Date date, int days) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.set(cld.DATE, (cld.get(cld.DATE) + days));
		return cld.getTime();
	}

	/** 重载方法 <br> {@link getDateBefore(Date date, int days)} */
	public static Date getDateBefore(int days) {
		return getDateBefore(new Date(), days);
	}

	/**
	 * 取 指定天数 后的日期
	 * 
	 * @param date
	 *            参考日期
	 * @param days
	 *            指定天数
	 * @return date
	 */
	@SuppressWarnings("static-access")
	public static Date getDateAfter(Date date, int days) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.set(cld.DATE, (cld.get(cld.DATE) - days));
		return cld.getTime();
	}

	/** 重载方法 <br> {@link getDateAfter(Date date, int days)} */
	public static Date getDateAfter(int days) {
		return getDateAfter(new Date(), days);
	}

}
