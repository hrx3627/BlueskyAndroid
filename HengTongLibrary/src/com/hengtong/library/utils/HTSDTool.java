package com.hengtong.library.utils;

import java.io.File;

import android.os.Environment;

/**
 * 功能：sd卡工具
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class HTSDTool {
	/**
	 * 检测sdcard是否可用
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	public static String getSdFullPath(String path) {
		String SDCardRootDir = "";
		if (sdCardIsAvailable()) {
			SDCardRootDir = Environment.getExternalStorageDirectory().getPath() + "/" + path;
		}
		initSDDir(SDCardRootDir);
		return SDCardRootDir;
	}
	/**
	 * 
	 * method desc：
	 * @param dirPath
	 * @return
	 */
	private static void initSDDir(String dirPath) {
			File tempFile = new File(dirPath);
			if (!tempFile.exists()) {
				if (tempFile.mkdirs()) {
					LogControl.v("exception","tempFile=" + dirPath + " mkdir success!");
				}
			} else {
				LogControl.v("exception","tempFile=" + dirPath + " is exist!");
			}
	}


	/**
	 * 根据文件名删除文件
	 * @param filePath SDCard卡上完整路径
	 * @return void
	 */
	public static void deleteFile(String filePath) {
		if (StringUtils.isNullOrEmpty(filePath))
			return;
		File file = new File(filePath);
		if (file.exists())
			file.delete();
	}
}
