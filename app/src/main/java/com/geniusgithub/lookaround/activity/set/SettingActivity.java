package com.geniusgithub.lookaround.activity.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseActivityEx;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.FileHelper;
import com.geniusgithub.lookaround.util.FileManager;
import com.geniusgithub.lookaround.util.LogFactory;

public class SettingActivity extends BaseActivityEx implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();

	private Toolbar toolbar;

	private View mBindView;
	private View mMyPushView;
	private View mMyCollectView;
	private View mClieaCacheView;
	private View mAboutView;
	private ImageView mIVUpageIcon;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
		initToolBar();

		mBindView = findViewById(R.id.ll_bindaccount);
		mMyPushView = findViewById(R.id.ll_mypush);
		mMyCollectView = findViewById(R.id.ll_mycollect);
		mClieaCacheView = findViewById(R.id.ll_clearcache);
		mAboutView = findViewById(R.id.ll_about);
		mIVUpageIcon = (ImageView)findViewById(R.id.iv_updateicon);


    	mBindView.setOnClickListener(this);
    	mMyPushView.setOnClickListener(this);
    	mMyCollectView.setOnClickListener(this);
    	mClieaCacheView.setOnClickListener(this);	
    	mAboutView.setOnClickListener(this);   	
    }

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.setting);
		setSupportActionBar(toolbar);

		final ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
	}

	private void initData(){
    	PublicType.UserLoginResult object = LAroundApplication.getInstance().getUserLoginResult();
    	log.e("object.mHaveNewVer = " + object.mHaveNewVer);
    	if (object.mHaveNewVer != 0){
    			mIVUpageIcon.setImageResource(R.drawable.app_new);	
    	}
    }


	@Override
	public void onClick(View view) {
	
		switch(view.getId()){
			case R.id.ll_bindaccount:
				goBindActivity();
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
			case R.id.ll_about:
				goAboutActivity();
				break;
		}
	}
	
	private void goBindActivity(){
		Toast.makeText(this, "功能暂时屏蔽，敬请谅解", Toast.LENGTH_SHORT).show();
/*		Intent intent = new Intent();
		intent.setClass(this, BindActivity.class);
		startActivity(intent);*/
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
		LAroundApplication.getInstance().onEvent("UM004");
		CommonUtil.showToast(R.string.toast_clear_success, this);
		
		clearImageCache();
		
	}
	
	private ClearThread thread = null;
	private void clearImageCache(){
		if (thread == null){
			thread = new ClearThread();
			thread.start();
		}else{
			if (!thread.isAlive()){
				thread = new ClearThread();
				thread.start();
			}
		}

	}
	
	
	
	private class ClearThread extends Thread{

		@Override
		public void run() {
			String path = FileManager.getCacheFileSavePath();
			log.e("clearThread run path:" + path);
			long time1 = System.currentTimeMillis();
			
			FileHelper.deleteDirectory(path);
			
			long time2 = System.currentTimeMillis();
			log.e("clearThread complete, cost time:" + (time2 - time1));
		}
		
	}
}
