package com.hengtong.library.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.hengtong.library.enty.HTResponseObject;
import com.hengtong.library.utils.StringUtils;

/**
 * 功能：解析返回接口
 * @ahthor：黄荣星
 * @date:2014-1-9
 * @version::V1.0
 */
public class ParseHandler {

	private static ParseHandler parseHandler;

	private ParseHandler() {

	}

	public ParseHandler getInstance(Context aContext) {
		if (parseHandler == null)
			parseHandler = new ParseHandler();
		return parseHandler;
	}

	/**
	 * method desc：处理错误信息编码
	 * @return
	 */
	private static String handlerErrorCode(String aError) {
		if (!StringUtils.isNullOrEmpty(aError)) {
			if (aError.contains("请求对象无法序列化")) {
				return aError;
			}
			int start = aError.indexOf("#");
			int end = aError.indexOf(":");
			aError = aError.substring(start + 1, end);
		}
		return aError;
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
	 * method desc：解析json字符串
	 * @param json json字符串
	 * @return
	 */
	public static HTResponseObject parseJson(String json) throws JSONException {
		HTResponseObject mResponseObj = new HTResponseObject();
		if (!StringUtils.isNullOrEmpty(json)) {
			JSONObject jsonObject = new JSONObject(json);
			mResponseObj.setmError(handlerError(jsonObject.optString("Error", "")));
			mResponseObj.setmErrorCode(handlerErrorCode(jsonObject.optString("Error", "")));
			mResponseObj.setmSuccess(jsonObject.optBoolean("Success"));
			mResponseObj.setmResult(jsonObject.optString("Result", ""));
		}
		return mResponseObj;
	}

	/**
	 * method desc：解析json字符串
	 * @param json json字符串
	 * @return
	 */
	public static HTResponseObject parseJson(Context aContext, String json) {
		HTResponseObject mResponseObj = new HTResponseObject();
		try {
			if (!StringUtils.isNullOrEmpty(json)) {
				JSONObject jsonObject = new JSONObject(json);
				mResponseObj.setmError(handlerError(jsonObject.optString("Error", "")));
				mResponseObj.setmErrorCode(handlerErrorCode(jsonObject.optString("Error", "")));
				mResponseObj.setmSuccess(jsonObject.optBoolean("Success"));
				mResponseObj.setmResult(jsonObject.optString("Result", ""));
			} else {
				Toast.makeText(aContext, "服务器返回的数据为空", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			Toast.makeText(aContext, "服务器返回json字符串解析有误", Toast.LENGTH_LONG).show();
		}
		return mResponseObj;
	}

}
