package com.geniusgithub.lookaround.maincontent.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import com.geniusgithub.lookaround.base.adapter.IBaseAdapterView;

public abstract class ContentIiemView<T extends ContentItemModel> extends LinearLayout implements IBaseAdapterView<T> {

    protected Context mContext;
    protected Drawable mPlaceHolder;

    public ContentIiemView(Context context) {
        super(context);
        mContext = context;
    }

    public void setPlaceHolder(Drawable drawable){
        mPlaceHolder = drawable;
    }
}
