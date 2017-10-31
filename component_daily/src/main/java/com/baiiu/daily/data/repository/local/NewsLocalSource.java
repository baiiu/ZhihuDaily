package com.baiiu.daily.data.repository.local;


import com.baiiu.common.util.DateUtil;
import com.baiiu.daily.data.bean.Daily;
import com.baiiu.daily.data.bean.DailyDetail;
import com.baiiu.daily.data.db.DBManager;
import com.baiiu.daily.data.repository.INewsDataSource;
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
                    //daily.top_stories = DBManager.instance().getTopStoryList();
                    daily.date = s;
                    daily.stories = DBManager.instance()
                            .getStoryList(s);

                    return Observable.just(daily);
                });
    }

    @Override public Observable<DailyDetail> loadNewsDetail(long id) {
        return Observable.just(id)
                .flatMap(aLong -> Observable.just(DBManager.instance()
                                                          .getDetailStory(id))
                         //.delay(1, TimeUnit.SECONDS)
                )
                .flatMap(dailyDetail -> {
                    if (dailyDetail == null) {
                        return Observable.error(new NullPointerException("存储为空"));
                    }

                    return Observable.just(dailyDetail);
                });

    }

}
