package com.baiiu.zhihudaily.data.newsListData.source;

import com.baiiu.zhihudaily.data.newsListData.Daily;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description:
 */
public interface INewsListDataSource {

  void loadNewsList(String date, boolean loadMore, LoadNewsListCallback callback);

  interface LoadNewsListCallback {
    void onSuccess(Daily daily);

    void onFailure();
  }
}
