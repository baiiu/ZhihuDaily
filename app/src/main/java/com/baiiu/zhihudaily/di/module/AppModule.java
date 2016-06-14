package com.baiiu.zhihudaily.di.module;

import com.baiiu.zhihudaily.DailyApplication;
import com.baiiu.zhihudaily.di.scope.PerApp;
import dagger.Module;
import dagger.Provides;

/**
 * author: baiiu
 * date: on 16/6/14 15:13
 * description:
 */
@Module
public class AppModule {

    private DailyApplication mApplication;

    public AppModule(DailyApplication application) {
        this.mApplication = application;
    }


    @Provides @PerApp public DailyApplication provideApplication() {
        return mApplication;
    }



}
