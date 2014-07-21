package com.hengtong.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * 功能：图片压缩相关
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class CompressImage {

	/**
	 * 将Bitmap对象以一定质量保存为文件形式
	 * @param bm
	 * @param strFileName
	 * @param quality
	 * @return
	 */
	public static boolean save(Bitmap bm, String strFileName, int quality) {
		if (bm == null) {
			return false;
		}
		boolean result = false;
		try {
			if (quality > 100 || quality < 0) {
				quality = 90;
			}
			FileOutputStream file = new FileOutputStream(strFileName);
			result = bm.compress(CompressFormat.JPEG, quality, file);
			file.flush();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return result;
	}

	/**
	 * 根据路径得到不超过指定高,宽的Bitmap对象
	 * @param filePath
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static final Bitmap getCompressBitmap(String filePath, int maxWidth, int maxHeight) {
		File mFile = new File(filePath);
		if (!mFile.exists() || !mFile.isFile()) {
			mFile = null;
			return null;
		}
		Bitmap bm = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		// _computeSampleSize(opts, SysConstants.MSG_SMALL_IMG_WIDTH,
		// SysConstants.MSG_SMALL_IMG_HEIGHT);
		int roundedSize = 1, picWidth = opts.outWidth, picHeight = opts.outHeight;
		if (picWidth > maxWidth || picHeight > maxHeight) {
			// if (picWidth > picHeight) roundedSize = (picWidth > maxWidth) ?
			// picWidth / maxWidth : roundedSize;
			// else roundedSize = (picHeight > maxHeight) ? picHeight /
			// maxHeight : roundedSize;
			int scaleWidth = (int) Math.ceil((float) picWidth / maxWidth);
			int scaleHeight = (int) Math.ceil((float) picHeight / maxHeight);
			roundedSize = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
		}
		opts.inSampleSize = roundedSize;
		opts.inJustDecodeBounds = false;
		try {
			// bm = BitmapFactory.decodeFile(filePath, opts);
			bm = BitmapFactory.decodeStream(new FileInputStream(mFile), null, opts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * 获取Bitmap的读取参数
	 */
	public static final BitmapFactory.Options getBitmapOption() {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return opt;
	}
}
