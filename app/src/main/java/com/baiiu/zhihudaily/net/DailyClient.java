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

    public static void getLatestNews(String tag, RequestCallBack callback) {
        HttpUtil.get().httpGet(getRequestUrl("news/latest"), callback, tag);
    }


    public static void getBeforeNews(String tag, String date, RequestCallBack callBack) {
        HttpUtil.get().httpGet(getRequestUrl("news/before/" + date), callBack, tag);
    }
}
