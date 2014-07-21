package com.hengtong.library.utils;

import android.os.Handler;

/**
 * 功能：定时操作类
 * @ahthor：黄荣星
 * @date:2014年7月9日
 * @version::V1.0
 */
public class TimeHelp {
	private static int mDelayTime;
	private static ITimeHelp mTimeHelpListener;
	private static Handler mHandler = new Handler();
	private static Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (mTimeHelpListener != null)
				mTimeHelpListener.handlerTime();
			mHandler.postDelayed(mRunnable, mDelayTime);
		}
	};

	public void setmDelayTime(int mDelayTime) {
		TimeHelp.mDelayTime = mDelayTime;
	}

	public void setmTimeHelpListener(ITimeHelp mTimeHelpListener) {
		TimeHelp.mTimeHelpListener = mTimeHelpListener;
	}

	public void start() {
		mHandler.post(mRunnable);
	}

	public void stop() {
		if (mHandler != null)
			mHandler.removeCallbacks(mRunnable);
	}

	public interface ITimeHelp {
		public void handlerTime();
	}
}
