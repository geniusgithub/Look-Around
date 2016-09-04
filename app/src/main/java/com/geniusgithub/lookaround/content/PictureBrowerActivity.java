package com.geniusgithub.lookaround.content;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.GalleryAdapterEx;
import com.geniusgithub.lookaround.base.BaseActivityEx;
import com.geniusgithub.lookaround.cache.FileCache;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.FileHelper;
import com.geniusgithub.lookaround.util.FileManager;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.ImageViewEx;
import com.geniusgithub.lookaround.widget.PicGallery;

import java.util.List;

public class PictureBrowerActivity extends BaseActivityEx implements OnItemSelectedListener{
	private static final CommonLog log = LogFactory.createLog();
	// 屏幕宽度
	public static int screenWidth;
	// 屏幕高度
	public static int screenHeight;

	private Toolbar toolbar;
	private PicGallery gallery;

	
	private GalleryAdapterEx mAdapter;

	private BaseType.InfoItem mItem = new BaseType.InfoItem();
	private int mCurPos = 0;
	private int mTotalNum = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		log.e("PictureBrowerActivity onCreate");
		setContentView(R.layout.picture_view_activity);

		initViews();
		initData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.browse_options_menu, menu);
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
			case R.id.menu_download:
				save();
				break;
		}

		return super.onOptionsItemSelected(item);
	}


	@SuppressWarnings("deprecation")
	private void initViews() {
		initToolBar();

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();


		gallery = (PicGallery)findViewById(R.id.pic_gallery);
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setDetector(new GestureDetector(this, new MySimpleGesture()));

		
	}


	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setBackgroundColor(Color.parseColor("#00ffffff"));
		setSupportActionBar(toolbar);

		final ActionBar ab = getSupportActionBar();
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
	}
	
	
	private void initData(){
		ContentCache mContentCache = ContentCache.getInstance();
		mItem = mContentCache.getInfoItem();
		List<String> list = mItem.mImageUrlList;
		log.e("mItem.mImageUrlList.size = " + mItem.mImageUrlList.size());
		mTotalNum = mItem.mImageUrlList.size();
		mAdapter = new GalleryAdapterEx(this);
		gallery.setAdapter(mAdapter);
		gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				return false;
			}
		});
	
		mAdapter.setData(list);
		
		gallery.setOnItemSelectedListener(this);
		
		updateTitle(0);
	}
	
	private void updateTitle(int pos){
		toolbar.setTitle(String.valueOf(pos + 1) + "/" + String.valueOf(mTotalNum));
	}
	
	private class MySimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {

			View view = gallery.getSelectedView();
			if (view instanceof ImageViewEx) {
				ImageViewEx imageView = (ImageViewEx) view;
				if (imageView.getScale() > imageView.getMiniZoom()) {
					imageView.zoomTo(imageView.getMiniZoom());
				} else {
					imageView.zoomTo(imageView.getMaxZoom());
				}

			} else {

			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// Logger.LOG("onSingleTapConfirmed",
			// "onSingleTapConfirmed excute");
			// mTweetShow = !mTweetShow;
			// tweetLayout.setVisibility(mTweetShow ? View.VISIBLE
			// : View.INVISIBLE);
			return true;
		}
	}



	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int pos,
			long arg3) {
		
		log.e("onItemSelected pos = " + pos);
		
		mCurPos = pos;
		
		ImageViewEx imageViewEx = (ImageViewEx) view;
		boolean isDefaultBitmap = imageViewEx.getDefaultBitmapFlag();
		if (isDefaultBitmap){
			mAdapter.syncRefreshImageViewEx(imageViewEx);
		}
		
		updateTitle(mCurPos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void save(){
		LAroundApplication.getInstance().onEvent("SAVE01");
		boolean isSDCard = CommonUtil.hasSDCard();
		if (!isSDCard){
			CommonUtil.showToast(R.string.toast_save_fail, this);
			return ;
		}
		
		if (mCurPos >= mItem.mImageUrlList.size()){
			
			return ;
		}
		
		if (!FileHelper.createDirectory(FileManager.getDownloadFileSavePath())){
			
			return ;
		}
		
		
		FileCache fileCache = new FileCache(this);
		String url = mItem.mImageUrlList.get(mCurPos);	
		String fromPath = fileCache.getSavePath(url);

		
		String toPath = FileManager.getDownloadFileSavePath() + String.valueOf(url.hashCode());
		boolean ret = FileHelper.saveBitmap(fromPath, toPath);
		if (ret){
			String text = getResources().getString(R.string.toast_save_success) + "," + 
						getResources().getString(R.string.toast_savefile_end) + toPath + ".jpg";
			
			CommonUtil.showToast(text, this);
		}else{
			CommonUtil.showToast(R.string.toast_save_fail2, this);
		}
			
	}
}