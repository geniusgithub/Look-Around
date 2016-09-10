package com.geniusgithub.lookaround.detailcontent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.geniusgithub.lookaround.base.ToolbarFragmentActivity;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class DetailActivity extends ToolbarFragmentActivity {

    private static final CommonLog log = LogFactory.createLog();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public Fragment newContentFragment() {
        return new DetailFragment();
    }
}
