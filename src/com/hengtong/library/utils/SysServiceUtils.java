package com.hengtong.library.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import com.hengtong.library.utils.struct.NetWorkInfo;
import com.hengtong.library.utils.struct.SimInfo;
import com.hengtong.library.utils.struct.StorageInfo;
import com.hengtong.library.utils.struct.SysVersionInfo;

public class SysServiceUtils 
{	
	/**
	 * 网络是否连接
	 * @return
	 */
	public static boolean isConnect(Context context)
	{
		NetWorkInfo info =  getNetWorkState(context);
		if(info == null)
			return false;
		//WIFI网络
		switch(info.get_type())
		{
		case ConnectivityManager.TYPE_WIFI:
			return true;
		case ConnectivityManager.TYPE_MOBILE:
			return true;
//		case ConnectivityManager.TYPE_BLUETOOTH:	//蓝牙
//			return false;
//		case ConnectivityManager.TYPE_DUMMY:		//
//			return false;
//		case ConnectivityManager.TYPE_ETHERNET:		//以太网
//			return true;
		case ConnectivityManager.TYPE_MOBILE_DUN:		
			return false;
		case ConnectivityManager.TYPE_MOBILE_MMS:		
			return false;
		case ConnectivityManager.TYPE_MOBILE_SUPL:		
			return false;
		case ConnectivityManager.TYPE_WIMAX:		
			return false;
		default:
			return false;
		}
	}
	/**
	 * 获取手机网络状态
	 * @return
	 */
	public static NetWorkInfo getNetWorkState(Context context) 
	{
		NetWorkInfo info = new NetWorkInfo();
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if(networkInfo != null)
		{
			info.set_type(networkInfo.getType());
			info.set_extraInfo(networkInfo.getExtraInfo());
			if(StringUtils.isNullOrEmpty(info.get_extraInfo()))
			{
				info.set_extraInfo("");
			}
			return info;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获取SIM状态
	 */
	public static SimInfo getSimInfo(Context context)
	{
		SimInfo info = new SimInfo();
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		info.set_state(tm.getSimState());  
		info.set_desc(getSIMDesc(info));
		info.set_key(tm.getSimSerialNumber());
		
		return info;
	}
	
	/**
	 *  获取 SIM的文字描述
	 * @param SimInfo
	 * @return
	 */
	private static String getSIMDesc(SimInfo info)
	{
		switch (info.get_state()) 
		{
		case TelephonyManager.SIM_STATE_ABSENT:  
			return "无SIM卡";
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return "需要NetworkPIN解锁";
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			return "需要PIN解锁";
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			return "需要PUK解锁";
		case TelephonyManager.SIM_STATE_READY: 
			return "SIM正常";
		default:
			return "未知状态";
		}
	}
	
	/**
	 * 测试GPS是否打开
	 * @return
	 */
	public static boolean isGpsOpen(Context context)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if(lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER ))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 跳到拨打电话界面打电话
	 * @param context
	 * @param phone
	 */
	public static void jumpToCallApp(final Context context,String phone)
	{
		String action = Intent.ACTION_CALL;// 显示拨号界面
		Intent intent = new Intent(action, Uri.parse("tel:" + phone));
		context.startActivity(intent);
	}
	
	/**
	 * 获取系统的SD卡和系统的存储空间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static StorageInfo getStorageInfo()
	{
		StorageInfo storage = new StorageInfo();
		//是否有SD卡
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			storage.set_haveSD(true);
		}
		else
		{
			storage.set_haveSD(false);
		}
		
		//获取SD卡的信息
		if(storage.is_haveSD())
		{
			storage.set_SDPath(Environment.getExternalStorageDirectory().getAbsolutePath());
			StatFs statfs = new StatFs(storage.get_SDPath());
			if(android.os.Build.VERSION.SDK_INT >= 18)
			{
				storage.set_SDBlockSize(statfs.getBlockSizeLong());
				storage.set_SDBlockTotal(statfs.getBlockCountLong());
				storage.set_SDBlockIdle(statfs.getAvailableBlocksLong());
			}
			else
			{
				storage.set_SDBlockSize(statfs.getBlockSize());
				storage.set_SDBlockTotal(statfs.getBlockCount());
				storage.set_SDBlockIdle(statfs.getAvailableBlocks());
			}
		}
		
		//获取系统的空间
		storage.set_currentPath(Environment.getRootDirectory().getAbsolutePath());
		StatFs sysstatfs = new StatFs(storage.get_currentPath());
		if(android.os.Build.VERSION.SDK_INT >= 18)
		{
			storage.set_SysBlockSize(sysstatfs.getBlockSizeLong());
			storage.set_SysBlockTotal(sysstatfs.getBlockCountLong());
			storage.set_SysBlockIdle(sysstatfs.getAvailableBlocksLong());	
		}
		else
		{
			storage.set_SysBlockSize(sysstatfs.getBlockSize());
			storage.set_SysBlockTotal(sysstatfs.getBlockCount());
			storage.set_SysBlockIdle(sysstatfs.getAvailableBlocks());	
		}
		
		return storage;
	}
	
	/**
	 * 获取系统的安卓版本
	 * @return
	 */
	public static SysVersionInfo getAndroidVersion(Context context)
	{
		SysVersionInfo info = new SysVersionInfo();
		info.set_phoneType(android.os.Build.MODEL);
		info.set_androidSDK(android.os.Build.VERSION.SDK_INT);
		info.set_androidRelease(android.os.Build.VERSION.RELEASE);
		
		try 
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			info.set_appCode(packageInfo.versionCode);
			info.set_appVersion(packageInfo.versionName);
			info.set_appPackName(packageInfo.packageName);
		} 
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return info;
	}
}

