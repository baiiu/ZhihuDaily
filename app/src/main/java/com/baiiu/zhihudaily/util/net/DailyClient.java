package com.baiiu.zhihudaily.util.net;

import com.baiiu.zhihudaily.util.net.http.HttpUtil;
import com.baiiu.zhihudaily.util.net.http.RequestCallBack;

/**
 * author: baiiu
 * date: on 16/4/5 15:08
 * description:
 */
public class DailyClient implements DailyApiConstant {

  public static String getRequestUrl(String partUrl) {
    return BASE_URL + partUrl;
  }

  public static void getLatestNews(RequestCallBack callback) {
    HttpUtil.get().httpGet(getRequestUrl("news/latest"), callback, "newsListFragment");
  }

  public static void getBeforeNews(String date, RequestCallBack callBack) {
    HttpUtil.get().httpGet(getRequestUrl("news/before/" + date), callBack, "newsListFragment");
  }

  public static void getNewsDetail(long id, RequestCallBack callBack) {
    HttpUtil.get().httpGet(getRequestUrl("news/" + id), callBack, "newsDetail");
  }
}
