package com.hengtong.library.manager;

import org.json.JSONException;
import org.json.JSONObject;

import com.hengtong.library.HTContant;
import com.hengtong.library.enty.HTRequestObject;
import com.hengtong.library.http.HttpUtils;

/**
 * 功能：网络请求管理基础类
 * @ahthor：黄荣星
 * @date:2013-12-16
 * @version::V1.0
 */
public class HTBaseManager implements HTContant {

	/**
	 * method desc：异常标识处理
	 * @param aErrorCode
	 * @return
	 */
	protected static final int getCorrectErrorCode(String aErrorCode) {
		int temp = RET_FAIL;
		if (HttpUtils.NET_ERROR.equals(aErrorCode)) {
			temp = RET_NET_ERROR;
		} else if (HttpUtils.NET_ERROR_RESULT_NULL.equals(aErrorCode)) {
			temp = RET_NET_ERROR_NULL;
		} else {
			temp = RET_FAIL;
		}
		return temp;
	}

	/**
	 * method desc：请求的公共参数
	 * @param aRequestObject
	 * @return
	 * @throws Exception
	 */
	protected static JSONObject commentReqeustObj(HTRequestObject aRequestObject) throws JSONException {
		JSONObject requst = new JSONObject();
		requst.put("Method", aRequestObject.getmMethod());
		requst.put("TypeName", aRequestObject.getmTypeName());
		requst.put("Token", aRequestObject.getmToken());
		requst.put("Format", "json");
		return requst;
	}

}
