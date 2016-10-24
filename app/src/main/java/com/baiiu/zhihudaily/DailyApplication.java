package com.baiiu.zhihudaily;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import com.baiiu.zhihudaily.di.component.AppComponent;
import com.baiiu.zhihudaily.di.component.DaggerAppComponent;
import com.baiiu.zhihudaily.di.module.AppModule;
import com.baiiu.zhihudaily.di.module.RepositoryModule;
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
    private AppComponent appComponent;

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

        Logger.init();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repositoryModule(new RepositoryModule())
                .build();



    }

    public static Context getContext() {
        return mContext;
    }


    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
