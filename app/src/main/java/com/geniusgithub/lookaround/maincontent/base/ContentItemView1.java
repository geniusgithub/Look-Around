package com.geniusgithub.lookaround.maincontent.base;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.component.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentItemView1<T extends ContentItemModel> extends ContentIiemView<T> {



    @BindView(R.id.cardview)
    public CardView mCardView;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.tv_content)
    public TextView tvContent;

    @BindView(R.id.tv_artist)
    public TextView tvArtist;

    @BindView(R.id.iv_content)
    public ImageView ivContent;

    @BindView(R.id.tv_imagecount)
    public TextView tvImageCount;



    public ContentItemView1(Context context) {
        super(context);
        View.inflate(context, R.layout.content_listitem_layout1, this);
        ButterKnife.bind(this);
    }


    @Override
    public void bindView(T data, int position) {
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
        tvArtist.setText(data.getUserName());

        int thumailImageCount = data.getThumnaiImageCount();
        tvImageCount.setText(String.valueOf(thumailImageCount));

        ImageLoader.loadThumail(mContext, data.getThumnaiImageURL(0), ivContent, mPlaceHolder);
    }
}
