package com.geniusgithub.lookaround.maincontent.base;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geniusgithub.lookaround.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentFootView extends RelativeLayout {

    @BindView(R.id.pb_load_more)
    public ProgressBar pbLoadMore;

    @BindView(R.id.tv_nodata)
    public TextView tvNoData;


    public ContentFootView(Context context) {
        super(context);
        View.inflate(context, R.layout.recyclerview_footer, this);
        ButterKnife.bind(this);
    }


    public void updateState(int state){
        switch (state){
            case ILoadMoreViewState.LMVS_NORMAL:
            case ILoadMoreViewState.LMVS_LOADING:
                pbLoadMore.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
                break;
            case ILoadMoreViewState.LMVS_OVER:
                pbLoadMore.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                break;
        }
    }
}
