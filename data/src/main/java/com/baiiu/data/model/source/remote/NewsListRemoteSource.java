package com.baiiu.data.model.source.remote;

import com.baiiu.data.db.DBManager;
import com.baiiu.data.db.MappingConvertUtil;
import com.baiiu.data.model.Daily;
import com.baiiu.data.model.Story;
import com.baiiu.data.model.TopStory;
import com.baiiu.data.model.source.INewsListDataSource;
import com.baiiu.data.net.ApiFactory;
import com.baiiu.data.util.CommonUtil;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import java.util.List;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:51
 * description:
 */
public class NewsListRemoteSource implements INewsListDataSource {

    // 可以在Dagger2中这样注入,再添加一个@Provider方法即可
    //public NewsListRemoteSource(DailyApplication dailyAPI) {
    //    this.mDailyAPI = dailyAPI;
    //}

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
