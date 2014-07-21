package com.hengtong.library.manager;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.hengtong.library.HTContant;
import com.hengtong.library.enty.HTRequestObject;
import com.hengtong.library.enty.HTResponseObject;
import com.hengtong.library.http.HttpNetCenter;
import com.hengtong.library.http.HttpNetTask;
import com.hengtong.library.http.HttpUtils;
import com.hengtong.library.http.INetTask;
import com.hengtong.library.utils.LogControl;
import com.hengtong.library.utils.StringUtils;

/**
 * 功能：客户端网络管理类
 * @ahthor：黄荣星
 * @date:2013-11-11
 * @version::V1.0
 */
public class NetManager extends HTBaseManager {
	private static final String TAG = "NetManager";

	/**
	 * method desc：网络post请求
	 * @param aRequestObject 请求对象
	 * @param aRequestCallBack 回调处理函数
	 */
	public static void doHttpRequestPost(String url, final HTRequestObject aRequestObject, final HTRequestCallBack aRequestCallBack) {
		final Handler mHandler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				int aFlage = 0; // 请求的标志
				switch (msg.what) {
				case RET_SUCCESS: // 请求成功
					aFlage = RET_SUCCESS;
					break;
				case RET_FAIL: // 请求失败（例如：参数json构造错误）
					aFlage = RET_FAIL;
					break;
				case RET_NET_ERROR: // 网络原因
					aFlage = RET_NET_ERROR;
					break;
				}
				if (aRequestCallBack != null) {
					aRequestCallBack.handler(aFlage, msg.obj);
				}
				return false;
			}
		});
		try {
			// 公共参数
			JSONObject requst = commentReqeustObj(aRequestObject);
			// 参数构造
			HashMap<Object, Object> map = aRequestObject.getmPamsMap();
			JSONObject parmas = new JSONObject();
			if (map != null && map.size() > 0) {
				for (Entry<Object, Object> entry : map.entrySet()) {
					parmas.put(entry.getKey().toString(), entry.getValue());
				}
			}
			requst.put("Parms", parmas);
			LogControl.e(TAG, "去包 = " + url + " parms:" + requst.toString());
			HttpNetTask lHttpNetTask = new HttpNetTask(url.trim(), requst, new INetTask() {
				@Override
				public void onEnd(String aContent) {
					try {
						LogControl.e(TAG, "回包 = " + aContent);
						JSONObject jsonObject = new JSONObject(aContent);
						HTResponseObject mResponseObj = new HTResponseObject();
						mResponseObj.mRequestType = HTResponseObject.WEBSERVER_REQUEST;
						mResponseObj.setmError(handlerError(jsonObject.optString("Error", "")));
						mResponseObj.setmErrorCode(handlerErrorCode(jsonObject.optString("Error", "")));
						mResponseObj.setmSuccess(jsonObject.optBoolean("Success"));
						mResponseObj.setmResult(jsonObject.optString("Result", ""));
						mResponseObj.setmToken(aRequestObject.getmToken());
						mResponseObj.setmTypeName(aRequestObject.getmTypeName());
						mResponseObj.setmForamt(aRequestObject.getmForamt());
						mResponseObj.setmMethod(aRequestObject.getmMethod());
						mResponseObj.setmPamsMap(aRequestObject.getmPamsMap());

						sendMessage(RET_SUCCESS, mResponseObj, mHandler);
					} catch (JSONException e) {
						e.printStackTrace();
						sendMessage(getCorrectErrorCode(aContent), null, mHandler);
					}
				}
			});
			HttpNetCenter.getInstance().addTaskAndRun(lHttpNetTask);
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(RET_FAIL, null, mHandler);
		}
	}

	// 发消息
	private static void sendMessage(int result, Object aRespontObj, Handler aHandler) {
		Message msg = new Message();
		msg.what = result;
		msg.obj = aRespontObj;
		aHandler.sendMessage(msg);
	}

	/**
	 * method desc：处理错误信息
	 * @return
	 */
	private static String handlerError(String aError) {
		if (!StringUtils.isNullOrEmpty(aError)) {
			int start = aError.indexOf(":");
			aError = aError.substring(start + 1);
		} else {
			aError = "操作失败";
		}
		return aError;
	}

	/**
	 * method desc：处理错误信息编码
	 * @return
	 */
	private static String handlerErrorCode(String aError) {
		if (!StringUtils.isNullOrEmpty(aError)) {
			int start = aError.indexOf("#");
			int end = aError.indexOf(":");
			aError = aError.substring(start + 1, end);
		} else {
			aError = "0000";
		}
		return aError;
	}

	/**
	 * 网银支付接口 method desc：网络post请求
	 * @param aRequestObject 请求对象
	 * @param aRequestCallBack 回调处理函数
	 */
	public static void doHttpRequestPay(String url, final HTRequestObject aRequestObject, final HTRequestCallBack aRequestCallBack) {
		final Handler mHandler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				LogControl.e("url", "mesg:" + msg.what);
				int aFlage = 0; // 请求的标志
				switch (msg.what) {
				case RET_SUCCESS: // 请求成功
					aFlage = RET_SUCCESS;
					break;
				case RET_FAIL: // 请求失败（例如：参数json构造错误）
					aFlage = RET_FAIL;
					break;
				case RET_NET_ERROR: // 网络原因
					aFlage = RET_NET_ERROR;
					break;
				}
				if (aRequestCallBack != null) {
					aRequestCallBack.handler(aFlage, msg.obj);
				}
				return false;
			}
		});

		try {
			String requst = "";
			// 参数构造
			HashMap<Object, Object> map = aRequestObject.getmPamsMap();
			if (map.containsKey("content")) {
				requst = map.get("content").toString();
			}
			if (StringUtils.isNullOrEmpty(requst)) {
				sendMessage(RET_FAIL, null, mHandler);
				return;
			}
			LogControl.i(TAG, "去包 = " + url + " parms:" + requst.toString());

			HttpNetTask lHttpNetTask = new HttpNetTask(url.trim(), requst, new INetTask() {
				@Override
				public void onEnd(String aContent) {
					try {
						LogControl.i(TAG, "回包 = " + aContent);
						HTResponseObject mResponseObj = new HTResponseObject();
						mResponseObj.mRequestType = HTResponseObject.PAY_REQUEST;

						if (!StringUtils.isNullOrEmpty(aContent)) {
							if (HttpUtils.NET_ERROR.equals(aContent)) {
								mResponseObj.setmSuccess(false);
								int flage = getCorrectErrorCode(aContent);
								switch (flage) {
								case HTContant.RET_NET_ERROR:
									mResponseObj.setmError("网络太差，操作失败！");
									break;
								default:
									mResponseObj.setmError("操作失败！");
									break;
								}
							} else {
								mResponseObj.setmSuccess(true);
								mResponseObj.setmResult(aContent);
							}
						} else {
							mResponseObj.setmSuccess(false);
							mResponseObj.setmError("启动支付插件失败！请重新确认支付！");
						}
						sendMessage(RET_SUCCESS, mResponseObj, mHandler);
					} catch (Exception e) {
						e.printStackTrace();
						sendMessage(getCorrectErrorCode(aContent), null, mHandler);
					}
				}
			});
			lHttpNetTask.addHeader("Content-Type", "application/x-www-form-urlencoded");
			HttpNetCenter.getInstance().addTaskAndRun(lHttpNetTask);
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(RET_FAIL, null, mHandler);
		}
	}

}
