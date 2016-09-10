package com.geniusgithub.lookaround.share;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.geniusgithub.lookaround.base.ToolbarFragmentActivity;

public class ShareActivity extends ToolbarFragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public Fragment newContentFragment() {
        return new ShareFragment();
    }
}
