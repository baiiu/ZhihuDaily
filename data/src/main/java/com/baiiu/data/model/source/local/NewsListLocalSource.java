package com.baiiu.data.model.source.local;


import com.baiiu.data.db.DBManager;
import com.baiiu.data.model.Daily;
import com.baiiu.data.model.source.INewsListDataSource;
import com.baiiu.data.util.DateUtil;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:50
 * description:
 */
public class NewsListLocalSource implements INewsListDataSource {

    @Override public Observable<Daily> loadNewsList(String date, boolean refresh) {
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
