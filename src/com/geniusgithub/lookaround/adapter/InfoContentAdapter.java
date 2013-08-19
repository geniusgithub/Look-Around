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
	public View getView(int pos, View view, ViewGroup parent) {
		
		ViewHolder holder = null; 


		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.content_listitem_layout, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			holder.tvContent = (TextView) view.findViewById(R.id.tv_content);
			holder.ivContent = (ImageView) view.findViewById(R.id.iv_content);
			holder.tvCommentCount = (TextView) view.findViewById(R.id.tv_commentcount);
			holder.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		BaseType.InfoItem item = data.get(pos);
		holder.tvTitle.setText(item.mTitle);
		holder.tvContent.setText(item.mContent);
		
		holder.tvCommentCount.setText(item.mCommentCount);
		holder.tvArtist.setText(item.mUserName);

		return view;
	}
	
	
    static class ViewHolder { 
        public TextView tvTitle;
        public TextView tvContent;
        public ImageView ivContent;
        public TextView tvCommentCount;
        public TextView tvArtist;
    } 
}
