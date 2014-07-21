package com.hengtong.library;

/**
 * 功能：系统常量
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public interface HTContant {

	/**
	 * 对话框消失等待时间：1.5秒
	 */
	public static final int DIG_DISMISS_DELAY = 1500;

	// **************************** 返回值定义 ***************************
	public static final int RET_SUCCESS = 0;// 请求成功
	public static final int RET_FAIL = -1;// 请求失败
	public static final int RET_NET_ERROR = -1000;// 网络错误
	public static final int RET_NET_ERROR_NULL = -1001;// 服务器返回空

	public static final String SDCARD_PATH_ROOT = "hengtong"; // 根目录
	public static final String SDCARD_PATH_TEMP = SDCARD_PATH_ROOT + "/temp/"; // 临时目录

}
