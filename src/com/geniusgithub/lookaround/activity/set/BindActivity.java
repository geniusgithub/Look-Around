package com.geniusgithub.lookaround.activity.set;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.SwitchButton;

public class BindActivity extends BaseActivity implements OnClickListener, 
														OnCheckedChangeListener,
														PlatformActionListener{
	
	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
	
	private SwitchButton mCBSina;
	private SwitchButton mCBTencent;
	private SwitchButton mCBQZone;

	private TextView mTVSina;
	private TextView mTVTencent;
	private TextView mTVQZone;
	
	private Platform mPlatformSina;
	private Platform mPlatformTencent;
	private Platform mPlatformQZone;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(this);
    	
    	mCBSina = (SwitchButton) findViewById(R.id.sb_sina);
    	mCBTencent = (SwitchButton) findViewById(R.id.sb_tencent);
    	mCBQZone = (SwitchButton) findViewById(R.id.sb_qzone);
    	
    	mCBSina.setOnCheckedChangeListener(this);
    	mCBTencent.setOnCheckedChangeListener(this);
    	mCBQZone.setOnCheckedChangeListener(this);
    	
    	mTVSina = (TextView) findViewById(R.id.tv_sina_owner);
    	mTVTencent = (TextView) findViewById(R.id.tv_tencent_owner);
    	mTVQZone = (TextView) findViewById(R.id.tv_qzone_owner);
    	
    }
    
    private void initData(){
    	mPlatformSina = ShareSDK.getPlatform(this, SinaWeibo.NAME);
    	mPlatformTencent = ShareSDK.getPlatform(this, TencentWeibo.NAME);
    	mPlatformQZone = ShareSDK.getPlatform(this, QZone.NAME);
    	

		PlatformDb db = mPlatformSina.getDb();
		String nickname = db.get("nickname");
		updateSinaStatus(nickname);
		log.e("updateSinaStatus nickname = " + nickname);
		
		db = mPlatformTencent.getDb();
		nickname = db.get("nickname");
		updateTencentStatus(nickname);
		log.e("updateTencentStatus nickname = " + nickname);
		
		db = mPlatformQZone.getDb();
		nickname = db.get("nickname");
		updateQZoneStatus(nickname);
		log.e("updateQZoneStatus nickname = " + nickname);
		
		
    }
    
    
    private void updateSinaStatus(String nickName){
    	if (nickName != null && nickName.length() != 0){
    		mTVSina.setText(nickName);
    		mCBSina.setChecked(true);
    	}else{
    		mTVSina.setText(getResources().getString(R.string.unbind));
    		mCBSina.setChecked(false);
    	}
    }
    
    private void updateTencentStatus(String nickName){
    	if (nickName != null && nickName.length() != 0){
    		mTVTencent.setText(nickName);
    		mCBTencent.setChecked(true);
    	}else{
    		mTVTencent.setText(getResources().getString(R.string.unbind));
    		mCBTencent.setChecked(false);
    	}
    }
    
    private void updateQZoneStatus(String nickName){
    	if (nickName != null && nickName.length() != 0){
    		mTVQZone.setText(nickName);
    		mCBQZone.setChecked(true);
    	}else{
    		mTVQZone.setText(getResources().getString(R.string.unbind));
    		mCBQZone.setChecked(false);
    	}
    }


	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
			case R.id.btn_sina:
				onSinalCheck(isChecked);
				break;
			case R.id.btn_tencent:
				onTencentCheck(isChecked);
				break;
			case R.id.btn_qzone:
				onQZoneCheck(isChecked);
				break;
		}
		
	}
	
	private void onSinalCheck(boolean isChecked){
		if (isChecked){
			
		}else{
			updateSinaStatus(null);
			mPlatformSina.removeAccount();
		}
	}
	
	private void onTencentCheck(boolean isChecked){
		if (isChecked){
			
		}else{
			updateTencentStatus(null);
			mPlatformTencent.removeAccount();
		}
	}
	
	private void onQZoneCheck(boolean isChecked){
		if (isChecked){
			
		}else{
			updateQZoneStatus(null);
			mPlatformQZone.removeAccount();
		}
	}


	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}
}
