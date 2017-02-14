package com.baiiu.zhihudaily.newsList.model.source.local;

import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.source.INewsListDataSource;
import com.baiiu.zhihudaily.util.DateUtil;
import com.baiiu.zhihudaily.util.db.DBManager;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:50
 * description:
 */
public class NewsListLocalSource implements INewsListDataSource {

    @RxLogObservable @Override public Observable<Daily> loadNewsList(String date, boolean refresh) {
        return Observable.just(date)
                .map(s -> {
                    if (!refresh) {
                        s = DateUtil.getYesterdayDate(s);
                    }
                    return s;
                })
                .flatMap(s -> {
                    Daily daily = new Daily();
                    daily.top_stories = DBManager.instance()
                            .getTopStoryList();
                    daily.date = s;

                    daily.stories = DBManager.instance()
                            .getStoryList(s);

                    return Observable.just(daily);
                });

    }
}
