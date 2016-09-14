package com.geniusgithub.lookaround.maincontent.infomation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.detailcontent.DetailActivity;
import com.geniusgithub.lookaround.detailcontent.DetailCache;
import com.geniusgithub.lookaround.maincontent.infomation.InfomationContract.IPresenter;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.proxy.InfoRequestProxy;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfomationPresenter implements IPresenter, InfoRequestProxy.IRequestResult {


    private static final CommonLog log = LogFactory.createLog();

    private Context mContext;
    private InfomationContract.IView mView;
    private BaseType.ListItem mTypeData;

    private boolean loginStatus = false;
    private Handler mHandler;

    private final static int MSG_REQUEST = 0x0001;
    private InfoRequestProxy mInfoRequestProxy;
    private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();



    ///////////////////////////////////////     presenter callback begin
    @Override
    public void bindView(InfomationContract.IView view) {
        mView = view;
        mView.bindPresenter(this);
    }

    @Override
    public void unBindView() {

    }

    @Override
    public void onPullRefresh() {
        mHandler.sendEmptyMessage(MSG_REQUEST);
    }

    @Override
    public void onLoadMore() {
        mInfoRequestProxy.requestMoreInfo();
        mView.updateLoadMoreViewState(InfomationAdapter.ILoadMoreViewState.LMVS_LOADING);
    }

    @Override
    public void onEnterDetail(BaseType.InfoItem item) {
        BaseType.InfoItemEx itemEx = new BaseType.InfoItemEx(item, mTypeData);
        DetailCache.getInstance().setTypeItem(mTypeData);
        DetailCache.getInstance().setInfoItem(itemEx);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(BaseType.ListItem.KEY_TYPEID, mTypeData.mTypeID);
        map.put(BaseType.ListItem.KEY_TITLE, itemEx.mTitle);
        LAroundApplication.getInstance().onEvent("UMID0002", map);

        //        log.i("setTypeItem = \n" + mTypeData.getShowString() + "\nsetInfoItem = " + itemEx.toString());

        goContentActivity();
    }
    ///////////////////////////////////////     presenter callback end


    ///////////////////////////////////////     lifecycle or ui operator begin
    public void onUiCreate(Context context, BaseType.ListItem item, List<BaseType.InfoItem> infoList){
        mContext = context;
        mTypeData = item;
        mContentData = infoList;
        loginStatus = LAroundApplication.getInstance().getLoginStatus();

        mInfoRequestProxy = new InfoRequestProxy(mContext, mTypeData, this);
        mView.updateInfomationView(mContentData);


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

    public void onResume() {
        if (loginStatus){
            dealyToRequest();
        }

    }

    public void onUiDestroy() {
        if (loginStatus){
            mInfoRequestProxy.cancelRequest();
        }
    }
    ///////////////////////////////////////     lifecycle or ui operator end
    @Override
    public void onSuccess(boolean isLoadMore, boolean isLoadDataComplete) {
        mView.showFailView(false);
        mContentData = mInfoRequestProxy.getData();
        mView.updateInfomationView(mContentData);


        log.i("onSuccess isLoadMore = " + isLoadMore + ", isLoadDataComplete = " + isLoadDataComplete);
        if (isLoadMore){
            if (isLoadDataComplete){
                mView.updateLoadMoreViewState(InfomationAdapter.ILoadMoreViewState.LMVS_OVER);
            }else{
                mView.updateLoadMoreViewState(InfomationAdapter.ILoadMoreViewState.LMVS_NORMAL);
            }

        }else{
            mView.showLoadView(false);
            mView.updateLoadMoreViewState(InfomationAdapter.ILoadMoreViewState.LMVS_NORMAL);
        }
    }

    @Override
    public void onRequestFailure(boolean isLoadMore) {
        CommonUtil.showToast(R.string.toast_getdata_fail, mContext);
        if (isLoadMore){
            mView.updateLoadMoreViewState(InfomationAdapter.ILoadMoreViewState.LMVS_NORMAL);
        }else{
            mView.showLoadView(false);
        }

        if (mContentData.size() == 0){
            mView.showFailView(true);
            return ;
        }
    }

    @Override
    public void onAnylizeFailure(boolean isLoadMore) {
        CommonUtil.showToast(R.string.toast_anylizedata_fail, mContext);
        if (isLoadMore){
            mView.updateLoadMoreViewState(InfomationAdapter.ILoadMoreViewState.LMVS_NORMAL);
        }else{
            mView.showLoadView(false);
        }
        if (mContentData.size() == 0){
            mView.showFailView(true);
            return ;
        }
    }



    private void dealyToRequest(){
            log.i("dealyToRequest");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mView.showLoadView(true);
                }
            }, 200);
            mHandler.sendEmptyMessageDelayed(MSG_REQUEST, 1000);

    }


    private void goContentActivity(){
        Intent intent = new Intent();
        intent.setClass(mContext, DetailActivity.class);
        mContext.startActivity(intent);
    }
}
