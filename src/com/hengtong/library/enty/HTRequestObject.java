package com.hengtong.library.enty;

import java.util.HashMap;

/**
 * 功能：请求对象
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class HTRequestObject {
	protected String mToken;
	protected String mTypeName;// 类型
	protected String mMethod;// 方法
	protected HashMap<Object, Object> mPamsMap;
	protected String mForamt = "json";// 返回数据格式,默认json
	public static String mFlageMethod;

	public String getmToken() {
		return mToken;
	}

	public void setmToken(String mToken) {
		this.mToken = mToken;
	}

	public String getmTypeName() {
		return mTypeName;
	}

	public void setmTypeName(String mTypeName) {
		this.mTypeName = mTypeName;
	}

	public String getmMethod() {
		return mMethod;
	}

	public void setmMethod(String mMethod) {
		this.mMethod = mMethod;
	}

	// 设置参数
	public void setmParms(Object key, Object value) {
		if (mPamsMap == null)
			mPamsMap = new HashMap<Object, Object>();
		mPamsMap.put(key, value);
	}

	public HashMap<Object, Object> getmPamsMap() {
		return mPamsMap;
	}

	public void setmPamsMap(HashMap<Object, Object> aParms) {
		mPamsMap = aParms;
	}

	public String getmForamt() {
		return mForamt;
	}

	public void setmForamt(String mForamt) {
		this.mForamt = mForamt;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("{RequestObject:");
		sb.append(" mToken=" + this.mToken + "\n");
		sb.append(" mTypeName=" + this.mTypeName + "\n");
		sb.append(" mMethod=" + this.mMethod + "\n");
		sb.append(" mPamsMap=" + this.mPamsMap + "\n");
		sb.append(" mForamt=" + this.mForamt + "\n");
		sb.append("}");
		return sb.toString();
	}

}
