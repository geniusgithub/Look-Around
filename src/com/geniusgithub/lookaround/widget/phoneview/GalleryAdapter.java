package com.geniusgithub.lookaround.widget.phoneview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

public class GalleryAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<MyImageView> imageViews = new ArrayList<MyImageView>();

	private ImageCacheManager imageCache;

	private List<String> mItems;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.obj;
			Bundle bundle = msg.getData();
			String url = bundle.getString("url");
			for (int i = 0; i < imageViews.size(); i++) {
				if (imageViews.get(i).getTag().equals(url)) {
					imageViews.get(i).setImageBitmap(bitmap);
				}
			}
		}
	};

	public void setData(List<String> data) {
		this.mItems = data;
		notifyDataSetChanged();
	}

	public GalleryAdapter(Context context) {
		this.context = context;
		imageCache = ImageCacheManager.getInstance(context);
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
		MyImageView view = new MyImageView(context);
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		String item = mItems.get(position);
		if (item != null) {
			Bitmap bmp;
			try {
				bmp = imageCache.get(item);
				view.setTag(item);
				if (bmp != null) {
					view.setImageBitmap(bmp);
				} 
				if (!this.imageViews.contains(view)) {
					imageViews.add(view);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return view;
	}


}
