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

import java.util.ArrayList;
import java.util.List;

import com.geniusgithub.lookaround.activity.MainLookAroundActivity;
import com.geniusgithub.lookaround.activity.WelcomActivity;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Application;
import android.content.Intent;

public class LAroundApplication extends Application{

	private static final CommonLog log = LogFactory.createLog();
	
	private static LAroundApplication mInstance;

	private PublicType.UserLoginResult mUserLoginResult = new PublicType.UserLoginResult();
	
	private boolean isLogin = false;
	
	public synchronized static LAroundApplication getInstance(){
		return mInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		log.e("LAroundApplication  onCreate!!!");
		mInstance = this;
		startBackgroundService();
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
}
