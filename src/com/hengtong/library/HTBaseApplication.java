package com.hengtong.library;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 功能：基础应用上下文
 * @ahthor：黄荣星
 * @date:2013-11-11
 * @version::V1.0
 */
public class HTBaseApplication extends Application implements HTContant {

	protected ArrayList<Activity> activities = null;
	protected static HTBaseApplication instance = null;
	protected Context mContext;
	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		mContext = this;

		imageConfig();
	}

	private void imageConfig() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "cyzl/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCacheExtraOptions(480, 800) // default = device screen dimensions
				.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null).threadPoolSize(3) // default
				.threadPriority(Thread.NORM_PRIORITY - 1) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory()
				// 内存缓存
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
				// sd卡缓存
				.discCache(new UnlimitedDiscCache(cacheDir)) // default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100).discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(mContext)) // default
				.imageDecoder(new BaseImageDecoder(false)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);

	}

	public ImageLoader getmImageLoader() {
		return mImageLoader;
	}

	public DisplayImageOptions displayOptions() {
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
		return displayImageOptions;
	}

	public static HTBaseApplication getInstance() {
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activities == null)
			activities = new ArrayList<Activity>();
		activities.add(activity);
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	/**
	 * method desc：关闭所有的activity
	 */
	public void closeAllActivitys() {
		if (activities != null) {
			for (Activity activity : activities) {
				if (activity != null) {
					activity.finish();
				}
			}
		}
	}

}
