package com.geniusgithub.lookaround.content;

import java.util.List;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.GalleryAdapterEx;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.ImageViewEx;
import com.geniusgithub.lookaround.widget.PicGallery;


import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class PictureBrowerActivity extends Activity implements OnItemSelectedListener
{
	private static final CommonLog log = LogFactory.createLog();
	// 屏幕宽度
	public static int screenWidth;
	// 屏幕高度
	public static int screenHeight;
	

	private PicGallery gallery;
	private GalleryAdapterEx mAdapter;

	private BaseType.InfoItem mItem = new BaseType.InfoItem();
	

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
	
}