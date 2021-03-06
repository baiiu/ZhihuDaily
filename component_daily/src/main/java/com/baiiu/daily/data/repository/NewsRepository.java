package com.baiiu.daily.data.repository;


import android.text.TextUtils;
import com.baiiu.common.net.http.HttpNetUtil;
import com.baiiu.common.util.CommonUtil;
import com.baiiu.common.util.PreferenceUtil;
import com.baiiu.common.util.ReadedListUtil;
import com.baiiu.daily.data.bean.Daily;
import com.baiiu.daily.data.bean.DailyDetail;
import com.baiiu.daily.data.bean.Story;
import com.baiiu.daily.data.repository.local.NewsLocalSource;
import com.baiiu.daily.data.repository.remote.NewsRemoteSource;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description: NewsList数据管理类,控制从哪里取数据,并对数据进行处理.
 */
public class NewsRepository implements INewsDataSource {
    public static final String LATEST_DATE = "Latest_Date";
    public static final String READ_LIST = "read_list";

    private NewsLocalSource mNewsLocalSource;
    private NewsRemoteSource mNewsRemoteSource;

    /**
     * 从网络加载数据
     */
    private boolean mFromRemote = true;

    private String mCurrentDate;

    public NewsRepository() {
        mNewsLocalSource = new NewsLocalSource();
        mNewsRemoteSource = new NewsRemoteSource();
    }

    @Override public Observable<Daily> loadNewsList(String date, boolean refresh) {
        if (refresh) {
            //下拉刷新时,reset
            mCurrentDate = null;
        }

        if (TextUtils.isEmpty(mCurrentDate)) {
            mCurrentDate = PreferenceUtil.instance()
                    .get(LATEST_DATE, "");
        }

        Observable<Daily> observable;

        if (mFromRemote) {
            observable = mNewsRemoteSource.loadNewsList(mCurrentDate, refresh)
                    .doOnNext(daily -> {
                        if (refresh) {
                            PreferenceUtil.instance()
                                    .put(LATEST_DATE, daily.date)
                                    .commit();
                        }
                    });

        } else {
            observable = mNewsLocalSource.loadNewsList(mCurrentDate, refresh);
        }


        return observable
                //return Observable.concat(mLocalDaily, mRemoteDaily)
                //        .first(daily -> Daily.isAvailable())
                .filter(daily -> daily != null)
                .doOnNext(daily -> {
                    mCurrentDate = daily.date;
                    markRead(daily.date, daily.stories);
                });
    }


    /**
     * 初始化
     */
    private void markRead(String date, List<Story> list) {
        Map<String, String> readedMap = ReadedListUtil.getReadedMap(READ_LIST);

        if (CommonUtil.isEmpty(list)) {
            return;
        }

        for (Story story : list) {
            story.date = date;
            story.isRead = readedMap.get(String.valueOf(story.id)) != null;
        }
    }

    /**
     * 控制是否从远端拉去数据
     */
    public void refreshNewsList(boolean fromRemote) {
        this.mFromRemote = fromRemote && HttpNetUtil.isConnected();
        Daily.setAvailable(!mFromRemote);
    }

    @Override public Observable<DailyDetail> loadNewsDetail(long id) {
        if (HttpNetUtil.isConnected()) {
            return mNewsRemoteSource.loadNewsDetail(id);
        } else {
            return mNewsLocalSource.loadNewsDetail(id);
        }
    }

}
