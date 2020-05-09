package com.baiiu.zhihudaily;

import com.baiiu.common.BaseApplication;
import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.baiiu.library.LogUtil;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import java.util.ServiceLoader;

/**
 * author: baiiu
 * date: on 16/4/5 11:14
 * description:
 */
public class MainApplication extends TinkerApplication {

    public MainApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.baiiu.common.BaseApplication");
    }

    @Override public void onCreate() {
        super.onCreate();
        BaseApplication.isDebug = BuildConfig.DEBUG;

        LogUtil.init(BaseApplication.isDebug);

        ServiceLoader<ApplicationDelegate> loaders = ServiceLoader.load(ApplicationDelegate.class);
        for (ApplicationDelegate loader : loaders) {
            Router.registerComponent(loader.getClass().getName());
        }

    }

}
