package com.baiiu.zhihudaily.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * author: baiiu
 * date: on 16/4/5 11:14
 * description:
 */
public class DailyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        LeakCanary.install(this);
    }


    public static Context getContext() {
        return mContext;
    }

}
