package com.hengtong.library.utils;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 功能：程序辅助管理
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class AppHelper {

	/**
	 * 模拟Home键
	 */
	public static void imitateHome(Context context) {
		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 通过文件路径安装程序
	 */
	public static boolean install(Context context, String filaPath) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(filaPath)), "application/vnd.android.package-archive");
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	// /**
	// * Dip转成对应Px值
	// */
	// public static int dipTopx(Context context, float dip) {
	// float s = context.getResources().getDisplayMetrics().density;
	// return (int) (dip * s + 0.5f);
	// }

	/**
	 * 判断程序是否前段运行
	 */
	public static boolean isAppRunningForeground(Context context, String packageName) {
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		List<RunningTaskInfo> mRunningTaskInfoList = mActivityManager.getRunningTasks(1);
		if (mRunningTaskInfoList == null || mRunningTaskInfoList.isEmpty())
			return false;
		RunningTaskInfo mRunningTaskInfo = mRunningTaskInfoList.get(0);
		String strPackageName = mRunningTaskInfo.topActivity.getPackageName();
		if (strPackageName != null && strPackageName.equals(packageName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取sdk系统版本
	 */
	public static String getOsVersion() {
		String osVersion = android.os.Build.VERSION.RELEASE;
		return osVersion;
	}

	/**
	 * 获取手机生产商
	 */
	public static String getVendor() {
		String vendor = android.os.Build.MODEL + android.os.Build.ID;
		return vendor;
	}

	/**
	 * Dip转成对应Px值
	 * @param context 上下文
	 * @param dip
	 * @return
	 */
	public static int dipTopx(Context context, float dip) {
		float s = context.getResources().getDisplayMetrics().density;
		return (int) (dip * s + 0.5f);
	}
}
