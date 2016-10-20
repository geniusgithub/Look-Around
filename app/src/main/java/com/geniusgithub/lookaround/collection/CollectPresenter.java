package com.geniusgithub.lookaround.collection;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.geniusgithub.common.util.AlwaysLog;
import com.geniusgithub.lookaround.datastore.DaoMaster;
import com.geniusgithub.lookaround.datastore.DaoSession;
import com.geniusgithub.lookaround.datastore.InfoItemDao;
import com.geniusgithub.lookaround.detailcontent.DetailActivity;
import com.geniusgithub.lookaround.detailcontent.DetailCache;
import com.geniusgithub.lookaround.model.BaseType;


import java.util.ArrayList;
import java.util.List;

public class CollectPresenter implements  CollectContract.IPresenter {

    private static final String TAG = CollectPresenter.class.getSimpleName();

    private Context mContext;
    private CollectContract.IView mView;

    private List<BaseType.InfoItemEx> mContentData = new ArrayList<BaseType.InfoItemEx>();


    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private InfoItemDao infoItemDao;
    private SQLiteDatabase db;

    public CollectPresenter(){

    }


    ///////////////////////////////////////     presenter callback begin
    @Override
    public void bindView(CollectContract.IView view) {
        mView = view;
        mView.bindPresenter(this);
    }

    @Override
    public void unBindView() {

    }

    @Override
    public void onEnterDetail(BaseType.InfoItemEx item) {
        DetailCache.getInstance().setTypeItem(item.mType);
        DetailCache.getInstance().setInfoItem(item);
        goContentActivity();
    }

    @Override
    public void clearCollcet() {
        infoItemDao.deleteAll();
        mContentData = infoItemDao.loadAll();
        mView.updateInfomationView(mContentData);
    }

    @Override
    public int getCollectCount() {
        return (int)infoItemDao.count();
    }
    ///////////////////////////////////////     presenter callback end


    ///////////////////////////////////////     lifecycle or ui operator begin
    public void onUiCreate(Context context){
        mContext = context;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, "lookaround-db", null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        infoItemDao = daoSession.getInfoItemDao();

        mContentData = infoItemDao.loadAll();
        AlwaysLog.i(TAG, "load all size = " + mContentData.size());
        mView.updateInfomationView(mContentData);
    }




    public void onUiDestroy() {
        if (db != null){
            db.close();
        }
    }
    ///////////////////////////////////////     lifecycle or ui operator end

    private void goContentActivity(){
        Intent intent = new Intent();
        intent.setClass(mContext, DetailActivity.class);
        mContext.startActivity(intent);
    }




}
