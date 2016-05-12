package com.baiiu.zhihudaily.util.net.util;

import com.android.volley.toolbox.HurlStack;
import com.baiiu.zhihudaily.util.net.http.OkHttpFactory;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by baiiu on 15/11/15.
 * 使用OkHttp
 */
public class OkHttpStack extends HurlStack {
    private final OkUrlFactory okUrlFactory;

    public OkHttpStack() {
        this(OkHttpFactory.get().getOkHttpClient());
    }

    public OkHttpStack(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.okUrlFactory = new OkUrlFactory(okHttpClient);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okUrlFactory.open(url);
    }

}
