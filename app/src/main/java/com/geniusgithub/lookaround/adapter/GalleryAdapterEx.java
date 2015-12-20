package com.geniusgithub.lookaround.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.cache.ImageLoaderEx;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;
import com.geniusgithub.lookaround.widget.ImageViewEx;

public class GalleryAdapterEx extends BaseAdapter {
	
	private static final CommonLog log = LogFactory.createLog();
	
	private Context context;
	private List<String> mItems = new ArrayList<String>();
	private ImageLoaderEx mImageLoader;
	
	
	public void setData(List<String> data) {
		this.mItems = data;
		notifyDataSetChanged();
	}
	
	public GalleryAdapterEx(Context context) {
		this.context = context;

		mImageLoader = new ImageLoaderEx(context);
		int size =Math.min( CommonUtil.getScreenWidth(context), CommonUtil.getScreenHeight(context));
		mImageLoader.setScaleParam(size);
		mImageLoader.setDefaultBitmap(context.getResources().getDrawable(R.drawable.load_img));
		mImageLoader.setLoadLocalBitmapQuick(true);
	}
	
	@Override
	public int getCount() {
		return mItems != null ? mItems.size() : 0;
	}
	
	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageViewEx view  = null;
		if (convertView == null){
			view = new ImageViewEx(context);
		}else{
			view = (ImageViewEx) convertView;
		}

		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	
		String url = mItems.get(position);
		log.e("pos = " + position + ", url = " + url);
		
		view.setDefaultBitmapFlag(true);
		boolean ret = mImageLoader.DisplayImage(url, view, true);
		view.setDefaultBitmapFlag(!ret);
		
		view.setURL(url);
		
		return view;
	}
	
	public boolean syncRefreshImageViewEx(ImageViewEx imageViewEx){
		return mImageLoader.syncLoadBitmap(imageViewEx.getURL(), imageViewEx);
	}

}

