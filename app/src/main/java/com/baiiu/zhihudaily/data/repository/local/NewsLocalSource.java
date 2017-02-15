package com.baiiu.zhihudaily.data.repository.local;


import com.baiiu.zhihudaily.data.bean.Daily;
import com.baiiu.zhihudaily.data.bean.DailyDetail;
import com.baiiu.zhihudaily.data.db.DBManager;
import com.baiiu.zhihudaily.data.repository.INewsDataSource;
import com.baiiu.zhihudaily.data.util.DateUtil;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:50
 * description:
 */
public class NewsLocalSource implements INewsDataSource {

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

    @Override public Observable<DailyDetail> loadNewsDetail(long id) {
        return Observable.just(id)
                .flatMap(aLong -> Observable.just(DBManager.instance()
                                                          .getDetailStory(id)));

    }

}
