package com.geniusgithub.lookaround.maincontent.base;

import android.content.Context;
import android.view.View;

import com.geniusgithub.lookaround.base.adapter.MulViewTypeFooterAdapter;

import java.util.List;

public class ContentAdapter<T extends ContentItemModel> extends MulViewTypeFooterAdapter<T , ContentIiemView> {

    private final int FOOTVIEW_TYPE = 100;
    private final int BANNER_VIEW0_TYPE = 0;
    private final int BANNER_VIEW1_TYPE = 1;
    private final int BANNER_VIEW2_TYPE = 2;


    public ContentAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    protected View createFooterView(Context context){

        if (mFooterView == null) {
            mFooterView = new ContentFootView(context);
        }

        return mFooterView;
    }

    @Override
    protected void bindFooterView(Context context){
        ContentFootView footView = (ContentFootView) mFooterView;
        footView.updateState(mLoadMoreState);
    }

    @Override
    public ContentIiemView createView(Context context, int viewType) {
        switch (viewType){
            case BANNER_VIEW0_TYPE:
                return createBanner0View(context);
            case BANNER_VIEW1_TYPE:
                return createBanner1View(context);
            case BANNER_VIEW2_TYPE:
                return createBanner2View(context);
        }
        return createBanner0View(context);
    }

    @Override
    protected int getFooterViewType() {
        return FOOTVIEW_TYPE;
    }

    @Override
    public int getNormalViewType(T data, int position) {
        return data.getBannerType();
    }


    private ContentIiemView createBanner0View(Context context) {
        ContentIiemView view = new ContentItemView0(context);
        return view;
    }

    private ContentIiemView createBanner1View(Context context) {
        ContentIiemView view = new ContentItemView1(context);
        return view;
    }

    private ContentIiemView createBanner2View(Context context) {
        ContentIiemView view = new ContentItemView2(context);
        return view;
    }

    protected int mLoadMoreState = ILoadMoreViewState.LMVS_NORMAL;
    public void updateLoadMoreViewState(int state){

        switch (state){
            case ILoadMoreViewState.LMVS_NORMAL:
            case ILoadMoreViewState.LMVS_LOADING:
            case ILoadMoreViewState.LMVS_OVER:
                mLoadMoreState = state;
                notifyDataSetChanged();
                break;
        }

    }

    public int getLoadMoreViewState(){
        return mLoadMoreState;
    }
}
