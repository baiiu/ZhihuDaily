package com.baiiu.zhihudaily.util.net;

import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;
import com.baiiu.zhihudaily.newsList.model.Daily;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * author: baiiu
 * date: on 16/5/16 14:40
 * description:
 *
 * github的API
 *
 * baiiu is an example user.
 */
public interface DailyAPI {

    @GET("news/latest") Call<Daily> newsLatest();

    @GET("news/before/{date}") Call<Daily> newsBefore(@Path("date") String date);

    @GET("news/{id}") Call<DailyDetail> newsDetail(@Path("id") long id);
}
