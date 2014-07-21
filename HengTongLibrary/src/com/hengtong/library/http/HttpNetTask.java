package com.hengtong.library.http;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：网络操作任务类
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class HttpNetTask {
	private String url = "";
	private INetTask callBack;
	private RequestType requestType = RequestType.POST; // 默认post方式
	private Object postParams; // HttpUtils类中Post方法需要的参数对象
	private String filePath;
	private List<HttpHeader> headers;
	private boolean forceToInvokeInterface = false;

	public HttpNetTask() {
	}

	/**
	 * 默认GET方式
	 */
	public HttpNetTask(String url, INetTask callBack) {
		this.url = url;
		this.requestType = RequestType.GET; // 默认get方式
		this.callBack = callBack;
	}

	/**
	 * 默认POST方式
	 */
	public HttpNetTask(String url, Object postParams, INetTask callBack) {
		this.url = url;
		this.requestType = RequestType.POST; // 默认post方式
		this.postParams = postParams;
		this.callBack = callBack;
	}

	public INetTask getCallBack() {
		return callBack;
	}

	public void setCallBack(INetTask callBack) {
		this.callBack = callBack;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public void addHeader(String key, String value) {
		if (headers == null) {
			headers = new ArrayList<HttpHeader>();
		}
		headers.add(new HttpHeader(key, value));
	}

	public List<HttpHeader> getHeaders() {
		return headers;
	}

	

	public Object getPostParams() {
		return postParams;
	}

	public void setPostParams(Object postParams) {
		this.postParams = postParams;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isForceToInvokeInterface() {
		return forceToInvokeInterface;
	}

	public void setForceToInvokeInterface(boolean forceToInvokeInterface) {
		this.forceToInvokeInterface = forceToInvokeInterface;
	}

}
