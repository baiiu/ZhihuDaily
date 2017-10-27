package com.baiiu.common.net;


import com.baiiu.common.net.retrofit.RetrofitClient;

/**
 * author: baiiu
 * date: on 16/5/16 17:42
 * description:
 */
public abstract class ApiFactory {


    // @formatter:off
    protected static <T> T getAPI(T t, Class<T> tClass) {
        return t == null ? RetrofitClient.INSTANCE.getRetrofit().create(tClass) : t;
    }
    // @formatter:on

}
