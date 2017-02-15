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

    // @formatter:off
    public static <T> T getAPI(T t, Class<T> tClass) {
        return t == null ? RetrofitClient.INSTANCE.getRetrofit().create(tClass) : t;
    }
    // @formatter:on

    public static DailyAPI getDailyAPI() {
        return dailyAPI = getAPI(dailyAPI, DailyAPI.class);
    }

}
