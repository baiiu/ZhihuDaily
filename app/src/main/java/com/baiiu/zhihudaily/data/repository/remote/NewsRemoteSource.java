package com.baiiu.zhihudaily.data.repository.remote;


import com.baiiu.zhihudaily.data.bean.Daily;
import com.baiiu.zhihudaily.data.bean.DailyDetail;
import com.baiiu.zhihudaily.data.bean.Story;
import com.baiiu.zhihudaily.data.bean.TopStory;
import com.baiiu.zhihudaily.data.bean.mapper.MappingConvertUtil;
import com.baiiu.zhihudaily.data.db.DBManager;
import com.baiiu.zhihudaily.data.net.ApiFactory;
import com.baiiu.zhihudaily.data.repository.INewsDataSource;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import java.util.List;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:51
 * description:
 */
public class NewsRemoteSource implements INewsDataSource {

    // 可以在Dagger2中这样注入,再添加一个@Provider方法即可
    //public NewsListRemoteSource(DailyApplication dailyAPI) {
    //    this.mDailyAPI = dailyAPI;
    //}

    @Override public Observable<Daily> loadNewsList(String date, boolean refresh) {
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
                .saveList(DBManager.instance()
                                  .getSavedStoryDao(), MappingConvertUtil.toSavedStory(stories, date));
        DBManager.instance()
                .saveStoryList(MappingConvertUtil.toSavedStory(stories, date));
    }

    @Override public Observable<DailyDetail> loadNewsDetail(long id) {
        return ApiFactory.getDailyAPI()
                .newsDetail(id)
                .doOnNext(this::saveData);
    }

    private void saveData(DailyDetail response) {
        DBManager.instance()
                .saveDetailStory(response);
    }

}
