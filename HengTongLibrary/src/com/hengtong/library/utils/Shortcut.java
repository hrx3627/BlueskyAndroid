package com.hengtong.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

/**
 * 关于桌面快捷方式
 * 
 * @author Dragon
 * @time 2013-3-5
 */
public class Shortcut {

	/**
	 * 创建快捷方式
	 * 
	 * @param context
	 *            上下文
	 * @param aCls
	 *            快捷方式启动对应类对象
	 * @param aIconImageId
	 *            快捷方式图标对应资源id
	 * @param aIconText
	 *            快捷方式标题
	 */
	public static void create(final Context context, Class<?> aCls, int aIconImageId, String aIconText) {
		if (!shortCutInstalled(context, aIconText)) {
			Intent myIntent = new Intent(Intent.ACTION_MAIN);
			myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			myIntent.setClassName(context, context.getPackageName() + "." + aCls.getSimpleName());
			Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			Parcelable icon = Intent.ShortcutIconResource.fromContext(context, aIconImageId); // 获取快捷键的图标
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);// 快捷方式的图标
			// shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, R.drawable.beach);
			// //可以代替上面两行代码
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, aIconText);// 快捷方式的标题
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);// 快捷方式的动作
			shortcut.putExtra("duplicate", false);
			context.sendBroadcast(shortcut);// 完了你还可以告诉系统你创建了个快捷方式
		}
	}

	/**
	 * 判断快捷方式是否创建过
	 * 
	 * @param context
	 *            上下文
	 * @param aIconText
	 *            快捷方式标题
	 * @return
	 */
	public static boolean shortCutInstalled(Context context, String aIconText) {
		boolean isInstallShortcut = false;
		ContentResolver cr = context.getContentResolver();
		String AUTHORITY = "com.android.launcher.settings";
		Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

		Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?", new String[] { aIconText }, null);
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
			c.close();
			c = null;
		} else {
			AUTHORITY = "com.android.launcher2.settings";
			CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
			Cursor c2 = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?", new String[] { aIconText }, null);
			if (c2 != null && c2.getCount() > 0) {
				isInstallShortcut = true;
				c2.close();
				c2 = null;
			}
		}
		return isInstallShortcut;
	}

	public static void unCreate(Context context, Class<?> aCls, String aAppName) {
		Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, aAppName);
		Intent myIntent = new Intent(Intent.ACTION_MAIN);
		myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		myIntent.setClass(context, aCls);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
		context.sendBroadcast(intent);
	}
}
