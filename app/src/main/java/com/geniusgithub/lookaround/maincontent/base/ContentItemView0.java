package com.geniusgithub.lookaround.maincontent.base;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentItemView0<T extends ContentItemModel> extends ContentIiemView<T> {

    @BindView(R.id.cardview)
    public CardView mCardView;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.tv_content)
    public TextView tvContent;

    @BindView(R.id.tv_artist)
    public TextView tvArtist;


    public ContentItemView0(Context context) {
        super(context);
        View.inflate(context, R.layout.content_listitem_layout0, this);
        ButterKnife.bind(this);
    }


    @Override
    public void bindView(T data, int position) {
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
        tvArtist.setText(data.getUserName());

    }
}
