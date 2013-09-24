package com.geniusgithub.lookaround.content;

import java.util.List;
import java.util.logging.FileHandler;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.GalleryAdapterEx;
import com.geniusgithub.lookaround.cache.FileCache;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.FileHelper;
import com.geniusgithub.lookaround.util.FileManager;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.ImageViewEx;
import com.geniusgithub.lookaround.widget.PicGallery;


import android.R.integer;
import android.R.mipmap;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class PictureBrowerActivity extends Activity implements OnItemSelectedListener,
													OnClickListener
{
	private static final CommonLog log = LogFactory.createLog();
	// 屏幕宽度
	public static int screenWidth;
	// 屏幕高度
	public static int screenHeight;
	

	private Button mBtnBack;
	private Button mBtnSave;
	
	private PicGallery gallery;
	private GalleryAdapterEx mAdapter;

	private BaseType.InfoItem mItem = new BaseType.InfoItem();
	private int mCurPos = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		log.e("PictureBrowerActivity onCreate");
		setContentView(R.layout.picture_view_activity);

		initViews();
		initData();
	}

	@SuppressWarnings("deprecation")
	private void initViews() {

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();	

		gallery = (PicGallery)findViewById(R.id.pic_gallery);
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setDetector(new GestureDetector(this, new MySimpleGesture()));

		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnSave = (Button) findViewById(R.id.btn_right);
		mBtnSave.setOnClickListener(this);
	}
	
	
	private void initData(){
		ContentCache mContentCache = ContentCache.getInstance();
		mItem = mContentCache.getInfoItem();
		List<String> list = mItem.mImageUrlList;
		log.e("mItem.mImageUrlList.size = " + mItem.mImageUrlList.size());
		
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
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_right:
			save();
			break;
		}
	}
	
	
	private void save(){
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