package com.geniusgithub.lookaround.activity.set;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseActivityEx;
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

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class AboutActivity extends BaseActivityEx implements OnClickListener,
															IRequestDataPacketCallback,
															IDialogInterface,
															 PlatformActionListener{

	private static final CommonLog log = LogFactory.createLog();



	private Toolbar toolbar;
	private  View mAdviseView;
	private View mAttentionWeiboView;
	private View mCheckUpdateView;
	private ImageView mIVUpageIcon;
	private ImageView mLogoIcon;
	private TextView mTVVersion;
		
	private ClientEngine mClientEngine;	
	private PublicType.CheckUpdateResult object = null;

	private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abount_layout);

        setupViews();
        initData();

    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}



	private void setupViews(){
		initToolBar();

		mAdviseView = findViewById(R.id.ll_advise);
		mAttentionWeiboView = findViewById(R.id.ll_attention);
		mCheckUpdateView = findViewById(R.id.ll_checkupdate);
		mIVUpageIcon = (ImageView)findViewById(R.id.iv_updateicon);
		mLogoIcon = (ImageView)findViewById(R.id.iv_logo);
		mTVVersion = (TextView) findViewById(R.id.tv_version);


    	mAttentionWeiboView.setOnClickListener(this);
    	mCheckUpdateView.setOnClickListener(this);
    	mAdviseView.setOnClickListener(this);

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

		mHandler = new Handler();

    }

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.about);
		setSupportActionBar(toolbar);

		final ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
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
			case R.id.ll_advise:
				goAdviseActivity();
				break;
			case R.id.ll_attention:
				attention();
				break;
			case R.id.ll_checkupdate:
				checkUpdate();
				break;
		}
	}
	
	private void updateView(){
		
		String value = getResources().getString(R.string.tvt_ver_pre) + CommonUtil.getSoftVersion(this);
		mTVVersion.setText(value);
	}
	
	private void goAdviseActivity(){
		Intent intent = new Intent();
		intent.setClass(this, AdviseActivity.class);
		startActivity(intent);
	}
	
	private void attention(){
		Toast.makeText(this, "功能暂时屏蔽，敬请谅解", Toast.LENGTH_SHORT).show();
		/*Platform plat = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		plat.setPlatformActionListener(this);
		plat.followFriend("2881812642");*/
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
			sBuffer.append(value);
			if (i != size - 1){
				sBuffer.append("\n");	
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
