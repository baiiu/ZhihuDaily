package com.baiiu.gank;

import com.baiiu.componentservice.ApplicationDelegate;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.service.GankService;
import com.baiiu.gank.serviceImpl.GankServiceImpl;
import com.baiiu.interfaces.annotation.RouterService;

/**
 * author: baiiu
 * date: on 17/10/31 14:01
 * description:
 */
@RouterService(ApplicationDelegate.class)
public class GankApplicationDelegate implements ApplicationDelegate {

    @Override public void onCreate() {
        Router.INSTANCE.addService(GankService.class.getName(), new GankServiceImpl());
    }

    @Override public void onStop() {
        Router.INSTANCE.removeService(GankService.class.getName());
    }

}
