package com.geniusgithub.lookaround.maincontent.content;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.maincontent.base.ILoadMoreViewState;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.List;

public  class ContentFragment extends BaseFragment {

    private static final CommonLog log = LogFactory.createLog();
    private BaseType.ListItem mTypeData;
    private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();

    private View mRootView;
    private InfomationPresenter mInfomationPresenter;
    private InfomationContract.IView mInfomationView;
    private boolean isFirstResume = true;

    public ContentFragment(BaseType.ListItem data){
        mTypeData = data;
    }

    public ContentFragment(){
    
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.content_fragment_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        onUIReady(view);
    }


    private void onUIReady(View view){
        mRootView = view.findViewById(R.id.ll_rootview);

        mInfomationPresenter = new InfomationPresenter();
        mInfomationView = new InfomationView(getActivity());
        mInfomationView.setupView(mRootView);
        mInfomationPresenter.bindView(mInfomationView);
        mInfomationPresenter.onUiCreate(getActivity(), mTypeData, mContentData);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume){
            mInfomationPresenter.onResume();
            isFirstResume = false;
        }

    }

    @Override
    public void onDestroy() {
        mInfomationPresenter.onUiDestroy();
        super.onDestroy();
    }


    public BaseType.ListItem getTypeData(){
        return  mTypeData;
    }
    private class InfomationView implements InfomationContract.IView,  SwipeRefreshLayout.OnRefreshListener, OnContentItemClickListener {

        private Context mContext;
        private InfomationContract.IPresenter mPresenter;
        private View mRootView;


        private View mInvalidView;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private RecyclerView mListView;
        private LinearLayoutManager mLayoutManager;
        private ContentAdapterEx mAdapter;
        private LoadMoreListener mLoadMoreListener;
        private int totalItemCount;
        private int lastVisibleItem;

        public InfomationView(Context context){
            mContext = context;
        }

        @Override
        public void bindPresenter(InfomationContract.IPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void setupView(View rootView) {
            mRootView = rootView;
            mInvalidView = rootView.findViewById(R.id.invalid_view);
            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.content_view);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            mSwipeRefreshLayout.setOnRefreshListener(this);

            mListView = (RecyclerView) rootView.findViewById(R.id.recycle_listview);
            mLayoutManager = new LinearLayoutManager(ContentFragment.this.getParentActivity());
            mListView.setLayoutManager(mLayoutManager);
            mListView.setHasFixedSize(true);
            mListView.setNestedScrollingEnabled(false);

            mLoadMoreListener = new LoadMoreListener();
            mListView.addOnScrollListener(mLoadMoreListener);

            mAdapter = new ContentAdapterEx(mContext, new ArrayList<BaseType.InfoItem>());
            mAdapter.setOnItemClickListener(this);
            mListView.setAdapter(mAdapter);
        }

        @Override
        public void showFailView(boolean bShow) {
            mInvalidView.setVisibility(bShow ? View.VISIBLE : View.GONE);
        }

        @Override
        public void showLoadView(boolean bShow) {
            mSwipeRefreshLayout.setRefreshing(bShow);
        }

        @Override
        public void updateInfomationView(List<BaseType.InfoItem> dataList) {
            mContentData = dataList;
            mAdapter.setData(dataList);
        }

        @Override
        public void updateLoadMoreViewState(int state) {
            mAdapter.updateLoadMoreViewState(state);
        }

        @Override
        public void onRefresh() {
            mPresenter.onPullRefresh();
        }


        @Override
        public void onItemClick(BaseType.InfoItem data, int position) {
            mPresenter.onEnterDetail(data);
        }


        private class LoadMoreListener extends RecyclerView.OnScrollListener{
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem == totalItemCount - 1){
                    int loadMoreState = mAdapter.getLoadMoreViewState();
                    if (loadMoreState == ILoadMoreViewState.LMVS_NORMAL){
                        mPresenter.onLoadMore();
                    }
                }
            }
        }

    }



}
