package com.baiiu.setting;

import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.UIRouter;
import com.baiiu.componentservice.service.SettingService;
import com.baiiu.interfaces.annotation.RouterService;

/**
 * author: baiiu
 * date: on 17/10/31 15:45
 * description:
 */
@RouterService(ApplicationDelegate.class)
public class SettingApplicationDelegate implements ApplicationDelegate {
    @Override public void onCreate() {
        //Router.INSTANCE.addService(SettingService.class.getName(), new SettingServiceImpl());
        UIRouter.put(SettingService.URL_TO_SETTING, SettingActivity.class);
    }

    @Override public void onStop() {
        Router.INSTANCE.removeService(SettingService.class.getName());
    }
}
