package com.geniusgithub.lookaround.activity.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.FileHelper;
import com.geniusgithub.lookaround.util.FileManager;
import com.geniusgithub.lookaround.util.LogFactory;

public class SetExActivity extends BaseActivity implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
	private View mBindView;
	private View mMyPushView;
	private View mMyCollectView;
	private View mClieaCacheView;	
	private View mAboutView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_ex_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);  	
    	mBtnBack.setOnClickListener(this);  	

    	mBindView = findViewById(R.id.ll_bindaccount);
    	mMyPushView = findViewById(R.id.ll_mypush);
    	mMyCollectView = findViewById(R.id.ll_mycollect);
    	mClieaCacheView = findViewById(R.id.ll_clearcache);
    	mAboutView = findViewById(R.id.ll_about);   	
    	
    	mBindView.setOnClickListener(this);
    	mMyPushView.setOnClickListener(this);
    	mMyCollectView.setOnClickListener(this);
    	mClieaCacheView.setOnClickListener(this);	
    	mAboutView.setOnClickListener(this);   	
    }
    
    private void initData(){

    }


	@Override
	public void onClick(View view) {
	
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
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
		Intent intent = new Intent();
		intent.setClass(this, BindActivity.class);
		startActivity(intent);
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
