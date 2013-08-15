package com.geniusgithub.lookaround.activity;

import org.json.JSONException;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.R.string;
import com.geniusgithub.lookaround.datastore.LocalConfigSharePreference;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomActivity extends Activity implements IRequestDataPacketCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private LAroundApplication mApplication;
	private ClientEngine mClientEngine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupViews();	
		initData();
		requestRegister();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	private void setupViews(){
		setContentView(R.layout.welcome_layout);
	}
	
	private void initData(){
		mApplication = LAroundApplication.getInstance();
		
		mClientEngine = ClientEngine.getInstance(getApplicationContext());
	}
	
	private void requestRegister(){
		String keys = LocalConfigSharePreference.getKeys(this);
		if (keys.equals("")){
			PublicType.UserRegister object = PublicTypeBuilder.buildUserRegister(this);		
			mClientEngine.httpGetRequestEx(this, PublicType.USER_REGISTER_MASID, object, this);
		}else{
			requestLogin(keys);
		}
	}
	
	private void requestLogin(String keys){
		PublicType.UserLogin object = PublicTypeBuilder.buildUserLogin(this, keys);
		
		mClientEngine.httpGetRequestEx(this, PublicType.USER_LOGIN_MASID, object, this);
	}

	@Override
	public void onBackPressed() {
		mClientEngine.cancelTask(this);	
		exit();
	}
	
	
	private void exit(){
		finish();
	}

	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket) {
		switch (requestAction) {
		case PublicType.USER_REGISTER_MASID:
			onTransdelRegister(dataPacket);
			break;
		case PublicType.USER_LOGIN_MASID:
			onTransdelLogin(dataPacket);
			break;
		default:
			break;
		}
	}

	@Override
	public void onRequestFailure(int requestAction, String content) {
		log.e("WelcomActivity --> onRequestFailure \nrequestAction = " + requestAction + "\ncontent = " + content);
		
		switch (requestAction) {
		case PublicType.USER_REGISTER_MASID:
			CommonUtil.showToast(R.string.toast_register_fail, this);
			exit();
			break;
		case PublicType.USER_LOGIN_MASID:
			CommonUtil.showToast(R.string.toast_login_fail, this);
			exit();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content) {
		log.e("WelcomActivity --> onAnylizeFailure \nrequestAction = " + requestAction + "\ncontent = " + content);
		
		switch (requestAction) {
		case PublicType.USER_REGISTER_MASID:
			CommonUtil.showToast(R.string.toast_register_fail, this);
			exit();
			break;
		case PublicType.USER_LOGIN_MASID:
			CommonUtil.showToast(R.string.toast_login_fail, this);
			exit();
			break;
		default:
			break;
		}
		
	}

	
	
	
	private void onTransdelRegister(ResponseDataPacket dataPacket){
		log.e("Register success...");
		PublicType.UserRegisterResult object = new PublicType.UserRegisterResult();
		try {
			object.parseJson(dataPacket.data);
			LocalConfigSharePreference.commintKeys(this, object.mKeys);
			requestLogin(object.mKeys);
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_register_fail, this);
			exit();
		}
	}
	
	private void onTransdelLogin(ResponseDataPacket dataPacket){
		log.e("Login success...");
		PublicType.UserLoginResult object = new PublicType.UserLoginResult();
		try {
			object.parseJson(dataPacket.data);
			mApplication.setUserLoginResult(object);
			goMainActivity();
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_login_fail, this);
			exit();
		}
	}
	
	private void goMainActivity(){
		Intent intent = new Intent();
		intent.setClass(this, MainLookAroundActivity.class);
		startActivity(intent);
		finish();
	}
}
