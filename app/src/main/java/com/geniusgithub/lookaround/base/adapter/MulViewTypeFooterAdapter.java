package com.geniusgithub.lookaround.base.adapter;

import android.content.Context;

import java.util.List;

public abstract  class MulViewTypeFooterAdapter<T, V extends IBaseAdapterView> extends AbstractFooterAdapter<T, V> {

    public MulViewTypeFooterAdapter(Context context, List<T> data) {
        super(context, data);
    }

    protected abstract int getFooterViewType();

    public abstract int getNormalViewType(T data, int position);

    @Override
    public int getItemViewType(int position) {
        if (isFooterEnabled() && position == getItemCount() - 1) {
            return getFooterViewType();
        }
        return getNormalViewType(mData.get(position), position);
    }

}
