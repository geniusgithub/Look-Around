package com.geniusgithub.lookaround.maincontent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.content.ContentActivity;
import com.geniusgithub.lookaround.content.ContentCache;
import com.geniusgithub.lookaround.maincontent.InfoAdapter;
import com.geniusgithub.lookaround.maincontent.IContentItemClick;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.model.BaseType.InfoItem;
import com.geniusgithub.lookaround.proxy.InfoRequestProxy;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public  class CommonFragmentEx extends BaseFragment implements InfoRequestProxy.IRequestResult,
                                                     SwipeRefreshLayout.OnRefreshListener,
                                                    IContentItemClick{

    private static final CommonLog log = LogFactory.createLog();

	private View mInvalidView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mListView;
    private LinearLayoutManager mLayoutManager;
    private BaseType.ListItem mTypeData;
    private InfoAdapter mAdapter;
    private Context mContext;
    private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();
    private InfoRequestProxy mInfoRequestProxy;
    
    private Handler mHandler;
    private boolean loginStatus = false;
    private boolean isFirstRequest = true;
    private final static int MSG_REQUEST = 0x0001;

    private int totalItemCount;
    private int lastVisibleItem;
    private boolean loading = false;
    private boolean bottom = false;

    public CommonFragmentEx(BaseType.ListItem data){
            mTypeData = data;        
    }
    
    public CommonFragmentEx(){
    
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
            log.e("CommonFragmentEx onCreate");
            super.onCreate(savedInstanceState);
        loginStatus = LAroundApplication.getInstance().getLoginStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            log.i("CommonFragmentEx onCreateView");
       View view = inflater.inflate(R.layout.commonex_layout, null);
       mInvalidView = view.findViewById(R.id.invalid_view);
        mListView = (RecyclerView) view.findViewById(R.id.recycle_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.content_view);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            log.i("CommonFragmentEx onActivityCreated");
            
            if (loginStatus){
                    setupViews();                        
                    initData();
            }

    
    }


    @Override
    public void onResume() {
        super.onResume();
        if (loginStatus){
            dealyToRequest();
        }

    }

    @Override
    public void onDestroy() {
        log.e("CommonFragmentEx onDestroy");
        if (loginStatus){
            mInfoRequestProxy.cancelRequest();
        }

        super.onDestroy();
    }



    private void setupViews(){
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }
    
    private void initData(){

            mContext = getActivity();
    
            mAdapter = new InfoAdapter(mContext, mContentData);
             mAdapter.setOnItemClickListener(this);
            mListView.setAdapter(mAdapter);

            mListView.setHasFixedSize(true);
            mListView.setNestedScrollingEnabled(false);
            mLayoutManager = new LinearLayoutManager(mContext);
            mListView.setLayoutManager(mLayoutManager);
             mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p/>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                        totalItemCount = layoutManager.getItemCount();
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        if (lastVisibleItem == totalItemCount - 1){
                            int loadMoreState = mAdapter.getLoadMoreViewState();
                            if (loadMoreState == InfoAdapter.ILoadMoreViewState.LMVS_NORMAL){
                                mInfoRequestProxy.requestMoreInfo();
                                mAdapter.updateLoadMoreViewState(InfoAdapter.ILoadMoreViewState.LMVS_LOADING);
                            }
                        }
                    }
            });


            mSwipeRefreshLayout.setOnRefreshListener(this);
            mInfoRequestProxy = new InfoRequestProxy(mContext, mTypeData, this);          
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case MSG_REQUEST:
                            mInfoRequestProxy.requestRefreshInfo();
                        break;
                    }
                }
            };
    }

    private void dealyToRequest(){
        if (isFirstRequest){
            log.i("dealyToRequest");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLoadView(true);
                }
            }, 200);
            mHandler.sendEmptyMessageDelayed(MSG_REQUEST, 1000);
            isFirstRequest = false;
        }

    }

    
    public BaseType.ListItem getData(){
            return mTypeData;
    }


    public void showFailView(boolean bShow){
        mInvalidView.setVisibility(bShow ? View.VISIBLE : View.GONE);
    }

    public void showLoadView(boolean bShow){
        mSwipeRefreshLayout.setRefreshing(bShow);
    }


    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessage(MSG_REQUEST);
    }


    private boolean isFirst = true;
    @Override
    public void onSuccess(boolean isLoadMore, boolean isLoadDataComplete) {
            showFailView(false);
            mContentData = mInfoRequestProxy.getData();
            mAdapter.refreshData(mContentData);
            
            log.i("onSuccess isLoadMore = " + isLoadMore + ", isLoadDataComplete = " + isLoadDataComplete + ", isfrist = "  + isFirst);
            if (isLoadMore){
                if (isLoadDataComplete){
                    mAdapter.updateLoadMoreViewState(InfoAdapter.ILoadMoreViewState.LMVS_OVER);
                }else{
                    mAdapter.updateLoadMoreViewState(InfoAdapter.ILoadMoreViewState.LMVS_NORMAL);
                }

            }else{
                showLoadView(false);
                mAdapter.updateLoadMoreViewState(InfoAdapter.ILoadMoreViewState.LMVS_NORMAL);
            }
    }


    @Override
    public void onRequestFailure(boolean isLoadMore) {
            CommonUtil.showToast(R.string.toast_getdata_fail, mContext);
            if (isLoadMore){
                mAdapter.updateLoadMoreViewState(InfoAdapter.ILoadMoreViewState.LMVS_NORMAL);
            }else{
                showLoadView(false);
            }

            if (mContentData.size() == 0){
                    showFailView(true);
                    return ;
            }


    }


    @Override
    public void onAnylizeFailure(boolean isLoadMore) {
            CommonUtil.showToast(R.string.toast_anylizedata_fail, mContext);
            if (isLoadMore){
                mAdapter.updateLoadMoreViewState(InfoAdapter.ILoadMoreViewState.LMVS_NORMAL);
            }else{
                showLoadView(false);
            }
            if (mContentData.size() == 0){
                showFailView(true);
                 return ;
            }


    }


    @Override
    public void onItemClick(InfoItem item) {

            BaseType.InfoItemEx itemEx = new BaseType.InfoItemEx(item, mTypeData);
            ContentCache.getInstance().setTypeItem(mTypeData);
            ContentCache.getInstance().setInfoItem(itemEx);
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(BaseType.ListItem.KEY_TYPEID, mTypeData.mTypeID);
            map.put(BaseType.ListItem.KEY_TITLE, itemEx.mTitle);
            LAroundApplication.getInstance().onEvent("UMID0002", map);

    //        log.i("setTypeItem = \n" + mTypeData.getShowString() + "\nsetInfoItem = " + itemEx.toString());
            
            goContentActivity();
    }
    

    private void goContentActivity(){
            Intent intent = new Intent();
            intent.setClass(mContext, ContentActivity.class);
            startActivity(intent);
    }



}