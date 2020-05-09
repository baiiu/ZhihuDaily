package com.baiiu.daily;


import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.service.DailyService;
import com.baiiu.daily.serviceImpl.DailyServiceImpl;
import com.google.auto.service.AutoService;

/**
 * auther: baiiu
 * time: 17/10/30 30 22:55
 * description:
 */
@AutoService(ApplicationDelegate.class)
public class DailyApplicationDelegate implements ApplicationDelegate {

    @Override public void onCreate() {
        Router.INSTANCE.addService(DailyService.class.getName(), new DailyServiceImpl());
    }

    @Override public void onStop() {
        Router.INSTANCE.removeService(DailyService.class.getName());
    }

}
