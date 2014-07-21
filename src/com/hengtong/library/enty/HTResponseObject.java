package com.hengtong.library.enty;

/**
 * 功能：返回对象
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class HTResponseObject extends HTRequestObject {

	public static final int WEBSERVER_REQUEST = 1;
	public static final int PAY_REQUEST = 2;
	public static final int DOWNLOAD_REQUEST = 3;

	// 返回结果对象,Success=true时赋值此属性
	public Object mResult;
	// 错误信息,Success=false时赋值此属性值
	public String mError;
	// 错我代码
	public String mErrorCode;
	// 是否成功,成功为true,失败为false
	public boolean mSuccess;
	// 数据的请求类型如: 1 接口请求 2:网银请求 3:下载请求
	public int mRequestType;

	public Object getmResult() {
		return mResult;
	}

	public void setmResult(Object mResult) {
		this.mResult = mResult;
	}

	public String getmError() {
		return mError;
	}

	public void setmError(String mError) {
		this.mError = mError;
	}

	public boolean ismSuccess() {
		return mSuccess;
	}

	public void setmSuccess(boolean mSuccess) {
		this.mSuccess = mSuccess;
	}

	public String getmErrorCode() {
		return mErrorCode;
	}

	public void setmErrorCode(String mErrorCode) {
		this.mErrorCode = mErrorCode;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		super.toString();
		StringBuffer sb = new StringBuffer("{HTResponseObject:\n");
		sb.append(" mResult=" + this.mResult + "\n");
		sb.append(" mError=" + this.mError + "\n");
		sb.append(" mSuccess=" + this.mSuccess + "\n");
		sb.append("HTRequestObject[\n");
		sb.append(" mToken=" + this.mToken + "\n");
		sb.append(" mTypeName=" + this.mTypeName + "\n");
		sb.append(" mMethod=" + this.mMethod + "\n");
		sb.append(" mPamsMap=" + this.mPamsMap + "\n");
		sb.append(" mForamt=" + this.mForamt + "\n]");
		sb.append("}");
		return sb.toString();
	}

}
