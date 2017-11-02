package com.baiiu.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import com.baiiu.common.tinker.TinkerManager;
import com.baiiu.lib_common.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

/**
 * author: baiiu
 * date: on 17/10/27 15:14
 * description:
 */
public class BaseApplication extends DefaultApplicationLike {

    private static BaseApplication baseApplication;
    private static Context mContext;

    private RefWatcher refWatcher;

    public BaseApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
            long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime,
              tinkerResultIntent);
    }


    @Override public void onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
                                               .penaltyLog()
                                               //.detectDiskReads()
                                               //.detectDiskWrites()
                                               //.detectNetwork()
                                               //.detectCustomSlowCalls()
                                               .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                                           .penaltyLog()
                                           //.detectLeakedSqlLiteObjects()
                                           //.detectLeakedClosableObjects()
                                           //.detectActivityLeaks()
                                           //.penaltyDeath() //Crashes the whole process on violation，一旦发现问题崩溃进程
                                           .build());
        }

        super.onCreate();
        mContext = getApplication();
        refWatcher = LeakCanary.install(getApplication());

        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        //TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker.with(getApplication());

    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public static Context getContext() {
        return mContext;
    }


    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

}
