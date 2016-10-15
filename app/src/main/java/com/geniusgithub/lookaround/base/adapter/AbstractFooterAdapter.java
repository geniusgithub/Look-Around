package com.geniusgithub.lookaround.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract  class AbstractFooterAdapter<T, V extends IBaseAdapterView> extends BaseListAdapter<T, V> {

    protected static final int VIEW_TYPE_ITEM = 1; // Normal list item
    protected static final int VIEW_TYPE_FOOTER = 2;  // Footer
    protected View mFooterView;
    protected boolean mIsFootViewEnable = false;
    public AbstractFooterAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public boolean isFooterEnabled() {
        return mIsFootViewEnable;
    }

    public void setFooterEnable(boolean enable){
        mIsFootViewEnable = enable;
    }

    protected View createFooterView(Context context) {
        mFooterView = null;
        return mFooterView;
    }

    protected void bindFooterView(Context context){

    }

    protected int  getFooterViewType(){
        return VIEW_TYPE_FOOTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == getFooterViewType()) {
            return new RecyclerView.ViewHolder(createFooterView(mContext)) {};
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == getFooterViewType()) {
            bindFooterView(mContext);
        }else{
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterEnabled() && position == getItemCount() - 1) {
            return getFooterViewType();
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (itemCount == 0){
            return 0;
        }
        if (isFooterEnabled()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public T getItem(int position) {
        if (getItemViewType(position) == getFooterViewType()) {
            return null;
        }
        return super.getItem(position);
    }

}
