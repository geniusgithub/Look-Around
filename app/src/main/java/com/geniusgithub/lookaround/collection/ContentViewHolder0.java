package com.geniusgithub.lookaround.collection;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.model.BaseType;

public class ContentViewHolder0 extends  RecyclerView.ViewHolder implements View.OnClickListener{
    public CardView mCardView;
    public TextView tvTitle;
    public TextView tvContent;
    public TextView tvArtist;
    public BaseType.InfoItemEx mItem;
    public IContentItemClick mListener;

    public ContentViewHolder0(View itemView, IContentItemClick listener) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        tvArtist = (TextView) itemView.findViewById(R.id.tv_artist);
        mCardView = (CardView) itemView.findViewById(R.id.cardview);
        mCardView.setOnClickListener(this);
        mListener = listener;
    }

    public void bindInfo( BaseType.InfoItemEx item){
        mItem = item;
        tvTitle.setText(item.mTitle);
        tvContent.setText(item.mContent);
        tvArtist.setText(item.mUserName);

    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onItemClick(mItem);
        }
    }
}
