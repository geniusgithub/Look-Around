package com.geniusgithub.lookaround.activity.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.FileHelper;
import com.geniusgithub.lookaround.util.FileManager;
import com.geniusgithub.lookaround.util.LogFactory;

public class SettingActivity extends BaseActivity implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
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
    	
    	mIVUpageIcon = (ImageView) findViewById(R.id.iv_updateicon);
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
