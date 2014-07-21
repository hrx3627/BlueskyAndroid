package com.hengtong.library.utils.struct;

public class SysVersionInfo 
{
	private String _phoneType;			//机型
	private int _androidSDK;			//安卓版本号
	private String _androidRelease;		//系统版本编号
	
	private int _appCode;				//应用程序编号
	private String _appVersion;			//应用程序版本号
	private String _appPackName;		//应用程序包名
	
	private int _updateCode;			//更新的程序编号
	private String _updateVersion;		//获取的更新版本
	private String _updateUrl;			//地址
	private String _updateInfo;			//信息
	private boolean _updateForce;		//是否强制更新
	private double _updateSize;		//更新的包大小M为单位
	
	public String get_phoneType() {
		return _phoneType;
	}
	public String get_androidVer() 
	{
		switch(_androidSDK)
		{
		case 1:
			return "android1.0";
		case 2:
			return "android1.1";
		case 3:
			return "android1.5";
		case 4:
			return "android1.6";
		case 5:
			return "android2.0";
		case 6:
			return "android2.0.1";
		case 7:
			return "android2.1";
		case 8:
			return "android2.2";
		case 9:
			return "android2.3";
		case 10:
			return "android2.3.3";
		case 11:
			return "android3.0";
		case 12:
			return "android3.1";
		case 13:
			return "android3.2";
		case 14:
			return "android4.0";
		case 15:
			return "android4.0.3";
		case 16:
			return "android4.1.2";
		case 17:
			return "android4.2.2";
		case 18:
			return "android4.3";
		case 19:
			return "android4.4";
		default:
			return "unkwon sys";
		}
	}
	public int get_androidSDK() {
		return _androidSDK;
	}
	public String get_androidRelease() {
		return _androidRelease;
	}
	public int get_appCode() {
		return _appCode;
	}
	public String get_appVersion() {
		return _appVersion;
	}
	public String get_appPackName() {
		return _appPackName;
	}
	public void set_phoneType(String _phoneType) {
		this._phoneType = _phoneType;
	}
	public void set_androidSDK(int _androidSDK) {
		this._androidSDK = _androidSDK;
	}
	public void set_androidRelease(String _androidRelease) {
		this._androidRelease = _androidRelease;
	}
	public void set_appCode(int _appCode) {
		this._appCode = _appCode;
	}
	public void set_appVersion(String _appVersion) {
		this._appVersion = _appVersion;
	}
	public void set_appPackName(String _appPackeName) {
		this._appPackName = _appPackeName;
	}
	public String get_updateVersion() {
		return _updateVersion;
	}
	public String get_updateUrl() {
		return _updateUrl;
	}
	public String get_updateInfo() {
		return _updateInfo;
	}
	public boolean is_updateForce() {
		return _updateForce;
	}
	public void set_updateVersion(String _updateVersion) {
		this._updateVersion = _updateVersion;
	}
	public void set_updateUrl(String _updateUrl) {
		this._updateUrl = _updateUrl;
	}
	public void set_updateInfo(String _updateInfo) {
		this._updateInfo = _updateInfo;
	}
	public void set_updateForce(boolean _updateForce) {
		this._updateForce = _updateForce;
	}
	
	public int get_updateCode() {
		return _updateCode;
	}
	public void set_updateCode(int _updateCode) {
		this._updateCode = _updateCode;
	}
	public boolean needUPdate()
	{
		if(_updateCode > _appCode)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public double get_updateSize() {
		return _updateSize;
	}
	public void set_updateSize(double _updateSize) {
		this._updateSize = _updateSize;
	}
	
}
