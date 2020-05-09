package com.baiiu.settiing.app;

import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import java.util.ServiceLoader;

/**
 * author: zhuzhe
 * time: 2020-01-09
 * description:
 */
public class SettingApplication extends TinkerApplication {

    public SettingApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.baiiu.common.BaseApplication",
              "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override public void onCreate() {
        super.onCreate();
        ServiceLoader<ApplicationDelegate> loaders = ServiceLoader.load(ApplicationDelegate.class);

        for (ApplicationDelegate loader : loaders) {
            Router.registerComponent(loader.getClass().getName());
        }
    }
}
