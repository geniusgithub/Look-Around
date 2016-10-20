package com.geniusgithub.lookaround.component;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.geniusgithub.common.util.AlwaysLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DownloadImageCacheTask extends AsyncTask<String, Void, File> {

    public static interface onDownloadCallback{
        public void onDownloadComplete(boolean isSuccess, String savePath);
    }

    private final static  String TAG = DownloadImageCacheTask.class.getSimpleName();
    private final Context context;
    private String mUrl;
    private String mSavePath;

    private onDownloadCallback mCallback;

    public DownloadImageCacheTask(Context context, String savePath) {
        this.context = context;
        mSavePath = savePath;
    }

    public void bindDownloadCallback(onDownloadCallback calback){
        mCallback = calback;
    }

    @Override
    protected File doInBackground(String... params) {
        mUrl =  params[0];
        AlwaysLog.i(TAG, "doInBackground mUrl = " + mUrl);
        try {
            return Glide.with(context)
                    .load(mUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception ex) {
            AlwaysLog.e(TAG, "ex.getException = " + ex.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(File result) {
        boolean ret = false;
        if (result != null){
            String path = result.getPath();
            ret = copyFile(path, mSavePath);
            AlwaysLog.i(TAG, "onPostExecute result.getPath = " + path + ", ready to save to:" + mSavePath + ", copyFile = "  + ret);
        }else{
            AlwaysLog.e(TAG, "onPostExecute result = null");
        }

        if (mCallback != null){
            mCallback.onDownloadComplete(ret, mSavePath);
        }
    }

    public  boolean copyFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ( (byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                return true;
            }
        }
        catch (Exception e) {
            AlwaysLog.e(TAG, "copyFile getException = " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}