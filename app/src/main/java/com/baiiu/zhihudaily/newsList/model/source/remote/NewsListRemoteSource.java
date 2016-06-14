package com.baiiu.zhihudaily.newsList.model.source.remote;

import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.model.TopStory;
import com.baiiu.zhihudaily.newsList.model.source.INewsListDataSource;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.async.MappingConvertUtil;
import com.baiiu.zhihudaily.util.db.DBManager;
import com.baiiu.zhihudaily.util.net.ApiFactory;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import java.util.List;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:51
 * description:
 */
public class NewsListRemoteSource implements INewsListDataSource {

    @RxLogObservable(RxLogObservable.Scope.EVERYTHING) @Override
    public Observable<Daily> loadNewsList(String date, boolean refresh) {
        Observable<Daily> dailyObservable;

        if (refresh) {
            dailyObservable = ApiFactory.getDailyAPI()
                    .newsLatest();
        } else {
            dailyObservable = ApiFactory.getDailyAPI()
                    .newsBefore(date);
        }


        return dailyObservable.filter(daily -> daily != null && !CommonUtil.isEmpty(daily.stories))
                .doOnNext(daily -> saveStories(daily.stories, daily.date));
    }

    private void saveTopStories(final List<TopStory> top_stories) {
        DBManager.instance()
                .saveTopStoryList(top_stories);
    }

    private void saveStories(final List<Story> stories, final String date) {
        DBManager.instance()
                .saveStoryList(MappingConvertUtil.toSavedStory(stories, date));
    }

}
