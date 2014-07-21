package com.hengtong.library.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 功能：数据共享
 * @ahthor：黄荣星
 * @date:2013-11-13
 * @version::V1.0
 */
public class AppShareData {
	private SharedPreferences mSharedPreferences;
	private String mShareName = "hengtongsj_data";

	public void setmShareName(String mShareName) {
		this.mShareName = mShareName;
	}

	public AppShareData(Application aApplication) {
		if (aApplication != null) {
			mSharedPreferences = aApplication.getSharedPreferences(mShareName, Application.MODE_PRIVATE);
		}
	}

	public String getString(String key, String defaultValue) {
		if (mSharedPreferences == null) {
			return null;
		}
		return mSharedPreferences.getString(key, defaultValue);
	}

	public String getString(String key) {
		if (mSharedPreferences == null) {
			return null;
		}
		return mSharedPreferences.getString(key, "");
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (mSharedPreferences == null) {
			return false;
		}
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		if (mSharedPreferences == null) {
			return false;
		}
		return mSharedPreferences.getBoolean(key, false);
	}

	public float getFloat(String key, float defualtValue) {
		if (mSharedPreferences == null) {
			return 0;
		}
		return mSharedPreferences.getFloat(key, defualtValue);
	}

	public float getFloat(String key) {
		if (mSharedPreferences == null) {
			return 0;
		}
		return mSharedPreferences.getFloat(key, 0.0f);
	}

	public int getInt(String key, int defualtValue) {
		if (mSharedPreferences == null) {
			return 0;
		}
		return mSharedPreferences.getInt(key, defualtValue);
	}

	public int getInt(String key) {
		if (mSharedPreferences == null) {
			return 0;
		}
		return mSharedPreferences.getInt(key, 0);
	}

	public long getLong(String key, long defualtValue) {
		if (mSharedPreferences == null) {
			return 0;
		}
		return mSharedPreferences.getLong(key, defualtValue);
	}

	public long getLong(String key) {
		if (mSharedPreferences == null) {
			return 0;
		}
		return mSharedPreferences.getLong(key, 0);
	}

	public synchronized void setData(String key, Object o) {
		if (o == null) {
			return;
		}
		LogControl.d("AppShareData", "存放数据 : key=" + key + " value=" + o.toString());
		Editor mEditor = mSharedPreferences.edit();
		if (o instanceof String) {
			mEditor.putString(key, o.toString());
		} else if (o instanceof Integer) {
			mEditor.putInt(key, (Integer) o);
		} else if (o instanceof Float) {
			mEditor.putFloat(key, (Float) o);
		} else if (o instanceof Long) {
			mEditor.putLong(key, (Long) o);
		} else if (o instanceof Boolean) {
			mEditor.putBoolean(key, (Boolean) o);
		}
		mEditor.commit();
	}

}
