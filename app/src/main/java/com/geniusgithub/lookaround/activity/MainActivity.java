package com.geniusgithub.lookaround.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.geniusgithub.lookaround.FragmentControlCenter;
import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.NavigationViewEx;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.activity.set.SettingActivity;
import com.geniusgithub.lookaround.adapter.NavChannelAdapter;
import com.geniusgithub.lookaround.base.BaseActivityEx;
import com.geniusgithub.lookaround.fragment.CommonFragmentEx;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivityEx implements OnItemClickListener, NavigationViewEx.INavClickListener{
    private static final CommonLog log = LogFactory.createLog();
    private Context mContext;

    private NavigationViewEx navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    private ListView mListView;
    private List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
    private NavChannelAdapter mAdapter;


    private CommonFragmentEx mContentFragment;
    private FragmentControlCenter mControlCenter;
    private boolean loginStatus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        loginStatus = LAroundApplication.getInstance().getLoginStatus();
        if (!loginStatus){
            log.e("loginStatus is false ,jump to welcome view!!!");
            LAroundApplication.getInstance().startToSplashActivity();
            finish();
            return ;
        }

        initView();
        initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_MENU) {
            boolean ret = drawerLayout.isDrawerOpen(Gravity.LEFT);
            if (!ret){
                goSettingActivity();
            }
            return false;

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        BaseType.ListItem item = (BaseType.ListItem) adapter.getItemAtPosition(position);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(BaseType.ListItem.KEY_TYPEID, item.mTypeID);
        map.put(BaseType.ListItem.KEY_TITLE, item.mTitle);
        LAroundApplication.getInstance().onEvent("UMID0020", map);

        CommonFragmentEx fragmentEx = mControlCenter.getCommonFragmentEx(item);
        switchContent(fragmentEx);

    }

    @Override
    public void onSettingClick() {
        goSettingActivity();
    }

    @Override
    public void onBackPressed() {
        if(showExitToast()){
            finish();
        }else{
            CommonUtil.showToast(R.string.toast_exit_again, this);
        }

    }

    private void initView(){
        initToolBar();
        initDrawLayout();

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initDrawLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle mDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mDrawerToggle);

        navigationView = (NavigationViewEx) findViewById(R.id.navigationView);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


    }

    private void setupDrawerContent(NavigationViewEx navigationView) {
        mListView = (ListView) navigationView.getmNavListview();
        mListView.setOnItemClickListener(this);
        navigationView.setmNavListener(this);
    }

    private void initData(){
        mControlCenter = FragmentControlCenter.getInstance(getApplicationContext());

        mDataList = LAroundApplication.getInstance().getUserLoginResult().mDataList;
        mAdapter = new NavChannelAdapter(mContext, mDataList);
        mListView.setAdapter(mAdapter);
        int size = mDataList.size();
        if (size > 0){
            mContentFragment = mControlCenter.getCommonFragmentEx(mDataList.get(0));
            switchContent(mContentFragment);
        }

    }



    public void switchContent(final CommonFragmentEx fragment) {
        mContentFragment = fragment;

        getFragmentManager().beginTransaction().replace(R.id.content, mContentFragment).commit();

        toolbar.setTitle(mContentFragment.getData().mTitle);
        drawerLayout.closeDrawers();
    }

    private void goSettingActivity(){
        Intent intent = new Intent();
        intent.setClass(this, SettingActivity.class);
        startActivity(intent);
    }

    private long curMillios = 0;
    private boolean showExitToast(){
        long time = System.currentTimeMillis();
        if (time - curMillios < 2000){
            return true;
        }

        curMillios = time;
        return false;
    }


}
