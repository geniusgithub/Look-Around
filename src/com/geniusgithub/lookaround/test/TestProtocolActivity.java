package com.geniusgithub.lookaround.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.view.ext.SatelliteMenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.animation.MyAnimations;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
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

public class TestProtocolActivity extends Activity implements OnClickListener, IRequestDataPacketCallback, IRequestContentCallback{

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnRegiste;
	private Button mBtnLogin;
	private Button mBtnBindToken;
	private Button mBtnAdClick;
	private Button mBtnAbout;
	private Button mBtnGetinfo;
	private Button mBtnDelinfo;
	private Button mBtnUpdate;
	private Button mBtnDialog;
	private Button mBtnTest;
	
	private ClientEngine mClientEngine;

	private SatelliteMenu SatelliteMenu; 
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
		mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
		mBtnDialog = (Button) findViewById(R.id.btnDialog);
		mBtnTest = (Button) findViewById(R.id.btnTest);
		
		mBtnRegiste.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mBtnBindToken.setOnClickListener(this);
		mBtnAdClick.setOnClickListener(this);
		mBtnAbout.setOnClickListener(this);
		mBtnGetinfo.setOnClickListener(this);
		mBtnDelinfo.setOnClickListener(this);
		mBtnUpdate.setOnClickListener(this);
		mBtnDialog.setOnClickListener(this);
		mBtnTest.setOnClickListener(this);
			
		SatelliteMenu = (SatelliteMenu) findViewById(R.id.SatelliteMenu);	    
		  
	}
	
	private void initData(){
		mClientEngine=  ClientEngine.getInstance(this);	
		   
	
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
//        items.add(new SatelliteMenuItem(4, R.drawable.ic_1));
//        items.add(new SatelliteMenuItem(4, R.drawable.ic_3));
//        items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
//        items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
//        items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
//        items.add(new SatelliteMenuItem(1, R.drawable.ic_2));

        SatelliteMenu.addItems(items);        
        
        SatelliteMenu.setOnItemClickedListener(new SateliteClickedListener() {
			
			public void eventOccured(int id) {
				Log.e("sat", "Clicked on " + id);
			}
		});
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
			case R.id.btnUpdate:
				update();
				break;
			case R.id.btnDialog:
				dialog();
				break;
			case R.id.btnTest:
				test();
				break;
		}
	}
	
	private void test(){
		String mac = CommonUtil.getMacAdress(this);
		log.e("mac = " + mac);
		String imsi = CommonUtil.getIMSI(this);
		log.e("imsi = " + imsi);
		String providerString = CommonUtil.getProvidersName(this);
		log.e("providerString = " + providerString);
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

	private void update(){
		log.e("update");
		PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(this);
		
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.action = PublicType.CHECK_UPDATE_MSGID;
		packet.object = object;
		
		mClientEngine.httpGetRequestEx(packet, this);
	}
	
	Dialog dialog = null;
	private void dialog(){
		log.e("dialog");
	
		 dialog = DialogBuilder.buildNormalDialog(this, "update title", "update message", new IDialogInterface() {
			
			@Override
			public void onSure() {
				log.e("onSure");
				dialog.dismiss();
			}

			@Override
			public void onCancel() {
				log.e("onCancel");
				dialog.dismiss();
			}
			
		
		});
		 
		 dialog.show();
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
			case PublicType.CHECK_UPDATE_MSGID:
				onGetCheckUpdate(dataPacket);
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
	
	private void onGetCheckUpdate( ResponseDataPacket dataPacket){
		PublicType.CheckUpdateResult object = new PublicType.CheckUpdateResult();
		
		try {
			object.parseJson(dataPacket.data);
			log.e("mHaveNewVer = " + object.mHaveNewVer +  "\nmVerCode = " + object.mVerCode + 
					"\nmVerName = " + object.mVerName + "\nmAppUrl = " + object.mAppUrl + "\nmContent.sizea = " + object.mContentList.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	

}
