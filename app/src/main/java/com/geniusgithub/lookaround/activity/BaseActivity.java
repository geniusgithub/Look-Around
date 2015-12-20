package com.geniusgithub.lookaround.activity;

import roboguice.activity.RoboActivity;

import com.geniusgithub.lookaround.LAroundApplication;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends RoboActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LAroundApplication.onCatchError(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		LAroundApplication.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		LAroundApplication.onResume(this);
	}

}
