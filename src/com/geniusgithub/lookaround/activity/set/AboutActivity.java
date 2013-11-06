package com.geniusgithub.lookaround.activity.set;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.BaseRequestPacket;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends BaseActivity implements OnClickListener,
															IRequestDataPacketCallback,
															IDialogInterface,
															 PlatformActionListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
	
	private View mAdviseView;
	private View mAttentionWeiboView;
	private View mCheckUpdateView;
	private View mSupportDevelopterView;
	
	private ImageView mIVUpageIcon;
	private TextView mTVVersion;
	
	private ClientEngine mClientEngine;	
	private PublicType.CheckUpdateResult object = null; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abount_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mBtnBack = (Button) findViewById(R.id.btn_back);  	
    	mBtnBack.setOnClickListener(this);    	

    	mSupportDevelopterView = findViewById(R.id.ll_support);
    	mAttentionWeiboView = findViewById(R.id.ll_attention);
    	mCheckUpdateView = findViewById(R.id.ll_checkupdate);
    	mAdviseView = findViewById(R.id.ll_advise);       	
    
    	
    	mSupportDevelopterView.setOnClickListener(this);
    	mAttentionWeiboView.setOnClickListener(this);
    	mCheckUpdateView.setOnClickListener(this);
    	mAdviseView.setOnClickListener(this);
    	
    	mTVVersion = (TextView) findViewById(R.id.tv_version);
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
    	
    	updateView();
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
		}
	}
	
	private void updateView(){
		
		String value = getResources().getString(R.string.tvt_ver_pre) + CommonUtil.getSoftVersion(this);
		mTVVersion.setText(value);
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
				CommonUtil.showToast(R.string.toast_attention_success, AboutActivity.this);
				
			}
		});
		
		
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		log.e("onError Platform = " + platform.getName() + ", action = " + action);

		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				CommonUtil.showToast(R.string.toast_attention_fail, AboutActivity.this);
				
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

}
