package com.baiiu.settiing.app;

import com.baiiu.componentservice.Router;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

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

        Router.registerComponent("com.baiiu.daily.DailyApplicationDelegate");
        Router.registerComponent("com.baiiu.gank.GankApplicationDelegate");
    }

}
