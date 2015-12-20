package com.geniusgithub.lookaround.activity.set;

import java.util.HashMap;

import roboguice.inject.InjectView;

import android.app.Dialog;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.SwitchButton;

public class BindActivity extends BaseActivity implements OnClickListener, 
														OnCheckedChangeListener,
														PlatformActionListener,
														Callback,
														IDialogInterface{
	
	private static final CommonLog log = LogFactory.createLog();
	
	
	@InjectView (R.id.btn_back) Button mBtnBack;  
	@InjectView (R.id.sb_sina) SwitchButton mCBSina;  
	@InjectView (R.id.sb_tencent) SwitchButton mCBTencent;  
	@InjectView (R.id.sb_qzone) SwitchButton mCBQZone;  
	@InjectView (R.id.tv_sina_owner) TextView mTVSina;  
	@InjectView (R.id.tv_tencent_owner) TextView mTVTencent;  
	@InjectView (R.id.tv_qzone_owner) TextView mTVQZone;  

	
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

    	mBtnBack.setOnClickListener(this);	
    	mCBSina.setOnCheckedChangeListener(this);
    	mCBTencent.setOnCheckedChangeListener(this);
    	mCBQZone.setOnCheckedChangeListener(this);
    	
    }
    
    private void initData(){
    	mPlatformSina = ShareSDK.getPlatform(this, SinaWeibo.NAME);
    	mPlatformTencent = ShareSDK.getPlatform(this, TencentWeibo.NAME);
    	mPlatformQZone = ShareSDK.getPlatform(this, QZone.NAME);    	
    	mPlatformSina.setPlatformActionListener(this);
    	mPlatformTencent.setPlatformActionListener(this);
    	mPlatformQZone.setPlatformActionListener(this);

    	updateSinaStatus();
    	updateTencentStatus();
		updateQZoneStatus();
			
    }
    
    private void updateWeiboStatus(String platName){
    	if (platName.equals(SinaWeibo.NAME)){
			updateSinaStatus();
		}else if (platName.equals(TencentWeibo.NAME)){
			updateTencentStatus();
		}else if (platName.equals(QZone.NAME)){
			updateQZoneStatus();
		}
    }
    
    
    private void updateSinaStatus(){
		PlatformDb db = mPlatformSina.getDb();
		String nickname = db.get("nickname");
    	if (nickname != null && nickname.length() != 0){
    		mTVSina.setText(nickname);
    		mCBSina.setChecked(true);
    	}else{
    		mTVSina.setText(getResources().getString(R.string.unbind));
    		mCBSina.setChecked(false);
    	}
    }
    
    
    private void updateTencentStatus(){
		PlatformDb db = mPlatformTencent.getDb();
		String nickname = db.get("nickname");
    	if (nickname != null && nickname.length() != 0){
    		mTVTencent.setText(nickname);
    		mCBTencent.setChecked(true);
    	}else{
    		mTVTencent.setText(getResources().getString(R.string.unbind));
    		mCBTencent.setChecked(false);
    	}
    }
    
    private void updateQZoneStatus(){
    	PlatformDb db = mPlatformQZone.getDb();
		String nickname = db.get("nickname");
    	if (nickname != null && nickname.length() != 0){
    		mTVQZone.setText(nickname);
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
			case R.id.sb_sina:
				onSinalCheck(isChecked);
				break;
			case R.id.sb_tencent:
				onTencentCheck(isChecked);
				break;
			case R.id.sb_qzone:
				onQZoneCheck(isChecked);
				break;
		}
		
	}
	
	private void onSinalCheck(boolean isChecked){
		if (isChecked){
			if (!mPlatformSina.isValid()){
				mPlatformSina.authorize();
			}			
		}else{
			if (mPlatformSina.isValid()){
				unbindPlatform = mPlatformSina;
				getUnbindDialog().show();	
			}
		}
	}
	
	private void onTencentCheck(boolean isChecked){
		if (isChecked){
			if (!mPlatformTencent.isValid()){
				mPlatformTencent.authorize();
			}		
		}else{
			if (mPlatformTencent.isValid()){
				unbindPlatform = mPlatformTencent;
				getUnbindDialog().show();
			}	
		}
	}
	
	private void onQZoneCheck(boolean isChecked){
		if (isChecked){
			if (!mPlatformQZone.isValid()){
				mPlatformQZone.authorize();
			}
		}else{
			if (mPlatformQZone.isValid()){
				unbindPlatform = mPlatformQZone;
				getUnbindDialog().show();
			}		
		}
	}

	
	private static final int MSG_ACTION_SUCCESS = 1;
	private static final int MSG_ACTION_FAIL = 2;
	private static final int MSG_ACTION_CANCEL = 3;
	
	@Override
	public boolean handleMessage(Message msg) {
		Platform platform = (Platform) msg.obj;
		String platName = platform.getName();
		switch(msg.what) {
			case MSG_ACTION_SUCCESS: 
				CommonUtil.showToast(R.string.toast_bind_success, this);			
				updateWeiboStatus(platName);
				break;
			case MSG_ACTION_FAIL: 
				updateWeiboStatus(platName);
				break;
			case MSG_ACTION_CANCEL:
				updateWeiboStatus(platName);
				break;
			default:
				break;
		}
		
		return false;
	}

	
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		log.e("onComplete Platform = " + platform.getName() + ", action = " + action);
		Message msg = new Message();
		msg.what = MSG_ACTION_SUCCESS;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		log.e("onError Platform = " + platform.getName() + ", action = " + action);
		Message msg = new Message();
		msg.what = MSG_ACTION_FAIL;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);

		// 分享失败的统计
		ShareSDK.logDemoEvent(4, platform);
	}

	public void onCancel(Platform platform, int action) {
		log.e("onCancel Platform = " + platform.getName() + ", action = " + action);
		Message msg = new Message();
		msg.what = MSG_ACTION_CANCEL;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}
	
	
	private Platform unbindPlatform;
	private Dialog unbindDialog;
	private Dialog getUnbindDialog(){
		Dialog dialog = DialogBuilder.buildNormalDialog(this,
				getResources().getString(R.string.dia_msg_unbind_title),
				getResources().getString(R.string.dia_msg_unbind_msg),
				this);
		dialog.setCancelable(false);
		return dialog;
	}

	@Override
	public void onSure() {
		if (unbindDialog != null){
			unbindDialog.dismiss();
		}
		
		if (unbindPlatform == mPlatformSina){
			mPlatformSina.removeAccount();
			updateSinaStatus();
		}else if (unbindPlatform == mPlatformTencent){
			mPlatformTencent.removeAccount();
			updateTencentStatus();
		}else if (unbindPlatform == mPlatformQZone){
			mPlatformQZone.removeAccount();
			updateQZoneStatus();
		}	
	
	}


	@Override
	public void onCancel() {
		if (unbindDialog != null){
			unbindDialog.dismiss();
		}
		
		if (unbindPlatform == mPlatformSina){
			mCBSina.setChecked(true);
		}else if (unbindPlatform == mPlatformTencent){
			mCBTencent.setChecked(true);
		}else if (unbindPlatform == mPlatformQZone){
			mCBQZone.setChecked(true);
		}	
	}


	
}
