package com.hengtong.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 功能：日期工具类
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

	// 获取当前时间
	public static String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(new Date());
		return currentTime;
	}

	// 比较两个时间大小 0 表示两个时间一样，-1代表t1比t2小，1代表t1比t2大。
	public static int compareToTime(String t1, String t2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		try {
			c1.setTime(format.parse(t1));
			c2.setTime(format.parse(t2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int result = c1.compareTo(c2);
		return result;
	}

	/* 时间比大小 */
	public static int compareToTime(long t1, long t2) {
		int result = 0;
		if (t1 == t2) {
			return result;
		}
		if (t1 > t2) {
			return result = 1;
		}
		if (t1 < t2) {
			return result = -1;
		}
		return result;
	}

	/**
	 * 增加几天的方法
	 * @param day 增加的天数
	 */
	public static String addDays(int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String currentTime = format.format(new Date());
		Calendar c1 = Calendar.getInstance();
		try {
			c1.setTime(format.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c1.add(Calendar.DATE, day);
		String endTime = format.format(c1.getTime());
		return endTime;
	}

	/**
	 * 减少几天的方法
	 * @param day 增加的天数
	 */
	public static String reduceDays(long date, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = format.format(new Date(date));
		Calendar c1 = Calendar.getInstance();
		try {
			c1.setTime(format.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c1.add(Calendar.DATE, day);
		String endTime = format.format(c1.getTime());
		return endTime;
	}

	/**
	 * 增加几月的方法
	 * @param day 增加的月数
	 */
	public static String addMonths(int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = format.format(new Date());
		Calendar c1 = Calendar.getInstance();
		try {
			c1.setTime(format.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c1.add(Calendar.MONTH, month);
		String endTime = format.format(c1.getTime());
		return endTime;
	}

	/**
	 * 增加几年的方法
	 * @param day 增加的年数
	 */
	public static String addYears(int year) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = format.format(new Date());
		Calendar c1 = Calendar.getInstance();
		try {
			c1.setTime(format.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c1.add(Calendar.YEAR, year);
		String endTime = format.format(c1.getTime());
		return endTime;
	}

	/**
	 * 时间戳,返回你想要的时间格式（时间为长整型）
	 * @param time例如:1328007600000
	 * @param timeFormat例如:"yyyy-MM-dd"
	 */
	public static String getTimeFormat(long time, String timeFormat) {
		if (StringUtils.isNullOrEmpty(time + ""))
			return "";
		return new SimpleDateFormat(timeFormat).format(new Date(time));
	}

	/**
	 * 将时间戳转化为日期形式，比如时间戳1328007600000转化后输出“2012-01-31”
	 */
	public static String getTimeFormat(long date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(date));
	}

	/**
	 * 将时间戳转化为日期形式，比如时间戳“1328007600000”转化后输出“2012-01-31”
	 */
	public static String getTimeFormat(String date) {
		if (StringUtils.isNullOrEmpty(date))
			return "";
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(date)));
	}

	/**
	 * 将时间戳转化为日期形式，比如时间戳“2013-11-23 12:13:23”转化后输出“2012-01-31”
	 */
	@SuppressLint("UseValueOf")
	public static String getTimeFormat(String date, String timeFormat) {
		if (StringUtils.isNullOrEmpty(date))
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date(new Double(date).longValue()));
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurrentTimeFormat(String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(new Date());
	}

	/**
	 * method desc： 毫秒转化成时分秒(计算等待时间)
	 * @param l 两点之间的时间间隔
	 * @return 返回时间
	 */
	public static String ToTimeStr(Long l) {
		int hour = 0;
		int minute = 0;
		int second = 0;

		second = l.intValue();

		if (second >= 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute >= 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		String hourStr = "";
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = hour + "";
		}
		String minuteStr = "";
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = minute + "";
		}
		String secondStr = "";
		if (second < 10) {
			secondStr = "0" + second;
		} else {
			secondStr = second + "";
		}

		StringBuilder str = new StringBuilder().append(hourStr + ":").append(minuteStr).append(":").append(secondStr);
		return str.toString();
	}

	/**
	 * 讲字符串时间转化为Long
	 * @param strTime 字符串时间
	 * @param format 时间格式
	 * @return Long 返回时间戳，如果异常或者错误，则返回当前时间戳
	 */
	public static long stringToLong(String strTime, String format) {
		try {
			if (StringUtils.isNullOrEmpty(format))
				format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			Date date = dateFormat.parse(strTime);
			if (date == null)
				return System.currentTimeMillis();
			return date.getTime();
		} catch (Exception e) {

		}
		return System.currentTimeMillis();
	}

	/**
	 * method desc：获取当前时间
	 * @param paramString 时间格式
	 * @param paramLong 时间戳
	 * @return 返回时间格式字符串
	 */
	public static String getTime(String paramString, long paramLong) {
		return new SimpleDateFormat(paramString).format(new Date(paramLong));
	}

}
