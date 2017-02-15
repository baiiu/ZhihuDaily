package com.baiiu.zhihudaily.data.net;


import com.baiiu.zhihudaily.data.net.retrofit.RetrofitClient;

/**
 * author: baiiu
 * date: on 16/5/16 17:42
 * description:
 */
public enum ApiFactory {
    INSTANCE;

    private static DailyAPI dailyAPI;

    public static DailyAPI getDailyAPI() {
        if (dailyAPI == null) {
            dailyAPI = RetrofitClient.INSTANCE.getRetrofit().create(DailyAPI.class);
        }

        return dailyAPI;
    }
}
