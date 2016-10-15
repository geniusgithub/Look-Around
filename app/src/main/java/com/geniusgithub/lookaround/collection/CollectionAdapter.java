package com.geniusgithub.lookaround.collection;

import android.content.Context;

import com.geniusgithub.lookaround.maincontent.base.ContentAdapter;
import com.geniusgithub.lookaround.model.BaseType;

import java.util.List;

public class CollectionAdapter extends ContentAdapter<BaseType.InfoItemEx> {

    public CollectionAdapter(Context context, List<BaseType.InfoItemEx> data) {
        super(context, data);
        setFooterEnable(false);
    }
}
