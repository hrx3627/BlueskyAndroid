package com.hengtong.library;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hengtong.library.utils.AppManager;
import com.hengtong.library.utils.StringUtils;

/**
 * 功能：Activity封装的基类
 * @ahthor：黄荣星
 * @date:2013-11-11
 * @version::V1.0
 */
public abstract class HTBaseActivity extends Activity implements HTContant {

	public enum OpResult {
		FAIL, SUCCESS
	}

	// 等待操作的对话框
	private LoadingDialog mLoadingDialog;

	protected Context mContext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Activity管理
		AppManager.getAppManager().addActivity(this);
		// 统一背景风格
		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.sys_bg));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 拨打电话
	 * @param callNumber
	 */
	protected void callOut(Context context, String callNumber) {
		try {
			context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + StringUtils.str2int(callNumber.trim()))));
		} catch (Exception e) {
			context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringUtils.str2int(callNumber.trim()))));
		}
	}

	/**
	 * 弹出Toast提示
	 * @param aText
	 */
	protected void showToast(String aText) {
		Toast.makeText(this, aText, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int aResId) {
		Toast.makeText(this, aResId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示加载对话框
	 */
	public void showLoadingDialog(String mTip) {
		try {
			if (mLoadingDialog != null) {
				mLoadingDialog.dismiss();
				mLoadingDialog = null;
			}
			mLoadingDialog = new LoadingDialog(this);
			mLoadingDialog.setMessage(mTip);
			mLoadingDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示加载对话框
	 */
	public void showLoadingDialog(Context context, String mTip) {
		try {
			if (mLoadingDialog != null) {
				mLoadingDialog.dismiss();
				mLoadingDialog = null;
			}
			mLoadingDialog = new LoadingDialog(context);
			mLoadingDialog.setMessage(mTip);
			mLoadingDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 消除加载对话框
	 */
	protected void dismissLoadingDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing())
			mLoadingDialog.dismiss();
	}

	/**
	 * 消除加载对话框
	 */
	protected void dismissLoadingDialog(String mTip) {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.setMessage(mTip);
			mLoadingDialog.dismiss();
		}
	}

	/**
	 * 消除加载对话框
	 */
	protected void dismissLoadingDialog(String mTip, OpResult aOpResult, boolean aDelay) {
		try {
			if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
				mLoadingDialog.setMessage(mTip);
				switch (aOpResult) {
				case FAIL:
					mLoadingDialog.setSuccess(false);
					break;
				case SUCCESS:
					mLoadingDialog.setSuccess(true);
					break;
				}
				if (!aDelay) {
					mLoadingDialog.dismiss();
				} else {
					mLoadingDialog.setCancelable(false);
					startTimer();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =======================下面是定时器Timer=========================
	private Timer mTimer;
	private TimerTask mTimerTask;

	private void startTimer() {
		releaseTimer();
		mTimer = new Timer();
		mTimer.schedule(getTimerTask(), HTContant.DIG_DISMISS_DELAY);
	}

	private TimerTask getTimerTask() {

		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (mLoadingDialog != null)
					mLoadingDialog.dismiss();
				releaseTimer();
			}
		};
		return mTimerTask;
	}

	private void releaseTimer() {
		try {
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			if (mTimerTask != null) {
				mTimerTask.cancel();
				mTimerTask = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义等待操作框
	 * @author 黄荣星
	 */
	private class LoadingDialog {
		private Context mContext;
		private Dialog mDialog;
		private ProgressBar mProgressBar;
		private TextView mTextView;
		private ImageView mImageView;

		public LoadingDialog(Context aContext) {
			mContext = aContext;
			init();
		}

		private void init() {
			mDialog = new Dialog(mContext, R.style.no_frame_dialog);
			mDialog.setContentView(R.layout.sys_dlg_loading);
			mProgressBar = (ProgressBar) mDialog.findViewById(R.id.dlg_loading_progress);
			mTextView = (TextView) mDialog.findViewById(R.id.dlg_loading_tv);
			mImageView = (ImageView) mDialog.findViewById(R.id.dlg_loading_img);
			mDialog.setCanceledOnTouchOutside(false);
		}

		public void setSuccess(boolean aSuccess) {
			if (aSuccess) {
				mImageView.setImageResource(R.drawable.sys_success);
			} else {
				mImageView.setImageResource(R.drawable.sys_fail);
			}
			mProgressBar.setVisibility(View.INVISIBLE);
			mImageView.setVisibility(View.VISIBLE);
		}

		public boolean isShowing() {
			if (mDialog != null) {
				return mDialog.isShowing();
			}
			return false;
		}

		public void setMessage(String aText) {
			mTextView.setText(aText);
		}

		public void show() {
			if (mDialog != null && !mDialog.isShowing()) {
				mDialog.show();
			}
		}

		public void dismiss() {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}

		public void release() {
			dismiss();
			mImageView = null;
			mTextView = null;
			mProgressBar = null;
			mDialog = null;
		}

		public void setCancelable(boolean aCancelable) {
			mDialog.setCancelable(aCancelable);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			if (mLoadingDialog != null) {
				mLoadingDialog.release();
			}
			releaseTimer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	/**
	 * 启动Activity
	 * @return void
	 */
	protected void startActivity(Class<?> cls) {
		this.startActivity(cls, null, false);
	}

	protected void startActivity(Class<?> cls, Bundle mBundle) {
		this.startActivity(cls, mBundle, false);
	}

	protected void startActivity(Class<?> cls, Bundle mBundle, boolean isFinish) {
		Intent mIntent = new Intent(HTBaseActivity.this, cls);
		if (null != mBundle) {
			mIntent.putExtras(mBundle);
		}
		startActivity(mIntent);
		if (isFinish) {
			finish();
		}
	}

	/**
	 * method desc：处理错误
	 */
	protected void handlerError(int aResult) {
		switch (aResult) {
		case HTContant.RET_NET_ERROR:
			dismissLoadingDialog("网络太差，操作失败！", OpResult.FAIL, true);
			break;
		case HTContant.RET_FAIL:
			dismissLoadingDialog("操作失败！", OpResult.FAIL, true);
			break;
		default:
			dismissLoadingDialog("操作失败！", OpResult.FAIL, true);
			break;
		}
	}

}
