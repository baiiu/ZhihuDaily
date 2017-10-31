package com.baiiu.setting;

import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.service.SettingService;
import com.baiiu.setting.serviceImpl.SettingServiceImpl;

/**
 * author: baiiu
 * date: on 17/10/31 15:45
 * description:
 */
public class SettingApplicationDelegate implements ApplicationDelegate {
    @Override public void onCreate() {
        Router.INSTANCE.addService(SettingService.class.getName(), new SettingServiceImpl());
    }

    @Override public void onStop() {
        Router.INSTANCE.removeService(SettingService.class.getName());
    }
}
