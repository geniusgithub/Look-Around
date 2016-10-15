package com.geniusgithub.lookaround.maincontent.content;

import android.content.Context;

import com.geniusgithub.lookaround.maincontent.base.ContentAdapter;
import com.geniusgithub.lookaround.model.BaseType;

import java.util.List;

public class ContentAdapterEx extends ContentAdapter<BaseType.InfoItem> {


    public ContentAdapterEx(Context context, List<BaseType.InfoItem> data) {
        super(context, data);
        setFooterEnable(true);
    }

}
