package com.hengtong.library.enty;

import java.io.Serializable;

public class HTVersionItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public  int mVersionCode;
	public  String mVersionName;
	public  String mAppDownUrl;
	public  boolean mForce;  //是否强制更新     true   强制      false   非强制
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "{VERSION_ITEM:" );
		builder.append(" mVersionName="+ this.mVersionName );
		builder.append(" mVersionCode="+ this.mVersionCode );
		builder.append(" mForce="+ this.mForce );
		builder.append(" mAppDownUrl="+ this.mAppDownUrl );
		builder.append("}");
		return builder.toString();
	}
}
