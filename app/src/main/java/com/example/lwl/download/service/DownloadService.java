package com.example.lwl.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class DownloadService extends Service{
	private Handler mHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String downloadUrl = intent.getStringExtra("url");
		new AsyncTaskUtil(getApplication(),mHandler).execute(downloadUrl);
		return super.onStartCommand(intent, flags, startId);
	}

}
