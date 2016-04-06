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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hengtong.library.R;
import com.hengtong.library.utils.LogControl;

/**
 * 功能：升级管理类
 * @ahthor：黄荣星
 * @date:2013-12-16
 * @version::V1.0
 */

@SuppressLint("HandlerLeak")
public class UpdateManager {

	private Context mContext;
	private ProgressBar mProgress;
	private boolean interceptFlag = false;

	// update
	public static final String ERROR_MESSAGE = "下载失败";
	public static final String DOWNLOAD = "正在下载…";
	private NotificationManager mNotificationManager;
	protected int progress;
	protected boolean downFailure;
	private AlertDialog dialog;

	public UpdateManager(Context context) {
		this.mContext = context;
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void setNotify(int iconId, String text, Intent intent) {
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

	}

	private void downloadFile(final String aURL, String aSavePath) {
		// 下载函数
		String filename = aURL.substring(aURL.lastIndexOf("/") + 1);
		try {
			URL url = new URL(aURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();
			File ApkFile = new File(aSavePath + filename);
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
			mProgress.setProgress(progress);
			break;
		case DOWN_ERROR:
			if (dialog != null) {
				dialog.dismiss();
				Toast.makeText(mContext, "手机存储不能挂载，无法下载", Toast.LENGTH_LONG).show();
			}
			break;
		case DOWN_OVER:
			Toast.makeText(mContext, "下载完成", Toast.LENGTH_LONG).show();
			break;
		}
	};

}
