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

public class ContentItemView2<T extends ContentItemModel> extends ContentIiemView<T> {


    @BindView(R.id.cardview)
    public CardView mCardView;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.tv_artist)
    public TextView tvArtist;

    @BindView(R.id.tv_imagecount)
    public TextView tvImageCount;

    @BindView(R.id.iv_content1)
    public ImageView ivContent1;

    @BindView(R.id.iv_content2)
    public ImageView ivContent2;

    @BindView(R.id.iv_content3)
    public ImageView ivContent3;


    public ContentItemView2(Context context) {
        super(context);
        View.inflate(context, R.layout.content_listitem_layout2, this);
        ButterKnife.bind(this);
    }

    @Override
    public void bindView(T data, int position) {
        tvTitle.setText(data.getTitle());
        tvArtist.setText(data.getUserName());

        int thumailImageCount = data.getThumnaiImageCount();
        tvImageCount.setText(String.valueOf(thumailImageCount));

        if (thumailImageCount == 1){
            ivContent1.setVisibility(View.VISIBLE);
            ivContent2.setVisibility(View.INVISIBLE);
            ivContent3.setVisibility(View.INVISIBLE);
        }else if (thumailImageCount == 2){
            ivContent1.setVisibility(View.VISIBLE);
            ivContent2.setVisibility(View.VISIBLE);
            ivContent3.setVisibility(View.INVISIBLE);
        }else{
            ivContent1.setVisibility(View.VISIBLE);
            ivContent2.setVisibility(View.VISIBLE);
            ivContent3.setVisibility(View.VISIBLE);
        }

        ImageLoader.loadThumail(mContext, data.getThumnaiImageURL(0), ivContent1, null);
        ImageLoader.loadThumail(mContext, data.getThumnaiImageURL(1), ivContent2, null);
        ImageLoader.loadThumail(mContext, data.getThumnaiImageURL(2), ivContent3, null);
    }
}
