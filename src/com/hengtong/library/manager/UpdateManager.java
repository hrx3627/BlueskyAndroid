package com.hengtong.library.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.hengtong.library.HTBaseApplication;
import com.hengtong.library.HTContant;
import com.hengtong.library.R;
import com.hengtong.library.utils.HTSDTool;
import com.hengtong.library.utils.LogControl;

/**
 * 功能：升级管理类
 * @ahthor：黄荣星
 * @date:2013-12-16
 * @version::V1.0
 */

@SuppressLint("HandlerLeak")
public class UpdateManager {
	private final String prompt_update_title = "软件更新";
	private final String prompt_update_msg_no_update = "当前版本: {verName},已是最新版,无需更新!";
	private final String prompt_update_btn_ok = "确定";
	private final String prompt_update_btn_cannel = "退出";
	private final String prompt_update_btn_update = "更新";
	private final String prompt_update_btn_update_no = "暂不更新";
	private final String prompt_down_title = "正在下载";
	private final String prompt_down_btn_cannel = "取消下载";
	private Context mContext;
	private int updateType;
	private String currentVerName, nowVerName, appDownUrl;
	private String mUpdateDescribe;// 升级描述
	private String saveFilePath = "cyzl.apk";
	private ProgressBar mProgress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;

	// update
	public static final String ERROR_MESSAGE = "下载失败";
	public static final String DOWNLOAD = "正在下载…";
	private NotificationManager mNotificationManager;
	protected int progress;
	protected boolean downFailure;
	private TextView tv_old_version;
	private TextView tv_update_version;
	private TextView tv_update_describe;
	private TextView tv_update_version_size;
	private double updateSize;
	private AlertDialog dialog;

	public UpdateManager(Context context) {
		this.mContext = context;

		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		this.currentVerName = this.nowVerName = this.appDownUrl = "";
		this.saveFilePath = HTSDTool.getSdFullPath(HTContant.SDCARD_PATH_TEMP) + saveFilePath;
		// HTSDTool.deleteFile(this.saveFilePath);
	}

	/**
	 * method desc：设置版本信息
	 * @param updateType 更新类型 2：强制更新 1 非强制性更新 0 不更新
	 * @param currentVerName 当前版本名称
	 * @param nowVerName 最新版名称
	 * @param appDownUrl 下载地址
	 * @param aUpdateDescribe 更新版本描述
	 */
	public void setVersionInfo(int updateType, String currentVerName, String nowVerName, String appDownUrl, String aUpdateDescribe, double aUpdateSize) {
		this.updateType = updateType;
		this.currentVerName = currentVerName;
		this.nowVerName = nowVerName;
		this.appDownUrl = appDownUrl;
		this.mUpdateDescribe = aUpdateDescribe;
		this.updateSize = aUpdateSize;
	}

	public void Run(boolean inBackground) {
		switch (this.updateType) {
		// 强制更新
		case 2:
			this.updateForMust();
			break;
		// 有非强制更新版本
		case 1:
			this.updateForChoose();
			break;
		// 已经是最新版,是否后台执行
		case 0:
		default:
			if (!inBackground) {
				this.updateForNot();
			}
			break;
		}
	}

