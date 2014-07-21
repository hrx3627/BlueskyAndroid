package com.hengtong.library.http;

/**
 * 功能：http请求头部
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class HttpHeader {
	private String key, value;

	public HttpHeader(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "HttpNetTaskHeader [key=" + key + ", value=" + value + "]";
	}

}
