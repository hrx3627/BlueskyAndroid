package com.hengtong.library.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 功能：网络工具
 * @ahthor：黄荣星
 * @date:2013-11-18
 * @version::V1.0
 */
public class HTNetTool {

	private static String LOG_TAG = "HTNetTool";

	public static Uri uri = Uri.parse("content://telephony/carriers");

	/**
	 * 判断是否有网络连接 true if have network and if false have no network
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			LogControl.e(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isAvailable()) {
						Log.i(LOG_TAG, "network is available");
						return true;
					}
				}
			}
		}
		Log.i(LOG_TAG, "network is not available");
		return false;
	}

	/**
	 * method desc：判断是否有网络连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	/**
	 * 判断网络是否为漫游
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null && tm.isNetworkRoaming()) {
					Log.d(LOG_TAG, "network is roaming");
					return true;
				} else {
					Log.d(LOG_TAG, "network is not roaming");
				}
			} else {
				Log.d(LOG_TAG, "not using mobile network");
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isMobileDataEnable(Context context) {
		boolean isMobileDataEnable = false;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			isMobileDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		} catch (Exception e) {
			LogControl.e(LOG_TAG, "isMobileDataEnable()==>exception:" + e.getMessage());
		}

		return isMobileDataEnable;
	}

	/**
	 * 判断wifi 是否可用
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isWifiDataEnable(Context context) {
		boolean isWifiDataEnable = false;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			isWifiDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		} catch (Exception e) {
			LogControl.e(LOG_TAG, "isWifiDataEnable()==>exception:" + e.getMessage());
		}
		return isWifiDataEnable;
	}

	/**
	 * WIFI网络开关
	 */
	public static void openWiFi(Context context, boolean enabled) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wm.setWifiEnabled(enabled);
	}

	/**
	 * 移动网络开关
	 */
	public static void openMobileData(Context context, boolean enabled) {
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context
	 * @return true 表示开启
	 */
	public static boolean isOPenGps(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return gps;
	}

	/**
	 * 强制帮用户打开GPS
	 * @param context
	 */
	public static void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	public static void openPGSToSercialPhone(Context context) {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		context.sendBroadcast(intent);

		@SuppressWarnings("deprecation")
		String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			context.sendBroadcast(poke);
		}
	}

	// 判断手机是否有GPS模块
	public static boolean hasGPSDevice(Context context) {
		final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (mgr == null)
			return false;
		final List<String> providers = mgr.getAllProviders();
		if (providers == null)
			return false;
		return providers.contains(LocationManager.GPS_PROVIDER);
	}

}
