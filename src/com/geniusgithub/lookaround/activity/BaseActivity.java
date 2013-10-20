package com.geniusgithub.lookaround.activity;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MobclickAgent.onError(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		MobclickAgent.onResume(this);
	}

}
