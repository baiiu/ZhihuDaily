package com.baiiu.gank;

import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.service.GankService;
import com.baiiu.gank.serviceImpl.GankServiceImpl;
import com.google.auto.service.AutoService;

/**
 * author: baiiu
 * date: on 17/10/31 14:01
 * description:
 */
@AutoService(ApplicationDelegate.class)
public class GankApplicationDelegate implements ApplicationDelegate {

    @Override public void onCreate() {
        Router.INSTANCE.addService(GankService.class.getName(), new GankServiceImpl());
    }

    @Override public void onStop() {
        Router.INSTANCE.removeService(GankService.class.getName());
    }

}
