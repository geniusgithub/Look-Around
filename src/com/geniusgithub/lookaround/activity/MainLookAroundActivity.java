package com.geniusgithub.lookaround.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.geniusgithub.lookaround.FragmentControlCenter;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.set.SettingActivity;
import com.geniusgithub.lookaround.adapter.NavChannelAdapter;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.fragment.CommonFragmentEx;
import com.geniusgithub.lookaround.fragment.NavigationFragment;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;


public class MainLookAroundActivity extends SlidingFragmentActivity implements OnClickListener, IDialogInterface{

	private static final CommonLog log = LogFactory.createLog();
	
	private CommonFragmentEx mContentFragment;
	
	private ImageView mLeftIcon;
	private ImageView mRightIcon;
	private TextView mTitleTextView;
	
	private FragmentControlCenter mControlCenter;
	
	private List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
	private NavChannelAdapter mAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.e("MainLookAroundActivity  onCreate!!!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_slidemenu_layout);	
		boolean loginStatus = LAroundApplication.getInstance().getLoginStatus();
		if (!loginStatus){
			log.e("loginStatus is false ,jump to welcome view!!!");		
			LAroundApplication.getInstance().startToWelcomeActivity();
			finish();
			return ;
		}
		
		setupViews();	
	
		initData();
		
		MobclickAgent.onError(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		MobclickAgent.onResume(this);
	}

	private void setupViews(){
		
		initActionBar();
		
		initSlideMenu();
	
	}
	
	private void initSlideMenu(){
		log.e("MainLookAroundActivity initSlideMenu"); 
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);

		setBehindContentView(R.layout.left_menu_frame);
		sm.setSlidingEnabled(true);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.left_menu_frame, new NavigationFragment())
		.commit();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindScrollScale(0);
		sm.setFadeDegree(0.25f);

		
	}
	
	private void initActionBar(){
	
		ActionBar actionBar = getSupportActionBar();

		actionBar.setCustomView(R.layout.actionbar_layout);

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		
		mLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
		mRightIcon = (ImageView) findViewById(R.id.iv_right_icon);
		mLeftIcon.setOnClickListener(this);
		mRightIcon.setOnClickListener(this);
		
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
	}
	
	private void initData(){
		mDataList = LAroundApplication.getInstance().getUserLoginResult().mDataList;
		mControlCenter = FragmentControlCenter.getInstance(this);
		
		
		int size = mDataList.size();
		if (size > 0){
			mContentFragment = mControlCenter.getCommonFragmentEx(mDataList.get(0));
			switchContent(mContentFragment);
		}
	}
	
	
	public void switchContent(final CommonFragmentEx fragment) {
		mContentFragment = fragment;

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContentFragment)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
		
		mTitleTextView.setText(mContentFragment.getData().mTitle);
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_left_icon:
			toggle();
			break;
		case R.id.iv_right_icon:
			goSettingActivity();
			break;
		}
	}	

	
	private void goSettingActivity(){
		Intent intent = new Intent();
		intent.setClass(this, SettingActivity.class);
		startActivity(intent);
	}
	
	
	@Override
	public void onBackPressed() {
		if (exitDialog != null){
			exitDialog.dismiss();
		}
		
		exitDialog = getExitDialog();
		exitDialog.show();
	}
	
	
	private Dialog exitDialog;
	private Dialog getExitDialog(){
		Dialog dialog = DialogBuilder.buildNormalDialog(this,
				getResources().getString(R.string.dia_msg_exit_title),
				getResources().getString(R.string.dia_msg_exit_msg),
				this);
		return dialog;
	}



	@Override
	public void onSure() {
		if (exitDialog != null){
			exitDialog.dismiss();
		}
		finish();
	}



	@Override
	public void onCancel() {
		if (exitDialog != null){
			exitDialog.dismiss();
		}
		
	}

}

