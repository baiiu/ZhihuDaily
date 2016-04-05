package com.baiiu.zhihudaily.net;

import com.baiiu.zhihudaily.net.http.HttpUtil;
import com.baiiu.zhihudaily.net.http.RequestCallBack;

/**
 * author: baiiu
 * date: on 16/4/5 15:08
 * description:
 */
public class DailyClient implements DailyApiConstant {

    public static String getRequestUrl(String partUrl) {
        return BASE_URL + partUrl;
    }

    public static void getDailyNews(String tag, RequestCallBack callback) {
        HttpUtil.get().httpGet(getRequestUrl("news/latest"), callback, tag);
    }
}
