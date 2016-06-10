package com.baiiu.zhihudaily.newsDetail.model;

import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/12 15:48
 * description:
 */
public interface INewsDetailRepository {

    Observable<DailyDetail> loadNewsDetail(long id);

}
