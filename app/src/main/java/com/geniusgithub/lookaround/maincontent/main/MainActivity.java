package com.geniusgithub.lookaround.maincontent.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.geniusgithub.lookaround.FragmentControlCenter;
import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.NavigationViewEx;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.adapter.NavChannelAdapter;
import com.geniusgithub.lookaround.base.BaseActivity;
import com.geniusgithub.lookaround.maincontent.infomation.InfomationFragment;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.setting.SettingActivity;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final CommonLog log = LogFactory.createLog();
    public static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;


    private View mRootView;
    private MainPresenter mMainPresenter;
    private MainContract.IView mMainView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initData();
    }




    @Override
    public void onBackPressed() {
        if (showExitToast()) {
            finish();
        } else {
            CommonUtil.showToast(R.string.toast_exit_again, this);
        }

    }

    private long curMillios = 0;

    private boolean showExitToast() {
        long time = System.currentTimeMillis();
        if (time - curMillios < 2000) {
            return true;
        }

        curMillios = time;
        return false;
    }

    private void initData() {
        mRootView = findViewById(R.id.drawerLayout);

        mMainPresenter = new MainPresenter();
        mMainView = new MainView();
        mMainView.setupView(mRootView);
        mMainPresenter.bindView(mMainView);
        mMainPresenter.onUiCreate(this);
    }

    private class MainView implements MainContract.IView, AdapterView.OnItemClickListener, NavigationViewEx.INavClickListener {


        private MainContract.IPresenter mPresenter;
        private View mRootView;

        private NavigationViewEx navigationView;
        private DrawerLayout drawerLayout;
        private Toolbar toolbar;


        private ListView mListView;
        private List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
        private NavChannelAdapter mAdapter;


        private InfomationFragment mContentFragment;
        private FragmentControlCenter mControlCenter;


        @Override
        public void bindPresenter(MainContract.IPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void setupView(View rootView) {
            mRootView = rootView;

            initToolBar();
            initDrawLayout();
            initFragmentControl();
        }

        @Override
        public void updateNavView(List<BaseType.ListItem> dataList) {
            mDataList = dataList;
            int size = mDataList.size();
            log.i("mDataList.size = " + size);
            mAdapter.refreshData(mDataList);
            if (size > 0) {
                mContentFragment = mControlCenter.getCommonFragmentEx(mDataList.get(0));
                switchContent(mContentFragment);
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            BaseType.ListItem item = (BaseType.ListItem) adapter.getItemAtPosition(position);

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(BaseType.ListItem.KEY_TYPEID, item.mTypeID);
            map.put(BaseType.ListItem.KEY_TITLE, item.mTitle);
            LAroundApplication.getInstance().onEvent("UMID0020", map);

            InfomationFragment fragmentEx = mControlCenter.getCommonFragmentEx(item);
            switchContent(fragmentEx);
        }

        @Override
        public void onSettingClick() {
            goSettingActivity();
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
                    new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
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


        private void initFragmentControl() {
            mControlCenter = FragmentControlCenter.getInstance(getApplicationContext());
            mAdapter = new NavChannelAdapter(mContext, mDataList);
            mListView.setAdapter(mAdapter);

        }

        public void switchContent(final InfomationFragment fragment) {
            mContentFragment = fragment;

            getFragmentManager().beginTransaction().replace(R.id.content, mContentFragment).commit();

            toolbar.setTitle(mContentFragment.getTypeData().mTitle);
            drawerLayout.closeDrawers();
        }


        private void goSettingActivity() {
            Intent intent = new Intent();
            intent.setClass(mContext, SettingActivity.class);
            mContext.startActivity(intent);
        };

    }
}