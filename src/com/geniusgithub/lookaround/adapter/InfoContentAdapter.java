package com.geniusgithub.lookaround.adapter;

import java.util.ArrayList;
import java.util.List;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.cache.ImageLoader;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoContentAdapter extends BaseAdapter{

	private static final CommonLog log = LogFactory.createLog();
	
	private List<BaseType.InfoItem> data = new ArrayList<BaseType.InfoItem>();
	private Context mContext;
	private boolean mBusy = false;
	private ImageLoader mImageLoader;
	
	public InfoContentAdapter(Context context, List<BaseType.InfoItem> data)
	{
		mContext = context;
		this.data = data;
		mImageLoader = new ImageLoader(context);
	}
	
	public void refreshData(List<BaseType.InfoItem> data)
	{
		this.data = data;
		notifyDataSetChanged();
		
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}
	
	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int pos) {

		return data.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
	
		return 0;
	}
	
	

	@Override
	public int getItemViewType(int position) {	
	
		BaseType.InfoItem item = data.get(position); 
		return item.mBannerType;
	}

	
	@Override
	public int getViewTypeCount() {

		
		return 3;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {

		BaseType.InfoItem item = data.get(pos); 
		int type = item.mBannerType;
	
		switch (type) {
		case 0:
			return getBanner_0_View(pos, view);
		case 1:
			return getBanner_1_View(pos, view);
		case 2:
			return getBanner_2_View(pos, view);
		default:
			break;
		}
		
		return getBanner_0_View(pos, view);
	}
	
	
	private View getBanner_0_View(int pos, View view){
		ViewHolder0 holder = null; 
		
		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.content_listitem_layout0, null);
			holder = new ViewHolder0();
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
			holder.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
			view.setTag(holder);
		}else{
			holder = (ViewHolder0) view.getTag();
		}
		
		BaseType.InfoItem item = data.get(pos);
		holder.tvTitle.setText(item.mTitle);
		holder.tvContent.setText(item.mContent);
		holder.tvArtist.setText(item.mUserName);

		return view;
	}
	
	private View getBanner_1_View(int pos, View view){
		ViewHolder1 holder = null; 
		
		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.content_listitem_layout1, null);
			holder = new ViewHolder1();
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
			holder.ivContent = (ImageView) view.findViewById(R.id.iv_content);
			holder.tvImageCount = (TextView) view.findViewById(R.id.tv_imagecount);
			holder.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
			view.setTag(holder);
		}else{
			holder = (ViewHolder1) view.getTag();
		}
		
		BaseType.InfoItem item = data.get(pos);
		holder.tvTitle.setText(item.mTitle);
		holder.tvContent.setText(item.mContent);
		holder.tvArtist.setText(item.mUserName);
		
		int thumailImageCount = item.getThumnaiImageCount();
		holder.tvImageCount.setText(String.valueOf(thumailImageCount));
	
	
		holder.ivContent.setImageResource(R.drawable.load_img);
		if (!mBusy) {
			mImageLoader.DisplayImage(item.getThumnaiImageURL(0), holder.ivContent, false);
		} else {
			mImageLoader.DisplayImage(item.getThumnaiImageURL(0),  holder.ivContent, true);		
		}

		return view;
	}
	
	private View getBanner_2_View(int pos, View view){
		ViewHolder2 holder = null; 
		
		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.content_listitem_layout2, null);
			holder = new ViewHolder2();
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			holder.tvImageCount = (TextView) view.findViewById(R.id.tv_imagecount);
			holder.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
			holder.ivContent1 = (ImageView) view.findViewById(R.id.iv_content1);
			holder.ivContent2 = (ImageView) view.findViewById(R.id.iv_content2);
			holder.ivContent3 = (ImageView) view.findViewById(R.id.iv_content3);
			view.setTag(holder);
		}else{
			
			holder = (ViewHolder2) view.getTag();
		}
		
		BaseType.InfoItem item = data.get(pos);
		holder.tvTitle.setText(item.mTitle);	
		holder.tvArtist.setText(item.mUserName);
				
		int thumailImageCount = item.getThumnaiImageCount();
		holder.tvImageCount.setText(String.valueOf(thumailImageCount));
		
		if (thumailImageCount == 1){
			holder.ivContent1.setVisibility(View.VISIBLE);
			holder.ivContent2.setVisibility(View.GONE);
			holder.ivContent3.setVisibility(View.GONE);
			holder.ivContent1.setImageResource(R.drawable.load_img);	
		}else if (thumailImageCount == 2){		
			holder.ivContent1.setVisibility(View.VISIBLE);
			holder.ivContent2.setVisibility(View.VISIBLE);
			holder.ivContent3.setVisibility(View.GONE);
			holder.ivContent1.setImageResource(R.drawable.load_img);
			holder.ivContent2.setImageResource(R.drawable.load_img);
		}else{
			holder.ivContent1.setVisibility(View.VISIBLE);
			holder.ivContent2.setVisibility(View.VISIBLE);
			holder.ivContent3.setVisibility(View.VISIBLE);
			holder.ivContent1.setImageResource(R.drawable.load_img);
			holder.ivContent2.setImageResource(R.drawable.load_img);
			holder.ivContent3.setImageResource(R.drawable.load_img);
		}
		
		mImageLoader.DisplayImage(item.getThumnaiImageURL(0), holder.ivContent1, mBusy);
		mImageLoader.DisplayImage(item.getThumnaiImageURL(1), holder.ivContent2, mBusy);
		mImageLoader.DisplayImage(item.getThumnaiImageURL(2), holder.ivContent3, mBusy);
		
		return view;
	}
	
    static class ViewHolder0 { 
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvArtist;
    } 
    
    
    static class ViewHolder1 { 
        public TextView tvTitle;
        public TextView tvContent;
        public ImageView ivContent;
        public TextView tvImageCount;
        public TextView tvArtist;
    } 
    
    static class ViewHolder2 { 
        public TextView tvTitle;
        public ImageView ivContent1;
        public ImageView ivContent2;
        public ImageView ivContent3;
        public TextView tvImageCount;
        public TextView tvArtist;
    } 
}
