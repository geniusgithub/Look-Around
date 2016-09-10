package com.geniusgithub.lookaround.collection;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.detailcontent.DetailActivity;
import com.geniusgithub.lookaround.detailcontent.DetailCache;
import com.geniusgithub.lookaround.dialog.DialogBuilder;
import com.geniusgithub.lookaround.dialog.IDialogInterface;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends BaseFragment implements IDialogInterface, IContentItemClick{
    private static final CommonLog log = LogFactory.createLog();


    private RecyclerView mListView;
    private LinearLayoutManager mLayoutManager;
    private CollectionAdapter mAdapter;
    private List<BaseType.InfoItemEx> mContentData = new ArrayList<BaseType.InfoItemEx>();



    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private InfoItemDao infoItemDao;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collect_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        db.close();

        super.onDestroy();
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
                showDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSure() {
        if (deleteDialog != null){
            deleteDialog.dismiss();
        }

        clear();
    }


    @Override
    public void onCancel() {
        if (deleteDialog != null) {
            deleteDialog.dismiss();
        }
    }


    @Override
    public void onItemClick(BaseType.InfoItemEx item){
        DetailCache.getInstance().setTypeItem(item.mType);
        DetailCache.getInstance().setInfoItem(item);
        log.i("onItemClick typeitem --> \n" + item.mType.getShowString());

        goContentActivity();
    }



    private void onUIReady(View view){
        mListView = (RecyclerView) view.findViewById(R.id.recycle_listview);
        mListView.setHasFixedSize(true);
        mListView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(getParentActivity());
        mListView.setLayoutManager(mLayoutManager);

        initData();

    }

    private void initData(){

        mAdapter = new CollectionAdapter(getParentActivity(), mContentData);
        mAdapter.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);

        inidDataBase();

        refreshData();
    }

    private void inidDataBase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getParentActivity(), "lookaround-db", null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        infoItemDao = daoSession.getInfoItemDao();


    }

    private void refreshData(){

        mContentData = infoItemDao.loadAll();
        log.e("load all size = " + mContentData.size());
//		int size = mContentData.size();
//		for(int i = 0; i < size; i++){
//			log.e("index = " + i + ", mContentData[0] = \n" + mContentData.get(i).toString());
//		}

        mAdapter.refreshData(mContentData);
    }


    private void clear(){
        infoItemDao.deleteAll();
        refreshData();
    }

    private void goContentActivity(){
        Intent intent = new Intent();
        intent.setClass(getParentActivity(), DetailActivity.class);
        startActivity(intent);
    }


    private Dialog deleteDialog;

    private void showDeleteDialog(){

        long count = infoItemDao.count();
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

}
