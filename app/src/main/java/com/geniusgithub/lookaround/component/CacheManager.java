package com.geniusgithub.lookaround.component;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.geniusgithub.common.util.AlwaysLog;

public class CacheManager {
    private final static String TAG = CacheManager.class.getSimpleName();
    private static CacheManager mInstance;

    private Context mContext;

    private static final String ORIGIN_CACHE = "origin_cache";


    public static CacheManager getInstance(){
        return mInstance;
    }

    public static CacheManager newInstance(Context context){
        if (mInstance == null){
            mInstance = new CacheManager(context);
        }

        return mInstance;
    }

    private CacheManager(Context context){
        mContext = context;
    }


    public void initConfigure(GlideBuilder build){
        AlwaysLog.i(TAG, "initConfigure");
        build.setDiskCache(new ExternalCacheDiskCacheFactory(mContext, ORIGIN_CACHE, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
    }

    private static boolean isclearThumnailCacheStart = false;
    public void clearCache(){
        clearMemoryCache();

        clearDiskCache();
    }

    public void clearDiskCache(){
        Thread mThead = new Thread(new Runnable() {
            @Override
            public void run() {
                AlwaysLog.i(TAG, "clearDiskCache thread start...");
                if (isclearThumnailCacheStart){
                    AlwaysLog.i(TAG, "clearDiskCache  start = true, so return now");
                    return ;
                }
                synchronized (CacheManager.this){
                    isclearThumnailCacheStart = true;
                    long time1 = System.currentTimeMillis();
                    Glide.get(mContext).clearDiskCache();
                    long time2 = System.currentTimeMillis();
                    AlwaysLog.i(TAG, "clearDiskCache cost time:" + (time2 - time1));
                    isclearThumnailCacheStart = false;
                }
            }
        });
        mThead.start();
    }

    public void clearMemoryCache(){
        long time1 = System.currentTimeMillis();
        Glide.get(mContext).clearMemory();
        long time2 = System.currentTimeMillis();
        AlwaysLog.i(TAG, "clearMemoryCache cost time:" + (time2 - time1));
    }

}
