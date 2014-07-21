package com.hengtong.library.manager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.hengtong.library.async.AsyncHttpClient;
import com.hengtong.library.async.AsyncHttpResponseHandler;
import com.hengtong.library.async.RequestParams;
import com.hengtong.library.enty.HTRequestObject;
import com.hengtong.library.utils.LogControl;

/**
 * 功能：开源网络框架封装类
 * @ahthor：黄荣星
 * @date:2014-1-7
 * @version::V1.0
 */
public class HTAsyncNetManager extends NetManager {
	private final static String TAG = HTAsyncNetManager.class.getSimpleName();

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	// public static void post(HTRequestObject aRequestObject, AsyncHttpResponseHandler responseHandler) {
	// post(API_URL_JSON_NETWORK, aRequestObject, null, "UTF-8", responseHandler);
	// }

	public static void post(String url, HTRequestObject aRequestObject, AsyncHttpResponseHandler responseHandler) {
		post(url, aRequestObject, null, "UTF-8", responseHandler);
	}

	public static void post(String url, HTRequestObject aRequestObject, String charset, AsyncHttpResponseHandler responseHandler) {
		post(url, aRequestObject, null, charset, responseHandler);
	}

	public static void post(Context aContext, String url, HTRequestObject aRequestObject, AsyncHttpResponseHandler responseHandler) {
		post(url, aRequestObject, aContext, "UTF-8", responseHandler);
	}

	public static void post(Context aContext, String url, HTRequestObject aRequestObject, String charset, AsyncHttpResponseHandler responseHandler) {
		post(url, aRequestObject, aContext, charset, responseHandler);
	}

	public static void post(Context context, String url, HTRequestObject aRequestObject, Map<String, String> headers, AsyncHttpResponseHandler responseHandler) {
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				client.addHeader(entry.getKey(), entry.getValue());
			}
		}
		post(url, aRequestObject, context, "UTF-8", responseHandler);
	}

	public static void post(String url, HTRequestObject aRequestObject, Context aContext, String charset, AsyncHttpResponseHandler responseHandler) {
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
			} else {
				LogControl.e(TAG, "the request is null");
			}
			requst.put("Parms", parmas);
			// LogControl.e("url", "url:" + url + " method:" + aRequestObject.getmMethod() + " params:" + requst.toString());
			StringEntity entity = new StringEntity(requst.toString(), charset);
			client.post(aContext, url, entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LogControl.e(TAG, "create the paramas is UnsupportedEncodingException!!!");
		} catch (JSONException e) {
			e.printStackTrace();
			LogControl.e(TAG, "create the paramas is json exception!!!");
		}
	}

}
