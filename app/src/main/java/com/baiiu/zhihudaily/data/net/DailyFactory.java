package com.baiiu.zhihudaily.data.net;


import com.baiiu.common.net.ApiFactory;

/**
 * author: baiiu
 * date: on 16/5/16 17:42
 * description: 知乎API
 */
public class DailyFactory extends ApiFactory {

    private static DailyAPI dailyAPI;

    public static DailyAPI getDailyAPI() {
        return dailyAPI = getAPI(dailyAPI, DailyAPI.class);
    }

}
