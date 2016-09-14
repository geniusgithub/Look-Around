package com.geniusgithub.lookaround.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.geniusgithub.lookaround.R;

public abstract  class ToolbarFragmentActivity extends BaseActivity {

    private final String TAG_FRAGMENT = "Content_Fragment";
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.toolbar_activity_layout);
        initToolBar();

        getFragmentManager().beginTransaction().add(R.id.content_container, newContentFragment(), TAG_FRAGMENT).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }





    public abstract Fragment newContentFragment();

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

    }


    public  void updateToolTitle(String title){
        mToolbar.setTitle(title);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

}
