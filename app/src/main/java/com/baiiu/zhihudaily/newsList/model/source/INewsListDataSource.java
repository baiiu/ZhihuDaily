package com.baiiu.zhihudaily.newsList.model.source;

import com.baiiu.zhihudaily.newsList.model.Daily;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description:
 */
public interface INewsListDataSource {

  void loadNewsList(String date, boolean refresh, LoadNewsListCallback callback);

  interface LoadNewsListCallback {
    void onSuccess(Daily daily);

    void onFailure();
  }
}
