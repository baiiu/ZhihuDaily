package com.baiiu.zhihudaily.newsDetail.model;

/**
 * author: baiiu
 * date: on 16/5/12 15:48
 * description:
 */
public interface INewsDetailRepository {

  void loadNewsDetail(long id, LoadNewsDetailCallback callback);

  interface LoadNewsDetailCallback {
    void onSuccess(DailyDetail daily);

    void onFailure();
  }
}
