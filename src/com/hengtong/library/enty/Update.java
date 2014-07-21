package com.hengtong.library.enty;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 应用程序更新实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable {

	/**
	 * serialVersionUID：
	 */
	private static final long serialVersionUID = 1L;
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "oschina";

	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	public String info;// 版本描述
	public double Size;// 版本大小
	public  boolean mForce;  //是否强制更新     true   强制      false   非强制

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public Update(String jsons) {
		try {
			JSONObject jsonObject = new JSONObject(jsons);
			this.downloadUrl = jsonObject.optString("Url", "");
			this.versionName = jsonObject.optString("Version", "");
			this.versionCode = jsonObject.optInt("BuildNo", 0);
			this.info = jsonObject.optString("Info", "");
			this.Size = jsonObject.optDouble("Size", 0);
			int temp = jsonObject.optInt("Forced", 0);
			this.mForce = (temp == 1 ? true : false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