	private void updateForNot() {
		AlertDialog dialog = new AlertDialog.Builder(this.mContext).setTitle(this.prompt_update_title).setMessage(this.getDialogMsg(this.prompt_update_msg_no_update)).setPositiveButton(this.prompt_update_btn_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void updateForChoose() {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.update_dialog_item, null);
		// 最新版本
		tv_update_version = (TextView) view.findViewById(R.id.tv_update_version);
		tv_update_version.setText("最新版本:" + nowVerName);
		// 当前版本
		tv_old_version = (TextView) view.findViewById(R.id.tv_old_version);
		tv_old_version.setText("当前版本:" + currentVerName);
		// 新版本大小
		tv_update_version_size = (TextView) view.findViewById(R.id.tv_update_version_size_value);
		tv_update_version_size.setText("新版本大小:" + updateSize + "M");

		// 版本描述
		tv_update_describe = (TextView) view.findViewById(R.id.tv_update_describe);
		mUpdateDescribe = Html.fromHtml(mUpdateDescribe).toString();
		tv_update_describe.setText("更新内容\r\n" + mUpdateDescribe);

		AlertDialog dialog = new AlertDialog.Builder(this.mContext).setTitle(this.prompt_update_title).setView(view).setPositiveButton(this.prompt_update_btn_update, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				setNotify(R.drawable.icon_download_small, "正在下载", null);
			}
		}).setNegativeButton(this.prompt_update_btn_update_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void updateForMust() {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.update_dialog_item, null);
		// 最新版本
		tv_update_version = (TextView) view.findViewById(R.id.tv_update_version);
		tv_update_version.setText("最新版本:" + nowVerName);
		// 当前版本
		tv_old_version = (TextView) view.findViewById(R.id.tv_old_version);
		tv_old_version.setText("当前版本:" + currentVerName);
		// 新版本大小
		tv_update_version_size = (TextView) view.findViewById(R.id.tv_update_version_size_value);
		tv_update_version_size.setText("新版本大小:" + updateSize + "M");

		// 版本描述
		tv_update_describe = (TextView) view.findViewById(R.id.tv_update_describe);
		mUpdateDescribe = Html.fromHtml(mUpdateDescribe).toString();
		tv_update_describe.setText("更新内容\r\n" + mUpdateDescribe);
		AlertDialog dialog = new AlertDialog.Builder(this.mContext).setTitle(this.prompt_update_title).setView(view).setPositiveButton(this.prompt_update_btn_update, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		}).setNegativeButton(prompt_update_btn_cannel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 这里做软件推出处理
				dialog.dismiss();
				HTBaseApplication.getInstance().closeAllActivitys();
			}
		}).setCancelable(false).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void showDownloadDialog() {
		final LayoutInflater inflater = LayoutInflater.from(this.mContext);
		View view = inflater.inflate(R.layout.sys_progress, null);
		this.mProgress = (ProgressBar) view.findViewById(R.id.sys_progress);
		dialog = new AlertDialog.Builder(this.mContext).setTitle(this.prompt_down_title).setView(view).setNegativeButton(this.prompt_down_btn_cannel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (updateType == 2) {
					// 这里做软件推出处理
					HTBaseApplication.getInstance().closeAllActivitys();
				}
				interceptFlag = true;
			}
		}).setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		}).create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		this.downloadApk();
	}

	private void setNotify(int iconId, String text, Intent intent) {
		if (progress >= 99) {
			return;
		}
		intent = new Intent();
		PendingIntent appIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

		final Notification notification = new Notification();
		notification.icon = iconId;
		notification.tickerText = text;

		final RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.notify_item);
		contentView.setProgressBar(R.id.progressbar_updown, 100, progress, false);
		notification.contentView = contentView;
		notification.contentIntent = appIntent;
		mNotificationManager.notify(R.layout.notify_item, notification);

		Thread th = new Thread() {
			public void run() {
				while (progress < 99 && !downFailure) {
					contentView.setTextViewText(R.id.updatebar_tip, DOWNLOAD);
					contentView.setTextViewText(R.id.update_size, progress + "%");
					contentView.setProgressBar(R.id.progressbar_updown, 100, progress, false);
					mNotificationManager.notify(R.layout.notify_item, notification);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (downFailure) {
					contentView.setTextViewText(R.id.updatebar_tip, ERROR_MESSAGE);
					mNotificationManager.notify(R.layout.notify_item, notification);
				} else {
					mNotificationManager.cancelAll();
				}

			};
		};
		th.start();
		this.downloadApk();
	}

	private void downloadApk() {
		this.downLoadThread = new Thread(this.mdownApkRunnable);
		this.downLoadThread.start();
	}

	private void installApk() {
		File apkfile = new File(saveFilePath);
		if (!apkfile.exists())
			return;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		this.mContext.startActivity(intent);
	}

	private String getDialogMsg(String promptTpl) {
		return promptTpl.replace("{verName}", this.currentVerName).replace("{newVerName}", this.nowVerName);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			UpdateManager.this.handleMessage(msg);
		};
	};

	private final int DOWN_UPDATE = 1;
	private final int DOWN_OVER = 2;
	private final int DOWN_ERROR = 3;

	private void handleMessage(Message msg) {
		switch (msg.what) {
		case DOWN_UPDATE:
			if (updateType == 2) {
				mProgress.setProgress(progress);
			}
			break;
		case DOWN_ERROR:
			downFailure = true;
			if (dialog != null) {
				dialog.dismiss();
				Toast.makeText(mContext, "手机存储不能挂载，无法下载", Toast.LENGTH_LONG).show();
			}
			break;
		case DOWN_OVER:
			if (dialog != null) {
				dialog.dismiss();
			}
			if (updateType == 2) {
				installApk();
			} else {
				showInstallDialog(false);
			}
			break;
		}
	};

	private void showInstallDialog(boolean isForce) {
		Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mContext.getString(R.string.update_tip));
		builder.setMessage(mContext.getString(R.string.downed_install));
		builder.setPositiveButton(mContext.getString(R.string.install), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				installApk();
			}
		});
		if (isForce) {
			builder.setCancelable(false);
		} else {
			builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		builder.create().show();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(appDownUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File ApkFile = new File(saveFilePath);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWN_ERROR);
			} catch (IOException e) {
				e.printStackTrace();
				LogControl.e("exception", e.getMessage());
				mHandler.sendEmptyMessage(DOWN_ERROR);
			}
		}
	};

}
