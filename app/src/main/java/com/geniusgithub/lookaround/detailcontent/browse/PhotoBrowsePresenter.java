package com.geniusgithub.lookaround.detailcontent.browse;

import android.content.Context;
import android.content.Intent;

import com.geniusgithub.common.util.FileHelper;
import com.geniusgithub.lookaround.LAroundApplication;
import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.component.DownloadImageCacheTask;
import com.geniusgithub.lookaround.component.FileManager;
import com.geniusgithub.lookaround.detailcontent.DetailCache;
import com.geniusgithub.lookaround.model.BaseType;
import com.geniusgithub.lookaround.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class PhotoBrowsePresenter implements PhotoBrowseContact.IPresenter, DownloadImageCacheTask.onDownloadCallback{

    public static final String TAG = PhotoBrowsePresenter.class.getSimpleName();

    private Context mContext;
    private PhotoBrowseContact.IView mView;

    private BaseType.InfoItem mItem = new BaseType.InfoItem();
    private List<String> mCurItemsList;

    public PhotoBrowsePresenter(Context contect){
        mContext = contect;
    }


    ///////////////////////////////////////     presenter callback begin
    @Override
    public void bindView(PhotoBrowseContact.IView view) {
        mView = view;
        mView.bindPresenter(this);
    }

    @Override
    public void unBindView() {

    }
    ///////////////////////////////////////     presenter callback end


    private static final String[] URLS = {
            "http://ww3.sinaimg.cn/thumbnail/6cc0b934jw1e0bnwu709kj.jpg",
            "http://ww2.sinaimg.cn/thumbnail/519e330bjw1e0bw1icjokj.jpg",
            "http://ww3.sinaimg.cn/thumbnail/a163c684jw1e0c0gbftdpj.jpg",
            "http://ww4.sinaimg.cn/thumbnail/a163c684jw1e0c0g9tgmhj.jpg",
            "http://ww3.sinaimg.cn/thumbnail/a163c684jw1e0c0g9fxcqj.jpg",
            "http://ww4.sinaimg.cn/thumbnail/a81bce36jw1e0brpwol6yj.jpg",
            "http://ww4.sinaimg.cn/thumbnail/a81bce36jw1e0brqequ1oj.jpg",
            "http://ww1.sinaimg.cn/thumbnail/a81bce36jw1e0brpwnn0bj.jpg",
            "http://ww2.sinaimg.cn/thumbnail/721f7167jw1e0aeqrqtlfj.jpg",
            "http://ww1.sinaimg.cn/thumbnail/60dd4473jw1e0aj52otyaj.jpg"};

    private void unitTest(){
        mCurItemsList = new ArrayList<String>();
        for (String value:URLS) {
            mCurItemsList.add(value);
        }
    }

    ///////////////////////////////////////     lifecycle or ui operator begin
    public void onUiCreate(Context context, Intent intent) {
        mContext = context;

        DetailCache mDetailCache = DetailCache.getInstance();
        mItem = mDetailCache.getInfoItem();
        mCurItemsList = mItem.mImageUrlList;

      //  unitTest();

        mView.initBrowseData(mCurItemsList, 0);

    }



    public void onUiDestroy() {

    }
    ///////////////////////////////////////     lifecycle or ui operator end
    @Override
    public void onDownloadComplete(boolean isSuccess, String savePath) {
        String text = "";
        if (isSuccess){
            text  = mContext.getResources().getString(R.string.toast_save_success) + "," +
                    mContext.getResources().getString(R.string.toast_savefile_end) + savePath;
        }else{
            text = mContext.getResources().getString(R.string.toast_save_fail2);
        }

       CommonUtil.showToast(text, mContext);
    }

    public void startDownLoad(String url) {
        LAroundApplication.getInstance().onEvent("SAVE01");

        if (!FileHelper.createDirectory(FileManager.getDownloadFileSaveDir())){
            CommonUtil.showToast(R.string.toast_save_fail2, mContext);
            return ;
        }

        String savePath  = FileManager.getDownloadFileSavePath(url);
        DownloadImageCacheTask task = new DownloadImageCacheTask(mContext, savePath);
        task.bindDownloadCallback(this);
        task.execute(url);


    }


}
