package com.geniusgithub.lookaround.content;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.BaseActivity;
import com.geniusgithub.lookaround.cache.FileCache;
import com.geniusgithub.lookaround.cache.SimpleImageLoader;
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.datastore.DaoMaster.DevOpenHelper;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.weibo.sdk.ShareItem;
import com.geniusgithub.lookaround.weibo.sdk.ShareActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends BaseActivity implements OnClickListener, SateliteClickedListener{

	private static final CommonLog log = LogFactory.createLog();
		
	private final static int SINA_ID = 1;
	private final static int TENCENT_ID = 2;
	private final static int WECHAT_ID = 3;
	private final static int WECHAT_MOM_ID = 4;
	private final static int QZONE = 5;

	private Button mBtnBack;
	private Button mBtnCollect;
	private Button mBtnReadOrign;
	private TextView mTVBarTitle;
	private TextView mTVTitle;
	private TextView mTVArtist;
	private TextView mTVContent;
	private TextView mTVTime;
	private TextView mTVSource;
	private ImageView mIVContent;
	

	private BaseType.ListItem mTypeItem = new BaseType.ListItem();
	private BaseType.InfoItemEx mInfoItem = new BaseType.InfoItemEx();
	
	private SimpleImageLoader mImageLoader;
	
	private SatelliteMenu SatelliteMenu; 
	
	
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private InfoItemDao infoItemDao;
    private SQLiteDatabase db;
    private boolean isCollect = false;
    private boolean loginStatus = false;
    
    private FileCache fileCache = new FileCache(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginStatus = LAroundApplication.getInstance().getLoginStatus();
		if (!loginStatus){
			log.e("loginStatus is false ,jump to welcome view!!!");		
			LAroundApplication.getInstance().startToMainActivity();
			finish();
			return ;
		}
		
		setupViews();	
		initData();

	}

	@Override
	protected void onDestroy() {
		
		if (loginStatus){
			db.close();
		}
		
		
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
		mTVContent = (TextView) findViewById(R.id.tv_content);
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
		
		//log.e("infoItem --> \n" + mInfoItem.toString());
		
		mTVBarTitle.setText(mTypeItem.mTitle);
		mTVTitle.setText(mInfoItem.mTitle);
		mTVArtist.setText(mInfoItem.mUserName);
		mTVContent.setText(mInfoItem.mContent);
		mTVTime.setText(mInfoItem.mTime);
		mTVContent.setText(mInfoItem.mContent);
		
		mImageLoader.DisplayImage(mInfoItem.getImageURL(0), mIVContent);
		if (mInfoItem.getThumnaiImageCount() == 0){
			mIVContent.setVisibility(View.GONE);
		}

		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(SINA_ID, R.drawable.logo_sina));
        items.add(new SatelliteMenuItem(TENCENT_ID, R.drawable.logo_tencentweibo));
        items.add(new SatelliteMenuItem(WECHAT_ID, R.drawable.logo_wechat));
        items.add(new SatelliteMenuItem(WECHAT_MOM_ID, R.drawable.logo_wechatmoments));
        items.add(new SatelliteMenuItem(QZONE, R.drawable.logo_qzone));


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

        isCollect = infoItemDao.isCollect(mInfoItem);
        if (isCollect){
        	mBtnCollect.setVisibility(View.GONE);
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
				goWebviewActivity();
				break;
			case R.id.iv_content:
				goPhoneView();
				break;

		}
	}
	

	private void collect(){
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseType.ListItem.KEY_TYPEID, mTypeItem.mTypeID);
		map.put(BaseType.ListItem.KEY_TITLE, mInfoItem.mTitle);
		LAroundApplication.getInstance().onEvent("UM0011", map);
		
		infoItemDao.insert(mInfoItem);
		CommonUtil.showToast(R.string.toast_collect_success, this);
		mBtnCollect.setVisibility(View.GONE);
	}
	
	private void goWebviewActivity(){
		log.e("goWebviewActivity ");
		LAroundApplication.getInstance().onEvent("UMID0009");
		Intent intent = new Intent();
		intent.setClass(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, mInfoItem.mSourceUrl);
		startActivity(intent);
	}
	
	private void goPhoneView(){
		log.e("goPhoneView ");
		LAroundApplication.getInstance().onEvent("UMID0003");
		Intent intent = new Intent();
		intent.setClass(this, PictureBrowerActivity.class);
		startActivity(intent);
	}

	@Override
	public void eventOccured(int id) {
		log.e("Clicked on " + id);
		
		switch(id){
			case SINA_ID:
				shareToSina();
				break;
			case TENCENT_ID:
				shareToTencent();
				break;
			case WECHAT_ID:
				shareToWChat();
				break;
			case WECHAT_MOM_ID:
				shareToWFriend();
				break;
			case QZONE:
				shareToQZone();
				break;
		}
	}

	private void shareToSina(){
		LAroundApplication.getInstance().onEvent("UMID0008");
		ShareItem.clear();
		
		String imageURL = mInfoItem.getImageURL(0);
			
		ShareItem.setText(mInfoItem.mContent);
		if (imageURL != null){
			ShareItem.setImageUrl(imageURL);
			String sharPath = fileCache.getSavePath(imageURL);
			ShareItem.setShareImagePath(sharPath);
		}
		ShareItem.setPlatform(SinaWeibo.NAME);		
		goShareActivity();
	}
	
	private void shareToTencent(){
		ShareItem.clear();
		
		String imageURL = mInfoItem.getImageURL(0);
		
		ShareItem.setTitle(mInfoItem.mTitle);
	//	ShareItem.setTitleUrl("http://blog.csdn.net/lancees");
		ShareItem.setText(mInfoItem.mContent);
		if (imageURL != null){
			String sharPath = fileCache.getSavePath(imageURL);
			ShareItem.setImagePath(sharPath);
			ShareItem.setShareImagePath(sharPath);
			
		}
	
		ShareItem.setPlatform(TencentWeibo.NAME);	
	
		goShareActivity();
	}
	
	private void shareToQZone(){
		ShareItem.clear();
		
		String imageURL = mInfoItem.getImageURL(0);
		
		ShareItem.setTitle(mInfoItem.mTitle);
		ShareItem.setTitleUrl(mInfoItem.mSourceUrl);
		ShareItem.setText(mInfoItem.mContent);
		if (imageURL != null){
			ShareItem.setImageUrl(imageURL);
			String sharPath = fileCache.getSavePath(imageURL);
			ShareItem.setShareImagePath(sharPath);
		}
		ShareItem.setPlatform(QZone.NAME);
	
		goShareActivity();
	}
	
	private void shareToWChat(){
		CommonUtil.showToast(R.string.toast_nosupport_weixin, this);
	}
	
	private void shareToWFriend(){
		CommonUtil.showToast(R.string.toast_nosupport_friend, this);
	}


	private void goShareActivity(){
		
		
		Intent intent = new Intent();
		intent.setClass(this, ShareActivity.class);
		startActivity(intent);
	}

}
