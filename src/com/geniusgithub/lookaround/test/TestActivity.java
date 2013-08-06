package com.geniusgithub.lookaround.test;

import java.net.NetworkInterface;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.util.SecuteUtil;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity implements OnClickListener, IRequestCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnRegiste;
	private Button mBtnLogin;
	private Button mBtn3;

	private ClientEngine mClientEngine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setupViews();
		
		initData();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
	private void setupViews(){
		setContentView(R.layout.test_layout);
		
		mBtnRegiste = (Button) findViewById(R.id.btnRegister);
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtn3 = (Button) findViewById(R.id.button3);
		
		mBtnRegiste.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mBtn3.setOnClickListener(this);
	}
	
	private void initData(){
		mClientEngine=  ClientEngine.getInstance(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btnRegister:
				register();
				break;
			case R.id.btnLogin:
				login();
				break;
			case R.id.button3:
				break;
		}
	}
	
	
	private void register(){
		log.e("register");
		PublicType.UserRegister object = PublicTypeBuilder.buildUserRegister(this);
		
		mClientEngine.httpGetRequest(PublicType.USER_REGISTER_MASID, object, this);
	}
	
	private void login(){
		log.e("login");
		PublicType.UserLogin object = PublicTypeBuilder.buildUserLogin(this);
		
		mClientEngine.httpGetRequest(PublicType.USER_LOGIN_MASID, object, this);
	}
	
	


	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
	}

	@Override
	public void onRequestFailure(int requestAction, String content) {
	//	log.e("onRequestFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
		
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
	}

	
}
