package com.hengtong.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 功能：文件工具
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class FileUtils {
	private static final String TAG = "FileUtils";

	/**
	 * 创建文件夹
	 * @param filePath
	 */
	public static boolean mkdir(String filePath) {
		try {
			if (filePath == null) {
				return false;
			}
			if (!filePath.endsWith(File.separator))
				filePath = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			} else if (file.isFile()) {
				file.delete();
				file.mkdirs();
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LogControl.e(TAG, "mkdir() -> e", LogControl.getExceptionInfo(e));
			return false;
		}
	}

	/**
	 * 删除目录中除了过滤文件名的所有文件
	 * @param filePath
	 * @param filterPath
	 */
	public static void cleanFilePathWithoutFilterFile(String filePath, String filterPath) {
		File f = new File(filePath);
		if (f.exists() && f.isDirectory() && f.listFiles().length > 0) {
			File delFile[] = f.listFiles();
			int i = delFile.length;
			for (int j = 0; j < i; j++) {
				if (!delFile[j].getPath().equals(filterPath)) {
					if (delFile[j].isDirectory()) {
						cleanFilePathWithoutFilterFile(delFile[j].getAbsolutePath(), filterPath);
					}
					delFile[j].delete();
				}
			}
		}
	}

	/**
	 * 清空指定文件夹路径的文件
	 * @param filePath
	 */
	public static void cleanFilePath(String filePath) {
		File f = new File(filePath);
		if (f.exists() && f.isDirectory() && f.listFiles().length > 0) {
			File delFile[] = f.listFiles();
			int i = delFile.length;
			for (int j = 0; j < i; j++) {
				if (delFile[j].isDirectory()) {
					cleanFilePath(delFile[j].getAbsolutePath());
				}
				delFile[j].delete();
			}
		}
	}

	/**
	 * 复制文件
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public static boolean copyFile(String fromFile, String toFile) {
		try {
			InputStream is = new FileInputStream(fromFile);
			OutputStream os = new FileOutputStream(toFile);
			byte[] buffer = new byte[8192];
			int c = -1;
			while ((c = is.read(buffer)) > -1) {
				os.write(buffer, 0, c);
			}
			is.close();
			os.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LogControl.e(TAG, "copyFile() -> e", LogControl.getExceptionInfo(e));
		}
		return false;
	}
}
