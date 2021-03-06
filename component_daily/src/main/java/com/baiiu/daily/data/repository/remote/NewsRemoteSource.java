package com.baiiu.daily.data.repository.remote;


import com.baiiu.common.util.CommonUtil;
import com.baiiu.daily.data.bean.Daily;
import com.baiiu.daily.data.bean.DailyDetail;
import com.baiiu.daily.data.bean.Story;
import com.baiiu.daily.data.bean.mapper.MappingConvertUtil;
import com.baiiu.daily.data.db.DBManager;
import com.baiiu.daily.data.net.DailyFactory;
import com.baiiu.daily.data.repository.INewsDataSource;
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
            dailyObservable = DailyFactory.getDailyAPI()
                    .newsLatest();
        } else {
            dailyObservable = DailyFactory.getDailyAPI()
                    .newsBefore(date);
        }


        return dailyObservable.filter(daily -> daily != null && !CommonUtil.isEmpty(daily.stories))
                .doOnNext(daily -> saveStories(daily.stories, daily.date));
    }

    //private void saveTopStories(final List<TopStory> top_stories) {
    //    DBManager.instance()
    //            .saveTopStoryList(top_stories);
    //}

    private void saveStories(final List<Story> stories, final String date) {
        DBManager.instance()
                .saveStoryList(MappingConvertUtil.toSavedStory(stories, date));
    }

    @Override public Observable<DailyDetail> loadNewsDetail(long id) {
        return DailyFactory.getDailyAPI()
                .newsDetail(id)
                .doOnNext(this::saveData);
    }

    private void saveData(DailyDetail response) {
        DBManager.instance()
                .saveDetailStory(response);
    }

}
