package com.baiiu.zhihudaily.data.repository;

import android.text.TextUtils;
import com.baiiu.zhihudaily.data.bean.Daily;
import com.baiiu.zhihudaily.data.bean.DailyDetail;
import com.baiiu.zhihudaily.data.bean.Story;
import com.baiiu.zhihudaily.data.net.http.HttpNetUtil;
import com.baiiu.zhihudaily.data.repository.local.NewsLocalSource;
import com.baiiu.zhihudaily.data.repository.remote.NewsRemoteSource;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import com.baiiu.zhihudaily.data.util.PreferenceUtil;
import com.baiiu.zhihudaily.data.util.ReadedListUtil;
import com.baiiu.zhihudaily.di.scope.PerFragment;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description: NewsList数据管理类,控制从哪里取数据,并对数据进行处理.
 */
@PerFragment
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

    @Inject public NewsRepository(NewsLocalSource localSource, NewsRemoteSource remoteSource) {
        mNewsLocalSource = localSource;
        mNewsRemoteSource = remoteSource;
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
                    markRead(daily.stories);
                });
    }


    /**
     * 标记已读
     */
    private void markRead(List<Story> list) {
        Map<String, String> readedMap = ReadedListUtil.getReadedMap(READ_LIST);

        if (!CommonUtil.isEmpty(list)) {
            for (Story story : list) {
                story.isRead = readedMap.get(String.valueOf(story.id)) != null;
            }
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
