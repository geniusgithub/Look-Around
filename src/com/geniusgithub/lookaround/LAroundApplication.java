/*
 * Copyright 2013 Lance(https://github.com/geniusgithub).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.geniusgithub.lookaround;


import java.util.HashMap;

import com.geniusgithub.lookaround.activity.MainLookAroundActivity;
import com.geniusgithub.lookaround.activity.WelcomActivity;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.gfan.sdk.statitistics.GFAgent;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class LAroundApplication extends Application implements ItatisticsEvent{

	private static final CommonLog log = LogFactory.createLog();
	
	private static LAroundApplication mInstance;

	private PublicType.UserLoginResult mUserLoginResult = new PublicType.UserLoginResult();
	
	
	private boolean isLogin = false;
	
	private PublicType.CheckUpdateResult updateObject = null;
	
	
	public synchronized static LAroundApplication getInstance(){
		return mInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		log.e("LAroundApplication  onCreate!!!");
		mInstance = this;
		startBackgroundService();
		MobclickAgent.setDebugMode(true);
		
		GFAgent.init(this);

	}
	
	public void setUserLoginResult(PublicType.UserLoginResult object){
		mUserLoginResult = object;
	}
	
	public PublicType.UserLoginResult getUserLoginResult(){
		return mUserLoginResult;
	}
	
	public void setLoginStatus(boolean flag){
		isLogin = flag;
	}
	
	public PublicType.CheckUpdateResult  getNewVersion(){
	
		return updateObject;
	}
	
	public void setNewVersionFlag(PublicType.CheckUpdateResult object){
		updateObject = object;
	}
	
	public boolean getLoginStatus(){
	
		return isLogin;
	}
	
	public void startToWelcomeActivity(){
		Intent intent = new Intent();
		intent.setClass(this, WelcomActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void startToMainActivity(){
		Intent intent = new Intent();
		intent.setClass(this, MainLookAroundActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void startBackgroundService(){
		Intent intent = new Intent();
		intent.setClass(this, BackgroundService.class);
		startService(intent);
	}

	@Override
	public void onEvent(String eventID) {
		log.e("eventID = " + eventID);
		
		MobclickAgent.onEvent(this, eventID);
		GFAgent.onEvent(this, eventID);
	}

	@Override
	public void onEvent(String eventID, HashMap<String, String> map) {
		log.e("eventID = " + eventID);
		
		MobclickAgent.onEvent(this, eventID, map);
		GFAgent.onEvent(this, eventID, map);
	}
	
	public static void onPause(Activity context){
		MobclickAgent.onPause(context);
		GFAgent.onPause(context);
	}
	
	public static void onResume(Activity context){
		MobclickAgent.onResume(context);
		GFAgent.onResume(context);
	}
	
	public static void onCatchError(Context context){
		MobclickAgent.onError(context);
		GFAgent.setReportUncaughtExceptions(true);
	}
}
