package com.hengtong.library.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 功能：屏幕工具
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class DensityTool {
	private Context mContext;
	private DisplayMetrics dm;

	public DensityTool(Activity activity) {
		this.dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(this.dm);
		this.mContext = activity;
	}

	public int getScreenHeight() {
		return this.dm.heightPixels;
	}

	public int getScreenWidth() {
		return this.dm.widthPixels;
	}

	public int dip2px(float dpValue) {
		final float scale = this.mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		final float scale = this.mContext.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕高度
	 * @param activity
	 * @return
	 */
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * @param activity
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * Dip转成对应Px值
	 */
	public static int dip2px(Context context, float dip) {
		float s = context.getResources().getDisplayMetrics().density;
		return (int) (dip * s + 0.5f);
	}

	/**
	 * Px转成对应Dip值
	 */
	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
