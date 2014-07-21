package com.hengtong.library.utils.struct;

public class StorageInfo 
{
	private boolean _haveSD;		//是否有SD卡
	private String _SDPath;			//SD卡路径
	private long _SDBlockSize;		//
	private long _SDBlockTotal;		//总数
	private long _SDBlockIdle;		//空闲
	
	private long _sysBlockSize;
	private long _sysBlockTotal;
	private long _sysBlockIdle;
	private String _currentPath;	//当前路径

	public boolean is_haveSD() {
		return _haveSD;
	}

	public void set_haveSD(boolean _haveSD) {
		this._haveSD = _haveSD;
	}

	public String get_SDPath() {
		return _SDPath;
	}

	public void set_SDPath(String _SDPath) {
		this._SDPath = _SDPath;
	}

	public long get_SDBlockSize() {
		return _SDBlockSize;
	}

	public long get_SDBlockTotal() {
		return _SDBlockTotal;
	}

	public long get_SDBlockIdle() {
		return _SDBlockIdle;
	}

	public void set_SDBlockSize(long _SDBlockSize) {
		this._SDBlockSize = _SDBlockSize;
	}

	public void set_SDBlockTotal(long _SDBlockTotal) {
		this._SDBlockTotal = _SDBlockTotal;
	}

	public void set_SDBlockIdle(long _SDBlockIdle) {
		this._SDBlockIdle = _SDBlockIdle;
	}

	public long get_SysBlockSize() {
		return _sysBlockSize;
	}

	public long get_SysBlockTotal() {
		return _sysBlockTotal;
	}

	public long get_SysBlockIdle() {
		return _sysBlockIdle;
	}
	
	public String get_currentPath() {
		return _currentPath;
	}

	public void set_currentPath(String _currentPath) {
		this._currentPath = _currentPath;
	}

	public void set_SysBlockSize(long _SysBlockSize) {
		this._sysBlockSize = _SysBlockSize;
	}

	public void set_SysBlockTotal(long _SysBlockTotal) {
		this._sysBlockTotal = _SysBlockTotal;
	}

	public void set_SysBlockIdle(long _SysBlockIdle) {
		this._sysBlockIdle = _SysBlockIdle;
	}
	
	public String getSDCardIdlySize()
	{
		if(_haveSD)
		{
			long byteSize = _SDBlockSize * _SDBlockIdle;
			return sizeToString(byteSize);
		}
		else
		{
			return "无SD卡";
		}
	}
	
	public String getSysIdlysize()
	{
		long byteSize = _sysBlockSize * _sysBlockIdle;
		return sizeToString(byteSize);
	}
	
	private String sizeToString(long byteSize)
	{
		int unit = 0;
		while(byteSize > 1024)
		{
			unit++;
			byteSize = byteSize/1024;
			if(unit == 4)
				break;
		}
		switch(unit)
		{
		case 0:
			return byteSize + "字节";
		case 1:
			return byteSize + "KB";
		case 2:
			return byteSize + "MB";
		case 3:
			return byteSize + "GB";
		case 4:
			return byteSize + "TB";
		default:
			return "获取错误";
		}
	}
}
