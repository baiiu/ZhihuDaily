package com.baiiu.zhihudaily;

import com.baiiu.annotations.Module;
import com.baiiu.library.LogUtil;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * author: baiiu
 * date: on 16/4/5 11:14
 * description:
 */
@Module(

        application = {
                "com.baiiu.daily.DailyApplicationDelegate",
                "com.baiiu.gank.GankApplicationDelegate",
                "com.baiiu.setting.SettingApplicationDelegate"
        },

        dependencies = {
                ":component_daily",
                ":component_gank",
                ":component_setting"
        }
)
public class MainApplication extends TinkerApplication {

    public MainApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.baiiu.common.BaseApplication",
              "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override public void onCreate() {
        super.onCreate();
        LogUtil.init(BuildConfig.DEBUG);
    }

}
