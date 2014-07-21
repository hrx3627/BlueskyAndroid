package com.hengtong.library.utils.struct;

public class SimInfo 
{
	private int _state;
	private String _desc;
	private String _key;		//SIM卡的序列
	
	public int get_state() {
		return _state;
	}
	public void set_state(int _state) {
		this._state = _state;
	}
	public String get_desc() {
		return _desc;
	}
	public void set_desc(String _desc) {
		this._desc = _desc;
	}
	public String get_key() {
		return _key;
	}
	public void set_key(String _key) 
	{
		this._key = _key;
	}
	
}
