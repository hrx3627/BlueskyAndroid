package com.hengtong.library.manager;

import java.util.concurrent.TimeUnit;

import com.hengtong.library.async.RequestParams;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * 功能：OKHttp 网络管理类
 * @ahthor：黄荣星
 * @date:2015-6-25
 * @version::V1.0
 */
public class HTOkHttpNetManager {
	private static OkHttpClient mClient=new OkHttpClient();
	static{
		mClient.setConnectTimeout(10, TimeUnit.SECONDS);
		mClient.setWriteTimeout(10, TimeUnit.SECONDS);
		mClient.setReadTimeout(30, TimeUnit.SECONDS);
	}
	
	public static void post(Request request,Callback responseCallback) {
		mClient.newCall(request).enqueue(responseCallback);
	}
	public static void get(Request request,Callback responseCallback) {
		mClient.newCall(request).enqueue(responseCallback);
	}
	

}
