package com.hengtong.library.audio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class RecorderSound 
{
	private final static String EXTENSION=".amr";
	private final static String PREFIX="CR-";
	private Context mContext;
	
	private MediaRecorder mMediaRecorder;
	private File mFile = null;
	private String mDisplayName;
	
	public RecorderSound(Context context,String displayName)
	{
		mContext = context;
		mDisplayName = PREFIX + displayName;
	}
	
	public void startRecording() throws Exception
	{
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		String dir = Environment.getExternalStorageDirectory() + "/Recording";
		if(mFile == null)
		{
			File newRootDir = new File(dir);
			if(!newRootDir.exists())
			{
				newRootDir.mkdir();
			}
			//mFile = File.createTempFile(mDisplayName, EXTENSION, newRootDir);
			mFile = new File(newRootDir,mDisplayName + EXTENSION);
		}
		
		mMediaRecorder.setOutputFile(mFile.getAbsolutePath());
		
		mMediaRecorder.prepare();
		mMediaRecorder.start();
	}
	
	public void stopRecording()
	{
		mMediaRecorder.stop();
		mMediaRecorder.release();
		mMediaRecorder = null;
	}
	
	@SuppressLint("SimpleDateFormat") public void saveToDB()
	{
		ContentValues values = new ContentValues(3);
		long current = System.currentTimeMillis();
		long modDate = mFile.lastModified();
		Date date = new Date(current);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String title = formatter.format(date);
		values.put(MediaStore.Audio.Media.IS_MUSIC, "0");
		values.put(MediaStore.Audio.Media.TITLE,title);
		values.put(MediaStore.Audio.Media.DATA,mFile.getAbsolutePath());
		values.put(MediaStore.Audio.Media.DATE_ADDED,(int)(current / 1000));
		values.put(MediaStore.Audio.Media.DATE_MODIFIED,(int)(modDate / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE,"audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST,"CallRecord");
		values.put(MediaStore.Audio.Media.ALBUM,"CallRecord");
		ContentResolver contentResolver = mContext.getContentResolver();
		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);
		mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,newUri));
	}
	
	public String getFilePath()
	{
		return mFile.getAbsolutePath();
	}
}
