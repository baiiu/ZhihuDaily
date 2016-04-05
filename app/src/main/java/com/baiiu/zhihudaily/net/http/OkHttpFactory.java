package com.baiiu.zhihudaily.net.http;


import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;


/**
 * Created by zhy on 15/8/17.
 * 生成OkHttpClient
 */
public class OkHttpFactory {

    private static OkHttpFactory mFactory;
    private OkHttpClient mOkHttpClient;

    private static final int TIMEOUT_READ = 25;
    private static final int TIMEOUT_CONNECTION = 25;

    private OkHttpFactory() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setReadTimeout(TIMEOUT_READ, TimeUnit.SECONDS);
        mOkHttpClient.setConnectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);
//        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
    }

    public synchronized static OkHttpFactory get() {
        if (mFactory == null) {
            mFactory = new OkHttpFactory();
        }
        return mFactory;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


}

