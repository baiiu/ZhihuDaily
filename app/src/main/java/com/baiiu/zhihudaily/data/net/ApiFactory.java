package com.baiiu.zhihudaily.data.net;


import com.baiiu.zhihudaily.data.net.retrofit.RetrofitClient;
import com.baiiu.zhihudaily.data.repository.api.NewsAPI;

/**
 * author: baiiu
 * date: on 16/5/16 17:42
 * description:
 */
public enum ApiFactory {
    ;

    private static NewsAPI newsAPI;

    // @formatter:off
    public static <T> T getAPI(T t, Class<T> tClass) {
        return t == null ? RetrofitClient.INSTANCE.getRetrofit().create(tClass) : t;
    }
    // @formatter:on

    public static NewsAPI getNewsAPI() {
        return newsAPI = getAPI(newsAPI, NewsAPI.class);
    }

}
