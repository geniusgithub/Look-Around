package com.geniusgithub.lookaround.adapter;

import java.util.ArrayList;
import java.util.List;

import com.geniusgithub.lookaround.R;
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
	
	public InfoContentAdapter(Context context, List<BaseType.InfoItem> data)
	{
		mContext = context;
		this.data = data;
	}
	
	public void refreshData(List<BaseType.InfoItem> data)
	{
		this.data = data;
		notifyDataSetChanged();
		
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
		log.e("pos = " + pos + ", type = " + type);
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
		
		holder.tvImageCount.setText("1");
		holder.tvArtist.setText(item.mUserName);

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
			view.setTag(holder);
		}else{
			
			holder = (ViewHolder2) view.getTag();
		}
		
		BaseType.InfoItem item = data.get(pos);
		holder.tvTitle.setText(item.mTitle);	
		holder.tvImageCount.setText("2");
		holder.tvArtist.setText(item.mUserName);

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
