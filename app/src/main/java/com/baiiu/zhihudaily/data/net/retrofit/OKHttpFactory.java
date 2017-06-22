package com.baiiu.zhihudaily.data.net.retrofit;

import com.baiiu.library.okHttpLog.HttpLoggingInterceptorM;
import com.baiiu.library.okHttpLog.LogInterceptor;
import com.baiiu.zhihudaily.BuildConfig;
import com.baiiu.zhihudaily.data.net.retrofit.interceptor.UserAgentInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * author: baiiu
 * date: on 16/5/16 16:43
 * description:
 */
enum OKHttpFactory {

    INSTANCE;

    private final OkHttpClient okHttpClient;

    private static final int TIMEOUT_READ = 25;
    private static final int TIMEOUT_CONNECTION = 25;

    OKHttpFactory() {
        HttpLoggingInterceptorM interceptor = new HttpLoggingInterceptorM(LogInterceptor.INSTANCE);
        interceptor.setLevel(HttpLoggingInterceptorM.Level.BODY);

        //Cache cache = new Cache(UIUtil.getContext().getCacheDir(), 10 * 1024 * 1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()

                //添加UA
                .addInterceptor(new UserAgentInterceptor(HttpHelper.getUserAgent()))

                //必须是设置Cache目录
                //.cache(cache)

                //走缓存
                //.addInterceptor(new OnOffLineCachedInterceptor())
                //.addNetworkInterceptor(new OnOffLineCachedInterceptor())

                //失败重连
                .retryOnConnectionFailure(true)

                //time out
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(new HttpLoggingInterceptorM(LogInterceptor.INSTANCE).setLevel(
                            HttpLoggingInterceptorM.Level.BODY));
        }

        okHttpClient = builder.build();

    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
