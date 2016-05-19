package com.baiiu.zhihudaily.util.net;

import com.baiiu.zhihudaily.util.net.retrofit.RetrofitClient;

/**
 * author: baiiu
 * date: on 16/5/16 17:42
 * description:
 */
public enum ApiFactory {
  INSTANCE;

  private final DailyAPI dailyAPI;

  ApiFactory() {
    dailyAPI = RetrofitClient.INSTANCE.getRetrofit().create(DailyAPI.class);
  }

  public DailyAPI getDailyAPI() {
    return dailyAPI;
  }
}
