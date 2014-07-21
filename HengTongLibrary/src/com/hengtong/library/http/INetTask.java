package com.hengtong.library.http;

/**
 * 功能：网络操作任务接口类
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public interface INetTask {
	/**
	 * 当任务完成
	 * @param content
	 */
	public void onEnd(String aContent);
}
