package com.geniusgithub.lookaround.content;


import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.animation.MyAnimations;
import com.geniusgithub.lookaround.cache.ImageLoaderEx;
import com.geniusgithub.lookaround.cache.SimpleImageLoader;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentActivity extends Activity implements OnClickListener{

private static final CommonLog log = LogFactory.createLog();
	
	private Button mBtnBack;
	private Button mBtnCollect;
	private Button mBtnReadOrign;
	private TextView mTVBarTitle;
	private TextView mTVTitle;
	private TextView mTVArtist;
	private TextView mTVTime;
	private TextView mTVSource;
	private ImageView mIVContent;
	

	private BaseType.ListItem mTypeItem = new BaseType.ListItem();
	private BaseType.InfoItem mInfoItem = new BaseType.InfoItem();
	
	private SimpleImageLoader mImageLoader;
	
	
	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupViews();	
		initData();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	private void setupViews(){
		setContentView(R.layout.info_content_layout);
		
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnCollect = (Button) findViewById(R.id.btn_right);
		mBtnReadOrign = (Button) findViewById(R.id.btn_readorign);
		mTVBarTitle = (TextView) findViewById(R.id.tv_bartitle);
		mTVTitle = (TextView) findViewById(R.id.tv_title);
		mTVArtist = (TextView) findViewById(R.id.tv_artist);
		mTVTime = (TextView) findViewById(R.id.tv_time);
		mTVSource = (TextView) findViewById(R.id.tv_source);
		mIVContent = (ImageView) findViewById(R.id.iv_content);
		
		mBtnBack.setOnClickListener(this);
		mBtnCollect.setOnClickListener(this);
		mBtnReadOrign.setOnClickListener(this);
		
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
		composerButtonsShowHideButton.setOnClickListener(this);
	}
	
	private void initData(){
		mImageLoader = new SimpleImageLoader(this);
		
		ContentCache mContentCache = ContentCache.getInstance();
		mTypeItem = mContentCache.getTypeItem();
		mInfoItem = mContentCache.getInfoItem();
		
		mTVBarTitle.setText(mTypeItem.mTitle);
		mTVTitle.setText(mInfoItem.mTitle);
		mTVArtist.setText(mInfoItem.mUserName);
		mTVTime.setText(mInfoItem.mTime);
		
		mImageLoader.DisplayImage(mInfoItem.getImageURL(0), mIVContent);

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
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_right:
				collect();
				break;
			case R.id.btn_readorign:
				readOrign();
				break;
			case R.id.composer_buttons_show_hide_button:
				toggle();
				break;
		}
	}
	

	private void collect(){
		
	}
	
	private void readOrign(){
		
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

}
