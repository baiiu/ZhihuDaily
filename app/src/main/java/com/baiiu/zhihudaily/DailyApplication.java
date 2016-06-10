package com.baiiu.zhihudaily;

import android.app.Application;
import android.content.Context;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * author: baiiu
 * date: on 16/4/5 11:14
 * description:
 */
public class DailyApplication extends Application {

    private static Context mContext;
    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        refWatcher = LeakCanary.install(this);

        Logger.init();
    }

    public static Context getContext() {
        return mContext;
    }


    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

}
