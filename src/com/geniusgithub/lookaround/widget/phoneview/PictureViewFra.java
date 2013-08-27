package com.geniusgithub.lookaround.widget.phoneview;

import java.util.List;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PictureViewFra extends Fragment implements LoaderCallbacks<List<String>> {
	private static final CommonLog log = LogFactory.createLog();
	private PicGallery gallery;
	// private ViewGroup tweetLayout; // 弹层
	private boolean mTweetShow = false; // 弹层是否显示

	private GalleryAdapter mAdapter;

	// private ProgressDialog mProgress;

	public GalleryAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.picture_view, null);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		

		gallery = (PicGallery) view.findViewById(R.id.pic_gallery);
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setDetector(new GestureDetector(getActivity(),
				new MySimpleGesture()));
		mAdapter = new GalleryAdapter(getActivity());
		gallery.setAdapter(mAdapter);
		gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getActivity(), "LongClick唤起复制、保存操作",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		getLoaderManager().initLoader(0, null, this);

		// mProgress = ProgressDialog.show(getActivity(),
		// null,getActivity().getString(R.string.loading));
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {

			View view = gallery.getSelectedView();
			if (view instanceof MyImageView) {
				MyImageView imageView = (MyImageView) view;
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
	public Loader<List<String>> onCreateLoader(int arg0, Bundle arg1) {
		log.e("onCreateLoader");
		return new PictureLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<String>> arg0, List<String> arg1) {
		log.e("onLoadFinished");
		mAdapter.setData(arg1);
	}

	@Override
	public void onLoaderReset(Loader<List<String>> arg0) {
		log.e("onLoaderReset");
		mAdapter.setData(null);
	}



}
