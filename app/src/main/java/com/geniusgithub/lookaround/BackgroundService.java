package com.geniusgithub.lookaround;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.geniusgithub.common.util.AlwaysLog;


public class BackgroundService extends Service{

	private final static String TAG = BackgroundService.class.getSimpleName();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		AlwaysLog.i(TAG, "BackgroundService onCreate...");
	}
	
	
}
