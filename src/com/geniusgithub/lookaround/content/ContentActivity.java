package com.geniusgithub.lookaround.content;


import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
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
		}
	}
	

	private void collect(){
		
	}
	
	private void readOrign(){
		
	}

}
