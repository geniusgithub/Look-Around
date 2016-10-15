package com.geniusgithub.lookaround.collection;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public  class CollectFragment extends BaseFragment {

    private View mRootView;
    private CollectPresenter mCollectPresenter;
    private CollectContract.IView mCollectView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.collect_fragment_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.collect_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                mCollectView.showDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void onUIReady(View view){
        mRootView = view.findViewById(R.id.ll_rootview);

        mCollectPresenter = new CollectPresenter();
        mCollectView = new CollectView(getActivity());
        mCollectView.setupView(mRootView);
        mCollectPresenter.bindView(mCollectView);
        mCollectPresenter.onUiCreate(getActivity());

    }


    @Override
    public void onDestroy() {
        mCollectPresenter.onUiDestroy();
        super.onDestroy();
    }



    private class CollectView implements CollectContract.IView, OnContentItemClickListener, IDialogInterface {

        private Context mContext;
        private CollectContract.IPresenter mPresenter;

        private RecyclerView mListView;
        private LinearLayoutManager mLayoutManager;
        private CollectionAdapter mAdapter;

        private Dialog deleteDialog;
        public CollectView(Context context){
            mContext = context;
        }

        @Override
        public void bindPresenter(CollectContract.IPresenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void setupView(View view) {
            mListView = (RecyclerView) view.findViewById(R.id.recycle_listview);
            mListView.setHasFixedSize(true);
            mListView.setNestedScrollingEnabled(false);
            mLayoutManager = new LinearLayoutManager(getParentActivity());
            mListView.setLayoutManager(mLayoutManager);

            mAdapter = new CollectionAdapter(getParentActivity(),  new ArrayList<BaseType.InfoItemEx>());
            mAdapter.setOnItemClickListener(this);
            mListView.setAdapter(mAdapter);

        }


        @Override
        public void updateInfomationView(List<BaseType.InfoItemEx> dataList) {
            mAdapter.setData(dataList);
        }

        @Override
        public void showDeleteDialog(){

            long count = mPresenter.getCollectCount();
            if (count == 0){
                CommonUtil.showToast(R.string.toast_no_delcollect, getParentActivity());
                return ;
            }
            if (deleteDialog != null){
                deleteDialog.show();
                return ;
            }

            deleteDialog = DialogBuilder.buildNormalDialog(getParentActivity(),
                    getResources().getString(R.string.dia_msg_delcollect_title),
                    getResources().getString(R.string.dia_msg_delcollect_msg),
                    this);
            deleteDialog.show();
        }



        @Override
        public void onItemClick(BaseType.InfoItemEx data, int position) {
            mPresenter.onEnterDetail(data);
        }

        @Override
        public void onSure() {
            if (deleteDialog != null){
                deleteDialog.dismiss();
            }

            mPresenter.clearCollcet();
        }

        @Override
        public void onCancel() {
            if (deleteDialog != null) {
                deleteDialog.dismiss();
            }
        }

    }



}