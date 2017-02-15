package com.baiiu.zhihudaily.data.net.retrofit;

import com.baiiu.zhihudaily.data.net.ApiConstants;
import com.baiiu.zhihudaily.data.util.GsonUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: baiiu
 * date: on 16/5/16 16:34
 * description: 初始化Retrofit
 */
public enum RetrofitClient implements ApiConstants {
    INSTANCE;

    private final Retrofit retrofit;

    RetrofitClient() {
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(OKHttpFactory.INSTANCE.getOkHttpClient())

                //baseUrl
                .baseUrl(GITHUB_BASEURL)

                //gson转化器
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.gson))

                //Rx
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                //创建
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
