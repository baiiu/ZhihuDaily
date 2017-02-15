package com.baiiu.zhihudaily.data.repository;

import com.baiiu.zhihudaily.data.bean.Daily;
import com.baiiu.zhihudaily.data.bean.DailyDetail;
import rx.Observable;

/**
 * author: baiiu
 * date: on 17/2/15 16:22
 * description:
 */

public interface INewsDataSource {

    Observable<Daily> loadNewsList(String date, boolean refresh);

    Observable<DailyDetail> loadNewsDetail(long id);

}
