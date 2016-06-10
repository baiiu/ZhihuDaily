package com.baiiu.zhihudaily.newsList.model.source;

import com.baiiu.zhihudaily.newsList.model.Daily;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description:
 */
public interface INewsListDataSource {

    Observable<Daily> loadNewsList(String date, boolean refresh);

}
