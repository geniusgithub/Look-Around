package com.geniusgithub.lookaround.activity.set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseActivityEx;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class AdviseActivity extends BaseActivityEx implements Callback , TextWatcher, PlatformActionListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	
	private static final int MAX_TEXT_LENGTH = 100;

	private Toolbar toolbar;
	private EditText mETContent;
	private TextView mTVTarget;
	private TextView mTVLive;
	

	private int notifyIcon;
	private String notifyTitle;
	private String sharePath;
	
	private Platform mPlatform;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advise_layout);
		setupViews();
		initData();
	}



	@Override
	protected void onDestroy() {
		

		
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.advise_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return  super.onPrepareOptionsMenu(menu);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
			case R.id.menu_advise:
				share();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	
	private void setupViews(){

		initToolBar();
		setNotification(R.drawable.logo_icon,"Look Around");

		mETContent = (EditText) findViewById(R.id.et_content);
		mTVTarget = (TextView) findViewById(R.id.tv_target);
		mTVLive = (TextView) findViewById(R.id.tv_live);
		mETContent.addTextChangedListener(this);

	}

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.advise);
		setSupportActionBar(toolbar);

		final ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
	}

	private void initData(){

		mPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);		
		PlatformDb db = mPlatform.getDb();
		String nickname = db.get("nickname");
		if (nickname != null){
			mTVTarget.setText(nickname);
		}
		updateTVLive();
	}
	
	
	/** 分享时Notification的图标和文字 */
	public void setNotification(int icon, String title) {
		notifyIcon = icon;
		notifyTitle = title;
	}

	
	/** 执行分享 */
	public void share() {
			boolean started = false;	
			
			int relen = MAX_TEXT_LENGTH -  mETContent.length();
			if (relen == MAX_TEXT_LENGTH){
				CommonUtil.showToast(R.string.toast_no_txtcount, this);
				return ;
			}
			
			if (relen < 0){
				CommonUtil.showToast(R.string.toast_too_txtcount, this);
				return ;
			}

			Toast.makeText(this, "功能暂时屏蔽，敬请谅解", Toast.LENGTH_SHORT).show();

		/*	int shareType = Platform.SHARE_TEXT;
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("shareType", shareType);
			
			String sendContent = getSendContent();
			log.e("sendContent = " + sendContent);
			reqMap.put("text", sendContent);
			
			if (!started) {
				started = true;
				showNotification(2000, getString(R.string.sharing));
				finish();
			}
			
			mPlatform.setPlatformActionListener(this);
			ShareCore shareCore = new ShareCore();
			shareCore.share(mPlatform, reqMap);*/
	
	}
	
	public String getSendContent(){
		String value = mETContent.getText().toString();
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("@android火星人 ");
		sBuffer.append("#意见与反馈#");
		sBuffer.append("\n");
		sBuffer.append(getMobileInfo());
		sBuffer.append(value);
		return sBuffer.toString();
	}
	
	public String getMobileInfo(){
		String value = "(版本" + CommonUtil.getSoftVersion(this) + 
					",厂商" + CommonUtil.getDeviceManufacturer() + 
					", 型号" + CommonUtil.getDeviceModel() + 
					", 系统" + CommonUtil.getOSVersion() + ")";
		return value;
	}


	
	@Override
	public boolean handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_TOAST: {
				String text = String.valueOf(msg.obj);
				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
			case MSG_ACTION_CCALLBACK: {
				switch (msg.arg1) {
					case 1: { // 成功
						showNotification(2000, getString(R.string.share_completed));
					}
					break;
					case 2: { // 失败
						showNotification(2000, getString(R.string.share_failed));
					}
					break;
					case 3: { // 取消
						showNotification(2000, getString(R.string.share_canceled));
					}
					break;
				}
			}
			break;
			case MSG_CANCEL_NOTIFY: {
				NotificationManager nm = (NotificationManager) msg.obj;
				if (nm != null) {
					nm.cancel(msg.arg1);
				}
			}
			break;
		}
		return false;
	}
		
	// 在状态栏提示分享操作
	private void showNotification(long cancelTime, String text) {
				try {
					NotificationManager nm = (NotificationManager) 
							getSystemService(Context.NOTIFICATION_SERVICE);
					final int id = Integer.MAX_VALUE / 13 + 1;
					nm.cancel(id);

					long when = System.currentTimeMillis();
					Notification.Builder builder = new Notification.Builder(this);
					builder.setSmallIcon(notifyIcon);
					builder.setContentText(text);
					builder.setWhen(when);
					builder.setContentTitle(notifyTitle);
					PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(), 0);
					builder.setContentIntent(pi);
					builder.setAutoCancel(true);
					nm.notify(id, builder.build());

					if (cancelTime > 0) {
						Message msg = new Message();
						msg.what = MSG_CANCEL_NOTIFY;
						msg.obj = nm;
						msg.arg1 = id;
						UIHandler.sendMessageDelayed(msg, cancelTime, this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		log.e("onComplete Platform = " + platform.getName() + ", action = " + action);
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		log.e("onError Platform = " + platform.getName() + ", action = " + action);
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);

		// 分享失败的统计
		ShareSDK.logDemoEvent(4, platform);
	}

	public void onCancel(Platform platform, int action) {
		log.e("onCancel Platform = " + platform.getName() + ", action = " + action);
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		updateTVLive();
	}

	private void updateTVLive(){
		int remain = MAX_TEXT_LENGTH - mETContent.length();
		mTVLive.setText("您还可以输入" + String.valueOf(remain) + "字");
		mTVLive.setTextColor(remain > 0 ? 0xffcfcfcf : 0xffff0000);
	}
}
