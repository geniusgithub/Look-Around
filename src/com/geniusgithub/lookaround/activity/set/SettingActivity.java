package com.geniusgithub.lookaround.activity.set;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.DialogEntity;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.BaseRequestPacket;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestContentCallback;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.FileHelper;
import com.geniusgithub.lookaround.util.FileManager;
import com.geniusgithub.lookaround.util.LogFactory;
import com.umeng.analytics.MobclickAgent;



public class SettingActivity extends BaseActivity implements OnClickListener,
														IRequestDataPacketCallback,
														IDialogInterface,
														 PlatformActionListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
	private View mMyPushView;
	private View mMyCollectView;
	private View mClieaCacheView;
	private View mShareGradingView;
	private View mSupportDevelopterView;
	private View mAttentionWeiboView;
	private View mCheckUpdateView;
	private View mAboutView;
	private View mAdviseView;
	
	private ImageView mIVUpageIcon;
	
	private ClientEngine mClientEngine;
	
	private PublicType.CheckUpdateResult object = null; 
	

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
    	mShareGradingView = findViewById(R.id.ll_garding);
    	mSupportDevelopterView = findViewById(R.id.ll_support);
    	mAttentionWeiboView = findViewById(R.id.ll_attention);
    	mCheckUpdateView = findViewById(R.id.ll_checkupdate);
    	mAboutView = findViewById(R.id.ll_about);
    	mAdviseView = findViewById(R.id.ll_advise);
    	
    	
    	mMyPushView.setOnClickListener(this);
    	mMyCollectView.setOnClickListener(this);
    	mClieaCacheView.setOnClickListener(this);	
    	mShareGradingView.setOnClickListener(this); 	
    	mSupportDevelopterView.setOnClickListener(this);
    	mAttentionWeiboView.setOnClickListener(this);
    	mCheckUpdateView.setOnClickListener(this);
    	mAboutView.setOnClickListener(this);
    	mAdviseView.setOnClickListener(this);
    	
    	mIVUpageIcon = (ImageView) findViewById(R.id.iv_update);
    	
    }
    
    private void initData(){

    	
    	mClientEngine=  ClientEngine.getInstance(this);	
    	
    	object = LAroundApplication.getInstance().getNewVersion();
    	if (object != null && object.mHaveNewVer != 0){
    		showUpdateIcon(true);
    	}else{
    		showUpdateIcon(false);
    		
    		PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(this);		
    		BaseRequestPacket packet = new BaseRequestPacket();
    		packet.action = PublicType.CHECK_UPDATE_MSGID;
    		packet.object = object;
    		packet.extra = new Object();
    		mClientEngine.httpGetRequestEx(packet, this);
    	}
    	
    }
    
    private void showUpdateIcon(boolean flag){
    	if (flag){
    		mIVUpageIcon.setVisibility(View.VISIBLE);
    	}else{
    		mIVUpageIcon.setVisibility(View.GONE);
    	}
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
				shareGrade();
				break;
			case R.id.ll_support:
				support();
				break;
			case R.id.ll_attention:
				attention();
				break;
			case R.id.ll_checkupdate:
				checkUpdate();
				break;
			case R.id.ll_about:
				goAboutActivity();
				break;
			case R.id.ll_advise:
				goAdviseActivity();
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
	
	private void goAdviseActivity(){
		Intent intent = new Intent();
		intent.setClass(this, AdviseActivity.class);
		startActivity(intent);
	}
	
	private void clearCache(){
		LAroundApplication.getInstance().onEvent("UM004");
		Platform mPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		mPlatform.removeAccount();
		mPlatform = ShareSDK.getPlatform(this, TencentWeibo.NAME);
		mPlatform.removeAccount();
		mPlatform = ShareSDK.getPlatform(this, QZone.NAME);
		mPlatform.removeAccount();
		CommonUtil.showToast(R.string.toast_clear_success, this);
		
		clearImageCache();
		
	}
	
	private void shareGrade(){
		
	}
	
	private void support(){
		
	}

	private void attention(){
		Platform plat = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		plat.setPlatformActionListener(this);
		plat.followFriend("2881812642");
	}
	
	private void checkUpdate(){
		
		if (object != null && object.mHaveNewVer != 0){
			if (updateDialog != null){
				updateDialog.dismiss();
			}
			
			updateDialog = getUpdateDialog(object.mContentList);
			updateDialog.show();
		}else{
			PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(this);
			
			
			BaseRequestPacket packet = new BaseRequestPacket();
			packet.action = PublicType.CHECK_UPDATE_MSGID;
			packet.object = object;
			
			mClientEngine.httpGetRequestEx(packet, this);
			CommonUtil.showToast(R.string.toast_checking_update, this);
		}


	}


	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket,
			Object extra) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		switch(requestAction){
		case PublicType.CHECK_UPDATE_MSGID:
			onGetCheckUpdate(dataPacket, extra);
			break;
			
		}
	}


	@Override
	public void onRequestFailure(int requestAction, String content, Object extra) {
		log.e("onRequestFailure --> requestAction = " + requestAction);
		
		CommonUtil.showToast(R.string.toast_getdata_fail, this);
	}


	@Override
	public void onAnylizeFailure(int requestAction, String content, Object extra) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		log.e("onComplete Platform = " + platform.getName() + ", action = " + action);

		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				CommonUtil.showToast(R.string.toast_attention_success, SettingActivity.this);
				
			}
		});
		
		
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		log.e("onError Platform = " + platform.getName() + ", action = " + action);

		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				CommonUtil.showToast(R.string.toast_attention_fail, SettingActivity.this);
				
			}
		});
	
		
	}

	public void onCancel(Platform platform, int action) {
		log.e("onCancel Platform = " + platform.getName() + ", action = " + action);

	}

	private Dialog updateDialog;
	private void onGetCheckUpdate( ResponseDataPacket dataPacket, Object extra){
		object = new PublicType.CheckUpdateResult();
		
		try {
			object.parseJson(dataPacket.data);
		//	log.e("mHaveNewVer = " + object.mHaveNewVer +  "\nmVerCode = " + object.mVerCode + 
		//			"\nmVerName = " + object.mVerName + "\nmAppUrl = " + object.mAppUrl + "\nmContent.size = " + object.mContentList.size());
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_anylizedata_fail, this);
			object = null;
			return ;
		}
		
		if (object.mHaveNewVer != 0){	
			showUpdateIcon(true);
			if (extra == null){
				if (updateDialog != null){
					updateDialog.dismiss();
				}
				
				updateDialog = getUpdateDialog(object.mContentList);
				updateDialog.show();
			}
			LAroundApplication.getInstance().setNewVersionFlag(object);
		}else{
			if (extra == null){
				CommonUtil.showToast(R.string.toast_no_update, this);
			}
			
		}
		
		

	}
	
	private Dialog getUpdateDialog(List<String > list){
		int size = list.size();
		StringBuffer sBuffer = new StringBuffer();
		for(int i = 0; i < size; i++){
			String value = String.valueOf(i + 1) + "." + list.get(i);
			if (i != size - 1){
				sBuffer.append(value +  "\n");	
			}
		}
		log.e("msg = " + sBuffer.toString());
		Dialog dialog = DialogBuilder.buildNormalDialog(this, "版本更新" + object.mVerName, sBuffer.toString(), this);
		return dialog;
	}


	@Override
	public void onSure() {
		if (updateDialog != null){
			updateDialog.dismiss();
		}
		
		log.e("object:" + object);
		if (object != null){
			Intent intents = new Intent(Intent.ACTION_VIEW);
			intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intents.setData(Uri.parse(object.mAppUrl));
			startActivity(intents);
			log.e("jump to url:" + object.mAppUrl);
		}
		
	}

	
	@Override
	public void onCancel() {
		if (updateDialog != null){
			updateDialog.dismiss();
		}
	
		
		
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