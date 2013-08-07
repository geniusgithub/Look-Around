package com.geniusgithub.lookaround.test;

import java.net.NetworkInterface;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.model.PublicType.GetTypeList;
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
	private Button mBtnBindToken;
	private Button mBtnAdClick;
	private Button mBtnAbout;
	private Button mBtnGetTypeList;
	private Button mBtnGetinfo;
	private Button mBtnDelinfo;
	
	
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
		mBtnBindToken = (Button) findViewById(R.id.btnBindToken);
		mBtnAdClick = (Button) findViewById(R.id.btnAdClick);
		mBtnAbout = (Button) findViewById(R.id.btnAbout);
		mBtnGetTypeList = (Button) findViewById(R.id.btnGetTypeList);
		mBtnGetinfo = (Button) findViewById(R.id.btnGetInfo);
		mBtnDelinfo = (Button) findViewById(R.id.btnDelInfo);
		
		mBtnRegiste.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mBtnBindToken.setOnClickListener(this);
		mBtnAdClick.setOnClickListener(this);
		mBtnAbout.setOnClickListener(this);
		mBtnGetTypeList.setOnClickListener(this);
		mBtnGetinfo.setOnClickListener(this);
		mBtnDelinfo.setOnClickListener(this);
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
			case R.id.btnBindToken:
				bindtoken();
				break;
			case R.id.btnAdClick:
				adClilck();
				break;
			case R.id.btnAbout:
				about();
				break;
			case R.id.btnGetTypeList:
				break;
			case R.id.btnGetInfo:
				getInfo();
				break;
			case R.id.btnDelInfo:
				delInfo();
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
	
	private void bindtoken(){
		log.e("bindtoken");
		PublicType.BindToken object = PublicTypeBuilder.buildBindToken(this);
		
		mClientEngine.httpGetRequest(PublicType.BIND_TOKEN_MSGID, object, this);
	}
	
	private void adClilck(){
		log.e("adClilck");
		PublicType.AdClick object = PublicTypeBuilder.buildAdClick(this);
		
		mClientEngine.httpGetRequest(PublicType.AD_CLICK_MSGID, object, this);
	}
	
	private void about(){
		log.e("about");
		PublicType.AboutPage object = PublicTypeBuilder.buildAboutPage(this);
		
		mClientEngine.httpGetRequest(PublicType.ABOUT_MSGID, object, this);
	}
	
	
	private void getInfo(){
		log.e("getInfo");
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(this);
		
		mClientEngine.httpGetRequest(PublicType.GET_INFO_MSGID, object, this);
	}
	
	private void delInfo(){
		log.e("delInfo");
		PublicType.DeleteInfo object = PublicTypeBuilder.buildDeleteInfo(this);
		
		mClientEngine.httpGetRequest(PublicType.DELETE_INFO_MSGID, object, this);
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
