package com.geniusgithub.lookaround;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.geniusgithub.lookaround.fragment.NavigationFragment;
import com.geniusgithub.lookaround.fragment.SettingFragment;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainLookAroundActivity extends SlidingFragmentActivity implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private String mTitle;
	private Fragment mContent;
	
	private ImageView mLeftIcon;
	private ImageView mRightIcon;
	private TextView mTitleTextView;
	
	private FragmentControlCenter mControlCenter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mControlCenter = FragmentControlCenter.getInstance(this);
		
		setupViews();
		
		initData();
	}
	
	
	
	private void setupViews(){

		setContentView(R.layout.main_slidemenu_layout);
		
		initActionBar();
		
		initSlideMenu();
	
	}
	
	private void initSlideMenu(){
		FragmentModel fragmentModel = mControlCenter.getMirrorFragmentModel();
		switchContent(fragmentModel);

		
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);

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

		
		sm.setSecondaryMenu(R.layout.right_menu_frame);
		sm.setSecondaryShadowDrawable(R.drawable.shadow);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.right_menu_frame, new SettingFragment())
		.commit();
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
		
	}
	
	
	public void switchContent(final FragmentModel fragment) {
		mTitle = fragment.mTitle;
		mContent = fragment.mFragment;

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
		
		mTitleTextView.setText(mTitle);
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_left_icon:
			toggle();
			break;
		case R.id.iv_right_icon:
			showSecondaryMenu();
			break;
		}
	}	

}

