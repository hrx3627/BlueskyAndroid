package com.hengtong.library.manager;

/**
 * 功能：处理返回数据接口
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public interface HTRequestCallBack {
	/**
	 * method desc：http请求返回数据处理
	 * @param aResult 返回标志
	 * @param aResponseObject 返回数据
	 */
	public void handler(int aResult, Object aResponseObject);

}
