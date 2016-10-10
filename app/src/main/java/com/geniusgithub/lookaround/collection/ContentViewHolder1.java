package com.geniusgithub.lookaround.collection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.component.ImageLoader;
import com.geniusgithub.lookaround.model.BaseType;

public class ContentViewHolder1 extends  RecyclerView.ViewHolder implements View.OnClickListener{
    private Context mContext;
    public CardView mCardView;
    public TextView tvTitle;
    public TextView tvContent;
    public ImageView ivContent;
    public TextView tvImageCount;
    public TextView tvArtist;
    public BaseType.InfoItemEx mItem;
    public IContentItemClick mListener;

    public ContentViewHolder1(Context context, View itemView, IContentItemClick listener) {
        super(itemView);
        mContext = context;
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        ivContent = (ImageView) itemView.findViewById(R.id.iv_content);
        tvImageCount = (TextView) itemView.findViewById(R.id.tv_imagecount);
        tvArtist = (TextView) itemView.findViewById(R.id.tv_artist);
        mCardView = (CardView) itemView.findViewById(R.id.cardview);
        mCardView.setOnClickListener(this);
        mListener = listener;
    }

    public void bindInfo(BaseType.InfoItemEx item, Drawable placeHodler){
        mItem = item;
        tvTitle.setText(item.mTitle);
        tvContent.setText(item.mContent);
        tvArtist.setText(item.mUserName);

        int thumailImageCount = item.getThumnaiImageCount();
        tvImageCount.setText(String.valueOf(thumailImageCount));

        ImageLoader.loadThumail(mContext, item.getThumnaiImageURL(0), ivContent, placeHodler);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onItemClick(mItem);
        }
    }
}
