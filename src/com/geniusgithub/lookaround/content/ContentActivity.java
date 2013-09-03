package com.geniusgithub.lookaround.content;


import java.util.ArrayList;
import java.util.List;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.InfoContentExAdapter;
import com.geniusgithub.lookaround.animation.MyAnimations;
import com.geniusgithub.lookaround.cache.ImageLoaderEx;
import com.geniusgithub.lookaround.cache.SimpleImageLoader;
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.datastore.DaoMaster.DevOpenHelper;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.network.ClientEngine;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentActivity extends Activity implements OnClickListener, SateliteClickedListener{

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
	
	private SatelliteMenu SatelliteMenu; 
	
	
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private InfoItemDao infoItemDao;
    private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupViews();	
		initData();

	}

	@Override
	protected void onDestroy() {
		
		db.close();
		
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
		mIVContent.setOnClickListener(this);
		
		SatelliteMenu = (SatelliteMenu) findViewById(R.id.SatelliteMenu);	   
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

		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(4, R.drawable.ic_1));
        items.add(new SatelliteMenuItem(4, R.drawable.ic_3));
        items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
        items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
        items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
        items.add(new SatelliteMenuItem(1, R.drawable.ic_2));

        SatelliteMenu.addItems(items);        	        
        SatelliteMenu.setOnItemClickedListener(this);
        
        inidDataBase();
	}
	
	private void inidDataBase(){
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lookaround-db", null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        infoItemDao = daoSession.getInfoItemDao();

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
			case R.id.iv_content:
				goPhoneView();
				break;

		}
	}
	

	private void collect(){
		BaseType.InfoItemEx itemEx = new BaseType.InfoItemEx(mInfoItem, mTypeItem);
		infoItemDao.insert(itemEx);
		CommonUtil.showToast(R.string.toast_collect_success, this);
		
	}
	
	private void readOrign(){
		
	}
	
	private void goPhoneView(){
		log.e("goPhoneView ");
		
		Intent intent = new Intent();
		intent.setClass(this, PictureBrowerActivity.class);
		startActivity(intent);
	}

	@Override
	public void eventOccured(int id) {
		log.e("Clicked on " + id);
	}
	
	

}
