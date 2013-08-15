package com.geniusgithub.lookaround.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.geniusgithub.lookaround.FragmentControlCenter;
import com.geniusgithub.lookaround.FragmentModel;
import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.R.dimen;
import com.geniusgithub.lookaround.R.drawable;
import com.geniusgithub.lookaround.R.id;
import com.geniusgithub.lookaround.R.layout;
import com.geniusgithub.lookaround.adapter.NavChannelAdapter;
import com.geniusgithub.lookaround.fragment.CommonFragmentEx;
import com.geniusgithub.lookaround.fragment.NavigationFragment;
import com.geniusgithub.lookaround.fragment.NavigationFragment;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainLookAroundActivity extends SlidingFragmentActivity implements OnClickListener{

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setupViews();
		
		initData();
	}
	
	
	
	private void setupViews(){

		setContentView(R.layout.main_slidemenu_layout);
		
		initActionBar();
		
		initSlideMenu();
	
	}
	
	private void initSlideMenu(){

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
}

