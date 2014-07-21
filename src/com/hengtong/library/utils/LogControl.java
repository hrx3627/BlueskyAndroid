package com.hengtong.library.utils;

import android.util.Log;

/**
 * 功能： 日志打印管理
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class LogControl {

	private static LogType mLogType;
	private static String mDefaultPrefix = " HT--- ";

	static {
		mLogType = LogType.ERROR;
	}

	/**
	 * 设置日志默认潜在,默认为 --- "
	 * @param mDefaultPrefix
	 */
	public static void setDefaultPrefix(String mDefaultPrefix) {
		LogControl.mDefaultPrefix = mDefaultPrefix;
	}

	/**
	 * 日志类型
	 */
	public enum LogType {
		NONE, VERBOSE, DEBUG, INFO, WARN, ERROR, ALL
	}

	/**
	 * 获取日志输出类型
	 * @return
	 */
	public static LogType getLogType() {
		return mLogType;
	}

	/**
	 * 设置日志输出类型
	 * @param mLogType
	 */
	public static void setLogType(LogType mLogType) {
		LogControl.mLogType = mLogType;
	}

	public static final void e(String tag, Object... objects) {
		if (logable(LogType.ERROR)) {
			StrBuffer sb = new StrBuffer();
			sb.append(mDefaultPrefix);
			sb.append(objects);
			Log.e(tag, sb.toString());
		}
	}

	public static final String getExceptionInfo(Throwable tr) {
		return Log.getStackTraceString(tr);
	}

	public static final void w(String tag, Object... objects) {
		if (logable(LogType.WARN)) {
			StrBuffer sb = new StrBuffer();
			sb.append(mDefaultPrefix);
			sb.append(objects);
			Log.w(tag, sb.toString());
		}
	}

	public static final void i(String tag, Object... objects) {
		if (logable(LogType.INFO)) {
			StrBuffer sb = new StrBuffer();
			sb.append(mDefaultPrefix);
			sb.append(objects);
			Log.i(tag, sb.toString());
		}
	}

	public static final void d(String tag, Object... objects) {
		if (logable(LogType.DEBUG)) {
			StrBuffer sb = new StrBuffer();
			sb.append(mDefaultPrefix);
			sb.append(objects);
			Log.d(tag, sb.toString());
		}
	}

	public static final void v(String tag, Object... objects) {
		if (logable(LogType.VERBOSE)) {
			StrBuffer sb = new StrBuffer();
			sb.append(mDefaultPrefix);
			sb.append(objects);
			Log.v(tag, sb.toString());
		}
	}

	private static final boolean logable(LogType aLogType) {
		if (mLogType == LogType.ALL || mLogType == aLogType) {
			return true;
		}
		return false;
	}

	/**
	 * debug标签日志输出(建议仅在测试时使用改方法)
	 * @param objects
	 */
	public static final void debug(Object... objects) {
		if (logable(LogType.DEBUG)) {
			StrBuffer sb = new StrBuffer();
			sb.append(mDefaultPrefix);
			sb.append(objects);
			Log.d("debug", sb.toString());
		}
	}

}
