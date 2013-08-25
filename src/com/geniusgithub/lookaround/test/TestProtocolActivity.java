package com.geniusgithub.lookaround.test;

import java.net.NetworkInterface;

import org.json.JSONException;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.animation.MyAnimations;
import com.geniusgithub.lookaround.model.PublicType;
import com.geniusgithub.lookaround.model.PublicTypeBuilder;
import com.geniusgithub.lookaround.model.PublicType.GetTypeList;
import com.geniusgithub.lookaround.network.BaseRequestPacket;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.network.IRequestContentCallback;
import com.geniusgithub.lookaround.network.IRequestDataPacketCallback;
import com.geniusgithub.lookaround.network.ResponseDataPacket;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class TestProtocolActivity extends Activity implements OnClickListener, IRequestDataPacketCallback, IRequestContentCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnRegiste;
	private Button mBtnLogin;
	private Button mBtnBindToken;
	private Button mBtnAdClick;
	private Button mBtnAbout;
	private Button mBtnGetinfo;
	private Button mBtnDelinfo;
	
	
	private ClientEngine mClientEngine;
	
	
	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;

	private RelativeLayout composerButtonsShowHideButton;
	
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
		MyAnimations.initOffset(TestProtocolActivity.this);
		
		mBtnRegiste = (Button) findViewById(R.id.btnRegister);
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtnBindToken = (Button) findViewById(R.id.btnBindToken);
		mBtnAdClick = (Button) findViewById(R.id.btnAdClick);
		mBtnAbout = (Button) findViewById(R.id.btnAbout);
		mBtnGetinfo = (Button) findViewById(R.id.btnGetInfo);
		mBtnDelinfo = (Button) findViewById(R.id.btnDelInfo);
		
		mBtnRegiste.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mBtnBindToken.setOnClickListener(this);
		mBtnAdClick.setOnClickListener(this);
		mBtnAbout.setOnClickListener(this);
		mBtnGetinfo.setOnClickListener(this);
		mBtnDelinfo.setOnClickListener(this);
		
		
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
		composerButtonsShowHideButton.setOnClickListener(this);
	}
	
	private void initData(){
		mClientEngine=  ClientEngine.getInstance(this);
		
		areButtonsShowing = false;
		
		// 加号的动画
		composerButtonsShowHideButton.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));
		
		// 给小图标设置点击事件
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			final ImageView smallIcon = (ImageView) composerButtonsWrapper.getChildAt(i);
			final int position = i;
			smallIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 这里写各个item的点击事件
					// 1.加号按钮缩小后消失 缩小的animation
					// 2.其他按钮缩小后消失 缩小的animation
					// 3.被点击按钮放大后消失 透明度渐变 放大渐变的animation
					//composerButtonsShowHideButton.startAnimation(MyAnimations.getMiniAnimation(300));
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
					areButtonsShowing = !areButtonsShowing;
					smallIcon.startAnimation(MyAnimations.getMaxAnimation(400));
					for (int j = 0; j < composerButtonsWrapper.getChildCount(); j++) {
						if (j != position) {
							final ImageView smallIcon = (ImageView) composerButtonsWrapper.getChildAt(j);
							smallIcon.startAnimation(MyAnimations.getMiniAnimation(300));
						}
					}
				}
			});
		}
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
			case R.id.btnGetInfo:
				getInfo();
				break;
			case R.id.btnDelInfo:
				delInfo();
				break;
			case R.id.composer_buttons_show_hide_button:
				toggle();
				break;
		}
	}
	
	
	private void register(){
		log.e("register");
		PublicType.UserRegister object = PublicTypeBuilder.buildUserRegister(this);
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.USER_REGISTER_MASID;
		packet.object = object;
	
		mClientEngine.httpGetRequestEx(packet, this);
	}
	
	private void login(){
		log.e("login");
		PublicType.UserLogin object = PublicTypeBuilder.buildUserLogin(this, "0");

		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.USER_LOGIN_MASID;
		packet.object = object;
		
		mClientEngine.httpGetRequestEx(packet, this);
	}
	
	private void bindtoken(){
		log.e("bindtoken");
		PublicType.BindToken object = PublicTypeBuilder.buildBindToken(this);
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.BIND_TOKEN_MSGID;
		packet.object = object;
		
		
		mClientEngine.httpGetRequestEx(packet, this);
	}
	
	private void adClilck(){
		log.e("adClilck");
		PublicType.AdClick object = PublicTypeBuilder.buildAdClick(this);
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.AD_CLICK_MSGID;
		packet.object = object;
		
		mClientEngine.httpGetRequestEx(packet, this);
	}
	
	private void about(){
		log.e("about");
		PublicType.AboutPage object = PublicTypeBuilder.buildAboutPage(this);
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.ABOUT_MSGID;
		packet.object = object;
		
		mClientEngine.httpGetRequest(packet, this);
	}
	
	
	private void getInfo(){
		log.e("getInfo");
		PublicType.GetInfo object = PublicTypeBuilder.buildGetInfo(this, "1");
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.GET_INFO_MSGID;
		packet.object = object;
		
		mClientEngine.httpGetRequestEx(packet, this);
	}
	
	private void delInfo(){
//		log.e("delInfo");
//		PublicType.DeleteInfo object = PublicTypeBuilder.buildDeleteInfo(this);
//		
//		mClientEngine.httpGetRequestEx(PublicType.DELETE_INFO_MSGID, object, this);
	}

	private void toggle(){
		log.e("toggle areButtonsShowing = " + areButtonsShowing);
		if (!areButtonsShowing) {
			// 图标的动画
			MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
			// 加号的动画
			composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(0, -225, 300));
		} else {
			// 图标的动画
			MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
			// 加号的动画
			composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
		}
		areButtonsShowing = !areButtonsShowing;
	}
	
	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket, Object extra) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		switch(requestAction){
			case PublicType.USER_REGISTER_MASID:{
				onRegisterResult(dataPacket);
			}
				break;
			case PublicType.USER_LOGIN_MASID:{
				onLoginResult(dataPacket);
			}
				break;	
			case PublicType.GET_INFO_MSGID:{
				onGetInfoResult(dataPacket);
			}
				break;
				
		}
	}

	@Override
	public void onRequestFailure(int requestAction, String content, Object extra) {
	//	log.e("onRequestFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
		
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content, Object extra) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
	}
	
	@Override
	public void onResult(int requestAction, Boolean isSuccess, String content, Object extra) {
		log.e("onResult!isSuccess = " + isSuccess + "\n requestAction = " + + requestAction  + "\n dataPacket ==> \n" + content);
		
	}

	private void onRegisterResult( ResponseDataPacket dataPacket){
		PublicType.UserRegisterResult object = new PublicType.UserRegisterResult();
		
		try {
			object.parseJson(dataPacket.data);
			log.e("keys = " + object.mKeys);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void onLoginResult( ResponseDataPacket dataPacket){
		PublicType.UserLoginResult object = new PublicType.UserLoginResult();
		
		try {
			object.parseJson(dataPacket.data);
			log.e("mIsAdmin = " + object.mIsAdmin + "\nmAdType = " + object.mAdType + "\ndatalist.size = " + object.mDataList.size());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void onGetInfoResult( ResponseDataPacket dataPacket){
		PublicType.GetInfoResult object = new PublicType.GetInfoResult();
		
		try {
			object.parseJson(dataPacket.data);
			log.e("mDataList.size = " + object.mDataList.size());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
