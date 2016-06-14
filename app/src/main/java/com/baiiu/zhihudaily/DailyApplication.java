package com.baiiu.zhihudaily;

import android.app.Application;
import android.content.Context;
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
