package com.geniusgithub.lookaround;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class BackgroundService extends Service{

	private static final CommonLog log = LogFactory.createLog();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		log.e("BackgroundService onCreate...");
	}
	
	
}
