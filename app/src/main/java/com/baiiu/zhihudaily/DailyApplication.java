package com.baiiu.zhihudaily;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
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
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                               .detectAll()
                                               .penaltyLog()
                                               //.detectDiskReads()
                                               //.detectDiskWrites()
                                               //.detectNetwork()
                                               //.detectCustomSlowCalls()
                                               .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                           .detectAll()
                                           .penaltyLog()
                                           //.detectLeakedSqlLiteObjects()
                                           //.detectLeakedClosableObjects()
                                           //.detectActivityLeaks()
                                           //.penaltyDeath() //Crashes the whole process on violation，一旦发现问题崩溃进程
                                           .build());
        }

        super.onCreate();
        mContext = getApplicationContext();

        refWatcher = LeakCanary.install(this);

    }

    public static Context getContext() {
        return mContext;
    }


    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

}
