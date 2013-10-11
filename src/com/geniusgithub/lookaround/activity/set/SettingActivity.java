package com.geniusgithub.lookaround.activity.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.util.CommonUtil;



public class SettingActivity extends Activity implements OnClickListener{

	private Button mBtnBack;
	private View mMyPushView;
	private View mMyCollectView;
	private View mClieaCacheView;
	private View mGradingView;
	private View mCheckUpdateView;
	private View mAboutView;
	private View mAdviseView;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);  	
    	mBtnBack.setOnClickListener(this);
    	

    	mMyPushView = findViewById(R.id.ll_mypush);
    	mMyCollectView = findViewById(R.id.ll_mycollect);
    	mClieaCacheView = findViewById(R.id.ll_clearcache);
    	mGradingView = findViewById(R.id.ll_garding);
    	mCheckUpdateView = findViewById(R.id.ll_checkupdate);
    	mAdviseView = findViewById(R.id.ll_advise);
    	mAboutView = findViewById(R.id.ll_about);
    	
    	mMyPushView.setOnClickListener(this);
    	mMyCollectView.setOnClickListener(this);
    	mClieaCacheView.setOnClickListener(this);	
    	mGradingView.setOnClickListener(this); 	
    	mCheckUpdateView.setOnClickListener(this);
    	mAdviseView.setOnClickListener(this);
    	mAboutView.setOnClickListener(this);
    	
    }
    
    private void initData(){
    	ShareSDK.initSDK(this);
    }


	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.ll_mypush:
				CommonUtil.showToast(R.string.toast_no_push, this);
				break;
			case R.id.ll_mycollect:
				goCollectActivity();
				break;
			case R.id.ll_clearcache:
				clearCache();
				break;
			case R.id.ll_garding:
				CommonUtil.showToast(R.string.toast_no_function, this);
				break;
			case R.id.ll_checkupdate:
				CommonUtil.showToast(R.string.toast_no_function, this);
				break;
			case R.id.ll_about:
				goAboutActivity();
				break;
			case R.id.ll_advise:
				CommonUtil.showToast(R.string.toast_no_function, this);
				break;
		}
	}
	
	private void goCollectActivity(){
		Intent intent = new Intent();
		intent.setClass(this, CollectActivity.class);
		startActivity(intent);
	}
	
	private void goAboutActivity(){
		Intent intent = new Intent();
		intent.setClass(this, AboutActivity.class);
		startActivity(intent);
	}
	
	private void clearCache(){
		Platform mPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		mPlatform.removeAccount();
		mPlatform = ShareSDK.getPlatform(this, TencentWeibo.NAME);
		mPlatform.removeAccount();
		mPlatform = ShareSDK.getPlatform(this, QZone.NAME);
		mPlatform.removeAccount();
		CommonUtil.showToast(R.string.toast_clear_success, this);
		
	}
}