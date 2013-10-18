package com.geniusgithub.lookaround.activity.set;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.geniusgithub.lookaround.R;
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
import com.geniusgithub.lookaround.util.LogFactory;



public class SettingActivity extends Activity implements OnClickListener,
														IRequestDataPacketCallback,
														IDialogInterface{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
	private View mMyPushView;
	private View mMyCollectView;
	private View mClieaCacheView;
	private View mGradingView;
	private View mCheckUpdateView;
	private View mAboutView;
	private View mAdviseView;
	
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
    	
    	mClientEngine=  ClientEngine.getInstance(this);	
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
				checkUpdate();
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
	
	private void checkUpdate(){
		PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(this);
		
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.CHECK_UPDATE_MSGID;
		packet.object = object;
		
		mClientEngine.httpGetRequestEx(packet, this);
		CommonUtil.showToast(R.string.toast_checking_update, this);

	}


	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket,
			Object extra) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		switch(requestAction){
		case PublicType.CHECK_UPDATE_MSGID:
			onGetCheckUpdate(dataPacket);
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


	private Dialog updateDialog;
	private void onGetCheckUpdate( ResponseDataPacket dataPacket){
		object = new PublicType.CheckUpdateResult();
		
		try {
			object.parseJson(dataPacket.data);
			log.e("mHaveNewVer = " + object.mHaveNewVer +  "\nmVerCode = " + object.mVerCode + 
					"\nmVerName = " + object.mVerName + "\nmAppUrl = " + object.mAppUrl + "\nmVerDescribre = " + object.mVerDescribre);
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_anylizedata_fail, this);
			object = null;
			return ;
		}
		
		if (updateDialog != null){
			updateDialog.dismiss();
		}
		
		updateDialog = getUpdateDialog(object.mVerDescribre);
		updateDialog.show();
		
	}
	
	private Dialog getUpdateDialog(String message){
		Dialog dialog = DialogBuilder.buildNormalDialog(this, "check update", message, this);
		return dialog;
	}


	@Override
	public void onSure() {
		if (updateDialog != null){
			updateDialog.dismiss();
		}
		
	
		
	}


	@Override
	public void onNev() {
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

}